package com.coder.springbootinit.controller;

import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.entity.DevelopmentStageLog;
import com.coder.springbootinit.service.DevelopmentStageLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发展阶段变更日志 控制器
 *
 */
@RestController
@RequestMapping("/developmentStageLog")
@Api(tags = "发展阶段变更日志")
public class DevelopmentStageLogController {

    @Resource
    private DevelopmentStageLogService developmentStageLogService;

    /**
     * 根据发展阶段ID查询变更日志列表
     * @param stageId 发展阶段ID
     * @return 变更日志列表
     */
    @GetMapping("/list/byStageId")
    @ApiOperation(value = "根据发展阶段ID查询变更日志列表")
    public BaseResponse<List<DevelopmentStageLog>> getLogsByStageId(
            @ApiParam(value = "发展阶段ID", required = true) @RequestParam Long stageId) {
        if (stageId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<DevelopmentStageLog> logs = developmentStageLogService.getLogsByStageId(stageId);
        return ResultUtils.success(logs);
    }

    /**
     * 根据用户ID查询变更日志列表
     * @param userId 用户ID
     * @return 变更日志列表
     */
    @GetMapping("/list/byUserId")
    @ApiOperation(value = "根据用户ID查询变更日志列表")
    public BaseResponse<List<DevelopmentStageLog>> getLogsByUserId(
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<DevelopmentStageLog> logs = developmentStageLogService.getLogsByUserId(userId);
        return ResultUtils.success(logs);
    }
}
