/**
  * Copyright 2022 bejson.com 
  */
package cn.getech.wms.api.dto;

import lombok.Data;

/**
 * Auto-generated: 2022-11-24 14:48:28
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class FInStockFin {

    /**
     *  结算组织 其组织机构的编码 （必填）
     */
    private K3BaseEntity FSettleOrgId;
    /**
     * 结算方式 默认 JSFS04_SYS
     */
    private K3BaseEntity FSettleTypeId = new K3BaseEntity("JSFS04_SYS");
    /**
     * 付款条件 默认 FKTJ03_SYS
     */
    private K3BaseEntity FPayConditionId = new K3BaseEntity("FKTJ03_SYS");
    /**
     * 结算币别编码 默认 PRE001
     */
    private K3BaseEntity FSettleCurrId = new K3BaseEntity("PRE001");
    /**
     * 含税 默认 true
     */
    private boolean FIsIncludedTax;
    /**
     * 定价时点 默认 1
     */
    private String FPriceTimePoint;
    /**
     * 本位币编码 默认  PRE001
     */
    private K3BaseEntity FLocalCurrId;
    /**
     * 汇率类型 默认 HLTX01_SYS
     */
    private K3BaseEntity FExchangeTypeId;
    /**
     * 汇率 默认 1.0
     */
    private int FExchangeRate = 1;
    /**
     * 价外税 默认 true
     */
    private boolean FISPRICEEXCLUDETAX = true;

}