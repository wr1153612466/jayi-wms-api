package cn.getech.wms.api.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
/**
 * 组装盘点回写参数
 */
@Data
public class StkStockCountInputEntity {

    /**
     *单据内码ID
     */
    private String FID;

    /**
     * 盘点更新明细
     */
    private List<FBillEntry> FBillEntry;




}
