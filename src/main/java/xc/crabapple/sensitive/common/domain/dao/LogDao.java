package xc.crabapple.sensitive.common.domain.dao;

public class LogDao extends AppDao{

    //主键id
    private Long id;
    //功能模块
    private String module;
    //操作类型
    private String subModule;
    //用户昵称
    private String nikeName;
    //用户账号
    private String userNo ;
    //用户手机号
    private String mobile;
    //内容
    private String content;
    //接口
    private String action;
    //ip
    private String ip;
    //用户网络信息
    private String userAgent;
    //创建时间
    private String createTime;
    //创建人
    private Long createBy;

    public LogDao() {
    }

    public LogDao(Long id, String module, String subModule, String nikeName, String userNo, String mobile, String content, String action, String ip, String userAgent, String createTime, Long createBy) {
        this.id = id;
        this.module = module;
        this.subModule = subModule;
        this.nikeName = nikeName;
        this.userNo = userNo;
        this.mobile = mobile;
        this.content = content;
        this.action = action;
        this.ip = ip;
        this.userAgent = userAgent;
        this.createTime = createTime;
        this.createBy = createBy;
    }

    /**
     * 获取
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取
     * @return module
     */
    public String getModule() {
        return module;
    }

    /**
     * 设置
     * @param module
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 获取
     * @return subModule
     */
    public String getSubModule() {
        return subModule;
    }

    /**
     * 设置
     * @param subModule
     */
    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    /**
     * 获取
     * @return nikeName
     */
    public String getNikeName() {
        return nikeName;
    }

    /**
     * 设置
     * @param nikeName
     */
    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    /**
     * 获取
     * @return userNo
     */
    public String getUserNo() {
        return userNo;
    }

    /**
     * 设置
     * @param userNo
     */
    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    /**
     * 获取
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * 设置
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 获取
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取
     * @return userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 设置
     * @param userAgent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * 获取
     * @return createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return createBy
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * 设置
     * @param createBy
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String toString() {
        return "LogDao{id = " + id + ", module = " + module + ", subModule = " + subModule + ", nikeName = " + nikeName + ", userNo = " + userNo + ", mobile = " + mobile + ", content = " + content + ", action = " + action + ", ip = " + ip + ", userAgent = " + userAgent + ", createTime = " + createTime + ", createBy = " + createBy + "}";
    }
}
