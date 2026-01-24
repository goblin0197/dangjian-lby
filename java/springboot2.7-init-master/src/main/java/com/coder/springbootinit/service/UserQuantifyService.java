package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.UserQuantify;

/**
 * 用户量化统计Service
 *
 */
public interface UserQuantifyService extends IService<UserQuantify> {

    /**
     * 生成指定用户的量化统计数据
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean generateUserQuantifyData(Long userId);

    /**
     * 生成所有用户的量化统计数据
     *
     * @return 是否成功
     */
    boolean generateAllUserQuantifyData();
}
