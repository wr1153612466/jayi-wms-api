package cn.getech.wms.api.form;

import cn.getech.wms.api.vaildator.DateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


@Data
@ApiModel(value = "SaveProductionDrawForm",description = "生产领料wms入参实体")
public class SaveProductionDrawForm {

    @ApiModelProperty(value = "单据编号", required = true,example="TEST001")
    @NotBlank(message = "单据编号不能为空")
    private String fbillNo;

    @ApiModelProperty(value = "领料日期", required = true,example = "2022-12-03 20:36:16")
    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "创建日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    @NotBlank
    private String date;

    @ApiModelProperty(value = "生产用料清单编号", required = true,example = "PPBOM00039980")
    @NotBlank(message = "生产用料清单编号不能为空")
    private String fsbillNo;

    @ApiModelProperty(value = "组织id", required = true,example = "10001")
    @NotBlank(message = "组织id不能为空")
    private String organization;

    @ApiModelProperty(value = "货主id", required = true,example = "10001")
    @NotBlank(message = "货主id不能为空")
    private String factoryId;

    @ApiModelProperty("备注")
    private String remake;

    @ApiModelProperty(value = "物料明细",required = true)
    @NotEmpty(message = "生产领料明细不能为空")
    @Valid
    private List<SaveProductionDrawDetailForm> details;

    @Data
    @ApiModel("生产领料明细")
    public static class SaveProductionDrawDetailForm {

        @ApiModelProperty(value = "源单分录(生产用料清单明细)内码", required = true,example = "403358")
        @NotBlank(message = "源单分录(生产用料清单明细)内码不能为空")
        private String fsid;

        @ApiModelProperty(value = "仓库id", required = true,example = "201002")
        @NotBlank(message = "仓库id不能为空")
        private String fstockid;

        @ApiModelProperty(value = "物料编码",required = true,example = "113.34.0204")
        @NotBlank(message = "物料编码不能为空")
        private String mcode;

        @ApiModelProperty(value = "数量", required = true,example = "1")
        @NotNull(message = "数量不能为空")
        private BigDecimal qty;

        @ApiModelProperty("批次")
        private String batch;
    }
}
