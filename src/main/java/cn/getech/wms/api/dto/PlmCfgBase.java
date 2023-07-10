package cn.getech.wms.api.dto;

import cn.getech.wms.api.enums.FormIDEnum;
import lombok.Data;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/30
 */
@Data
@K3Entity(value = {"FID","FCODE","FNAME"},formId = FormIDEnum.PLM_CFG_BASE,fName = "FCode",defaultCondition = "")
public class PlmCfgBase {

    private String FID;
    private String FCODE;
    private String FNAME;

}
