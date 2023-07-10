package cn.getech.wms.api.controller;

import cn.getech.wms.api.dto.*;
import cn.getech.wms.api.enums.FormIDEnum;
import cn.getech.wms.api.form.*;
import cn.getech.wms.api.service.KingdeeService;
import cn.getech.wms.api.util.ResultVOUtil;
import com.kingdee.bos.webapi.entity.QueryParam;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 应收单控制器
 */
@RestController
@Slf4j
@RequestMapping("/v1/kingdee")
@Api(tags="K3Cloud对接")
@CrossOrigin
public class KingdeeController {

    @Autowired
   private KingdeeService service;

    @Autowired
    private K3CloudApi k3CloudApi;

//    @Autowired
//    private IWmsLogService iWmsLogService;

//    @GetMapping("/view/test")
//    @ApiOperation(value="通用查询测试", notes="通用查询测试")
//    public ResultVO view(String formId,String number) throws Exception {
//        String  data = "{\"CreateOrgId\": 0,\"Number\": \""+number+"\",\"Id\": \"\",\"IsSortBySeq\": \"false\"}";
//        String result = k3CloudApi.view(formId, data);
//        return ResultVOUtil.success(result);
//    }

//    @GetMapping("/get/supplier")
//    @ApiOperation(value="供应商-查询", notes="供应商-查询")
//    public ResultVO getSupplier(String supplierNo) throws Exception {
//        Supplier supplier = service.getSupplier(supplierNo);
//        return ResultVOUtil.success(supplier);
//    }


    /**
     * 测试参数
     {
     "billTypeID": "RKD01_SYS",
     "createDate": "2022-11-25 09:00:00",
     "createUser": "admin",
     "deliveryBillNo": "2121212",
     "inStockDate": "2022-11-25 09:00:00",
     "inStockDetails": [
     {
     "materialId": "112.13.53001",
     "note": "111",
     "ownerId": "10001",
     "ownerTypeId": "BD_OwnerOrg",
     "priceUnitID": "kg",
     "realQty": 1,
     "remainInStockUnitId": "根",
     "stockId": "101001",
     "unitID": "根",
     "inStockLinks": [
     {
     "remainQty": 1,
     "remainQtyOld": 1,
     "srcBillId": "101387",
     "srcId": "103111"
     }
     ]
     }
     ],
     "ownerIdHead": "10001",
     "ownerTypeIdHead": "BD_OwnerOrg",
     "purchaseOrgId": "10001",
     "settleOrgId": "10001",
     "srcBillNo": "111",
     "stockOrgId": "10001",
     "supplierName": "0001",
     "supplierNo": "0001"
     }
     * @param form
     * @return
     * @throws Exception
     */
    @PostMapping("/save/purchaseInStock")
    @ApiOperation(value="采购入库新增并回写", notes="采购入库新增并回写<br/> 测试参数：<br/> {<br/>" +
            "     \"billTypeID\": \"RKD01_SYS\",<br/>" +
            "     \"createDate\": \"2022-11-25 09:00:00\",<br/>" +
            "     \"createUser\": \"admin\",<br/>" +
            "     \"deliveryBillNo\": \"2121212\",<br/>" +
            "     \"inStockDate\": \"2022-11-25 09:00:00\",<br/>" +
            "     \"inStockDetails\": [<br/>" +
            "     {<br/>" +
            "     \"materialId\": \"112.13.53001\",<br/>" +
            "     \"note\": \"111\",<br/>" +
            "     \"ownerId\": \"10001\",<br/>" +
            "     \"ownerTypeId\": \"BD_OwnerOrg\",<br/>" +
            "     \"priceUnitID\": \"kg\",<br/>" +
            "     \"realQty\": 1,<br/>" +
            "     \"remainInStockUnitId\": \"根\",<br/>" +
            "     \"stockId\": \"101001\",<br/>" +
            "     \"unitID\": \"根\",<br/>" +
            "     \"inStockLinks\": [<br/>" +
            "     {<br/>" +
            "     \"remainQty\": 1,<br/>" +
            "     \"remainQtyOld\": 1,<br/>" +
            "     \"srcBillId\": \"101387\",<br/>" +
            "     \"srcId\": \"103111\"<br/>" +
            "     }<br/>" +
            "     ]<br/>" +
            "     }<br/>" +
            "     ],<br/>" +
            "     \"ownerIdHead\": \"10001\",<br/>" +
            "     \"ownerTypeIdHead\": \"BD_OwnerOrg\",<br/>" +
            "     \"purchaseOrgId\": \"10001\",<br/>" +
            "     \"settleOrgId\": \"10001\",<br/>" +
            "     \"srcBillNo\": \"111\",<br/>" +
            "     \"stockOrgId\": \"10001\",<br/>" +
            "     \"supplierName\": \"0001\",<br/>" +
            "     \"supplierNo\": \"0001\"<br/>" +
            "     }<br/>" +
            "     成功示例：<br/>" +
            "{<br/>" +
            "  \"code\": 0,<br/>" +
            "  \"msg\": \"成功\",<br/>" +
            "  \"data\": {<br/>" +
            "    \"errCode\": null,<br/>" +
            "    \"errors\": null,<br/>" +
            "    \"successEntities\": [<br/>" +
            "      {<br/>" +
            "        \"id\": \"106673\",<br/>" +
            "        \"number\": \"CGRK06538\",<br/>" +
            "        \"index\": 0,<br/>" +
            "        \"billNo\": null<br/>" +
            "      }<br/>" +
            "    ]<br/>" +
            "  }<br/>" +
            "}" +
            "     失败示例:<br/>" +
            "   {<br/>" +
            "  \"code\": 27,<br/>" +
            "  \"msg\": \"保存失败\",<br/>" +
            "  \"data\": {<br/>" +
            "    \"errCode\": \"500\",<br/>" +
            "    \"errors\": [<br/>" +
            "      {<br/>" +
            "        \"fieldName\": null,<br/>" +
            "        \"message\": \"ResolveFiled_InnerEx解析字段(Key:FOwnerIdHead,name:货主)时发生异常,异常信息:多类别基础资料实际指向基础资料不明！\"<br/>" +
            "        \"index\": 0<br/>" +
            "      }<br/>" +
            "    ],<br/>" +
            "    \"successEntities\": null<br/>" +
            "  }<br/>" +
            "}" +
            "")
    public ResultVO<K3ResultDTO> savePurchaseInStock(@RequestBody @Valid PurchaseInStockForm form) throws Exception {
        return service.savePurchaseInStock(form);
    }


    @PostMapping("/save/productionDraw")
    @ApiOperation(value="生产领料单新增并回写")
    public ResultVO<K3ResultDTO> saveProinventoryResultsductionDraw(@RequestBody @Valid SaveProductionDrawForm form) throws Exception {
        ResultVO resultVO = service.saveProductionDraw(form);
        return resultVO;
    }
    
    @PostMapping("/save/prdInstock")
    @ApiOperation(value="生产入库单新增并回写", notes="生产入库单新增并回写")
    public ResultVO<K3ResultDTO> savePrdInstock(@RequestBody @Valid PrdInstockWmsForm form) throws Exception {

        ResultVO resultVO = service.savePrdInstock(form);

        return resultVO;
    }

    @PostMapping("/save/prdReturnMtrl")
    @ApiOperation(value="生产退料单新增并回写", notes="生产退料单新增并回写")
    public ResultVO<K3ResultDTO> saveProReturnMtrl(@RequestBody @Valid PrdReturnMtrlForm form) throws Exception {
        ResultVO resultVO = service.savePrdReturnMtrl(form);
        return resultVO;
    }

    /**
     * 其他入库单新增并回写
     * @return ResultVO
     */
    @PostMapping("/save/otherInStock")
    @ApiOperation(value="其他入库单-新增", notes="其他入库单-新增<br/> 测试参数：<br/>{<br/>" +
            "  \"baseCurrId\": \"PRE001\",<br/>" +
            "  \"billTypeId\": \"QTRKD01_SYS\",<br/>" +
            "  \"date\": \"2022-11-24 00:00:00\",<br/>" +
            "  \"ownerIdHead\": \"10001\",<br/>" +
            "  \"ownerTypeIdHead\": \"BD_OwnerOrg\",<br/>" +
            "  \"stockDirect\": \"GENERAL\",<br/>" +
            "  \"stockOrgId\": \"10001\",<br/>" +
            "  \"supplierId\": \"0001\",<br/>" +
            "  \"details\": [<br/>" +
            "    {<br/>" +
            "     <br/>" +
            "      \"keeperId\": \"10001\",<br/>" +
            "      \"keeperTypeId\": \"BD_KeeperOrg\",<br/>" +
            "     <br/>" +
            "      \"materialId\": \"112.13.53001\",<br/>" +
            "      \"ownerId\": \"10001\",<br/>" +
            "      \"ownerTypeId\": \"BD_OwnerOrg\",<br/>" +
            "      \"qty\": 1,<br/>" +
            "      \"stockId\": \"101001\",<br/>" +
            "      \"stockStatusId\": \"KCZT01_SYS\",<br/>" +
            "      \"unitId\": \"根\"<br/>" +
            "    }<br/>" +
            "  ]<br/>" +
            "}")
    public ResultVO<K3ResultDTO> saveOtherInStock(@RequestBody @Valid OtherInStockForm form) throws Exception {
        return  service.saveOtherInStock(form);
    }

    /**
     * 其他出库单新增并回写
     * @return ResultVO
     */
    @PostMapping("/save/otherOutStock")
    @ApiOperation(value="其他出库单-新增", notes="其他出库单-新增<br/> 测试参数：<br/>{<br/>" +
            "  \"baseCurrId\": \"PRE001\",<br/>" +
            "  \"billTypeId\": \"QTCKD01_SYS\",<br/>" +
            "  \"custId\": \"10001\",<br/>" +
            "  \"date\": \"2022-11-24 00:00:00\",<br/>" +
            "  \"ownerIdHead\": \"10001\",<br/>" +
            "  \"ownerTypeIdHead\": \"BD_OwnerOrg\",<br/>" +
            "  \"pickOrgId\": \"10001\",<br/>" +
            "  \"stockDirect\": \"GENERAL\",<br/>" +
            "  \"stockOrgId\": \"10001\",<br/>" +
            "   \"details\": [<br/>" +
            "    {<br/>" +
            "      \"baseUnitId\": \"根\",<br/>" +
            "      \"distribution\": false,<br/>" +
            "      \"keeperId\": \"10001\",<br/>" +
            "      \"keeperTypeId\": \"BD_KeeperOrg\",<br/>" +
            "      \"materialId\": \"112.13.53001\",<br/>" +
            "      \"ownerId\": \"10001\",<br/>" +
            "      \"ownerTypeId\": \"BD_OwnerOrg\",<br/>" +
            "      \"qty\": 1.000,<br/>" +
            "      \"stockId\": \"101001\",<br/>" +
            "      \"stockStatusId\": \"KCZT01_SYS\",<br/>" +
            "      \"unitId\": \"根\"<br/>" +
            "    }<br/>" +
            "  ]<br/>" +
            "}")
    public ResultVO<K3ResultDTO> saveOtherOutStock(@RequestBody @Valid OtherOutStockForm form) throws Exception {
        return  service.saveOtherOutStock(form);
    }

    /**
     * 盘点方案结果回传
     * @return ResultVO
     */
    @PostMapping("/save/inventoryResults")
    @ApiOperation(value="盘点方案结果-新增", notes="盘点方案结果-新增<br/> 测试数据：<br/>{<br/>" +
            " \"id\" : \"100033\",<br/>" +
            "  \"details\": [<br/>" +
            "   {<br/>" +
            "    \"entryId\": \"100026\",<br/>" +
            "    \"countQty\": 1<br/>" +
            "   }<br/>" +
            "  ]<br/>" +
            "}")
    public ResultVO<K3ResultDTO> saveInventoryResults(@RequestBody @Valid InventoryResultsForm form) throws Exception {
        return service.saveInventoryResults(form);
    }

    /**
     {
     "billTypeID": "ZJDB01_SYS",
     "createDate": "2022-11-24 00:00:00",
     "creatorId": "admin",
     "destStockId": "CK017",
     "documentStatus": "A",
     "note": "111",
     "srcBillNo": "111",
     "srcStockId": "101001",
     "stockOrgId": "10001",
     "stockOutOrgId": "10001",
     "transferDetails": [
     {
     "factQty": 1,
     "materialId": "112.13.53001",
     "noteEntry": "121212",
     "unitID": "根"
     }
     ]
     }
     * 调拨单-新增并回写
     * @return ResultVO
     */
    @PostMapping("/save/transfer")
    @ApiOperation(value="调拨单-新增并回写", notes="调拨单-新增并回写<br/>" +
            " 测试参数：<br/> " +
            " {<br/>" +
            "\"billTypeID\": \"ZJDB01_SYS\",<br/>" +
            "\"createDate\": \"2022-11-24 00:00:00\",<br/>" +
            "\"creatorId\": \"admin\",<br/>" +
            "\"destStockId\": \"CK017\",<br/>" +
            "\"documentStatus\": \"A\",<br/>" +
            "\"note\": \"111\",<br/>" +
            "\"srcBillNo\": \"111\",<br/>" +
            "\"srcStockId\": \"101001\",<br/>" +
            "\"stockOrgId\": \"10001\",<br/>" +
            "\"stockOutOrgId\": \"10001\",<br/>" +
            "\"transferDetails\": [<br/>" +
            "{<br/>" +
            "\"factQty\": 1,<br/>" +
            "\"materialId\": \"112.13.53001\",<br/>" +
            "\"noteEntry\": \"121212\",<br/>" +
            "\"unitID\": \"根\",<br/>" +
            "\"transferDetailLinks\":[<br/>" +
            "    {   <br/>" +
            "        \"srcBillId\":\"147695\",<br/>" +
            "        \"srcId\":\"147900\",<br/>" +
            "        \"baseQtyOld\":1,<br/>" +
            "        \"baseQty\":1<br/>" +
            "    }<br/>" +
            "    ]<br/>" +
            "}<br/>" +
            "]<br/>" +
            "}")
    public ResultVO<K3ResultDTO> saveTransfer(@RequestBody @Valid TransferForm form) throws Exception {
        return service.saveTransfer(form);
    }

    @PostMapping("/save/productionSaveTransfer")
    @ApiOperation(value="生产领料/生产退料单走调拨单")
    public ResultVO<K3ResultDTO> productionSaveTransfer(@RequestBody @Valid TransferFormByProduction form) throws Exception {
        ResultVO<K3ResultDTO> k3ResultDTOResultVO = service.productionSaveTransfer(form);
        return k3ResultDTOResultVO;
    }


    /**
     * 保存接口
     * @return ResultVO
     */
//    @PostMapping("/frdUseEntry/test")
//    @ApiOperation(value="用料清单反查-测试", notes="用料清单反查-测试")
//    public ResultVO frdUseEntry() throws Exception {
//
//
//        QueryParam queryParam = new QueryParam();
//        queryParam.setFormId("PLM_STD_DOCUMENT");
//        queryParam.setFieldKeys("Fcode");
//        queryParam.setFilterString("{\"Left\":\"(\",\"FieIdName\":\"FID\",\"Compare\":\"=\",\"Value\":\"509810\",\"Right\":\")\",\"Logic\":\"AND\"}");
//        //调用保存接口
//        List<PDFFileDto> result = null;
//        try {
//            result = k3CloudApi.executeBillQuery(queryParam, PDFFileDto.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//        return  ResultVOUtil.success(result);
//    }


    /**
     * 2303.0333.001
     * @param filedId
     * @return
     * @throws Exception
     */
    @GetMapping("/plm/download/{filedId}/{fileName}")
    @ApiOperation(value="PLM下载", notes="PLM下载")
    public void plmDownload(@PathVariable String filedId,@PathVariable String fileName, HttpServletResponse res) throws Exception {
        service.plmDownload(filedId,fileName,res);

    }

    /**
     * 2303.0333.001
     * @param materialCode
     * @return
     * @throws Exception
     */
    @GetMapping("/plm/list/documents")
    @ApiOperation(value="PLM查询文档", notes="PLM查询文档")
    public ResultVO<List<K3PlmDocumentDTO>> listDocuments(String materialCode) throws Exception {
        return ResultVOUtil.success(service.listDocuments(materialCode));
    }



}
