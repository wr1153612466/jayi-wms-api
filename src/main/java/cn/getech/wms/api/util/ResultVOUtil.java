package cn.getech.wms.api.util;


import cn.getech.wms.api.dto.ResultVO;
import cn.getech.wms.api.enums.IErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success(Integer code, String message, Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(code);
        resultVO.setMsg(message);
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static ResultVO error(IErrorCode errorCode) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(errorCode.getCode());
        resultVO.setMsg(errorCode.getMessage());
        return resultVO;
    }

    public static ResultVO error(IErrorCode errorCode,String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(errorCode.getCode());
        resultVO.setMsg(errorCode.getMessage()+":"+msg);
        return resultVO;
    }

    public static ResultVO errorData(IErrorCode errorCode,Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(errorCode.getCode());
        resultVO.setMsg(errorCode.getMessage());
        resultVO.setData(data);
        return resultVO;
    }
}