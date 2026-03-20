package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberAddRequest;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberQueryRequest;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberUpdateRoleRequest;
import com.coder.springbootinit.model.vo.UserVO;
import com.coder.springbootinit.service.OrgMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 组织成员管理接口
 *
 */
@RestController
@RequestMapping("/orgMember")
@Api(tags = "组织成员管理")
@Slf4j
public class OrgMemberController {

    @Resource
    private OrgMemberService orgMemberService;

    /**
     * 分页获取组织成员列表
     *
     * @param orgMemberQueryRequest 查询请求
     * @return 组织成员分页列表
     */
    @PostMapping("/list/page")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "分页获取组织成员列表")
    public BaseResponse<Page<UserVO>> listOrgMemberByPage(@RequestBody OrgMemberQueryRequest orgMemberQueryRequest) {
        if (orgMemberQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<UserVO> userVOPage = orgMemberService.listOrgMemberByPage(orgMemberQueryRequest);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 添加组织成员
     *
     * @param orgMemberAddRequest 添加请求
     * @return 新用户ID
     */
    @PostMapping("/add")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "添加组织成员")
    public BaseResponse<Boolean> addOrgMember(@RequestBody OrgMemberAddRequest orgMemberAddRequest) {
        if (orgMemberAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = orgMemberService.addOrgMember(orgMemberAddRequest);
        return ResultUtils.success(result);
    }

    /**
     * 移除组织成员
     *
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否成功
     */
    @PostMapping("/remove")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "移除组织成员")
    public BaseResponse<Boolean> removeOrgMember(@RequestParam Long userId, @RequestParam Long orgId) {
        if (userId == null || orgId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = orgMemberService.removeOrgMember(userId, orgId);
        return ResultUtils.success(result);
    }

    /**
     * 编辑组织成员角色
     *
     * @param orgMemberUpdateRoleRequest 编辑请求
     * @return 是否成功
     */
    @PostMapping("/updateRole")
    // @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    @ApiOperation(value = "编辑组织成员角色")
    public BaseResponse<Boolean> updateOrgMemberRole(@RequestBody OrgMemberUpdateRoleRequest orgMemberUpdateRoleRequest) {
        if (orgMemberUpdateRoleRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = orgMemberService.updateOrgMemberRole(orgMemberUpdateRoleRequest);
        return ResultUtils.success(result);
    }
}
