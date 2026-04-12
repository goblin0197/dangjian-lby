package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.mapper.AnnouncementAttachmentMapper;
import com.coder.springbootinit.model.entity.AnnouncementAttachment;
import com.coder.springbootinit.service.AnnouncementAttachmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告附件服务实现
 *
 */
@Service
public class AnnouncementAttachmentServiceImpl extends ServiceImpl<AnnouncementAttachmentMapper, AnnouncementAttachment> implements AnnouncementAttachmentService {

    @Override
    public List<AnnouncementAttachment> getByAnnouncementId(Long announcementId) {
        LambdaQueryWrapper<AnnouncementAttachment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnouncementAttachment::getAnnouncementId, announcementId);
        return this.list(queryWrapper);
    }
}
