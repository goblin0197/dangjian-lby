package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.AnnouncementReadRecord;

import java.util.List;

/**
 * 公告阅读记录服务
 *
 */
public interface AnnouncementReadRecordService extends IService<AnnouncementReadRecord> {

    /**
     * 获取未读公告数量
     * @param userId 用户ID
     * @return 未读数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 标记公告为已读
     * @param announcementId 公告ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsRead(Long announcementId, Long userId);

    /**
     * 根据用户ID获取已读列表
     * @param userId 用户ID
     * @return 已读列表
     */
    List<AnnouncementReadRecord> getByUserId(Long userId);
}
