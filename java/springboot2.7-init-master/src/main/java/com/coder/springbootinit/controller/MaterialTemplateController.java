package com.coder.springbootinit.controller;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.material.MaterialTemplateAddRequest;
import com.coder.springbootinit.model.dto.material.MaterialTemplateBatchDeleteRequest;
import com.coder.springbootinit.model.dto.material.MaterialTemplateBatchToggleRequest;
import com.coder.springbootinit.model.dto.material.MaterialTemplateQueryRequest;
import com.coder.springbootinit.model.dto.material.MaterialTemplateUpdateRequest;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.MaterialTemplateVO;
import com.coder.springbootinit.service.MaterialTemplateService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 材料模板控制器
 *
 */
@RestController
@RequestMapping("/api/material/template")
@Api(tags = "材料模板管理")
@Slf4j
public class MaterialTemplateController {

    @Resource
    private MaterialTemplateService materialTemplateService;

    @Resource
    private UserService userService;

    /**
     * 获取材料模板列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取材料模板列表")
    public BaseResponse<List<MaterialTemplateVO>> listMaterialTemplate(MaterialTemplateQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<MaterialTemplateVO> result = materialTemplateService.listMaterialTemplateVO(request);
        return ResultUtils.success(result);
    }

    /**
     * 添加材料模板
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加材料模板")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Long> addMaterialTemplate(@RequestBody MaterialTemplateAddRequest request, HttpServletRequest httpServletRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpServletRequest);
        MaterialTemplateVO template = materialTemplateService.addMaterialTemplate(request, loginUser.getId(), loginUser.getUserName());
        return ResultUtils.success(template.getId());
    }

    /**
     * 更新材料模板
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新材料模板")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> updateMaterialTemplate(@RequestBody MaterialTemplateUpdateRequest request) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialTemplateService.updateMaterialTemplate(request);
        return ResultUtils.success(result);
    }

    /**
     * 删除材料模板
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除材料模板")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> deleteMaterialTemplate(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialTemplateService.deleteMaterialTemplate(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 批量删除材料模板
     */
    @DeleteMapping("/batchDelete")
    @ApiOperation(value = "批量删除材料模板")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> batchDeleteMaterialTemplate(@RequestBody MaterialTemplateBatchDeleteRequest request) {
        if (request == null || request.getIds() == null || request.getIds().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialTemplateService.batchDeleteMaterialTemplate(request.getIds());
        return ResultUtils.success(result);
    }

    /**
     * 切换模板状态
     */
    @PutMapping("/toggleStatus")
    @ApiOperation(value = "切换模板状态")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> toggleMaterialTemplateStatus(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialTemplateService.toggleMaterialTemplateStatus(id);
        return ResultUtils.success(result);
    }

    /**
     * 批量切换模板状态
     */
    @PutMapping("/batchToggleStatus")
    @ApiOperation(value = "批量切换模板状态")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> batchToggleMaterialTemplateStatus(@RequestBody MaterialTemplateBatchToggleRequest request) {
        if (request == null || request.getIds() == null || request.getIds().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialTemplateService.batchToggleMaterialTemplateStatus(request.getIds(), request.getStatus());
        return ResultUtils.success(result);
    }

    /**
     * 预览模板文件
     */
    @GetMapping("/preview")
    @ApiOperation(value = "预览模板文件")
    public BaseResponse<String> previewMaterialTemplate(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MaterialTemplateVO template = materialTemplateService.getMaterialTemplateVO(id);
        return ResultUtils.success(template.getFileUrl());
    }

    /**
     * 下载模板文件
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载模板文件")
    public BaseResponse<String> downloadMaterialTemplate(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MaterialTemplateVO template = materialTemplateService.getMaterialTemplateVO(id);
        return ResultUtils.success(template.getFileUrl());
    }
}
