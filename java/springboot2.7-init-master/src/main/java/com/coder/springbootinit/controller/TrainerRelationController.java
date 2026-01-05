package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.trainerRelation.TrainerRelationAddRequest;
import com.coder.springbootinit.model.entity.TrainerRelation;
import com.coder.springbootinit.service.TrainerRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 培养人关联表 控制器
 *
 */
@RestController
@RequestMapping("/trainerRelation")
@Api(tags = "培养人关联管理")
public class TrainerRelationController {

    @Resource
    private TrainerRelationService trainerRelationService;

    /**
     * 添加培养人关联
     * @param trainerRelationAddRequest 培养人关联添加请求
     * @return 培养人关联对象
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加培养人关联")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE,UserConstant.ORG_ADMIN_ROLE,UserConstant.PARTY_MEMBER_ROLE})
    public BaseResponse<TrainerRelation> addTrainerRelation(@RequestBody TrainerRelationAddRequest trainerRelationAddRequest) {
        if(trainerRelationAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrainerRelation trainerRelation = trainerRelationService.addTrainerRelation(trainerRelationAddRequest);
        return ResultUtils.success(trainerRelation);
    }

    /**
     * 根据用户ID查询被培养情况
     * @param userId 党员ID（被培养人）
     * @return 培养人关联列表
     */
    @GetMapping("/trainer/byUserId")
    @ApiOperation(value = "根据用户ID查询被培养情况")
    public BaseResponse<TrainerRelation> getTrainerRelationByPartyMemberId(@RequestParam Long userId) {
        QueryWrapper<TrainerRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        TrainerRelation trainerRelations = trainerRelationService.getOne(queryWrapper);
        return ResultUtils.success(trainerRelations);
    }

    /**
     * 根据培养人ID查询被培养人关联
     * @param trainerId 培养人ID
     * @return 培养人关联列表
     */
    @GetMapping("/trainRelation/byTrainerId")
    @ApiOperation(value = "根据培养人ID查询被培养人关联")
    public BaseResponse<List<TrainerRelation>> getTrainerRelationByTrainerId(@RequestParam Long trainerId) {
        QueryWrapper<TrainerRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trainerId", trainerId);
        List<TrainerRelation> trainerRelations = trainerRelationService.list(queryWrapper);
        return ResultUtils.success(trainerRelations);
    }

//    /**
//     * 验证培养人是否合法
//     * @param trainerId 培养人ID
//     * @return 是否合法
//     */
//    @GetMapping("/validateTrainer")
//    @ApiOperation(value = "验证培养人是否合法")
//    public BaseResponse<Boolean> validateTrainer(@RequestParam Long trainerId) {
//        boolean isValid = trainerRelationService.validateTrainer(trainerId);
//        return ResultUtils.success(isValid);
//    }

}