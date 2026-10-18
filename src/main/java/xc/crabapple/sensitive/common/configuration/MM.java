package xc.crabapple.sensitive.common.configuration;

import xc.crabapple.sensitive.module.auth.AuthMapper;
import xc.crabapple.sensitive.module.auth.AuthServiceImpl;
import xc.crabapple.sensitive.module.log.LogMapper;
import xc.crabapple.sensitive.module.log.LogServiceImpl;
import xc.crabapple.sensitive.module.properties.PropertiesMapper;
import xc.crabapple.sensitive.module.properties.PropertiesServiceImpl;
import xc.crabapple.sensitive.module.sensitiveWord.SensitiveWordMapper;
import xc.crabapple.sensitive.module.sensitiveWord.SensitiveWordServiceImpl;

/**
 * 添加全局自动扫描实例化所有全局变量
 * 模块管理器 - ModuleManager
 */
public class MM {

    public static AuthServiceImpl authService;
    public static AuthMapper authMapper;

    public static PropertiesServiceImpl propertiesService;
    public static PropertiesMapper propertiesMapper;

    public static SensitiveWordServiceImpl sensitiveWordService;
    public static SensitiveWordMapper sensitiveWordMapper;

    public static LogServiceImpl logService;
    public static LogMapper logMapper;

}
