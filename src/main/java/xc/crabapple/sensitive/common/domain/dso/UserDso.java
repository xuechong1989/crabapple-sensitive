package xc.crabapple.sensitive.common.domain.dso;

public class UserDso extends AppDso {

    private Long id;
    private String nikeName;
    //密码
    private String password;
    //密码
    private String pwdSecond;
    //短信验证码
    private String validCode;
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
    //微信唯一标识
    private String openid;

    public UserDso() {
    }

    public UserDso(Long id, String nikeName, String password, String pwdSecond, String validCode, String mobile, String userNo, String mail, String sex, String age, String openid) {
        this.id = id;
        this.nikeName = nikeName;
        this.password = password;
        this.pwdSecond = pwdSecond;
        this.validCode = validCode;
        this.mobile = mobile;
        this.userNo = userNo;
        this.mail = mail;
        this.sex = sex;
        this.age = age;
        this.openid = openid;
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
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return pwdSecond
     */
    public String getPwdSecond() {
        return pwdSecond;
    }

    /**
     * 设置
     * @param pwdSecond
     */
    public void setPwdSecond(String pwdSecond) {
        this.pwdSecond = pwdSecond;
    }

    /**
     * 获取
     * @return validCode
     */
    public String getValidCode() {
        return validCode;
    }

    /**
     * 设置
     * @param validCode
     */
    public void setValidCode(String validCode) {
        this.validCode = validCode;
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
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * 设置
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * 获取
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return age
     */
    public String getAge() {
        return age;
    }

    /**
     * 设置
     * @param age
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * 获取
     * @return openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String toString() {
        return "UserDso{id = " + id + ", nikeName = " + nikeName + ", password = " + password + ", pwdSecond = " + pwdSecond + ", validCode = " + validCode + ", mobile = " + mobile + ", userNo = " + userNo + ", mail = " + mail + ", sex = " + sex + ", age = " + age + ", openid = " + openid + "}";
    }
}
