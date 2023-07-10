package cn.getech.wms.api.form;

import cn.getech.wms.api.vaildator.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;


@Data
@ApiModel("生产退料参数")
public class PrdReturnMtrlForm {

    @ApiModelProperty(value = "生产退料单号",example = "TEST000001")
    private String fbillNo;

    @ApiModelProperty(value = "单据日期 (必填项)",example = "2022-12-05 11:02:37")
    @NotBlank(message = "单据日期 (必填项)不能为空")
    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "入库日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String date;

    @ApiModelProperty(value = "组织id", required = true,example = "10001")
    @NotBlank(message = "组织id不能为空")
    private String organization;

    @ApiModelProperty(value = "货主id", required = true,example = "10001")
    @NotBlank(message = "货主id不能为空")
    private String factoryId;

    @ApiModelProperty(value = "备注")
    private String remake;

    @ApiModelProperty(value = "退料明细", required = true)
    @Valid
    private List<Detail> details;

    @Data
    @ApiModel("生产退料明细参数")
    public static class Detail{

        @ApiModelProperty(value = "生产订单号",required = true,example = "生产订单110962")
        private String srcbillno;

        @ApiModelProperty(value = "生产订单分录内码",required = true)
        @NotBlank(message = "生产订单分录内码不能为空")
        private String moFsId;

        @ApiModelProperty(value = "生产订单产品编码",required = true,example = "30117.10557.206")
        private String srcbillmcode;

        @ApiModelProperty(value = "物料编码",required = true,example = "112.13.53001")
        private String mcode;

        @ApiModelProperty(value = "退料类型",required = true,example = "1")
        private String freturntype;

        @ApiModelProperty(value = "单位",required = true,example = "根")
        private String unit;

        @ApiModelProperty(value = "退料原因编码",example = "106")
        private String freturnreason;

        @ApiModelProperty("批次")
        private String batch;

        @ApiModelProperty(value = "仓库编码", required = true,example = "101001")
        private String stockNo;

        @ApiModelProperty(value = "数量",required = true,example = "1")
        private BigDecimal qty;
    }

}
