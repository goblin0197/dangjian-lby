package com.coder.springbootinit.controller;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.entity.QuantifyIndicator;
import com.coder.springbootinit.service.QuantifyIndicatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 量化指标控制器
 *
 */
@RestController
@RequestMapping("/quantify/indicator")
@Api(tags = "量化指标")
@Slf4j
public class QuantifyIndicatorController {

    @Resource
    private QuantifyIndicatorService quantifyIndicatorService;

    /**
     * 获取指标列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取指标列表")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<List<QuantifyIndicator>> listIndicator(@RequestParam(required = false) String status) {
        List<QuantifyIndicator> list;
        if (status != null && !status.isEmpty()) {
            list = quantifyIndicatorService.listByStatus(status);
        } else {
            list = quantifyIndicatorService.list();
        }
        return ResultUtils.success(list);
    }

    /**
     * 添加指标
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加指标")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Long> addIndicator(@RequestBody QuantifyIndicator indicator) {
        if (indicator == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = quantifyIndicatorService.save(indicator);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(indicator.getId());
    }

    /**
     * 更新指标
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新指标")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Boolean> updateIndicator(@RequestBody QuantifyIndicator indicator) {
        if (indicator == null || indicator.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = quantifyIndicatorService.updateById(indicator);
        return ResultUtils.success(result);
    }

    /**
     * 删除指标
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除指标")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Boolean> deleteIndicator(@PathVariable Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = quantifyIndicatorService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 切换指标状态
     */
    @PutMapping("/toggle/{id}")
    @ApiOperation(value = "切换指标状态")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Boolean> toggleStatus(@PathVariable Long id, @RequestParam String status) {
        if (id == null || status == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = quantifyIndicatorService.toggleStatus(id, status);
        return ResultUtils.success(result);
    }

    /**
     * 批量删除量化指标
     *
     * @param ids 指标ID列表
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    @ApiOperation(value = "批量删除量化指标")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Boolean> batchDeleteIndicators(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = quantifyIndicatorService.removeByIds(ids);
        return ResultUtils.success(result);
    }

    /**
     * 导入量化指标模板
     *
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入量化指标模板")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<String> importQuantifyIndicatorTemplate(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }
        // 校验文件类型（Excel）
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".xls") 
                && !originalFilename.toLowerCase().endsWith(".xlsx"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持Excel格式的文件");
        }
        String result = quantifyIndicatorService.importFromExcel(file);
        return ResultUtils.success(result);
    }
}
