import request from "@/request";

/** 添加材料模板 POST /api/material/template/add */
export async function addMaterialTemplateUsingPost(
    body: API.MaterialTemplateAddRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseLong_>("/api/material/template/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 批量删除材料模板 DELETE /api/material/template/batchDelete */
export async function batchDeleteMaterialTemplateUsingDelete(
    body: API.MaterialTemplateBatchRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>(
        "/api/material/template/batchDelete",
        {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            data: body,
            ...(options || {}),
        },
    );
}

/** 批量切换模板状态 PUT /api/material/template/batchToggleStatus */
export async function batchToggleMaterialTemplateStatusUsingPut(
    body: API.MaterialTemplateBatchRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>(
        "/api/material/template/batchToggleStatus",
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            data: body,
            ...(options || {}),
        },
    );
}

/** 删除材料模板 DELETE /api/material/template/delete */
export async function deleteMaterialTemplateUsingDelete(
    body: API.DeleteRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>(
        "/api/material/template/delete",
        {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            data: body,
            ...(options || {}),
        },
    );
}

/** 下载模板文件 GET /api/material/template/download */
export async function downloadMaterialTemplateUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.downloadMaterialTemplateUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseString_>(
        "/api/material/template/download",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 获取材料模板列表 GET /api/material/template/list */
export async function listMaterialTemplateUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.listMaterialTemplateUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListMaterialTemplateVO_>(
        "/api/material/template/list",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 预览模板文件 GET /api/material/template/preview */
export async function previewMaterialTemplateUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.previewMaterialTemplateUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseString_>(
        "/api/material/template/preview",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 切换模板状态 PUT /api/material/template/toggleStatus */
export async function toggleMaterialTemplateStatusUsingPut(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.toggleMaterialTemplateStatusUsingPUTParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>(
        "/api/material/template/toggleStatus",
        {
            method: "PUT",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 更新材料模板 PUT /api/material/template/update */
export async function updateMaterialTemplateUsingPut(
    body: API.MaterialTemplateUpdateRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>(
        "/api/material/template/update",
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            data: body,
            ...(options || {}),
        },
    );
}
