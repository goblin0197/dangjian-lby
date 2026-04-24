// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 创建组织关系转移申请 POST /api/orgRelationTransfer/add */
export async function addOrgRelationTransferUsingPost(
  body: API.OrgRelationTransferAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/orgRelationTransfer/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 审批组织关系转移申请 POST /api/orgRelationTransfer/approve */
export async function approveOrgRelationTransferUsingPost(
  body: API.OrgRelationTransferApproveRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/orgRelationTransfer/approve", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据党组织ID查询组织关系转移记录 GET /api/orgRelationTransfer/byOrgId */
export async function getOrgRelationTransfersByOrgIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrgRelationTransfersByOrgIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListOrgRelationTransfer_>(
    "/api/orgRelationTransfer/byOrgId",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 根据党员ID查询组织关系转移记录 GET /api/orgRelationTransfer/byUserId */
export async function getOrgRelationTransfersByUserIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrgRelationTransfersByUserIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListOrgRelationTransfer_>(
    "/api/orgRelationTransfer/byUserId",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 删除组织关系转移记录 POST /api/orgRelationTransfer/delete */
export async function deleteOrgRelationTransferUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/orgRelationTransfer/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据ID查询组织关系转移记录详情 GET /api/orgRelationTransfer/get */
export async function getOrgRelationTransferByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrgRelationTransferByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseOrgRelationTransfer_>(
    "/api/orgRelationTransfer/get",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 查询所有组织关系转移记录 GET /api/orgRelationTransfer/list */
export async function listOrgRelationTransfersUsingGet(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseListOrgRelationTransfer_>(
    "/api/orgRelationTransfer/list",
    {
      method: "GET",
      ...(options || {}),
    }
  );
}

/** 分页查询组织关系转移记录 GET /api/orgRelationTransfer/page */
export async function pageOrgRelationTransfersUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageOrgRelationTransfersUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageOrgRelationTransfer_>(
    "/api/orgRelationTransfer/page",
    {
      method: "GET",
      params: {
        // pageNum has a default value: 1
        pageNum: "1",
        // pageSize has a default value: 10
        pageSize: "10",
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 更新组织关系转移信息 POST /api/orgRelationTransfer/update */
export async function updateOrgRelationTransferUsingPost(
  body: API.OrgRelationTransferUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/orgRelationTransfer/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
