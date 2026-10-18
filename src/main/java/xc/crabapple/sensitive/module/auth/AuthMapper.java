package xc.crabapple.sensitive.module.auth;

import xc.crabapple.sensitive.common.domain.dao.AccessTokenDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xc.crabapple.ssm.mybatis.pagination.PageList;

import java.util.Map;

@Component
@Mapper
public interface AuthMapper {

    // ########## insert
    void create(AccessTokenDao accessTokenDao);



    // ########## select
    PageList<AccessTokenDao> selectList(Map<String, Object> paramMap);
    AccessTokenDao selectAccessTokenClientId(@Param("clientId") String clientId);



    // ########## update



    // ########## delete


}