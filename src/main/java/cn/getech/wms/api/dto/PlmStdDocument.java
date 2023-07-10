package cn.getech.wms.api.dto;

import cn.getech.wms.api.enums.FormIDEnum;
import lombok.Data;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/30
 */
@Data
@K3Entity(value = "FID,FCODE,FNAME,FFileId,FRELEVANTOBJECT",formId = FormIDEnum.PLM_STD_DOCUMENT,fName = "FID",defaultCondition = "")
public class PlmStdDocument {

    private String FID;
    private String FCODE;
    private String FNAME;
    private String FFileId;
    private String FRELEVANTOBJECT;

}
