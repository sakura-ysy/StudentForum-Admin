package com.fengxi.forumadmin.springAOP;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengxi.forumadmin.common.annotation.OperLog;
import com.fengxi.forumadmin.model.entity.ExceptionLog;
import com.fengxi.forumadmin.model.entity.OperationLog;
import com.fengxi.forumadmin.model.entity.User;
import com.fengxi.forumadmin.service.IExceptionLogService;
import com.fengxi.forumadmin.service.IOperationLogService;
import com.fengxi.forumadmin.service.IUserService;
import com.fengxi.forumadmin.uitls.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LogAOP {


    public static final int INT = 156;

    @Resource
    private IOperationLogService iOperationLogService;
    @Resource
    private IExceptionLogService iExceptionLogService;
    @Resource
    private IUserService iUserService;

    // 操作
    @Pointcut("@annotation(com.fengxi.forumadmin.common.annotation.OperLog)")
    public void operLogPoinCut(){

    }

    // 异常
    @Pointcut("execution(* com.fengxi.forumadmin.controller..*.*(..))")
    public void operExceptionLogPoinCut() {
    }


/**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果

*/
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        // 操作用户
        String username = (String) StpUtil.getLoginId();
        User user = iUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername,username));
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operlog = new OperationLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog opLog = method.getDeclaredAnnotation(OperLog.class);
            if (opLog != null) {
                String operModul = opLog.operModul();
                String operType = opLog.operType();
                String operDesc = opLog.operDesc();
                operlog.setOperModule(operModul); // 操作模块
                                 operlog.setOperType(operType); // 操作类型
                                 operlog.setOperDesc(operDesc); // 操作描述
                             }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;

            operlog.setOperMethod(methodName); // 请求方法

            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            operlog.setOperIp(IpUtils.getIpAddr(request)); // 请求IP
            operlog.setOperRequParam(params); // 请求参数
            operlog.setOperRespParam(JSON.toJSONString(keys)); // 返回结果
            operlog.setOperUserId(user.getId()); // 请求用户ID
            operlog.setOperUserName(username); // 请求用户名称
            operlog.setOperUrl(request.getRequestURI()); // 请求URL
            operlog.setOperCreateTime(new Date()); // 创建时间
            //operlog.setOperVer(operVer); // 操作版本
            iOperationLogService.getBaseMapper().insert(operlog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")  public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
         // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        ExceptionLog excepLog = new ExceptionLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            excepLog.setExcRequParam(params); // 请求参数
            excepLog.setExcMethod(methodName); // 请求方法名
            excepLog.setExcName(e.getClass().getName()); // 异常名称
            excepLog.setExcMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
            User user = iUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,StpUtil.getLoginId()));
            if (!ObjectUtils.isEmpty(user)){
                excepLog.setExcUserId(user.getId()); // 操作员ID
                excepLog.setExcUserName(user.getUsername()); // 操作员用户名
            }

            excepLog.setExcUrl(request.getRequestURI());
            excepLog.setExcIp(IpUtils.getIpAddr(request));
            excepLog.setOperCreateTime(new Date()); // 发生异常时间
            iExceptionLogService.getBaseMapper().insert(excepLog);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
}


    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
         StringBuffer strbuff = new StringBuffer();
         for (StackTraceElement stet : elements) {
             strbuff.append(stet + "\n");
         }
         String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
         return message;
     }
}
