package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.mapper.AnnouncementShareMapper;
import com.coder.springbootinit.model.entity.AnnouncementShare;
import com.coder.springbootinit.service.AnnouncementShareService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 公告分享服务实现
 *
 */
@Service
public class AnnouncementShareServiceImpl extends ServiceImpl<AnnouncementShareMapper, AnnouncementShare> implements AnnouncementShareService {

    @Override
    public AnnouncementShare generateShare(Long announcementId, Long shareUserId, String shareUserName, String expireTime) {
        String shareCode = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        String shareUrl = "/api/announcement/share/access?shareCode=" + shareCode;

        AnnouncementShare share = new AnnouncementShare();
        share.setAnnouncementId(announcementId);
        share.setShareCode(shareCode);
        share.setShareUrl(shareUrl);
        share.setShareUserId(shareUserId);
        share.setShareUserName(shareUserName);
        share.setShareTime(new Date());
        share.setViewCount(0);

        if (expireTime != null && !expireTime.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                share.setExpireTime(sdf.parse(expireTime));
            } catch (Exception e) {
                // 解析失败，不设置过期时间
            }
        }

        this.save(share);
        return share;
    }

    @Override
    public AnnouncementShare accessShare(String shareCode) {
        LambdaQueryWrapper<AnnouncementShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnouncementShare::getShareCode, shareCode);
        AnnouncementShare share = this.getOne(queryWrapper);

        if (share != null) {
            // 更新查看次数
            share.setViewCount(share.getViewCount() + 1);
            this.updateById(share);
        }

        return share;
    }

    @Override
    public List<AnnouncementShare> getShareListByAnnouncementId(Long announcementId) {
        if (announcementId == null) {
            throw new com.coder.springbootinit.exception.BusinessException(com.coder.springbootinit.common.ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        
        LambdaQueryWrapper<AnnouncementShare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnouncementShare::getAnnouncementId, announcementId)
                .orderByDesc(AnnouncementShare::getShareTime);
        
        return this.list(queryWrapper);
    }
}
