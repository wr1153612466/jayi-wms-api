package cn.getech.wms.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FEntityIn {

    private String FInStockType = "1";//入库类型  默认1  合格入库 必填
    private K3BaseEntity FMATERIALID; //物料编码  必填
    private K3BaseEntity FUnitID;  //单位编码 必填
    private K3BaseEntity FSTOCKID;//仓库编码 必填
    private K3BaseEntity FSTOCKSTATUSID = new K3BaseEntity("KCZT01_SYS");//库存状态 默认KCZT01_SYS 可用
    private K3BaseEntity FLOT; //批号主档编码 启用批号管理的物料，批号必填

    private BigDecimal FQty; //数量 FUnitID单位对应的数量
    private String FOWNERTYPEID; //货主类型 必填
    private K3BaseEntity FOWNERID;  //货主编码 必填
    private String FKEEPERTYPEID;  //保管者类型
    private K3BaseEntity FKEEPERID;  //保管者编码
}
