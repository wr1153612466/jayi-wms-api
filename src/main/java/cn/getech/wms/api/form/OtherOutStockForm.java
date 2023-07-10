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

/**
 * 其他出库单入参（wms请求入参）
 */
@Data
public class OtherOutStockForm {


    @ApiModelProperty(value = "单据类型编码 默认QTCKD01_SYS 必填")
    @NotBlank(message = "单据类型编码 默认QTCKD01_SYS 必填")
    private String billTypeId = "QTCKD01_SYS";

    @ApiModelProperty(value = "库存组织编码 必填")
    @NotBlank(message = "库存组织编码 必填")
    private String stockOrgId;

    @ApiModelProperty(value = "领用组织编码 必填")
    @NotBlank(message = "领用组织编码 必填")
    private String pickOrgId;;

    @ApiModelProperty(value = "库存方向 必填 默认 GENERAL 普通")
    @NotBlank(message = "库存方向 必填 默认 GENERAL 普通")
    private String stockDirect = "GENERAL";

    @ApiModelProperty("单据日期 (必填项)")
    @NotBlank(message = "单据日期 (必填项")
    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "创建日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String date;

    @ApiModelProperty(value = "客户编码 ；与下面的 部门编码 两者必填一项")
    private String custId;

    @ApiModelProperty(value = "部门编码 ；与上面的 客户编码 两者必填一项")
    private String deptId;

    @ApiModelProperty(value = "货主类型  必填")
    @NotBlank(message = "货主类型  必填")
    private String ownerTypeIdHead;

    @ApiModelProperty(value = "货主编码")
    private String ownerIdHead;

    @ApiModelProperty(value = "本位币编码 必填")
    @NotBlank(message = "本位币编码 必填")
    private String baseCurrId;


    @ApiModelProperty("其他出库单单据体")
    @NotEmpty(message = "其他出库单单据体 不能为空")
    private List<Detail> details;

    @ApiModel("其他出库单单据体")
    @Data
    public static class Detail {
        @ApiModelProperty(value = "物料编码  必填")
        @NotBlank(message = "物料编码  必填")
        private String materialId;
        @ApiModelProperty(value = "单位编码 必填")
        @NotBlank(message = "单位编码 必填")
        private String unitId;
        @ApiModelProperty(value = "数量 必填")
        @NotBlank(message = "数量 必填")
        private BigDecimal qty;
        @ApiModelProperty(value = "基本单位编码 必填")
        @NotBlank(message = "基本单位编码 必填")
        private String baseUnitId;
        @ApiModelProperty(value = "仓库编码 必填")
        @NotBlank(message = "仓库编码 必填")
        private String stockId;
        @ApiModelProperty(value = "批号 启用批号管理的物料 必填")
        @NotBlank(message = "批号 启用批号管理的物料 必填")
        private String lot;
        @ApiModelProperty(value = "货主类型")
        private String ownerTypeId;
        @ApiModelProperty(value = "货主编码")
        private String ownerId;
        @ApiModelProperty(value = "库存可用状态")
        private String stockStatusId;
        @ApiModelProperty(value = "保管者类型")
        private String keeperTypeId;
        @ApiModelProperty(value = "参加费用分配 默认 false")
        private Boolean distribution = false;
        @ApiModelProperty(value = "保管者编码")
        private String keeperId;
    }

}
