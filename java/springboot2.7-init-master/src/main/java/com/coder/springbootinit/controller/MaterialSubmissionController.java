package com.coder.springbootinit.controller;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionAuditRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionFinalAuditRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionQueryRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionSubmitRequest;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.MaterialSubmissionVO;
import com.coder.springbootinit.model.vo.MaterialTodoCountVO;
import com.coder.springbootinit.service.MaterialSubmissionService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 材料提交控制器
 *
 */
@RestController
@RequestMapping("/api/material/submission")
@Api(tags = "材料提交审核")
@Slf4j
public class MaterialSubmissionController {

    @Resource
    private MaterialSubmissionService materialSubmissionService;

    @Resource
    private UserService userService;

    /**
     * 获取材料提交列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取材料提交列表")
    public BaseResponse<List<MaterialSubmissionVO>> listMaterialSubmission(MaterialSubmissionQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<MaterialSubmissionVO> result = materialSubmissionService.listMaterialSubmissionVO(request);
        return ResultUtils.success(result);
    }

    /**
     * 提交材料
     */
    @PostMapping("/submit")
    @ApiOperation(value = "提交材料审核")
    public BaseResponse<Long> submitMaterial(@RequestBody MaterialSubmissionSubmitRequest request, HttpServletRequest httpServletRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpServletRequest);
        var submission = materialSubmissionService.submitMaterial(request, loginUser.getId(), loginUser.getUserName());
        return ResultUtils.success(submission.getId());
    }

    /**
     * 审核材料（初审）
     */
    @PutMapping("/audit")
    @ApiOperation(value = "审核材料（初审）")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> auditMaterial(@RequestBody MaterialSubmissionAuditRequest request, HttpServletRequest httpServletRequest) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpServletRequest);
        boolean result = materialSubmissionService.auditMaterial(request, loginUser.getId(), loginUser.getUserName());
        return ResultUtils.success(result);
    }

    /**
     * 退回材料
     */
    @PutMapping("/reject")
    @ApiOperation(value = "退回材料")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> rejectMaterial(@RequestParam Long id, @RequestParam String auditOpinion, HttpServletRequest httpServletRequest) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpServletRequest);
        boolean result = materialSubmissionService.rejectMaterial(id, auditOpinion, loginUser.getId(), loginUser.getUserName());
        return ResultUtils.success(result);
    }

    /**
     * 终审材料
     */
    @PutMapping("/finalAudit")
    @ApiOperation(value = "终审材料")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Boolean> finalAuditMaterial(@RequestBody MaterialSubmissionFinalAuditRequest request, HttpServletRequest httpServletRequest) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpServletRequest);
        boolean result = materialSubmissionService.finalAuditMaterial(request, loginUser.getId(), loginUser.getUserName());
        return ResultUtils.success(result);
    }

    /**
     * 查看材料详情
     */
    @GetMapping("/view")
    @ApiOperation(value = "查看材料详情")
    public BaseResponse<MaterialSubmissionVO> viewMaterial(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MaterialSubmissionVO result = materialSubmissionService.getMaterialSubmissionVO(id);
        return ResultUtils.success(result);
    }

    /**
     * 下载材料文件
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载材料文件")
    public BaseResponse<String> downloadMaterial(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MaterialSubmissionVO submission = materialSubmissionService.getMaterialSubmissionVO(id);
        return ResultUtils.success(submission.getFileUrl());
    }

    /**
     * 获取待提交和待审核数量
     */
    @GetMapping("/todoCount")
    @ApiOperation(value = "获取待提交和待审核数量")
    public BaseResponse<MaterialTodoCountVO> getMaterialTodoCount(HttpServletRequest httpServletRequest) {
        User loginUser = userService.getLoginUser(httpServletRequest);
        MaterialTodoCountVO result = materialSubmissionService.getMaterialTodoCount(loginUser.getId(), loginUser.getUserRole(), null);
        return ResultUtils.success(result);
    }
}
