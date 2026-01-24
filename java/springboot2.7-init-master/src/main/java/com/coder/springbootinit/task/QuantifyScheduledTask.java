package com.coder.springbootinit.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.entity.ActivityEnroll;
import com.coder.springbootinit.service.ActivityEnrollService;
import com.coder.springbootinit.service.ActivityService;
import com.coder.springbootinit.service.OrgQuantifyService;
import com.coder.springbootinit.service.UserQuantifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 量化统计定时任务
 *
 */
@Component
@Slf4j
public class QuantifyScheduledTask {

    @Resource
    private UserQuantifyService userQuantifyService;

    @Resource
    private OrgQuantifyService orgQuantifyService;

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityEnrollService activityEnrollService;

    /**
     * 每日凌晨1点自动生成所有量化统计数据
     * cron表达式：秒 分 时 日 月 周
     * 0 0 1 * * ? 表示每天凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void generateQuantifyDataDaily() {
        log.info("开始执行每日量化统计数据生成任务");

        try {
            // 更新所有活动的参与和签到情况统计
            updateActivityStatistics();

            // 生成所有用户的量化统计数据
            boolean userResult = userQuantifyService.generateAllUserQuantifyData();
            if (userResult) {
                log.info("用户量化统计数据生成成功");
            } else {
                log.error("用户量化统计数据生成失败");
            }

            // 生成所有组织的量化统计数据
            boolean orgResult = orgQuantifyService.generateAllOrgQuantifyData();
            if (orgResult) {
                log.info("组织量化统计数据生成成功");
            } else {
                log.error("组织量化统计数据生成失败");
            }

            log.info("每日量化统计数据生成任务执行完成");
        } catch (Exception e) {
            log.error("每日量化统计数据生成任务执行异常", e);
        }
    }

    /**
     * 更新所有活动的参与和签到情况统计
     */
    private void updateActivityStatistics() {
        log.info("开始更新活动参与和签到情况统计");

        // 获取所有未删除的活动
        List<Activity> activityList = activityService.list();

        for (Activity activity : activityList) {
            boolean result = activityService.updateActivityStatisticsByActivity(activity);
            if (!result) {
                log.error("更新活动统计信息失败，活动ID：{}", activity.getId());
            }
        }
        log.info("活动参与和签到情况统计更新完成");
    }
}
