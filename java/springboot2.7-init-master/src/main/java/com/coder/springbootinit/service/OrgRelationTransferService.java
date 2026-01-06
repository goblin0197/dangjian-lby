package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.OrgRelationTransfer;

import java.util.List;

/**
 * 组织关系转移表 Service 接口
 *
 */
public interface OrgRelationTransferService extends IService<OrgRelationTransfer> {
    
    /**
     * 创建组织关系转移申请(管理员)
     * @param orgRelationTransfer 组织关系转移信息
     * @return 组织关系转移对象
     */
    boolean createOrgRelationTransfer(OrgRelationTransfer orgRelationTransfer);
    
    /**
     * 更新组织关系转移信息
     * @param orgRelationTransfer 组织关系转移信息
     * @return 是否更新成功
     */
    boolean updateOrgRelationTransfer(OrgRelationTransfer orgRelationTransfer);
    
    /**
     * 删除组织关系转移记录
     * @param id 组织关系转移ID
     * @return 是否删除成功
     */
    boolean deleteOrgRelationTransfer(Long id);
    
    /**
     * 审批组织关系转移申请
     * @param id 组织关系转移ID
     * @param approveStatus 审批状态
     * @param approveUserId 审批人ID
     * @param approveComment 审批意见
     * @return 是否审批成功
     */
    boolean approveOrgRelationTransfer(long id, int approveStatus, long approveUserId, String approveUserName ,String approveComment);
    
    /**
     * 根据党员ID查询组织关系转移记录
     * @param memberId 党员ID
     * @return 组织关系转移列表
     */
    List<OrgRelationTransfer> getOrgRelationTransfersByUserId(Long userId);
    
    /**
     * 根据党组织ID查询组织关系转移记录
     * @param orgId 党组织ID
     * @return 组织关系转移列表
     */
    List<OrgRelationTransfer> getOrgRelationTransfersByOrgId(Long orgId);

    /**
     * 校验组织关系转移信息
     * @param userId 用户ID
     * @param fromOrgId 原党组织ID
     * @param toOrgId 目标党组织ID
     */
    List<String> validOrgRelationTransfer(long userId,long fromOrgId,long toOrgId);
}