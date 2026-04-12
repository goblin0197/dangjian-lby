package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.MaterialAuditLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 材料审核日志 Mapper
 *
 */
@Mapper
public interface MaterialAuditLogMapper extends BaseMapper<MaterialAuditLog> {

}