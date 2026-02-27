package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.organization.OrganizationAddRequest;
import com.coder.springbootinit.model.dto.organization.OrganizationBindRequest;
import com.coder.springbootinit.model.dto.organization.OrganizationQueryRequest;
import com.coder.springbootinit.model.dto.organization.OrganizationUpdateRequest;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.vo.OrganizationGradedVO;
import com.coder.springbootinit.model.vo.OrganizationVO;
import com.coder.springbootinit.service.OrganizationService;
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
@RequestMapping("/organization")
@Api(tags = "党组织管理")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;
    
    @Resource
    private UserService userService;

    /**
     * 创建党组织
     * @param organizationAddRequest 党组织信息
     * @return 党组织对象
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建党组织")
    public BaseResponse<Boolean> addOrganization(@RequestBody OrganizationAddRequest organizationAddRequest) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationAddRequest, organization);
        boolean result = organizationService.createOrganization(organization);
        return ResultUtils.success(result);
    }

    /**
     * 更新党组织信息
     * @param organizationUpdateRequest 党组织信息
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新党组织信息")
    public BaseResponse<Boolean> updateOrganization(@RequestBody OrganizationUpdateRequest organizationUpdateRequest) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationUpdateRequest, organization);
        boolean result = organizationService.updateOrganization(organization);
        return ResultUtils.success(result);
    }

    /**
     * 删除党组织
     * @param deleteRequest 党组织ID
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除党组织")
    public BaseResponse<Boolean> deleteOrganization(@RequestParam DeleteRequest deleteRequest) {
        boolean result = organizationService.deleteOrganization(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 根据ID查询党组织
     * @param id 党组织ID
     * @return 党组织对象
     */
    @GetMapping("/get")
    @ApiOperation(value = "根据ID查询党组织")
    public BaseResponse<OrganizationVO> getOrganizationById(@RequestParam Long id) {
        Organization organization = organizationService.getById(id);
        if (organization == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "党组织不存在");
        }
        return ResultUtils.success(organizationService.fillOrganizationLeader(organization));
    }


    /**
     * 查询所有党组织
     * @return 党组织列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有党组织")
    public BaseResponse<List<OrganizationVO>> listOrganizations() {
        List<Organization> organizations = organizationService.list();
        List<OrganizationVO> organizationVOList = new ArrayList<>();
        for (Organization organization : organizations) {
            organizationVOList.add(organizationService.fillOrganizationLeader(organization));
        }
        return ResultUtils.success(organizationVOList);
    }

    /**
     * 根据条件分页查询党组织
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation(value = "根据条件分页查询党组织")
    public BaseResponse<Page<OrganizationVO>> queryOrganizations(@RequestBody OrganizationQueryRequest organizationQueryRequest) {
        if (organizationQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询参数不能为空");
        }
        int current = organizationQueryRequest.getCurrent();
        int pageSize = organizationQueryRequest.getPageSize();
        Page<Organization> organizationPage = organizationService.page(
                new Page<>(current, pageSize),
                organizationService.getQueryWrapper(organizationQueryRequest));
        List<OrganizationVO> organizationVOList = new ArrayList<>();
        for (Organization organization : organizationPage.getRecords()) {
            organizationVOList.add(organizationService.fillOrganizationLeader(organization));
        }
        Page<OrganizationVO> organizationVOPage = new Page<>(current, pageSize, organizationPage.getTotal());
        organizationVOPage.setRecords(organizationVOList);
        return ResultUtils.success(organizationVOPage);
    }

    /**
     * 党组织绑定负责人
     * @param organizationBindRequest 党组织绑定负责人请求
     * @return 是否绑定成功
     */
    @PostMapping("/bind")
    @ApiOperation(value = "党组织绑定负责人")
    public BaseResponse<Boolean> bindOrganizationLeader(@RequestBody OrganizationBindRequest organizationBindRequest) {
        if (organizationBindRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "绑定参数不能为空");
        }
        Long orgId = organizationBindRequest.getOrgId();
        Long leaderId = organizationBindRequest.getLeaderId();
        boolean result = organizationService.bindOrganizationLeader(orgId, leaderId);
        return ResultUtils.success(result);
    }
    
    /**
     * 获取党组织及其子党组织
     * @param orgId 党组织ID
     * @return 党组织及其子党组织列表
     */
    @GetMapping("/get/graded")
    @ApiOperation(value = "获取党组织及其子党组织")
    public BaseResponse<OrganizationGradedVO> getOrganizationGradedById(@RequestParam Long orgId) {
        OrganizationGradedVO organizationGradedVO = organizationService.getSubGradedOrgs(orgId);
        return ResultUtils.success(organizationGradedVO);
    }
}