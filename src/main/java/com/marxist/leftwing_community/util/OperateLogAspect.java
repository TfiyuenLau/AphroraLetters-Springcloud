package com.marxist.leftwing_community.util;

import com.alibaba.fastjson.JSON;
import com.marxist.leftwing_community.entity.SysLog;
import com.marxist.leftwing_community.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * AOP实现操作访客日志
 */
@Aspect
@Component
@Slf4j
public class OperateLogAspect {
    @Autowired
    private ISysLogService sysLogService;

    /**
     * 操作日志切入点
     */
    @Pointcut("@annotation(com.marxist.leftwing_community.util.OperateLog)")
    public void operateLogPointCut() {
    }

    /**
     * 拦截用户操作日志
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operateLogPointCut()", returning = "keys")
    public void saveOperateLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String requestIp = RequestUtils.getRemoteIp(request);

        // 本机访问的不记录日志
        if (!"0:0:0:0:0:0:0:1".equals(requestIp) && !"127.0.0.1".equals(requestIp)) {
            SysLog operateLog = new SysLog();
            try {
                // 从切面织入点处通过反射机制获取织入点处的方法
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                // 获取切入点所在的方法
                Method method = signature.getMethod();
                // 获取操作的类型
                OperateLog opLog = method.getAnnotation(OperateLog.class);
                if (opLog != null) {
                    String operateDesc = opLog.operateDesc();
                    operateLog.setRemark(operateDesc); // 操作描述
                }
                // 请求IP
                operateLog.setIp(RequestUtils.getRemoteIp(request));
                // 请求的参数
                assert request != null;
                Map<String, String> rtnMap = converMap(request.getParameterMap());
                String params = JSON.toJSONString(rtnMap);// 将参数所在的数组转换成json
                operateLog.setOperateParam(params);
                // 请求URI
                operateLog.setOperateUrl(request.getRequestURI());
                // 请求的浏览器标识
                operateLog.setOperateBy(RequestUtils.getOsAndBrowserInfo(request));

                //保存至数据库
                sysLogService.save(operateLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }
}
