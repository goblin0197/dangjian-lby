// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加活动（仅管理员） POST /api/activity/add */
export async function addActivityUsingPost(
    body: API.ActivityAddRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseActivity_>("/api/activity/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 删除活动（仅管理员） POST /api/activity/delete */
export async function deleteActivityUsingPost(
    body: API.DeleteRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/activity/delete", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 结束活动（仅管理员） POST /api/activity/end */
export async function endActivityUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.endActivityUsingPOSTParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/activity/end", {
        method: "POST",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 获取活动详情 GET /api/activity/get */
export async function getActivityByIdUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getActivityByIdUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseActivityVO_>("/api/activity/get", {
        method: "GET",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 分页获取活动列表 POST /api/activity/list/page */
export async function listActivityByPageUsingPost(
    body: API.ActivityQueryRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponsePageActivity_>("/api/activity/list/page", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}

/** 分页获取活动VO列表 POST /api/activity/list/page/vo */
export async function listActivityVoByPageUsingPost(
    body: API.ActivityQueryRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListActivityVO_>(
        "/api/activity/list/page/vo",
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

/** 发布活动（仅管理员） POST /api/activity/publish */
export async function publishActivityUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.publishActivityUsingPOSTParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/activity/publish", {
        method: "POST",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 更新指定活动的参与和签到情况统计（仅管理员） POST /api/activity/statistics/update */
export async function updateActivityStatisticsUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.updateActivityStatisticsUsingPOSTParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/activity/statistics/update", {
        method: "POST",
        params: {
            ...params,
        },
        ...(options || {}),
    });
}

/** 更新所有活动的参与和签到情况统计（仅管理员） POST /api/activity/statistics/update/all */
export async function updateAllActivityStatisticsUsingPost(options?: {
    [key: string]: any;
}) {
    return request<API.BaseResponseBoolean_>(
        "/api/activity/statistics/update/all",
        {
            method: "POST",
            ...(options || {}),
        },
    );
}

/** 更新活动（仅管理员） POST /api/activity/update */
export async function updateActivityUsingPost(
    body: API.ActivityUpdateRequest,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseBoolean_>("/api/activity/update", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
    });
}
