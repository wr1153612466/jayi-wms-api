package cn.getech.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/28
 */
@Data
public class PrdUseEntry {

   /************************用料清单维度****************************/

   @ApiModelProperty("用料清单编号源单内码")
   private String fID;

   @ApiModelProperty("用料清单编号")
   private String fBillNo;


   /************************生产订单维度****************************/

   @ApiModelProperty("生产订单内码")
   private String FMoId;

   @ApiModelProperty("生产订单编号")
   private String fMOBillNO;

   @ApiModelProperty("生产车间")
   private String FWorkshopID;

   @ApiModelProperty("生产订单行号")
   private String FMoEntrySeq;

   @ApiModelProperty("生产订单分录内码")
   private String fMOEntryID;

   @ApiModelProperty("BOM版本")
   private String FBOMID;

   @ApiModelProperty("产品编码")
   private String FMaterialID_FNUMBER;


   /************************用料清单明细维度****************************/
   @ApiModelProperty("用料清单源单分录内码")
   private String FEntity_FEntryID;

   @ApiModelProperty("用料清单源单分录物料")
   private String FMaterialID2_FNumber;

   @ApiModelProperty("用料清单源单分录物料单位")
   private String FUnitID2_FNumber;



}
