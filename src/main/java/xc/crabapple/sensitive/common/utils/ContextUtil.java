package xc.crabapple.sensitive.common.utils;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dao.UserDao;
import xc.crabapple.sensitive.common.domain.dco.ContextDco;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import xc.crabapple.core.collection.StringUtil;
import xc.crabapple.core.cookie.CookieBean;
import xc.crabapple.core.cookie.CookieUtil;
import xc.crabapple.core.log.LogUtil;
import xc.crabapple.core.net.ip.IPUtil;
import xc.crabapple.core.random.RandomUtil;
import xc.crabapple.core.servlet.ServletUtil;
import xc.crabapple.core.thread.ThreadUtil;
import xc.crabapple.core.timer.TimerUtil;
import xc.crabapple.ssm.cache.CacheUtil;

/**
 * update
 * 描述：程序的应用上下文
 * INS_MSG_DEF表中有该类的包路径记录，故更改包名时需同步更新该表字段
 */
public class ContextUtil {

    // 登录超时配置
    // 登录超时配置更新
    public static Long loginTimeLimited = 21600L;  // 登录时间限制，单位秒。 默认6小时
    public static Long lastReqTimeLimited = 21600L;  // 未操作时间限制，单位秒。 默认6小时

    // ContextSession里面的东西
    private static Map<String, Object> p_sessionMap = null;
    // 虚拟用户
    private static Map<String, UserDao> p_virtualUserMap = null;
    // 存储userId到user对象的映射
    private static Map<Long, UserDao> p_userMap = null;
    private static Map<String, String>loginTimeMap = null;
    private static Map<String, String> lastRequsetMap =  new ConcurrentHashMap<>(512);;

    /**
     * 初始化虚拟用户、用于第三方系统接口对接
     */
    public static final void init() {
        loginTimeLimited = MM.propertiesService.getLong("loginTimeLimited", loginTimeLimited);
        lastReqTimeLimited = MM.propertiesService.getLong("lastReqTimeLimited", lastReqTimeLimited);
        p_sessionMap = new ConcurrentHashMap<>(1024);
        p_userMap = new ConcurrentHashMap<>(512);
    }

    /**
     * 会话超时配置
     */
    public static final S2cDto tokenMonitor() {
//        loginTimeLimited = MM.propertiesService.getLong("loginTimeLimited", loginTimeLimited);
        lastReqTimeLimited = MM.propertiesService.getLong("lastReqTimeLimited", lastReqTimeLimited);
        String token = ContextUtil.getToken();
        // 最后请求时间
        if (lastRequsetMap==null) {
            lastRequsetMap.put(token, TimerUtil.time2str());
            return S2cDto.okay();
        }
        String lastRequestTime = lastRequsetMap.get(token);
        if (StringUtil.isBlank(lastRequestTime)) {
            lastRequsetMap.put(token, TimerUtil.time2str());
            return S2cDto.okay();
        }
        long lastReqTime = TimerUtil.str2time(lastRequestTime).getTime();
        long currentTimeMillis = System.currentTimeMillis();  // 当前时间，毫秒时间戳
        if ((currentTimeMillis - lastReqTime) > (lastReqTimeLimited * 1000)) {
            LogUtil.warn("ContextUtil_tokenMonitor_2", "当期用户已过期=" + ContextUtil.getCurrentUserId(), "最近请求时间=" + lastReqTime, "当前时间="+currentTimeMillis);
            removeCurrentUser(token);
            lastRequsetMap.remove(token);
            return S2cDto.failure("登录超时");
        }
        lastRequsetMap.put(token, TimerUtil.time2str());
        return S2cDto.okay();
    }

    /**
     * 获取token
     */
    public static final String getToken() {
        //
        String threadToken = getThreadToken();
        if (!StringUtil.isBlank(threadToken)) {
            return threadToken;
        }
        //
        String cookieToken = getCookieToken();
        if (!StringUtil.isBlank(cookieToken)) {
            return cookieToken;
        }
        //
        String paramToken = getParamToken();
        if (!StringUtil.isBlank(paramToken)) {
            return paramToken;
        }
        return "";
    }

    /**
     * 获取系统子线程中的token
     */
    public static final String getThreadToken() {
        Object object = ThreadUtil.getData("token");
        if (object != null) {
            String threadToken = object.toString();
            return threadToken;
        }
        return "";
    }

    /**
     * 通过客户端的cookie(httponly)获取token
     */
    public static final String getCookieToken() {
        final String tokenPreKey = CacheUtil.getStandardKey("token");
        String cookieToken = CookieUtil.get(tokenPreKey);
        if (!StringUtil.isBlank(cookieToken)) {
            return cookieToken;
        }
        return "";
    }

    /**
     * 通过客户端传参获取token
     *
     * @return
     */
    public static final String getParamToken() {
        //
        String cipherParamToken = ServletUtil.getString("token");
        if (!StringUtil.isBlank(cipherParamToken)) {
            return cipherParamToken;
        }
        final String tokenPreKey = CacheUtil.getStandardKey("token");
        cipherParamToken = ServletUtil.getString(tokenPreKey);
        if (!StringUtil.isBlank(cipherParamToken)) {
            return cipherParamToken;
        }
        return "";
    }

    public static final String setCurrentUser(final UserDao userDao) {
        final String clientId = ServletUtil.getString("clientId");
        final String clientIp =  IPUtil.getClientIp();
        final String userAgent = ServletUtil.getUserAgent();
        //
        String token = setCurrentUser(userDao, clientId, clientIp, userAgent, true);
        return token;
    }

    /**
     * @param osUser
     * @return
     */
    public static final void updateCurrentUser(final UserDao osUser) {
        Long userId = osUser.getId();
        p_userMap.put(userId, osUser);
    }

    /**
     * 生成token并绑定用户
     *
     * @param clientId
     * @param clientIp
     * @param userAgent
     * @param isAddCookie 是否添加cookie用于启动项目初始化时可以生成一个固定token以及绑定用户
     * @return
     */
    public static final String setCurrentUser(final UserDao userDao, final String clientId, final String clientIp, final String userAgent, boolean isAddCookie) {
        //
        ContextDco contextDco = new ContextDco();
        contextDco.setClientId(clientId);
        contextDco.setUserId(userDao.getId());
        contextDco.setUserNo(userDao.getUserNo());
        contextDco.setNikeName(userDao.getNikeName());
        contextDco.setClientIp(clientIp);
        contextDco.setUserAgent(userAgent);
        String dateStr = TimerUtil.time2str(new Date());
        contextDco.setLoginTime(dateStr);
        contextDco.setLastReqTime(dateStr);
        final String token = RandomUtil.getStrWithStamp(128, false);
        p_setCurrentUser(token, contextDco, userDao, isAddCookie);
        return token;
    }

    public static final String setCurrentUser(final String token, final UserDao user) {
        p_setCurrentUser(token, null, user, true);
        return token;
    }

    /**
     * 新增参数是否添加cookie
     *
     * @param token       128位token
     * @param contextDco
     * @param isAddCookie 是否添加cookie用于启动项目初始化时可以生成一个固定token以及绑定用户
     */
    private static final void p_setCurrentUser(final String token, final ContextDco contextDco, final UserDao userDao, boolean isAddCookie) {
        if (StringUtil.isBlank(token)) {
            return;
        }
        if (contextDco == null) {
            return;
        }
        if (userDao == null) {
            return;
        }
        // 建立 OsUser与ContextDco之间的关联关系
        final Long userId = contextDco.getUserId();
        p_userMap.put(userId, userDao);
        final String tokenKey = "token_" + token;
        CacheUtil.set(tokenKey, contextDco, loginTimeLimited);
        //
        if (isAddCookie) {
            // 存入浏览器cookie
            final String tokenPreKey = CacheUtil.getStandardKey("token");
            CookieBean cookieBean = new CookieBean();
            cookieBean.setName(tokenPreKey);
            cookieBean.setValue(token);
            cookieBean.setHttpOnly(true);
            cookieBean.setMaxAge(loginTimeLimited);
            CookieUtil.add(cookieBean);
            //
            ServletUtil.setAttribute("cookieToken", token);
        }
    }

    public static final void setContextDco(final String token, final ContextDco contextDco) {
        if (StringUtil.isBlank(token) || contextDco == null) {
            return;
        }
        // 存入后端缓存
        final String tokenKey = "token_" + token;
        CacheUtil.set(tokenKey, contextDco, 3600 * 24 * 365 * 100 * 1000);
    }

    public static final UserDao getCurrentUser() {
        String token = getToken();
        if (StringUtil.isBlank(token)) {
            return null;
        }
        UserDao userDao = p_getCurrentUser(token);
        // 从url获取token
        if (userDao == null) {
            userDao = ContextUtil.getCurrentUserByParamToken();
        }
        return userDao;
    }

    /**
     * 通过客户端传参传过来的token获取user对象
     *
     * @return
     */
    public static final UserDao getCurrentUserByParamToken() {
        final String paramToken = getParamToken();
        UserDao userDao = p_getCurrentUser(paramToken);
        return userDao;
    }

    private static UserDao p_getCurrentUser(String token) {
        ContextDco contextDco = getContextDco(token);
        if (contextDco == null) {
             LogUtil.error("ContextUtil_getCurrentUser_2", "没有token对应的用户");
            return null;
        }
        // 解决admin更名兼容redis缓存
        final Long userId = contextDco.getUserId();
        final UserDao userDao = p_userMap.get(userId);
        if (userDao == null) {
            return null;
        }
        String userNo = contextDco.getUserNo();
        if (StringUtil.equals(userNo, "admin")) {
            // 兼容历史数据：admin 对外统一映射为可配置的超级管理员账号
            userDao.setUserNo(MM.propertiesService.getString("auth_root_userno", "root"));
        }
        return userDao;
    }

    public static final ContextDco getContextDco(String token) {
        final String tokenKey = "token_" + token;
        ContextDco contextDco = CacheUtil.get(tokenKey);
        // LogUtil.errorNo("ContextUtil_getCurrentUser_2", "没有token对应的用户", paramToken);
        return contextDco;
    }

    public static final void removeCurrentUser() {
        final String token = getToken();
        removeCurrentUser(token);
    }

    public static final void removeCurrentUser(final String token) {
        final String tokenPreKey = CacheUtil.getStandardKey("token");
        CookieUtil.remove(tokenPreKey);
        final String tokenKey = "token_" + token;
        ContextDco contextDco = CacheUtil.get(tokenKey);
        if (contextDco != null) {
            Long userId = contextDco.getUserId();
            p_userMap.remove(userId);
        }
        CacheUtil.remove(tokenKey);
    }

    public static final Long getCurrentUserId() {
        UserDao userDao = getCurrentUser();
        if (userDao == null) {
            return null;
        }
        final Long userId = userDao.getId();
        return userId;
    }

    public static final String getCurrentUserNo() {
        UserDao userDao = getCurrentUser();
        if (userDao == null) {
            return null;
        }
        String userNo = userDao.getUserNo();
        return userNo;
    }

    public static final String getCurrentUserNikeName() {
        UserDao userDao = getCurrentUser();
        if (userDao == null) {
            return null;
        }
        String fullname = userDao.getNikeName();
        return fullname;
    }


    /**
     * 返回所有ContextDco
     *
     * @return
     */
    public static final List<String> getContextDcoTokenList() {
        final String tokenPreKey = CacheUtil.getStandardKey("token");
        final String prefixKey = tokenPreKey + "_";
        List<String> tokenList = CacheUtil.getLocalCacheKeyList();
        List<String> resultList = new ArrayList<>();
        for (String token : tokenList) {
            if (StringUtil.isBlank(token)) {
                continue;
            }
            if (token.startsWith(prefixKey)) {
                String realToken = token.substring(prefixKey.length());
                resultList.add(realToken);
            }
        }
        return resultList;
    }



    /**
     * session相关
     * 设置sessionAttribute
     *
     * @param name
     * @param value
     */
    public static void setSessionAttribute(String name, Object value) {
        HttpServletRequest request = ServletUtil.getRequest();
        if (request == null) {
            LogUtil.error("ContextUtil_setSessionAttribute_1", "请求为空！", name, value);
        }
        HttpSession session = request.getSession();
        session.setAttribute(name, value);
    }

    /**
     * 获取sessionAttribute
     */
    public static Object getSessionAttribute(String name) {
        HttpServletRequest request = ServletUtil.getRequest();
        if (request == null) {
            LogUtil.error("ContextUtil_getSessionAttribute_1", "请求为空！", name);
        }
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(name);
        return attribute;
    }

    /**
     * 让session失效
     */
    public static void invalidateSession() {
        HttpServletRequest request = ServletUtil.getRequest();
        if (request == null) {
            LogUtil.error("ContextUtil_invalidateSession_1", "请求为空！");
        }
        HttpSession session = request.getSession();
        session.invalidate();
    }

    /**
     * ContextSession中让session失效
     *
     * @param sessionId
     */
    public static void invalidateContextSession(String sessionId) {
        HttpSession session = getSession(sessionId);
        //已经存在登录，旧session失效
        if (session != null) {
            try {
                session.invalidate();
            } catch (Exception e) {
                LogUtil.error("ContextUtil_invalidateContextSession_e1", e);
            }
        }
    }

    /**
     * 获取sessionId
     */
    public static final String getSessionId() {
        HttpServletRequest request = ServletUtil.getRequest();
        if (request == null) {
            LogUtil.error("ContextUtil_getSessionId_1", "请求为空！");
            return null;
        }
        HttpSession httpSession = request.getSession();
        String sessionId = httpSession.getId();
        return sessionId;
    }

    public static synchronized HttpSession getSession(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        HttpSession httpSession = (HttpSession) p_sessionMap.get(sessionId);
        return httpSession;
    }

    /**
     * ContextSession里面的东西 首
     */
    public static synchronized void addSession(HttpSession session) {
        if (session != null) {
            String sessionId = session.getId();
            p_sessionMap.put(sessionId, session);
        }
    }

    public static synchronized void removeSession(HttpSession session) {
        if (session != null) {
            String sessionId = session.getId();
            p_sessionMap.remove(sessionId);
        }
    }

    public static synchronized void removeSessionById(String sessionId) {
        HttpSession session = (HttpSession) p_sessionMap.get(sessionId);
        if (session != null) {
            p_sessionMap.remove(sessionId);
        }
    }

    /**
     * 判断当前用户是否已登录
     */
    public static final Boolean isLoginedUser() {
        String paramToken = getToken();
        if (!StringUtil.isBlank(paramToken)) {
            UserDao userDao = getCurrentUser();
            if (userDao != null) {
                return true;
            }
        }
        return false;
    }

}