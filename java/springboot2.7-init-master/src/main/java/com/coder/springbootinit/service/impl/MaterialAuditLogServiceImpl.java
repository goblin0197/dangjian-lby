package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.mapper.MaterialAuditLogMapper;
import com.coder.springbootinit.model.entity.MaterialAuditLog;
import com.coder.springbootinit.service.MaterialAuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 材料审核日志服务实现类
 *
 */
@Service
@Slf4j
public class MaterialAuditLogServiceImpl extends ServiceImpl<MaterialAuditLogMapper, MaterialAuditLog> implements MaterialAuditLogService {

}