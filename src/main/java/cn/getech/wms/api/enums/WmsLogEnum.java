package cn.getech.wms.api.enums;

import lombok.Getter;

@Getter
public enum WmsLogEnum {

    PRD_PICKMTRL_IN("回写生产领料-wms-入参"),
    PRD_PICKMTRL_OUT("回写生产领料-金蝶-返回"),

    PRD_INSTOCK_IN("回写生产入库单-wms-入参"),
    PRD_INSTOCK_OUT("回写生产入库单-金蝶-返回"),

    PRD_RETURNMTRL_IN("回写生产退料单-wms-入参"),
    PRD_RETURNMTRL_OUT("回写生产退料单-金蝶-返回"),
    ;



    private String logType;

    WmsLogEnum(String logType) {
        this.logType = logType;
    }
}
