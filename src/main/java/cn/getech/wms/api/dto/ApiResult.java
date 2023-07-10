package cn.getech.wms.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResult<T> implements Serializable{

    private static final long serialVersionUID = 2299391642539142798L;
    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}