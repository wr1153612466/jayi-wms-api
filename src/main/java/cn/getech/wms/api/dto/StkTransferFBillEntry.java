package cn.getech.wms.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/28
 */
@Data
public class StkTransferFBillEntry {

    /**
     * 产品类型
     */
    private String FRowType;
    /**
     * 物料代码 varchar(50)
     */
    private K3BaseEntity FMaterialId;
    /**
     * 单位 varchar(50)
     */
    private K3BaseEntity FUnitID;
    /**
     * 调拨数量 decimal
     */
    private BigDecimal FQty;
    /**
     * 应调拨数量
     */
    private BigDecimal F_RVMM_Qty1;
    /**
     * 批号代码 varchar(50)
     */
    private K3BaseEntity FLot;
    /**
     * 调出仓库 varchar(50) 下发
     */
    private K3BaseEntity FSrcStockId;
    /**
     * 调出仓位 varchar(50)
     */
    private K3BaseEntity FSrcStockLocId;
    /**
     * 调入仓库 varchar(50)
     */
    private K3BaseEntity FDestStockId;
    /**
     * 调出库存状态
     */
    private K3BaseEntity FSrcStockStatusId;
    /**
     * 调出库存状态
     */
    private K3BaseEntity FDestStockStatusId;
    /**
     * 入库日期 datetime
     */
    private String FBusinessDate;
    /**
     * 调出货主类型varchar(50)
     */
    private String FOwnerTypeOutId;
    /**
     * 调出货主varchar(50)
     */
    private K3BaseEntity FOwnerOutId;
    /**
     * 调入货主类型 varchar(50)
     */
    private String FOwnerTypeId;
    /**
     * 调入货主 varchar(50)
     */
    private K3BaseEntity FOwnerId;
    /**
     * 基本单位 varchar(50)
     */
    private K3BaseEntity FBaseUnitId;
    /**
     * 调拨数量（基本单位）
     */
    private BigDecimal FBaseQty;
    /**
     * 赠品
     */
    private boolean FISFREE;
    /**
     * 调入保管者类型 varchar(50)
     */
    private String FKeeperTypeId;
    /**
     * 调入保管者 varchar(50)
     */
    private K3BaseEntity FKeeperId;
    /**
     * 调出保管者类型
     */
    private String FKeeperTypeOutId;
    /**
     * 调出保管者 varchar(50)
     */
    private K3BaseEntity FKeeperOutId;
    /**
     * 调入物料编码 varchar(50)
     */
    private K3BaseEntity FDestMaterialId;
    /**
     * BOM版本 varchar(50)
     */
    private K3BaseEntity FBomId;
    /**
     * 计划跟踪号 varchar(50)
     */
    private String FMtoNo;
    /**
     * 源单类型
     */
    private String FSrcBillTypeId;

    //生产订单号
    private String FOrderNo;

    //生产车间
    private Map F_PVCE_WORKCENTER;
    /**
     * 源单编号 varchar(50)
     */
    private String FSrcBillNo;
    private K3BaseEntity FSaleUnitId;
    private BigDecimal FSaleQty;
    private BigDecimal FSalBaseQty;
    private K3BaseEntity FPriceUnitID;
    private BigDecimal FPriceQty;
    private BigDecimal FPriceBaseQty;
    private boolean FTransReserveLink;
    private boolean FCheckDelivery;
    /**
     * 关联关系表
     */
    private List<StkTransferFBillEntryLink> FBillEntry_Link;
}
