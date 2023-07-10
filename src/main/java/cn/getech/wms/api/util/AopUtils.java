//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.getech.wms.api.util;

import cn.getech.wms.api.context.PorosContextHolder;
import cn.getech.wms.api.dto.ResultVO;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.http.HttpMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AopUtils {
    public AopUtils() {
    }

    public static String getParameters(JoinPoint joinPoint) {
        String requestMethod = PorosContextHolder.getRequest().getMethod();
        if (!HttpMethod.PUT.name().equals(requestMethod) && !HttpMethod.POST.name().equals(requestMethod)) {
            Map<?, ?> paramsMap = (Map)PorosContextHolder.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            List paramList = (List)Arrays.stream(joinPoint.getArgs()).filter((param) -> {
                return !isFilterObject(param);
            }).collect(Collectors.toList());
            if (MapUtil.isNotEmpty(paramsMap)) {
                paramList.add(paramsMap);
            }

            return JSON.toJSONString(paramList);
        } else {
            return argsArrayToString(joinPoint.getArgs());
        }
    }

    public static String argsArrayToString(Object[] paramsArray) {
        return ArrayUtils.isNotEmpty(paramsArray) ? (String)Arrays.stream(paramsArray).filter((param) -> {
            return !isFilterObject(param);
        }).map((param) -> {
            return JSON.toJSONString(param);
        }).collect(Collectors.joining(" ")) : "";
    }

    public static boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

    public static String logStr(Object object) {
        JSONObject jsonObject = null;
        if (object instanceof ResultVO) {
            jsonObject = new JSONObject();
            ResultVO restResponse = (ResultVO)object;
            jsonObject.put("code", restResponse.getCode());
            jsonObject.put("msg", restResponse.getMsg());
            if (restResponse.getData() != null) {
                jsonObject.put("data", JSON.toJSONString(restResponse.getData()));
            }

            return jsonObject.toJSONString();
        } else {
            return JSON.toJSONString(object);
        }
    }
}
