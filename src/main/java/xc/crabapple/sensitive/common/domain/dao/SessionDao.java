package xc.crabapple.sensitive.common.domain.dao;

import xc.crabapple.core.random.SnowflakeUtil;

/**
 * xuechong 2024-12-27
 */
public class SessionDao extends AppDao{

    private Long id;
    private Long userId;
    private String createTime;

    public SessionDao(){

    }

    public SessionDao(boolean initial){
        if(initial){
            id = SnowflakeUtil.getLong();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


}