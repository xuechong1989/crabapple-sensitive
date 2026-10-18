package xc.crabapple.sensitive.common.utils;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.core.log.LogUtil;
import xc.crabapple.core.timer.TimerClockUtil;

/**
 * xuechong 2025-01-18
 * 系统定时器任务调度工具类
 */
public final class AppScheduleUtil {

    private static boolean p_inited = false;

    /**
     * xuechong 2025-01-18
     */
    public static final void init() {
        if (p_inited) {
            return;
        }
        p_inited = true;
        LogUtil.print("AppScheduleUtil_init_1", "系统定时器初始化完成");
        //
        // 时间点执行
         TimerClockUtil.clockByPoints(MM.sensitiveWordService, "deleteRepeatMap", "****-**-** 04:00:00");
        //
        // 时间延迟循环执行
//         TimerClockUtil.clockByDelay(MM.sensitiveWordService, "deleteRepeatMap", 60000L);
    }

}