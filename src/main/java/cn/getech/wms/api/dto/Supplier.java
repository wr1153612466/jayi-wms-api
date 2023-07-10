package cn.getech.wms.api.dto;

import cn.getech.wms.api.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author william
 * @description
 * @Date: 2020-04-26 14:13
 */
@K3Entity(value = {"FNumber", "FName","FSupplierId"}, formId = FormIDEnum.SUPPLIER,fName = "FNumber")
@Data
public class Supplier implements Serializable {
    private static final long serialVersionUID = 3289864047340390304L;

    /**
     * 供应商主键ID
     */
    private String FSupplierId;

    /**
     * 供应商代码
     */
    private String FNumber;

    /**
     * 供应商名称
     */
    private String FName;
}
