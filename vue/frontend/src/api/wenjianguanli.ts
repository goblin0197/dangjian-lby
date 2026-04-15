import request from "@/request";

/** 上传材料文件 POST /api/file/api/upload/material */
export async function uploadMaterialFileUsingPost(
    body: {},
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

    return request<API.BaseResponseString_>("/api/file/api/upload/material", {
        method: "POST",
        data: formData,
        ...(options || {}),
    });
}

/** 上传模板文件 POST /api/file/api/upload/template */
export async function uploadTemplateFileUsingPost(
    body: {},
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

    return request<API.BaseResponseString_>("/api/file/api/upload/template", {
        method: "POST",
        data: formData,
        ...(options || {}),
    });
}

/** 文件删除 POST /api/file/delete */
export async function deleteFileUsingPost(
    body: API.DeleteRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/file/delete", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 文件下载 GET /api/file/download */
export async function downloadFileUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.downloadFileUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<any>("/api/file/download", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 材料文件列表(没有校验权限) GET /api/file/material/list */
export async function getMaterialListUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getMaterialListUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListMyFile_>("/api/file/material/list", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 模板文件列表(没有校验权限) GET /api/file/template/list */
export async function getTemplateListUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getTemplateListUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListMyFile_>("/api/file/template/list", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 文件上传 POST /api/file/upload */
export async function uploadFileUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.uploadFileUsingPOSTParams,
    body: {},
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

    return request<API.BaseResponseString_>("/api/file/upload", {
        method: "POST",
        params: {
            ...params,
        },
        data: formData,
        ...(options || {}),
    });
}
