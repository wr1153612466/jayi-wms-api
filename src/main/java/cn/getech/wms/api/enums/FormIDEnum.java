package cn.getech.wms.api.enums;

import lombok.Getter;

@Getter
public enum FormIDEnum {

    MATERIAL("BD_MATERIAL"),//物料
    OUT_STOCK("SAL_OUTSTOCK"),//出库单
    STOCK("BD_STOCK"),//仓库
    CUSTOMER("BD_Customer"),//客户
    PAYABLE("AP_Payable"),//应付单PAYABLE
    RECEIVABLE("AR_receivable"),//应收单
    ORGANIZATION("ORG_Organizations"),//组织架构
    DEPARTMENT("BD_Department"),//部门
    EXPENSE("BD_Expense"),//费用
    SUPPLIER("BD_Supplier"),//供应商
    ASSISTANT("BOS_ASSISTANTDATA_DETAIL"),//数据类型（辅助属性值）
    CURRENCY("BD_Currency"),//币别
    PURCHASE_INSTOCK("STK_Instock"),//采购入库
    STK_STOCK_COUNT_INPUT("STK_StockCountInput"),//盘点作业
    STK_MISCELLANEOUS("STK_MISCELLANEOUS"),//其他入库单
    STK_MISDELIVERY("STK_MisDelivery"),//其他出库单
    PRD_MO("PRD_MO", "生产订单"),
    PRD_PICKMTRL("PRD_PickMtrl","生产领料"),
    PRD_INSTOCK("PRD_INSTOCK","生产入库"),
    PRD_RETURN_MTRL("PRD_ReturnMtrl","生产退料"),
    PUR_RECEIVE_BILL("PUR_ReceiveBill"),//收料通知
    STK_TRANSFER_DIRECT("STK_TransferDirect"),//直接调拨
    PRD_PPBOM("PRD_PPBOM"),//用料清单
    PLM_CFG_BASE("PLM_CFG_BASE"),//PDM基础对象
    PLM_CFG_RELATEDOBJECT("PLM_CFG_RELATEDOBJECT"),
    PLM_STD_DOCUMENT("PLM_STD_DOCUMENT"),
    ;


    private String formid;

    private String remake;


    FormIDEnum(String formid) {
        this.formid = formid;
    }

    FormIDEnum(String formid, String remake) {
        this.formid = formid;
        this.remake = remake;
    }


}
