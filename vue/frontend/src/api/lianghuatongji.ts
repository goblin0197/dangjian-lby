// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 生成所有量化统计数据（仅管理员） POST /api/quantify/generate/all */
export async function generateAllQuantifyDataUsingPost(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseBoolean_>("/api/quantify/generate/all", {
    method: "POST",
    ...(options || {}),
  });
}

/** 获取指定组织的量化统计数据（仅管理员） GET /api/quantify/org/${param0} */
export async function getOrgQuantifyDataUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrgQuantifyDataUsingGETParams,
  options?: { [key: string]: any }
) {
  const { orgId: param0, ...queryParams } = params;
  return request<API.BaseResponseOrgQuantify_>(`/api/quantify/org/${param0}`, {
    method: "GET",
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 生成指定组织的量化统计数据（仅管理员） POST /api/quantify/org/generate/${param0} */
export async function generateOrgQuantifyDataUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.generateOrgQuantifyDataUsingPOSTParams,
  options?: { [key: string]: any }
) {
  const { orgId: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean_>(
    `/api/quantify/org/generate/${param0}`,
    {
      method: "POST",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 生成所有组织的量化统计数据（仅管理员） POST /api/quantify/org/generate/all */
export async function generateAllOrgQuantifyDataUsingPost(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseBoolean_>("/api/quantify/org/generate/all", {
    method: "POST",
    ...(options || {}),
  });
}

/** 获取指定用户的量化统计数据（仅管理员） GET /api/quantify/user/${param0} */
export async function getUserQuantifyDataUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserQuantifyDataUsingGETParams,
  options?: { [key: string]: any }
) {
  const { userId: param0, ...queryParams } = params;
  return request<API.BaseResponseUserQuantify_>(
    `/api/quantify/user/${param0}`,
    {
      method: "GET",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 生成指定用户的量化统计数据（仅管理员） POST /api/quantify/user/generate/${param0} */
export async function generateUserQuantifyDataUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.generateUserQuantifyDataUsingPOSTParams,
  options?: { [key: string]: any }
) {
  const { userId: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean_>(
    `/api/quantify/user/generate/${param0}`,
    {
      method: "POST",
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 生成所有用户的量化统计数据（仅管理员） POST /api/quantify/user/generate/all */
export async function generateAllUserQuantifyDataUsingPost(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseBoolean_>("/api/quantify/user/generate/all", {
    method: "POST",
    ...(options || {}),
  });
}
