package cn.getech.wms.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FEntityOut {

    private K3BaseEntity FMaterialId; //物料编码  必填
    private K3BaseEntity FUnitID;  //单位编码 必填
    private BigDecimal FQty;  //数量 必填
    private BigDecimal FBaseQty;  //数量 必填
    private K3BaseEntity FBaseUnitId;//基本单位编码 必填
    private K3BaseEntity FStockId; //仓库编码 必填
    private K3BaseEntity FLot; //批号 启用批号管理的物料 必填
    private String FOwnerTypeId;  //货主类型
    private K3BaseEntity FOwnerId; //货主编码
    private K3BaseEntity FStockStatusId; //库存可用状态
    private String FKeeperTypeId;  //保管者类型
    private Boolean FDistribution = false;  //参加费用分配 默认 false
    private K3BaseEntity FKeeperId; //保管者编码
}
