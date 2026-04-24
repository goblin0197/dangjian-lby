// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 审核材料（初审） PUT /api/api/material/submission/audit */
export async function auditMaterialUsingPut(
  body: API.MaterialSubmissionAuditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>(
    "/api/api/material/submission/audit",
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** 下载材料文件 GET /api/api/material/submission/download */
export async function downloadMaterialUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadMaterialUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString_>(
    "/api/api/material/submission/download",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 终审材料 PUT /api/api/material/submission/finalAudit */
export async function finalAuditMaterialUsingPut(
  body: API.MaterialSubmissionFinalAuditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>(
    "/api/api/material/submission/finalAudit",
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** 获取材料提交列表 GET /api/api/material/submission/list */
export async function listMaterialSubmissionUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listMaterialSubmissionUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListMaterialSubmissionVO_>(
    "/api/api/material/submission/list",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 退回材料 PUT /api/api/material/submission/reject */
export async function rejectMaterialUsingPut(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.rejectMaterialUsingPUTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>(
    "/api/api/material/submission/reject",
    {
      method: "PUT",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 提交材料审核 POST /api/api/material/submission/submit */
export async function submitMaterialUsingPost(
  body: API.MaterialSubmissionSubmitRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>("/api/api/material/submission/submit", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取待提交和待审核数量 GET /api/api/material/submission/todoCount */
export async function getMaterialTodoCountUsingGet(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseMaterialTodoCountVO_>(
    "/api/api/material/submission/todoCount",
    {
      method: "GET",
      ...(options || {}),
    }
  );
}

/** 查看材料详情 GET /api/api/material/submission/view */
export async function viewMaterialUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.viewMaterialUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseMaterialSubmissionVO_>(
    "/api/api/material/submission/view",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
