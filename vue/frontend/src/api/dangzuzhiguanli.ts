import request from "@/request";

/** 创建党组织 POST /api/organization/add */
export async function addOrganizationUsingPost(
    body: API.OrganizationAddRequest,
    options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/organization/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 党组织绑定负责人 POST /api/organization/bind */
export async function bindOrganizationLeaderUsingPost(
    body: API.OrganizationBindRequest,
    options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/organization/bind", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除党组织 POST /api/organization/delete */
export async function deleteOrganizationUsingPost(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.deleteOrganizationUsingPOSTParams,
    options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/organization/delete", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 根据ID查询党组织 GET /api/organization/get */
export async function getOrganizationByIdUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: API.getOrganizationByIdUsingGETParams,
    options?: { [key: string]: any }
) {
  return request<API.BaseResponseOrganizationVO_>("/api/organization/get", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取党组织及其子党组织 GET /api/organization/get/graded */
export async function getOrganizationGradedByIdUsingGet(
    // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
    params: { orgId: string },
    options?: { [p: string]: any },
) {
  return request<API.BaseResponseOrganizationGradedVO_>(
      "/api/organization/get/graded",
      {
        method: "GET",
        params: {
          ...params,
        },
        ...(options || {}),
      }
  );
}

/** 查询所有党组织 GET /api/organization/list */
export async function listOrganizationsUsingGet(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseListOrganizationVO_>(
      "/api/organization/list",
      {
        method: "GET",
        ...(options || {}),
      }
  );
}

/** 根据条件分页查询党组织 POST /api/organization/list/page */
export async function queryOrganizationsUsingPost(
    body: API.OrganizationQueryRequest,
    options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageOrganizationVO_>(
      "/api/organization/list/page",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        data: body,
        ...(options || {}),
      }
  );
}

/** 更新党组织信息 POST /api/organization/update */
export async function updateOrganizationUsingPost(
    body: API.OrganizationUpdateRequest,
    options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/organization/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
