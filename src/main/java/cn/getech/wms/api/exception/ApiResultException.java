package cn.getech.wms.api.exception;

import cn.getech.wms.api.enums.IErrorCode;
import lombok.Getter;

@Getter
public class ApiResultException extends RuntimeException {

    private Integer code;

    public ApiResultException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ApiResultException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiResultException(String message) {
        super(message);
    }


}
