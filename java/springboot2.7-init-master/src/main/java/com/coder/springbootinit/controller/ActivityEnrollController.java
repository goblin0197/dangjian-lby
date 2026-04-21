package com.coder.springbootinit.controller;

import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollAddRequest;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollCancelRequest;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollListRequest;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollSignInRequest;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.ActivityEnrollUserVO;
import com.coder.springbootinit.service.ActivityEnrollService;
import com.coder.springbootinit.service.UserService;

import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 活动报名接口
 *
*/
@RestController
@RequestMapping("/activity/enroll")
@Api(tags = "活动报名管理")
@Slf4j
public class ActivityEnrollController {

    @Resource
    private ActivityEnrollService activityEnrollService;

    @Resource
    private UserService userService;

    /**
     * 报名活动
     *
     * @param activityEnrollAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "报名活动")
    public BaseResponse<Boolean> enrollActivity(@RequestBody ActivityEnrollAddRequest activityEnrollAddRequest, HttpServletRequest request) {
        if (activityEnrollAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = activityEnrollService.enrollActivity(activityEnrollAddRequest, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 取消报名
     *
     * @param activityEnrollCancelRequest
     * @param request
     * @return
     */
    @PostMapping("/cancel")
    @ApiOperation(value = "取消报名")
    public BaseResponse<Boolean> cancelEnroll(@RequestBody ActivityEnrollCancelRequest activityEnrollCancelRequest, HttpServletRequest request) {
        if (activityEnrollCancelRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = activityEnrollService.cancelEnroll(activityEnrollCancelRequest, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 签到
     *
     * @param activityEnrollSignInRequest
     * @param request
     * @return
     */
    @PostMapping("/signIn")
    @ApiOperation(value = "签到")
    public BaseResponse<Boolean> signInActivity(@RequestBody ActivityEnrollSignInRequest activityEnrollSignInRequest, HttpServletRequest request) {
        if (activityEnrollSignInRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long activityId = activityEnrollSignInRequest.getActivityId();
        Long userId = activityEnrollSignInRequest.getUserId();
        User loginUser = userService.getLoginUser(request);
        boolean result = activityEnrollService.signInActivity(activityId, userId, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 检查是否已报名
     *
     * @param activityId
     * @param userId
     * @param request
     * @return
     */
    @GetMapping("/check")
    @ApiOperation(value = "检查是否已报名")
    public BaseResponse<Boolean> checkEnrolled(long activityId, long userId, HttpServletRequest request) {
        if (activityId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = activityEnrollService.checkEnrolled(activityId, userId);
        return ResultUtils.success(result);
    }

    /**
     * 获取指定活动已报名人员列表
     *
     * @param activityEnrollListRequest
     * @param request
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "获取指定活动已报名人员列表")
    public BaseResponse<List<ActivityEnrollUserVO>> listEnrolledUsers(@RequestBody ActivityEnrollListRequest activityEnrollListRequest, HttpServletRequest request) {
        if (activityEnrollListRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ActivityEnrollUserVO> result = activityEnrollService.listEnrolledUsers(activityEnrollListRequest);
        return ResultUtils.success(result);
    }
}