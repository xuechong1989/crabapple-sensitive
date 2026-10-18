package xc.crabapple.sensitive.module.log;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dao.LogDao;
import xc.crabapple.sensitive.common.domain.dao.UserDao;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import xc.crabapple.sensitive.common.utils.ContextUtil;
import tools.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xc.crabapple.core.collection.MapUtil;
import xc.crabapple.core.collection.StringUtil;
import xc.crabapple.core.json.jackson.JacksonUtil;
import xc.crabapple.core.random.SnowflakeUtil;
import xc.crabapple.core.servlet.ServletUtil;
import xc.crabapple.core.thread.Business;
import xc.crabapple.core.thread.ThreadUtil;
import xc.crabapple.core.timer.TimerUtil;
import xc.crabapple.ssm.mybatis.pagination.PageList;
import xc.crabapple.ssm.mybatis.pagination.PageResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务日志模块
 */
@Service(value = "logServiceImpl")
public class LogServiceImpl {

    /**
     * 每次调用接口都会执行该方法
     * 标准做法是单独启动一个新子线程处理（使用线程池工具）
     */
    @Transactional
    public static final void insert(Object... array) {
        HttpServletRequest request = ServletUtil.getRequest();
        Map<String, Object> map = MapUtil.array2map(array);
        // 当前操作人
        UserDao user = ContextUtil.getCurrentUser();
        String clientIp = ServletUtil.getClientIp();
        String iPv4 = ServletUtil.getIPv4();
        ThreadUtil.start(new Business("appLog"){
            @Override
            public void process() {
                final String requestURI = ServletUtil.getRequestURI(request);
                if (StringUtil.isBlank(requestURI)) {
                    return;
                }
                LogDao logDao = new LogDao();
                // 参数信息
                if ("/api/crabapple/sensitive/login/login".equals(requestURI)) {
                    logDao.setModule("登录");
                    logDao.setSubModule("账号密码登录");
                    logDao.setMobile(map.get("mobile").toString());
                    logDao.setUserNo(map.get("userNo").toString());
                    logDao.setNikeName(map.get("nikeName").toString());
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/login/loginBySms".equals(requestURI)) {
                    logDao.setModule("登录");
                    logDao.setSubModule("短信登录");
                    logDao.setMobile(map.get("mobile").toString());
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/login/register".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("用户注册");
                    logDao.setMobile(map.get("mobile").toString());
                    logDao.setUserNo(map.get("userNo").toString());
                    logDao.setNikeName(map.get("nikeName").toString());
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/login/logout".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("用户退出");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/login/sendSms".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("发送短信");
                    logDao.setMobile(map.get("mobile").toString());
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/user/appeal".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("用户申诉");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/user/accessForbidden".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("禁止用户登录");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/user/accessAllow".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("恢复用户登录");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/user/handleAppeal".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("处理用户申诉");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/sensitiveWord/upload".equals(requestURI)) {
                    logDao.setModule("敏感词管理");
                    logDao.setSubModule("上传敏感词列表");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/sensitiveWord/insert".equals(requestURI)) {
                    logDao.setModule("敏感词管理");
                    logDao.setSubModule("新增敏感词");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/sensitiveWord/update".equals(requestURI)) {
                    logDao.setModule("敏感词管理");
                    logDao.setSubModule("修改敏感词");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/sensitiveWord/delete".equals(requestURI)) {
                    logDao.setModule("敏感词管理");
                    logDao.setSubModule("删除敏感词");
                    logDao.setContent(map.get("content").toString());
                } else if ("/api/crabapple/sensitive/user/changePassword".equals(requestURI)) {
                    logDao.setModule("用户管理");
                    logDao.setSubModule("修改密码");
                    logDao.setContent(map.get("content").toString());
                }else if ("/api/crabapple/sensitive/login/bindingWechat".equals(requestURI)) {
                    logDao.setModule("登录管理");
                    logDao.setSubModule("微信绑定");
                    logDao.setMobile(map.get("mobile").toString());
                    logDao.setContent(map.get("content").toString());
                }else if ("/api/crabapple/sensitive/login/wechatLogin".equals(requestURI)) {
                    logDao.setModule("登录管理");
                    logDao.setSubModule("微信登录");
                    logDao.setMobile(map.get("mobile").toString());
                    logDao.setUserNo(map.get("userNo").toString());
                    logDao.setNikeName(map.get("nikeName").toString());
                    logDao.setContent(map.get("content").toString());
                }
                String userAgent="";
                if (!"/api/crabapple/sensitive/sensitiveWord/upload".equals(requestURI)) {
                    userAgent = request.getHeader("user-agent");
                }
                logDao.setId(SnowflakeUtil.getLong());
                if (user != null) {
                    logDao.setNikeName(user.getNikeName());
                    logDao.setUserNo(user.getUserNo());
                    logDao.setMobile(user.getMobile());
                    logDao.setCreateBy(user.getId());
                }
                logDao.setAction(requestURI);
                logDao.setIp(clientIp+"/"+iPv4);
                logDao.setUserAgent(userAgent);
                logDao.setCreateTime(TimerUtil.time2str());
                MM.logMapper.create(logDao);
            }
        });
    }

    public final S2cDto getList() {
        String text = ServletUtil.getString("text");
        JsonNode jsonNode = JacksonUtil.json2jsonNode(text);
        String module = JacksonUtil.getString(jsonNode, "module");
        String subModule = JacksonUtil.getString(jsonNode, "subModule");
        String nikeName = JacksonUtil.getString(jsonNode, "nikeName");
        String userNo = JacksonUtil.getString(jsonNode, "userNo");
        String mobile = JacksonUtil.getString(jsonNode, "mobile");
        String orderBy = JacksonUtil.getString(jsonNode, "orderBy");
        String startTime = JacksonUtil.getString(jsonNode, "startTime");
        String endTime = JacksonUtil.getString(jsonNode, "endTime");
        String action = JacksonUtil.getString(jsonNode, "action");
        String ip = JacksonUtil.getString(jsonNode, "ip");
        String startIndex = JacksonUtil.getString(jsonNode, "startIndex");
        String pageSize = JacksonUtil.getString(jsonNode, "pageSize");
        Map<String, Object> param = new HashMap<>();
        if (!StringUtil.isBlank(module)) {
            param.put("module", module);
        }
        if (!StringUtil.isBlank(subModule)) {
            param.put("subModule", subModule);
        }
        if (!StringUtil.isBlank(nikeName)) {
            param.put("nikeName", nikeName);
        }
        if (!StringUtil.isBlank(userNo)) {
            param.put("userNo", userNo);
        }
        if (!StringUtil.isBlank(mobile)) {
            param.put("mobile", mobile);
        }
        if (!StringUtil.isBlank(startTime)) {
            param.put("startTime", startTime);
        }
        if (!StringUtil.isBlank(endTime)) {
            param.put("endTime", endTime);
        }
        if (!StringUtil.isBlank(action)) {
            param.put("action", action);
        }
        if (!StringUtil.isBlank(ip)) {
            param.put("ip", ip);
        }
        if (!StringUtil.isBlank(startIndex)) {
            param.put("startIndex", Integer.valueOf(startIndex));
        }
        if (!StringUtil.isBlank(pageSize)) {
            param.put("pageSize", Integer.valueOf(pageSize));
        }
        param.put("needTotal","yes");
        PageList<LogDao> list=MM.logMapper.selectList(param);
        PageResult pageResult = list.getPageResult();
        int total = pageResult.getTotalCount();
        S2cDto s2cDto = S2cDto.okay();
        s2cDto.setParam("data", list);
        s2cDto.setParam("total", total);
        return s2cDto;
    }

}