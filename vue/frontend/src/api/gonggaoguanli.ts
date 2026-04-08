import request from "@/request";

/** 添加公告 POST /api/notice/add */
export async function addNoticeUsingPost(
  body: API.NoticeAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除公告 POST /api/notice/delete */
export async function deleteNoticeUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取公告详情 GET /api/notice/get */
export async function getNoticeVoUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getNoticeVOUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseNoticeVO_>("/api/notice/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 分页获取公告列表 GET /api/notice/list/page */
export async function listNoticeVoByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listNoticeVOByPageUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListNoticeVO_>("/api/notice/list/page", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 发布公告 POST /api/notice/publish */
export async function publishNoticeUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.publishNoticeUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/publish", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 更新公告 POST /api/notice/update */
export async function updateNoticeUsingPost(
  body: API.NoticeUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 撤回公告 POST /api/notice/withdraw */
export async function withdrawNoticeUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.withdrawNoticeUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/withdraw", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
