package com.coder.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.dto.notice.NoticeAddRequest;
import com.coder.springbootinit.model.dto.notice.NoticeQueryRequest;
import com.coder.springbootinit.model.dto.notice.NoticeUpdateRequest;
import com.coder.springbootinit.model.entity.Notice;
import com.coder.springbootinit.model.vo.NoticeVO;

import java.util.List;

/**
 * 公告服务
 *
*/
public interface NoticeService extends IService<Notice> {

    /**
     * 添加公告
     * @param noticeAddRequest 公告添加请求
     * @param publisherId 发布人ID
     * @return 公告
     */
    Notice addNotice(NoticeAddRequest noticeAddRequest, Long publisherId);

    /**
     * 更新公告
     * @param noticeUpdateRequest 公告更新请求
     * @return 是否成功
     */
    boolean updateNotice(NoticeUpdateRequest noticeUpdateRequest);

    /**
     * 删除公告
     * @param id 公告ID
     * @return 是否成功
     */
    boolean deleteNotice(Long id);

    /**
     * 获取公告详情
     * @param id 公告ID
     * @return 公告VO
     */
    NoticeVO getNoticeVO(Long id);

    /**
     * 获取公告列表
     * @param noticeQueryRequest 公告查询请求
     * @return 公告VO列表
     */
    List<NoticeVO> listNoticeVO(NoticeQueryRequest noticeQueryRequest);

    /**
     * 发布公告
     * @param id 公告ID
     * @return 是否成功
     */
    boolean publishNotice(Long id);

    /**
     * 撤回公告
     * @param id 公告ID
     * @return 是否成功
     */
    boolean withdrawNotice(Long id);

    /**
     * 标记公告为已读
     * @param announcementId 公告 ID
     * @param userId 用户 ID
     * @return 是否成功
     */
    boolean markAsRead(Long announcementId, Long userId);

    /**
     * 获取未读公告数量
     * @param userId 用户 ID
     * @return 未读公告数量
     */
    Integer getUnreadCount(Long userId);

    /**
     * 获取查询条件
     * @param noticeQueryRequest 公告查询请求
     * @return 查询条件
     */
    QueryWrapper<Notice> getQueryWrapper(NoticeQueryRequest noticeQueryRequest);

    /**
     * 批量删除公告
     * @param ids 公告ID列表
     * @return 是否成功
     */
    boolean batchDeleteNotices(List<Long> ids);

    /**
     * 下架公告（将已发布公告设置为下架状态）
     * @param id 公告ID
     * @return 是否成功
     */
    boolean offlineNotice(Long id);

    /**
     * 重新发布已下架的公告
     * @param id 公告ID
     * @return 是否成功
     */
    boolean republishNotice(Long id);

    /**
     * 设置公告置顶状态
     * @param id 公告ID
     * @param isTop 是否置顶（0-否，1-是）
     * @return 是否成功
     */
    boolean topNotice(Long id, Integer isTop);

    /**
     * 增加公告阅读量
     * @param announcementId 公告ID
     * @return 是否成功
     */
    boolean incrementReadCount(Long announcementId);

    /**
     * 获取公告时间线列表
     * @param timeRange 时间范围（如：today、week、month）
     * @param status 公告状态
     * @param current 页码
     * @param pageSize 每页数量
     * @return 公告VO列表
     */
    List<com.coder.springbootinit.model.vo.NoticeVO> getTimelineList(String timeRange, Integer status, Integer current, Integer pageSize);

    /**
     * 搜索时间线公告
     * @param keyword 搜索关键词
     * @param timeRange 时间范围
     * @param current 页码
     * @param pageSize 每页数量
     * @return 公告VO列表
     */
    List<com.coder.springbootinit.model.vo.NoticeVO> searchTimeline(String keyword, String timeRange, Integer current, Integer pageSize);
}