package xc.crabapple.sensitive.common.domain.dto;

public class AppealDto extends AppDto {

    //主键
    private Long id;
    //用户id
    private Long userId;
    //申诉状态 pending:待处理 pass:申诉通过 refuse:申诉不通过
    private String appealStatus;
    private String reason;
    private String createName;

    public AppealDto() {

    }

    public AppealDto(Long id, Long userId, String appealStatus, String reason, String createName) {
        this.id = id;
        this.userId = userId;
        this.appealStatus = appealStatus;
        this.reason = reason;
        this.createName = createName;
    }

    /**
     * 获取
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

    public String toString() {
        return "AppealDto{id = " + id + ", userId = " + userId + ", appealStatus = " + appealStatus + ", reason = " + reason + ", createName = " + createName + "}";
    }
}
