import request from "@/request";

/** 添加公告附件 POST /api/announcement/attachment/add */
export async function addAttachmentUsingPost(
    body: API.AnnouncementAttachment,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseLong_>("/api/announcement/attachment/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 删除公告附件 DELETE /api/announcement/attachment/delete/${param0} */
export async function deleteAttachmentUsingDelete(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.deleteAttachmentUsingDELETEParams,
    options?: { [key: string]: any },
) {
    const {id: param0, ...queryParams} = params;
    return request<API.BaseResponseBoolean_>(
        `/api/announcement/attachment/delete/${param0}`,
        {
            method: "DELETE",
            params: {...queryParams},
            ...(options || {}),
        },
    );
}

/** 下载公告附件 GET /api/announcement/attachment/download */
export async function downloadAnnouncementAttachmentUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.downloadAnnouncementAttachmentUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<any>("/api/announcement/attachment/download", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 获取公告附件列表 GET /api/announcement/attachment/list */
export async function listAttachmentUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.listAttachmentUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListAnnouncementAttachment_>(
        "/api/announcement/attachment/list",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 预览公告附件 GET /api/announcement/attachment/preview */
export async function previewAnnouncementAttachmentUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.previewAnnouncementAttachmentUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<any>("/api/announcement/attachment/preview", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 增加公告阅读量 PUT /api/announcement/incrementReadCount */
export async function incrementReadCountUsingPut(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.incrementReadCountUsingPUTParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>(
        "/api/announcement/incrementReadCount",
        {
            method: "PUT",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 标记公告为已读 POST /api/announcement/markRead/${param0} */
export async function markAsReadUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.markAsReadUsingPOSTParams,
    options?: { [key: string]: any },
) {
    const {announcementId: param0, ...queryParams} = params;
    return request<API.BaseResponseBoolean_>(
        `/api/announcement/markRead/${param0}`,
        {
            method: "POST",
            params: {...queryParams},
            ...(options || {}),
        },
    );
}

/** 获取用户已读公告列表 GET /api/announcement/readList */
export async function getReadListUsingGet(options?: { [key: string]: any }) {
    return request<API.BaseResponseListAnnouncementReadRecord_>(
        "/api/announcement/readList",
        {
            method: "GET",
            ...(options || {}),
        },
    );
}

/** 访问分享链接 GET /api/announcement/share/access */
export async function accessShareUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.accessShareUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseAnnouncementShare_>(
        "/api/announcement/share/access",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 生成公告分享链接 POST /api/announcement/share/generate */
export async function generateShareUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.generateShareUsingPOSTParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseAnnouncementShare_>(
        "/api/announcement/share/generate",
        {
            method: "POST",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 获取公告分享记录列表 GET /api/announcement/share/list */
export async function getAnnouncementShareListUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getAnnouncementShareListUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListAnnouncementShare_>(
        "/api/announcement/share/list",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 获取公告时间线列表 GET /api/announcement/timeline */
export async function getAnnouncementTimelineUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getAnnouncementTimelineUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListNoticeVO_>("/api/announcement/timeline", {
        method: "GET",
        params: {
            // current has a default value: 1
            current: "1",
            // pageSize has a default value: 10
            pageSize: "10",

            ...params,
        },
        ...(options || {}),
    });
}

/** 搜索时间线公告 GET /api/announcement/timeline/search */
export async function searchAnnouncementTimelineUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.searchAnnouncementTimelineUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListNoticeVO_>(
        "/api/announcement/timeline/search",
        {
            method: "GET",
            params: {
                // current has a default value: 1
                current: "1",

                // pageSize has a default value: 10
                pageSize: "10",
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 获取未读公告数量 GET /api/announcement/unreadCount */
export async function getUnreadCountUsingGet(options?: { [key: string]: any }) {
    return request<API.BaseResponseLong_>("/api/announcement/unreadCount", {
        method: "GET",
        ...(options || {}),
    });
}

/** 上传公告附件 POST /api/announcement/upload/notice/attachment */
export async function uploadAnnouncementAttachmentUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.uploadAnnouncementAttachmentUsingPOSTParams,
    body: Record<string, any>,
    file?: File,
    options?: { [key: string]: any },
) {
    const formData = new FormData();

    if (file) {
        formData.append("file", file);
    }

    Object.keys(body).forEach((ele) => {
        const item = (body as any)[ele];

        if (item !== undefined && item !== null) {
            if (typeof item === "object" && !(item instanceof File)) {
                if (item instanceof Array) {
                    item.forEach((f) => formData.append(ele, f || ""));
                } else {
                    formData.append(
                        ele,
                        new Blob([JSON.stringify(item)], {type: "application/json"}),
                    );
                }
            } else {
                formData.append(ele, item);
            }
        }
    });

    return request<API.BaseResponseLong_>(
        "/api/announcement/upload/notice/attachment",
        {
            method: "POST",
            params: {
                ...params,
            },
            data: formData,
            ...(options || {}),
        },
    );
}

/** 上传富文本编辑器图片 POST /api/announcement/upload/notice/image */
export async function uploadAnnouncementImageUsingPost(
    body: Record<string, any>,
    file?: File,
    options?: { [key: string]: any },
) {
    const formData = new FormData();

    if (file) {
        formData.append("file", file);
    }

    Object.keys(body).forEach((ele) => {
        const item = (body as any)[ele];

        if (item !== undefined && item !== null) {
            if (typeof item === "object" && !(item instanceof File)) {
                if (item instanceof Array) {
                    item.forEach((f) => formData.append(ele, f || ""));
                } else {
                    formData.append(
                        ele,
                        new Blob([JSON.stringify(item)], {type: "application/json"}),
                    );
                }
            } else {
                formData.append(ele, item);
            }
        }
    });

    return request<API.BaseResponseString_>(
        "/api/announcement/upload/notice/image",
        {
            method: "POST",
            data: formData,
            ...(options || {}),
        },
    );
}
