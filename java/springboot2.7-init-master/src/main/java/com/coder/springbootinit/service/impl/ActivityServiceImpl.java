package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.ThrowUtils;
import com.coder.springbootinit.mapper.ActivityMapper;
import com.coder.springbootinit.mapper.ActivityEnrollMapper;
import com.coder.springbootinit.model.dto.activity.ActivityAddRequest;
import com.coder.springbootinit.model.dto.activity.ActivityQueryRequest;
import com.coder.springbootinit.model.dto.activity.ActivityUpdateRequest;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.ActivityStatusEnum;
import com.coder.springbootinit.model.enums.ActivityTypeEnum;
import com.coder.springbootinit.model.vo.ActivityVO;
import com.coder.springbootinit.service.ActivityService;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 活动服务实现
 *
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Resource
    private UserService userService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ActivityEnrollMapper activityEnrollMapper;

    @Override
    public Activity addActivity(ActivityAddRequest activityAddRequest, Long userId) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityAddRequest, activity);
        activity.setCurrentNum(0);
        activity.setUserId(userId);
        boolean result = this.save(activity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return activity;
    }

    @Override
    public boolean updateActivity(ActivityUpdateRequest activityUpdateRequest) {
        Activity activity = this.getById(activityUpdateRequest.getId());
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);

        activity.setActivityName(activityUpdateRequest.getActivityName());
        activity.setOrgId(activityUpdateRequest.getOrgId());
        activity.setActivityType(activityUpdateRequest.getActivityType());
        activity.setActivityContent(activityUpdateRequest.getActivityContent());
        activity.setEnrollDeadline(activityUpdateRequest.getEnrollDeadline());
        activity.setStartTime(activityUpdateRequest.getStartTime());
        activity.setEndTime(activityUpdateRequest.getEndTime());
        activity.setLocation(activityUpdateRequest.getLocation());
        activity.setMaxNum(activityUpdateRequest.getMaxNum());
        activity.setStatus(activityUpdateRequest.getStatus());
        activity.setReviewContent(activityUpdateRequest.getReviewContent());
        activity.setUpdateTime(new Date());

        return this.updateById(activity);
    }

    @Override
    public boolean deleteActivity(Long id) {
        return this.removeById(id);
    }

    @Override
    public ActivityVO getActivityVO(Long id) {
        Activity activity = this.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        return this.activityToVO(activity);
    }

    @Override
    public List<ActivityVO> listActivityVO(ActivityQueryRequest activityQueryRequest) {
        QueryWrapper<Activity> queryWrapper = this.getQueryWrapper(activityQueryRequest);
        List<Activity> activityList = this.list(queryWrapper);
        List<ActivityVO> activityVOList = new ArrayList<>();
        for (Activity activity : activityList) {
            activityVOList.add(this.activityToVO(activity));
        }
        return activityVOList;
    }

    @Override
    public QueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest activityQueryRequest) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        if (activityQueryRequest == null) {
            return queryWrapper;
        }
        Long id = activityQueryRequest.getId();
        String activityName = activityQueryRequest.getActivityName();
        Long orgId = activityQueryRequest.getOrgId();
        Long userId = activityQueryRequest.getUserId();
        Integer activityType = activityQueryRequest.getActivityType();
        Integer status = activityQueryRequest.getStatus();
        Date startTime = activityQueryRequest.getStartTime();
        Date endTime = activityQueryRequest.getEndTime();
        String location = activityQueryRequest.getLocation();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(activityName), "activityName", activityName);
        queryWrapper.eq(orgId != null, "orgId", orgId);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(activityType != null, "activityType", activityType);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.ge(startTime != null, "startTime", startTime);
        queryWrapper.le(endTime != null, "endTime", endTime);
        queryWrapper.like(StringUtils.isNotBlank(location), "location", location);
        queryWrapper.orderByDesc("createTime");

        return queryWrapper;
    }

    @Override
    public boolean publishActivity(Long id) {
        Activity activity = this.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        activity.setStatus(ActivityStatusEnum.PUBLISHED.getCode()); // 已发布
        return this.updateById(activity);
    }

    @Override
    public boolean endActivity(Long id) {
        Activity activity = this.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        activity.setStatus(ActivityStatusEnum.ENDED.getCode()); // 已结束
        return this.updateById(activity);
    }

    @Override
    public boolean decreaseCurrentNum(Long id) {
        // 使用XML中定义的SQL语句执行自减操作
        int result = baseMapper.decreaseCurrentNum(id);
        return result > 0;
    }

    /**
     * 将活动实体转换为VO
     * @param activity 活动实体
     * @return 活动VO
     */
    private ActivityVO activityToVO(Activity activity) {
        ActivityVO activityVO = new ActivityVO();
        BeanUtils.copyProperties(activity, activityVO);

        // 设置党组织名称
        if (activity.getOrgId() != null) {
            Organization organization = organizationService.getById(activity.getOrgId());
            if (organization != null) {
                activityVO.setOrgName(organization.getOrgName());
            }
        }

        // 设置创建人姓名
        if (activity.getUserId() != null) {
            User user = userService.getById(activity.getUserId());
            if (user != null) {
                activityVO.setUserName(user.getUserName());
            }
        }

        // 设置活动类型名称
        ActivityTypeEnum activityTypeEnum = ActivityTypeEnum.getByCode(activity.getActivityType());
        activityVO.setActivityTypeName(activityTypeEnum != null ? activityTypeEnum.getDescription() : "未知");

        // 设置活动状态名称
        ActivityStatusEnum activityStatusEnum = ActivityStatusEnum.getByCode(activity.getStatus());
        activityVO.setStatusName(activityStatusEnum != null ? activityStatusEnum.getDescription() : "未知");

        return activityVO;
    }
}