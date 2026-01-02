package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.PartyRelationTransfer;

import java.util.List;

/**
 * 组织关系转移表 Service 接口
 *
 */
public interface PartyRelationTransferService extends IService<PartyRelationTransfer> {
    
    /**
     * 创建组织关系转移申请(管理员)
     * @param partyRelationTransfer 组织关系转移信息
     * @return 组织关系转移对象
     */
    boolean createPartyRelationTransfer(PartyRelationTransfer partyRelationTransfer);
    
    /**
     * 更新组织关系转移信息
     * @param partyRelationTransfer 组织关系转移信息
     * @return 是否更新成功
     */
    boolean updatePartyRelationTransfer(PartyRelationTransfer partyRelationTransfer);
    
    /**
     * 删除组织关系转移记录
     * @param id 组织关系转移ID
     * @return 是否删除成功
     */
    boolean deletePartyRelationTransfer(Long id);
    
    /**
     * 审批组织关系转移申请
     * @param id 组织关系转移ID
     * @param approveStatus 审批状态
     * @param approveUserId 审批人ID
     * @param approveComment 审批意见
     * @return 是否审批成功
     */
    boolean approvePartyRelationTransfer(long id, int approveStatus, long approveUserId, String approveUserName ,String approveComment);
    
    /**
     * 根据党员ID查询组织关系转移记录
     * @param memberId 党员ID
     * @return 组织关系转移列表
     */
    List<PartyRelationTransfer> getPartyRelationTransfersByMemberId(Long memberId);
    
    /**
     * 根据党组织ID查询组织关系转移记录
     * @param partyId 党组织ID
     * @return 组织关系转移列表
     */
    List<PartyRelationTransfer> getPartyRelationTransfersByPartyId(Long partyId);

    /**
     * 校验组织关系转移信息
     * @param userId 用户ID
     * @param fromPartyId 原党组织ID
     * @param toPartyId 目标党组织ID
     */
    List<String> validPartyRelationTransfer(long userId,long fromPartyId,long toPartyId);
}