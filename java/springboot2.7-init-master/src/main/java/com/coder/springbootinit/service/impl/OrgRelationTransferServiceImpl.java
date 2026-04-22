package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.OrgRelationTransferMapper;
import com.coder.springbootinit.model.entity.OrgRelationTransfer;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.ApproveStatusEnum;
import com.coder.springbootinit.service.OrgRelationTransferService;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织关系转移表 Service 实现类
 *
 */
@Service
public class OrgRelationTransferServiceImpl extends ServiceImpl<OrgRelationTransferMapper, OrgRelationTransfer> implements OrgRelationTransferService {

    @Resource
    private UserService userService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private OrgRelationTransferMapper orgRelationTransferMapper;

    @Override
    public boolean createOrgRelationTransfer(OrgRelationTransfer orgRelationTransfer) {
        // 验证用户ID、原党组织ID和目标党组织ID是否为空
        if (orgRelationTransfer.getUserId() == null || orgRelationTransfer.getFromOrgId() == null || orgRelationTransfer.getToOrgId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID、原党组织ID和目标党组织ID不能为空");
        }
        long userId = orgRelationTransfer.getUserId();
        long fromOrgId = orgRelationTransfer.getFromOrgId();
        long toOrgId = orgRelationTransfer.getToOrgId();
        Long count = orgRelationTransferMapper.getPendingCountByUserId(userId);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已存在待处理的组织关系转移记录");
        }
        List<String> nameList = this.validOrgRelationTransfer(userId, fromOrgId, toOrgId);
        if (nameList.size() != 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        orgRelationTransfer.setFromOrgName(nameList.get(0));
        orgRelationTransfer.setToOrgName(nameList.get(1));
        // 设置默认值
        orgRelationTransfer.setTransferTime(new Date());
        orgRelationTransfer.setApproveStatus(ApproveStatusEnum.PENDING.getCode());
        // 保存组织关系转移记录
        boolean result = this.save(orgRelationTransfer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建组织关系转移记录失败");
        }
        return result;
    }

    @Override
    public boolean updateOrgRelationTransfer(OrgRelationTransfer orgRelationTransfer) {
        // 验证组织关系转移ID是否为空
        if (orgRelationTransfer.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        this.validOrgRelationTransfer(orgRelationTransfer.getUserId(), orgRelationTransfer.getFromOrgId(), orgRelationTransfer.getToOrgId());
        // 更新组织关系转移记录
        boolean result = this.updateById(orgRelationTransfer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新组织关系转移记录失败");
        }
        return result;
    }

    @Override
    public boolean deleteOrgRelationTransfer(Long id) {
        // 验证组织关系转移ID是否为空
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织关系转移ID不能为空");
        }
        // 删除组织关系转移记录
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除组织关系转移记录失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveOrgRelationTransfer(long id, int approveStatus, long approveUserId,String approveUserName, String approveComment) {
        // 验证组织关系转移ID和审批状态是否为空
        if (id < 0 || !ApproveStatusEnum.getCodes().contains(approveStatus) || approveUserId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取组织关系转移记录
        OrgRelationTransfer orgRelationTransfer = this.getById(id);
        if (orgRelationTransfer == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织关系转移记录不存在");
        }
        // 更新审批状态
        // 不检查是否已审批，允许修改审批结果
        orgRelationTransfer.setApproveStatus(approveStatus);
        orgRelationTransfer.setApproveUserId(approveUserId);
        orgRelationTransfer.setApproveUserName(approveUserName);
        orgRelationTransfer.setApproveTime(new Date());
        orgRelationTransfer.setApproveComment(approveComment);
        // 保存审批结果
        boolean result = this.updateById(orgRelationTransfer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "审批组织关系转移记录失败");
        }
        // 如果审批通过，更新用户的党组织ID
        if (ApproveStatusEnum.APPROVED.equals(ApproveStatusEnum.getByCode(approveStatus))) {
            User user = userService.getById(orgRelationTransfer.getUserId());
            if (user != null) {
                User updateUser = new User();
                updateUser.setId(user.getId());
                updateUser.setOrgId(orgRelationTransfer.getToOrgId());
                if(UserConstant.POLITICAL_STATUS_TEAM_UNION_MEMBER.equals(user.getPoliticalStatus())){
                    // 如果是共青团员就转为预备党员
                    updateUser.setPoliticalStatus(UserConstant.POLITICAL_STATUS_PROBATIONARY_ORG_MEMBER);
                }
                boolean updated = userService.updateById(updateUser);
                if (!updated) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新用户党组织ID失败");
                }
            }
        }
        return true;
    }

    @Override
    public List<OrgRelationTransfer> getOrgRelationTransfersByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        return this.lambdaQuery().eq(OrgRelationTransfer::getUserId, userId).list();
    }

    @Override
    public List<OrgRelationTransfer> getOrgRelationTransfersByOrgId(Long orgId) {
        if (orgId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织ID不能为空");
        }
        // 返回该党组织相关的所有转移记录
        return this.lambdaQuery()
                .eq(OrgRelationTransfer::getFromOrgId, orgId)
                .or()
                .eq(OrgRelationTransfer::getToOrgId, orgId)
                .list();
    }

    @Override
    public List<String> validOrgRelationTransfer(long userId, long fromOrgId, long toOrgId) {
        if (userId <= 0 || fromOrgId < 0 || toOrgId < 0 || fromOrgId == toOrgId) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<String> nameList = new ArrayList<>();
        // 验证用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        if(user.getOrgId() != fromOrgId){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不是该党组织的成员");
        }
        if(fromOrgId > 0){
            // 验证原党组织是否存在
            Organization orgOrganization = organizationService.getById(fromOrgId);
            if (orgOrganization == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "原党组织不存在");
            }
            nameList.add(orgOrganization.getOrgName());
        }else
            nameList.add("无");
        if(toOrgId > 0){
            // 验证目标党组织是否存在
            Organization orgOrganization = organizationService.getById(toOrgId);
            if (orgOrganization == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "目标党组织不存在");
            }
            nameList.add(orgOrganization.getOrgName());
        }else
            nameList.add("无");
        return nameList;
    }
}