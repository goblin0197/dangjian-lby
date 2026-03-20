package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.mapper.DevelopmentStageLogMapper;
import com.coder.springbootinit.model.entity.DevelopmentStageLog;
import com.coder.springbootinit.service.DevelopmentStageLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发展阶段变更日志表 Service 实现类
 *
 */
@Service
public class DevelopmentStageLogServiceImpl extends ServiceImpl<DevelopmentStageLogMapper, DevelopmentStageLog> implements DevelopmentStageLogService {

    @Resource
    private DevelopmentStageLogMapper developmentStageLogMapper;

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
    @Override
    public void logOperation(Long stageId, Long userId, String operationType, Long operatorId, String operatorName,
                             String beforeData, String afterData, String remark) {
        DevelopmentStageLog log = new DevelopmentStageLog();
        log.setStageId(stageId);
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setBeforeData(beforeData);
        log.setAfterData(afterData);
        log.setRemark(remark);
        this.save(log);
    }

    /**
     * 根据发展阶段ID查询变更日志列表
     * @param stageId 发展阶段ID
     * @return 变更日志列表
     */
    @Override
    public List<DevelopmentStageLog> getLogsByStageId(Long stageId) {
        return developmentStageLogMapper.getLogsByStageId(stageId);
    }

    /**
     * 根据用户ID查询变更日志列表
     * @param userId 用户ID
     * @return 变更日志列表
     */
    @Override
    public List<DevelopmentStageLog> getLogsByUserId(Long userId) {
        return developmentStageLogMapper.getLogsByUserId(userId);
    }
}
