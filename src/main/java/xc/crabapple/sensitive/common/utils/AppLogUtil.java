package xc.crabapple.sensitive.common.utils;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dao.LogDao;
import xc.crabapple.sensitive.common.domain.dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import xc.crabapple.core.collection.MapUtil;
import xc.crabapple.core.collection.StringUtil;
import xc.crabapple.core.file.properties.PropertiesUtil;
import xc.crabapple.core.log.LogConfigBean;
import xc.crabapple.core.log.LogUtil;
import xc.crabapple.core.random.SnowflakeUtil;
import xc.crabapple.core.servlet.ServletUtil;
import xc.crabapple.core.timer.TimerUtil;

import java.util.Map;

public final class AppLogUtil {

    /**
     * xuechong 2025-01-09
     */
    public static final void init(){
        LogConfigBean logConfigBean = new LogConfigBean();
        logConfigBean.setType(PropertiesUtil.getValue("log.type"));
        logConfigBean.setLevel(PropertiesUtil.getValue("log.level"));
        logConfigBean.setPath(PropertiesUtil.getValue("log.file.path"));
        logConfigBean.setMaxHistory(PropertiesUtil.getInt("log.file.maxHistory"));
        logConfigBean.setMaxFileSize(PropertiesUtil.getValue("log.file.maxFileSize"));
        LogUtil.init(logConfigBean);
    }

}