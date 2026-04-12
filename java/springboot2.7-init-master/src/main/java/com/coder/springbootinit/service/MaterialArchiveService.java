package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.material.archive.MaterialArchiveQueryRequest;
import com.coder.springbootinit.model.dto.material.archive.MaterialArchiveRequest;
import com.coder.springbootinit.model.entity.MaterialArchive;
import com.coder.springbootinit.model.vo.MaterialArchiveStatVO;
import com.coder.springbootinit.model.vo.MaterialArchiveVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 材料归档服务接口
 *
 */
public interface MaterialArchiveService extends IService<MaterialArchive> {

    /**
     * 归档材料
     * @param archiveRequest 归档请求
     * @param archiveUserId 归档人 ID
     * @param archiveUserName 归档人姓名
     * @return 归档记录
     */
    MaterialArchive archiveMaterial(MaterialArchiveRequest archiveRequest, Long archiveUserId, String archiveUserName);

    /**
     * 获取归档详情 VO
     * @param id 归档 ID
     * @return 归档 VO
     */
    MaterialArchiveVO getMaterialArchiveVO(Long id);

    /**
     * 获取归档列表
     * @param queryRequest 查询请求
     * @param userId 当前用户 ID
     * @param userRole 当前用户角色
     * @param orgLevel 当前用户组织层级
     * @return 归档 VO 列表
     */
    List<MaterialArchiveVO> listMaterialArchiveVO(MaterialArchiveQueryRequest queryRequest, Long userId, String userRole, String orgLevel);

    /**
     * 获取归档统计
     * @param userId 用户 ID
     * @param userRole 用户角色
     * @param orgLevel 组织层级
     * @return 归档统计 VO
     */
    MaterialArchiveStatVO getMaterialArchiveStat(Long userId, String userRole, String orgLevel);

    /**
     * 获取查询条件
     * @param queryRequest 查询请求
     * @param userId 当前用户 ID
     * @param userRole 当前用户角色
     * @param orgLevel 当前用户组织层级
     * @return 查询条件
     */
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MaterialArchive> getQueryWrapper(MaterialArchiveQueryRequest queryRequest, Long userId, String userRole, String orgLevel);

    /**
     * 导出单个归档材料文件
     * @param id 归档 ID
     * @param response HTTP 响应
     */
    void exportSingleArchive(Long id, HttpServletResponse response);
}