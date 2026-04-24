// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加组织成员 POST /api/orgMember/add */
export async function addOrgMemberUsingPost(
  body: API.OrgMemberAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/orgMember/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取组织成员列表 POST /api/orgMember/list/page */
export async function listOrgMemberByPageUsingPost(
  body: API.OrgMemberQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageUserVO_>("/api/orgMember/list/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 移除组织成员 POST /api/orgMember/remove */
export async function removeOrgMemberUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeOrgMemberUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/orgMember/remove", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 编辑组织成员角色 POST /api/orgMember/updateRole */
export async function updateOrgMemberRoleUsingPost(
  body: API.OrgMemberUpdateRoleRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/orgMember/updateRole", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
