package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.PartyOrganization.PartyOrganizationQueryRequest;
import com.coder.springbootinit.model.entity.PartyOrganization;
import com.coder.springbootinit.model.vo.PartyOrganizationVO;

import java.util.Map;

/**
 * 党组织表 Service 接口
 *
 */
public interface PartyOrganizationService extends IService<PartyOrganization> {
    
    /**
     * 创建党组织
     * @param partyOrganization 党组织信息
     * @return 党组织对象
     */
    boolean createPartyOrganization(PartyOrganization partyOrganization);
    
    /**
     * 更新党组织信息
     * @param partyOrganization 党组织信息
     * @return 是否更新成功
     */
    boolean updatePartyOrganization(PartyOrganization partyOrganization);
    
    /**
     * 删除党组织
     * @param id 党组织ID
     * @return 是否删除成功
     */
    boolean deletePartyOrganization(Long id);

    /**
     * 获取查询条件
     *
     * @param partyOrganizationQueryRequest 查询条件
     * @return 查询包装器
     */
    QueryWrapper<PartyOrganization> getQueryWrapper(PartyOrganizationQueryRequest partyOrganizationQueryRequest);

    /**
     * 绑定党组织
     * @param orgId 党组织ID
     * @param leaderId 负责人ID
     * @return 是否绑定成功
     */
    boolean bindPartyOrganizationLeader(Long orgId, Long leaderId);

    /**
     * 填充党组织负责人
     * @param partyOrganization 党组织信息
     * @return 是否填充成功
     */
    PartyOrganizationVO fillPartyOrganizationLeader(PartyOrganization partyOrganization);
}