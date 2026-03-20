package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberAddRequest;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberQueryRequest;
import com.coder.springbootinit.model.dto.orgMember.OrgMemberUpdateRoleRequest;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.UserVO;

/**
 * 组织成员服务接口
 *
 */
public interface OrgMemberService extends IService<User> {

    /**
     * 获取组织成员查询条件
     *
     * @param orgMemberQueryRequest 查询请求
     * @return 查询条件包装器
     */
    QueryWrapper<User> getQueryWrapper(OrgMemberQueryRequest orgMemberQueryRequest);

    /**
     * 分页获取组织成员列表
     *
     * @param orgMemberQueryRequest 查询请求
     * @return 分页结果
     */
    Page<UserVO> listOrgMemberByPage(OrgMemberQueryRequest orgMemberQueryRequest);

    /**
     * 添加组织成员
     *
     * @param orgMemberAddRequest 添加请求
     * @return 新用户ID
     */
    boolean addOrgMember(OrgMemberAddRequest orgMemberAddRequest);

    /**
     * 移除组织成员
     *
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否成功
     */
    boolean removeOrgMember(Long userId, Long orgId);

    /**
     * 编辑组织成员角色
     *
     * @param orgMemberUpdateRoleRequest 编辑请求
     * @return 是否成功
     */
    boolean updateOrgMemberRole(OrgMemberUpdateRoleRequest orgMemberUpdateRoleRequest);
}
