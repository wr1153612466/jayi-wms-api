package cn.getech.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PrdMoEntry {

   @ApiModelProperty("生产订单id")
   private String FID;

   @ApiModelProperty("生产订单编号")
   private String FBillNo;

   @ApiModelProperty("生产订单明细物料id")
   private String FTreeEntity_FEntryId;

   @ApiModelProperty("生产订单明细物料编码")
   private String FMaterialId_FNumber;

   @ApiModelProperty("单位")
   private String FUnitId_FNumber;

   @ApiModelProperty("单据头车间编码")
   private String FWorkShopID0_FNumber;

   @ApiModelProperty("单据体车间编码")
   private String FWorkShopID_FNumber;

   @ApiModelProperty("产品类型")
   private String FProductType;

   @ApiModelProperty("生产订单行号")
   private String FTreeEntity_FSeq;

   @ApiModelProperty("需求来源")
   private String FReqSrc;

   @ApiModelProperty("销售订单号")
   private String FSaleOrderNo;

   @ApiModelProperty("需求单据行号")
   private String FSaleOrderEntrySeq;

   @ApiModelProperty("工序车间")
   private String F_RVMM_WorkShopID_FNUMBER;

   @ApiModelProperty("生产车间2")
   private String F_RVMM_WORKSHOPID;

   @ApiModelProperty("BOM版本")
   private String FBomId_FNUMBER;




}
