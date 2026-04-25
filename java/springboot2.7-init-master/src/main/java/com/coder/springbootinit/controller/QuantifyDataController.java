package com.coder.springbootinit.controller;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.entity.QuantifyData;
import com.coder.springbootinit.model.vo.QuantifyCoreIndicatorVO;
import com.coder.springbootinit.model.vo.QuantifyStatisticsVO;
import com.coder.springbootinit.service.QuantifyDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 量化数据控制器
 *
 */
@RestController
@RequestMapping("/quantify/data")
@Api(tags = "量化数据")
@Slf4j
public class QuantifyDataController {

    @Resource
    private QuantifyDataService quantifyDataService;

    /**
     * 获取数据列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取数据列表")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<List<QuantifyData>> listData(@RequestParam(required = false) String period) {
        List<QuantifyData> list;
        if (period != null && !period.isEmpty()) {
            list = quantifyDataService.getByPeriod(period);
        } else {
            list = quantifyDataService.list();
        }
        return ResultUtils.success(list);
    }

    /**
     * 根据目标获取数据
     */
    @GetMapping("/target")
    @ApiOperation(value = "根据目标获取数据")
    public BaseResponse<List<QuantifyData>> getByTarget(
            @RequestParam Long targetId,
            @RequestParam String targetType,
            @RequestParam String period) {
        if (targetId == null || targetType == null || period == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<QuantifyData> list = quantifyDataService.getByTargetAndPeriod(targetId, targetType, period);
        return ResultUtils.success(list);
    }

    /**
     * 生成数据（根据量化指标）
     */
    @PostMapping("/generate")
    @ApiOperation(value = "根据量化指标生成数据")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Boolean> generateData() {
        boolean result = quantifyDataService.generateAllDataByIndicators();
        return ResultUtils.success(result);
    }

    /**
     * 生成单个指标数据
     */
    @PostMapping("/generate/{indicatorId}")
    @ApiOperation(value = "根据单个量化指标生成数据")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE})
    public BaseResponse<Boolean> generateDataByIndicator(@PathVariable Long indicatorId) {
        if (indicatorId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = quantifyDataService.generateDataByIndicator(indicatorId);
        return ResultUtils.success(result);
    }

    /**
     * 获取核心指标数据（活动参与率、签到率、材料完成率）
     *
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param dimension 统计维度
     * @return 核心指标数据
     */
    @GetMapping("/core")
    @ApiOperation(value = "获取核心指标数据")
    public BaseResponse<QuantifyCoreIndicatorVO> getCoreIndicators(
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String orgLevel,
            @RequestParam(required = false) String dimension) {
        QuantifyCoreIndicatorVO result = quantifyDataService.getCoreIndicators(timeRange, orgLevel, dimension);
        return ResultUtils.success(result);
    }

    /**
     * 获取组织维度统计数据
     *
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param indicator 统计指标
     * @return 组织统计数据列表
     */
    @GetMapping("/organization")
    @ApiOperation(value = "获取组织维度统计数据")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<List<QuantifyStatisticsVO>> getOrganizationStatistics(
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String orgLevel,
            @RequestParam(required = false) String indicator) {
        List<QuantifyStatisticsVO> result = quantifyDataService.getOrganizationStatistics(timeRange, orgLevel, indicator);
        return ResultUtils.success(result);
    }

    /**
     * 获取个人维度统计数据
     *
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param indicator 统计指标
     * @return 个人统计数据列表
     */
    @GetMapping("/personal")
    @ApiOperation(value = "获取个人维度统计数据")
    public BaseResponse<List<QuantifyStatisticsVO>> getPersonalStatistics(
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String orgLevel,
            @RequestParam(required = false) String indicator) {
        List<QuantifyStatisticsVO> result = quantifyDataService.getPersonalStatistics(timeRange, orgLevel, indicator);
        return ResultUtils.success(result);
    }

    /**
     * 获取数据详情
     *
     * @param id 数据ID
     * @param dimension 统计维度
     * @return 数据详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取数据详情")
    public BaseResponse<QuantifyData> getQuantifyDataDetail(
            @RequestParam Long id,
            @RequestParam(required = false) String dimension) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuantifyData data = quantifyDataService.getById(id);
        if (data == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "数据不存在");
        }
        return ResultUtils.success(data);
    }

    /**
     * 获取用户权限信息
     *
     * @param request HTTP请求
     * @return 用户权限信息
     */
    @GetMapping("/permission")
    @ApiOperation(value = "获取用户权限")
    public BaseResponse<String> getUserPermission(HttpServletRequest request) {
        // 简单实现：返回用户角色信息，实际应用中可以返回更详细的权限配置
        return ResultUtils.success("user");
    }

    /**
     * 导出统计报表为Excel格式
     *
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param dimension 统计维度
     * @param indicator 统计指标
     * @param response HTTP响应
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出统计报表")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public void exportQuantifyReport(
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String orgLevel,
            @RequestParam(required = false) String dimension,
            @RequestParam(required = false) String indicator,
            HttpServletResponse response) {
        try {
            // 设置响应头，指定文件类型和编码
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = URLEncoder.encode("量化统计报表.xlsx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            
            // 调用服务层生成Excel文件并写入响应流
            quantifyDataService.exportToExcel(timeRange, orgLevel, dimension, indicator, response.getOutputStream());
            
        } catch (Exception e) {
            log.error("导出统计报表失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "导出失败: " + e.getMessage());
        }
    }
}
