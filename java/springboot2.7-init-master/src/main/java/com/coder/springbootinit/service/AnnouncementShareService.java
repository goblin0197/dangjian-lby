package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.AnnouncementShare;

import java.util.List;

/**
 * 公告分享服务
 *
 */
public interface AnnouncementShareService extends IService<AnnouncementShare> {

    /**
     * 生成分享链接
     * @param announcementId 公告ID
     * @param shareUserId 分享用户ID
     * @param shareUserName 分享用户姓名
     * @param expireTime 过期时间
     * @return 分享对象
     */
    AnnouncementShare generateShare(Long announcementId, Long shareUserId, String shareUserName, String expireTime);

    /**
     * 访问分享链接
     * @param shareCode 分享码
     * @return 分享对象
     */
    AnnouncementShare accessShare(String shareCode);

    /**
     * 根据公告ID获取分享记录列表
     * @param announcementId 公告ID
     * @return 分享记录列表
     */
    List<AnnouncementShare> getShareListByAnnouncementId(Long announcementId);
}
