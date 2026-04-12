package com.coder.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.notice.NoticeAddRequest;
import com.coder.springbootinit.model.dto.notice.NoticeQueryRequest;
import com.coder.springbootinit.model.dto.notice.NoticeUpdateRequest;
import com.coder.springbootinit.model.entity.Notice;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.vo.NoticeVO;
import com.coder.springbootinit.service.NoticeService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 公告控制器
 *
 */
@RestController
@RequestMapping("/notice")
@Api(tags = "公告管理")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private UserService userService;

    /**
     * 添加公告
     *
     * @param noticeAddRequest 公告添加请求
     * @param request          HttpServletRequest
     * @return 公告ID
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> addNotice(@RequestBody NoticeAddRequest noticeAddRequest, HttpServletRequest request) {
        if (noticeAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 添加公告
        noticeService.addNotice(noticeAddRequest, loginUser.getId());
        return ResultUtils.success(true);
    }

    /**
     * 更新公告
     *
     * @param noticeUpdateRequest 公告更新请求
     * @param request             HttpServletRequest
     * @return 是否成功
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> updateNotice(@RequestBody NoticeUpdateRequest noticeUpdateRequest, HttpServletRequest request) {
        if (noticeUpdateRequest == null || noticeUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 更新公告
        boolean result = noticeService.updateNotice(noticeUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 删除公告
     *
     * @param deleteRequest 删除请求
     * @param request       HttpServletRequest
     * @return 是否成功
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> deleteNotice(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 删除公告
        boolean result = noticeService.deleteNotice(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 获取公告详情
     *
     * @param id 公告ID
     * @return 公告VO
     */
    @GetMapping("/get")
    @ApiOperation(value = "获取公告详情")
    public BaseResponse<NoticeVO> getNoticeVO(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取公告详情
        NoticeVO noticeVO = noticeService.getNoticeVO(id);
        return ResultUtils.success(noticeVO);
    }

    /**
     * 分页获取公告列表
     *
     * @param noticeQueryRequest 公告查询请求
     * @return 公告VO列表
     */
    @GetMapping("/list/page")
    @ApiOperation(value = "分页获取公告列表")
    public BaseResponse<List<NoticeVO>> listNoticeVOByPage(NoticeQueryRequest noticeQueryRequest) {
        if (noticeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 分页获取公告列表
        List<NoticeVO> noticeVOList = noticeService.listNoticeVO(noticeQueryRequest);
        return ResultUtils.success(noticeVOList);
    }

    /**
     * 发布公告
     *
     * @param id      公告ID
     * @param request HttpServletRequest
     * @return 是否成功
     */
    @PostMapping("/publish")
    @ApiOperation(value = "发布公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> publishNotice(@RequestParam Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 发布公告
        boolean result = noticeService.publishNotice(id);
        return ResultUtils.success(result);
    }

    /**
     * 撤回公告
     *
     * @param id      公告ID
     * @param request HttpServletRequest
     * @return 是否成功
     */
    @PostMapping("/withdraw")
    @ApiOperation(value = "撤回公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> withdrawNotice(@RequestParam Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 撤回公告
        boolean result = noticeService.withdrawNotice(id);
        return ResultUtils.success(result);
    }

    /**
     * 批量删除公告
     *
     * @param ids 公告ID列表
     * @param request HttpServletRequest
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    @ApiOperation(value = "批量删除公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> batchDeleteNotices(@RequestBody List<Long> ids, HttpServletRequest request) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID列表不能为空");
        }
        boolean result = noticeService.batchDeleteNotices(ids);
        return ResultUtils.success(result);
    }

    /**
     * 下架公告
     *
     * @param id 公告ID
     * @return 是否成功
     */
    @PutMapping("/offline")
    @ApiOperation(value = "下架公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> offlineNotice(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        boolean result = noticeService.offlineNotice(id);
        return ResultUtils.success(result);
    }

    /**
     * 重新发布公告
     *
     * @param id 公告ID
     * @return 是否成功
     */
    @PutMapping("/republish")
    @ApiOperation(value = "重新发布公告")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> republishNotice(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        boolean result = noticeService.republishNotice(id);
        return ResultUtils.success(result);
    }

    /**
     * 设置公告置顶状态
     *
     * @param id 公告ID
     * @param isTop 是否置顶（0-否，1-是）
     * @return 是否成功
     */
    @PutMapping("/top")
    @ApiOperation(value = "设置公告置顶状态")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> topNotice(@RequestParam Long id, @RequestParam Integer isTop) {
        if (id == null || isTop == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        boolean result = noticeService.topNotice(id, isTop);
        return ResultUtils.success(result);
    }

    /**
     * 标记公告为已读
     *
     * @param announcementId 公告 ID
     * @param request HttpServletRequest
     * @return 是否成功
     */
    @PostMapping("/markRead")
    @ApiOperation(value = "标记公告为已读")
    public BaseResponse<Boolean> markAnnouncementAsRead(@RequestParam Long announcementId, HttpServletRequest request) {
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = noticeService.markAsRead(announcementId, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 获取未读公告数量
     *
     * @param request HttpServletRequest
     * @return 未读公告数量
     */
    @GetMapping("/unreadCount")
    @ApiOperation(value = "获取未读公告数量")
    public BaseResponse<Integer> getUnreadCount(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Integer count = noticeService.getUnreadCount(loginUser.getId());
        return ResultUtils.success(count);
    }
}