package com.coder.springbootinit.controller;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.material.archive.MaterialArchiveBatchExportRequest;
import com.coder.springbootinit.model.dto.material.archive.MaterialArchiveQueryRequest;
import com.coder.springbootinit.model.dto.material.archive.MaterialArchiveRequest;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.MaterialArchiveStatVO;
import com.coder.springbootinit.model.vo.MaterialArchiveVO;
import com.coder.springbootinit.service.MaterialArchiveService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 材料归档控制器
 *
 */
@RestController
@RequestMapping("/api/material/archive")
@Api(tags = "材料归档查询")
@Slf4j
public class MaterialArchiveController {

    @Resource
    private MaterialArchiveService materialArchiveService;

    @Resource
    private UserService userService;

    /**
     * 获取归档材料列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取归档材料列表")
    public BaseResponse<List<MaterialArchiveVO>> listMaterialArchive(MaterialArchiveQueryRequest request, HttpServletRequest httpServletRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpServletRequest);
        List<MaterialArchiveVO> result = materialArchiveService.listMaterialArchiveVO(request, loginUser.getId(), loginUser.getUserRole(), null);
        return ResultUtils.success(result);
    }

    /**
     * 查看归档详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "查看归档材料详情")
    public BaseResponse<MaterialArchiveVO> viewMaterialArchiveDetail(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MaterialArchiveVO result = materialArchiveService.getMaterialArchiveVO(id);
        return ResultUtils.success(result);
    }

    /**
     * 归档材料
     */
    @PostMapping("/archive")
    @ApiOperation(value = "将终审通过的材料归档")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Long> archiveMaterial(@RequestParam Long submissionId, @RequestParam(required = false) String archiveRemark, HttpServletRequest httpServletRequest) {
        if (submissionId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MaterialArchiveRequest request = new MaterialArchiveRequest();
        request.setSubmissionId(submissionId);
        request.setArchiveRemark(archiveRemark);
        User loginUser = userService.getLoginUser(httpServletRequest);
        var archive = materialArchiveService.archiveMaterial(request, loginUser.getId(), loginUser.getUserName());
        return ResultUtils.success(archive.getId());
    }

    /**
     * 下载归档材料文件
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载归档材料文件")
    public BaseResponse<String> downloadMaterialArchive(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MaterialArchiveVO archive = materialArchiveService.getMaterialArchiveVO(id);
        return ResultUtils.success(archive.getFileUrl());
    }

    /**
     * 导出单个归档材料
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出单个归档材料")
    public void exportMaterialArchive(@RequestParam Long id, javax.servlet.http.HttpServletResponse response) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        materialArchiveService.exportSingleArchive(id, response);
    }

    /**
     * 批量导出归档材料
     */
    @PostMapping("/batchExport")
    @ApiOperation(value = "批量导出归档材料为压缩包")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<String> batchExportMaterialArchive(@RequestBody MaterialArchiveBatchExportRequest request) {
        if (request == null || request.getIds() == null || request.getIds().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String result = materialArchiveService.batchExportMaterialArchive(request.getIds());
        return ResultUtils.success(result);
    }

    /**
     * 获取归档材料统计数据
     */
    @GetMapping("/stat")
    @ApiOperation(value = "获取归档材料统计数据")
    public BaseResponse<MaterialArchiveStatVO> getMaterialArchiveStat(HttpServletRequest httpServletRequest) {
        User loginUser = userService.getLoginUser(httpServletRequest);
        MaterialArchiveStatVO result = materialArchiveService.getMaterialArchiveStat(loginUser.getId(), loginUser.getUserRole(), null);
        return ResultUtils.success(result);
    }
}
