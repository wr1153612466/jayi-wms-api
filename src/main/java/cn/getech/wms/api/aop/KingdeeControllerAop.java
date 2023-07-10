package cn.getech.wms.api.aop;

import cn.getech.wms.api.dto.ResultVO;
import cn.getech.wms.api.entity.WmsLog;
import cn.getech.wms.api.service.IWmsLogService;
import cn.getech.wms.api.util.ResultVOUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Aspect
@Component
@Slf4j
public class KingdeeControllerAop {

    @Autowired
    private IWmsLogService iWmsLogService;

    @Pointcut("execution(* cn.getech.wms.api.controller.KingdeeController.*(..))")
    public void point() {}

    @Around("point()")
    public ResultVO consumeLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
    ResultVO result;
     // 获取方法参数
    Object[] args = joinPoint.getArgs();
     // 获取方法签名和注解
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    ApiOperation annotation = signature.getMethod().getAnnotation(ApiOperation.class);
     // 保存日志并获取日志ID
    Long logId = iWmsLogService.saveLog(annotation.value(), JSONUtil.toJsonStr(args));
     try {
        // 执行原方法
        Object obj = joinPoint.proceed();
        result = (ResultVO) obj;
         // 更新日志备注
        iWmsLogService.lambdaUpdate().eq(WmsLog::getId, logId).set(WmsLog::getRemark, JSONUtil.toJsonStr(obj)).update();
    } catch (Exception e) {
        // 异常处理
        result = ResultVOUtil.error(500, e.getMessage());
         // 更新日志备注
        iWmsLogService.lambdaUpdate().eq(WmsLog::getId, logId).set(WmsLog::getRemark, e.getMessage()).update();
    }
     return result;
}

}
