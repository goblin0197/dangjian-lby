package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.trainerRelation.TrainerRelationAddRequest;
import com.coder.springbootinit.model.entity.TrainerRelation;

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
}