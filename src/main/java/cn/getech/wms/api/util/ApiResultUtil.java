package cn.getech.wms.api.util;


import cn.getech.wms.api.dto.*;
import cn.getech.wms.api.enums.IErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiResultUtil {

    public static ApiResult success(Object object) {
        ApiResult apiResult = new ApiResult();
        apiResult.setData(object);
        apiResult.setCode(0);
        apiResult.setMsg("成功");
        return apiResult;
    }

    public static ApiResult success(Integer code, String message, Object object) {
        ApiResult apiResult = new ApiResult();
        apiResult.setData(object);
        apiResult.setCode(code);
        apiResult.setMsg(message);
        return apiResult;
    }

    public static ApiResult success() {
        return success(null);
    }

    public static ApiResult error(Integer code, String msg) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static ApiResult error(IErrorCode errorCode) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(errorCode.getCode());
        apiResult.setMsg(errorCode.getMessage());
        return apiResult;
    }
}