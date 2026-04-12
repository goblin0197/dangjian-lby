package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionAuditRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionFinalAuditRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionQueryRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionSubmitRequest;
import com.coder.springbootinit.model.entity.MaterialSubmission;
import com.coder.springbootinit.model.vo.MaterialSubmissionVO;
import com.coder.springbootinit.model.vo.MaterialTodoCountVO;

import java.util.List;

/**
 * 材料提交服务接口
 *
 */
public interface MaterialSubmissionService extends IService<MaterialSubmission> {

    /**
     * 提交材料
     * @param materialSubmissionSubmitRequest 提交请求
     * @param userId 用户 ID
     * @param userName 用户姓名
     * @return 提交记录
     */
    MaterialSubmission submitMaterial(MaterialSubmissionSubmitRequest materialSubmissionSubmitRequest, Long userId, String userName);

    /**
     * 审核材料（初审）
     * @param auditRequest 审核请求
     * @param auditorId 审核人 ID
     * @param auditorName 审核人姓名
     * @return 是否成功
     */
    boolean auditMaterial(MaterialSubmissionAuditRequest auditRequest, Long auditorId, String auditorName);

    /**
     * 终审材料
     * @param finalAuditRequest 终审请求
     * @param auditorId 审核人 ID
     * @param auditorName 审核人姓名
     * @return 是否成功
     */
    boolean finalAuditMaterial(MaterialSubmissionFinalAuditRequest finalAuditRequest, Long auditorId, String auditorName);

    /**
     * 退回材料
     * @param id 提交 ID
     * @param auditOpinion 退回意见
     * @param auditorId 审核人 ID
     * @param auditorName 审核人姓名
     * @return 是否成功
     */
    boolean rejectMaterial(Long id, String auditOpinion, Long auditorId, String auditorName);

    /**
     * 获取材料提交 VO
     * @param id 提交 ID
     * @return 提交 VO
     */
    MaterialSubmissionVO getMaterialSubmissionVO(Long id);

    /**
     * 获取材料提交列表
     * @param queryRequest 查询请求
     * @return 提交 VO 列表
     */
    List<MaterialSubmissionVO> listMaterialSubmissionVO(MaterialSubmissionQueryRequest queryRequest);

    /**
     * 获取待办数量
     * @param userId 用户 ID
     * @param userRole 用户角色
     * @param orgLevel 组织层级
     * @return 待办数量 VO
     */
    MaterialTodoCountVO getMaterialTodoCount(Long userId, String userRole, String orgLevel);

    /**
     * 获取查询条件
     * @param queryRequest 查询请求
     * @param userId 当前用户 ID
     * @param userRole 当前用户角色
     * @return 查询条件
     */
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MaterialSubmission> getQueryWrapper(MaterialSubmissionQueryRequest queryRequest, Long userId, String userRole);
}