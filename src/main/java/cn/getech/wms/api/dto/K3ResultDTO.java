package cn.getech.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/29
 */
@ApiModel("K3返回结果实体")
@Data
public class K3ResultDTO {

    @ApiModelProperty("错误编码")
    private String errCode;

    @ApiModelProperty("错误信息")
    private List<RepoError> errors;

    @ApiModelProperty("成功实体")
    private List<SuccessEntity> successEntities;

    @Data
    @ApiModel("错误信息")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RepoError{

        @ApiModelProperty("字段名称")
        private String fieldName;
        @ApiModelProperty("错误信息")
        private String message;
        @ApiModelProperty("下标")
        private int index;

    }

    @Data
    @ApiModel("成功实体")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuccessEntity{

        @ApiModelProperty("单据内码")
        private String id;

        @ApiModelProperty("单据编码")
        private String number;

        @ApiModelProperty("下表")
        private int index;

        @ApiModelProperty("单据号")
        private String billNo;

    }

}
