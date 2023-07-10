/**
  * Copyright 2022 bejson.com 
  */
package cn.getech.wms.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 */
@Data
public class PrdReturnMtrlFEntity {

    /**
     * 物料编码（必填）
     */
    private K3BaseEntity FMaterialId;
    /**
     * 单位编码（必填）
     */
    private K3BaseEntity FUnitID;
    /**
     * 申请退数量
     */
    private BigDecimal FAPPQty;
    /**
     * 实退数量（必填
     */
    private BigDecimal FQty;
    /**
     * 退料原因类型
     */
    private String FReturnType;
    /**
     * 退料原因编码
     */
    private K3BaseEntity FReturnReason;
    /**
     * 仓库编码
     */
    private K3BaseEntity FStockId;
    /**
     * 源单类型
     */
    private String FSrcBillType;
    /**
     * 源单编号
     */
    private String FSrcBillNo;
    /**
     * 用料清单编号
     */
    private String FPPBomBillNo;
    /**
     * 产品编码
     */
    private K3BaseEntity FParentMaterialId;
    /**
     * 生产订单内码ID
     */
    private String FMoId;
    private String FReserveType;
    private BigDecimal FBASESTOCKQTY;
    /**
     * 生产订单编号
     */
    private String FMoBillNo;
    private boolean FEntryVmiBusiness;
    private String FMoEntryId;
    private boolean FIsUpdateQty;
    private boolean FIsOverLegalOrg;
    private boolean FCheckReturnMtrl;
    private Long FMoEntrySeq;
    /**
     * 库存单位
     */
    private K3BaseEntity FStockUnitId;
    /**
     * 库存单位数量
     */
    private BigDecimal FStockAppQty;
    /**
     * 库存单位数量
     */
    private BigDecimal FStockQty;
    /**
     * 库存状态编码
     */
    private K3BaseEntity FStockStatusId;
    /**
     * 保管者类型
     */
    private String FKeeperTypeId;
    /**
     * 保管者编码
     */
    private K3BaseEntity FKeeperId;
    /**
     * 基本单位编码
     */
    private K3BaseEntity FBaseUnitId;
    /**
     * 基本单位申请数量
     */
    private BigDecimal FBaseAppQty;
    /**
     * 基本单位数量
     */
    private BigDecimal FBaseQty;
    /**
     * 货主类型
     */
    private String FOwnerTypeId;
    /**
     * 货主编码
     */
    private K3BaseEntity FOwnerId;
    private String FEntrySrcEnteryId;
    private K3BaseEntity FWorkShopId1;
    /**
     * 产品货主类型
     */
    private String FParentOwnerTypeId;
    /**
     * 产品货主编码
     */
    private K3BaseEntity FParentOwnerId;
    /**
     * 批号
     */
    private K3BaseEntity FLot;

}