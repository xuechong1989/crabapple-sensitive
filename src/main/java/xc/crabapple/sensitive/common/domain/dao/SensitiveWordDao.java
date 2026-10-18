package xc.crabapple.sensitive.common.domain.dao;

public class SensitiveWordDao extends AppDao {
    //id
    private Long id;
    //关键词
    private String keyword;
    //类别
    private String type;
    //备注
    private String desc;

    public SensitiveWordDao() {
    }

    public SensitiveWordDao(Long id, String keyword, String type, String desc) {
        this.id = id;
        this.keyword = keyword;
        this.type = type;
        this.desc = desc;
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
     * @return keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * 设置
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 获取
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return "SensitiveWordDao{id = " + id + ", keyword = " + keyword + ", type = " + type + ", desc = " + desc + "}";
    }
}
