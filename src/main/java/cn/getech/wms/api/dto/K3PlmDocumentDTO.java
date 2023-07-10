package cn.getech.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/12/6
 */
@Data
public class K3PlmDocumentDTO {

    @ApiModelProperty("文件ID")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("签字文件下载地址")
    private String pdfDownloadUrl;

}
