package cn.getech.wms.api.dto;

import cn.getech.wms.api.enums.FormIDEnum;
import lombok.Data;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/30
 */
@Data
@K3Entity(value = "FID,FRE_FCode,FRELATEDOBJECT",formId = FormIDEnum.PLM_CFG_RELATEDOBJECT,fName = "FRELATEDOBJECT",defaultCondition = "")
public class PlmCfgRelatedObject {

    private String FID;
    private String FRE_FCode;
    private String FRELATEDOBJECT;

}
