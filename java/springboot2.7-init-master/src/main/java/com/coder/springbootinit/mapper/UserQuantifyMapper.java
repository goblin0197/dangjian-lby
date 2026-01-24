package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.UserQuantify;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户量化统计Mapper
 *
 */
@Mapper
public interface UserQuantifyMapper extends BaseMapper<UserQuantify> {
}
