package cn.getech.wms.api.util;

/**
 * 用于库存历史信息storage_history表的remark字段统一生成数据
 *  <li>一般情况下，Origin字段只记录操作单号信息
 *  <li>remark字段记录Origin字段对应订单的操作过程描述
 * @author william
 * @Date: 2020-04-17 16:33
 */
public class HistoryOriginUtis {
    /**
     * 操作描述语句
     */
    final static String DESCRIPTION_ADJUST_ON_SHELVE = "调整上架-操作凭据:";
    final static String DESCRIPTION_ADJUST_DOWN_SHELVE = "调整下架-操作凭据:";

    /**
     * 调整上架
     *
     * @return
     */
    public static String adjustOnShelve(String opVoucher) {
        return DESCRIPTION_ADJUST_ON_SHELVE + opVoucher;
    }

    /**
     * 调整下架
     *
     * @return
     */
    public static String adjustDownShelve() {
        return DESCRIPTION_ADJUST_DOWN_SHELVE;
    }
}
