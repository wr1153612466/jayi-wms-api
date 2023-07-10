package cn.getech.wms.api.service;

import cn.getech.wms.api.entity.WmsLog;
import cn.getech.wms.api.enums.WmsLogEnum;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IWmsLogService extends IService<WmsLog> {

    public Long saveLog(String logType, String logJson);

    public void test();

}
