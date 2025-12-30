package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.PartyRelationTransferMapper;
import com.coder.springbootinit.model.entity.PartyOrganization;
import com.coder.springbootinit.model.entity.PartyRelationTransfer;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.ApproveStatusEnum;
import com.coder.springbootinit.model.vo.PartyRelationTransferVO;
import com.coder.springbootinit.service.PartyOrganizationService;
import com.coder.springbootinit.service.PartyRelationTransferService;
import com.coder.springbootinit.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 组织关系转移表 Service 实现类
 *
 */
@Service
public class PartyRelationTransferServiceImpl extends ServiceImpl<PartyRelationTransferMapper, PartyRelationTransfer> implements PartyRelationTransferService {

    @Resource
    private UserService userService;

    @Resource
    private PartyOrganizationService partyOrganizationService;

    @Override
    public boolean createPartyRelationTransfer(PartyRelationTransfer partyRelationTransfer) {
        // 验证用户ID、原党组织ID和目标党组织ID是否为空
        long userId = partyRelationTransfer.getUserId();
        long fromPartyId = partyRelationTransfer.getFromPartyId();
        long toPartyId = partyRelationTransfer.getToPartyId();
        this.validPartyRelationTransfer(userId,fromPartyId,toPartyId);
        // 设置默认值
        partyRelationTransfer.setTransferTime(new Date());
        partyRelationTransfer.setApproveStatus(ApproveStatusEnum.PENDING.getCode());
        // 保存组织关系转移记录
        boolean result = this.save(partyRelationTransfer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建组织关系转移记录失败");
        }
        return result;
    }

    @Override
    public boolean updatePartyRelationTransfer(PartyRelationTransfer partyRelationTransfer) {
        // 验证组织关系转移ID是否为空
        if (partyRelationTransfer.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        this.validPartyRelationTransfer(partyRelationTransfer.getUserId(),partyRelationTransfer.getFromPartyId(),partyRelationTransfer.getToPartyId());
        // 更新组织关系转移记录
        boolean result = this.updateById(partyRelationTransfer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新组织关系转移记录失败");
        }
        return result;
    }

    @Override
    public boolean deletePartyRelationTransfer(Long id) {
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
    public boolean approvePartyRelationTransfer(long id, int approveStatus, long approveUserId, String approveComment) {
        // 验证组织关系转移ID和审批状态是否为空
        if (id < 0 || !ApproveStatusEnum.getCodes().contains(approveStatus) || approveUserId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取组织关系转移记录
        PartyRelationTransfer partyRelationTransfer = this.getById(id);
        if (partyRelationTransfer == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织关系转移记录不存在");
        }
        // 更新审批状态
        // 不检查是否已审批，允许修改审批结果
        partyRelationTransfer.setApproveStatus(approveStatus);
        partyRelationTransfer.setApproveUserId(approveUserId);
        partyRelationTransfer.setApproveTime(new Date());
        partyRelationTransfer.setApproveComment(approveComment);
        // 保存审批结果
        boolean result = this.updateById(partyRelationTransfer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "审批组织关系转移记录失败");
        }
        // 如果审批通过，更新用户的党组织ID
        if (ApproveStatusEnum.APPROVED.equals(ApproveStatusEnum.getByCode(approveStatus))) {
            User user = userService.getById(partyRelationTransfer.getUserId());
            if (user != null) {
                User updateUser = new User();
                updateUser.setId(user.getId());
                updateUser.setOrgId(partyRelationTransfer.getToPartyId());
                boolean updated = userService.updateById(updateUser);
                if (!updated) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新用户党组织ID失败");
                }
            }
        }
        return true;
    }

    @Override
    public List<PartyRelationTransfer> getPartyRelationTransfersByMemberId(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        return this.lambdaQuery().eq(PartyRelationTransfer::getUserId, userId).list();
    }

    @Override
    public List<PartyRelationTransfer> getPartyRelationTransfersByPartyId(Long partyId) {
        if (partyId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "党组织ID不能为空");
        }
        // 返回该党组织相关的所有转移记录
        return this.lambdaQuery()
                .eq(PartyRelationTransfer::getFromPartyId, partyId)
                .or()
                .eq(PartyRelationTransfer::getToPartyId, partyId)
                .list();
    }

    @Override
    public void validPartyRelationTransfer(long userId, long fromPartyId, long toPartyId) {
        if (userId <= 0 || fromPartyId < 0 || toPartyId < 0 || fromPartyId == toPartyId) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 验证用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        QueryWrapper<PartyOrganization> queryWrapper = new QueryWrapper<>();
        if(fromPartyId > 0){
            // 验证原党组织是否存在
            queryWrapper.eq("id", fromPartyId);
            long count = partyOrganizationService.count(queryWrapper);
            if (count == 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "原党组织不存在");
            }
        }
        if(toPartyId > 0){
            // 验证目标党组织是否存在
            queryWrapper.eq("id", toPartyId);
            long count = partyOrganizationService.count(queryWrapper);
            if (count == 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "目标党组织不存在");
            }
        }
    }

    @Override
    public PartyRelationTransferVO fillPartyRelationTransferVO(PartyRelationTransfer partyRelationTransfer) {
        return null;
    }
}