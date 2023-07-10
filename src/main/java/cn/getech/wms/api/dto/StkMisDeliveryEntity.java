package cn.getech.wms.api.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
/**
 * 组装其他出库参数
 */
@Data
public class StkMisDeliveryEntity {
    /**
     * 单据内码ID 新增时固定为0
     */
    private int FID;
    /**
     * 单据类型编码 默认QTCKD01_SYS 必填
     */
    private K3BaseEntity FBillTypeID = new K3BaseEntity("QTCKD01_SYS");
    /**
     * 库存组织编码 必填
     */
    private K3BaseEntity FStockOrgId;
    /**
     * 领用组织编码 必填
     */
    private K3BaseEntity FPickOrgId;
    /**
     * 库存方向 必填 默认 GENERAL 普通
     */
    private String FStockDirect = "GENERAL";
    /**
     * 单据日期
     */
    private String FDate;
    /**
     * 客户编码 ；与下面的 部门编码 两者必填一项
     */
    private K3BaseEntity FCustId;
    /**
     * 部门编码 ；与上面的 客户编码 两者必填一项
     */
    private K3BaseEntity FDeptId;
    /**
     * 货主类型  必填
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
     * 其他出库单单据体（多条单据体）
     */
    private List<FEntityOut> FEntity;
}
