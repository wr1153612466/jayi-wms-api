package cn.getech.wms.api.service.impl;

import cn.getech.wms.api.entity.WmsLog;
import cn.getech.wms.api.enums.WmsLogEnum;
import cn.getech.wms.api.mapper.WmsLogMapper;
import cn.getech.wms.api.service.IWmsLogService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class WmsLogServiceImpl extends ServiceImpl<WmsLogMapper, WmsLog> implements IWmsLogService {

    @Autowired
    private IWmsLogService iWmsLogService;

    @Value("${kingdee-identify.userName}")
    private String k3username;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long saveLog(String logType, String logJson) {

        WmsLog log = new WmsLog();
        log.setLogType(logType);
        log.setLogJson(logJson);
        log.setCreateTime(new Date());
        log.setK3username(k3username);

        iWmsLogService.save(log);

        return log.getId();
    }


    @Override
    public void test() {

    }
}
