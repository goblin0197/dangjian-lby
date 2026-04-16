package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.mapper.QuantifyIndicatorMapper;
import com.coder.springbootinit.model.entity.QuantifyIndicator;
import com.coder.springbootinit.service.QuantifyIndicatorService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 量化指标服务实现
 *
 */
@Service
public class QuantifyIndicatorServiceImpl extends ServiceImpl<QuantifyIndicatorMapper, QuantifyIndicator> implements QuantifyIndicatorService {

    @Override
    public List<QuantifyIndicator> listByStatus(String status) {
        LambdaQueryWrapper<QuantifyIndicator> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuantifyIndicator::getStatus, status);
        return this.list(queryWrapper);
    }

    @Override
    public boolean toggleStatus(Long id, String status) {
        QuantifyIndicator indicator = new QuantifyIndicator();
        indicator.setId(id);
        indicator.setStatus(status);
        return this.updateById(indicator);
    }

    @Override
    public String importFromExcel(MultipartFile file) {
        List<QuantifyIndicator> indicatorList = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // 跳过标题行，从第二行开始读取数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                // 读取各列数据（假设Excel格式：名称、规则、维度、组织层级、状态）
                String name = getCellStringValue(row.getCell(0));
                String rule = getCellStringValue(row.getCell(1));
                String dimension = getCellStringValue(row.getCell(2));
                String orgLevel = getCellStringValue(row.getCell(3));
                String status = getCellStringValue(row.getCell(4));
                
                if (name == null || name.isEmpty()) continue;
                
                QuantifyIndicator indicator = new QuantifyIndicator();
                indicator.setName(name);
                indicator.setRule(rule != null ? rule : "");
                indicator.setDimension(getDimensionValue(dimension));
                indicator.setOrgLevel(orgLevel != null ? orgLevel : "[]");
                indicator.setStatus(getStatusValue(status));
                
                indicatorList.add(indicator);
            }
            
            if (!indicatorList.isEmpty()) {
                boolean result = this.saveBatch(indicatorList);
                if (result) {
                    return "成功导入 " + indicatorList.size() + " 条量化指标";
                } else {
                    return "导入失败";
                }
            } else {
                return "没有找到有效数据";
            }
            
        } catch (IOException e) {
            throw new RuntimeException("解析Excel文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取单元格字符串值
     * @param cell 单元格
     * @return 字符串值
     */
    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    /**
     * 组织：organization
     * 个人：personal
     * 两者：both
     * @param dimension
     * @return
     */
    private String getDimensionValue(String dimension) {
        if(dimension == "组织"){
            return "organization";
        }
        if(dimension == "个人"){
            return "personal";
        }
        return "both";
    }

    /**
     * 启用：enable
     * 禁用：disable
     * @param status
     * @return
     */
    private String getStatusValue(String status) {
        if(status == "启用"){
            return "enable";
        }
        return "disable";
    }
}
