package xc.crabapple.sensitive.module.log;

import xc.crabapple.sensitive.common.domain.dao.LogDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xc.crabapple.ssm.mybatis.pagination.PageList;

import java.util.Map;

@Component
@Mapper
public interface LogMapper {

    // ########## insert
    void create(LogDao logDao);



    // ########## select
    PageList<LogDao> selectList(Map<String, Object> paramMap);



    // ########## update



    // ########## delete


}