/**
  * Copyright 2022 bejson.com 
  */
package cn.getech.wms.api.dto;

import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class PrdReturnMtrlEntity {

    /**
     * 单据内码ID
     */
    private int FID;
    /**
     * 单据编号（必填）
     */
    private String FBillNo;
    /**
     * 单据类型（必填）
     */
    private K3BaseEntity FBillType;
    /**
     * 单据日期（必填）
     */
    private String FDate;
    /**
     * 收料组织编码（必填）
     */
    private K3BaseEntity FStockOrgId;
    /**
     * 生产组织编码（必填）
     */
    private K3BaseEntity FPrdOrgId;
    /**
     * 货主类型（必填）
     */
    private String FOwnerTypeId0;
    private boolean FIsCrossTrade;
    private boolean FVmiBusiness;
    private boolean FIsOwnerTInclOrg;
    /**
     * 明细
     */
    private List<PrdReturnMtrlFEntity> FEntity;

}