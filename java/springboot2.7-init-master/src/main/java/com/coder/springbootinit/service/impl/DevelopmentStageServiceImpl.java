package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.DevelopmentStageMapper;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageAddRequest;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageQueryRequest;
import com.coder.springbootinit.model.dto.developmentStage.DevelopmentStageUpdateRequest;
import com.coder.springbootinit.model.entity.DevelopmentStage;
import com.coder.springbootinit.model.entity.MyFile;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.AssessmentResultEnum;
import com.coder.springbootinit.model.enums.DevelopmentStageStatusEnum;
import com.coder.springbootinit.model.vo.DevelopmentStageVO;
import com.coder.springbootinit.service.DevelopmentStageService;
import com.coder.springbootinit.service.FileService;
import com.coder.springbootinit.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发展阶段表 Service 实现类
 *
 */
@Service
public class DevelopmentStageServiceImpl extends ServiceImpl<DevelopmentStageMapper, DevelopmentStage> implements DevelopmentStageService {

    @Resource
    private DevelopmentStageMapper developmentStageMapper;

    @Resource
    private UserService userService;

    @Resource
    private FileService fileService;

    /**
     * 添加发展阶段记录
     * @param addRequest 添加请求
     * @return 发展阶段记录
     */
    @Override
    public DevelopmentStage addDevelopmentStage(DevelopmentStageAddRequest addRequest) {
        if (addRequest == null || addRequest.getUserId() == null || addRequest.getTrainerId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        if (StringUtils.isBlank(addRequest.getStageName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "阶段名称不能为空");
        }
        
        // 验证用户和培养人是否存在
        User user = userService.getById(addRequest.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        User trainer = userService.getById(addRequest.getTrainerId());
        if (trainer == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "培养人不存在");
        }

        DevelopmentStage developmentStage = new DevelopmentStage();
        developmentStage.setUserId(addRequest.getUserId());
        developmentStage.setTrainerId(addRequest.getTrainerId());
        developmentStage.setStageName(addRequest.getStageName());
        developmentStage.setStageStartTime(addRequest.getStageStartTime() != null ? addRequest.getStageStartTime() : new Date());
        developmentStage.setStageStatus(DevelopmentStageStatusEnum.IN_PROGRESS.getValue());
        developmentStage.setAssessmentContent(addRequest.getAssessmentContent());

        this.save(developmentStage);
        return developmentStage;
    }

    /**
     * 更新发展阶段记录
     * @param updateRequest 更新请求
     * @return 是否更新成功
     */
    @Override
    public boolean updateDevelopmentStage(DevelopmentStageUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }

        DevelopmentStage existingStage = this.getById(updateRequest.getId());
        if (existingStage == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "发展阶段记录不存在");
        }

        DevelopmentStage developmentStage = new DevelopmentStage();
        developmentStage.setId(updateRequest.getId());
        if (updateRequest.getStageEndTime() != null) {
            developmentStage.setStageEndTime(updateRequest.getStageEndTime());
        }
        if (updateRequest.getStageStatus() != null) {
            developmentStage.setStageStatus(updateRequest.getStageStatus());
        }
        if (StringUtils.isNotBlank(updateRequest.getAssessmentContent())) {
            developmentStage.setAssessmentContent(updateRequest.getAssessmentContent());
        }
        if (updateRequest.getAssessmentResult() != null) {
            developmentStage.setAssessmentResult(updateRequest.getAssessmentResult());
        }

        return this.updateById(developmentStage);
    }

    /**
     * 删除发展阶段记录
     * @param id 发展阶段ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDevelopmentStage(Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID不能为空");
        }
        DevelopmentStage developmentStage = this.getById(id);
        if (developmentStage == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "发展阶段记录不存在");
        }
        // 已完成审核的发展记录不允许删除，除系统管理员外
        if (developmentStage.getStageStatus() == DevelopmentStageStatusEnum.COMPLETED.getValue() && !userService.isSuperAdmin(request)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "已完成审核的发展阶段不允许删除");
        }
        return this.removeById(id);
    }

    /**
     * 根据用户ID获取发展阶段列表
     * @param userId 用户ID
     * @return 发展阶段列表
     */
    @Override
    public List<DevelopmentStageVO> getDevelopmentStagesByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        List<DevelopmentStage> developmentStages = developmentStageMapper.getDevelopmentStagesByUserId(userId);
        List<DevelopmentStageVO> developmentStageVOList = new ArrayList<>();
        
        for (DevelopmentStage stage : developmentStages) {
            DevelopmentStageVO vo = new DevelopmentStageVO();
            BeanUtils.copyProperties(stage, vo);
            
            List<MyFile> files = fileService.lambdaQuery()
                    .eq(MyFile::getStageId, stage.getId())
                    .list();
            vo.setFiles(files);
            
            developmentStageVOList.add(vo);
        }
        
        return developmentStageVOList;
    }

    /**
     * 根据组织ID获取发展阶段列表
     * @param orgId 组织ID
     * @return 发展阶段列表
     */
    @Override
    public List<DevelopmentStage> getDevelopmentStagesByOrgId(Long orgId) {
        if (orgId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织ID不能为空");
        }
        return developmentStageMapper.getDevelopmentStagesByOrgId(orgId);
    }

    /**
     * 完成发展阶段（设置考察结果）
     * @param id 发展阶段ID
     * @param assessmentResult 考察结果
     * @param assessmentContent 考察内容
     * @return 是否完成成功
     */
    @Override
    public boolean completeDevelopmentStage(Long id, Integer assessmentResult, String assessmentContent) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID不能为空");
        }
        if (assessmentResult == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "考察结果不能为空");
        }

        DevelopmentStage developmentStage = this.getById(id);
        if (developmentStage == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "发展阶段记录不存在");
        }

        developmentStage.setStageEndTime(new Date());
        developmentStage.setStageStatus(DevelopmentStageStatusEnum.COMPLETED.getValue());
        developmentStage.setAssessmentResult(assessmentResult);
        if (StringUtils.isNotBlank(assessmentContent)) {
            developmentStage.setAssessmentContent(assessmentContent);
        }

        return this.updateById(developmentStage);
    }

    /**
     * 提交审核（设置考察结果为未审核状态）
     * @param id 发展阶段ID
     * @param assessmentContent 考察内容
     * @return 是否提交成功
     */
    @Override
    public boolean submitForAudit(Long id, String assessmentContent) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID不能为空");
        }

        DevelopmentStage developmentStage = this.getById(id);
        if (developmentStage == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "发展阶段记录不存在");
        }

        developmentStage.setStageEndTime(new Date());
        developmentStage.setStageStatus(DevelopmentStageStatusEnum.COMPLETED.getValue());
        developmentStage.setAssessmentResult(AssessmentResultEnum.PENDING.getValue());
        if (StringUtils.isNotBlank(assessmentContent)) {
            developmentStage.setAssessmentContent(assessmentContent);
        }

        return this.updateById(developmentStage);
    }

    /**
     * 审核发展阶段记录
     * @param id 发展阶段ID
     * @param auditUserId 审核人员ID
     * @param assessmentResult 审核结果：1合格/0不合格
     * @param auditRemark 审核意见
     * @param assessmentContent 考察内容
     * @return 是否审核成功
     */
    @Override
    public boolean auditDevelopmentStage(Long id, Long auditUserId, Integer assessmentResult, String auditRemark, String assessmentContent) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ID不能为空");
        }
        if (auditUserId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "审核人员ID不能为空");
        }
        if (assessmentResult == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "审核结果不能为空");
        }

        DevelopmentStage developmentStage = this.getById(id);
        if (developmentStage == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "发展阶段记录不存在");
        }

        // 验证是否为未审核状态
        if (!AssessmentResultEnum.PENDING.getValue().equals(developmentStage.getAssessmentResult())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该记录不是待审核状态");
        }

        developmentStage.setAuditUserId(auditUserId);
        developmentStage.setAuditTime(new Date());
        developmentStage.setAssessmentResult(assessmentResult);
        // 审核通过表示该发展阶段结束
        developmentStage.setStageEndTime(new Date());
        developmentStage.setStageStatus(DevelopmentStageStatusEnum.COMPLETED.getValue());
        if (StringUtils.isNotBlank(auditRemark)) {
            developmentStage.setAuditRemark(auditRemark);
        }
        if (StringUtils.isNotBlank(assessmentContent)) {
            developmentStage.setAssessmentContent(assessmentContent);
        }

        return this.updateById(developmentStage);
    }

    /**
     * 获取查询条件的查询包装器
     * @param queryRequest 查询请求
     * @return 查询包装器
     */
    @Override
    public QueryWrapper<DevelopmentStage> getQueryWrapper(DevelopmentStageQueryRequest queryRequest) {
        QueryWrapper<DevelopmentStage> queryWrapper = new QueryWrapper<>();
        if(queryRequest == null) {
            return queryWrapper;
        }
        queryWrapper.eq(queryRequest.getUserId() != null, "user_id", queryRequest.getUserId());
        queryWrapper.eq(queryRequest.getTrainerId() != null, "trainer_id", queryRequest.getTrainerId());
        queryWrapper.like(StringUtils.isNotBlank(queryRequest.getStageName()), "stage_name", queryRequest.getStageName());
        queryWrapper.ge(queryRequest.getStageStartTime() != null, "stage_start_time", queryRequest.getStageStartTime());
        queryWrapper.le(queryRequest.getStageEndTime() != null, "stage_end_time", queryRequest.getStageEndTime());
        queryWrapper.eq(queryRequest.getStageStatus() != null, "stage_status", queryRequest.getStageStatus());
        queryWrapper.eq(queryRequest.getAssessmentResult() != null, "assessment_result", queryRequest.getAssessmentResult());
        queryWrapper.eq(queryRequest.getAuditUserId() != null, "audit_user_id", queryRequest.getAuditUserId());
        queryWrapper.ge(queryRequest.getAuditTime() != null, "audit_time", queryRequest.getAuditTime());
        return queryWrapper;
    }

    /**
     * 获取发展阶段列表
     * @param queryRequest 查询请求
     * @return 发展阶段列表
     */
    @Override
    public List<DevelopmentStage> getDevelopmentStagesList(DevelopmentStageQueryRequest queryRequest) {
        QueryWrapper<DevelopmentStage> queryWrapper = getQueryWrapper(queryRequest);
        return this.list(queryWrapper);
    }
}
