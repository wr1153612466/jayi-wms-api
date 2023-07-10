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
 * 其他入库单入参（wms请求入参）
 */
@Data
public class OtherInStockForm {

    @ApiModelProperty(value = "单据类型编码 必填")
    @NotBlank(message = "单据类型编码 必填")
    private String billTypeId = "QTRKD01_SYS";

    @ApiModelProperty(value = "库存组织编码 必填")
    @NotBlank(message = "库存组织编码 必填")
    private String stockOrgId;

    @ApiModelProperty(value = "库存方向 默认 GENERAL 普通")
    @NotBlank(message = "库存方向 必填")
    private String stockDirect = "GENERAL";

    @ApiModelProperty("单据日期 (必填项)")
    @NotBlank(message = "单据日期 (必填项)不能为空")
    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "入库日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String date;

    @ApiModelProperty(value = "供应商编码 与 部门，两者 必填一项")
    private String supplierId;

    @ApiModelProperty(value = "部门编码 与供应商 两者，必填一项")
    private String deptId;

    /**
     * 货主类型 必填，明细也有此字段，看实际业务，明细中都一致的，可以填单据头，明细中有不一样的，以明细为主
     */
    @ApiModelProperty(value = "货主类型 必填")
    @NotBlank(message = "货主类型 必填")
    private String ownerTypeIdHead;

    @ApiModelProperty(value = "货主编码")
    private String ownerIdHead;

    @ApiModelProperty(value = "本位币编码 必填")
    @NotBlank(message = "本位币编码 必填")
    private String baseCurrId;

    @ApiModelProperty("其他入库单单据体")
    @NotEmpty(message = "其他入库单单据体 不能为空")
    private List<Detail> details;

    @ApiModel("其他入库单单据体")
    @Data
    public static class Detail {
        @ApiModelProperty(value = "入库类型  默认1  合格入库 必填")
        @NotBlank(message = "入库类型  默认1  合格入库 必填")
        private String inStockType = "1";
        @ApiModelProperty(value = "物料编码  必填")
        @NotBlank(message = "物料编码  必填")
        private String materialId;
        @ApiModelProperty(value = "单位编码 必填")
        @NotBlank(message = "单位编码 必填")
        private String unitId;
        @ApiModelProperty(value = "仓库编码 必填")
        @NotBlank(message = "仓库编码 必填")
        private String stockId;
        @ApiModelProperty(value = "库存状态 默认KCZT01_SYS 可用")
        private String stockStatusId ="KCZT01_SYS";
        @ApiModelProperty(value = "批号主档编码 启用批号管理的物料，批号必填")
        @NotBlank(message = "批号主档编码 启用批号管理的物料，批号必填")
        private String lot;
        @ApiModelProperty(value = "数量 FUnitID单位对应的数量")
        @NotNull(message = "数量 FUnitID单位对应的数量不能为空")
        private BigDecimal qty;
        @ApiModelProperty(value = "货主类型 必填")
        @NotBlank(message = "货主类型 必填")
        private String ownerTypeId;
        @ApiModelProperty(value = "货主编码 必填")
        @NotBlank(message = "货主编码 必填")
        private String ownerId;
        @ApiModelProperty(value = "保管者类型")
        private String keeperTypeId;
        @ApiModelProperty(value = "保管者编码")
        private String keeperId;
    }


}
