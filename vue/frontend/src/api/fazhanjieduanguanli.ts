// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加发展阶段记录 POST /api/developmentStage/add */
export async function addDevelopmentStageUsingPost(
  body: API.DevelopmentStageAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseDevelopmentStage_>(
    "/api/developmentStage/add",
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

/** 审核发展阶段记录 POST /api/developmentStage/audit */
export async function auditDevelopmentStageUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.auditDevelopmentStageUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/developmentStage/audit", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 删除发展阶段记录 POST /api/developmentStage/delete */
export async function deleteDevelopmentStageUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/developmentStage/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据ID查询发展阶段记录 GET /api/developmentStage/get */
export async function getDevelopmentStageByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDevelopmentStageByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseDevelopmentStage_>(
    "/api/developmentStage/get",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取发展阶段列表 POST /api/developmentStage/list */
export async function getDevelopmentStagesListUsingPost(
  body: API.DevelopmentStageQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDevelopmentStage_>(
    "/api/developmentStage/list",
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

/** 根据组织ID获取发展阶段列表 GET /api/developmentStage/list/byOrgId */
export async function getDevelopmentStagesByOrgIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDevelopmentStagesByOrgIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDevelopmentStage_>(
    "/api/developmentStage/list/byOrgId",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 根据用户ID获取发展阶段列表 GET /api/developmentStage/list/byUserId */
export async function getDevelopmentStagesByUserIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDevelopmentStagesByUserIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDevelopmentStageVO_>(
    "/api/developmentStage/list/byUserId",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 提交审核 POST /api/developmentStage/submitForAudit */
export async function submitForAuditUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.submitForAuditUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>(
    "/api/developmentStage/submitForAudit",
    {
      method: "POST",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 更新发展阶段记录 POST /api/developmentStage/update */
export async function updateDevelopmentStageUsingPost(
  body: API.DevelopmentStageUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/developmentStage/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** updateAttachmentById POST /api/developmentStage/updateAttachment/byId */
export async function updateAttachmentByIdUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateAttachmentByIdUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>(
    "/api/developmentStage/updateAttachment/byId",
    {
      method: "POST",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
