import request from "@/request";

/** 报名活动 POST /api/activity/enroll/add */
export async function enrollActivityUsingPost(
  body: API.ActivityEnrollAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/activity/enroll/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 取消报名 POST /api/activity/enroll/cancel */
export async function cancelEnrollUsingPost(
  body: API.ActivityEnrollCancelRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/activity/enroll/cancel", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 检查是否已报名 GET /api/activity/enroll/check */
export async function checkEnrolledUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkEnrolledUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/activity/enroll/check", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 签到 POST /api/activity/enroll/signIn */
export async function signInActivityUsingPost(
  body: API.ActivityEnrollSignInRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/activity/enroll/signIn", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
