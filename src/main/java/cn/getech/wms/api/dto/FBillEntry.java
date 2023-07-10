package cn.getech.wms.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 盘点作业结果回写明细
 */
@Data
public class FBillEntry {

    private String FEntryID;//明细内码ID
    private BigDecimal FCountQty;//盘点数量

}
