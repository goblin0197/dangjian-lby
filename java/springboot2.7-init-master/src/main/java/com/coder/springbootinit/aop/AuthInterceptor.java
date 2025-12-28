package com.coder.springbootinit.aop;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.UserRoleEnum;
import com.coder.springbootinit.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 AOP
 *
*/
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String[] mustRoles = authCheck.mustRole();
        // 不需要权限，放行
        if (mustRoles == null || mustRoles.length == 0) {
            return joinPoint.proceed();
        }

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 获取用户角色
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 获取必须角色枚举数组
        UserRoleEnum[] mustRoleEnums = UserRoleEnum.getEnumsByValues(mustRoles);
        // 检查用户是否有任意一个必须角色
        boolean hasRole = false;
        for (UserRoleEnum mustRoleEnum : mustRoleEnums) {
            // 只要用户角色是其中一个，就认为校验通过
            if (mustRoleEnum != null && userRoleEnum.equals(mustRoleEnum)) {
                hasRole = true;
                break;
            }
        }

        // 没有匹配的角色，拒绝访问
        if (!hasRole) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

