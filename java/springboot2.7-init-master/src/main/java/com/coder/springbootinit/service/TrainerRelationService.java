package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.trainerRelation.TrainerRelationAddRequest;
import com.coder.springbootinit.model.entity.TrainerRelation;
import com.coder.springbootinit.model.entity.User;

import java.util.List;

/**
 * 培养人关联表 Service 接口
 *
 */
public interface TrainerRelationService extends IService<TrainerRelation> {

    /**
     * 添加培养人关联
     * @param trainerRelationAddRequest 添加培养人关联请求
     * @return 培养人关联对象
     */
    TrainerRelation addTrainerRelation(TrainerRelationAddRequest trainerRelationAddRequest);

    /**
     * 验证培养人:
     * 用户类型为教师
     * 用户角色不为积极分子/发展对象角色
     * 政治面貌为党员
     * @param trainerId 培养人ID
     * @return 是否合法
     */
    boolean validateTrainer(Long trainerId);

    /**
     * 验证被培养人:
     * 用户类型为学生
     * 用户角色为积极分子/发展对象角色
     * 政治面貌为团员
     * @param orgMemberId 组织党员ID（被培养人）
     * @return 是否合法
     */
    boolean validateOrgMember(Long orgMemberId);

    /**
     * 根据组织ID获取培养关系列表
     * @param orgId 组织ID
     * @return 培养关系列表
     */
    List<TrainerRelation> getTrainerRelationsByOrgId(Long orgId);

    /**
     * 删除培养关系
     * @param id 培养关系ID
     * @return 是否删除成功
     */
    boolean deleteTrainerRelation(Long id);

    /**
     * 获取可选的培养人列表
     * 培养人条件：用户类型为教师、政治面貌为党员、用户角色不为积极分子/发展对象
     * @param orgId 组织ID（可选）
     * @param userType 用户类型（可选）
     * @return 培养人列表
     */
    List<User> getAvailableTrainers(Long orgId, String userType);
}