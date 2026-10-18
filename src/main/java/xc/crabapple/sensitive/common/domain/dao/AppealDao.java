package xc.crabapple.sensitive.common.domain.dao;

public class AppealDao extends AppDao{

    //主键
    private Long id;
    //用户id
    private Long userId;
    //申诉原因
    private String reason;
    //创建人
    private Long createBy;
    //申诉人昵称
    private String createName;
    //创建时间
    private String createTime;
    //申诉状态 pending:待处理 pass:申诉通过 refuse:申诉不通过
    private String appealStatus;

    public AppealDao() {

    }

    public AppealDao(Long id, Long userId, String reason, Long createBy, String createName, String createTime, String appealStatus) {
        this.id = id;
        this.userId = userId;
        this.reason = reason;
        this.createBy = createBy;
        this.createName = createName;
        this.createTime = createTime;
        this.appealStatus = appealStatus;
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
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
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

    /**
     * 获取
     * @return createName
     */
    public String getCreateName() {
        return createName;
    }

    /**
     * 设置
     * @param createName
     */
    public void setCreateName(String createName) {
        this.createName = createName;
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
     * @return appealStatus
     */
    public String getAppealStatus() {
        return appealStatus;
    }

    /**
     * 设置
     * @param appealStatus
     */
    public void setAppealStatus(String appealStatus) {
        this.appealStatus = appealStatus;
    }

    public String toString() {
        return "AppealDao{id = " + id + ", userId = " + userId + ", reason = " + reason + ", createBy = " + createBy + ", createName = " + createName + ", createTime = " + createTime + ", appealStatus = " + appealStatus + "}";
    }
}
