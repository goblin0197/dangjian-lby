package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.User;

/**
 * 用户数据库操作
 *
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 移除组织成员（清空组织 ID 并重置角色）
     *
     * @param userId 用户 ID
     * @return 更新结果
     */
    int removeOrgMember(Long userId);
}




