package cn.getech.wms.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/28
 */
@Data
public class StkTransferFBillEntryLink {

    private String FBillEntry_Link_FFlowId;
    private String FBillEntry_Link_FFlowLineId;
    private String FBillEntry_Link_FRuleId = "StkTransferApply-StkTransferDirect";
    private String FBillEntry_Link_FSTableName = "T_STK_STKTRANSFERAPPENTRY";
    /**
     * 源单内码 下发
     */
    private String FBillEntry_Link_FSBillId;
    /**
     * 源单分录内码 下发
     */
    private String FBillEntry_Link_FSId;
    /**
     * 数量decimal  原始携带
     */
    private BigDecimal FBillEntry_Link_FBaseQtyOld;
    /**
     * 数量decimal  调拨数量
     */
    private BigDecimal FBillEntry_Link_FBaseQty;


}
