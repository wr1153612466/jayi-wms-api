package cn.getech.wms.api.component;

import cn.getech.wms.api.dto.ResultVO;
import cn.getech.wms.api.exception.ApiException;
import cn.getech.wms.api.util.AopUtils;
import cn.getech.wms.api.util.MethodReleaseUtils;
import cn.getech.wms.api.util.ResultVOUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class ResponseAspect {
    private static final Logger log = LoggerFactory.getLogger(ResponseAspect.class);
    private static final String SPLIT_STR = ",";

    public ResponseAspect() {
    }

    @Pointcut("execution(public * cn.getech.wms.*.controller.*.*(..))")
    public void responseBody() {
    }

    @Around("responseBody()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object result = null;
        long start = System.currentTimeMillis();
        String token = RandomStringUtils.randomAlphanumeric(8);
        MethodReleaseUtils.setInvokeToken(token, start);
        String params = AopUtils.getParameters(joinPoint);
        this.printRequestParam(token, signature, params);
        try {
            result = joinPoint.proceed(args);
            if (result == null) {
                result = ResultVOUtil.success();
            } else if (!(result instanceof ResultVO)) {
                result = ResultVOUtil.success(result);
            }
        } catch (ApiException var10) {
            result = ResultVOUtil.error(var10.getCode(), var10.getMessage());
        }

        log.info("[{}] - 耗时：{}，请求应答：{}", new Object[]{token, System.currentTimeMillis() - start, AopUtils.logStr(result)});
        return result;
    }

    private void printRequestParam(String token, MethodSignature signature, String params) {
        log.info("[{}] - {}.{} 请求参数：{}", new Object[]{token, signature.getDeclaringType().getSimpleName(), signature.getMethod().getName(), params});
    }

}