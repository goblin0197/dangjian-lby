package com.coder.springbootinit.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.DeleteRequest;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.model.dto.file.UploadFileRequest;
import com.coder.springbootinit.model.entity.MyFile;
import com.coder.springbootinit.model.entity.User;
import com.coder.springbootinit.model.enums.FileUploadBizEnum;
import com.coder.springbootinit.service.DevelopmentStageService;
import com.coder.springbootinit.service.FileService;
import com.coder.springbootinit.service.OrganizationService;
import com.coder.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 文件接口
 *
*/
@RestController
@RequestMapping("/file")
@Slf4j
@Api(tags = "文件管理")
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private FileService fileService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private DevelopmentStageService developmentStageService;

    // 本地文件存储路径
    private static final Path STATIC_PATH = Paths.get(System.getProperty("user.dir"), "static");
    // 文件访问前缀
    // private static final String FILE_ACCESS_PREFIX = "/static";

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
            UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        // 获取上传文件的业务类型
        String biz = uploadFileRequest.getBiz();
        Long orgId = uploadFileRequest.getOrgId();
        Long stageId = uploadFileRequest.getStageId();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(FileUploadBizEnum.TEMPLATE.equals(fileUploadBizEnum)){
            // 只有管理员(超管和组织管理员)可以上传模板
            if(!userService.isAdmin(request)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 校验文件
        fileService.validFile(multipartFile, fileUploadBizEnum);
        MyFile myFile = new MyFile();
        User loginUser = userService.getLoginUser(request);
        // 获取文件后缀
        String originalFilename = multipartFile.getOriginalFilename();
        String fileSuffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 生成随机文件名（hutool工具类生成 + 文件后缀）
        String randomFileName = RandomUtil.randomString(5) + IdUtil.getSnowflakeNextIdStr() + fileSuffix;
        myFile.setOriginFileName(originalFilename);
        myFile.setFileName(randomFileName);
        myFile.setUserId(loginUser.getId());
        myFile.setIsTemplate(FileUploadBizEnum.TEMPLATE.equals(fileUploadBizEnum) ? 1 : 0);
        myFile.setFileSize((int) multipartFile.getSize());
        if(organizationService.getById(orgId) != null){
            myFile.setOrgId(orgId);
        }
        if(developmentStageService.getById(stageId) != null){
            myFile.setStageId(stageId);
        }
        // 构建相对路径
        String relativePath = fileService.getRelativePath(fileUploadBizEnum, loginUser.getId(), randomFileName);
        myFile.setFileUrl(relativePath);
        // 确保relativePath不以斜杠开头，否则resolve会忽略当前路径
        String cleanRelativePath = relativePath;
        if (cleanRelativePath.startsWith("/") || cleanRelativePath.startsWith("\\") ) {
            cleanRelativePath = cleanRelativePath.substring(1);
        }
        // 构建绝对路径
        Path absolutePath = STATIC_PATH.resolve(cleanRelativePath.replace("/", File.separator));
        File file = null;
        try {
            // 创建目录
            File parentDir = absolutePath.getParent().toFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            // 上传文件
            file = absolutePath.toFile();
            multipartFile.transferTo(file);
            // 返回可访问地址
            String fileUrl = UriComponentsBuilder.fromUriString("")
                    .replacePath("")
                    .path(relativePath)
                    .toUriString();
            boolean result = fileService.save(myFile);
            if(!result){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
            }
            return ResultUtils.success(fileUrl);
        } catch (Exception e) {
            log.error("file upload error, absolutePath = " + absolutePath, e);
            if(file != null){
                file.delete();
            }
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
    }

    /**
     * 上传模板文件
     *
     * @param multipartFile 模板文件（Word/PDF）
     * @param request HTTP请求
     * @return 文件URL
     */
    @PostMapping("/api/upload/template")
    @ApiOperation("上传模板文件")
    public BaseResponse<String> uploadTemplateFile(@RequestPart("file") MultipartFile multipartFile,
            HttpServletRequest request) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板文件不能为空");
        }
        // 只有管理员可以上传模板
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限上传模板");
        }
        // 校验文件类型
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".doc") 
                && !originalFilename.toLowerCase().endsWith(".docx") 
                && !originalFilename.toLowerCase().endsWith(".pdf"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持Word或PDF格式的模板文件");
        }
        // 构建上传请求
        UploadFileRequest uploadRequest = new UploadFileRequest();
        uploadRequest.setBiz(FileUploadBizEnum.TEMPLATE.getValue());
        return uploadFile(multipartFile, uploadRequest, request);
    }

    /**
     * 上传材料文件
     *
     * @param multipartFile 材料文件（Word/PDF/图片）
     * @param request HTTP请求
     * @return 文件URL
     */
    @PostMapping("/api/upload/material")
    @ApiOperation("上传材料文件")
    public BaseResponse<String> uploadMaterialFile(@RequestPart("file") MultipartFile multipartFile,
            HttpServletRequest request) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "材料文件不能为空");
        }
        // 校验文件类型
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".doc") 
                && !originalFilename.toLowerCase().endsWith(".docx") 
                && !originalFilename.toLowerCase().endsWith(".pdf")
                && !originalFilename.toLowerCase().endsWith(".jpg")
                && !originalFilename.toLowerCase().endsWith(".jpeg")
                && !originalFilename.toLowerCase().endsWith(".png"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持Word、PDF或图片格式的材料文件");
        }
        // 构建上传请求
        UploadFileRequest uploadRequest = new UploadFileRequest();
        uploadRequest.setBiz(FileUploadBizEnum.MATERIAL.getValue());
        return uploadFile(multipartFile, uploadRequest, request);
    }

    /**
     * 文件下载
     *
     * @param fileUrl 文件URL:材料:/material/{userId}/{filename}，模板：/template/{filename}
     * @param response HTTP响应
     */
    @GetMapping("/download")
    @ApiOperation("文件下载")
    public void downloadFile(@ApiParam(value = "文件URL:材料:/material/{userId}/{filename}，模板：/template/{filename}", required = true) 
        @RequestParam String fileUrl, HttpServletResponse response) {
        try {
            // 构建文件路径
            Path absolutePath;
            if (fileUrl.startsWith("/material") || fileUrl.startsWith("/template")) {
                // 确保pathAfterStatic不以斜杠开头，否则resolve会忽略当前路径
                if (fileUrl.startsWith("/") || fileUrl.startsWith("\\")) {
                    fileUrl = fileUrl.substring(1);
                }
                // 构建绝对路径，使用Path类自动处理路径分隔符
                absolutePath = STATIC_PATH.resolve(fileUrl.replace("/", File.separator));
            } else {
                // 无效的文件URL
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的文件URL");
            }
            
            log.info("downloading file from: {}", absolutePath);

            // 检查文件是否存在
            File file = absolutePath.toFile();
            if (!file.exists()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不存在: " + absolutePath);
            }
            if (!file.isFile()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "路径不是文件: " + absolutePath);
            }

            // 设置响应头，直接使用文件系统中的文件名作为下载文件名
            // 注意：当前接口设计下无法直接获取原始文件名，如需获取原始文件名
            // 建议修改接口，添加fileId参数，通过数据库查询获取原始文件名
            String fileName = file.getName();
//            // 清理响应头，避免可能的冲突
            response.reset();
//            // 设置响应头，支持中文文件名
            response.setCharacterEncoding("UTF-8");
//            // 使用更兼容的Content-Disposition格式
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            // 强制使用二进制流，避免MIME类型问题
            response.setContentType("application/octet-stream");
//            // 设置正确的Content-Length
           response.setContentLengthLong(file.length());
//            // 禁用缓存，确保每次下载都是最新版本
           response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
           response.setHeader("Pragma", "no-cache");
           response.setDateHeader("Expires", 0);

            // 使用hutool读取文件字节数组
           byte[] fileBytes = FileUtil.readBytes(file);
           // 使用标准的OutputStream.write方法写入响应
           try (OutputStream outputStream = response.getOutputStream()) {
               outputStream.write(fileBytes);
               outputStream.flush();
               outputStream.close();
           }
            // 确保响应被刷新
            response.flushBuffer();
        } catch (Exception e) {
            log.error("file download error, fileUrl = " + fileUrl, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败: " + e.getMessage());
        }
    }




    @PostMapping("/delete")
    @ApiOperation("文件删除")
    public BaseResponse<Boolean> deleteFile(@RequestBody DeleteRequest deleteRequest , HttpServletRequest request) {
        Long id = deleteRequest.getId();
        if(id == null || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MyFile file = fileService.getById(id);
        if(file == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文件不存在");
        }
        // 除了管理员(超管和组织管理员)之外的角色只能删除自己上传的文件
        if(!userService.isAdmin(request)){
            User loginUser = userService.getLoginUser(request);
            if(!file.getUserId().equals(loginUser.getId())){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        boolean result = fileService.removeById(id);
        return ResultUtils.success(result);
    }



    /**
     * 所有人可以看到模板文件列表，根据党组织分组
     */
    @GetMapping("/template/list")
    @ApiOperation("模板文件列表(没有校验权限)")
    public BaseResponse<List<MyFile>> getTemplateList(@RequestParam Long orgId) {
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isTemplate", 1);
        if(orgId != null){
            queryWrapper.eq("orgId", orgId);
        }
        List<MyFile> fileList = fileService.list(queryWrapper);
        return ResultUtils.success(fileList);
    }



    /**
     * 培养人和被培养人可以看到材料列表
     * 根据userId查看材料文件
     */
    @GetMapping("/material/list")
    @ApiOperation("材料文件列表(没有校验权限)")
    public BaseResponse<List<MyFile>> getMaterialList(@RequestParam Long userId) {
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isTemplate", 0);
        queryWrapper.eq("userId", userId);
        List<MyFile> fileList = fileService.list(queryWrapper);
        return ResultUtils.success(fileList);
    }

    @GetMapping("/getFileInfo/byUrl")
    public BaseResponse<MyFile> getFileInfoByUrl(@RequestParam String fileUrl) {
        MyFile file = fileService.getOne(new QueryWrapper<MyFile>().eq("fileUrl", fileUrl));
        return ResultUtils.success(file);
    }
    
}