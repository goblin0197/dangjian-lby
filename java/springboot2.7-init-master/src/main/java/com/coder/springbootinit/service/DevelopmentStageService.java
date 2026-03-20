package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageAddRequest;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageUpdateRequest;
import com.coder.springbootinit.model.entity.DevelopmentStage;
import com.coder.springbootinit.model.vo.DevelopmentStageVO;

import java.util.List;

/**
 * 发展阶段表 Service 接口
 *
 */
public interface DevelopmentStageService extends IService<DevelopmentStage> {

    /**
     * 添加发展阶段记录
     * @param addRequest 添加请求
     * @return 发展阶段记录
     */
    DevelopmentStage addDevelopmentStage(DevelopmentStageAddRequest addRequest);

    /**
     * 更新发展阶段记录
     * @param updateRequest 更新请求
     * @return 是否更新成功
     */
    boolean updateDevelopmentStage(DevelopmentStageUpdateRequest updateRequest);

    /**
     * 删除发展阶段记录
     * @param id 发展阶段ID
     * @return 是否删除成功
     */
    boolean deleteDevelopmentStage(Long id);

    /**
     * 根据用户ID获取发展阶段列表
     * @param userId 用户ID
     * @return 发展阶段列表
     */
    List<DevelopmentStageVO> getDevelopmentStagesByUserId(Long userId);

    /**
     * 根据组织ID获取发展阶段列表
     * @param orgId 组织ID
     * @return 发展阶段列表
     */
    List<DevelopmentStage> getDevelopmentStagesByOrgId(Long orgId);

    /**
     * 完成发展阶段（设置考察结果）
     * @param id 发展阶段ID
     * @param assessmentResult 考察结果
     * @param assessmentContent 考察内容
     * @return 是否完成成功
     */
    boolean completeDevelopmentStage(Long id, Integer assessmentResult, String assessmentContent);

    /**
     * 提交审核（设置考察结果为未审核状态）
     * @param id 发展阶段ID
     * @param assessmentContent 考察内容
     * @return 是否提交成功
     */
    boolean submitForAudit(Long id, String assessmentContent);

    /**
     * 审核发展阶段记录
     * @param id 发展阶段ID
     * @param auditUserId 审核人员ID
     * @param assessmentResult 审核结果：1合格/0不合格
     * @param auditRemark 审核意见
     * @param assessmentContent 考察内容
     * @return 是否审核成功
     */
    boolean auditDevelopmentStage(Long id, Long auditUserId, Integer assessmentResult, String auditRemark, String assessmentContent);
}
