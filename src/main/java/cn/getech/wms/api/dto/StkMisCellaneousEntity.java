package cn.getech.wms.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 组装其他入库参数
 */
@Data
public class StkMisCellaneousEntity {
    /**
     * 单据内码ID，新增保存时固定为0
     */
    private int FID;
    /**
     * 单据类型 编码 必填
     */
    private K3BaseEntity FBillTypeID = new K3BaseEntity("QTRKD01_SYS");
    /**
     * 库存组织编码 必填
     */
    private K3BaseEntity FStockOrgId;
    /**
     * 库存方向 默认 GENERAL 普通
     */
    private String FStockDirect = "GENERAL";
    /**
     * 单据日期  （必填）
     */
    private String FDate;
    /**
     * 供应商编码 与 部门，两者 必填一项
     */
    private K3BaseEntity FSUPPLIERID;
    /**
     * 部门编码 与供应商 两者，必填一项
     */
    private K3BaseEntity FDEPTID;
    /**
     * 货主类型 必填，明细也有此字段，看实际业务，明细中都一致的，可以填单据头，明细中有不一样的，以明细为主
     */
    private String FOwnerTypeIdHead;
    /**
     * 货主编码
     */
    private K3BaseEntity FOwnerIdHead;
    /**
     * 本位币编码 必填
     */
    private K3BaseEntity FBaseCurrId;
    /**
     * 其他入库单单据体（多条单据体）
     */
    private List<FEntityIn> FEntity;
}
