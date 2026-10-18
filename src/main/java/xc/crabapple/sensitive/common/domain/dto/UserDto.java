package xc.crabapple.sensitive.common.domain.dto;

public class UserDto extends AppDto {

    //主键
    private Long id;
    //昵称
    private String nikeName;
    //手机号
    private String mobile;
    //用户账号
    private String userNo;
    //邮箱
    private String mail;
    //性别
    private String sex;
    //年龄
    private String age;
    //用户状态 yes-正常 no-封禁
    private String accessStatus;
    //出发敏感词次数
    private Integer sensitivityNumber;
    //申诉状态 yes-已申诉 no-未申诉
    private String appealStatus;
    //是否为管理员
    private String isAdmin;
    //创建时间
    private String createTime;

    public UserDto() {

    }

    public UserDto(Long id, String nikeName, String mobile, String userNo, String mail, String sex, String age, String accessStatus, Integer sensitivityNumber, String appealStatus, String isAdmin, String createTime) {
        this.id = id;
        this.nikeName = nikeName;
        this.mobile = mobile;
        this.userNo = userNo;
        this.mail = mail;
        this.sex = sex;
        this.age = age;
        this.accessStatus = accessStatus;
        this.sensitivityNumber = sensitivityNumber;
        this.appealStatus = appealStatus;
        this.isAdmin = isAdmin;
        this.createTime = createTime;
    }

    /**
     * 获取
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取
     *
     * @return nikeName
     */
    public String getNikeName() {
        return nikeName;
    }

    /**
     * 设置
     *
     * @param nikeName
     */
    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    /**
     * 获取
     *
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置
     *
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取
     *
     * @return userNo
     */
    public String getUserNo() {
        return userNo;
    }

    /**
     * 设置
     *
     * @param userNo
     */
    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    /**
     * 获取
     *
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * 设置
     *
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * 获取
     *
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置
     *
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取
     *
     * @return age
     */
    public String getAge() {
        return age;
    }

    /**
     * 设置
     *
     * @param age
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * 获取
     *
     * @return accessStatus
     */
    public String getAccessStatus() {
        return accessStatus;
    }

    /**
     * 设置
     *
     * @param accessStatus
     */
    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    /**
     * 获取
     *
     * @return sensitivityNumber
     */
    public Integer getSensitivityNumber() {
        return sensitivityNumber;
    }

    /**
     * 设置
     *
     * @param sensitivityNumber
     */
    public void setSensitivityNumber(Integer sensitivityNumber) {
        this.sensitivityNumber = sensitivityNumber;
    }

    /**
     * 获取
     *
     * @return appealStatus
     */
    public String getAppealStatus() {
        return appealStatus;
    }

    /**
     * 设置
     *
     * @param appealStatus
     */
    public void setAppealStatus(String appealStatus) {
        this.appealStatus = appealStatus;
    }

    /**
     * 获取
     *
     * @return isAdmin
     */
    public String getIsAdmin() {
        return isAdmin;
    }

    /**
     * 设置
     *
     * @param isAdmin
     */
    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 获取
     *
     * @return createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     *
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "UserDto{id = " + id + ", nikeName = " + nikeName + ", mobile = " + mobile + ", userNo = " + userNo + ", mail = " + mail + ", sex = " + sex + ", age = " + age + ", accessStatus = " + accessStatus + ", sensitivityNumber = " + sensitivityNumber + ", appealStatus = " + appealStatus + ", isAdmin = " + isAdmin + ", createTime = " + createTime + "}";
    }
}
