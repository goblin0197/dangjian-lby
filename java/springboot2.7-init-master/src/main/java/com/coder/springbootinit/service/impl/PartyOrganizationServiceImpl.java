package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.constant.CommonConstant;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.PartyOrganizationMapper;
import com.coder.springbootinit.model.dto.PartyOrganization.PartyOrganizationBindRequest;
import com.coder.springbootinit.model.dto.PartyOrganization.PartyOrganizationQueryRequest;
import com.coder.springbootinit.model.entity.PartyOrganization;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.PartyOrganizationVO;
import com.coder.springbootinit.service.PartyOrganizationService;
import com.coder.springbootinit.service.UserService;
import com.coder.springbootinit.utils.SqlUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import javax.annotation.Resource;

/**
 * 党组织表 Service 实现类
 *
 */
@Service
public class PartyOrganizationServiceImpl extends ServiceImpl<PartyOrganizationMapper, PartyOrganization> implements PartyOrganizationService {
    
    @Resource
    private  UserService userService;

    @Override
    public boolean createPartyOrganization(PartyOrganization partyOrganization) {
        // 验证组织名称和编码是否为空
        if (partyOrganization.getOrgName() == null || partyOrganization.getOrgCode() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织名称和编码不能为空");
        }
        // 保存党组织信息
        boolean result = this.save(partyOrganization);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建党组织失败");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePartyOrganization(PartyOrganization partyOrganization) {
        // 验证党组织ID是否为空
        if (partyOrganization.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织ID不能为空");
        }
        
        if(partyOrganization.getLeaderId() != null){
            boolean bindResult = this.bindPartyOrganizationLeader(partyOrganization.getId(), partyOrganization.getLeaderId());
            if(!bindResult){
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "绑定党组织负责人失败");
            }
        }
        // 更新党组织信息
        boolean result = this.updateById(partyOrganization);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新党组织失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePartyOrganization(Long id) {
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
    public QueryWrapper<PartyOrganization> getQueryWrapper(
            PartyOrganizationQueryRequest partyOrganizationQueryRequest) {
        if (partyOrganizationQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询参数不能为空");
        }
        Long id = partyOrganizationQueryRequest.getId();
        String orgName = partyOrganizationQueryRequest.getOrgName();
        String orgCode = partyOrganizationQueryRequest.getOrgCode();
        Long parentId = partyOrganizationQueryRequest.getParentId();
        String orgType = partyOrganizationQueryRequest.getOrgType();
        Integer orgLevel = partyOrganizationQueryRequest.getOrgLevel();
        Long leaderId = partyOrganizationQueryRequest.getLeaderId();
        String address = partyOrganizationQueryRequest.getAddress();
        String description = partyOrganizationQueryRequest.getDescription();
        Date createTime = partyOrganizationQueryRequest.getCreateTime();
        Integer isDelete = partyOrganizationQueryRequest.getIsDelete();
        String sortField = partyOrganizationQueryRequest.getSortField();
        String sortOrder = partyOrganizationQueryRequest.getSortOrder();
        QueryWrapper<PartyOrganization> queryWrapper = new QueryWrapper<>();
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
    public boolean bindPartyOrganizationLeader(Long orgId, Long leaderId) {
        // 验证党组织ID和负责人ID是否为空
        if (orgId == null || leaderId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织ID和负责人ID不能为空");
        }
        // 检查党组织是否存在
        PartyOrganization partyOrganization = this.getById(orgId);
        if (partyOrganization == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织不存在");
        }
        // 检查负责人是否存在
        User leader = userService.getById(leaderId);
        if (leader == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "负责人不存在");
        }
        if(!UserConstant.POLITICAL_STATUS_PARTY_MEMBER.equals(leader.getPoliticalStatus())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "负责人必须是党员");
        }
        // 更新党组织负责人ID
        PartyOrganization updatePartyOrganization = new PartyOrganization();
        updatePartyOrganization.setId(orgId);
        updatePartyOrganization.setLeaderId(leaderId);
        boolean result = this.updateById(updatePartyOrganization);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "绑定党组织负责人失败");
        }
        return true;
        
    }

    @Override
    public PartyOrganizationVO fillPartyOrganizationLeader(PartyOrganization partyOrganization, Long leaderId) {
        PartyOrganizationVO partyOrganizationVO = new PartyOrganizationVO();
        BeanUtils.copyProperties(partyOrganization, partyOrganizationVO);
        // 填充关联的负责人信息
        User leader = userService.getById(partyOrganization.getLeaderId());
        if (leader != null) {
            partyOrganizationVO.setLeader(leader);
        }
        return partyOrganizationVO;
    }
}
