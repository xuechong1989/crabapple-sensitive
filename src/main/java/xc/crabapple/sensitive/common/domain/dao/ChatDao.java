package xc.crabapple.sensitive.common.domain.dao;

import xc.crabapple.core.random.SnowflakeUtil;

/**
 * xuechong 2024-12-26
 */
public class ChatDao extends AppDao{

    private Long id;
    private Long parentId;
    private Long sessionId;
    private String content;
    private Long userId;
    private String style;
    private String createTime;

    public ChatDao(){

    }

    public ChatDao(boolean initial){
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
