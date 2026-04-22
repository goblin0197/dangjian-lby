package com.coder.springbootinit.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.OrgRelationTransfer;

/**
 * 组织关系转移表 Mapper 接口
 *
 */
public interface OrgRelationTransferMapper extends BaseMapper<OrgRelationTransfer> {
    @Select("select count(*) as count from org_relation_transfer where userId = #{userId} and approveStatus = 1")
    Long getPendingCountByUserId(Long userId);

}