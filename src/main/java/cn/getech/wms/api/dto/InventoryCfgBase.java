package cn.getech.wms.api.dto;

import cn.getech.wms.api.enums.FormIDEnum;
import lombok.Data;

/**
 * 盘点计划查询返回对象
 */
@Data
@K3Entity(value = "FID,FBILLNO",formId = FormIDEnum.STK_STOCK_COUNT_INPUT,fName = "FBILLNO",defaultCondition = "")
public class InventoryCfgBase {
    private String FID;
    private String FBILLNO;
}
