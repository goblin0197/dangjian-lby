import request from "@/request";

/** 添加指标 POST /api/quantify/indicator/add */
export async function addIndicatorUsingPost(
    body: API.QuantifyIndicator,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseLong_>("/api/quantify/indicator/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 批量删除量化指标 POST /api/quantify/indicator/batchDelete */
export async function batchDeleteIndicatorsUsingPost(
    body: number[],
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>(
        "/api/quantify/indicator/batchDelete",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            data: body,
            ...(options || {}),
        },
    );
}

/** 删除指标 DELETE /api/quantify/indicator/delete/${param0} */
export async function deleteIndicatorUsingDelete(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.deleteIndicatorUsingDELETEParams,
    options?: { [key: string]: any },
) {
    const {id: param0, ...queryParams} = params;
    return request<API.BaseResponseBoolean_>(
        `/api/quantify/indicator/delete/${param0}`,
        {
            method: "DELETE",
            params: {...queryParams},
            ...(options || {}),
        },
    );
}

/** 导入量化指标模板 POST /api/quantify/indicator/import */
export async function importQuantifyIndicatorTemplateUsingPost(
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

    return request<API.BaseResponseString_>("/api/quantify/indicator/import", {
        method: "POST",
        data: formData,
        ...(options || {}),
    });
}

/** 获取指标列表 GET /api/quantify/indicator/list */
export async function listIndicatorUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.listIndicatorUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListQuantifyIndicator_>(
        "/api/quantify/indicator/list",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 切换指标状态 PUT /api/quantify/indicator/toggle/${param0} */
export async function toggleStatusUsingPut(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.toggleStatusUsingPUTParams,
    options?: { [key: string]: any },
) {
    const {id: param0, ...queryParams} = params;
    return request<API.BaseResponseBoolean_>(
        `/api/quantify/indicator/toggle/${param0}`,
        {
            method: "PUT",
            params: {
                ...queryParams,
            },
            ...(options || {}),
        },
    );
}

/** 更新指标 PUT /api/quantify/indicator/update */
export async function updateIndicatorUsingPut(
    body: API.QuantifyIndicator,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/quantify/indicator/update", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}
