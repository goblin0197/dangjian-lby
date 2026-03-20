package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.DevelopmentStage;
import com.coder.springbootinit.model.entity.DevelopmentStageLog;

import java.util.List;

/**
 * 发展阶段变更日志表 Service 接口
 *
 */
public interface DevelopmentStageLogService extends IService<DevelopmentStageLog> {

    /**
     * 记录变更日志
     * @param stageId 发展阶段ID
     * @param userId 用户ID
     * @param operationType 操作类型
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param beforeData 变更前数据
     * @param afterData 变更后数据
     * @param remark 备注
     */
    void logOperation(Long stageId, Long userId, String operationType, Long operatorId, String operatorName,
                      String beforeData, String afterData, String remark);

    /**
     * 根据发展阶段ID查询变更日志列表
     * @param stageId 发展阶段ID
     * @return 变更日志列表
     */
    List<DevelopmentStageLog> getLogsByStageId(Long stageId);

    /**
     * 根据用户ID查询变更日志列表
     * @param userId 用户ID
     * @return 变更日志列表
     */
    List<DevelopmentStageLog> getLogsByUserId(Long userId);
}
