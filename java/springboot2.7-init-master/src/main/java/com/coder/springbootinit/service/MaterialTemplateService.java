package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.material.template.MaterialTemplateAddRequest;
import com.coder.springbootinit.model.dto.material.template.MaterialTemplateQueryRequest;
import com.coder.springbootinit.model.dto.material.template.MaterialTemplateUpdateRequest;
import com.coder.springbootinit.model.entity.MaterialTemplate;
import com.coder.springbootinit.model.vo.MaterialTemplateVO;

import java.util.List;

/**
 * 材料模板服务接口
 *
 */
public interface MaterialTemplateService extends IService<MaterialTemplate> {

    /**
     * 添加材料模板
     * @param materialTemplateAddRequest 模板添加请求
     * @param uploadUserId 上传用户 ID
     * @param uploadUserName 上传用户姓名
     * @return 模板
     */
    MaterialTemplate addMaterialTemplate(MaterialTemplateAddRequest materialTemplateAddRequest, Long uploadUserId, String uploadUserName);

    /**
     * 更新材料模板
     * @param materialTemplateUpdateRequest 模板更新请求
     * @return 是否成功
     */
    boolean updateMaterialTemplate(MaterialTemplateUpdateRequest materialTemplateUpdateRequest);

    /**
     * 删除材料模板
     * @param id 模板 ID
     * @return 是否成功
     */
    boolean deleteMaterialTemplate(Long id);

    /**
     * 批量删除材料模板
     * @param ids 模板 ID 列表
     * @return 是否成功
     */
    boolean batchDeleteMaterialTemplate(List<Long> ids);

    /**
     * 切换模板状态
     * @param id 模板 ID
     * @return 是否成功
     */
    boolean toggleMaterialTemplateStatus(Long id);

    /**
     * 批量切换模板状态
     * @param ids 模板 ID 列表
     * @param targetStatus 目标状态
     * @return 是否成功
     */
    boolean batchToggleMaterialTemplateStatus(List<Long> ids, String targetStatus);

    /**
     * 获取材料模板 VO
     * @param id 模板 ID
     * @return 模板 VO
     */
    MaterialTemplateVO getMaterialTemplateVO(Long id);

    /**
     * 获取材料模板列表
     * @param materialTemplateQueryRequest 查询请求
     * @return 模板 VO 列表
     */
    List<MaterialTemplateVO> listMaterialTemplateVO(MaterialTemplateQueryRequest materialTemplateQueryRequest);

    /**
     * 获取查询条件
     * @param materialTemplateQueryRequest 查询请求
     * @return 查询条件
     */
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MaterialTemplate> getQueryWrapper(MaterialTemplateQueryRequest materialTemplateQueryRequest);
}