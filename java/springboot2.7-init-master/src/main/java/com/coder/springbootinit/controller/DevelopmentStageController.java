package com.coder.springbootinit.controller;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.annotation.DevelopmentStageLogAnnotation;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageAddRequest;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageQueryRequest;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageUpdateRequest;
import com.coder.springbootinit.model.entity.DevelopmentStage;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.DevelopmentStageOperationEnum;
import com.coder.springbootinit.model.vo.DevelopmentStageVO;
import com.coder.springbootinit.service.DevelopmentStageService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 发展阶段表 控制器
 *
 */
@RestController
@RequestMapping("/developmentStage")
@Api(tags = "发展阶段管理")
public class DevelopmentStageController {

    @Resource
    private DevelopmentStageService developmentStageService;

    @Resource
    private UserService userService;

    /**
     * 添加发展阶段记录
     * @param addRequest 添加请求
     * @return 发展阶段记录
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加发展阶段记录")
    @DevelopmentStageLogAnnotation(operationType = DevelopmentStageOperationEnum.CREATE, description = "创建发展阶段记录")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<DevelopmentStage> addDevelopmentStage(@RequestBody DevelopmentStageAddRequest addRequest) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DevelopmentStage developmentStage = developmentStageService.addDevelopmentStage(addRequest);
        return ResultUtils.success(developmentStage);
    }

    /**
     * 更新发展阶段记录
     * @param updateRequest 更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新发展阶段记录")
    @DevelopmentStageLogAnnotation(operationType = DevelopmentStageOperationEnum.UPDATE, description = "更新发展阶段记录")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> updateDevelopmentStage(@RequestBody DevelopmentStageUpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = developmentStageService.updateDevelopmentStage(updateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 删除发展阶段记录
     * @param deleteRequest 删除请求
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除发展阶段记录")
    @DevelopmentStageLogAnnotation(operationType = DevelopmentStageOperationEnum.DELETE, description = "删除发展阶段记录")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> deleteDevelopmentStage(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = developmentStageService.deleteDevelopmentStage(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 根据ID查询发展阶段记录
     * @param id 发展阶段ID
     * @return 发展阶段记录
     */
    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询发展阶段记录")
    public BaseResponse<DevelopmentStage> getDevelopmentStageById(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DevelopmentStage developmentStage = developmentStageService.getById(id);
        return ResultUtils.success(developmentStage);
    }

    /**
     * 根据用户ID获取发展阶段列表
     * @param userId 用户ID
     * @return 发展阶段列表
     */
    @GetMapping("/list/byUserId")
    @ApiOperation(value = "根据用户ID获取发展阶段列表")
    public BaseResponse<List<DevelopmentStageVO>> getDevelopmentStagesByUserId(@RequestParam Long userId) {
        List<DevelopmentStageVO> developmentStageVOList = developmentStageService.getDevelopmentStagesByUserId(userId);
        return ResultUtils.success(developmentStageVOList);
    }

    /**
     * 根据组织ID获取发展阶段列表
     * @param orgId 组织ID
     * @return 发展阶段列表
     */
    @GetMapping("/list/byOrgId")
    @ApiOperation(value = "根据组织ID获取发展阶段列表")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<List<DevelopmentStage>> getDevelopmentStagesByOrgId(@RequestParam Long orgId) {
        List<DevelopmentStage> developmentStages = developmentStageService.getDevelopmentStagesByOrgId(orgId);
        return ResultUtils.success(developmentStages);
    }

    /**
     * 完成发展阶段（设置考察结果）
     * @param id 发展阶段ID
     * @param assessmentResult 考察结果
     * @param assessmentContent 考察内容
     * @return 是否完成成功
     */
    // @PostMapping("/complete")
    // @ApiOperation(value = "完成发展阶段")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> completeDevelopmentStage(
            @ApiParam(value = "发展阶段ID", required = true) @RequestParam Long id,
            @ApiParam(value = "考察结果：1合格/0不合格", required = true) @RequestParam Integer assessmentResult,
            @ApiParam(value = "考察内容") @RequestParam(required = false) String assessmentContent) {
        boolean result = developmentStageService.completeDevelopmentStage(id, assessmentResult, assessmentContent);
        return ResultUtils.success(result);
    }

    /**
     * 提交审核
     * @param id 发展阶段ID
     * @param assessmentContent 考察内容
     * @return 是否提交成功
     */
    @PostMapping("/submitForAudit")
    @ApiOperation(value = "提交审核")
    @DevelopmentStageLogAnnotation(operationType = DevelopmentStageOperationEnum.SUBMIT_AUDIT, description = "提交审核")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> submitForAudit(
            @ApiParam(value = "发展阶段ID", required = true) @RequestParam Long id,
            @ApiParam(value = "考察内容") @RequestParam(required = false) String assessmentContent) {
        boolean result = developmentStageService.submitForAudit(id, assessmentContent);
        return ResultUtils.success(result);
    }

    /**
     * 审核发展阶段记录
     * @param id 发展阶段ID
     * @param assessmentResult 审核结果：1合格/0不合格
     * @param auditRemark 审核意见
     * @param assessmentContent 考察内容
     * @param request HTTP请求
     * @return 是否审核成功
     */
    @PostMapping("/audit")
    @ApiOperation(value = "审核发展阶段记录")
    @DevelopmentStageLogAnnotation(operationType = DevelopmentStageOperationEnum.AUDIT, description = "审核发展阶段记录")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> auditDevelopmentStage(
            @ApiParam(value = "发展阶段ID", required = true) @RequestParam Long id,
            @ApiParam(value = "审核结果：1合格/0不合格", required = true) @RequestParam Integer assessmentResult,
            @ApiParam(value = "审核意见") @RequestParam(required = false) String auditRemark,
            @ApiParam(value = "考察内容") @RequestParam(required = false) String assessmentContent,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long auditUserId = loginUser.getId();
        boolean result = developmentStageService.auditDevelopmentStage(id, auditUserId, assessmentResult, auditRemark, assessmentContent);
        return ResultUtils.success(result);
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取发展阶段列表")
    public BaseResponse<List<DevelopmentStage>> getDevelopmentStagesList(@RequestBody DevelopmentStageQueryRequest query) {
        List<DevelopmentStage> developmentStages = developmentStageService.getDevelopmentStagesList(query);
        return ResultUtils.success(developmentStages);
    }

    @PostMapping("/updateAttachment/byId")
    public BaseResponse<Boolean> updateAttachmentById(@RequestParam String attachment , @RequestParam Long id){
        DevelopmentStage developmentStage = new DevelopmentStage();
        developmentStage.setId(id);
        developmentStage.setAttachment(attachment);
        boolean result = developmentStageService.updateById(developmentStage);
        return ResultUtils.success(result);
    }
}
