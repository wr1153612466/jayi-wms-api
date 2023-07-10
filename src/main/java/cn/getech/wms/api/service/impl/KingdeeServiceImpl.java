package cn.getech.wms.api.service.impl;

import ch.qos.logback.core.util.CloseUtil;
import cn.getech.wms.api.config.K3IdentifyConfig;
import cn.getech.wms.api.dto.*;
import cn.getech.wms.api.entity.WmsLog;
import cn.getech.wms.api.enums.FormIDEnum;
import cn.getech.wms.api.enums.KingdeeResultEnum;
import cn.getech.wms.api.enums.ResultEnum;
import cn.getech.wms.api.exception.ApiException;
import cn.getech.wms.api.exception.Asserts;
import cn.getech.wms.api.form.*;
import cn.getech.wms.api.service.BaseService;
import cn.getech.wms.api.service.KingdeeService;
import cn.getech.wms.api.util.JsonUtil;
import cn.getech.wms.api.util.ResultVOUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.kingdee.bos.webapi.entity.*;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.kingdee.bos.webapi.utils.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 金蝶物料接口实现类
 */
@Service
@Slf4j
public class KingdeeServiceImpl implements KingdeeService {

    @Autowired
    private BaseService baseService;
    @Autowired
    private WmsLogServiceImpl wmsLogService;
    @Autowired
    private K3CloudApi k3CloudApi;

    @Autowired
    private K3IdentifyConfig k3IdentifyConfig;

    /**
     * 列表查询数据
     * @param filterString  过滤条件
     * @param clz
     * @param k3Entity
     * @return
     */
    @Override
    public <T> ResultVO<List<T>>  query(String filterString, Class<T> clz, K3Entity k3Entity) {
        String[] fieldKeys = k3Entity.value();

        String formId = k3Entity.formId().getFormid();
        QueryParam queryParam = new QueryParam();
        queryParam.setFieldKeys(String.join(",",fieldKeys));
        queryParam.setFormId(formId);
        queryParam.setFilterString(filterString);
        try {
            List<T> list = k3CloudApi.executeBillQuery(queryParam, clz);
            return ResultVOUtil.success(list);
        } catch (Exception e) {
            log.error("查询异常:"+e);
            return ResultVOUtil.error(1,e.getMessage());
        }
    }

    @Override
    public Supplier getSupplier(String supplierNo) {
        return baseService.get(supplierNo, Supplier.class).get();
    }

    @Override
    public ResultVO savePurchaseInStock(PurchaseInStockForm form) {
        return save(FormIDEnum.PURCHASE_INSTOCK,wrap2StkInStockEntity(form));
    }

    @Override
    public ResultVO saveOtherInStock(OtherInStockForm form) {
        return save(FormIDEnum.STK_MISCELLANEOUS,wrap2OtherInStockEntity(form));
    }

    @Override
    public ResultVO saveOtherOutStock(OtherOutStockForm form) {
        return save(FormIDEnum.STK_MISDELIVERY,wrap2OtherOutStockEntity(form));
    }

    @Override
    public ResultVO saveInventoryResults(InventoryResultsForm form) {
        return save(FormIDEnum.STK_STOCK_COUNT_INPUT,wrap2InventoryResultsEntity(form));
    }

    @Override
    public ResultVO saveProductionDraw(SaveProductionDrawForm form) {
        PrdPickmtrlEntity entity = toK3ProductionDrawParam(form);
        return save(FormIDEnum.PRD_PICKMTRL,entity);
    }

    @Override
    public ResultVO savePrdInstock(PrdInstockWmsForm form) {
        PrdInstockEntity entity = toPrdInstockEntity(form);
        return save(FormIDEnum.PRD_INSTOCK,entity);
//        return new ResultVO();
    }

    private PrdInstockEntity toPrdInstockEntity(PrdInstockWmsForm form) {
        PrdInstockEntity entity = new PrdInstockEntity();

        Set<String> mos = form.getDetails().stream().map(e -> e.getSrcbillNo()).collect(Collectors.toSet());
        //反查
        List<PrdMoEntry> prdMoEntries = listByMos(mos);
        for (PrdMoEntry prdMoEntry : prdMoEntries) {
            prdMoEntry.setFID(subPoint(prdMoEntry.getFID()));
            prdMoEntry.setFTreeEntity_FEntryId(subPoint(prdMoEntry.getFTreeEntity_FEntryId()));
        }
        if (CollectionUtil.isEmpty(prdMoEntries)) {
            throw new ApiException("生产订单"+mos.toString()+"不存在");
        }

//        Map<String, Map<String, PrdMoEntry>> moMcodeGroup = prdMoEntries.stream().collect(Collectors.groupingBy(PrdMoEntry::getFBillNo, Collectors.toMap(e -> e.getFMaterialId_FNumber(), e -> e)));
        Map<String, Map<String, PrdMoEntry>> moMcodeGroup = prdMoEntries.stream().collect(Collectors.groupingBy(PrdMoEntry::getFBillNo, Collectors.toMap(e -> e.getFTreeEntity_FEntryId(), e -> e)));

        entity.setFBillNo(form.getFbillNo());//生产入库单单号
        entity.setFID("0");
        entity.setFBillType(new K3BaseEntity("SCRKD02_SYS"));
        entity.setFDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        entity.setFPrdOrgId(new K3BaseEntity(form.getOrganization()));//生产组织
        entity.setFStockOrgId(new K3BaseEntity(form.getOrganization()));//入库组织代码
        entity.setFOwnerTypeId0("BD_OwnerOrg");//货主类型
        entity.setFOwnerId0(new K3BaseEntity(form.getFactoryId()));//货主
        entity.setFIsEntrust(false);
        entity.setFDescription(form.getRemake());//备注


        List<PrdInstockEntity.Detail> collect = form.getDetails().stream().map(e -> {
            PrdInstockEntity.Detail detail = new PrdInstockEntity.Detail();
            detail.setFIsNew(false);

            PrdMoEntry prdMoEntry = moMcodeGroup.get(e.getSrcbillNo()).get(e.getMoFsId());
            if (prdMoEntry == null) {
                throw new ApiException("生产订单["+e.getSrcbillNo()+"]下分录内码["+e.getMoFsId()+"]不存在");
            }
            detail.setFSrcEntryId(prdMoEntry.getFTreeEntity_FEntryId());//源单分录内码
            detail.setFEntryID("0");
            detail.setFIsFinished(e.getFisFinished());//完工
            detail.setFISBACKFLUSH(true);//倒冲领料
            detail.setFMATERIALID(new K3BaseEntity(e.getMcode()));//物料代码
            detail.setFMoBillNo(e.getSrcbillNo());//生产订单编号
            detail.setFMoEntrySeq(subPoint(prdMoEntry.getFTreeEntity_FSeq()));//生产订单行号
            detail.setFMoId(prdMoEntry.getFID());
            detail.setFProductType(prdMoEntry.getFProductType());//产品类型
            detail.setFWorkShopId1(new K3BaseEntity(prdMoEntry.getFWorkShopID_FNumber()));//明细车间
            detail.setFMoEntryId(prdMoEntry.getFTreeEntity_FEntryId());//生产订单分录内码
            detail.setFSrcBillType("PRD_MO");//源单类型
            detail.setFSrcBillNo(e.getSrcbillNo());//源单单号

            detail.setFSrcEntrySeq(detail.getFMoEntrySeq());//源单行号(生产订单行号)
            detail.setFReqSrc(prdMoEntry.getFReqSrc());//需求来源
            detail.setFReqBillNo(prdMoEntry.getFSaleOrderNo());//需求单据(销售定单号)
            detail.setFReqEntrySeq(subPoint(prdMoEntry.getFSaleOrderEntrySeq()));//需求单据行号


            detail.setFMEMO(null);//备注
            detail.setFUnitID(new K3BaseEntity(prdMoEntry.getFUnitId_FNumber()));//单位
            detail.setFLot(new K3BaseEntity(e.getBatch()));//批次
//            detail.setFBomId(new K3BaseEntity(prdMoEntry.getFBomId_FNUMBER()));//MOM版本
            detail.setFMustQty(e.getQty());//应收数量
            detail.setFRealQty(e.getQty());//实收数量
            detail.setFBaseMustQty(e.getQty());//应收数量
            detail.setFBaseRealQty(e.getQty());
            detail.setFBaseUnitId(new K3BaseEntity(prdMoEntry.getFUnitId_FNumber()));//基本单位
            detail.setFOwnerTypeId("BD_OwnerOrg");
            detail.setFOwnerId(new K3BaseEntity(form.getFactoryId()));//货主
            detail.setFStockId(new K3BaseEntity(e.getStockNo()));//仓库编码
            detail.setFStockUnitId(new K3BaseEntity(prdMoEntry.getFUnitId_FNumber()));//库存单位
            detail.setFStockStatusId(new K3BaseEntity("KCZT01_SYS"));//库存状态
            detail.setFKeeperTypeId("BD_KeeperOrg");//保管者类型
            detail.setFKeeperId(new K3BaseEntity(form.getFactoryId()));//保管者
            detail.setFMOMAINENTRYID(prdMoEntry.getFTreeEntity_FEntryId());//生产订单主产品分录内码
            detail.setF_RVMM_WORKSHOPID(new K3BaseEntity(prdMoEntry.getFWorkShopID_FNumber()));

            PrdInstockEntity.Link link = new PrdInstockEntity.Link();
            link.setFEntity_Link_FFlowId("f11b462a-8733-40bd-8f29-0906afc6a201");
            link.setFEntity_Link_FFlowLineId("");
            link.setFEntity_Link_FRuleId("PRD_MO2INSTOCK");
            link.setFEntity_Link_FSTableName("T_PRD_MOENTRY");
            link.setFEntity_Link_FSBillId(prdMoEntry.getFID());//源单内码
            link.setFEntity_Link_FSId(prdMoEntry.getFTreeEntity_FEntryId());//源单分录内码
            link.setFEntity_Link_FBasePrdRealQtyOld(e.getQty());
            link.setFEntity_Link_FBasePrdRealQty(e.getQty());


            detail.setFEntity_Link(Lists.newArrayList(link));

            return detail;
        }).collect(Collectors.toList());
        entity.setFEntity(collect);

        return entity;
    }

    @Override
    public ResultVO savePrdReturnMtrl(PrdReturnMtrlForm form) {
        PrdReturnMtrlEntity prdReturnMtrlEntity = wrap2PrdReturnMtrl(form);
        return save(FormIDEnum.PRD_RETURN_MTRL, prdReturnMtrlEntity);
    }

    private PrdReturnMtrlEntity wrap2PrdReturnMtrl(PrdReturnMtrlForm form) {
        PrdReturnMtrlEntity entity = new PrdReturnMtrlEntity();
        entity.setFID(0);
        entity.setFBillNo(form.getFbillNo());
        entity.setFBillType(new K3BaseEntity("SCTLD01_SYS"));
        entity.setFDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        entity.setFStockOrgId(new K3BaseEntity(form.getOrganization()));//收料组织编码
        entity.setFPrdOrgId(new K3BaseEntity(form.getOrganization()));//生产组织编码
        entity.setFOwnerTypeId0("BD_OwnerOrg");
        entity.setFIsCrossTrade(false);
        entity.setFVmiBusiness(false);
        entity.setFIsOwnerTInclOrg(false);

        Set<String> mos = form.getDetails().stream().map(e -> e.getSrcbillno()).collect(Collectors.toSet());
        //反查
        List<PrdMoEntry> prdMoEntries = listByMos(mos);
        for (PrdMoEntry prdMoEntry : prdMoEntries) {
            prdMoEntry.setFID(subPoint(prdMoEntry.getFID()));
            prdMoEntry.setFTreeEntity_FEntryId(subPoint(prdMoEntry.getFTreeEntity_FEntryId()));
        }
        if (CollectionUtil.isEmpty(prdMoEntries)) {
            throw new ApiException("生产订单"+mos.toString()+"不存在");
        }

        Map<String, Map<String, PrdMoEntry>> moMcodeGroup = prdMoEntries.stream().collect(Collectors.groupingBy(PrdMoEntry::getFBillNo, Collectors.toMap(e -> e.getFTreeEntity_FEntryId(), e -> e)));

        List<PrdReturnMtrlFEntity> fEntities = form.getDetails().stream().map(e -> {
            PrdReturnMtrlFEntity fentity = new PrdReturnMtrlFEntity();

            PrdMoEntry prdMoEntry = moMcodeGroup.get(e.getSrcbillno()).get(e.getMoFsId());
            if (prdMoEntry == null) {
                throw new ApiException("生产订单["+e.getSrcbillno()+"]下分录内码["+e.getMoFsId()+"]不存在");
            }
            fentity.setFMaterialId(new K3BaseEntity(e.getMcode()));//物料编码
            fentity.setFUnitID(new K3BaseEntity(e.getUnit()));//单位
            fentity.setFAPPQty(e.getQty());
            fentity.setFQty(e.getQty());
            fentity.setFReturnType(e.getFreturntype());//退料类型
            fentity.setFReturnReason(new K3BaseEntity(e.getFreturnreason()));//退来原因编码
            fentity.setFStockId(new K3BaseEntity(e.getStockNo()));//仓库
            fentity.setFSrcBillType("PRD_PPBOM");//源单类型
            fentity.setFSrcBillNo(prdMoEntry.getFBillNo());//源单编号--生产订单号
            fentity.setFPPBomBillNo(prdMoEntry.getFBillNo());//用料清单编号--生产订单号
            fentity.setFParentMaterialId(new K3BaseEntity(prdMoEntry.getFMaterialId_FNumber()));//生产订单产品编码
            fentity.setFMoId(prdMoEntry.getFID());//生产订单内码
            fentity.setFReserveType("1");//预留类型
            fentity.setFBASESTOCKQTY(e.getQty());
            fentity.setFMoBillNo(e.getSrcbillno());
            fentity.setFEntryVmiBusiness(false);
            fentity.setFMoEntryId(prdMoEntry.getFTreeEntity_FEntryId());
            fentity.setFIsUpdateQty(false);
            fentity.setFIsOverLegalOrg(false);
            fentity.setFCheckReturnMtrl(false);
            fentity.setFMoEntrySeq(1L);
            fentity.setFStockUnitId(new K3BaseEntity(e.getUnit()));
            fentity.setFStockAppQty(e.getQty());
            fentity.setFStockQty(e.getQty());
            fentity.setFStockStatusId(new K3BaseEntity("KCZT01_SYS"));
            fentity.setFKeeperTypeId("BD_KeeperOrg");
            fentity.setFKeeperId(new K3BaseEntity(form.getFactoryId()));//保管者,货主
            fentity.setFBaseUnitId(new K3BaseEntity(e.getUnit()));//基本单位编码
            fentity.setFBaseAppQty(e.getQty());
            fentity.setFBaseQty(e.getQty());
            fentity.setFOwnerTypeId("BD_OwnerOrg");
            fentity.setFOwnerId(new K3BaseEntity(form.getFactoryId()));
            fentity.setFEntrySrcEnteryId(prdMoEntry.getFTreeEntity_FEntryId());
            fentity.setFWorkShopId1(new K3BaseEntity(prdMoEntry.getFWorkShopID_FNumber()));//车间
            fentity.setFParentOwnerTypeId("BD_OwnerOrg");//产品货主类型
            fentity.setFParentOwnerId(new K3BaseEntity(form.getFactoryId()));//产品货主编码
            fentity.setFLot(new K3BaseEntity(e.getBatch()));//批号

            return fentity;
        }).collect(Collectors.toList());

        entity.setFEntity(fEntities);
        return entity;
    }

    @Override
    public ResultVO saveTransfer(TransferForm form) {
        return save(FormIDEnum.STK_TRANSFER_DIRECT,wrap2StkTransferEntry(form));
    }

    @Override
    public void plmDownload(String filedId, String fileName, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        String u = k3IdentifyConfig.getWarehouseUrl() + "Download";
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            String token = getToken();
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setRequestProperty("Content-Type","application/pdf");
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("FileID", Base64Utils.encodingToBase64(filedId.getBytes()));
            conn.setRequestProperty("CTX", Base64Utils.encodingToBase64(("LoginUrl="+k3IdentifyConfig.getServerUrl()+"&UserToken="+token+"").getBytes()));
            conn.setRequestProperty("token", token);
            conn.setRequestProperty("PLM_ACCESS_TYPE", "pure");
            conn.setDoOutput(true);
            inputStream = conn.getInputStream();
            response.reset();
            //response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","filename="+ URLEncoder.encode(fileName,"UTF-8"));
            byte[] buffer = new byte[1024];
            int len;
            outputStream = response.getOutputStream();
            while ((len = inputStream.read(buffer)) > 0){
                outputStream.write(buffer,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            ResultVO error = ResultVOUtil.error(500, u+":"+e.getMessage());
            out.write(JSONUtil.toJsonStr(error));
            out.flush();
            out.close();


        }finally {
            //关闭流
            CloseUtil.closeQuietly(inputStream);
            CloseUtil.closeQuietly(outputStream);
        }
    }

    @Override
    public List<PrdUseEntry> listByK3PrdPpbomBy(String FSBillNo) {
        QueryParam queryParam = new QueryParam();
        queryParam.setFormId(FormIDEnum.PRD_PPBOM.getFormid());
        queryParam.setFieldKeys(
                "FID," +
                        "FBillNo," +
                        "FMoId," +
                        "FMOBillNO," +
                        "FWorkshopID," +
                        "FMoEntrySeq," +
                        "FMOEntryID," +
                        "FBOMID," +
                        "FMaterialID.FNUMBER," +
                        "FEntity_FEntryID," +
                        "FMaterialID2.FNumber," + "FUnitID2.FNumber"
        );
        queryParam.setFilterString("FBillNo='"+FSBillNo+"'");
        //调用保存接口
        List<PrdUseEntry> result = null;
        try {
            result = k3CloudApi.executeBillQuery(queryParam, PrdUseEntry.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public PrdMoEntry getByMo(String mo) {
        List<PrdMoEntry> prdMoEntries = listByMos(Lists.newArrayList(mo));
        PrdMoEntry prdMoEntry = prdMoEntries.get(0);
        return prdMoEntry;
    }


    @Override
    public List<PrdMoEntry> listByMos(Collection<String> mos) {
        QueryParam queryParam = new QueryParam();
        queryParam.setFormId(FormIDEnum.PRD_MO.getFormid());//FormId对应一种单据类型
        //
        queryParam.setFieldKeys(
                "FID,FBillNo,FTreeEntity_FEntryId," +
                        "FMaterialId.FNumber,FUnitId.FNumber,FWorkShopID0.FNumber,FWorkShopID.FNumber," +
                        "FProductType,FTreeEntity_FSeq,FReqSrc,FSaleOrderNo,FSaleOrderEntrySeq," +
                        "F_RVMM_WorkShopID.FNUMBER,F_RVMM_WORKSHOPID,FBomId.FNUMBER"
        );

        String collect = mos.stream().map(e -> "'" + e + "'").collect(Collectors.joining(","));

        queryParam.setFilterString("FBillNo in (" + collect + ")");
        //注意实体的属性位置要和字段位置前后顺序要一致
        List<PrdMoEntry> result = null;
        try {
            result = k3CloudApi.executeBillQuery(queryParam, PrdMoEntry.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }



    @Override
    public List<K3PlmDocumentDTO> listDocuments(String materialCode) {
        //物料内码查询
        Optional<PlmCfgBase> plmCfgBases = baseService.get(materialCode,PlmCfgBase.class);
        if(!plmCfgBases.isPresent()){
            return Collections.emptyList();
        }
//        //通过物料ID获取文档ID
        List<PlmCfgRelatedObject> plmCfgRelatedObjects = baseService.query(formatK3Id(plmCfgBases.get().getFID()), PlmCfgRelatedObject.class);
        if(CollectionUtil.isEmpty(plmCfgRelatedObjects)){
            return Collections.emptyList();
        }
        List<PlmStdDocument> documents = baseService.queryIn(Lists.transform(plmCfgRelatedObjects,e->formatK3Id(e.getFID())), PlmStdDocument.class);
//        //根据文档id获取文档物理文件id
        if(CollectionUtil.isEmpty(documents)){
            return Collections.emptyList();
        }

        List<K3PlmDocumentDTO> plmDocuments = documents.stream().map(e -> {
            K3PlmDocumentDTO documentDTO = new K3PlmDocumentDTO();
            documentDTO.setFileId(e.getFFileId());

            String fname = e.getFNAME();
            String pdfName = fname.substring(0, fname.lastIndexOf(".")) + ".pdf";
            documentDTO.setPdfDownloadUrl(k3IdentifyConfig.getPlmDownLoadUrl()+ e.getFRELEVANTOBJECT()+"/"+pdfName.replace("#",""));

            documentDTO.setFileName(e.getFNAME());
            try {
                documentDTO.setDownloadUrl(k3IdentifyConfig.getPlmDownLoadUrl()+ e.getFFileId()+"/"+e.getFNAME().replace("#",""));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return documentDTO;
        }).collect(Collectors.toList());
        return plmDocuments;
    }

    @Override
    public ResultVO<K3ResultDTO> productionSaveTransfer(TransferFormByProduction form) {
        StkTransferEntry stkTransferEntry = wrap2StkTransferEntryByProduction(form);
        return save(FormIDEnum.STK_TRANSFER_DIRECT,stkTransferEntry);
    }

    @Override
    public List documentsByPdf(List<String> fids) {
        List<PlmStdDocument> documents = baseService.queryIn(fids, PlmStdDocument.class);
        Map<String, PlmStdDocument> collect = documents.stream().filter(e -> !e.getFRELEVANTOBJECT().equals(e.getFFileId())).collect(Collectors.toMap(e -> e.getFCODE(), e -> e));
        return null;
    }

    private PrdPickmtrlEntity toK3ProductionDrawParam(SaveProductionDrawForm form) {
        PrdPickmtrlEntity entity = new PrdPickmtrlEntity();

        //反查
        List<PrdUseEntry> prdUseEntries = listByK3PrdPpbomBy(form.getFsbillNo());
        if (CollectionUtil.isEmpty(prdUseEntries)) {
            throw new ApiException("用料清单["+form.getFsbillNo()+"]不存在");
        }
        entity.setFBillNo(form.getFbillNo());//领料单号
        entity.setFBillType(ImmutableMap.of("FNUMBER", "SCLLD01_SYS"));
        entity.setFDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//领料日期
        entity.setFStockOrgID(new K3BaseEntity(form.getOrganization()));//库存组织代码
        entity.setFStockId0(new K3BaseEntity(""));//仓库
        entity.setFPrdOrgId(new K3BaseEntity(form.getOrganization()));//生产组织代码
        entity.setFOwnerTypeId0("BD_OwnerOrg");
        entity.setFOwnerId0(new K3BaseEntity(form.getFactoryId()));//货主
        entity.setFDescription(form.getRemake());//备注

        Map<String, PrdUseEntry> collect = prdUseEntries.stream().collect(Collectors.toMap(e -> subPoint(e.getFEntity_FEntryID()), e -> e));
//        Map<String, PrdUseEntry> collect1 = prdUseEntries.stream().collect(Collectors.toMap(e ->e.getFMaterialID2_FNumber() , e -> e));


        List<PrdPickmtrlEntity.Detail> FEntitys = form.getDetails().stream().map(e -> {
            PrdPickmtrlEntity.Detail detail = new PrdPickmtrlEntity.Detail();
            detail.setFEntryID(0);

            PrdUseEntry prdUseEntry = collect.get(e.getFsid());
            if (prdUseEntry == null) {
                throw new ApiException("用料清单编号["+form.getFsbillNo()+"]下分录内码["+e.getFsid()+"]不存在");
            }
            detail.setFParentMaterialId(new K3BaseEntity(prdUseEntry.getFMaterialID_FNUMBER()));//父项物料代码,生产订单明细的产品编码
            detail.setFBaseStockActualQty(e.getQty());//主库存基本单位实发数量,实际拣货数量
            detail.setFMaterialId(new K3BaseEntity(e.getMcode()));//子项物料代码,拣货物料编码
            detail.setFUNITID(new K3BaseEntity(prdUseEntry.getFUnitID2_FNumber()));//单位代码
            detail.setFAppQty(e.getQty());//申请数量
            detail.setFActualQty(e.getQty());//实发数量
            detail.setFStockId(new K3BaseEntity(e.getFstockid()));//仓库代码
            detail.setFLot(new K3BaseEntity(e.getBatch()));//批次
            detail.setFBomId(new K3BaseEntity(subPoint(prdUseEntry.getFBOMID())));//BOM代码
            detail.setFStockStatusId(new K3BaseEntity("KCZT01_SYS"));//库存状态
            detail.setFEntrtyMemo("");//备注
            detail.setFMtoNo("");//计划跟踪号
            detail.setFMoEntrySeq(subPoint(prdUseEntry.getFMoEntrySeq()));//生产订单行号
            detail.setFMoId(subPoint(prdUseEntry.getFMoId()));//生产订单单号
            detail.setFMoBillNo(prdUseEntry.getFMOBillNO());//生产订单单号
            detail.setFMoEntryId(Long.valueOf(subPoint(prdUseEntry.getFMOEntryID())));//生产订单分录内码
            detail.setFOwnerTypeId("BD_OwnerOrg");
            detail.setFStockUnitId(new K3BaseEntity(prdUseEntry.getFUnitID2_FNumber()));//库存单位
            detail.setFBaseUnitID(new K3BaseEntity(prdUseEntry.getFUnitID2_FNumber()));//基本单位
            detail.setFBaseAppQty(e.getQty());//基本单位申请数量
            detail.setFBaseActualQty(e.getQty());//基本单位实发数量
            detail.setFEntryWorkShopId(new K3BaseEntity(""));//车间代码
            detail.setFKeeperTypeId("BD_KeeperOrg");
            detail.setFKeeperID(new K3BaseEntity(form.getFactoryId()));//保管者
            detail.setFOwnerID(new K3BaseEntity(form.getFactoryId()));//货主
            detail.setFSrcBillType("PRD_MO");//源单类型,生产订单formid
            detail.setFSrcBillNo(prdUseEntry.getFMOBillNO());//源单单号,生产订单单号
            detail.setFPPBomEntryId(subPoint(prdUseEntry.getFEntity_FEntryID()));//用料清单分录内码
            detail.setFPPBomBillNo(prdUseEntry.getFBillNo());//用料清单编号
            detail.setFParentOwnerTypeId("BD_OwnerOrg");//产品货主类型
            detail.setFParentOwnerId(new K3BaseEntity(form.getFactoryId()));//产品货主

            PrdPickmtrlEntity.Link link = new PrdPickmtrlEntity.Link();
//            link.setFEntity_Link_FFlowId();
//            link.setFEntity_Link_FFlowLineId();
            link.setFEntity_Link_FSTableName("T_PRD_PPBOMENTRY");
            link.setFEntity_Link_FRuleId("2a041a19-bf28-4a32-8708-c5ba53c01fa2");
            link.setFEntity_Link_FSBillId(subPoint(prdUseEntry.getFID()));//用料清单内码
            link.setFEntity_Link_FSId(subPoint(prdUseEntry.getFEntity_FEntryID()));//用料清单分录内码
            link.setFEntity_Link_FBaseActualQtyOld(e.getQty());//原始携带数
            link.setFEntity_Link_FBaseActualQty(e.getQty());//领料数量

            detail.setFEntity_Link(Lists.newArrayList(link));
            return detail;
        }).collect(Collectors.toList());

        entity.setFEntity(FEntitys);
        return entity;
    }


    private String subPoint(String str) {
        if (str.contains(".")) {
            String substring = str.substring(0, str.indexOf("."));
            return substring;
        }
        return str;
    }

    /**
     * 保存公共方法
     * @param formIDEnum
     * @param model
     * @return
     */
    private ResultVO save(FormIDEnum formIDEnum, Object model) {
        UUID uuid = UUID.randomUUID();
        String formId = formIDEnum.getFormid();
        try {
            SaveParam saveParam = new SaveParam(model);
            log.info("请求kingdee[{}]formId:[{}]参数:{}",uuid,formId,saveParam.toJson());

            Long logId = wmsLogService.saveLog(formId, saveParam.toJson());
            SaveResult result = k3CloudApi.save(formId, saveParam);

            log.info("请求kingdee[{}]formId:[{}]结果:{}",uuid,formId,JsonUtil.toJson(result));

            wmsLogService.lambdaUpdate().eq(WmsLog::getId, logId).set(WmsLog::getRemark, JsonUtil.toJson(result)).update();

            if (result.isSuccessfully()) {
                return ResultVOUtil.success(wrap2K3ResultDTO(result)) ;
            }else{
                return ResultVOUtil.errorData(KingdeeResultEnum.SAVE_ERROR,wrap2K3ResultDTO(result)) ;
            }
        } catch (Exception e) {
            log.error("请求kingdee[{}]formId:[{}]异常:"+e+":{}",uuid,formId,e.getMessage());
            e.printStackTrace();
            return ResultVOUtil.error(KingdeeResultEnum.SAVE_ERROR,e.getMessage());
        }
    }


    /**
     * 组装采购单参数
     * @param form
     * @return
     */
    private StkInStockEntry wrap2StkInStockEntity(PurchaseInStockForm form) {
        StkInStockEntry entity = new StkInStockEntry();
        //采购入库单内码ID，唯一值，新增的时候固定填 0 （必填）
        entity.setFID(0);
        //创建人
        entity.setFCreatorId(new K3BaseEntity(form.getCreateUser()));
        //创建时间
        entity.setFCreateDate(form.getCreateDate());
        //单据类型编码，标准采购入库单固定为 RKD01_SYS  （必填）
        entity.setFBillTypeID(new K3BaseEntity(form.getBillTypeID()));
        //单据日期  （必填）
        entity.setFDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //收料部门  其部门编码 （非必填 根据业务逻辑需要）
        entity.setFStockOrgId(new K3BaseEntity(form.getStockOrgId()));
        //收料部门  其部门编码 （非必填 根据业务逻辑需要）
//        entity.setFStockDeptId();
        //需求组织 其组织机构的编码 （非必填）
//        entity.setFStockDeptId();
        //采购部门 其部门编码 （非必填 根据业务逻辑需要）
//        entity.setFPurchaseOrgId();
        //供应商编码 （非必填 根据业务逻辑需要）
        entity.setFSupplierId(new K3BaseEntity(form.getSupplierNo()));
        // 供货方编码  （非必填 根据业务逻辑需要）
//        entity.setFSupplyId();
        //结算方编码（非必填 根据业务逻辑需要）
//        entity.setFSettleId();
        //收款方编码（非必填 根据业务逻辑需要）
//        entity.setFChargeId();
        //货主类型（必填）  固定值 BD_OwnerOrg
        entity.setFOwnerTypeIdHead(form.getOwnerTypeIdHead());
        //货主编码 其组织机构的编码 （必填）
        entity.setFOwnerIdHead(new K3BaseEntity(form.getOwnerIdHead()));
        //财务信息 子单据头（必填）
        entity.setFInStockFin(war2FInStockFin(form));
        entity.setFInStockEntry(Lists.transform(form.getInStockDetails(), e->wrap2FInStockEntry(e)));
        //送货单号
        entity.setFDeliveryBill(form.getDeliveryBillNo());
        return entity;
    }

    /**
     * 组装其它入库单参数
     * @param form
     * @return
     */
    private StkMisCellaneousEntity wrap2OtherInStockEntity(OtherInStockForm form) {
        if(StringUtils.isEmpty(form.getSupplierId())&&StringUtils.isEmpty(form.getDeptId())){
            Asserts.fail(ResultEnum.PARAM_ERROR,"供应商编码与部门编码,两者 必填一项");
        }
        StkMisCellaneousEntity entity = new StkMisCellaneousEntity();
        //单据内码ID，新增保存时固定为0
        entity.setFID(0);
        //单据类型 编码 必填
        entity.setFBillTypeID(new K3BaseEntity(form.getBillTypeId()));
        //库存组织编码 必填
        entity.setFStockOrgId(new K3BaseEntity(form.getStockOrgId()));
        //库存方向 默认 GENERAL 普通
        entity.setFStockDirect(form.getStockDirect());
        //单据日期  （必填）
//        entity.setFDate(form.getDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        entity.setFDate(simpleDateFormat.format(new Date()));
        //供应商编码 与 部门，两者 必填一项
        entity.setFSUPPLIERID(new K3BaseEntity(form.getSupplierId()));
        //部门编码 与供应商 两者，必填一项
        entity.setFDEPTID(new K3BaseEntity(form.getDeptId()));
        //货主类型 必填，明细也有此字段，看实际业务，明细中都一致的，可以填单据头，明细中有不一样的，以明细为主
        entity.setFOwnerTypeIdHead(form.getOwnerTypeIdHead());
        //货主编码
        entity.setFOwnerIdHead(new K3BaseEntity(form.getOwnerIdHead()));
        //本位币编码 必填
        entity.setFBaseCurrId(new K3BaseEntity(form.getBaseCurrId()));
        List<FEntityIn> FEntity = Lists.newArrayList();
        List<OtherInStockForm.Detail> details = form.getDetails();
        for (OtherInStockForm.Detail detail :details){
            FEntityIn fEntityIn = new FEntityIn();
            //入库类型  默认1  合格入库 必填
            fEntityIn.setFInStockType(detail.getInStockType());
            //物料编码  必填
            fEntityIn.setFMATERIALID(new K3BaseEntity(detail.getMaterialId()));
            //单位编码 必填
            fEntityIn.setFUnitID(new K3BaseEntity(detail.getUnitId()));
            //仓库编码 必填
            fEntityIn.setFSTOCKID(new K3BaseEntity(detail.getStockId()));
            //库存状态 默认KCZT01_SYS 可用
            fEntityIn.setFSTOCKSTATUSID(new K3BaseEntity(detail.getStockStatusId()));
            //批号主档编码 启用批号管理的物料，批号必填
            fEntityIn.setFLOT(new K3BaseEntity(detail.getLot()));
            //数量 FUnitID单位对应的数量
            fEntityIn.setFQty(detail.getQty());
            //货主类型 必填
            fEntityIn.setFOWNERTYPEID(detail.getOwnerTypeId());
            //货主编码 必填
            fEntityIn.setFOWNERID(new K3BaseEntity(detail.getOwnerId()));
            //保管者类型
            fEntityIn.setFKEEPERTYPEID(detail.getKeeperTypeId());
            //保管者编码
            fEntityIn.setFKEEPERID(new K3BaseEntity(detail.getKeeperId()));
            FEntity.add(fEntityIn);
        }
        entity.setFEntity(FEntity);
        return entity;
    }

    /**
     * 组装其它出库单参数
     * @param form
     * @return
     */
    private StkMisDeliveryEntity wrap2OtherOutStockEntity(OtherOutStockForm form) {
        if(StringUtils.isEmpty(form.getCustId())&&StringUtils.isEmpty(form.getDeptId())){
            Asserts.fail(ResultEnum.PARAM_ERROR,"客户编码与部门编码 两者必填一项");
        }
        StkMisDeliveryEntity entity = new StkMisDeliveryEntity();
        //单据内码ID 新增时固定为0
        entity.setFID(0);
        //单据类型编码 默认QTCKD01_SYS 必填
        entity.setFBillTypeID(new K3BaseEntity(form.getBillTypeId()));
        //领用组织编码 必填
        entity.setFStockOrgId(new K3BaseEntity(form.getStockOrgId()));
        //领用组织编码 必填
        entity.setFPickOrgId(new K3BaseEntity(form.getPickOrgId()));
        //库存方向 必填 默认 GENERAL 普通
        entity.setFStockDirect(form.getStockDirect());
        //单据日期
        entity.setFDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //客户编码 ；与下面的 部门编码 两者必填一项
        entity.setFCustId(new K3BaseEntity(form.getCustId()));
        //部门编码 ；与上面的 客户编码 两者必填一项
        entity.setFDeptId(new K3BaseEntity(form.getDeptId()));
        //货主类型  必填
        entity.setFOwnerTypeIdHead(form.getOwnerTypeIdHead());
        //货主编码
        entity.setFOwnerIdHead(new K3BaseEntity(form.getOwnerIdHead()));
        //本位币编码 必填
        entity.setFBaseCurrId(new K3BaseEntity(form.getBaseCurrId()));
        List<FEntityOut> FEntity = Lists.newArrayList();
        List<OtherOutStockForm.Detail> details = form.getDetails();
        //其他出库单单据体（多条单据体）
        for (OtherOutStockForm.Detail detail :details){
            FEntityOut fEntityOut = new FEntityOut();
            //物料编码  必填
            fEntityOut.setFMaterialId(new K3BaseEntity(detail.getMaterialId()));
            //单位编码 必填
            fEntityOut.setFUnitID(new K3BaseEntity(detail.getUnitId()));
            //数量 必填
            fEntityOut.setFQty(detail.getQty());
            fEntityOut.setFBaseQty(detail.getQty());
            //基本单位编码 必填
            fEntityOut.setFBaseUnitId(new K3BaseEntity(detail.getBaseUnitId()));
            //仓库编码 必填
            fEntityOut.setFStockId(new K3BaseEntity(detail.getStockId()));
            //批号 启用批号管理的物料 必填
            fEntityOut.setFLot(new K3BaseEntity(detail.getLot()));
            //货主类型
            fEntityOut.setFOwnerTypeId(detail.getOwnerTypeId());
            //货主编码
            fEntityOut.setFOwnerId(new K3BaseEntity(detail.getOwnerId()));
            //库存可用状态
            fEntityOut.setFStockStatusId(new K3BaseEntity(detail.getStockStatusId()));
            //保管者类型
            fEntityOut.setFKeeperTypeId(detail.getKeeperTypeId());
            //参加费用分配 默认 false
            fEntityOut.setFDistribution(detail.getDistribution());
            //保管者编码
            fEntityOut.setFKeeperId(new K3BaseEntity(detail.getKeeperId()));
            FEntity.add(fEntityOut);
        }
        entity.setFEntity(FEntity);
        return entity;
    }

    /**
     * 组装盘点方案回传参数
     * @param form
     * @return
     */
    private StkStockCountInputEntity wrap2InventoryResultsEntity(InventoryResultsForm form) {

        if(StringUtils.isBlank(form.getId())&&StringUtils.isNotBlank(form.getBillNo())){
            //物料内码查询
            List<InventoryCfgBase> inventoryCfgBasesList = baseService.query(form.getBillNo(), InventoryCfgBase.class);
            if(CollectionUtil.isEmpty(inventoryCfgBasesList)){
                Asserts.fail(ResultEnum.PARAM_ERROR,"通过单据编码未找到盘点作业");
            }
            if(inventoryCfgBasesList.size()>1){
                Asserts.fail(ResultEnum.PARAM_ERROR,"查询到多个盘点作业,无法选择唯一一个盘点作业");
            }
            String FID = inventoryCfgBasesList.get(0).getFID();
            if(StringUtils.isBlank(FID)){
                Asserts.fail(ResultEnum.PARAM_ERROR,"通过单据编码查询到的盘点作业内码为空");
            }
            form.setId(FID);
        }


        StkStockCountInputEntity countInputEntity = new StkStockCountInputEntity();
        countInputEntity.setFID(form.getId());//单据内码ID
        List<InventoryResultsForm.Detail> details = form.getDetails();
        List<FBillEntry> entrys = Lists.newArrayList();
        for(InventoryResultsForm.Detail detail : details){
            FBillEntry entry = new FBillEntry();
            entry.setFEntryID(detail.getEntryId());//明细内码ID
            entry.setFCountQty(detail.getCountQty());//盘点数量
            entrys.add(entry);
        }
        countInputEntity.setFBillEntry(entrys);
        return countInputEntity;
    }

    /**
     * 组装采购入库明细
     * @param detail
     * @return
     */
    private FInStockEntry wrap2FInStockEntry(PurchaseInStockForm.InStockDetail detail) {
        FInStockEntry inStockEntry = new FInStockEntry();
        //物料编码 （必填）
        inStockEntry.setFMaterialId(new K3BaseEntity(detail.getMaterialId()));
        //库存单位编码（必填）
        inStockEntry.setFUnitID(new K3BaseEntity(detail.getUnitID()));
        //备注
        inStockEntry.setFNote(detail.getNote());
        //物料说明
        inStockEntry.setFMaterialDesc(detail.getMaterialDesc());
        //含含税单价 (库存单位的价格) （必填）
//        inStockEntry.setFTaxPrice();
        //实收数量 (库存单位的数量) （必填）
        inStockEntry.setFRealQty(detail.getRealQty());
        //计价单位编码 （必填）
        inStockEntry.setFPriceUnitID(new K3BaseEntity(detail.getPriceUnitID()));
        // 单价(计价单位的价格) （必填）
//        inStockEntry.getFPrice()
        //批次号
        inStockEntry.setFLot(new K3BaseEntity(detail.getFlot()));
        //仓库编码（必填）
        inStockEntry.setFStockId(new K3BaseEntity(detail.getStockId()));
        //库存状态 默认值 KCZT01_SYS
//        inStockEntry.setFStockStatusId();
        //是否赠品 是为true 否为false
//        inStockEntry.setFGiveAway();
        //货主类型 默认 BD_OwnerOrg
        inStockEntry.setFOWNERTYPEID(detail.getOwnerTypeId());
        //是否来料检验
//        inStockEntry.setFCheckInComing();
        //是否收料更新库存
//        inStockEntry.setFIsReceiveUpdateStock();
        //计价基本数量
//        inStockEntry.setFPriceBaseQty();
        //采购单位 编码（必填）
//        inStockEntry.setFRemainInStockUnitId();
        //立账关闭 默认 false
//        inStockEntry.setFBILLINGCLOSE();
        //采购数量 (采购单位的数量) （必填）
        inStockEntry.setFRemainInStockQty(detail.getRealQty());
        //未关联应付数量（计价单位）
//        inStockEntry.setFAPNotJoinQty();
        //采购基本数量（必填）
        inStockEntry.setFRemainInStockBaseQty(detail.getRealQty());
        //税率(%) 默认 13.00
//        inStockEntry.setFEntryTaxRate();
//        货主编码 根据货主类型变化取不同的码表
//        当货主类型为BD_OwnerOrg时指其组织机构的编码 （必填）
        inStockEntry.setFOWNERID(new K3BaseEntity(detail.getOwnerId()));
        //价税合计（折前）
//        inStockEntry.setFAllAmountExceptDisCount();

        if(CollectionUtil.isNotEmpty(detail.getInStockLinks())){
            //收货通知更新明细
            inStockEntry.setFInStockEntry_Link(Lists.transform(detail.getInStockLinks(), e->wrap2FInStockLink(e)));
        }
        return inStockEntry;
    }


    /**
     *
     * 组装采购入库财务信息
     * @param form
     * @return
     */
    private FInStockFin war2FInStockFin(PurchaseInStockForm form) {
        FInStockFin fInStockFin = new FInStockFin();
        // 结算组织 其组织机构的编码 （必填）
        fInStockFin.setFSettleOrgId(new K3BaseEntity(form.getSettleOrgId()));
        return fInStockFin;
    }

    /**
     * 收货通知更新明细
     * @param inStockLink
     * @return
     */
    private StkInStockEntryLink wrap2FInStockLink(PurchaseInStockForm.InStockLink inStockLink) {
        StkInStockEntryLink stockEntryLink = new StkInStockEntryLink();
        stockEntryLink.setFInStockEntry_Link_FSBillId(inStockLink.getSrcBillId());
        stockEntryLink.setFInStockEntry_Link_FSId(inStockLink.getSrcId());
        stockEntryLink.setFInStockEntry_Link_FRemainInStockBaseQtyOld(inStockLink.getRemainQtyOld());
        stockEntryLink.setFInStockEntry_Link_FRemainInStockBaseQty(inStockLink.getRemainQty());
        stockEntryLink.setFInStockEntry_Link_FBaseUnitQtyOld(inStockLink.getRemainQtyOld());
        stockEntryLink.setFInStockEntry_Link_FBaseUnitQty(inStockLink.getRemainQty());
        return stockEntryLink;
    }

    public K3ResultDTO wrap2K3ResultDTO(SaveResult result){
        RepoResult repoResult = result.getResult();
        RepoStatus responseStatus = repoResult.getResponseStatus();
        K3ResultDTO k3ResultDTO = new K3ResultDTO();
        k3ResultDTO.setErrCode(responseStatus.getErrorCode());
        if(CollectionUtil.isNotEmpty(responseStatus.getErrors())){
            k3ResultDTO.setErrors(Lists.transform(responseStatus.getErrors(),e->new K3ResultDTO.RepoError(e.getFieldName(),e.getMessage(),e.getDIndex())));
        }
        if(CollectionUtil.isNotEmpty(responseStatus.getSuccessEntitys())){
            k3ResultDTO.setSuccessEntities(Lists.transform(responseStatus.getSuccessEntitys(),e->new K3ResultDTO.SuccessEntity(e.getId(),e.getNumber(),e.getDIndex(),e.getBillNo())));
        }
        return k3ResultDTO;
    }

    /**
     * 组装直接调拨参数
     * @param form
     * @return
     */
    private StkTransferEntry wrap2StkTransferEntry(TransferForm form) {
        StkTransferEntry stkTransferEntry = new StkTransferEntry();
        //业务类型varchar(50)
        stkTransferEntry.setFBillTypeID(new K3BaseEntity(form.getBillTypeID()));
        stkTransferEntry.setFDocumentStatus(form.getDocumentStatus());
        //业务类型
//        stkTransferEntry.setFBizType();
        //调拨方向 (必填项)
//        stkTransferEntry.setFTransferDirect();
        //调拨类型varchar(50)
//        stkTransferEntry.setFTransferBizType();
        //结算组织
//        stkTransferEntry.setFSettleOrgId();
        //销售组织
//        stkTransferEntry.setFSaleOrgId();
        //调出库存组织 varchar(50) 下发
        stkTransferEntry.setFStockOutOrgId(new K3BaseEntity(form.getStockOutOrgId()));
        //调出货主类型varchar(50)
//        stkTransferEntry.setFOwnerTypeOutIdHead();
        //调出货主varchar(50)
//        stkTransferEntry.setFOwnerOutIdHead();
        //调入库存组织
        stkTransferEntry.setFStockOrgId(new K3BaseEntity(form.getStockOrgId()));
        //是否含税
//        stkTransferEntry.setFIsIncludedTax();
        //价外税
//        stkTransferEntry.setFIsIncludedTax();
        //汇率类型
//        stkTransferEntry.setFExchangeTypeId();
        //调入货主类型
//        stkTransferEntry.setFOwnerTypeIdHead();
        //结算币别
//        stkTransferEntry.setFSETTLECURRID();
        // 调入货主 varchar(50)
//        stkTransferEntry.setFOwnerIdHead();
        //单据日期
        stkTransferEntry.setFDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //备注
        stkTransferEntry.setFNote(form.getNote());
        //明细信息
        stkTransferEntry.setFBillEntry(Lists.transform(form.getTransferDetails(),e->wrap2StkTransferFbillEntry(form,e)));

        return stkTransferEntry;
    }


    private StkTransferEntry wrap2StkTransferEntryByProduction(TransferFormByProduction form) {
        StkTransferEntry stkTransferEntry = new StkTransferEntry();
        //业务类型varchar(50)
        stkTransferEntry.setFBillTypeID(new K3BaseEntity("ZJDB01_SYS"));
        stkTransferEntry.setFDocumentStatus(form.getDocumentStatus());
        //业务类型
//        stkTransferEntry.setFBizType();
        //调拨方向 (必填项)
//        stkTransferEntry.setFTransferDirect();
        //调拨类型varchar(50)
//        stkTransferEntry.setFTransferBizType();
        //结算组织
//        stkTransferEntry.setFSettleOrgId();
        //销售组织
//        stkTransferEntry.setFSaleOrgId();
        //调出库存组织 varchar(50) 下发
        stkTransferEntry.setFStockOutOrgId(new K3BaseEntity(form.getStockOutOrgId()));
        //调出货主类型varchar(50)
//        stkTransferEntry.setFOwnerTypeOutIdHead();
        //调出货主varchar(50)
//        stkTransferEntry.setFOwnerOutIdHead();
        //调入库存组织
        stkTransferEntry.setFStockOrgId(new K3BaseEntity(form.getStockOrgId()));
        //是否含税
//        stkTransferEntry.setFIsIncludedTax();
        //价外税
//        stkTransferEntry.setFIsIncludedTax();
        //汇率类型
//        stkTransferEntry.setFExchangeTypeId();
        //调入货主类型
//        stkTransferEntry.setFOwnerTypeIdHead();
        //结算币别
//        stkTransferEntry.setFSETTLECURRID();
        // 调入货主 varchar(50)
//        stkTransferEntry.setFOwnerIdHead();


        //单据日期
//        stkTransferEntry.setFDate(form.getCreateDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        stkTransferEntry.setFDate(format);


        //备注
        stkTransferEntry.setFNote(form.getNote());
        //明细信息
        stkTransferEntry.setFBillEntry(Lists.transform(form.getTransferDetails(),detail->{
            StkTransferFBillEntry fBillEntry = new StkTransferFBillEntry();
            //产品类型
//        fBillEntry.setFRowType();
            //物料编码
            fBillEntry.setFMaterialId(new K3BaseEntity(detail.getMaterialId()));
            //单位
            fBillEntry.setFUnitID(new K3BaseEntity(detail.getUnitID()));
            //调拨数量
            fBillEntry.setFQty(detail.getFactQty());
            //应调拨数量
            fBillEntry.setF_RVMM_Qty1(detail.getRvmmQty());
            //批号代码
            fBillEntry.setFLot(new K3BaseEntity(detail.getFLot()));
            //调出仓库
            fBillEntry.setFSrcStockId(new K3BaseEntity(form.getSrcStockId()));
            //调出仓位
//        fBillEntry.setFSrcStockLocId();
            //调入仓库
            fBillEntry.setFDestStockId(new K3BaseEntity(form.getDestStockId()));
            //调出库存状态
//        fBillEntry.setFSrcStockStatusId();
            //调出库存状态
//        fBillEntry.setFDestStockStatusId();
            //调出货主类型
//        fBillEntry.setFOwnerTypeOutId();
            //调出货主varchar
//        fBillEntry.setFOwnerOutId();
            //调入货主类型
//        fBillEntry.setFOwnerTypeId();
            //调入货主
//        fBillEntry.setFOwnerId();
            //基本单位
            fBillEntry.setFBaseUnitId(new K3BaseEntity(detail.getUnitID()));
            //调拨数量
            fBillEntry.setFBaseQty(detail.getFactQty());
            //赠品
//        fBillEntry.setFISFREE();
            //调入保管者类型
//        fBillEntry.setFKeeperTypeId();
            //调入保管者
//        fBillEntry.setFKeeperId();
            //调出保管者类型
//        fBillEntry.setFKeeperOutId();
            //调出保管者
//        fBillEntry.setFKeeperOutId();
            //调入物料编码
            fBillEntry.setFDestMaterialId(new K3BaseEntity(detail.getMaterialId()));
            //BOM版本
//        fBillEntry.setFBomId();
            //计划跟踪号
//        fBillEntry.setFMtoNo();
            fBillEntry.setFOrderNo(detail.getOrderNo());//生产订单号

            //生产订单反查
            PrdMoEntry byMo = getByMo(detail.getOrderNo());
            String fWorkShopID_fNumber = byMo.getFWorkShopID_FNumber();

            //车间
            fBillEntry.setF_PVCE_WORKCENTER(ImmutableMap.of("FNUMBER", fWorkShopID_fNumber));
            //源单类型
            fBillEntry.setFSrcBillTypeId(detail.getSrcBillTypeId());
            //源单编号
            fBillEntry.setFSrcBillNo(form.getSrcBillNo());
            fBillEntry.setFSaleUnitId(new K3BaseEntity(detail.getUnitID()));
            fBillEntry.setFSaleQty(detail.getFactQty());
            fBillEntry.setFSalBaseQty(detail.getFactQty());
            fBillEntry.setFPriceUnitID(new K3BaseEntity(detail.getUnitID()));
            fBillEntry.setFPriceQty(detail.getFactQty());
            fBillEntry.setFPriceBaseQty(detail.getFactQty());

            if(CollectionUtil.isNotEmpty(detail.getTransferDetailLinks())){
                fBillEntry.setFBillEntry_Link(Lists.newArrayList());
                for (TransferFormByProduction.TransferDetailLink link : detail.getTransferDetailLinks()) {
                    StkTransferFBillEntryLink fBillEntryLink = new StkTransferFBillEntryLink();
                    fBillEntryLink.setFBillEntry_Link_FSBillId(link.getSrcBillId());
                    fBillEntryLink.setFBillEntry_Link_FSId(link.getSrcId());
                    fBillEntryLink.setFBillEntry_Link_FBaseQtyOld(link.getBaseQtyOld());
                    fBillEntryLink.setFBillEntry_Link_FBaseQty(link.getBaseQty());
                    fBillEntry.getFBillEntry_Link().add(fBillEntryLink);
                }
            }
            return fBillEntry;
        }));

        return stkTransferEntry;
    }




    /**
     *  组装调拨明细
     * @param form
     * @return
     */
    private StkTransferFBillEntry wrap2StkTransferFbillEntry(TransferForm form,TransferForm.TransferDetail detail) {

        StkTransferFBillEntry fBillEntry = new StkTransferFBillEntry();
        //产品类型
//        fBillEntry.setFRowType();
        //物料编码
        fBillEntry.setFMaterialId(new K3BaseEntity(detail.getMaterialId()));
        //单位
        fBillEntry.setFUnitID(new K3BaseEntity(detail.getUnitID()));
        //调拨数量
        fBillEntry.setFQty(detail.getFactQty());
        //批号代码
        fBillEntry.setFLot(new K3BaseEntity(detail.getFLot()));
        //调出仓库
        fBillEntry.setFSrcStockId(new K3BaseEntity(form.getSrcStockId()));
        //调出仓位
//        fBillEntry.setFSrcStockLocId();
        //调入仓库
        fBillEntry.setFDestStockId(new K3BaseEntity(form.getDestStockId()));
        //调出库存状态
//        fBillEntry.setFSrcStockStatusId();
        //调出库存状态
//        fBillEntry.setFDestStockStatusId();
        //调出货主类型
//        fBillEntry.setFOwnerTypeOutId();
        //调出货主varchar
//        fBillEntry.setFOwnerOutId();
        //调入货主类型
//        fBillEntry.setFOwnerTypeId();
        //调入货主
//        fBillEntry.setFOwnerId();
        //基本单位
        fBillEntry.setFBaseUnitId(new K3BaseEntity(detail.getUnitID()));
        //调拨数量
        fBillEntry.setFBaseQty(detail.getFactQty());
        //赠品
//        fBillEntry.setFISFREE();
        //调入保管者类型
//        fBillEntry.setFKeeperTypeId();
        //调入保管者
//        fBillEntry.setFKeeperId();
        //调出保管者类型
//        fBillEntry.setFKeeperOutId();
        //调出保管者
//        fBillEntry.setFKeeperOutId();
        //调入物料编码
        fBillEntry.setFDestMaterialId(new K3BaseEntity(detail.getMaterialId()));
        //BOM版本
//        fBillEntry.setFBomId();
        //计划跟踪号
//        fBillEntry.setFMtoNo();
        //源单类型
//        fBillEntry.setFSrcBillTypeId();
        //源单编号
        fBillEntry.setFSrcBillNo(form.getSrcBillNo());
        fBillEntry.setFSaleUnitId(new K3BaseEntity(detail.getUnitID()));
        fBillEntry.setFSaleQty(detail.getFactQty());
        fBillEntry.setFSalBaseQty(detail.getFactQty());
        fBillEntry.setFPriceUnitID(new K3BaseEntity(detail.getUnitID()));
        fBillEntry.setFPriceQty(detail.getFactQty());
        fBillEntry.setFPriceBaseQty(detail.getFactQty());

        if(CollectionUtil.isNotEmpty(detail.getTransferDetailLinks())){
            fBillEntry.setFBillEntry_Link(Lists.newArrayList());
            for (TransferForm.TransferDetailLink link : detail.getTransferDetailLinks()) {
                StkTransferFBillEntryLink fBillEntryLink = new StkTransferFBillEntryLink();
                fBillEntryLink.setFBillEntry_Link_FSBillId(link.getSrcBillId());
                fBillEntryLink.setFBillEntry_Link_FSId(link.getSrcId());
                fBillEntryLink.setFBillEntry_Link_FBaseQtyOld(link.getBaseQtyOld());
                fBillEntryLink.setFBillEntry_Link_FBaseQty(link.getBaseQty());
                fBillEntry.getFBillEntry_Link().add(fBillEntryLink);
            }
        }
        return fBillEntry;
    }



    /**
     * 查询token
     * @return
     */
    private String getToken(){
        String postJson = "{\n" +
                "    \"ap1\":{\n" +
                "        \"AcctID\":\""+k3IdentifyConfig.getdCID()+"\",\n" +
                "        \"Username\":\""+k3IdentifyConfig.getUserName()+"\",\n" +
                "        \"Password\":\""+k3IdentifyConfig.getPwd()+"\",\n" +
                "        \"Lcid\":\""+k3IdentifyConfig.getlCID()+"\",\n" +
                "        \"AuthenticateType\":1,\n" +
                "        \"PasswordIsEncrypted\":\"false\",\n" +
                "        \"ClientInfo\":{\"ClientType\":8}\n" +
                "\n" +
                "    }\n" +
                "}";
        String post = HttpUtil.post(k3IdentifyConfig.getServerUrl() + "Kingdee.BOS.ServiceFacade.ServicesStub.User.UserService.ValidateLoginInfo.common.kdsvc", postJson);
        log.info("查询token信息:{}",post);
        Gson gson = new Gson();
        UserTokenInfo userTokenInfo = gson.fromJson(post, UserTokenInfo.class);
        return  userTokenInfo.getContext().getUserToken();
    }

    /**
     * 格式化ID
     * @param id
     * @return
     */
    private String formatK3Id(String id){
        return id.replace(".0","");
    }

}
