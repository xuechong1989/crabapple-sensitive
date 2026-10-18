package xc.crabapple.sensitive.common.domain.dao;

/**
 *
 */
public class SmsValidCodeDao extends AppDao {

	//ID
	public Long id;
	//手机号码
	public String mobile;
	//验证码
	public String validCode;
	//验证码有效时间（单位分）
	public String validCodeTime;
	//创建时间
	private String createTime;
	//更新时间
	private String updateTime;
	//创建人ID或标识
	private String createBy;
	//更新人ID或标识
	private String updateBy;


	public SmsValidCodeDao() {
	}

	public SmsValidCodeDao(Long id, String mobile, String validCode, String validCodeTime, String createTime, String updateTime, String createBy, String updateBy) {
		this.id = id;
		this.mobile = mobile;
		this.validCode = validCode;
		this.validCodeTime = validCodeTime;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.createBy = createBy;
		this.updateBy = updateBy;
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
	 * @return phoneNumber
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
	 * @return validCodeTime
	 */
	public String getValidCodeTime() {
		return validCodeTime;
	}

	/**
	 * 设置
	 * @param validCodeTime
	 */
	public void setValidCodeTime(String validCodeTime) {
		this.validCodeTime = validCodeTime;
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
	 * @return updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置
	 * @param updateTime
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取
	 * @return createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * 设置
	 * @param createBy
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 获取
	 * @return updateBy
	 */
	public String getUpdateBy() {
		return updateBy;
	}

	/**
	 * 设置
	 * @param updateBy
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String toString() {
		return "SmsValidCode{id = " + id + ", phoneNumber = " + mobile + ", validCode = " + validCode + ", validCodeTime = " + validCodeTime + ", createTime = " + createTime + ", updateTime = " + updateTime + ", createBy = " + createBy + ", updateBy = " + updateBy + "}";
	}
}