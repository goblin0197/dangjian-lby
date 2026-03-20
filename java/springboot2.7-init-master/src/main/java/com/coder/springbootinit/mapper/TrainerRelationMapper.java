package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.TrainerRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 培养人关联表 Mapper 接口
 *
 */
public interface TrainerRelationMapper extends BaseMapper<TrainerRelation> {

    /**
     * 根据组织ID查询培养关系列表
     * @param orgId 组织ID
     * @return 培养关系列表
     */
    List<TrainerRelation> getTrainerRelationsByOrgId(@Param("orgId") Long orgId);
}