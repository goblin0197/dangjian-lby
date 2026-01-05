package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.TrainerRelationMapper;
import com.coder.springbootinit.model.dto.trainerRelation.TrainerRelationAddRequest;
import com.coder.springbootinit.model.entity.TrainerRelation;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.TrainerRelationStatusEnum;
import com.coder.springbootinit.service.TrainerRelationService;
import com.coder.springbootinit.service.UserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.Date;

/**
 * 培养人关联表 Service 实现类
 *
 */
@Service
public class TrainerRelationServiceImpl extends ServiceImpl<TrainerRelationMapper, TrainerRelation> implements TrainerRelationService {

    @Resource
    private UserService userService;

    @Override
    public TrainerRelation addTrainerRelation(TrainerRelationAddRequest trainerRelationAddRequest) {
        long userId = trainerRelationAddRequest.getUserId();
        long trainerId = trainerRelationAddRequest.getTrainerId();
        Date startDate = trainerRelationAddRequest.getStartDate();

        // 验证培养人是否合法
        if (!validateTrainer(trainerId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "培养人必须是教师党员");
        }

        // 验证被培养人是否合法
        if (!validatePartyMember(userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "积极分子/发展对象只能是学生");
        }
        User trainer = userService.getById(trainerId);
        User user = userService.getById(userId);
        // 创建培养人关联
        TrainerRelation trainerRelation = new TrainerRelation();
        trainerRelation.setUserId(userId);
        trainerRelation.setUserName(user.getUserName());
        trainerRelation.setTrainerId(trainerId);
        trainerRelation.setTrainerName(trainer.getUserName());
        trainerRelation.setStartDate(startDate != null ? startDate : new Date());
        trainerRelation.setStatus(TrainerRelationStatusEnum.TRAINING.getValue());

        // 保存到数据库
        this.save(trainerRelation);
        return trainerRelation;
    }

    @Override
    public boolean validateTrainer(Long trainerId) {
        // 获取培养人用户信息
        User trainer = userService.getById(trainerId);
        if (trainer == null) {
            return false;
        }

        // 验证培养人
        // 用户类型为教师
        // 用户角色不为积极分子/发展对象角色
        // 政治面貌为党员
        return UserConstant.USER_TYPE_TEACHER.equals(trainer.getUserType()) && 
               !UserConstant.ACTIVIST_DEVELOPMENT_ROLE.equals(trainer.getUserRole()) &&
               UserConstant.POLITICAL_STATUS_PARTY_MEMBER.equals(trainer.getPoliticalStatus());
    }

    @Override
    public boolean validatePartyMember(Long userId) {
        // 获取被培养人用户信息
        User user = userService.getById(userId);
        if (user == null) {
            return false;
        }

        // 验证积极分子/发展对象
        // 用户类型为学生
        // 用户角色为积极分子/发展对象角色
        // 政治面貌为团员
        return UserConstant.USER_TYPE_STUDENT.equals(user.getUserType()) && 
        UserConstant.ACTIVIST_DEVELOPMENT_ROLE.equals(user.getUserRole()) &&
        UserConstant.POLITICAL_STATUS_TEAM_UNION_MEMBER.equals(user.getPoliticalStatus());
    
    }
}