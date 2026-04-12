package com.coder.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.constant.CommonConstant;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.UserMapper;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberAddRequest;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberQueryRequest;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberUpdateRoleRequest;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.UserRoleEnum;
import com.coder.springbootinit.model.vo.UserVO;
import com.coder.springbootinit.service.OrgMemberService;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.UserService;
import com.coder.springbootinit.utils.EncryptUtils;
import com.coder.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织成员服务实现类
 *
 */
@Service
@Slf4j
public class OrgMemberServiceImpl extends ServiceImpl<UserMapper, User> implements OrgMemberService {

    @Resource
    private UserService userService;

    @Resource
    private OrganizationService organizationService;

    /**
     * 获取组织成员查询条件
     *
     * @param orgMemberQueryRequest 查询请求
     * @return 查询条件包装器
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(OrgMemberQueryRequest orgMemberQueryRequest) {
        if (orgMemberQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long orgId = orgMemberQueryRequest.getOrgId();
        String userAccount = orgMemberQueryRequest.getUserAccount();
        String userName = orgMemberQueryRequest.getUserName();
        String userRole = orgMemberQueryRequest.getUserRole();
        String userType = orgMemberQueryRequest.getUserType();
        String politicalStatus = orgMemberQueryRequest.getPoliticalStatus();
        Integer status = orgMemberQueryRequest.getStatus();
        String sortField = orgMemberQueryRequest.getSortField();
        String sortOrder = orgMemberQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(orgId != null, "orgId", orgId);
        queryWrapper.eq(StringUtils.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.eq(StringUtils.isNotBlank(userType), "userType", userType);
        queryWrapper.eq(StringUtils.isNotBlank(politicalStatus), "politicalStatus", politicalStatus);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 分页获取组织成员列表
     *
     * @param orgMemberQueryRequest 查询请求
     * @return 分页结果
     */
    @Override
    public Page<UserVO> listOrgMemberByPage(OrgMemberQueryRequest orgMemberQueryRequest) {
        if (orgMemberQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long orgId = orgMemberQueryRequest.getOrgId();
        if (orgId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织ID不能为空");
        }
        
        long current = orgMemberQueryRequest.getCurrent();
        long size = orgMemberQueryRequest.getPageSize();
        Page<User> userPage = this.page(new Page<>(current, size), this.getQueryWrapper(orgMemberQueryRequest));
        
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVOList = this.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return userVOPage;
    }

    /**
     * 添加组织成员
     *
     * @param orgMemberAddRequest 添加请求
     * @return 新用户ID
     */
    @Override
    public boolean addOrgMember(OrgMemberAddRequest orgMemberAddRequest) {
        if (orgMemberAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long orgId = orgMemberAddRequest.getOrgId();
        Long userId = orgMemberAddRequest.getUserId();

        if (orgId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织ID不能为空");
        }
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }

        Organization organization = organizationService.getById(orgId);
        if (organization == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "组织不存在");
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        
        if (user.getOrgId() == orgId) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已是该组织成员");
        }

        user = new User();
        user.setId(userId);
        user.setOrgId(orgId);
        boolean result = userService.updateById(user);
        
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加组织成员失败");
        }
        return result;
    }

    /**
     * 移除组织成员
     *
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否成功
     */
    @Override
    public boolean removeOrgMember(Long userId, Long orgId) {
        if (userId == null || orgId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID和组织ID不能为空");
        }
        
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        
        if (!orgId.equals(user.getOrgId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户不属于此组织");
        }

        if (UserConstant.SUPER_ADMIN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不能移除超级管理员");
        }

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setOrgId(null);
        updateUser.setUserRole(UserConstant.ACTIVIST_DEVELOPMENT_ROLE);
        
        return this.updateById(updateUser);
    }

    /**
     * 编辑组织成员角色
     *
     * @param orgMemberUpdateRoleRequest 编辑请求
     * @return 是否成功
     */
    @Override
    public boolean updateOrgMemberRole(OrgMemberUpdateRoleRequest orgMemberUpdateRoleRequest) {
        if (orgMemberUpdateRoleRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long userId = orgMemberUpdateRoleRequest.getUserId();
        Long orgId = orgMemberUpdateRoleRequest.getOrgId();
        String userRole = orgMemberUpdateRoleRequest.getUserRole();

        if (userId == null || orgId == null || StringUtils.isBlank(userRole)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID、组织ID和角色不能为空");
        }

        UserRoleEnum roleEnum = UserRoleEnum.getEnumByValue(userRole);
        if (roleEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的角色类型");
        }

        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        if (!orgId.equals(user.getOrgId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户不属于此组织");
        }

        if (UserConstant.SUPER_ADMIN_ROLE.equals(user.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不能修改超级管理员角色");
        }

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setUserRole(userRole);
        
        return this.updateById(updateUser);
    }

    /**
     * 获取用户VO列表
     *
     * @param userList 用户列表
     * @return 用户VO列表
     */
    private List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            if (user.getOrgId() != null) {
                Organization organization = organizationService.getById(user.getOrgId());
                if (organization != null) {
                    userVO.setOrgName(organization.getOrgName());
                }
            }
            return userVO;
        }).collect(Collectors.toList());
    }
}
