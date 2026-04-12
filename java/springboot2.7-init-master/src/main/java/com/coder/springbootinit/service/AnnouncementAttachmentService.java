package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.AnnouncementAttachment;

import java.util.List;

/**
 * 公告附件服务
 *
 */
public interface AnnouncementAttachmentService extends IService<AnnouncementAttachment> {

    /**
     * 根据公告ID获取附件列表
     * @param announcementId 公告ID
     * @return 附件列表
     */
    List<AnnouncementAttachment> getByAnnouncementId(Long announcementId);
}
