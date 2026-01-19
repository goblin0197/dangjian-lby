package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.Activity;

/**
 * 活动数据库操作
 *
*/
public interface ActivityMapper extends BaseMapper<Activity> {

    /**
     * 活动当前参与人数自减1
     * @param id 活动ID
     * @return 影响的行数
     */
    int decreaseCurrentNum(Long id);

}