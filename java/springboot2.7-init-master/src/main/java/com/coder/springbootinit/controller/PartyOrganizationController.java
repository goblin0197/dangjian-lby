package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.PartyOrganization.PartyOrganizationAddRequest;
import com.coder.springbootinit.model.dto.PartyOrganization.PartyOrganizationBindRequest;
import com.coder.springbootinit.model.dto.PartyOrganization.PartyOrganizationQueryRequest;
import com.coder.springbootinit.model.dto.PartyOrganization.PartyOrganizationUpdateRequest;
import com.coder.springbootinit.model.entity.PartyOrganization;
import com.coder.springbootinit.model.vo.PartyOrganizationVO;
import com.coder.springbootinit.service.PartyOrganizationService;
import com.coder.springbootinit.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
 * 党组织表 控制器
 *
 */
@RestController
@RequestMapping("/partyOrganization")
@Api(tags = "党组织管理")
public class PartyOrganizationController {

    @Resource
    private PartyOrganizationService partyOrganizationService;
    
    @Resource
    private UserService userService;

    /**
     * 创建党组织
     * @param partyOrganizationAddRequest 党组织信息
     * @return 党组织对象
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建党组织")
    public BaseResponse<Boolean> addPartyOrganization(@RequestBody PartyOrganizationAddRequest partyOrganizationAddRequest) {
        PartyOrganization partyOrganization = new PartyOrganization();
        BeanUtils.copyProperties(partyOrganizationAddRequest, partyOrganization);
        boolean result = partyOrganizationService.createPartyOrganization(partyOrganization);
        return ResultUtils.success(result);
    }

    /**
     * 更新党组织信息
     * @param partyOrganizationUpdateRequest 党组织信息
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新党组织信息")
    public BaseResponse<Boolean> updatePartyOrganization(@RequestBody PartyOrganizationUpdateRequest partyOrganizationUpdateRequest) {
        PartyOrganization partyOrganization = new PartyOrganization();
        BeanUtils.copyProperties(partyOrganizationUpdateRequest, partyOrganization);
        boolean result = partyOrganizationService.updatePartyOrganization(partyOrganization);
        return ResultUtils.success(result);
    }

    /**
     * 删除党组织
     * @param deleteRequest 党组织ID
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除党组织")
    public BaseResponse<Boolean> deletePartyOrganization(@RequestParam DeleteRequest deleteRequest) {
        boolean result = partyOrganizationService.deletePartyOrganization(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 根据ID查询党组织
     * @param id 党组织ID
     * @return 党组织对象
     */
    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询党组织")
    public BaseResponse<PartyOrganizationVO> getPartyOrganizationById(@RequestParam Long id) {
        PartyOrganization partyOrganization = partyOrganizationService.getById(id);
        if (partyOrganization == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "党组织不存在");
        }
        return ResultUtils.success(partyOrganizationService.fillPartyOrganizationLeader(partyOrganization));
    }


    /**
     * 查询所有党组织
     * @return 党组织列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有党组织")
    public BaseResponse<List<PartyOrganizationVO>> listPartyOrganizations() {
        List<PartyOrganization> partyOrganizations = partyOrganizationService.list();
        List<PartyOrganizationVO> partyOrganizationVOList = new ArrayList<>();
        for (PartyOrganization partyOrganization : partyOrganizations) {
            partyOrganizationVOList.add(partyOrganizationService.fillPartyOrganizationLeader(partyOrganization));
        }
        return ResultUtils.success(partyOrganizationVOList);
    }

    /**
     * 根据条件分页查询党组织
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation(value = "根据条件分页查询党组织")
    public BaseResponse<Page<PartyOrganizationVO>> queryPartyOrganizations(@RequestBody PartyOrganizationQueryRequest partyOrganizationQueryRequest) {
        if (partyOrganizationQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询参数不能为空");
        }
        int current = partyOrganizationQueryRequest.getCurrent();
        int pageSize = partyOrganizationQueryRequest.getPageSize();
        Page<PartyOrganization> partyOrganizationPage = partyOrganizationService.page(
                new Page<>(current, pageSize),
                partyOrganizationService.getQueryWrapper(partyOrganizationQueryRequest));
        List<PartyOrganizationVO> partyOrganizationVOList = new ArrayList<>();
        for (PartyOrganization partyOrganization : partyOrganizationPage.getRecords()) {
            partyOrganizationVOList.add(partyOrganizationService.fillPartyOrganizationLeader(partyOrganization));
        }
        Page<PartyOrganizationVO> partyOrganizationVOPage = new Page<>(current, pageSize, partyOrganizationPage.getTotal());
        partyOrganizationVOPage.setRecords(partyOrganizationVOList);
        return ResultUtils.success(partyOrganizationVOPage);
    }

    /**
     * 党组织绑定负责人
     * @param partyOrganizationBindRequest 党组织绑定负责人请求
     * @return 是否绑定成功
     */
    @PostMapping("/bind")
    @ApiOperation(value = "党组织绑定负责人")
    public BaseResponse<Boolean> bindPartyOrganizationLeader(@RequestBody PartyOrganizationBindRequest partyOrganizationBindRequest) {
        if (partyOrganizationBindRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "绑定参数不能为空");
        }
        Long orgId = partyOrganizationBindRequest.getOrgId();
        Long leaderId = partyOrganizationBindRequest.getLeaderId();
        boolean result = partyOrganizationService.bindPartyOrganizationLeader(orgId, leaderId);
        return ResultUtils.success(result);
    }
}