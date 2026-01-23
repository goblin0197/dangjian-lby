package com.coder.springbootinit.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.organization.OrganizationQueryRequest;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.vo.OrganizationVO;

/**
 * 党组织表 Service 接口
 *
 */
public interface OrganizationService extends IService<Organization> {
    
    /**
     * 创建党组织
     * @param organization 党组织信息
     * @return 党组织对象
     */
    boolean createOrganization(Organization organization);
    
    /**
     * 更新党组织信息
     * @param organization 党组织信息
     * @return 是否更新成功
     */
    boolean updateOrganization(Organization organization);
    
    /**
     * 删除党组织
     * @param id 党组织ID
     * @return 是否删除成功
     */
    boolean deleteOrganization(Long id);

    /**
     * 获取查询条件
     *
     * @param organizationQueryRequest 查询条件
     * @return 查询包装器
     */
    QueryWrapper<Organization> getQueryWrapper(OrganizationQueryRequest organizationQueryRequest);

    /**
     * 绑定党组织
     * @param orgId 党组织ID
     * @param leaderId 负责人ID
     * @return 是否绑定成功
     */
    boolean bindOrganizationLeader(Long orgId, Long leaderId);

    /**
     * 填充党组织负责人
     * @param organization 党组织信息
     * @return 是否填充成功
     */
    OrganizationVO fillOrganizationLeader(Organization organization);

    /**
     * 获取指定组织及其所有子组织（递归）的ID列表
     * @param orgId 组织ID
     * @return 组织ID列表
     */
    List<Long> getAllSubOrgIds(Long orgId);
    
    /**
     * 获取指定组织及其所有父组织（递归）的ID列表
     * @param orgId 组织ID
     * @return 组织ID列表
     */
    List<Long> getAllParentOrgIds(Long orgId);
}