package xc.crabapple.sensitive.common.domain.dao;

public class PropertiesDao extends AppDao {
    //参数ID
    public Long id;
    //名称
    public String name;
    //别名
    public String alias;
    //是否全局
    public String global;
    //是否加密存储
    public String encrypt;
    //参数值
    public String value;
    //分类
    public String category;
    //描述
    public String description;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //创建人ID或标识
    private Long createBy;
    //更新人ID或标识
    private Long updateBy;


    public PropertiesDao() {
    }

    public PropertiesDao(Long id, String name, String alias, String global, String encrypt, String value, String category, String description, String createTime, String updateTime, Long createBy, Long updateBy) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.global = global;
        this.encrypt = encrypt;
        this.value = value;
        this.category = category;
        this.description = description;
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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取
     * @return global
     */
    public String getGlobal() {
        return global;
    }

    /**
     * 设置
     * @param global
     */
    public void setGlobal(String global) {
        this.global = global;
    }

    /**
     * 获取
     * @return encrypt
     */
    public String getEncrypt() {
        return encrypt;
    }

    /**
     * 设置
     * @param encrypt
     */
    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * 获取
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 获取
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return updateBy
     */
    public Long getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置
     * @param updateBy
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String toString() {
        return "PropertiesDao{id = " + id + ", name = " + name + ", alias = " + alias + ", global = " + global + ", encrypt = " + encrypt + ", value = " + value + ", category = " + category + ", description = " + description + ", createTime = " + createTime + ", updateTime = " + updateTime + ", createBy = " + createBy + ", updateBy = " + updateBy + "}";
    }
}
