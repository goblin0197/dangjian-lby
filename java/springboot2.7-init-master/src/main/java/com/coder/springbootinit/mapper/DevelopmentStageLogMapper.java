package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.DevelopmentStageLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发展阶段变更日志表 Mapper 接口
 *
 */
public interface DevelopmentStageLogMapper extends BaseMapper<DevelopmentStageLog> {

    /**
     * 根据发展阶段ID查询变更日志列表
     * @param stageId 发展阶段ID
     * @return 变更日志列表
     */
    List<DevelopmentStageLog> getLogsByStageId(@Param("stageId") Long stageId);

    /**
     * 根据用户ID查询变更日志列表
     * @param userId 用户ID
     * @return 变更日志列表
     */
    List<DevelopmentStageLog> getLogsByUserId(@Param("userId") Long userId);
}
