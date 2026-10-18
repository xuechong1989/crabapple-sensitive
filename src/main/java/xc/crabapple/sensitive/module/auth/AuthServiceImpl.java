package xc.crabapple.sensitive.module.auth;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dao.AccessTokenDao;
import xc.crabapple.sensitive.common.domain.dao.UserDao;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import xc.crabapple.sensitive.common.utils.ContextUtil;
import org.springframework.stereotype.Service;
import xc.crabapple.core.cipher.sm2.Sm2Util;
import xc.crabapple.core.collection.StringUtil;
import xc.crabapple.core.log.LogUtil;
import xc.crabapple.core.net.ip.IPUtil;
import xc.crabapple.core.random.RandomUtil;
import xc.crabapple.core.random.SnowflakeUtil;
import xc.crabapple.core.servlet.ServletUtil;
import xc.crabapple.core.timer.TimerUtil;
import xc.crabapple.ssm.cache.CacheUtil;
import xc.crabapple.ssm.mybatis.pagination.PageList;
import xc.crabapple.ssm.mybatis.pagination.PageResult;

import java.util.HashMap;
import java.util.Map;

@Service(value = "authServiceImpl")
public class AuthServiceImpl {

    /**
     * SM2 密钥从配置读取，必须由部署方自行生成并保管，禁止提交到代码仓库。
     */
    private String p_publicKey() {
        return MM.propertiesService == null ? "" : MM.propertiesService.getString("auth_sm2_publickey", "");
    }

    private String p_privateKey() {
        return MM.propertiesService == null ? "" : MM.propertiesService.getString("auth_sm2_privatekey", "");
    }

    /**
     * 管理员口令登录。
     * 仅当配置了非空的 auth_admin_key 时启用；未配置时该入口直接拒绝，避免出现可预测的万能口令。
     */
    public S2cDto login(){
        final String adminKey = MM.propertiesService.getString("auth_admin_key", "");
        if(StringUtil.isBlank(adminKey)){
            return S2cDto.failure("未配置 auth_admin_key，管理员口令登录已禁用");
        }
        String key = ServletUtil.getString("key");
        if(!StringUtil.equals(adminKey, key)){
            return S2cDto.failure("key_error");
        }
        final String clientId = RandomUtil.getStrWithStamp(64);
        final String clientIp = IPUtil.getClientIp();
        final String userAgent = ServletUtil.getUserAgent();
        //
        UserDao userDao = new UserDao();
        userDao.setId(1L);
        userDao.setUserNo("admin");
        userDao.setNikeName("admin");
        userDao.setIsAdmin("yes");
        String token = ContextUtil.setCurrentUser(userDao, clientId, clientIp, userAgent, true);
        S2cDto s2cDto = S2cDto.okay();
        s2cDto.setParam("token", token);
        s2cDto.setParam("clientId", clientId);
        return s2cDto;
    }

    /**
     * 为受信任的客户端节点生成 accessToken（SM2 公钥加密）。
     * clientId、clientIp 均由配置指定；未配置 clientIp 时取当前请求来源 IP。
     */
    public S2cDto generate(){
        final String publicKey = p_publicKey();
        if(StringUtil.isBlank(publicKey)){
            return S2cDto.failure("未配置 auth_sm2_publickey，无法生成 accessToken");
        }
        String clientId = MM.propertiesService.getString("auth_client_id", "default");
        String clientIp = MM.propertiesService.getString("auth_client_ip", "");
        if(StringUtil.isBlank(clientIp)){
            clientIp = IPUtil.getClientIp();
        }
        String randomNow = RandomUtil.getStrWithStamp(64);  // 动态值
        String plaintextAccessToken = new StringBuilder().append(clientId).append("_").append(clientIp).append("_").append(randomNow).toString();
        String accessToken = Sm2Util.encrypt(publicKey, plaintextAccessToken);
        // 日志中不输出 token 明文或密文
        LogUtil.println("AuthServiceImpl_generate_1", "accessToken生成成功", clientId, clientIp);
        //
        AccessTokenDao accessTokenDao = MM.authMapper.selectAccessTokenClientId(clientId);
        if(accessTokenDao != null){
            LogUtil.error("AuthServiceImpl_generate_2", "clientId已存在", clientId, clientIp);
            S2cDto s2cDto = S2cDto.failure("clientId已存在");
            s2cDto.setParam("clientId", clientId);
            s2cDto.setParam("clientIp", clientIp);
            return s2cDto;
        }
        accessTokenDao = new AccessTokenDao();
        accessTokenDao.setId(SnowflakeUtil.getLong());
        accessTokenDao.setClientId(clientId);
        accessTokenDao.setClientIp(clientIp);
        MM.authMapper.create(accessTokenDao);
        S2cDto s2cDto = S2cDto.okay("创建成功");
        s2cDto.setParam("clientId", clientId);
        s2cDto.setParam("clientIp", clientIp);
        return s2cDto;
    }

    /**
     * 校验客户端 accessToken，并保证同一随机数只能使用一次。
     */
    public String checkAccessToken(final String accessToken) {
        final String privateKey = p_privateKey();
        if(StringUtil.isBlank(privateKey)){
            return "error_0_key_not_configured";
        }
        String plaintextAccessToken = Sm2Util.decrypt(privateKey, accessToken);
        if(StringUtil.isBlank(plaintextAccessToken)){
            return "error_1";
        }
        if(!StringUtil.containOfOne(plaintextAccessToken, "_")){
            return "error_2";
        }
        String[] array = StringUtil.split(plaintextAccessToken, "_");
        if(array.length < 3){
            return "error_2";
        }
        String clientId = array[0];  // 客户端节点唯一标识符
        String clientIp = array[1];
        String randomNow = array[2];
        Object value = CacheUtil.get(randomNow);
        if(value != null){
            return "error_3";
        }
        CacheUtil.set(randomNow, 1);
        AccessTokenDao accessTokenDao = MM.authMapper.selectAccessTokenClientId(clientId);
        if(accessTokenDao == null){
            return "error_4";
        }
        if(!StringUtil.equals(clientIp, accessTokenDao.getClientIp())){
            return "error_4";
        }
        return "success";
    }

    /**
     * 分页查询客户端节点。
     */
    public S2cDto getList(){
        Integer startIndex = ServletUtil.getInteger("startIndex", 0);
        Integer pageSize = ServletUtil.getInteger("pageSize", 20);
        //
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        PageList<AccessTokenDao> accessTokenDaoList = MM.authMapper.selectList(paramMap);
        PageResult pageResult = accessTokenDaoList.getPageResult();
        int total = pageResult.getTotalCount();
        S2cDto s2cDto = S2cDto.okay("查询成功");
        s2cDto.setParam("data", accessTokenDaoList);
        s2cDto.setParam("total", total);
        return s2cDto;
    }

}
