package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.OrgQuantify;

/**
 * 组织量化统计Service
 *
 */
public interface OrgQuantifyService extends IService<OrgQuantify> {

    /**
     * 生成指定组织的量化统计数据
     *
     * @param orgId 组织ID
     * @return 是否成功
     */
    boolean generateOrgQuantifyData(Long orgId);

    /**
     * 生成所有组织的量化统计数据
     *
     * @return 是否成功
     */
    boolean generateAllOrgQuantifyData();
}
