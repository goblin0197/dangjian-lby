// @ts-ignore
/* eslint-disable */
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

/** 批量删除公告 POST /api/notice/batchDelete */
export async function batchDeleteNoticesUsingPost(
  body: number[],
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/batchDelete", {
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

/** 标记公告为已读 POST /api/notice/markRead */
export async function markAnnouncementAsReadUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.markAnnouncementAsReadUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/markRead", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 下架公告 PUT /api/notice/offline */
export async function offlineNoticeUsingPut(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.offlineNoticeUsingPUTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/offline", {
    method: "PUT",
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

/** 重新发布公告 PUT /api/notice/republish */
export async function republishNoticeUsingPut(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.republishNoticeUsingPUTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/republish", {
    method: "PUT",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 设置公告置顶状态 PUT /api/notice/top */
export async function topNoticeUsingPut(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.topNoticeUsingPUTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/notice/top", {
    method: "PUT",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取未读公告数量 GET /api/notice/unreadCount */
export async function getUnreadCountUsingGet1(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseInt_>("/api/notice/unreadCount", {
    method: "GET",
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
