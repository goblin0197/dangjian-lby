package com.coder.springbootinit.controller;

import com.coder.springbootinit.annotation.AuthCheck;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.constant.UserConstant;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.entity.AnnouncementAttachment;
import com.coder.springbootinit.model.entity.AnnouncementReadRecord;
import com.coder.springbootinit.model.entity.AnnouncementShare;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.service.AnnouncementAttachmentService;
import com.coder.springbootinit.service.AnnouncementReadRecordService;
import com.coder.springbootinit.service.AnnouncementShareService;
import com.coder.springbootinit.service.NoticeService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 公告扩展控制器
 *
 */
@RestController
@RequestMapping("/announcement")
@Api(tags = "公告扩展功能")
@ApiOperation(value = "公告扩展功能")
@Slf4j
public class AnnouncementExtensionController {

    @Resource
    private AnnouncementAttachmentService announcementAttachmentService;

    @Resource
    private AnnouncementReadRecordService announcementReadRecordService;

    @Resource
    private AnnouncementShareService announcementShareService;

    @Resource
    private UserService userService;
    
    @Resource
    private NoticeService noticeService;

    // region 附件管理

    /**
     * 获取公告附件列表
     */
    @GetMapping("/attachment/list")
    @ApiOperation(value = "获取公告附件列表")
    public BaseResponse<List<AnnouncementAttachment>> listAttachment(@RequestParam Long announcementId) {
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<AnnouncementAttachment> list = announcementAttachmentService.getByAnnouncementId(announcementId);
        return ResultUtils.success(list);
    }

    /**
     * 上传公告附件
     */
    @PostMapping("/upload/notice/attachment")
    @ApiOperation(value = "上传公告附件")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Long> uploadAnnouncementAttachment(
            @RequestPart("file") MultipartFile multipartFile,
            @RequestParam Long announcementId,
            HttpServletRequest request) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "附件文件不能为空");
        }
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        
        User loginUser = userService.getLoginUser(request);
        
        // 校验文件类型（支持常见文档格式）
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".doc") 
                && !originalFilename.toLowerCase().endsWith(".docx") 
                && !originalFilename.toLowerCase().endsWith(".pdf")
                && !originalFilename.toLowerCase().endsWith(".xls")
                && !originalFilename.toLowerCase().endsWith(".xlsx")
                && !originalFilename.toLowerCase().endsWith(".ppt")
                && !originalFilename.toLowerCase().endsWith(".pptx")
                && !originalFilename.toLowerCase().endsWith(".txt")
                && !originalFilename.toLowerCase().endsWith(".zip")
                && !originalFilename.toLowerCase().endsWith(".rar"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持文档格式的附件文件");
        }
        
        try {
            // 生成随机文件名
            String fileSuffix = "";
            if (originalFilename.contains(".")) {
                fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String randomFileName = System.currentTimeMillis() + "_" + loginUser.getId() + fileSuffix;
            
            // 构建存储路径
            Path staticPath = Paths.get(System.getProperty("user.dir"), "static", "notice", "attachment");
            java.io.File uploadDir = staticPath.toFile();
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            java.io.File destFile = new java.io.File(uploadDir, randomFileName);
            multipartFile.transferTo(destFile);
            
            // 创建附件记录
            AnnouncementAttachment attachment = new AnnouncementAttachment();
            attachment.setAnnouncementId(announcementId);
            attachment.setName(originalFilename);
            attachment.setUrl("/notice/attachment/" + randomFileName);
            attachment.setSize(formatFileSize(multipartFile.getSize()));
            attachment.setType(fileSuffix.replace(".", ""));
            
            boolean result = announcementAttachmentService.save(attachment);
            if (!result) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
            }
            
            log.info("上传公告附件成功，attachmentId: {}, 公告ID: {}", attachment.getId(), announcementId);
            return ResultUtils.success(attachment.getId());
        } catch (Exception e) {
            log.error("上传公告附件失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传富文本编辑器图片
     */
    @PostMapping("/upload/notice/image")
    @ApiOperation(value = "上传富文本编辑器图片")
    public BaseResponse<String> uploadAnnouncementImage(
            @RequestPart("file") MultipartFile multipartFile,
            HttpServletRequest request) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图片文件不能为空");
        }
        
        User loginUser = userService.getLoginUser(request);
        
        // 校验文件类型（支持图片格式）
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".jpg") 
                && !originalFilename.toLowerCase().endsWith(".jpeg") 
                && !originalFilename.toLowerCase().endsWith(".png")
                && !originalFilename.toLowerCase().endsWith(".gif")
                && !originalFilename.toLowerCase().endsWith(".bmp"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持图片格式文件");
        }
        
        try {
            // 生成随机文件名
            String fileSuffix = "";
            if (originalFilename.contains(".")) {
                fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String randomFileName = System.currentTimeMillis() + "_" + loginUser.getId() + fileSuffix;
            
            // 构建存储路径
            Path staticPath = Paths.get(System.getProperty("user.dir"), "static", "notice", "image");
            java.io.File uploadDir = staticPath.toFile();
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            java.io.File destFile = new java.io.File(uploadDir, randomFileName);
            multipartFile.transferTo(destFile);
            
            log.info("上传公告图片成功，用户ID: {}", loginUser.getId());
            return ResultUtils.success("/notice/image/" + randomFileName);
        } catch (Exception e) {
            log.error("上传公告图片失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载公告附件
     */
    @GetMapping("/attachment/download")
    @ApiOperation(value = "下载公告附件")
    public void downloadAnnouncementAttachment(@RequestParam Long id, HttpServletResponse response) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "附件ID不能为空");
        }
        
        AnnouncementAttachment attachment = announcementAttachmentService.getById(id);
        if (attachment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "附件不存在");
        }
        
        try {
            // 构建文件路径
            Path staticPath = Paths.get(System.getProperty("user.dir"), "static");
            String fileUrl = attachment.getUrl();
            
            // 确保pathAfterStatic不以斜杠开头
            if (fileUrl.startsWith("/") || fileUrl.startsWith("\\")) {
                fileUrl = fileUrl.substring(1);
            }
            
            // 构建绝对路径
            Path absolutePath = staticPath.resolve(fileUrl.replace("/", java.io.File.separator));
            java.io.File file = absolutePath.toFile();

            // 检查文件是否存在
            if (!file.exists()) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文件不存在");
            }

            // 设置响应头
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            String fileName = URLEncoder.encode(attachment.getName(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLengthLong(file.length());
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

            // 使用hutool读取文件字节数组并写入响应
            cn.hutool.core.io.FileUtil.writeToStream(file, response.getOutputStream());
            
            response.flushBuffer();
            log.info("下载公告附件成功，attachmentId: {}, 文件名: {}", id, attachment.getName());
        } catch (Exception e) {
            log.error("下载公告附件失败，attachmentId: {}", id, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败: " + e.getMessage());
        }
    }

    /**
     * 预览公告附件
     */
    @GetMapping("/attachment/preview")
    @ApiOperation(value = "预览公告附件")
    public void previewAnnouncementAttachment(@RequestParam Long id, HttpServletResponse response) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "附件ID不能为空");
        }
        
        AnnouncementAttachment attachment = announcementAttachmentService.getById(id);
        if (attachment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "附件不存在");
        }
        
        try {
            // 构建文件路径
            Path staticPath = Paths.get(System.getProperty("user.dir"), "static");
            String fileUrl = attachment.getUrl();
            
            // 确保pathAfterStatic不以斜杠开头
            if (fileUrl.startsWith("/") || fileUrl.startsWith("\\")) {
                fileUrl = fileUrl.substring(1);
            }
            
            // 构建绝对路径
            Path absolutePath = staticPath.resolve(fileUrl.replace("/", java.io.File.separator));
            java.io.File file = absolutePath.toFile();

            // 检查文件是否存在
            if (!file.exists()) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文件不存在");
            }

            // 根据文件类型设置Content-Type
            String contentType = getContentType(attachment.getType());
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode(attachment.getName(), "UTF-8");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);

            // 使用hutool读取文件字节数组并写入响应
            cn.hutool.core.io.FileUtil.writeToStream(file, response.getOutputStream());
            
            response.flushBuffer();
            log.info("预览公告附件成功，attachmentId: {}, 文件名: {}", id, attachment.getName());
        } catch (Exception e) {
            log.error("预览公告附件失败，attachmentId: {}", id, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "预览失败: " + e.getMessage());
        }
    }

    /**
     * 添加公告附件
     */
    @PostMapping("/attachment/add")
    @ApiOperation(value = "添加公告附件")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Long> addAttachment(@RequestBody AnnouncementAttachment attachment) {
        if (attachment == null || attachment.getAnnouncementId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = announcementAttachmentService.save(attachment);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(attachment.getId());
    }

    /**
     * 删除公告附件
     */
    @DeleteMapping("/attachment/delete/{id}")
    @ApiOperation(value = "删除公告附件")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<Boolean> deleteAttachment(@PathVariable Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = announcementAttachmentService.removeById(id);
        return ResultUtils.success(result);
    }

    // endregion

    // region 阅读记录

    /**
     * 获取未读公告数量
     */
    @GetMapping("/unreadCount")
    @ApiOperation(value = "获取未读公告数量")
    public BaseResponse<Long> getUnreadCount(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long count = announcementReadRecordService.getUnreadCount(loginUser.getId());
        return ResultUtils.success(count);
    }

    /**
     * 标记公告为已读
     */
    @PostMapping("/markRead/{announcementId}")
    @ApiOperation(value = "标记公告为已读")
    public BaseResponse<Boolean> markAsRead(@PathVariable Long announcementId, HttpServletRequest request) {
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = announcementReadRecordService.markAsRead(announcementId, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 获取用户已读公告列表
     */
    @GetMapping("/readList")
    @ApiOperation(value = "获取用户已读公告列表")
    public BaseResponse<List<AnnouncementReadRecord>> getReadList(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<AnnouncementReadRecord> list = announcementReadRecordService.getByUserId(loginUser.getId());
        return ResultUtils.success(list);
    }

    /**
     * 增加公告阅读量
     *
     * @param announcementId 公告ID
     * @return 是否成功
     */
    @PutMapping("/incrementReadCount")
    @ApiOperation(value = "增加公告阅读量")
    public BaseResponse<Boolean> incrementReadCount(@RequestParam Long announcementId) {
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        boolean result = noticeService.incrementReadCount(announcementId);
        return ResultUtils.success(result);
    }

    // endregion

    // region 分享管理

    /**
     * 生成分享链接
     */
    @PostMapping("/share/generate")
    @ApiOperation(value = "生成公告分享链接")
    @AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})
    public BaseResponse<AnnouncementShare> generateShare(
            @RequestParam Long announcementId,
            @RequestParam(required = false) String expireTime,
            HttpServletRequest request) {
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        AnnouncementShare share = announcementShareService.generateShare(announcementId, loginUser.getId(), loginUser.getUserName(), expireTime);
        return ResultUtils.success(share);
    }

    /**
     * 访问分享链接
     */
    @GetMapping("/share/access")
    @ApiOperation(value = "访问分享链接")
    public BaseResponse<AnnouncementShare> accessShare(@RequestParam String shareCode) {
        if (shareCode == null || shareCode.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AnnouncementShare share = announcementShareService.accessShare(shareCode);
        return ResultUtils.success(share);
    }

    /**
     * 获取公告分享记录列表
     */
    @GetMapping("/share/list")
    @ApiOperation(value = "获取公告分享记录列表")
    public BaseResponse<List<AnnouncementShare>> getAnnouncementShareList(@RequestParam Long announcementId) {
        if (announcementId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公告ID不能为空");
        }
        List<AnnouncementShare> list = announcementShareService.getShareListByAnnouncementId(announcementId);
        return ResultUtils.success(list);
    }

    /**
     * 获取公告时间线列表
     */
    @GetMapping("/timeline")
    @ApiOperation(value = "获取公告时间线列表")
    public BaseResponse<List<com.coder.springbootinit.model.vo.NoticeVO>> getAnnouncementTimeline(
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        List<com.coder.springbootinit.model.vo.NoticeVO> list = noticeService.getTimelineList(timeRange, status, current, pageSize);
        return ResultUtils.success(list);
    }

    /**
     * 搜索时间线公告
     */
    @GetMapping("/timeline/search")
    @ApiOperation(value = "搜索时间线公告")
    public BaseResponse<List<com.coder.springbootinit.model.vo.NoticeVO>> searchAnnouncementTimeline(
            @RequestParam String keyword,
            @RequestParam(required = false) String timeRange,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        if (keyword == null || keyword.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "搜索关键词不能为空");
        }
        
        List<com.coder.springbootinit.model.vo.NoticeVO> list = noticeService.searchTimeline(keyword, timeRange, current, pageSize);
        return ResultUtils.success(list);
    }

    // endregion

    /**
     * 格式化文件大小
     *
     * @param size 文件大小（字节）
     * @return 格式化后的字符串
     */
    private String formatFileSize(Long size) {
        if (size == null) {
            return "0 B";
        }
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 根据文件类型获取Content-Type
     *
     * @param fileType 文件类型
     * @return Content-Type
     */
    private String getContentType(String fileType) {
        if (fileType == null) {
            return "application/octet-stream";
        }
        switch (fileType.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }
}
