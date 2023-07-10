package cn.getech.wms.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WmsLog{

    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    private Long id;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @TableField("k3username")
    private String k3username;

    @TableField("remark")
    private String remark;

    @ApiModelProperty("日志类型")
    private String logType;

    @ApiModelProperty("日志JSON")
    private String logJson;

}
