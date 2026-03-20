package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * 培养人关联表 Service 实现类
 *
 */
@Service
public class TrainerRelationServiceImpl extends ServiceImpl<TrainerRelationMapper, TrainerRelation> implements TrainerRelationService {

    @Resource
    private UserService userService;

    @Resource
    private TrainerRelationMapper trainerRelationMapper;

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
        if (!validateOrgMember(userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "积极分子/发展对象只能是学生团员");
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
               UserConstant.POLITICAL_STATUS_ORG_MEMBER.equals(trainer.getPoliticalStatus());
    }

    @Override
    public boolean validateOrgMember(Long userId) {
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

    /**
     * 根据组织ID获取培养关系列表
     * @param orgId 组织ID
     * @return 培养关系列表
     */
    @Override
    public List<TrainerRelation> getTrainerRelationsByOrgId(Long orgId) {
        if (orgId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织ID不能为空");
        }
        return trainerRelationMapper.getTrainerRelationsByOrgId(orgId);
    }

    /**
     * 删除培养关系
     * @param id 培养关系ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteTrainerRelation(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "培养关系ID不能为空");
        }
        // 检查培养关系是否存在
        TrainerRelation trainerRelation = this.getById(id);
        if (trainerRelation == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "培养关系不存在");
        }
        return this.removeById(id);
    }

    /**
     * 获取可选的培养人列表
     * 培养人条件：用户类型为教师、政治面貌为党员、用户角色不为积极分子/发展对象
     * @param orgId 组织ID（可选）
     * @param userType 用户类型（可选）
     * @return 培养人列表
     */
    @Override
    public List<User> getAvailableTrainers(Long orgId, String userType) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 培养人必须是教师
        queryWrapper.eq("userType", UserConstant.USER_TYPE_TEACHER);
        // 政治面貌为党员（包括正式党员和预备党员）
        queryWrapper.in("politicalStatus", UserConstant.POLITICAL_STATUS_ORG_MEMBER, UserConstant.POLITICAL_STATUS_PROBATIONARY_ORG_MEMBER);
        // 用户角色不能是积极分子/发展对象
        queryWrapper.ne("userRole", UserConstant.ACTIVIST_DEVELOPMENT_ROLE);
        // 可选条件：组织ID
        if (orgId != null) {
            queryWrapper.eq("orgId", orgId);
        }
        // 可选条件：用户类型（如果前端需要筛选）
        if (StringUtils.isNotBlank(userType)) {
            queryWrapper.eq("userType", userType);
        }
        // 按创建时间降序排列
        queryWrapper.orderByDesc("createTime");
        return userService.list(queryWrapper);
    }
}