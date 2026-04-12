package com.coder.springbootinit.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.MaterialTemplateMapper;
import com.coder.springbootinit.model.dto.material.template.MaterialTemplateAddRequest;
import com.coder.springbootinit.model.dto.material.template.MaterialTemplateQueryRequest;
import com.coder.springbootinit.model.dto.material.template.MaterialTemplateUpdateRequest;
import com.coder.springbootinit.model.entity.MaterialTemplate;
import com.coder.springbootinit.model.vo.MaterialTemplateVO;
import com.coder.springbootinit.service.MaterialTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 材料模板服务实现类
 *
 */
@Service
@Slf4j
public class MaterialTemplateServiceImpl extends ServiceImpl<MaterialTemplateMapper, MaterialTemplate> implements MaterialTemplateService {

    @Resource
    private MaterialTemplateMapper materialTemplateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaterialTemplate addMaterialTemplate(MaterialTemplateAddRequest request, Long uploadUserId, String uploadUserName) {
        // 参数校验
        if (StrUtil.isBlank(request.getName()) || StrUtil.isBlank(request.getStage())
                || StrUtil.isBlank(request.getType()) || StrUtil.isBlank(request.getFileUrl())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板名称、发展阶段、材料类型和文件不能为空");
        }

        MaterialTemplate template = new MaterialTemplate();
        BeanUtils.copyProperties(request, template);
        template.setUploadUserId(uploadUserId);
        template.setUploadUserName(uploadUserName);
        if (StrUtil.isBlank(template.getStatus())) {
            template.setStatus("enable");
        }

        boolean result = this.save(template);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加模板失败");
        }
        log.info("添加材料模板成功，templateId: {}, userId: {}", template.getId(), uploadUserId);
        return template;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMaterialTemplate(MaterialTemplateUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板 ID 不能为空");
        }

        MaterialTemplate oldTemplate = this.getById(request.getId());
        if (oldTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        MaterialTemplate newTemplate = new MaterialTemplate();
        BeanUtils.copyProperties(request, newTemplate);

        boolean result = this.updateById(newTemplate);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新模板失败");
        }
        log.info("更新材料模板成功，templateId: {}", request.getId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMaterialTemplate(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板 ID 不能为空");
        }

        MaterialTemplate template = this.getById(id);
        if (template == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除模板失败");
        }
        log.info("删除材料模板成功，templateId: {}", id);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteMaterialTemplate(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板 ID 列表不能为空");
        }

        boolean result = this.removeByIds(ids);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "批量删除模板失败");
        }
        log.info("批量删除材料模板成功，ids: {}", ids);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleMaterialTemplateStatus(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板 ID 不能为空");
        }

        MaterialTemplate template = this.getById(id);
        if (template == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        String newStatus = "enable".equals(template.getStatus()) ? "disable" : "enable";
        template.setStatus(newStatus);

        boolean result = this.updateById(template);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "切换模板状态失败");
        }
        log.info("切换材料模板状态成功，templateId: {}, newStatus: {}", id, newStatus);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchToggleMaterialTemplateStatus(List<Long> ids, String targetStatus) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板 ID 列表不能为空");
        }
        if (StrUtil.isBlank(targetStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "目标状态不能为空");
        }

        List<MaterialTemplate> templates = this.listByIds(ids);
        for (MaterialTemplate template : templates) {
            template.setStatus(targetStatus);
        }

        boolean result = this.updateBatchById(templates);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "批量切换模板状态失败");
        }
        log.info("批量切换材料模板状态成功，ids: {}, targetStatus: {}", ids, targetStatus);
        return result;
    }

    @Override
    public MaterialTemplateVO getMaterialTemplateVO(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板 ID 不能为空");
        }

        MaterialTemplate template = this.getById(id);
        if (template == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }

        return toVO(template);
    }

    @Override
    public List<MaterialTemplateVO> listMaterialTemplateVO(MaterialTemplateQueryRequest request) {
        QueryWrapper<MaterialTemplate> queryWrapper = getQueryWrapper(request);

        int current = request.getCurrent() != null ? request.getCurrent() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        Page<MaterialTemplate> page = this.page(new Page<>(current, pageSize), queryWrapper);

        return page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<MaterialTemplate> getQueryWrapper(MaterialTemplateQueryRequest request) {
        QueryWrapper<MaterialTemplate> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(request.getName())) {
            queryWrapper.like("name", request.getName());
        }
        if (StrUtil.isNotBlank(request.getStage())) {
            queryWrapper.eq("stage", request.getStage());
        }
        if (StrUtil.isNotBlank(request.getType())) {
            queryWrapper.eq("type", request.getType());
        }
        if (StrUtil.isNotBlank(request.getStatus())) {
            queryWrapper.eq("status", request.getStatus());
        }

        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    /**
     * 转换为 VO
     */
    private MaterialTemplateVO toVO(MaterialTemplate template) {
        MaterialTemplateVO vo = new MaterialTemplateVO();
        BeanUtils.copyProperties(template, vo);

        // 状态名称转换
        if ("enable".equals(template.getStatus())) {
            vo.setStatusName("启用");
        } else if ("disable".equals(template.getStatus())) {
            vo.setStatusName("停用");
        }

        // 文件大小格式化
        if (template.getFileSize() != null) {
            vo.setFileSizeFormatted(formatFileSize(template.getFileSize()));
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