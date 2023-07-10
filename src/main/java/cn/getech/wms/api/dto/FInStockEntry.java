package cn.getech.wms.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FInStockEntry {

    /**
     * 产品类型 默认 Standard  （必填）
     */
    private String FRowType = "Standard";
    /**
     * 物料编码 （必填）
     */
    private K3BaseEntity FMaterialId;
    /**
     * 备注
     */
    private String FNote;
    /**
     * 库存单位编码  （必填）
     */
    private K3BaseEntity FUnitID;
    /**
     * 物料说明
     */
    private String FMaterialDesc;
    /**
     * 含含税单价 (库存单位的价格) （必填）
     */
    private BigDecimal FTaxPrice;
    /**
     * 实收数量 (库存单位的数量) （必填）
     */
    private BigDecimal FRealQty;
    /**
     * 计价单位编码 （必填）
     */
    private K3BaseEntity FPriceUnitID;
    /**
     * 单价(计价单位的价格) （必填）
     */
    private BigDecimal FPrice;
    /**
     * 批次号
     */
    private K3BaseEntity FLot;
    /**
     * 仓库编码（必填）
     */
    private K3BaseEntity FStockId;
    /**
     * 库存状态 默认值 KCZT01_SYS
     */
    private K3BaseEntity FStockStatusId = new K3BaseEntity("KCZT01_SYS");
    /**
     * 是否赠品 是为true 否为false
     */
    private boolean FGiveAway;
    /**
     * 货主类型 默认 BD_OwnerOrg
     */
    private String FOWNERTYPEID = "OwnerOrg";
    /**
     * 是否来料检验
     */
    private boolean FCheckInComing;
    /**
     * 是否收料更新库存
     */
    private boolean FIsReceiveUpdateStock;
    /**
     * 计价基本数量
     */
    private BigDecimal FPriceBaseQty;
    /**
     * 采购单位 编码（必填）
     */
    private K3BaseEntity FRemainInStockUnitId;
    /**
     * 立账关闭 默认 false
     */
    private boolean FBILLINGCLOSE;
    /**
     * 采购数量 (采购单位的数量) （必填）
     */
    private BigDecimal FRemainInStockQty;
    /**
     * 未关联应付数量（计价单位）
     */
    private BigDecimal FAPNotJoinQty;
    /**
     * 采购基本数量（必填）
     */
    private BigDecimal FRemainInStockBaseQty;
    /**
     * 税率(%) 默认 13.00
     */
    private BigDecimal FEntryTaxRate = new BigDecimal(13);
    /**
     * 货主编码 根据货主类型变化取不同的码表
     * 当货主类型为BD_OwnerOrg时指其组织机构的编码 （必填）
     */
    private K3BaseEntity FOWNERID;
    /**
     * 价税合计（折前）
     */
    private BigDecimal FAllAmountExceptDisCount;

    /**
     * 关联关系表
     */
    private List<StkInStockEntryLink> FInStockEntry_Link;

}