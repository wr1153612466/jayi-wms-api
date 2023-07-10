/**
  * Copyright 2022 bejson.com 
  */
package cn.getech.wms.api.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class PrdInstockEntity {

    private String FBillNo;

    private String FID;

    private K3BaseEntity FBillType = new K3BaseEntity("SCRKD02_SYS");

    @ApiModelProperty("入库日期")
    private String FDate;

    @ApiModelProperty("生产组织代码")
    private K3BaseEntity FPrdOrgId;

    @ApiModelProperty("入库组织代码")
    private K3BaseEntity FStockOrgId;

    @ApiModelProperty("货主类型")
    private String FOwnerTypeId0;

    @ApiModelProperty("货主")
    private K3BaseEntity FOwnerId0;

    private Boolean FIsEntrust;

    @ApiModelProperty("备注")
    private String FDescription;

    @ApiModelProperty("明细")
    private List<Detail> FEntity;




    @Data
    public static class Detail{
        private Boolean FIsNew;

        @ApiModelProperty("源单分录内码")
        private String FSrcEntryId;

        private String FEntryID;

        @ApiModelProperty("完工")
        private Boolean FIsFinished;

        @ApiModelProperty("倒冲领料")
        private Boolean FISBACKFLUSH;

        @ApiModelProperty("物料代码")
        private K3BaseEntity FMATERIALID;

        @ApiModelProperty("生产订单编号")
        private String FMoBillNo;

        @ApiModelProperty("生产订单行号")
        private String FMoEntrySeq;

        @ApiModelProperty("生产订单内码")
        private String FMoId;

        @ApiModelProperty("产品类型")
        private String FProductType;

        @ApiModelProperty("明细车间")
        private K3BaseEntity FWorkShopId1;

        @ApiModelProperty("生产订单分录内码")
        private String FMoEntryId;

        @ApiModelProperty("源单类型")
        private String FSrcBillType;

        @ApiModelProperty("源单单号")
        private String FSrcBillNo;

        @ApiModelProperty("源单行号")
        private String FSrcEntrySeq;

        @ApiModelProperty("需求来源")
        private String FReqSrc;

        @ApiModelProperty("需求单据")
        private String FReqBillNo;

        @ApiModelProperty("需求单据行号")
        private String FReqEntrySeq;

        @ApiModelProperty("备注")
        private String FMEMO;

        @ApiModelProperty("单位")
        private K3BaseEntity FUnitID;

        @ApiModelProperty("批号")
        private K3BaseEntity FLot;

        @ApiModelProperty("BOM版本")
        private K3BaseEntity FBomId;

        @ApiModelProperty("应收数量")
        private BigDecimal FMustQty;

        @ApiModelProperty("实收数量")
        private BigDecimal FRealQty;

        @ApiModelProperty("应收数量(基本单位)")
        private BigDecimal FBaseMustQty;

        @ApiModelProperty("实收数量(基本单位)")
        private BigDecimal FBaseRealQty;

        @ApiModelProperty("基本单位")
        private K3BaseEntity FBaseUnitId;

        private String FOwnerTypeId;

        @ApiModelProperty("货主")
        private K3BaseEntity FOwnerId;

        @ApiModelProperty("仓库代码")
        private K3BaseEntity FStockId;

        @ApiModelProperty("库存单位")
        private K3BaseEntity FStockUnitId;

        @ApiModelProperty("库存状态")
        private K3BaseEntity FStockStatusId;

        @ApiModelProperty("保管者类型")
        private String FKeeperTypeId;

        @ApiModelProperty("保管者")
        private K3BaseEntity FKeeperId;

        @ApiModelProperty("生产订单主产品分录内码")
        private String FMOMAINENTRYID;

        @ApiModelProperty("生产车间2")
        private K3BaseEntity F_RVMM_WORKSHOPID;

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
        private BigDecimal FEntity_Link_FBasePrdRealQtyOld;

        @ApiModelProperty("入库数量")
        private BigDecimal FEntity_Link_FBasePrdRealQty;
    }

}