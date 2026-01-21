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
}