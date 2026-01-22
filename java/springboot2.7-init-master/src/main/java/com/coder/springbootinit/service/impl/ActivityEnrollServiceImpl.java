package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.exception.ThrowUtils;
import com.coder.springbootinit.mapper.ActivityEnrollMapper;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollAddRequest;
import com.coder.springbootinit.model.dto.activityEnroll.ActivityEnrollCancelRequest;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.entity.ActivityEnroll;
import com.coder.springbootinit.model.enums.ActivityEnrollStatusEnum;
import com.coder.springbootinit.model.enums.ActivityEnrollSignEnum;
import com.coder.springbootinit.model.enums.ActivityStatusEnum;
import com.coder.springbootinit.service.ActivityEnrollService;
import com.coder.springbootinit.service.ActivityService;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动报名服务实现
 *
*/
@Service
public class ActivityEnrollServiceImpl extends ServiceImpl<ActivityEnrollMapper, ActivityEnroll> implements ActivityEnrollService {

    @Resource
    private ActivityService activityService;

    @Override
    @Transactional
    public boolean enrollActivity(ActivityEnrollAddRequest activityEnrollAddRequest, Long opUserId) {
        Long activityId = activityEnrollAddRequest.getActivityId();
        Long userId = activityEnrollAddRequest.getUserId();

        // 检查活动是否存在
        Activity activity = activityService.getById(activityId);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR, "活动不存在");

        // 检查活动是否已发布
        if (activity.getStatus() != ActivityStatusEnum.PUBLISHED.getCode() && activity.getStatus() != ActivityStatusEnum.ENDED.getCode()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "活动未发布或已结束");
        }

        // 检查是否已过报名截止时间
        if (new Date().after(activity.getEnrollDeadline())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "报名已截止");
        }

        // 检查是否已达到最大参与人数
        if (activity.getCurrentNum() >= activity.getMaxNum()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "活动人数已满");
        }

        // 检查是否已经报名
        QueryWrapper<ActivityEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityId", activityId);
        queryWrapper.eq("userId", userId);
        ActivityEnroll existingEnroll = this.getOne(queryWrapper);
        if (existingEnroll != null && existingEnroll.getParticipantStatus() == ActivityEnrollStatusEnum.ENROLLED.getCode()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已经报名该活动");
        }else if(existingEnroll != null && existingEnroll.getParticipantStatus() == ActivityEnrollStatusEnum.CANCELLED.getCode()){
            existingEnroll.setParticipantStatus(ActivityEnrollStatusEnum.ENROLLED.getCode());
            boolean updateResult = this.updateById(existingEnroll);
            ThrowUtils.throwIf(!updateResult, ErrorCode.OPERATION_ERROR, "报名失败");
            return true;
        }

        // 创建报名记录
        ActivityEnroll activityEnroll = new ActivityEnroll();
        activityEnroll.setActivityId(activityId);
        activityEnroll.setUserId(userId);
        activityEnroll.setEnrollTime(new Date());
        activityEnroll.setParticipantStatus(ActivityEnrollStatusEnum.ENROLLED.getCode()); // 已报名
        activityEnroll.setOpUserId(opUserId);

        // 保存报名记录
        boolean saveResult = this.save(activityEnroll);
        ThrowUtils.throwIf(!saveResult, ErrorCode.OPERATION_ERROR, "报名失败");

        // 更新活动当前参与人数
        activity.setCurrentNum(activity.getCurrentNum() + 1);
        activityService.updateById(activity);
        return true;
    }

    @Override
    @Transactional
    public boolean cancelEnroll(ActivityEnrollCancelRequest activityEnrollCancelRequest, Long opUserId) {
        Long activityId = activityEnrollCancelRequest.getActivityId();
        Long userId = activityEnrollCancelRequest.getUserId();

        // 检查活动是否存在
        Activity activity = activityService.getById(activityId);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR, "活动不存在");

        // 检查是否已过报名截止时间
        if (new Date().after(activity.getEnrollDeadline())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "报名已截止，无法取消");
        }

        // 查找报名记录
        QueryWrapper<ActivityEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityId", activityId);
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("isDelete", 0);
        ActivityEnroll activityEnroll = this.getOne(queryWrapper);
        ThrowUtils.throwIf(activityEnroll == null, ErrorCode.NOT_FOUND_ERROR, "未找到报名记录");

        // 检查是否已签到
        if (ActivityEnrollSignEnum.SIGNED.getCode().equals(activityEnroll.getIsSign())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已签到，无法取消报名");
        }

        // 更新报名状态为已取消
        activityEnroll.setParticipantStatus(ActivityEnrollStatusEnum.CANCELLED.getCode()); // 已取消
        activityEnroll.setOpUserId(opUserId);
        activityEnroll.setUpdateTime(new Date());

        // 保存更新
        boolean updateResult = this.updateById(activityEnroll);
        ThrowUtils.throwIf(!updateResult, ErrorCode.OPERATION_ERROR, "取消报名失败");

        // 更新活动当前参与人数（使用SQL自减操作）
        boolean decreaseResult = activityService.decreaseCurrentNum(activityId);
        ThrowUtils.throwIf(!decreaseResult, ErrorCode.OPERATION_ERROR, "更新活动参与人数失败");

        return true;
    }

    @Override
    public boolean signInActivity(Long activityId, Long userId, Long opUserId) {
        // 检查活动是否存在
        Activity activity = activityService.getById(activityId);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR, "活动不存在");

        // 检查活动是否在进行中
        if (activity.getStatus() != ActivityStatusEnum.IN_PROGRESS.getCode()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "活动未开始或已结束");
        }

        // 检查是否在活动时间内
        Date now = new Date();
        if (now.before(activity.getStartTime()) || now.after(activity.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不在活动时间内");
        }

        // 查找报名记录
        QueryWrapper<ActivityEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityId", activityId);
        queryWrapper.eq("userId", userId);
        ActivityEnroll activityEnroll = this.getOne(queryWrapper);
        ThrowUtils.throwIf(activityEnroll == null, ErrorCode.NOT_FOUND_ERROR, "未找到报名记录");

        // 检查是否已报名
        if (!ActivityEnrollStatusEnum.ENROLLED.getCode().equals(activityEnroll.getParticipantStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未报名或已取消报名");
        }

        // 检查是否已签到
        if (ActivityEnrollSignEnum.SIGNED.getCode().equals(activityEnroll.getIsSign())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已签到，无法重复签到");
        }

        // 更新签到状态
        activityEnroll.setIsSign(ActivityEnrollSignEnum.SIGNED.getCode()); // 已签到
        activityEnroll.setOpUserId(opUserId);
        activityEnroll.setUpdateTime(new Date());

        return this.updateById(activityEnroll);
    }

    @Override
    public boolean checkEnrolled(Long activityId, Long userId) {
        QueryWrapper<ActivityEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityId", activityId);
        queryWrapper.eq("userId", userId);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public Integer getEnrollStatus(Long activityId, Long userId) {
        QueryWrapper<ActivityEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activityId", activityId);
        queryWrapper.eq("userId", userId);
        ActivityEnroll activityEnroll = this.getOne(queryWrapper);
        return activityEnroll != null ? activityEnroll.getParticipantStatus() : null;
    }
}