package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.ActivityEnroll;
import com.coder.springbootinit.model.vo.ActivityEnrollUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动报名记录数据库操作
 *
 */
public interface ActivityEnrollMapper extends BaseMapper<ActivityEnroll> {

    /**
     * 查询指定活动的已报名人员列表（只返回已报名未取消的人员）
     * @param activityId 活动ID
     * @return 已报名人员列表
     */
    List<ActivityEnrollUserVO> listEnrolledUsers(@Param("activityId") Long activityId);
}