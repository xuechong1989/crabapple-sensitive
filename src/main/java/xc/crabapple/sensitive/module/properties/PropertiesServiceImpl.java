package xc.crabapple.sensitive.module.properties;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dao.PropertiesDao;
import org.springframework.stereotype.Service;
import xc.crabapple.core.collection.StringUtil;
import xc.crabapple.core.file.properties.PropertiesUtil;
import xc.crabapple.core.log.LogUtil;
import xc.crabapple.core.regex.RegexUtil;

/**
 * 描述：系统参数 处理接口
 */
@Service(value="propertiesServiceImpl")
public class PropertiesServiceImpl {

    public PropertiesDao getSysProperties(String alias){
        PropertiesDao sysProperties = MM.propertiesMapper.selectSysProperties(alias);
        return sysProperties;
    }

    public String getString(String key){
        String value = getString(key, "");
        return value;
    }

    public String getString(String key, String defaultValue) {
        try {
            PropertiesDao propertiesDao = getSysProperties(key);
            if (propertiesDao == null || StringUtil.isBlank(propertiesDao.getValue())) {
                return PropertiesUtil.getValue(key, defaultValue);
            }
            return propertiesDao.getValue();
        } catch (Exception e) {
            LogUtil.error("PropertiesServiceImpl_getValue_e31", e, key, defaultValue);
            return "";
        }
    }

    public Integer getInt(String key){
        Integer value = getInt(key, 0);
        return value;
    }

    public Integer getInt(String key, Integer defaultValue){
        String str = getString(key);
        if(StringUtil.isBlank(str)){
            return defaultValue;
        }
        boolean flag = RegexUtil.inNumber(str);
        if(!flag){
            return defaultValue;
        }
        //
        Integer value = Integer.parseInt(str);
        if(value != null){
            return value;
        }
        return defaultValue;
    }

    public Long getLong(String key){
        Long value = getLong(key, 0L);
        return value;
    }

    public Long getLong(String key, Long defaultValue){
        String str = getString(key);
        if(StringUtil.isBlank(str)){
            return defaultValue;
        }
        boolean flag = RegexUtil.inNumber(str);
        if(!flag){
            return defaultValue;
        }
        //
        Long value = Long.valueOf(str);
        if(value != null){
            return value;
        }
        return defaultValue;
    }

}