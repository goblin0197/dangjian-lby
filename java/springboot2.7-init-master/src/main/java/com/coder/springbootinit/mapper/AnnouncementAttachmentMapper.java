package com.coder.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.springbootinit.model.entity.AnnouncementAttachment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告附件 Mapper
 *
 */
@Mapper
public interface AnnouncementAttachmentMapper extends BaseMapper<AnnouncementAttachment> {

}