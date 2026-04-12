package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.QuantifyIndicator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 量化指标服务
 *
 */
public interface QuantifyIndicatorService extends IService<QuantifyIndicator> {

    /**
     * 根据状态获取指标列表
     * @param status 状态
     * @return 指标列表
     */
    List<QuantifyIndicator> listByStatus(String status);

    /**
     * 切换指标状态
     * @param id 指标ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean toggleStatus(Long id, String status);

    /**
     * 从Excel导入量化指标
     * @param file Excel文件
     * @return 导入结果信息
     */
    String importFromExcel(MultipartFile file);
}
