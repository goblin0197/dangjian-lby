package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.model.entity.OrgQuantify;
import com.coder.springbootinit.model.entity.UserQuantify;
import com.coder.springbootinit.service.OrgQuantifyService;
import com.coder.springbootinit.service.UserQuantifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 量化统计接口
 *
 */
@RestController
@RequestMapping("/quantify")
@Api(tags = "量化统计")
@Slf4j
public class QuantifyController {

    @Resource
    private UserQuantifyService userQuantifyService;

    @Resource
    private OrgQuantifyService orgQuantifyService;

    // region 用户量化统计

    /**
     * 生成指定用户的量化统计数据
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    @PostMapping("/user/generate/{userId}")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "生成指定用户的量化统计数据（仅管理员）")
    public BaseResponse<Boolean> generateUserQuantifyData(@PathVariable Long userId) {
        boolean result = userQuantifyService.generateUserQuantifyData(userId);
        return ResultUtils.success(result);
    }

    /**
     * 生成所有用户的量化统计数据
     *
     * @return 是否成功
     */
    @PostMapping("/user/generate/all")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "生成所有用户的量化统计数据（仅管理员）")
    public BaseResponse<Boolean> generateAllUserQuantifyData() {
        boolean result = userQuantifyService.generateAllUserQuantifyData();
        return ResultUtils.success(result);
    }

    /**
     * 获取指定用户的量化统计数据
     *
     * @param userId 用户ID
     * @return 用户量化统计数据
     */
    @GetMapping("/user/{userId}")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "获取指定用户的量化统计数据（仅管理员）")
    public BaseResponse<UserQuantify> getUserQuantifyData(@PathVariable Long userId) {
        // 构建查询条件，根据userId查询
        UserQuantify userQuantify = userQuantifyService.lambdaQuery()
                .eq(UserQuantify::getUserId, userId)
                .one();
        return ResultUtils.success(userQuantify);
    }

    // endregion

    // region 组织量化统计

    /**
     * 生成指定组织的量化统计数据
     *
     * @param orgId 组织ID
     * @return 是否成功
     */
    @PostMapping("/org/generate/{orgId}")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "生成指定组织的量化统计数据（仅管理员）")
    public BaseResponse<Boolean> generateOrgQuantifyData(@PathVariable Long orgId) {
        boolean result = orgQuantifyService.generateOrgQuantifyData(orgId);
        return ResultUtils.success(result);
    }

    /**
     * 生成所有组织的量化统计数据
     *
     * @return 是否成功
     */
    @PostMapping("/org/generate/all")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "生成所有组织的量化统计数据（仅管理员）")
    public BaseResponse<Boolean> generateAllOrgQuantifyData() {
        boolean result = orgQuantifyService.generateAllOrgQuantifyData();
        return ResultUtils.success(result);
    }

    /**
     * 获取指定组织的量化统计数据
     *
     * @param orgId 组织ID
     * @return 组织量化统计数据
     */
    @GetMapping("/org/{orgId}")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "获取指定组织的量化统计数据（仅管理员）")
    public BaseResponse<OrgQuantify> getOrgQuantifyData(@PathVariable Long orgId) {
        // 构建查询条件，根据orgId查询
        OrgQuantify orgQuantify = orgQuantifyService.lambdaQuery()
                .eq(OrgQuantify::getOrgId, orgId)
                .one();
        return ResultUtils.success(orgQuantify);
    }

    // endregion

    /**
     * 生成所有量化统计数据
     *
     * @return 是否成功
     */
    @PostMapping("/generate/all")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "生成所有量化统计数据（仅管理员）")
    public BaseResponse<Boolean> generateAllQuantifyData() {
        boolean userResult = userQuantifyService.generateAllUserQuantifyData();
        boolean orgResult = orgQuantifyService.generateAllOrgQuantifyData();
        return ResultUtils.success(userResult && orgResult);
    }
}
