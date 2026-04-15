// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 根据发展阶段ID查询变更日志列表 GET /api/developmentStageLog/list/byStageId */
export async function getLogsByStageIdUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getLogsByStageIdUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListDevelopmentStageLog_>(
        "/api/developmentStageLog/list/byStageId",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}

/** 根据用户ID查询变更日志列表 GET /api/developmentStageLog/list/byUserId */
export async function getLogsByUserIdUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getLogsByUserIdUsingGETParams,
    options?: { [key: string]: any },
) {
    return request<API.BaseResponseListDevelopmentStageLog_>(
        "/api/developmentStageLog/list/byUserId",
        {
            method: "GET",
            params: {
                ...params,
            },
            ...(options || {}),
        },
    );
}
