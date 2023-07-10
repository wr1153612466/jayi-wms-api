package cn.getech.wms.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/25
 */
@Data
public class StkInStockEntryLink {

    /**
     * 业务流程图
     */
    private String FInStockEntry_Link_FFlowId;
    /**
     * 推进路线
     */
    private String FInStockEntry_Link_FFlowLineId;
    /**
     * 转换规则唯一标识
     */
    private String FInStockEntry_Link_FRuleId = "PUR_ReceiveBill-STK_InStock";
    /**
     *  源单表
     */
    private String FInStockEntry_Link_FSTableName = "T_PUR_ReceiveEntry";
    /**
     * 源单内码
     */
    private String FInStockEntry_Link_FSBillId;
    /**
     * 源单分录内码
     */
    private String FInStockEntry_Link_FSId;
    /**
     * 原始携带量 decimal
     */
    private BigDecimal FInStockEntry_Link_FRemainInStockBaseQtyOld;
    /**
     * 修改携带量decimal
     */
    private BigDecimal FInStockEntry_Link_FRemainInStockBaseQty;
    /**
     * 原始携带量decimal
     */
    private BigDecimal FInStockEntry_Link_FBaseUnitQtyOld;
    /**
     * 修改携带量decimal
     */
    private BigDecimal FInStockEntry_Link_FBaseUnitQty;
}
