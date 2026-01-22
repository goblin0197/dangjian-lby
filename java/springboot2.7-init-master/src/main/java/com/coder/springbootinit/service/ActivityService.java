package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.activity.ActivityAddRequest;
import com.coder.springbootinit.model.dto.activity.ActivityQueryRequest;
import com.coder.springbootinit.model.dto.activity.ActivityUpdateRequest;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.vo.ActivityVO;
import java.util.List;

/**
 * 活动服务
 *
*/
public interface ActivityService extends IService<Activity> {

    /**
     * 添加活动
     * @param activityAddRequest 活动添加请求
     * @param userId 创建人ID
     * @return 活动ID
     */
    Activity addActivity(ActivityAddRequest activityAddRequest, Long userId);

    /**
     * 更新活动
     * @param activityUpdateRequest 活动更新请求
     * @return 是否成功
     */
    boolean updateActivity(ActivityUpdateRequest activityUpdateRequest);

    /**
     * 删除活动
     * @param id 活动ID
     * @return 是否成功
     */
    boolean deleteActivity(Long id);

    /**
     * 获取活动详情
     * @param id 活动ID
     * @return 活动VO
     */
    ActivityVO getActivityVO(Long id);

    /**
     * 获取活动列表
     * @param activityQueryRequest 活动查询请求
     * @return 活动VO列表
     */
    List<ActivityVO> listActivityVO(ActivityQueryRequest activityQueryRequest);

    /**
     * 获取查询条件
     * @param activityQueryRequest 活动查询请求
     * @return 查询条件
     */
    QueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest activityQueryRequest);

    /**
     * 发布活动
     * @param id 活动ID
     * @return 是否成功
     */
    boolean publishActivity(Long id);

    /**
     * 结束活动
     * @param id 活动ID
     * @return 是否成功
     */
    boolean endActivity(Long id);

    /**
     * 活动当前参与人数自减1
     * @param id 活动ID
     * @return 是否成功
     */
    boolean decreaseCurrentNum(Long id);

    /**
     * 更新指定活动的参与和签到情况统计
     * @param id 活动ID
     * @return 是否成功
     */
    boolean updateActivityStatistics(Long id);

    /**
     * 更新所有活动的参与和签到情况统计
     * @return 是否成功
     */
    boolean updateAllActivityStatistics();
}