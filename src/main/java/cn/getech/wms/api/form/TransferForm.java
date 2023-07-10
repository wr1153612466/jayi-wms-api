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
 * @author bocong.zheng@getech.cn
 * @since 2022/11/28
 */
@Data
@ApiModel("调拨参数")
public class TransferForm {

    @ApiModelProperty(value = "单据类型（必填） 默认 ZJDB01_SYS",required = true)
    @NotBlank(message = "单据类型（必填）不能为空")
    private String billTypeID;

    @ApiModelProperty(value = "单据状态（必填）",required = true)
    @NotBlank(message = "单据状态（必填）不能为空")
    private String documentStatus;

    @ApiModelProperty(value = "调入库存组织")
    private String stockOrgId;

    @ApiModelProperty(value = "调出库存组织")
    private String stockOutOrgId;

    @ApiModelProperty(value = "源单编号")
    private String srcBillNo;

    @ApiModelProperty(value = "调出仓库.仓库ID")
    private String srcStockId;

    @ApiModelProperty(value = "调入仓库.仓库ID")
    private String destStockId;


    @ApiModelProperty(value = "创建人")
    private String creatorId;

    @ApiModelProperty(value = "创建日期")
    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "创建日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String createDate;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty(value = "调拨明细",required = true)
    @NotEmpty(message = "调拨明细不能为空")
    @Valid
    private List<TransferDetail> transferDetails;

    @Data
    @ApiModel("调拨明细参数")
    public static class TransferDetail{

        @ApiModelProperty(value = "物料.物料ID（必填）",required = true)
        @NotBlank(message = "物料.物料ID（必填）不能为空")
        private String materialId;

        @ApiModelProperty(value = "单位ID（必填）",required = true)
        @NotBlank(message = "单位ID（必填）不能为空")
        private String unitID;

        @ApiModelProperty(value = " 实收数量（必填）",required = true)
        @NotNull(message = "实收数量（必填）不能为空")
        private BigDecimal factQty;

        @ApiModelProperty("批号主档ID")
        private String fLot;

        @ApiModelProperty("备注")
        private String noteEntry;

        @ApiModelProperty("调拨关联明细")
        private List<TransferDetailLink> transferDetailLinks;

    }

    @Data
    @ApiModel("调拨关联明细参数")
    public static class TransferDetailLink{

        @ApiModelProperty("源单内码")
        @NotBlank(message = "源单内码不能为空")
        private String srcBillId;

        @ApiModelProperty(" 源单内码明细内码")
        @NotBlank(message = "源单内码明细内码")
        private String srcId;

        @ApiModelProperty(" 原始携带量")
        @NotNull(message = "原始携带量不能为空")
        private BigDecimal baseQtyOld;

        @ApiModelProperty("修改携带量")
        @NotNull(message = "修改携带量不能为空")
        private BigDecimal baseQty;

    }


}
