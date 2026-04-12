package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.NoticeMapper;
import com.coder.springbootinit.model.dto.notice.NoticeAddRequest;
import com.coder.springbootinit.model.dto.notice.NoticeQueryRequest;
import com.coder.springbootinit.model.dto.notice.NoticeUpdateRequest;
import com.coder.springbootinit.model.entity.Notice;
import com.coder.springbootinit.model.entity.Organization;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.NoticeIsTopEnum;
import com.coder.springbootinit.model.enums.NoticeStatusEnum;
import com.coder.springbootinit.model.vo.NoticeVO;
import com.coder.springbootinit.service.NoticeService;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告服务实现
 *
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private UserService userService;

    @Override
    public Notice addNotice(NoticeAddRequest noticeAddRequest, Long publisherId) {
        // 验证公告标题和内容
        if (StringUtils.isBlank(noticeAddRequest.getTitle())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告标题不能为空");
        }
        if (StringUtils.isBlank(noticeAddRequest.getContent())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告内容不能为空");
        }
        if(noticeAddRequest.getOrgId() != null && noticeAddRequest.getOrgId() != 0){
            Long orgId = noticeAddRequest.getOrgId();
            // 验证组织是否存在
            Organization organization = organizationService.getById(orgId);
            if (organization == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "组织不存在");
            }
        }
        // 创建公告实体
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeAddRequest, notice);
        notice.setPublisherId(publisherId);

        // 设置默认值
        if (notice.getStatus() == null) {
            notice.setStatus(NoticeStatusEnum.DRAFT.getCode());
        }
        if (notice.getIsTop() == null) {
            notice.setIsTop(NoticeIsTopEnum.NO.getCode());
        }
        if (notice.getPublishTime() == null && NoticeStatusEnum.PUBLISHED.getCode().equals(notice.getStatus())) {
            notice.setPublishTime(new Date());
        }

        // 保存公告
        boolean saveResult = this.save(notice);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加公告失败");
        }
        return notice;
    }

    @Override
    public boolean updateNotice(NoticeUpdateRequest noticeUpdateRequest) {
        // 验证公告ID
        if (noticeUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }

        // 验证公告标题和内容
        if (StringUtils.isBlank(noticeUpdateRequest.getTitle())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告标题不能为空");
        }
        if (StringUtils.isBlank(noticeUpdateRequest.getContent())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告内容不能为空");
        }

        // 获取公告
        Notice notice = this.getById(noticeUpdateRequest.getId());
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }

        // 检查公告状态，已发布的公告不可修改
        if (NoticeStatusEnum.PUBLISHED.getCode().equals(notice.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已发布的公告不可修改");
        }

        // 更新公告信息
        BeanUtils.copyProperties(noticeUpdateRequest, notice);

        // 保存更新
        return this.updateById(notice);
    }

    @Override
    public boolean deleteNotice(Long id) {
        // 验证公告ID
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }

        // 获取公告
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }

        // 软删除公告
        return this.removeById(id);
    }

    @Override
    public NoticeVO getNoticeVO(Long id) {
        // 验证公告ID
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }

        // 获取公告
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }

        // 转换为VO
        return noticeToVO(notice);
    }

    @Override
    public List<NoticeVO> listNoticeVO(NoticeQueryRequest noticeQueryRequest) {
        // 获取查询条件
        QueryWrapper<Notice> queryWrapper = this.getQueryWrapper(noticeQueryRequest);

        // 查询公告列表
        List<Notice> noticeList = this.list(queryWrapper);

        // 转换为VO列表
        return noticeList.stream()
                .map(this::noticeToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean publishNotice(Long id) {
        // 验证公告ID
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }

        // 获取公告
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }

        // 检查公告状态
        if (NoticeStatusEnum.PUBLISHED.getCode().equals(notice.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告已发布");
        }

        // 更新公告状态为已发布
        notice.setStatus(NoticeStatusEnum.PUBLISHED.getCode());
        if(notice.getPublishTime() == null)
            notice.setPublishTime(new Date());

        // 保存更新
        return this.updateById(notice);
    }

    @Override
    public boolean withdrawNotice(Long id) {
        // 验证公告ID
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }

        // 获取公告
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }

        // 检查公告状态
        if (!NoticeStatusEnum.PUBLISHED.getCode().equals(notice.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有已发布的公告可以撤回");
        }

        // 更新公告状态为草稿
        notice.setStatus(NoticeStatusEnum.DRAFT.getCode());

        // 保存更新
        return this.updateById(notice);
    }

    @Override
    public boolean markAsRead(Long announcementId, Long userId) {
        if (announcementId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告 ID 和用户 ID 不能为空");
        }
        // TODO: 实现标记已读逻辑
        return true;
    }

    @Override
    public Integer getUnreadCount(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 ID 不能为空");
        }
        // TODO: 实现获取未读数量逻辑
        return 0;
    }

    @Override
    public QueryWrapper<Notice> getQueryWrapper(NoticeQueryRequest noticeQueryRequest) {
        // 创建查询条件
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        if (noticeQueryRequest == null) {
            return queryWrapper;
        }

        // 设置查询条件
        Long id = noticeQueryRequest.getId();
        String title = noticeQueryRequest.getTitle();
        Long orgId = noticeQueryRequest.getOrgId();
        Long publisherId = noticeQueryRequest.getPublisherId();
        Integer status = noticeQueryRequest.getStatus();
        Integer isTop = noticeQueryRequest.getIsTop();
        Date publishTimeStart = noticeQueryRequest.getPublishTimeStart();
        Date publishTimeEnd = noticeQueryRequest.getPublishTimeEnd();

        // 拼接查询条件
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(orgId != null, "orgId", orgId);
        queryWrapper.eq(publisherId != null, "publisherId", publisherId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(isTop != null, "isTop", isTop);
        queryWrapper.ge(publishTimeStart != null, "publishTime", publishTimeStart);
        queryWrapper.le(publishTimeEnd != null, "publishTime", publishTimeEnd);
        queryWrapper.eq("isDelete", 0);

        // 排序，置顶的公告优先，然后按发布时间倒序
        queryWrapper.orderByDesc("isTop");
        queryWrapper.orderByDesc("publishTime");

        return queryWrapper;
    }

    /**
     * 将公告实体转换为VO
     * @param notice 公告实体
     * @return 公告VO
     */
    private NoticeVO noticeToVO(Notice notice) {
        NoticeVO noticeVO = new NoticeVO();
        BeanUtils.copyProperties(notice, noticeVO);

        // 设置发布人姓名
        if (notice.getPublisherId() != null) {
            User user = userService.getById(notice.getPublisherId());
            if (user != null) {
                noticeVO.setPublisherName(user.getUserName());
            }
        }

        // 设置党组织名称
        if (notice.getOrgId() != null) {
            Organization organization = organizationService.getById(notice.getOrgId());
            if (organization != null) {
                noticeVO.setOrgName(organization.getOrgName());
            }
        }

        // 设置状态名称
        NoticeStatusEnum noticeStatusEnum = NoticeStatusEnum.getByCode(notice.getStatus());
        noticeVO.setStatusName(noticeStatusEnum != null ? noticeStatusEnum.getDescription() : "未知");

        // 设置是否置顶名称
        NoticeIsTopEnum noticeIsTopEnum = NoticeIsTopEnum.getByCode(notice.getIsTop());
        noticeVO.setIsTopName(noticeIsTopEnum != null ? noticeIsTopEnum.getDescription() : "未知");

        return noticeVO;
    }

    @Override
    public boolean batchDeleteNotices(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID列表不能为空");
        }
        return this.removeByIds(ids);
    }

    @Override
    public boolean offlineNotice(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }
        
        // 检查公告状态，只有已发布的公告才能下架
        if (!NoticeStatusEnum.PUBLISHED.getCode().equals(notice.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有已发布的公告可以下架");
        }
        
        // 更新公告状态为下架（使用OFFLINE状态，如果枚举中没有则使用2表示下架）
        notice.setStatus(2); // 2-已下架
        
        return this.updateById(notice);
    }

    @Override
    public boolean republishNotice(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }
        
        // 检查公告状态，只有下架的公告才能重新发布
        if (!Integer.valueOf(2).equals(notice.getStatus()) && !NoticeStatusEnum.DRAFT.getCode().equals(notice.getStatus())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有下架或草稿状态的公告可以重新发布");
        }
        
        // 更新公告状态为已发布
        notice.setStatus(NoticeStatusEnum.PUBLISHED.getCode());
        if (notice.getPublishTime() == null) {
            notice.setPublishTime(new Date());
        }
        
        return this.updateById(notice);
    }

    @Override
    public boolean topNotice(Long id, Integer isTop) {
        if (id == null || isTop == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        
        Notice notice = this.getById(id);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }
        
        // 更新置顶状态
        notice.setIsTop(isTop);
        
        return this.updateById(notice);
    }

    @Override
    public boolean incrementReadCount(Long announcementId) {
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }

        Notice notice = this.getById(announcementId);
        if (notice == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公告不存在");
        }

        // 增加阅读量
        int currentCount = notice.getReadCount() != null ? notice.getReadCount() : 0;
        notice.setReadCount(currentCount + 1);

        return this.updateById(notice);
    }

    @Override
    public List<NoticeVO> getTimelineList(String timeRange, Integer status, Integer current, Integer pageSize) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        
        // 设置时间范围过滤
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        
        if (timeRange != null) {
            switch (timeRange.toLowerCase()) {
                case "today":
                    // 今天
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    queryWrapper.ge("publishTime", calendar.getTime());
                    break;
                case "week":
                    // 最近一周
                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    queryWrapper.ge("publishTime", calendar.getTime());
                    break;
                case "month":
                    // 最近一月
                    calendar.add(Calendar.MONTH, -1);
                    queryWrapper.ge("publishTime", calendar.getTime());
                    break;
                default:
                    // 默认不限制时间
                    break;
            }
        }
        
        // 设置状态过滤
        if (status != null) {
            queryWrapper.eq("status", status);
        } else {
            // 默认只查询已发布的公告
            queryWrapper.eq("status", NoticeStatusEnum.PUBLISHED.getCode());
        }
        
        // 排除已删除的记录
        queryWrapper.eq("isDelete", 0);
        
        // 按发布时间倒序排列
        queryWrapper.orderByDesc("isTop");
        queryWrapper.orderByDesc("publishTime");
        
        // 分页处理
        int pageNum = current != null ? current : 1;
        int size = pageSize != null ? pageSize : 10;
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Notice> page =
                this.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, size), queryWrapper);
        
        // 转换为VO列表
        return page.getRecords().stream()
                .map(this::noticeToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeVO> searchTimeline(String keyword, String timeRange, Integer current, Integer pageSize) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        
        // 关键词搜索（标题或内容）
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                    .like("title", keyword)
                    .or()
                    .like("content", keyword));
        }
        
        // 设置时间范围过滤
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        
        if (timeRange != null && !timeRange.isEmpty()) {
            switch (timeRange.toLowerCase()) {
                case "today":
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    queryWrapper.ge("publishTime", calendar.getTime());
                    break;
                case "week":
                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    queryWrapper.ge("publishTime", calendar.getTime());
                    break;
                case "month":
                    calendar.add(Calendar.MONTH, -1);
                    queryWrapper.ge("publishTime", calendar.getTime());
                    break;
                default:
                    break;
            }
        }
        
        // 只查询已发布的公告
        queryWrapper.eq("status", NoticeStatusEnum.PUBLISHED.getCode());
        
        // 排除已删除的记录
        queryWrapper.eq("isDelete", 0);
        
        // 按发布时间和置顶排序
        queryWrapper.orderByDesc("isTop");
        queryWrapper.orderByDesc("publishTime");
        
        // 分页处理
        int pageNum = current != null ? current : 1;
        int size = pageSize != null ? pageSize : 10;
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Notice> page =
                this.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, size), queryWrapper);
        
        // 转换为VO列表
        return page.getRecords().stream()
                .map(this::noticeToVO)
                .collect(Collectors.toList());
    }
}