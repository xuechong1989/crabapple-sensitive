package xc.crabapple.sensitive.module.properties;


import xc.crabapple.sensitive.common.domain.dao.PropertiesDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface PropertiesMapper {


    // ########## select
    PropertiesDao selectSysProperties(@Param("alias") String alias);


    // ########## insert


    // ########## update


    // ########## delete


}