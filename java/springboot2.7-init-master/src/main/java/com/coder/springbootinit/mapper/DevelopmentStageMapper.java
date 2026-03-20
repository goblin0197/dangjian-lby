package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.DevelopmentStage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发展阶段表 Mapper 接口
 *
 */
public interface DevelopmentStageMapper extends BaseMapper<DevelopmentStage> {

    /**
     * 根据用户ID查询发展阶段列表
     * @param userId 用户ID
     * @return 发展阶段列表
     */
    List<DevelopmentStage> getDevelopmentStagesByUserId(@Param("userId") Long userId);

    /**
     * 根据组织ID查询发展阶段列表
     * @param orgId 组织ID
     * @return 发展阶段列表
     */
    List<DevelopmentStage> getDevelopmentStagesByOrgId(@Param("orgId") Long orgId);
}
