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
     * 获取查询条件
     * @param noticeQueryRequest 公告查询请求
     * @return 查询条件
     */
    QueryWrapper<Notice> getQueryWrapper(NoticeQueryRequest noticeQueryRequest);
}