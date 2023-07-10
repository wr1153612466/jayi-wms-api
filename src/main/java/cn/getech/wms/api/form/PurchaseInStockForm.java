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
 * @since 2022/11/24
 */
@Data
@ApiModel("采购入库主参数")
public class PurchaseInStockForm {

    @ApiModelProperty("采购组织(必填)")
    @NotBlank(message = "采购组织(必填)不能为空")
    private String purchaseOrgId;

    @ApiModelProperty("源单编号")
    private String srcBillNo;

    @ApiModelProperty("送货单号")
    private String deliveryBillNo;

    @ApiModelProperty("供应商编码")
    private String supplierNo;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("创建日期")
    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "创建日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String createDate;

    @ApiModelProperty("收料组织(必填项)")
    @NotBlank(message = "收料组织(必填项)不能为空")
    private String stockOrgId;

    @ApiModelProperty("结算组织(必填项)")
    @NotBlank(message = "结算组织(必填项)不能为空")
    private String settleOrgId;

    @ApiModelProperty("入库日期 (必填项)")
    @NotBlank(message = "入库日期 (必填项)不能为空")
    @DateTime(format = "yyyy-MM-dd HH:mm:ss", message = "入库日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss")
    private String inStockDate;

    @ApiModelProperty("单据类型(必填项)")
    @NotBlank(message = "单据类型(必填项)不能为空")
    private String billTypeID;

    @ApiModelProperty("货主类型(必填项)")
    @NotBlank(message = "货主类型(必填项)不能为空")
    private String ownerTypeIdHead;

    @ApiModelProperty("货主(必填项)")
    @NotBlank(message = "货主(必填项)不能为空")
    private String ownerIdHead;

    @ApiModelProperty("采购入库明细")
    @NotEmpty(message = "采购入库明细不能为空")
    @Valid
    private List<InStockDetail> inStockDetails;

    @Data
    @ApiModel("采购入库明细参数")
    public static class InStockDetail {

        @ApiModelProperty("物料.物料编码(必填项)")
        @NotBlank(message = "物料.物料编码(必填项)不能为空")
        private String materialId;

        @ApiModelProperty("物料说明")
        private String materialDesc;

        @ApiModelProperty("库存单位")
        private String unitID;

        @ApiModelProperty("入库数量")
        @NotNull(message = "入库数量不能为空")
        private BigDecimal realQty;

        @ApiModelProperty("仓库ID")
        private String stockId;

        @ApiModelProperty("批号")
        private String flot;

        @ApiModelProperty("备注")
        private String note;

        @ApiModelProperty("计价单位 (必填项)")
        @NotBlank(message = "计价单位 (必填项)不能为空")
        private String priceUnitID;

        @ApiModelProperty("货主类型(必填项)")
        @NotBlank(message = "货主类型(必填项)不能为空")
        private String ownerTypeId = "BD_OwnerOrg";

        @ApiModelProperty("货主(必填项)")
        @NotBlank(message = "货主(必填项)不能为空")
        private String ownerId;

        @ApiModelProperty("采购单位 (必填项)")
        @NotBlank(message = "采购单位(必填项)不能为空")
        private String remainInStockUnitId;

        @ApiModelProperty("收料通知更新明细")
        @Valid
        private List<InStockLink> inStockLinks;
    }

    @Data
    @ApiModel("收料通知更新明细参数")
    public static class InStockLink {

        @ApiModelProperty("收货通知内码")
        @NotBlank(message = "收货通知内码不能为空")
        private String srcBillId;

        @ApiModelProperty(" 收货通知明细内码")
        @NotBlank(message = "收货通知明细内码不能为空")
        private String srcId;

        @ApiModelProperty(" 原始携带量")
        @NotNull(message = "原始携带量不能为空")
        private BigDecimal remainQtyOld;

        @ApiModelProperty("修改携带量")
        @NotNull(message = "修改携带量不能为空")
        private BigDecimal remainQty;

    }

}
