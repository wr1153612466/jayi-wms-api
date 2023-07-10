package cn.getech.wms.api.exception;

import cn.getech.wms.api.dto.*;
import cn.getech.wms.api.enums.ResultEnum;
import cn.getech.wms.api.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 * Created by macro on 2020/2/27.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public ResultVO handle(ApiException e) {
        log.error((e.getMessage()));
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = ApiResultException.class)
    public ApiResult handle2(ApiResultException e) {
        log.error((e.getMessage()));
        return ApiResultUtil.error(e.getCode(), e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVO handle(Exception e) {
        log.error(e.getMessage());
        return ResultVOUtil.error(ResultEnum.UNKNOWN_ERROR.getCode(), e.getMessage());
    }

}
