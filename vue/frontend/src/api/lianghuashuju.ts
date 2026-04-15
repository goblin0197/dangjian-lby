import request from "@/request";

/** 添加数据 POST /api/quantify/data/add */
export async function addDataUsingPost(
    body: API.QuantifyData,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseLong_>("/api/quantify/data/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 批量添加数据 POST /api/quantify/data/batchAdd */
export async function batchAddDataUsingPost(
    body: API.QuantifyData[],
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/quantify/data/batchAdd", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 获取核心指标数据 GET /api/quantify/data/core */
export async function getCoreIndicatorsUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getCoreIndicatorsUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseQuantifyCoreIndicatorVO_>(
        "/api/quantify/data/core",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 获取数据详情 GET /api/quantify/data/detail */
export async function getQuantifyDataDetailUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getQuantifyDataDetailUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseQuantifyData_>("/api/quantify/data/detail", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 导出统计报表 GET /api/quantify/data/export */
export async function exportQuantifyReportUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.exportQuantifyReportUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<any>("/api/quantify/data/export", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 获取数据列表 GET /api/quantify/data/list */
export async function listDataUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.listDataUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListQuantifyData_>("/api/quantify/data/list", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 获取组织维度统计数据 GET /api/quantify/data/organization */
export async function getOrganizationStatisticsUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getOrganizationStatisticsUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListQuantifyStatisticsVO_>(
        "/api/quantify/data/organization",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 获取用户权限 GET /api/quantify/data/permission */
export async function getUserPermissionUsingGet(options?: {
    [key: string]: any;
}) {
    return request<API.BaseResponseString_>("/api/quantify/data/permission", {
        method: "GET",
        ...(options || {}),
    });
}

/** 获取个人维度统计数据 GET /api/quantify/data/personal */
export async function getPersonalStatisticsUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getPersonalStatisticsUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListQuantifyStatisticsVO_>(
        "/api/quantify/data/personal",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 根据目标获取数据 GET /api/quantify/data/target */
export async function getByTargetUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getByTargetUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListQuantifyData_>(
        "/api/quantify/data/target",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}
