package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.orgRelationTransfer.OrgRelationTransferAddRequest;
import com.coder.springbootinit.model.dto.orgRelationTransfer.OrgRelationTransferApproveRequest;
import com.coder.springbootinit.model.dto.orgRelationTransfer.OrgRelationTransferUpdateRequest;
import com.coder.springbootinit.model.entity.OrgRelationTransfer;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.ApproveStatusEnum;
import com.coder.springbootinit.service.OrgRelationTransferService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 组织关系转移表 控制器
 *
 */
@RestController
@RequestMapping("/orgRelationTransfer")
@Api(tags = "组织关系转移管理")
public class OrgRelationTransferController {

    @Resource
    private OrgRelationTransferService orgRelationTransferService;

    @Resource
    private UserService userService;

    /**
     * 创建组织关系转移申请
     * @param orgRelationTransferAddRequest 组织关系转移添加请求
     * @return 组织关系转移对象
     */
    @PostMapping("/add")    
    @ApiOperation(value = "创建组织关系转移申请")
    public BaseResponse<Boolean> addOrgRelationTransfer(@RequestBody OrgRelationTransferAddRequest orgRelationTransferAddRequest) {
        if(orgRelationTransferAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 转换为实体类
        OrgRelationTransfer orgRelationTransfer = new OrgRelationTransfer();
        org.springframework.beans.BeanUtils.copyProperties(orgRelationTransferAddRequest, orgRelationTransfer);
        // 设置默认审批状态为待审批
        orgRelationTransfer.setApproveStatus(ApproveStatusEnum.PENDING.getCode());
        boolean result = orgRelationTransferService.createOrgRelationTransfer(orgRelationTransfer);
        return ResultUtils.success(result);
    }

    /**
     * 审批 组织关系转移申请
     * @param orgRelationTransferApproveRequest 组织关系转移审批请求
     * @return 是否审批成功
     */
    @PostMapping("/approve")
    @ApiOperation(value = "审批组织关系转移申请")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> approveOrgRelationTransfer(
            @RequestBody OrgRelationTransferApproveRequest orgRelationTransferApproveRequest,
            HttpServletRequest request
    ) {
        // 获取当前登录用户ID
        User loginUser = userService.getLoginUser(request);
        Long approveUserId = loginUser.getId();
        String approveUserName = loginUser.getUserName();
        boolean result = orgRelationTransferService.approveOrgRelationTransfer(
                orgRelationTransferApproveRequest.getId(),
                orgRelationTransferApproveRequest.getApproveStatus(),
                approveUserId,
                approveUserName,
                orgRelationTransferApproveRequest.getApproveComment());
        return ResultUtils.success(result);
    }

    /**
     * 更新组织关系转移信息
     * @param orgRelationTransferUpdateRequest 组织关系转移更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新组织关系转移信息")
    public BaseResponse<Boolean> updateOrgRelationTransfer(@RequestBody OrgRelationTransferUpdateRequest orgRelationTransferUpdateRequest) {
        if(orgRelationTransferUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 转换为实体类
        OrgRelationTransfer orgRelationTransfer = new OrgRelationTransfer();
        BeanUtils.copyProperties(orgRelationTransferUpdateRequest, orgRelationTransfer);
        boolean result = orgRelationTransferService.updateOrgRelationTransfer(orgRelationTransfer);
        return ResultUtils.success(result);
    }

    /**
     * 删除组织关系转移记录
     * @param deleteRequest 组织关系转移ID
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除组织关系转移记录")
    public BaseResponse<Boolean> deleteOrgRelationTransfer(@RequestBody DeleteRequest deleteRequest) {
        boolean result = orgRelationTransferService.deleteOrgRelationTransfer(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 根据ID查询组织关系转移记录
     * @param id 组织关系转移ID
     * @return 组织关系转移对象
     */
    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询组织关系转移记录详情")
    public BaseResponse<OrgRelationTransfer> getOrgRelationTransferById(@RequestParam Long id) {
        OrgRelationTransfer orgRelationTransfer = orgRelationTransferService.getById(id);
        return ResultUtils.success(orgRelationTransfer);
    }

    /**
     * 查询所有组织关系转移记录
     * @return 组织关系转移列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有组织关系转移记录")
    public BaseResponse<List<OrgRelationTransfer>> listOrgRelationTransfers() {
        List<OrgRelationTransfer> orgRelationTransfers = orgRelationTransferService.list();
        return ResultUtils.success(orgRelationTransfers);
    }

    /**
     * 分页查询组织关系转移记录
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 组织关系转移分页列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询组织关系转移记录")
    public BaseResponse<Page<OrgRelationTransfer>> pageOrgRelationTransfers(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        Page<OrgRelationTransfer> orgRelationTransferPage = orgRelationTransferService.page(
                new Page<>(pageNum, pageSize));
        return ResultUtils.success(orgRelationTransferPage);
    }



    /**
     * 根据党员ID查询组织关系转移记录
     * @param userId 党员ID
     * @return 组织关系转移列表
     */
    @GetMapping("/byUserId")
    @ApiOperation(value = "根据党员ID查询组织关系转移记录")
    public BaseResponse<List<OrgRelationTransfer>> getOrgRelationTransfersByUserId(@RequestParam Long userId) {
        List<OrgRelationTransfer> orgRelationTransfers = orgRelationTransferService.getOrgRelationTransfersByUserId(userId);
        return ResultUtils.success(orgRelationTransfers);
    }

    /**
     * 根据党组织ID查询组织关系转移记录
     * @param orgId 党组织ID
     * @return 组织关系转移列表
     */
    @GetMapping("/byOrgId")
    @ApiOperation(value = "根据党组织ID查询组织关系转移记录")
    public BaseResponse<List<OrgRelationTransfer>> getOrgRelationTransfersByOrgId(
            @RequestParam Long orgId) {
        List<OrgRelationTransfer> orgRelationTransfers = orgRelationTransferService.getOrgRelationTransfersByOrgId(orgId);
        return ResultUtils.success(orgRelationTransfers);
    }
}