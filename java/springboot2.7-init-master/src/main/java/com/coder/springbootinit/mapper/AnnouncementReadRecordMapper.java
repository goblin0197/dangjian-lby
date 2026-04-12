package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.AnnouncementReadRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告阅读记录 Mapper
 *
 */
@Mapper
public interface AnnouncementReadRecordMapper extends BaseMapper<AnnouncementReadRecord> {

}