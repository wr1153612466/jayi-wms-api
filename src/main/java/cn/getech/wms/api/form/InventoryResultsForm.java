package cn.getech.wms.api.form;

import cn.getech.wms.api.vaildator.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * REP发起的盘点
 * 盘点方案结果回传（wms请求入参）
 */
@Data
public class InventoryResultsForm {
    @ApiModelProperty(value = "单据编码")
    private String billNo;


    @ApiModelProperty(value = "单据内码ID")
    @NotBlank(message = "单据内码ID 必填")
    private String id;

    @ApiModelProperty("物料盘点作业明细")
    @NotEmpty(message = "物料盘点作业明细 不能为空")
    @Valid
    private List<Detail> details;

    @ApiModel("物料盘点作业明细")
    @Data
    public static class Detail {

        @ApiModelProperty(value = "明细内码ID（分录ID）")
        @NotBlank(message = "明细内码ID 必填")
        private String entryId;

        @ApiModelProperty(value = "盘点数量")
        @NotNull(message = "盘点数量 必填")
        private BigDecimal countQty;

    }

}
