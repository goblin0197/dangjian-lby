package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.MaterialSubmission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 材料提交 Mapper
 *
 */
@Mapper
public interface MaterialSubmissionMapper extends BaseMapper<MaterialSubmission> {

}