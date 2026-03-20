package com.coder.springbootinit.aop;

import com.coder.springbootinit.annotation.DevelopmentStageLogAnnotation;
import com.coder.springbootinit.model.entity.DevelopmentStage;
import com.coder.springbootinit.model.entity.DevelopmentStageLog;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.DevelopmentStageOperationEnum;
import com.coder.springbootinit.service.DevelopmentStageLogService;
import com.coder.springbootinit.service.DevelopmentStageService;
import com.coder.springbootinit.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 发展阶段操作日志 AOP 切面
 *
 */
@Aspect
@Component
@Slf4j
public class DevelopmentStageLogAspect {

    @Resource
    private DevelopmentStageLogService developmentStageLogService;

    @Resource
    private DevelopmentStageService developmentStageService;

    @Resource
    private UserService userService;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 拦截带有@DevelopmentStageLogAnnotation注解的方法
     */
    @Around("@annotation(com.coder.springbootinit.annotation.DevelopmentStageLogAnnotation)")
    public Object doLog(ProceedingJoinPoint point) throws Throwable {
        // 获取注解信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DevelopmentStageLogAnnotation annotation = method.getAnnotation(DevelopmentStageLogAnnotation.class);
        DevelopmentStageOperationEnum operationType = annotation.operationType();
        String description = annotation.description();

        // 获取方法参数
        Object[] args = point.getArgs();
        Long stageId = extractStageId(args);
        Long userId = null;
        String beforeData = null;

        // 如果是更新/删除/审核操作，先获取变更前数据
        if (stageId != null && needBeforeData(operationType)) {
            DevelopmentStage beforeStage = developmentStageService.getById(stageId);
            if (beforeStage != null) {
                userId = beforeStage.getUserId();
                try {
                    beforeData = objectMapper.writeValueAsString(beforeStage);
                } catch (JsonProcessingException e) {
                    log.error("序列化变更前数据失败", e);
                }
            }
        }

        // 执行原方法
        Object result = point.proceed();

        // 记录日志
        try {
            // 获取当前登录用户
            Long operatorId = null;
            String operatorName = null;
            User loginUser = getLoginUser();
            if (loginUser != null) {
                operatorId = loginUser.getId();
                operatorName = loginUser.getUserName();
            }

            // 获取变更后数据
            String afterData = null;
            DevelopmentStage afterStage = null;
            if (stageId != null && operationType != DevelopmentStageOperationEnum.DELETE) {
                afterStage = developmentStageService.getById(stageId);
                if (afterStage != null) {
                    userId = afterStage.getUserId();
                    try {
                        afterData = objectMapper.writeValueAsString(afterStage);
                    } catch (JsonProcessingException e) {
                        log.error("序列化变更后数据失败", e);
                    }
                }
            } else if (result instanceof DevelopmentStage) {
                // 创建操作，返回值就是新创建的对象
                afterStage = (DevelopmentStage) result;
                stageId = afterStage.getId();
                userId = afterStage.getUserId();
                try {
                    afterData = objectMapper.writeValueAsString(afterStage);
                } catch (JsonProcessingException e) {
                    log.error("序列化变更后数据失败", e);
                }
            }

            // 记录日志
            if (stageId != null && userId != null) {
                developmentStageLogService.logOperation(
                        stageId,
                        userId,
                        operationType.getValue(),
                        operatorId,
                        operatorName,
                        beforeData,
                        afterData,
                        description
                );
            }
        } catch (Exception e) {
            log.error("记录发展阶段操作日志失败", e);
        }

        return result;
    }

    /**
     * 从方法参数中提取发展阶段ID
     */
    private Long extractStageId(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
            if (arg instanceof DevelopmentStage) {
                return ((DevelopmentStage) arg).getId();
            }
            // 尝试从DTO中获取id
            try {
                Method getIdMethod = arg.getClass().getMethod("getId");
                Object idValue = getIdMethod.invoke(arg);
                if (idValue instanceof Long) {
                    return (Long) idValue;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 判断是否需要获取变更前数据
     */
    private boolean needBeforeData(DevelopmentStageOperationEnum operationType) {
        return operationType == DevelopmentStageOperationEnum.UPDATE
                || operationType == DevelopmentStageOperationEnum.DELETE
                || operationType == DevelopmentStageOperationEnum.SUBMIT_AUDIT
                || operationType == DevelopmentStageOperationEnum.AUDIT;
    }

    /**
     * 获取当前登录用户
     */
    private User getLoginUser() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return null;
            }
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return userService.getLoginUserPermitNull(request);
        } catch (Exception e) {
            return null;
        }
    }
}
