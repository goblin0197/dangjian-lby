package com.coder.springbootinit.service;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coder.springbootinit.model.entity.MyFile;
import com.coder.springbootinit.model.enums.FileUploadBizEnum;

/**
 * 文件表 Service 接口
 */
public interface FileService extends IService<MyFile> {
    /**
     * 校验文件
     *
     * @param multipartFile  multipart文件
     * @param fileUploadBizEnum  文件上传业务类型
     */
    void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum);

    /**
     * 获取文件相对路径
     *
     * @param fileUploadBizEnum  文件上传业务类型
     * @param userId 用户id
     * @param filename 文件名
     * @return 相对路径
     */
    String getRelativePath(FileUploadBizEnum fileUploadBizEnum,long userId,String filename);
}
