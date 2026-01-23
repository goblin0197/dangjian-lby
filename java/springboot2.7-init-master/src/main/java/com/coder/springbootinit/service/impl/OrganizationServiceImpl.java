package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.constant.CommonConstant;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.OrganizationMapper;
import com.coder.springbootinit.model.dto.organization.OrganizationQueryRequest;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.OrganizationVO;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.UserService;
import com.coder.springbootinit.utils.SqlUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * 党组织表 Service 实现类
 *
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {
    
    @Resource
    private  UserService userService;

    @Override
    public boolean createOrganization(Organization organization) {
        // 验证组织名称和编码是否为空
        if (organization.getOrgName() == null || organization.getOrgCode() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织名称和编码不能为空");
        }
        // 保存党组织信息
        boolean result = this.save(organization);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建党组织失败");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrganization(Organization organization) {
        // 验证党组织ID是否为空
        if (organization.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织ID不能为空");
        }
        
        if(organization.getLeaderId() != null){
            boolean bindResult = this.bindOrganizationLeader(organization.getId(), organization.getLeaderId());
            if(!bindResult){
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "绑定党组织负责人失败");
            }
        }
        // 更新党组织信息
        boolean result = this.updateById(organization);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新党组织失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrganization(Long id) {
        // 验证党组织ID是否为空
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织ID不能为空");
        }
        // 删除党组织信息
        boolean result = this.removeById(id);
        // TODO 级联删除其他关系关联，如党员、党员关系等
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除党组织失败");
        }
        return true;
    }

    @Override
    public QueryWrapper<Organization> getQueryWrapper(
            OrganizationQueryRequest organizationQueryRequest) {
        if (organizationQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询参数不能为空");
        }
        Long id = organizationQueryRequest.getId();
        String orgName = organizationQueryRequest.getOrgName();
        String orgCode = organizationQueryRequest.getOrgCode();
        Long parentId = organizationQueryRequest.getParentId();
        String orgType = organizationQueryRequest.getOrgType();
        Integer orgLevel = organizationQueryRequest.getOrgLevel();
        Long leaderId = organizationQueryRequest.getLeaderId();
        String address = organizationQueryRequest.getAddress();
        String description = organizationQueryRequest.getDescription();
        Date createTime = organizationQueryRequest.getCreateTime();
        Integer isDelete = organizationQueryRequest.getIsDelete();
        String sortField = organizationQueryRequest.getSortField();
        String sortOrder = organizationQueryRequest.getSortOrder();
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(orgName), "orgName", orgName);
        queryWrapper.like(StringUtils.isNotBlank(orgCode), "orgCode", orgCode);
        queryWrapper.eq(parentId != null, "parentId", parentId);
        queryWrapper.eq(StringUtils.isNotBlank(orgType), "orgType", orgType);
        queryWrapper.eq(orgLevel != null, "orgLevel", orgLevel);
        queryWrapper.eq(leaderId != null, "leaderId", leaderId);
        queryWrapper.like(StringUtils.isNotBlank(address), "address", address);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.eq(createTime != null, "createTime", createTime);
        queryWrapper.eq(isDelete != null, "isDelete", isDelete);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public boolean bindOrganizationLeader(Long orgId, Long leaderId) {
        // 验证党组织ID和负责人ID是否为空
        if (orgId == null || leaderId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织ID和负责人ID不能为空");
        }
        // 检查党组织是否存在
        Organization organization = this.getById(orgId);
        if (organization == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织不存在");
        }
        // 检查负责人是否存在
        User leader = userService.getById(leaderId);
        if (leader == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "负责人不存在");
        }
        if(!UserConstant.POLITICAL_STATUS_ORG_MEMBER.equals(leader.getPoliticalStatus())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "负责人必须是党员");
        }
        // 更新党组织负责人ID
        Organization updateOrganization = new Organization();
        updateOrganization.setId(orgId);
        updateOrganization.setLeaderId(leaderId);
        boolean result = this.updateById(updateOrganization);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "绑定党组织负责人失败");
        }
        return true;
        
    }

    @Override
    public OrganizationVO fillOrganizationLeader(Organization organization) {
        OrganizationVO organizationVO = new OrganizationVO();
        BeanUtils.copyProperties(organization, organizationVO);
        // 填充关联的负责人信息
        User leader = userService.getById(organization.getLeaderId());
        if (leader != null) {
            organizationVO.setLeader(leader);
        }
        return organizationVO;
    }

    @Override
    public List<Long> getAllSubOrgIds(Long orgId) {
        List<Long> orgIdList = new ArrayList<>();
        // 先添加当前组织ID
        orgIdList.add(orgId);
        // 递归获取所有子组织ID
        getSubOrgIdsRecursive(orgId, orgIdList);
        return orgIdList;
    }

    /**
     * 递归获取所有子组织ID
     * @param orgId 当前组织ID
     * @param orgIdList 组织ID列表，用于存储结果
     */
    private void getSubOrgIdsRecursive(Long orgId, List<Long> orgIdList) {
        // 查询直接子组织
        List<Organization> subOrgList = this.lambdaQuery().eq(Organization::getParentId, orgId).list();
        
        // 遍历子组织，添加到结果列表，并递归获取其子组织
        for (Organization subOrg : subOrgList) {
            orgIdList.add(subOrg.getId());
            getSubOrgIdsRecursive(subOrg.getId(), orgIdList);
        }
    }
    
    @Override
    public List<Long> getAllParentOrgIds(Long orgId) {
        List<Long> orgIdList = new ArrayList<>();
        // 先添加当前组织ID
        orgIdList.add(orgId);
        // 递归获取所有父组织ID
        getParentOrgIdsRecursive(orgId, orgIdList);
        return orgIdList;
    }
    
    /**
     * 递归获取所有父组织ID
     * @param orgId 当前组织ID
     * @param orgIdList 组织ID列表，用于存储结果
     */
    private void getParentOrgIdsRecursive(Long orgId, List<Long> orgIdList) {
        // 查询当前组织的父组织
        Organization currentOrg = this.getById(orgId);
        if (currentOrg != null && currentOrg.getParentId() != null) {
            // 添加父组织ID到结果列表
            Long parentId = currentOrg.getParentId();
            orgIdList.add(parentId);
            // 递归获取父组织的父组织
            getParentOrgIdsRecursive(parentId, orgIdList);
        }
    }
}
