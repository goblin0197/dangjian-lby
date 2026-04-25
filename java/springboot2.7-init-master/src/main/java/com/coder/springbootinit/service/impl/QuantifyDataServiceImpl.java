package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.mapper.QuantifyDataMapper;
import com.coder.springbootinit.model.entity.Activity;
import com.coder.springbootinit.model.entity.ActivityEnroll;
import com.coder.springbootinit.model.entity.MaterialSubmission;
import com.coder.springbootinit.model.entity.MaterialTemplate;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.entity.QuantifyData;
import com.coder.springbootinit.model.entity.QuantifyIndicator;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.ActivityEnrollSignEnum;
import com.coder.springbootinit.model.enums.ActivityEnrollStatusEnum;
import com.coder.springbootinit.model.vo.QuantifyCoreIndicatorVO;
import com.coder.springbootinit.model.vo.QuantifyStatisticsVO;
import com.coder.springbootinit.service.ActivityEnrollService;
import com.coder.springbootinit.service.ActivityService;
import com.coder.springbootinit.service.MaterialSubmissionService;
import com.coder.springbootinit.service.MaterialTemplateService;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.QuantifyDataService;
import com.coder.springbootinit.service.QuantifyIndicatorService;
import com.coder.springbootinit.service.UserService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 量化数据记录服务实现
 *
 */
@Service
@Slf4j
public class QuantifyDataServiceImpl extends ServiceImpl<QuantifyDataMapper, QuantifyData> implements QuantifyDataService {

    @Resource
    private UserService userService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityEnrollService activityEnrollService;

    @Resource
    private MaterialSubmissionService materialSubmissionService;

    @Resource
    private MaterialTemplateService materialTemplateService;

    @Override
    public List<QuantifyData> getByTargetAndPeriod(Long targetId, String targetType, String period) {
        LambdaQueryWrapper<QuantifyData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuantifyData::getTargetId, targetId)
                .eq(QuantifyData::getTargetType, targetType)
                .eq(QuantifyData::getPeriod, period);
        return this.list(queryWrapper);
    }

    @Override
    public List<QuantifyData> getByPeriod(String period) {
        LambdaQueryWrapper<QuantifyData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuantifyData::getPeriod, period);
        return this.list(queryWrapper);
    }

    @Override
    public QuantifyCoreIndicatorVO getCoreIndicators(String timeRange, String orgLevel, String dimension) {
        QuantifyCoreIndicatorVO coreIndicator = new QuantifyCoreIndicatorVO();
        
        // 获取指定时间范围的数据
        LambdaQueryWrapper<QuantifyData> queryWrapper = new LambdaQueryWrapper<>();
        if (timeRange != null && !timeRange.isEmpty()) {
            queryWrapper.eq(QuantifyData::getPeriod, timeRange);
        }
        
        List<QuantifyData> dataList = this.list(queryWrapper);
        
        if (dataList == null || dataList.isEmpty()) {
            // 返回默认值0%
            coreIndicator.setActivityRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            coreIndicator.setSignRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            coreIndicator.setMaterialRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        } else {
            // 计算平均值（简化实现，实际应用中可以根据业务需求调整计算逻辑）
            BigDecimal totalActivityRate = BigDecimal.ZERO;
            BigDecimal totalSignRate = BigDecimal.ZERO;
            BigDecimal totalMaterialRate = BigDecimal.ZERO;
            
            for (QuantifyData data : dataList) {
                if (data.getActivityRate() != null) {
                    totalActivityRate = totalActivityRate.add(data.getActivityRate());
                }
                if (data.getSignRate() != null) {
                    totalSignRate = totalSignRate.add(data.getSignRate());
                }
                if (data.getMaterialRate() != null) {
                    totalMaterialRate = totalMaterialRate.add(data.getMaterialRate());
                }
            }
            
            int count = dataList.size();
            coreIndicator.setActivityRate(count > 0 ? totalActivityRate.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            coreIndicator.setSignRate(count > 0 ? totalSignRate.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            coreIndicator.setMaterialRate(count > 0 ? totalMaterialRate.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        }
        
        coreIndicator.setTimeRange(timeRange != null ? timeRange : "all");
        coreIndicator.setOrgLevel(orgLevel != null ? orgLevel : "all");
        
        return coreIndicator;
    }

    @Override
    public List<QuantifyStatisticsVO> getOrganizationStatistics(String timeRange, String orgLevel, String indicator) {
        List<QuantifyStatisticsVO> statisticsList = new ArrayList<>();
        
        // 获取组织维度的数据
        LambdaQueryWrapper<QuantifyData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuantifyData::getTargetType, "organization");
        if (timeRange != null && !timeRange.isEmpty()) {
            queryWrapper.eq(QuantifyData::getPeriod, timeRange);
        }
        
        List<QuantifyData> dataList = this.list(queryWrapper);
        
        // 转换为统计视图对象（简化实现）
        int rank = 1;
        for (QuantifyData data : dataList) {
            QuantifyStatisticsVO vo = new QuantifyStatisticsVO();
            vo.setTargetId(data.getTargetId());
            vo.setTargetName("组织" + data.getTargetId()); // 实际应用中应该从组织表查询名称
            vo.setIndicator(indicator != null ? indicator : "综合指标");
            vo.setValue(data.getValue());
            vo.setRank(rank++);
            vo.setPeriod(data.getPeriod());
            
            statisticsList.add(vo);
        }
        
        return statisticsList;
    }

    @Override
    public List<QuantifyStatisticsVO> getPersonalStatistics(String timeRange, String orgLevel, String indicator) {
        List<QuantifyStatisticsVO> statisticsList = new ArrayList<>();
        
        // 获取个人维度的数据
        LambdaQueryWrapper<QuantifyData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuantifyData::getTargetType, "personal");
        if (timeRange != null && !timeRange.isEmpty()) {
            queryWrapper.eq(QuantifyData::getPeriod, timeRange);
        }
        
        List<QuantifyData> dataList = this.list(queryWrapper);
        
        // 转换为统计视图对象（简化实现）
        int rank = 1;
        for (QuantifyData data : dataList) {
            QuantifyStatisticsVO vo = new QuantifyStatisticsVO();
            vo.setTargetId(data.getTargetId());
            vo.setTargetName("用户" + data.getTargetId()); // 实际应用中应该从用户表查询名称
            vo.setIndicator(indicator != null ? indicator : "综合指标");
            vo.setValue(data.getValue());
            vo.setRank(rank++);
            vo.setPeriod(data.getPeriod());
            
            statisticsList.add(vo);
        }
        
        return statisticsList;
    }

    @Override
    public void exportToExcel(String timeRange, String orgLevel, String dimension, String indicator, OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建核心指标工作表
            Sheet coreSheet = workbook.createSheet("核心指标");
            createCoreIndicatorSheet(coreSheet, timeRange, orgLevel, dimension);
            
            // 创建组织统计工作表
            Sheet orgSheet = workbook.createSheet("组织统计");
            createOrganizationStatisticsSheet(orgSheet, timeRange, orgLevel, indicator);
            
            // 创建个人统计工作表
            Sheet personalSheet = workbook.createSheet("个人统计");
            createPersonalStatisticsSheet(personalSheet, timeRange, orgLevel, indicator);
            
            // 写入输出流
            workbook.write(outputStream);
            outputStream.flush();
            
        } catch (IOException e) {
            throw new RuntimeException("导出Excel文件失败: " + e.getMessage());
        }
    }

    /**
     * 创建核心指标工作表
     */
    private void createCoreIndicatorSheet(Sheet sheet, String timeRange, String orgLevel, String dimension) {
        QuantifyCoreIndicatorVO coreIndicator = getCoreIndicators(timeRange, orgLevel, dimension);
        
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("指标名称");
        headerRow.createCell(1).setCellValue("数值");
        
        createDataRow(sheet, 1, "活动参与率", coreIndicator.getActivityRate());
        createDataRow(sheet, 2, "签到率", coreIndicator.getSignRate());
        createDataRow(sheet, 3, "材料完成率", coreIndicator.getMaterialRate());
    }

    /**
     * 创建组织统计工作表
     */
    private void createOrganizationStatisticsSheet(Sheet sheet, String timeRange, String orgLevel, String indicator) {
        List<QuantifyStatisticsVO> statisticsList = getOrganizationStatistics(timeRange, orgLevel, indicator);
        
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("排名");
        headerRow.createCell(1).setCellValue("组织ID");
        headerRow.createCell(2).setCellValue("组织名称");
        headerRow.createCell(3).setCellValue("指标值");
        headerRow.createCell(4).setCellValue("统计周期");
        
        int rowNum = 1;
        for (QuantifyStatisticsVO vo : statisticsList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(vo.getRank() != null ? vo.getRank() : rowNum - 1);
            row.createCell(1).setCellValue(vo.getTargetId() != null ? vo.getTargetId().toString() : "");
            row.createCell(2).setCellValue(vo.getTargetName() != null ? vo.getTargetName() : "");
            row.createCell(3).setCellValue(vo.getValue() != null ? vo.getValue().doubleValue() : 0.0);
            row.createCell(4).setCellValue(vo.getPeriod() != null ? vo.getPeriod() : "");
        }
    }

    /**
     * 创建个人统计工作表
     */
    private void createPersonalStatisticsSheet(Sheet sheet, String timeRange, String orgLevel, String indicator) {
        List<QuantifyStatisticsVO> statisticsList = getPersonalStatistics(timeRange, orgLevel, indicator);
        
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("排名");
        headerRow.createCell(1).setCellValue("用户ID");
        headerRow.createCell(2).setCellValue("用户名");
        headerRow.createCell(3).setCellValue("指标值");
        headerRow.createCell(4).setCellValue("统计周期");
        
        int rowNum = 1;
        for (QuantifyStatisticsVO vo : statisticsList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(vo.getRank() != null ? vo.getRank() : rowNum - 1);
            row.createCell(1).setCellValue(vo.getTargetId() != null ? vo.getTargetId().toString() : "");
            row.createCell(2).setCellValue(vo.getTargetName() != null ? vo.getTargetName() : "");
            row.createCell(3).setCellValue(vo.getValue() != null ? vo.getValue().doubleValue() : 0.0);
            row.createCell(4).setCellValue(vo.getPeriod() != null ? vo.getPeriod() : "");
        }
    }

    /**
     * 创建数据行
     */
    private void createDataRow(Sheet sheet, int rowNum, String name, BigDecimal value) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(value != null ? value.doubleValue() : 0.0);
    }

    @Override
    public boolean generateDataByIndicator(Long indicatorId) {
        try {
            QuantifyIndicator indicator = quantifyIndicatorService.getById(indicatorId);
            if (indicator == null || !"enable".equals(indicator.getStatus())) {
                return false;
            }

            String dimension = indicator.getDimension();
            if ("organization".equals(dimension) || "both".equals(dimension)) {
                generateOrganizationData(indicator);
            }
            if ("personal".equals(dimension) || "both".equals(dimension)) {
                generatePersonalData(indicator);
            }

            return true;
        } catch (Exception e) {
            log.error("根据指标生成数据失败，指标ID: {}", indicatorId, e);
            return false;
        }
    }

    @Override
    public boolean generateAllDataByIndicators() {
        try {
            List<QuantifyIndicator> indicators = quantifyIndicatorService.listByStatus("enable");
            if (indicators.isEmpty()) {
                return true;
            }

            for (QuantifyIndicator indicator : indicators) {
                generateDataByIndicator(indicator.getId());
            }

            return true;
        } catch (Exception e) {
            log.error("根据所有指标生成数据失败", e);
            return false;
        }
    }

    /**
     * 生成组织维度的量化数据
     */
    private void generateOrganizationData(QuantifyIndicator indicator) {
        String period = java.time.LocalDate.now().toString();
        
        List<Organization> organizationList = organizationService.list(null);
        
        for (Organization organization : organizationList) {
            try {
                List<Long> orgIdList = organizationService.getAllSubOrgIds(organization.getId());
                
                QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
                activityQueryWrapper.in("orgId", orgIdList);
                List<Activity> activityList = activityService.list(activityQueryWrapper);
                int totalActivity = activityList.size();
                
                int totalParticipant = 0;
                int totalSign = 0;
                
                for (Activity activity : activityList) {
                    QueryWrapper<ActivityEnroll> enrollQueryWrapper = new QueryWrapper<>();
                    enrollQueryWrapper.eq("activityId", activity.getId());
                    enrollQueryWrapper.eq("participantStatus", ActivityEnrollStatusEnum.ENROLLED.getCode());
                    int participantCount = Math.toIntExact(activityEnrollService.count(enrollQueryWrapper));
                    totalParticipant += participantCount;
                    
                    QueryWrapper<ActivityEnroll> signQueryWrapper = new QueryWrapper<>();
                    signQueryWrapper.eq("activityId", activity.getId());
                    signQueryWrapper.eq("participantStatus", ActivityEnrollStatusEnum.ENROLLED.getCode());
                    signQueryWrapper.eq("isSign", ActivityEnrollSignEnum.SIGNED.getCode());
                    int signCount = Math.toIntExact(activityEnrollService.count(signQueryWrapper));
                    totalSign += signCount;
                }
                
                BigDecimal activityRate = totalActivity > 0 ? 
                    BigDecimal.valueOf(totalParticipant).divide(BigDecimal.valueOf(totalActivity * 1.0), 4, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;
                
                BigDecimal signRate = totalParticipant > 0 ? 
                    BigDecimal.valueOf(totalSign).divide(BigDecimal.valueOf(totalParticipant), 4, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;
                
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("orgId", organization.getId());
                List<User> userList = userService.list(userQueryWrapper);
                
                int totalTemplates = 0;
                int totalSubmitted = 0;
                
                QueryWrapper<MaterialTemplate> templateQueryWrapper = new QueryWrapper<>();
                templateQueryWrapper.eq("status", "enable");
                List<MaterialTemplate> templateList = materialTemplateService.list(templateQueryWrapper);
                
                for (User user : userList) {
                    totalTemplates += templateList.size();
                    
                    for (MaterialTemplate template : templateList) {
                        QueryWrapper<MaterialSubmission> submissionQueryWrapper = new QueryWrapper<>();
                        submissionQueryWrapper.eq("userId", user.getId());
                        submissionQueryWrapper.eq("materialName", template.getName());
                        submissionQueryWrapper.in("submitStatus", "submitted", "approved", "final_approved", "archived");
                        long count = materialSubmissionService.count(submissionQueryWrapper);
                        if (count > 0) {
                            totalSubmitted++;
                        }
                    }
                }
                
                BigDecimal materialRate = totalTemplates > 0 ? 
                    BigDecimal.valueOf(totalSubmitted).divide(BigDecimal.valueOf(totalTemplates), 4, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;
                
                BigDecimal totalValue = activityRate.add(signRate).add(materialRate);
                BigDecimal value = totalValue.divide(BigDecimal.valueOf(3), 4, RoundingMode.HALF_UP);
                
                saveOrUpdateQuantifyData(indicator.getId(), organization.getId(), "organization", period, 
                    value, activityRate, signRate, materialRate);
                
                log.info("组织维度数据生成成功，组织ID: {}, 指标: {}, 周期: {}", organization.getId(), indicator.getName(), period);
                
            } catch (Exception e) {
                log.error("组织维度数据生成失败，组织ID: {}, 指标: {}, 周期: {}", organization.getId(), indicator.getName(), period, e);
            }
        }
    }

    /**
     * 生成个人维度的量化数据
     */
    private void generatePersonalData(QuantifyIndicator indicator) {
        String period = java.time.LocalDate.now().toString();
        
        List<User> userList = userService.list(null);
        
        for (User user : userList) {
            try {
                List<Long> orgIdList = organizationService.getAllParentOrgIds(user.getOrgId());
                QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
                activityQueryWrapper.in("orgId", orgIdList);
                int totalActivity = Math.toIntExact(activityService.count(activityQueryWrapper));
                
                QueryWrapper<ActivityEnroll> enrollQueryWrapper = new QueryWrapper<>();
                enrollQueryWrapper.eq("userId", user.getId());
                enrollQueryWrapper.eq("participantStatus", ActivityEnrollStatusEnum.ENROLLED.getCode());
                List<ActivityEnroll> enrollList = activityEnrollService.list(enrollQueryWrapper);
                int participateActivity = enrollList.size();
                
                BigDecimal activityRate = totalActivity > 0 ? 
                    BigDecimal.valueOf(participateActivity).divide(BigDecimal.valueOf(totalActivity), 4, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;
                
                QueryWrapper<ActivityEnroll> signQueryWrapper = new QueryWrapper<>();
                signQueryWrapper.eq("userId", user.getId());
                signQueryWrapper.eq("participantStatus", ActivityEnrollStatusEnum.ENROLLED.getCode());
                signQueryWrapper.eq("isSign", ActivityEnrollSignEnum.SIGNED.getCode());
                int signActivity = Math.toIntExact(activityEnrollService.count(signQueryWrapper));
                
                BigDecimal signRate = participateActivity > 0 ? 
                    BigDecimal.valueOf(signActivity).divide(BigDecimal.valueOf(participateActivity), 4, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;
                
                QueryWrapper<MaterialTemplate> templateQueryWrapper = new QueryWrapper<>();
                templateQueryWrapper.eq("status", "enable");
                List<MaterialTemplate> templateList = materialTemplateService.list(templateQueryWrapper);
                
                int totalTemplates = templateList.size();
                int submittedCount = 0;
                
                for (MaterialTemplate template : templateList) {
                    QueryWrapper<MaterialSubmission> submissionQueryWrapper = new QueryWrapper<>();
                    submissionQueryWrapper.eq("userId", user.getId());
                    submissionQueryWrapper.eq("materialName", template.getName());
                    submissionQueryWrapper.in("submitStatus", "submitted", "approved", "final_approved", "archived");
                    long count = materialSubmissionService.count(submissionQueryWrapper);
                    if (count > 0) {
                        submittedCount++;
                    }
                }
                
                BigDecimal materialRate = totalTemplates > 0 ? 
                    BigDecimal.valueOf(submittedCount).divide(BigDecimal.valueOf(totalTemplates), 4, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;
                
                BigDecimal totalValue = activityRate.add(signRate).add(materialRate);
                BigDecimal value = totalValue.divide(BigDecimal.valueOf(3), 4, RoundingMode.HALF_UP);
                
                saveOrUpdateQuantifyData(indicator.getId(), user.getId(), "personal", period, 
                    value, activityRate, signRate, materialRate);
                
                log.info("个人维度数据生成成功，用户ID: {}, 指标: {}, 周期: {}", user.getId(), indicator.getName(), period);
                
            } catch (Exception e) {
                log.error("个人维度数据生成失败，用户ID: {}, 指标: {}, 周期: {}", user.getId(), indicator.getName(), period, e);
            }
        }
    }

    /**
     * 保存或更新量化数据
     */
    private void saveOrUpdateQuantifyData(Long indicatorId, Long targetId, String targetType, String period, 
                                         BigDecimal value, BigDecimal activityRate, BigDecimal signRate, BigDecimal materialRate) {
        // 检查是否已存在该指标的统计数据
        LambdaQueryWrapper<QuantifyData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuantifyData::getIndicatorId, indicatorId)
                .eq(QuantifyData::getTargetId, targetId)
                .eq(QuantifyData::getTargetType, targetType)
                .eq(QuantifyData::getPeriod, period);
        
        QuantifyData existing = this.getOne(queryWrapper);
        Date now = new Date();
        
        QuantifyData quantifyData = new QuantifyData();
        quantifyData.setIndicatorId(indicatorId);
        quantifyData.setTargetId(targetId);
        quantifyData.setTargetType(targetType);
        quantifyData.setPeriod(period);
        quantifyData.setValue(value);
        quantifyData.setActivityRate(activityRate);
        quantifyData.setSignRate(signRate);
        quantifyData.setMaterialRate(materialRate);
        quantifyData.setUpdateTime(now);
        
        if (existing != null) {
            // 更新已存在的数据
            quantifyData.setId(existing.getId());
            quantifyData.setCreateTime(existing.getCreateTime());
            this.updateById(quantifyData);
        } else {
            // 保存新数据
            quantifyData.setCreateTime(now);
            this.save(quantifyData);
        }
    }

    @Resource
    private QuantifyIndicatorService quantifyIndicatorService;
}
