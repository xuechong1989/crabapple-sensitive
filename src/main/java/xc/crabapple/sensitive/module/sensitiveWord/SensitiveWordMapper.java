package xc.crabapple.sensitive.module.sensitiveWord;


import xc.crabapple.sensitive.common.domain.dao.SensitiveWordDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xc.crabapple.ssm.mybatis.pagination.PageList;

import java.util.List;
import java.util.Map;


@Component
@Mapper
public interface SensitiveWordMapper {



    // ########## select
    PageList<SensitiveWordDao> selectList(Map<String, Object> paramMap);
    List<String> selectKeywordList();
    SensitiveWordDao selectById(@Param("id") long id);
    SensitiveWordDao selectListByKeyword(@Param("keyword") String keyword);

    // ########## insert

    void create(SensitiveWordDao sensitiveWordDao);

    // ########## update

    void update(SensitiveWordDao sensitiveWordDao);

    // ########## delete

    void delete(@Param("id")Long id);

}