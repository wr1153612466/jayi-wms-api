/**
  * Copyright 2022 bejson.com 
  */
package cn.getech.wms.api.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class PrdPickmtrlEntity {

    @ApiModelProperty("领料单号")
    private String FBillNo;

    private Map FBillType;

    @ApiModelProperty("领料日期")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private String FDate;

    @ApiModelProperty("库存组织代码")
    private K3BaseEntity FStockOrgID;

    @ApiModelProperty("仓库")
    private K3BaseEntity FStockId0;

    @ApiModelProperty("生产组织代码")
    private K3BaseEntity FPrdOrgId;

    @ApiModelProperty("货主类型")
    private String FOwnerTypeId0;

    @ApiModelProperty("货主代码")
    private K3BaseEntity FOwnerId0;

    @ApiModelProperty("备注")
    private String FDescription;

    @ApiModelProperty("明细")
    private List<Detail> FEntity;

    @Data
    public static class Detail{

        private Integer FEntryID;

        @ApiModelProperty("父项物料代码")
        private K3BaseEntity FParentMaterialId;

        @ApiModelProperty("主库存基本单位实发数量")
        private BigDecimal FBaseStockActualQty;

        @ApiModelProperty("子项物料代码")
        private K3BaseEntity FMaterialId;

        @ApiModelProperty("单位代码")
        private K3BaseEntity FUNITID;

        @ApiModelProperty("申请数量")
        private BigDecimal FAppQty;

        @ApiModelProperty("实发数量")
        private BigDecimal FActualQty;

        @ApiModelProperty("仓库代码")
        private K3BaseEntity FStockId;

        @ApiModelProperty("批号代码")
        private K3BaseEntity FLot;

        @ApiModelProperty("BOM代码")
        private K3BaseEntity FBomId;

        @ApiModelProperty("库存状态")
        private K3BaseEntity FStockStatusId;

        @ApiModelProperty("备注")
        private String FEntrtyMemo;

        @ApiModelProperty("计划跟踪号")
        private String FMtoNo;

        @ApiModelProperty("生产订单行号")
        private String FMoEntrySeq;

        @ApiModelProperty("生产订单内码id")
        private String FMoId;

        @ApiModelProperty("生产订单单号")
        private String FMoBillNo;

        @ApiModelProperty("生产订单分录内码")
        private Long FMoEntryId;

        private String FOwnerTypeId;

        @ApiModelProperty("库存单位")
        private K3BaseEntity FStockUnitId;

        @ApiModelProperty("基本单位")
        private K3BaseEntity FBaseUnitID;

        @ApiModelProperty("基本单位申请数量")
        private BigDecimal FBaseAppQty;

        @ApiModelProperty("基本单位实发数量")
        private BigDecimal FBaseActualQty;

        @ApiModelProperty("车间代码")
        private K3BaseEntity FEntryWorkShopId;

        private String FKeeperTypeId;

        @ApiModelProperty("保管者")
        private K3BaseEntity FKeeperID;

        @ApiModelProperty("货主")
        private K3BaseEntity FOwnerID;

        @ApiModelProperty("源单类型")
        private String FSrcBillType;

        @ApiModelProperty("源单单号")
        private String FSrcBillNo;

        @ApiModelProperty("用料清单分录内码")
        private String FPPBomEntryId;

        @ApiModelProperty("用料清单编号")
        private String FPPBomBillNo;

        @ApiModelProperty("产品货主类型")
        private String FParentOwnerTypeId;

        @ApiModelProperty("产品货主")
        private K3BaseEntity FParentOwnerId;

        private List<Link> FEntity_Link;

    }


    @Data
    public static class Link{

        private String FEntity_Link_FFlowId;
        private String FEntity_Link_FFlowLineId;
        private String FEntity_Link_FRuleId;
        private String FEntity_Link_FSTableName;

        @ApiModelProperty("源单内码")
        private String FEntity_Link_FSBillId;

        @ApiModelProperty("源单分录内码")
        private String FEntity_Link_FSId;

        @ApiModelProperty("原始携带数")
        private BigDecimal FEntity_Link_FBaseActualQtyOld;

        @ApiModelProperty("领料数量")
        private BigDecimal FEntity_Link_FBaseActualQty;


    }

}