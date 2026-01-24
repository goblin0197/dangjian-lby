package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.OrgQuantifyMapper;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.entity.ActivityEnroll;
import com.coder.springbootinit.model.entity.OrgQuantify;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.enums.ActivityEnrollSignEnum;
import com.coder.springbootinit.model.enums.ActivityEnrollStatusEnum;
import com.coder.springbootinit.service.ActivityEnrollService;
import com.coder.springbootinit.service.ActivityService;
import com.coder.springbootinit.service.OrgQuantifyService;
import com.coder.springbootinit.service.OrganizationService;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 组织量化统计ServiceImpl
 *
 */
@Service
@Slf4j
public class OrgQuantifyServiceImpl extends ServiceImpl<OrgQuantifyMapper, OrgQuantify> implements OrgQuantifyService {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityEnrollService activityEnrollService;


    @Override
    public boolean generateOrgQuantifyData(Long orgId) {
        // 验证组织是否存在
        Organization organization = organizationService.getById(orgId);
        if (organization == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织不存在");
        }

        // 获取统计日期（当前日期）
        Date statDate = new Date();

        // 1. 获取当前组织及其所有子组织的ID列表
        List<Long> orgIdList = organizationService.getAllSubOrgIds(orgId);
        
        // 2. 统计组织的活动次数（包含所有子组织）
        QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
        activityQueryWrapper.in("orgId", orgIdList);
        List<Activity> activityList = activityService.list(activityQueryWrapper);
        int activityCount = activityList.size();

        // 2. 统计总参与人数和总签到人数
        int totalParticipant = 0;
        int totalSign = 0;

        for (Activity activity : activityList) {
            // 统计该活动的报名参与人数
            QueryWrapper<ActivityEnroll> enrollQueryWrapper = new QueryWrapper<>();
            enrollQueryWrapper.eq("activityId", activity.getId());
            enrollQueryWrapper.eq("participantStatus", ActivityEnrollStatusEnum.ENROLLED.getCode()); // 已报名
            int participantCount = Math.toIntExact(activityEnrollService.count(enrollQueryWrapper));
            totalParticipant += participantCount;

            // 统计该活动的签到人数(实际参与)
            QueryWrapper<ActivityEnroll> signQueryWrapper = new QueryWrapper<>();
            signQueryWrapper.eq("activityId", activity.getId());
            signQueryWrapper.eq("participantStatus", ActivityEnrollStatusEnum.ENROLLED.getCode()); // 已报名
            signQueryWrapper.eq("isSign", ActivityEnrollSignEnum.SIGNED.getCode()); // 已签到
            int signCount = Math.toIntExact(activityEnrollService.count(signQueryWrapper));
            totalSign += signCount;
        }

        // 3. 计算平均每次活动报名人数和平均每次活动签到人数，并保留两位小数
        double avgParticipant = activityCount > 0 ? (double) totalParticipant / activityCount : 0.0;
        double avgSign = activityCount > 0 ? (double) totalSign / activityCount : 0.0;
        // 保留两位小数
        avgParticipant = Math.round(avgParticipant * 100.0) / 100.0;
        avgSign = Math.round(avgSign * 100.0) / 100.0;

        // 创建或更新组织量化统计数据
        OrgQuantify orgQuantify = new OrgQuantify();
        orgQuantify.setOrgId(orgId);
        orgQuantify.setStatDate(statDate);
        orgQuantify.setActivityCount(activityCount);
        orgQuantify.setTotalParticipant(totalParticipant);
        orgQuantify.setTotalSign(totalSign);
        orgQuantify.setAvgParticipant(avgParticipant);
        orgQuantify.setAvgSign(avgSign);

        // 检查是否已存在该组织当天的统计数据
        QueryWrapper<OrgQuantify> quantifyQueryWrapper = new QueryWrapper<>();
        quantifyQueryWrapper.eq("orgId", orgId);
        OrgQuantify existing = this.getOne(quantifyQueryWrapper);

        if (existing != null) {
            // 更新已存在的数据
            orgQuantify.setId(existing.getId());
            return this.updateById(orgQuantify);
        } else {
            // 保存新数据
            return this.save(orgQuantify);
        }
    }

    @Override
    public boolean generateAllOrgQuantifyData() {
        // 获取所有组织
        List<Organization> organizationList = organizationService.list(null);
        boolean allSuccess = true;

        for (Organization organization : organizationList) {
            boolean success = generateOrgQuantifyData(organization.getId());
            if (!success) {
                allSuccess = false;
                log.error("Failed to update quantify data for organization: {}", organization.getId());
            }
        }

        return allSuccess;
    }
}
