package xc.crabapple.sensitive;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.utils.AppCacheUtil;
import xc.crabapple.sensitive.common.utils.AppEnvUtil;
import xc.crabapple.sensitive.common.utils.AppLogUtil;
import xc.crabapple.sensitive.common.utils.AppScheduleUtil;
import xc.crabapple.sensitive.common.utils.AppSecurityUtil;
import xc.crabapple.sensitive.common.utils.AppSmsUtil;
import xc.crabapple.sensitive.common.utils.ContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import xc.crabapple.springboot.SpringbootUtil;
import xc.crabapple.ssm.mybatis.pagination.PaginationUtil;
import xc.crabapple.ssm.spring.SpringUtil;

@SpringBootApplication
@EntityScan
@MapperScan("xc.crabapple.sensitive.module.*")
public class SensitiveApplication {

    public static void main(String[] args) {
        // 开源版默认从 classpath:application.properties 读取配置；
        // 如沿用企业版 router.conf 机制，将后两个参数替换为 router 路径与 SM4 密钥即可。
        SpringbootUtil.start("Crabapple Sensitive", SensitiveApplication.class, MM.class, args, "", "", 0, 20);
        //
        PaginationUtil.init(0, 20);
        AppLogUtil.init();
        AppCacheUtil.init();
        AppSecurityUtil.init();
        AppSmsUtil.init();
        AppEnvUtil.init();
        AppScheduleUtil.init();
        ContextUtil.init();
        //
        SpringUtil.banner();
    }

}
