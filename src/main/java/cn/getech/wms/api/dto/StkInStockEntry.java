/**
  * Copyright 2022 bejson.com 
  */
package cn.getech.wms.api.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class StkInStockEntry {

    /**
     * 采购入库单内码ID，唯一值，新增的时候固定填 0 （必填）
     */
    private int FID;
    /**
     * 单据类型编码，标准采购入库单固定为 RKD01_SYS  （必填）
     */
    private K3BaseEntity FBillTypeID = new K3BaseEntity("RKD01_SYS");
    /**
     * 单据日期  （必填）
     */
    private String FDate;
    /**
     * 收料部门  其部门编码 （非必填 根据业务逻辑需要）
     */
    private K3BaseEntity FStockOrgId;
    /**
     * 收料部门  其部门编码 （非必填 根据业务逻辑需要）
     */

    private K3BaseEntity FStockDeptId;
    /**
     * 需求组织 其组织机构的编码 （非必填）
     */
    @Deprecated
    private K3BaseEntity FDemandOrgId;

    private K3BaseEntity FCorrespondOrgId;
    /**
     * 采购部门 其部门编码 （非必填 根据业务逻辑需要）
     */
    private K3BaseEntity FPurchaseOrgId;
    /**
     * 供应商编码 （非必填 根据业务逻辑需要）
     */
    private K3BaseEntity FSupplierId;
    /**
     * 供货方编码  （非必填 根据业务逻辑需要）
     */
    private K3BaseEntity FSupplyId;
    /**
     * 结算方编码（非必填 根据业务逻辑需要）
     */
    private K3BaseEntity FSettleId;
    /**
     * 收款方编码（非必填 根据业务逻辑需要）
     */
    private K3BaseEntity FChargeId;
    /**
     * 货主类型（必填）  固定值 BD_OwnerOrg
     */
    private String FOwnerTypeIdHead = "BD_OwnerOrg";
    /**
     * 货主编码 其组织机构的编码 （必填）
     */
    private K3BaseEntity FOwnerIdHead;
    /**
     * 拆单类型 默认A（非必填）
     */
    private String FSplitBillType = "A";
    /**
     * 财务信息 子单据头（必填）
     */
    private FInStockFin FInStockFin;
    /**
     * 明细信息 （必填）
     */
    private List<FInStockEntry> FInStockEntry;
    /**
     * 送货单号
     */
    private String FDeliveryBill;
    /**
     *  创建人
     */
    private K3BaseEntity FCreatorId;
    /**
     * 创建时间
     */
    private String FCreateDate;
}