package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.exception.ThrowUtils;
import com.coder.springbootinit.model.dto.activity.ActivityAddRequest;
import com.coder.springbootinit.model.dto.activity.ActivityQueryRequest;
import com.coder.springbootinit.model.dto.activity.ActivityUpdateRequest;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.ActivityVO;
import com.coder.springbootinit.service.ActivityService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 活动接口
 *
*/
@RestController
@RequestMapping("/activity")
@Api(tags = "活动管理")
@Slf4j
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @Resource
    private UserService userService;

    /**
     * 添加活动
     *
     * @param activityAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "添加活动（仅管理员）")
    public BaseResponse<Activity> addActivity(@RequestBody ActivityAddRequest activityAddRequest, HttpServletRequest request) {
        if (activityAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Activity result = activityService.addActivity(activityAddRequest, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新活动
     *
     * @param activityUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "更新活动（仅管理员）")
    public BaseResponse<Boolean> updateActivity(@RequestBody ActivityUpdateRequest activityUpdateRequest, HttpServletRequest request) {
        if (activityUpdateRequest == null || activityUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = activityService.updateActivity(activityUpdateRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 删除活动
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "删除活动（仅管理员）")
    public BaseResponse<Boolean> deleteActivity(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = activityService.deleteActivity(deleteRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 获取活动详情
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @ApiOperation(value = "获取活动详情")
    public BaseResponse<ActivityVO> getActivityById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ActivityVO activityVO = activityService.getActivityVO(id);
        ThrowUtils.throwIf(activityVO == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(activityVO);
    }

    /**
     * 分页获取活动列表
     *
     * @param activityQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation(value = "分页获取活动列表")
    public BaseResponse<Page<Activity>> listActivityByPage(@RequestBody ActivityQueryRequest activityQueryRequest, HttpServletRequest request) {
        long current = activityQueryRequest.getCurrent();
        long size = activityQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Activity> activityPage = activityService.page(new Page<>(current, size), activityService.getQueryWrapper(activityQueryRequest));
        return ResultUtils.success(activityPage);
    }

    /**
     * 分页获取活动VO列表
     *
     * @param activityQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    @ApiOperation(value = "分页获取活动VO列表")
    public BaseResponse<List<ActivityVO>> listActivityVOByPage(@RequestBody ActivityQueryRequest activityQueryRequest, HttpServletRequest request) {
        if (activityQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ActivityVO> activityVOList = activityService.listActivityVO(activityQueryRequest);
        return ResultUtils.success(activityVOList);
    }

    /**
     * 发布活动
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/publish")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "发布活动（仅管理员）")
    public BaseResponse<Boolean> publishActivity(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = activityService.publishActivity(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 结束活动
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/end")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "结束活动（仅管理员）")
    public BaseResponse<Boolean> endActivity(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = activityService.endActivity(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 更新指定活动的参与和签到情况统计
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/statistics/update")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "更新指定活动的参与和签到情况统计（仅管理员）")
    public BaseResponse<Boolean> updateActivityStatistics(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = activityService.updateActivityStatistics(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 更新所有活动的参与和签到情况统计
     *
     * @param request
     * @return
     */
    @PostMapping("/statistics/update/all")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "更新所有活动的参与和签到情况统计（仅管理员）")
    public BaseResponse<Boolean> updateAllActivityStatistics(HttpServletRequest request) {
        boolean result = activityService.updateAllActivityStatistics();
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }
}