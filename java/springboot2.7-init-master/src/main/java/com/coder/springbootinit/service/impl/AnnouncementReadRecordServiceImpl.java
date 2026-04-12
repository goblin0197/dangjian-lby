package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.mapper.AnnouncementReadRecordMapper;
import com.coder.springbootinit.model.entity.AnnouncementReadRecord;
import com.coder.springbootinit.service.AnnouncementReadRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 公告阅读记录服务实现
 *
 */
@Service
public class AnnouncementReadRecordServiceImpl extends ServiceImpl<AnnouncementReadRecordMapper, AnnouncementReadRecord> implements AnnouncementReadRecordService {

    @Override
    public Long getUnreadCount(Long userId) {
        // 这里应该查询未读公告数，简化处理
        return 0L;
    }

    @Override
    public boolean markAsRead(Long announcementId, Long userId) {
        // 检查是否已存在记录
        LambdaQueryWrapper<AnnouncementReadRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnouncementReadRecord::getAnnouncementId, announcementId)
                .eq(AnnouncementReadRecord::getUserId, userId);
        AnnouncementReadRecord existing = this.getOne(queryWrapper);
        if (existing != null) {
            return true;
        }

        // 创建新记录
        AnnouncementReadRecord record = new AnnouncementReadRecord();
        record.setAnnouncementId(announcementId);
        record.setUserId(userId);
        record.setReadTime(new Date());
        return this.save(record);
    }

    @Override
    public List<AnnouncementReadRecord> getByUserId(Long userId) {
        LambdaQueryWrapper<AnnouncementReadRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnouncementReadRecord::getUserId, userId);
        return this.list(queryWrapper);
    }
}
