package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.UserQuantifyMapper;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.entity.ActivityEnroll;
import com.coder.springbootinit.model.entity.MyFile;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.entity.UserQuantify;
import com.coder.springbootinit.model.enums.ActivityEnrollSignEnum;
import com.coder.springbootinit.model.enums.ActivityEnrollStatusEnum;
import com.coder.springbootinit.service.ActivityEnrollService;
import com.coder.springbootinit.service.ActivityService;
import com.coder.springbootinit.service.FileService;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.UserQuantifyService;
import com.coder.springbootinit.service.UserService;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户量化统计ServiceImpl
 *
 */
@Service
@Slf4j
public class UserQuantifyServiceImpl extends ServiceImpl<UserQuantifyMapper, UserQuantify> implements UserQuantifyService {

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityEnrollService activityEnrollService;

    @Resource
    private FileService fileService;

    @Resource
    private OrganizationService organizationService;

    @Override
    public boolean generateUserQuantifyData(Long userId) {
        // 验证用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }

        // 获取统计日期（当前日期）
        Date statDate = new Date();

        // 1. 统计用户所在组织及其所有父组织的总活动数
        List<Long> orgIdList = organizationService.getAllParentOrgIds(user.getOrgId());
        QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
        activityQueryWrapper.in("orgId", orgIdList);
        int totalActivity = Math.toIntExact(activityService.count(activityQueryWrapper));

        // 2. 统计用户参与的活动数
        QueryWrapper<ActivityEnroll> enrollQueryWrapper = new QueryWrapper<>();
        enrollQueryWrapper.eq("userId", userId);
        enrollQueryWrapper.eq("participantStatus",ActivityEnrollStatusEnum.ENROLLED.getCode()); // 已报名
        List<ActivityEnroll> enrollList = activityEnrollService.list(enrollQueryWrapper);
        int participateActivity = enrollList.size();

        // 3. 计算活动参与度，并保留两位小数
        double participateRate = totalActivity > 0 ? (double) participateActivity / totalActivity : 0.0;
        // 保留两位小数
        participateRate = Math.round(participateRate * 100.0) / 100.0;

        // 4. 统计用户报名且签到的活动数（有效参加数）
        QueryWrapper<ActivityEnroll> signQueryWrapper = new QueryWrapper<>();
        signQueryWrapper.eq("userId", userId);
        signQueryWrapper.eq("participantStatus",ActivityEnrollStatusEnum.ENROLLED.getCode()); // 已报名
        signQueryWrapper.eq("isSign", ActivityEnrollSignEnum.SIGNED.getCode()); // 已签到
        int signActivity = Math.toIntExact(activityEnrollService.count(signQueryWrapper));

        // 5. 计算签到率，并保留两位小数
        double signRate = participateActivity > 0 ? (double) signActivity / participateActivity : 0.0;
        // 保留两位小数
        signRate = Math.round(signRate * 100.0) / 100.0;

        // 6. 统计用户上传的材料文件数量
        QueryWrapper<MyFile> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("userId", userId);
        int fileCount = Math.toIntExact(fileService.count(fileQueryWrapper));

        // 创建或更新用户量化统计数据
        UserQuantify userQuantify = new UserQuantify();
        userQuantify.setUserId(userId);
        userQuantify.setStatDate(statDate);
        userQuantify.setTotalActivity(totalActivity);
        userQuantify.setParticipateActivity(participateActivity);
        userQuantify.setParticipateRate(participateRate);
        userQuantify.setSignActivity(signActivity);
        userQuantify.setSignRate(signRate);
        userQuantify.setFileCount(fileCount);

        // 检查是否已存在该用户的统计数据(每个用户只有一条数据)
        QueryWrapper<UserQuantify> quantifyQueryWrapper = new QueryWrapper<>();
        quantifyQueryWrapper.eq("userId", userId);
        UserQuantify existing = this.getOne(quantifyQueryWrapper);

        if (existing != null) {
            // 更新已存在的数据
            userQuantify.setId(existing.getId());
            return this.updateById(userQuantify);
        } else {
            // 保存新数据
            return this.save(userQuantify);
        }
    }

    @Override
    public boolean generateAllUserQuantifyData() {
        // 获取所有用户
        List<User> userList = userService.list();
        boolean allSuccess = true;

        for (User user : userList) {
            boolean success = generateUserQuantifyData(user.getId());
            if (!success) {
                allSuccess = false;
                log.error("Failed to update quantify data for user: {}", user.getId());
            }
        }

        return allSuccess;
    }
}
