package cn.getech.wms.api.service;

import cn.getech.wms.api.dto.*;
import cn.getech.wms.api.form.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 金蝶接口服务
 */
public interface KingdeeService {

    /**
     * 列表查询数据
     *
     * @param filterString 过滤条件
     * @param clz
     * @param k3Entity
     * @return
     */
    <T> ResultVO<List<T>> query(String filterString, Class<T> clz, K3Entity k3Entity);

    Supplier getSupplier(String supplierNo);

    ResultVO savePurchaseInStock(PurchaseInStockForm form);


    ResultVO saveOtherInStock(OtherInStockForm form);

    ResultVO saveOtherOutStock(OtherOutStockForm form);

    ResultVO saveInventoryResults(InventoryResultsForm form);

    //生产领料
    ResultVO saveProductionDraw(SaveProductionDrawForm form);
    //生产入库
    ResultVO savePrdInstock(PrdInstockWmsForm form);
    //生产退料
    ResultVO savePrdReturnMtrl(PrdReturnMtrlForm form);

    ResultVO saveTransfer(TransferForm form);

    void plmDownload(String filedId, String fileName, HttpServletResponse res);

    List<PrdUseEntry> listByK3PrdPpbomBy(String FSBillId);

    List<PrdMoEntry> listByMos(Collection<String> mos);

    PrdMoEntry getByMo(String mo);

    List<K3PlmDocumentDTO> listDocuments(String materialCode);

    ResultVO<K3ResultDTO> productionSaveTransfer(TransferFormByProduction form);

    List documentsByPdf(List<String> fids);
}
