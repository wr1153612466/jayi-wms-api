package cn.getech.wms.api.config;

import cn.getech.wms.api.entity.WmsLog;
import cn.getech.wms.api.service.IWmsLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Component
@Configuration
@EnableScheduling
@Slf4j
public class CompleteScheduleConfig implements Serializable {

    @Autowired
    private IWmsLogService iWmsLogService;

    @Scheduled(cron = "0 0 0 * * ?")//每天凌晨00:00
//    @Scheduled(cron = "0/2 * * * * ? ")//表示每2秒 执行任务
    public void executeSendAskForLeavePerson(){


        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -30);//保留30天
        Date newDate = c.getTime();

        boolean remove = iWmsLogService.lambdaUpdate().le(WmsLog::getCreateTime, newDate).remove();

    }

}
