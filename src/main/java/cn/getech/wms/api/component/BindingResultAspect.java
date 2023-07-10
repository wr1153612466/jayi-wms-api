package cn.getech.wms.api.component;

import cn.getech.wms.api.enums.ResultEnum;
import cn.getech.wms.api.util.JsonUtil;
import cn.getech.wms.api.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * HibernateValidator错误结果处理切面
 * Created by macro on 2018/4/26.
 */
@Aspect
@Component
@Order(2)
@Slf4j
public class BindingResultAspect {
    @Pointcut("execution(public * cn.getech.wms.*.controller.*.*(..))")
    public void BindingResult() {
    }

    @Around("BindingResult()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.hasErrors()) {
                    FieldError fieldError = result.getFieldError();
                    log.error("异常代码如下：\n方法[{}]\n参数[{}]\n不正确原因:[{}]", joinPoint.getStaticPart().toString(), JsonUtil.toJson(result.getTarget()), fieldError.getDefaultMessage());
                    if (fieldError != null) {
                        return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),
                                fieldError.getDefaultMessage());
                    } else {
                        return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
