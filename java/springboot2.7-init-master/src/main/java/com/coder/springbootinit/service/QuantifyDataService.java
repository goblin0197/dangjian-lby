package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.QuantifyData;
import com.coder.springbootinit.model.vo.QuantifyCoreIndicatorVO;
import com.coder.springbootinit.model.vo.QuantifyStatisticsVO;

import java.io.OutputStream;
import java.util.List;

/**
 * 量化数据记录服务
 *
 */
public interface QuantifyDataService extends IService<QuantifyData> {

    /**
     * 获取指定对象的量化数据
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param period 统计周期
     * @return 量化数据列表
     */
    List<QuantifyData> getByTargetAndPeriod(Long targetId, String targetType, String period);

    /**
     * 获取指定周期的量化数据
     * @param period 统计周期
     * @return 量化数据列表
     */
    List<QuantifyData> getByPeriod(String period);

    /**
     * 获取核心指标数据
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param dimension 统计维度
     * @return 核心指标数据
     */
    QuantifyCoreIndicatorVO getCoreIndicators(String timeRange, String orgLevel, String dimension);

    /**
     * 获取组织维度统计数据
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param indicator 统计指标
     * @return 组织统计数据列表
     */
    List<QuantifyStatisticsVO> getOrganizationStatistics(String timeRange, String orgLevel, String indicator);

    /**
     * 获取个人维度统计数据
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param indicator 统计指标
     * @return 个人统计数据列表
     */
    List<QuantifyStatisticsVO> getPersonalStatistics(String timeRange, String orgLevel, String indicator);

    /**
     * 导出统计报表为Excel格式
     * @param timeRange 时间范围
     * @param orgLevel 组织层级
     * @param dimension 统计维度
     * @param indicator 统计指标
     * @param outputStream 输出流
     */
    void exportToExcel(String timeRange, String orgLevel, String dimension, String indicator, OutputStream outputStream);

    /**
     * 根据单个量化指标生成数据
     * @param indicatorId 指标ID
     * @return 是否生成成功
     */
    boolean generateDataByIndicator(Long indicatorId);

    /**
     * 根据所有启用的量化指标生成数据
     * @return 是否生成成功
     */
    boolean generateAllDataByIndicators();
}
