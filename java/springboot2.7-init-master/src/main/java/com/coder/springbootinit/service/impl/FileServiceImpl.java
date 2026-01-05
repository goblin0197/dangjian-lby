package com.coder.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.exception.BusinessException;
import com.coder.springbootinit.mapper.FileMapper;
import com.coder.springbootinit.model.entity.MyFile;
import com.coder.springbootinit.model.enums.FileUploadBizEnum;
import com.coder.springbootinit.service.FileService;

import cn.hutool.core.io.FileUtil;

import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件表 Service 实现类
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, MyFile> implements FileService {
        /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    @Override
    public void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        final long FIFTY_M = 50 * ONE_M;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        } else {
            if (fileSize > FIFTY_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 50M");
            }
            if (!Arrays.asList("doc", "docx", "pdf", "txt", "ppt", "pptx", "xls", "xlsx", "zip", "rar", "jpeg", "jpg", "png", "svg", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误，支持doc/docx/pdf/txt/ppt/pptx/xls/xlsx/zip/rar/jpg/png/svg/webp");
            }
        }
    }

    /**
     * 获取相对路径
     * @param fileUploadBizEnum
     * @param userId
     * @param filename
     * @return
     */
    @Override
    public String getRelativePath(FileUploadBizEnum fileUploadBizEnum,long userId,String filename){
        // 模板文件的路径中不需要用户id
        if(FileUploadBizEnum.TEMPLATE.equals(fileUploadBizEnum)){
            return String.format("/%s/%s", fileUploadBizEnum.getValue(), filename);
        }
        return String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), userId, filename);
    }
}
