package cn.getech.wms.api.exception;


import cn.getech.wms.api.enums.IErrorCode;
import lombok.Getter;

/**
 * 自定义API异常
 * Created by macro on 2020/2/27.
 */
@Getter
public class ApiException extends RuntimeException {

    private Integer code;

    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiException(String message) {
        super(message);
    }


}
