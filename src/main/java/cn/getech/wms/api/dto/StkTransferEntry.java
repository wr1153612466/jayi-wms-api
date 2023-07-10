package cn.getech.wms.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/28
 */
@Data
public class StkTransferEntry {

    private int FID;
    /**
     * 单据状态
     */
    private String FDocumentStatus;
    /**
     * 业务类型varchar(50)
     */
    private K3BaseEntity FBillTypeID;
    /**
     * 业务类型
     */
    private String FBizType = "NORMAL";
    /**
     * 调拨方向 (必填项)
     */
    private String FTransferDirect = "GENERAL";
    /**
     * 调拨类型varchar(50)
     */
    private String FTransferBizType = "InnerOrgTransfer";

    /**
     * 结算组织
     */
    private K3BaseEntity FSettleOrgId;

    /**
     * 销售组织
     */
    private K3BaseEntity FSaleOrgId;
    /**
     * 调出库存组织 varchar(50) 下发
     */
    private K3BaseEntity FStockOutOrgId;
    /**
     * 调出货主类型varchar(50)
     */
    private String FOwnerTypeOutIdHead = "BD_OwnerOrg";
    /**
     * 调出货主varchar(50)
     */
    private K3BaseEntity FOwnerOutIdHead;

    /**
     * 调入库存组织
     */
    private K3BaseEntity FStockOrgId;
    /**
     * 是否含税
     */
    private boolean FIsIncludedTax;
    /**
     * 价外税
     */
    private boolean FIsPriceExcludeTax;
    /**
     * 汇率类型
     */
    private K3BaseEntity FExchangeTypeId;
    /**
     * 调入货主类型
     */
    private String FOwnerTypeIdHead = "BD_OwnerOrg";
    /**
     * 结算币别
     */
    private K3BaseEntity FSETTLECURRID = new K3BaseEntity("PRE001");
    private int FExchangeRate;
    /**
     * 调入货主 varchar(50)
     */
    private K3BaseEntity FOwnerIdHead;
    /**
     * 单据日期 datetime
     */
    private String FDate;
    /**
     * 本位币
     */
    private K3BaseEntity FBaseCurrId;
    /**
     * 对应供应商
     */
    private K3BaseEntity FSUPPLIERID;
    /**
     * 对应客户
     */
    private K3BaseEntity FCustID;
    private boolean FWriteOffConsign;
    private boolean FISOVER;
    /**
     * 备注
     */
    private String FNote;
    /**
     * 明细信息
     */
    private List<StkTransferFBillEntry> FBillEntry;
}
