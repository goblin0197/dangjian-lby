package com.coder.springbootinit.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.MaterialArchiveMapper;
import com.coder.springbootinit.model.dto.material.archive.MaterialArchiveQueryRequest;
import com.coder.springbootinit.model.dto.material.archive.MaterialArchiveRequest;
import com.coder.springbootinit.model.entity.MaterialArchive;
import com.coder.springbootinit.model.entity.MaterialSubmission;
import com.coder.springbootinit.model.vo.MaterialArchiveStatVO;
import com.coder.springbootinit.model.vo.MaterialArchiveVO;
import com.coder.springbootinit.service.MaterialArchiveService;
import com.coder.springbootinit.service.MaterialSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 材料归档服务实现类
 *
 */
@Service
@Slf4j
public class MaterialArchiveServiceImpl extends ServiceImpl<MaterialArchiveMapper, MaterialArchive> implements MaterialArchiveService {

    @Resource
    private MaterialArchiveMapper materialArchiveMapper;

    @Resource
    private MaterialSubmissionService materialSubmissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaterialArchive archiveMaterial(MaterialArchiveRequest request, Long archiveUserId, String archiveUserName) {
        if (request.getSubmissionId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交 ID 不能为空");
        }

        // 查询提交记录
        MaterialSubmission submission = materialSubmissionService.getById(request.getSubmissionId());
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        if (!"final_approved".equals(submission.getSubmitStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有终审通过的材料才能归档");
        }

        MaterialArchive archive = new MaterialArchive();
        archive.setSubmissionId(request.getSubmissionId());
        archive.setUserId(submission.getUserId());
        archive.setUserName(submission.getUserName());
        archive.setOrgLevel(submission.getOrgLevel());
        archive.setStage(submission.getStage());
        archive.setMaterialName(submission.getMaterialName());
        archive.setFileUrl(submission.getFileUrl());
        archive.setFileSize(submission.getFileSize());
        archive.setUploadTime(submission.getUploadTime());
        archive.setAuditor(submission.getAuditor());
        archive.setAuditTime(submission.getAuditTime());
        archive.setArchiveUser(archiveUserName);
        archive.setArchiveUserId(archiveUserId);
        archive.setArchiveTime(new Date());
        archive.setArchiveRemark(request.getArchiveRemark());

        boolean result = this.save(archive);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "归档失败");
        }

        // 更新提交记录状态
        submission.setSubmitStatus("archived");
        materialSubmissionService.updateById(submission);

        log.info("材料归档成功，archiveId: {}, submissionId: {}", archive.getId(), request.getSubmissionId());
        return archive;
    }

    @Override
    public MaterialArchiveVO getMaterialArchiveVO(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "归档 ID 不能为空");
        }

        MaterialArchive archive = this.getById(id);
        if (archive == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "归档记录不存在");
        }

        return toVO(archive);
    }

    @Override
    public List<MaterialArchiveVO> listMaterialArchiveVO(MaterialArchiveQueryRequest request, Long userId, String userRole, String orgLevel) {
        QueryWrapper<MaterialArchive> queryWrapper = getQueryWrapper(request, userId, userRole, orgLevel);

        int current = request.getCurrent() != null ? request.getCurrent() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<MaterialArchive> page =
                this.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, pageSize), queryWrapper);

        return page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialArchiveStatVO getMaterialArchiveStat(Long userId, String userRole, String orgLevel) {
        MaterialArchiveStatVO vo = new MaterialArchiveStatVO();

        QueryWrapper<MaterialArchive> baseQuery = new QueryWrapper<>();

        // 根据权限过滤数据
        if (UserConstant.SUPER_ADMIN_ROLE.equals(userRole) || UserConstant.ORG_ADMIN_ROLE.equals(userRole)) {
            // 管理员可以看到所有归档
        } else if (StrUtil.isNotBlank(orgLevel)) {
            // 普通用户只能看到自己的
            baseQuery.eq("user_id", userId);
        }

        // 归档总数
        Long totalCount = this.count(baseQuery);
        vo.setTotalCount(totalCount);

        // 本月归档数
        Date now = new Date();
        java.sql.Date startOfMonth = new java.sql.Date(now.getTime() - (now.getDate() - 1) * 24L * 3600 * 1000);
        QueryWrapper<MaterialArchive> monthQuery = new QueryWrapper<>(baseQuery);
        monthQuery.ge("archive_time", startOfMonth);
        Long monthCount = this.count(monthQuery);
        vo.setMonthCount(monthCount);

        // 月度增长率（简化计算）
        if (monthCount > 0 && totalCount > monthCount) {
            BigDecimal prevCount = BigDecimal.valueOf(totalCount - monthCount);
            BigDecimal growth = BigDecimal.valueOf(monthCount).divide(prevCount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            vo.setGrowthRate(growth);
        } else {
            vo.setGrowthRate(BigDecimal.ZERO);
        }

        // TODO: 各阶段归档统计
        vo.setStageStats(null);

        return vo;
    }

    @Override
    public QueryWrapper<MaterialArchive> getQueryWrapper(MaterialArchiveQueryRequest request, Long userId, String userRole, String orgLevel) {
        QueryWrapper<MaterialArchive> queryWrapper = new QueryWrapper<>();

        // 根据权限过滤数据
        if (UserConstant.SUPER_ADMIN_ROLE.equals(userRole) || UserConstant.ORG_ADMIN_ROLE.equals(userRole)) {
            // 管理员可以看到所有归档
        } else if (userId != null) {
            // 普通用户只能看到自己的
            queryWrapper.eq("user_id", userId);
        }

        if (StrUtil.isNotBlank(request.getUserName())) {
            queryWrapper.like("user_name", request.getUserName());
        }
        if (StrUtil.isNotBlank(request.getMaterialName())) {
            queryWrapper.like("material_name", request.getMaterialName());
        }
        if (StrUtil.isNotBlank(request.getStage())) {
            queryWrapper.eq("stage", request.getStage());
        }

        queryWrapper.orderByDesc("archive_time");
        return queryWrapper;
    }

    /**
     * 转换为 VO
     */
    private MaterialArchiveVO toVO(MaterialArchive archive) {
        MaterialArchiveVO vo = new MaterialArchiveVO();
        BeanUtils.copyProperties(archive, vo);

        // 文件大小格式化
        if (archive.getFileSize() != null) {
            vo.setFileSizeFormatted(formatFileSize(archive.getFileSize()));
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

    /**
     * 导出单个归档材料文件
     *
     * @param id 归档 ID
     * @param response HTTP 响应
     */
    @Override
    public void exportSingleArchive(Long id, HttpServletResponse response) {
        MaterialArchive archive = this.getById(id);
        if (archive == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "归档记录不存在");
        }

        try {
            // 构建文件路径
            Path staticPath = Paths.get(System.getProperty("user.dir"), "static");
            String fileUrl = archive.getFileUrl();
            
            // 确保pathAfterStatic不以斜杠开头，否则resolve会忽略当前路径
            if (fileUrl.startsWith("/") || fileUrl.startsWith("\\")) {
                fileUrl = fileUrl.substring(1);
            }
            
            // 构建绝对路径
            Path absolutePath = staticPath.resolve(fileUrl.replace("/", java.io.File.separator));
            java.io.File file = absolutePath.toFile();

            // 检查文件是否存在
            if (!file.exists()) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文件不存在: " + file.getName());
            }

            // 设置响应头
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            String fileName = URLEncoder.encode(file.getName(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLengthLong(file.length());
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            // 使用hutool读取文件字节数组并写入响应
            byte[] fileBytes = FileUtil.readBytes(file);
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(fileBytes);
                outputStream.flush();
                outputStream.close();
            }
            
            response.flushBuffer();
            log.info("导出单个归档材料成功，archiveId: {}, 文件名: {}", id, file.getName());
        } catch (Exception e) {
            log.error("导出单个归档材料失败，archiveId: {}", id, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "导出失败: " + e.getMessage());
        }
    }
}