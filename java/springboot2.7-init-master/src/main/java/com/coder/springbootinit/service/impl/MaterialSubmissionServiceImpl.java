package com.coder.springbootinit.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.MaterialSubmissionMapper;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionAuditRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionFinalAuditRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionQueryRequest;
import com.coder.springbootinit.model.dto.material.submission.MaterialSubmissionSubmitRequest;
import com.coder.springbootinit.model.entity.MaterialAuditLog;
import com.coder.springbootinit.model.entity.MaterialSubmission;
import com.coder.springbootinit.model.vo.MaterialSubmissionVO;
import com.coder.springbootinit.model.vo.MaterialTodoCountVO;
import com.coder.springbootinit.service.MaterialAuditLogService;
import com.coder.springbootinit.service.MaterialSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 材料提交服务实现类
 *
 */
@Service
@Slf4j
public class MaterialSubmissionServiceImpl extends ServiceImpl<MaterialSubmissionMapper, MaterialSubmission> implements MaterialSubmissionService {

    @Resource
    private MaterialSubmissionMapper materialSubmissionMapper;

    @Resource
    private MaterialAuditLogService materialAuditLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaterialSubmission submitMaterial(MaterialSubmissionSubmitRequest request, Long userId, String userName) {
        // 参数校验
        if (StrUtil.isBlank(request.getStage()) || StrUtil.isBlank(request.getMaterialName())
                || StrUtil.isBlank(request.getFileUrl())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "发展阶段、材料名称和文件不能为空");
        }

        MaterialSubmission submission = new MaterialSubmission();
        BeanUtils.copyProperties(request, submission);
        submission.setUserId(userId);
        submission.setUserName(userName);
        submission.setSubmitStatus("submitted");
        submission.setAuditStatus("pending");
        submission.setUploadTime(new Date());

        boolean result = this.save(submission);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交材料失败");
        }
        log.info("提交材料成功，submissionId: {}, userId: {}", submission.getId(), userId);
        return submission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditMaterial(MaterialSubmissionAuditRequest request, Long auditorId, String auditorName) {
        if (request.getId() == null || StrUtil.isBlank(request.getAuditResult())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交 ID 和审核结果不能为空");
        }

        MaterialSubmission submission = this.getById(request.getId());
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        if (!"pending".equals(submission.getAuditStatus()) && !"auditing".equals(submission.getAuditStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前状态不可审核");
        }

        // 更新提交记录
        submission.setAuditor(auditorName);
        submission.setAuditorId(auditorId);
        submission.setAuditTime(new Date());
        submission.setAuditOpinion(request.getAuditOpinion());

        if ("approved".equals(request.getAuditResult())) {
            submission.setAuditStatus("approved");
            submission.setSubmitStatus("approved");
        } else if ("rejected".equals(request.getAuditResult())) {
            submission.setAuditStatus("rejected");
            submission.setSubmitStatus("rejected");
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "审核结果无效");
        }

        boolean result = this.updateById(submission);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "审核材料失败");
        }

        // 记录审核日志
        MaterialAuditLog auditLog = new MaterialAuditLog();
        auditLog.setSubmissionId(submission.getId());
        auditLog.setAuditUserId(auditorId);
        auditLog.setAuditUserName(auditorName);
        auditLog.setAuditType("audit");
        auditLog.setAuditResult(request.getAuditResult());
        auditLog.setAuditOpinion(request.getAuditOpinion());
        auditLog.setAuditTime(new Date());
        materialAuditLogService.save(auditLog);

        log.info("审核材料成功，submissionId: {}, auditorId: {}, result: {}", submission.getId(), auditorId, request.getAuditResult());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finalAuditMaterial(MaterialSubmissionFinalAuditRequest request, Long auditorId, String auditorName) {
        if (request.getId() == null || StrUtil.isBlank(request.getAuditResult())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交 ID 和终审结果不能为空");
        }

        MaterialSubmission submission = this.getById(request.getId());
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        if (!"approved".equals(submission.getAuditStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有初审通过的材料才能进行终审");
        }

        // 更新提交记录
        submission.setFinalAuditor(auditorName);
        submission.setFinalAuditorId(auditorId);
        submission.setFinalAuditTime(new Date());
        submission.setFinalAuditOpinion(request.getAuditOpinion());

        if ("approved".equals(request.getAuditResult())) {
            submission.setSubmitStatus("final_approved");
        } else if ("rejected".equals(request.getAuditResult())) {
            submission.setSubmitStatus("rejected");
            submission.setAuditStatus("rejected");
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "终审结果无效");
        }

        boolean result = this.updateById(submission);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "终审材料失败");
        }

        // 记录审核日志
        MaterialAuditLog auditLog = new MaterialAuditLog();
        auditLog.setSubmissionId(submission.getId());
        auditLog.setAuditUserId(auditorId);
        auditLog.setAuditUserName(auditorName);
        auditLog.setAuditType("final_audit");
        auditLog.setAuditResult(request.getAuditResult());
        auditLog.setAuditOpinion(request.getAuditOpinion());
        auditLog.setAuditTime(new Date());
        materialAuditLogService.save(auditLog);

        log.info("终审材料成功，submissionId: {}, auditorId: {}, result: {}", submission.getId(), auditorId, request.getAuditResult());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectMaterial(Long id, String auditOpinion, Long auditorId, String auditorName) {
        MaterialSubmissionAuditRequest request = new MaterialSubmissionAuditRequest();
        request.setId(id);
        request.setAuditResult("rejected");
        request.setAuditOpinion(auditOpinion);
        return auditMaterial(request, auditorId, auditorName);
    }

    @Override
    public MaterialSubmissionVO getMaterialSubmissionVO(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交 ID 不能为空");
        }

        MaterialSubmission submission = this.getById(id);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        return toVO(submission);
    }

    @Override
    public List<MaterialSubmissionVO> listMaterialSubmissionVO(MaterialSubmissionQueryRequest request) {
        QueryWrapper<MaterialSubmission> queryWrapper = getQueryWrapper(request, null, null);

        int current = request.getCurrent() != null ? request.getCurrent() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<MaterialSubmission> page =
                this.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, pageSize), queryWrapper);

        return page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialTodoCountVO getMaterialTodoCount(Long userId, String userRole, String orgLevel) {
        MaterialTodoCountVO vo = new MaterialTodoCountVO();

        // 待提交数量：用户自己未提交的
        Long toSubmitCount = this.count(new QueryWrapper<MaterialSubmission>()
                .eq("user_id", userId)
                .eq("submit_status", "unsubmitted"));
        vo.setToSubmitCount(toSubmitCount.intValue());

        // 待审核数量：根据角色判断
        if ("trainer".equals(userRole)) {
            // 培养联系人：审核自己对接的学员
            vo.setToAuditCount(0); // TODO: 需要根据 trainer_relation 表关联查询
        } else if (UserConstant.ORG_ADMIN_ROLE.equals(userRole) || UserConstant.SUPER_ADMIN_ROLE.equals(userRole)) {
            // 管理员：所有待审核的
            Long toAuditCount = this.count(new QueryWrapper<MaterialSubmission>()
                    .eq("audit_status", "pending"));
            vo.setToAuditCount(toAuditCount.intValue());
        } else {
            vo.setToAuditCount(0);
        }

        // 待终审数量：管理员可见
        if (UserConstant.ORG_ADMIN_ROLE.equals(userRole) || UserConstant.SUPER_ADMIN_ROLE.equals(userRole)) {
            Long toFinalAuditCount = this.count(new QueryWrapper<MaterialSubmission>()
                    .eq("audit_status", "approved")
                    .eq("submit_status", "approved"));
            vo.setToFinalAuditCount(toFinalAuditCount.intValue());
        } else {
            vo.setToFinalAuditCount(0);
        }

        return vo;
    }

    @Override
    public QueryWrapper<MaterialSubmission> getQueryWrapper(MaterialSubmissionQueryRequest request, Long userId, String userRole) {
        QueryWrapper<MaterialSubmission> queryWrapper = new QueryWrapper<>();

        if (userId != null && StrUtil.isBlank(userRole)) {
            // 普通用户只能查看自己的
            queryWrapper.eq("user_id", userId);
        }

        if (StrUtil.isNotBlank(request.getUserName())) {
            queryWrapper.like("user_name", request.getUserName());
        }
        if (StrUtil.isNotBlank(request.getOrgLevel())) {
            queryWrapper.eq("org_level", request.getOrgLevel());
        }
        if (StrUtil.isNotBlank(request.getStage())) {
            queryWrapper.eq("stage", request.getStage());
        }
        if (StrUtil.isNotBlank(request.getMaterialName())) {
            queryWrapper.like("material_name", request.getMaterialName());
        }
        if (StrUtil.isNotBlank(request.getSubmitStatus())) {
            queryWrapper.eq("submit_status", request.getSubmitStatus());
        }
        if (StrUtil.isNotBlank(request.getAuditStatus())) {
            queryWrapper.eq("audit_status", request.getAuditStatus());
        }

        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    /**
     * 转换为 VO
     */
    private MaterialSubmissionVO toVO(MaterialSubmission submission) {
        MaterialSubmissionVO vo = new MaterialSubmissionVO();
        BeanUtils.copyProperties(submission, vo);

        // 提交状态名称转换
        switch (submission.getSubmitStatus()) {
            case "unsubmitted":
                vo.setSubmitStatusName("未提交");
                break;
            case "submitted":
                vo.setSubmitStatusName("已提交");
                break;
            case "approved":
                vo.setSubmitStatusName("审核通过");
                break;
            case "final_approved":
                vo.setSubmitStatusName("终审通过");
                break;
            case "rejected":
                vo.setSubmitStatusName("已退回");
                break;
            case "archived":
                vo.setSubmitStatusName("已归档");
                break;
            default:
                vo.setSubmitStatusName(submission.getSubmitStatus());
        }

        // 审核状态名称转换
        switch (submission.getAuditStatus()) {
            case "pending":
                vo.setAuditStatusName("待审核");
                break;
            case "auditing":
                vo.setAuditStatusName("审核中");
                break;
            case "approved":
                vo.setAuditStatusName("通过");
                break;
            case "rejected":
                vo.setAuditStatusName("退回");
                break;
            default:
                vo.setAuditStatusName(submission.getAuditStatus());
        }

        // 文件大小格式化
        if (submission.getFileSize() != null) {
            vo.setFileSizeFormatted(formatFileSize(submission.getFileSize()));
        }

        return vo;
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long size) {
        if (size == null) {
            return "";
        }
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}