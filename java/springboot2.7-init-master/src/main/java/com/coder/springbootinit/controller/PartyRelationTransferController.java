package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.partyRelationTransfer.PartyRelationTransferAddRequest;
import com.coder.springbootinit.model.dto.partyRelationTransfer.PartyRelationTransferApproveRequest;
import com.coder.springbootinit.model.dto.partyRelationTransfer.PartyRelationTransferUpdateRequest;
import com.coder.springbootinit.model.entity.PartyRelationTransfer;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.ApproveStatusEnum;
import com.coder.springbootinit.service.PartyRelationTransferService;
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
@RequestMapping("/partyRelationTransfer")
@Api(tags = "组织关系转移管理")
public class PartyRelationTransferController {

    @Resource
    private PartyRelationTransferService partyRelationTransferService;

    @Resource
    private UserService userService;

    /**
     * 创建组织关系转移申请
     * @param partyRelationTransferAddRequest 组织关系转移添加请求
     * @return 组织关系转移对象
     */
    @PostMapping("/add")    
    @ApiOperation(value = "创建组织关系转移申请")
    public BaseResponse<Boolean> addPartyRelationTransfer(@RequestBody PartyRelationTransferAddRequest partyRelationTransferAddRequest) {
        if(partyRelationTransferAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 转换为实体类
        PartyRelationTransfer partyRelationTransfer = new PartyRelationTransfer();
        org.springframework.beans.BeanUtils.copyProperties(partyRelationTransferAddRequest, partyRelationTransfer);
        // 设置默认审批状态为待审批
        partyRelationTransfer.setApproveStatus(ApproveStatusEnum.PENDING.getCode());
        boolean result = partyRelationTransferService.createPartyRelationTransfer(partyRelationTransfer);
        return ResultUtils.success(result);
    }

    /**
     * 审批 组织关系转移申请
     * @param partyRelationTransferApproveRequest 组织关系转移审批请求
     * @return 是否审批成功
     */
    @PostMapping("/approve")
    @ApiOperation(value = "审批组织关系转移申请")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> approvePartyRelationTransfer(
            @RequestBody PartyRelationTransferApproveRequest partyRelationTransferApproveRequest,
            HttpServletRequest request
    ) {
        // 获取当前登录用户ID
        User loginUser = userService.getLoginUser(request);
        Long approveUserId = loginUser.getId();
        String approveUserName = loginUser.getUserName();
        boolean result = partyRelationTransferService.approvePartyRelationTransfer(
                partyRelationTransferApproveRequest.getId(),
                partyRelationTransferApproveRequest.getApproveStatus(),
                approveUserId,
                approveUserName,
                partyRelationTransferApproveRequest.getApproveComment());
        return ResultUtils.success(result);
    }

    /**
     * 更新组织关系转移信息
     * @param partyRelationTransferUpdateRequest 组织关系转移更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新组织关系转移信息")
    public BaseResponse<Boolean> updatePartyRelationTransfer(@RequestBody PartyRelationTransferUpdateRequest partyRelationTransferUpdateRequest) {
        if(partyRelationTransferUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 转换为实体类
        PartyRelationTransfer partyRelationTransfer = new PartyRelationTransfer();
        BeanUtils.copyProperties(partyRelationTransferUpdateRequest, partyRelationTransfer);
        boolean result = partyRelationTransferService.updatePartyRelationTransfer(partyRelationTransfer);
        return ResultUtils.success(result);
    }

    /**
     * 删除组织关系转移记录
     * @param deleteRequest 组织关系转移ID
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除组织关系转移记录")
    public BaseResponse<Boolean> deletePartyRelationTransfer(@RequestBody DeleteRequest deleteRequest) {
        boolean result = partyRelationTransferService.deletePartyRelationTransfer(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 根据ID查询组织关系转移记录
     * @param id 组织关系转移ID
     * @return 组织关系转移对象
     */
    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询组织关系转移记录详情")
    public BaseResponse<PartyRelationTransfer> getPartyRelationTransferById(@RequestParam Long id) {
        PartyRelationTransfer partyRelationTransfer = partyRelationTransferService.getById(id);
        return ResultUtils.success(partyRelationTransfer);
    }

    /**
     * 查询所有组织关系转移记录
     * @return 组织关系转移列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有组织关系转移记录")
    public BaseResponse<List<PartyRelationTransfer>> listPartyRelationTransfers() {
        List<PartyRelationTransfer> partyRelationTransfers = partyRelationTransferService.list();
        return ResultUtils.success(partyRelationTransfers);
    }

    /**
     * 分页查询组织关系转移记录
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 组织关系转移分页列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询组织关系转移记录")
    public BaseResponse<Page<PartyRelationTransfer>> pagePartyRelationTransfers(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        Page<PartyRelationTransfer> partyRelationTransferPage = partyRelationTransferService.page(
                new Page<>(pageNum, pageSize));
        return ResultUtils.success(partyRelationTransferPage);
    }



    /**
     * 根据党员ID查询组织关系转移记录
     * @param userId 党员ID
     * @return 组织关系转移列表
     */
    @GetMapping("/byUserId")
    @ApiOperation(value = "根据党员ID查询组织关系转移记录")
    public BaseResponse<List<PartyRelationTransfer>> getPartyRelationTransfersByMemberId(@RequestParam Long userId) {
        List<PartyRelationTransfer> partyRelationTransfers = partyRelationTransferService.getPartyRelationTransfersByMemberId(userId);
        return ResultUtils.success(partyRelationTransfers);
    }

    /**
     * 根据党组织ID查询组织关系转移记录
     * @param partyId 党组织ID
     * @return 组织关系转移列表
     */
    @GetMapping("/byPartyId")
    @ApiOperation(value = "根据党组织ID查询组织关系转移记录")
    public BaseResponse<List<PartyRelationTransfer>> getPartyRelationTransfersByPartyId(
            @RequestParam Long partyId) {
        List<PartyRelationTransfer> partyRelationTransfers = partyRelationTransferService.getPartyRelationTransfersByPartyId(partyId);
        return ResultUtils.success(partyRelationTransfers);
    }
}