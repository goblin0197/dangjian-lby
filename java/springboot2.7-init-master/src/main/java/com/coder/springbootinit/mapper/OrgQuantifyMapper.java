package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.OrgQuantify;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织量化统计Mapper
 *
 */
@Mapper
public interface OrgQuantifyMapper extends BaseMapper<OrgQuantify> {
}
