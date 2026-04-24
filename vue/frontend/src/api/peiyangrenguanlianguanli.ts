// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 添加培养人关联 POST /api/trainerRelation/add */
export async function addTrainerRelationUsingPost(
  body: API.TrainerRelationAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseTrainerRelation_>("/api/trainerRelation/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除培养关系 POST /api/trainerRelation/delete */
export async function deleteTrainerRelationUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/trainerRelation/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 全量获取培养关系列表 GET /api/trainerRelation/list */
export async function getTrainerRelationsListUsingGet(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseListTrainerRelation_>(
    "/api/trainerRelation/list",
    {
      method: "GET",
      ...(options || {}),
    }
  );
}

/** 根据组织ID获取培养关系列表 GET /api/trainerRelation/list/byOrgId */
export async function getTrainerRelationsByOrgIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTrainerRelationsByOrgIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListTrainerRelation_>(
    "/api/trainerRelation/list/byOrgId",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取可选的培养人列表 GET /api/trainerRelation/listAvailableTrainers */
export async function listAvailableTrainersUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listAvailableTrainersUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListUser_>(
    "/api/trainerRelation/listAvailableTrainers",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 根据用户ID查询被培养情况 GET /api/trainerRelation/trainer/byUserId */
export async function getTrainerRelationByUserIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTrainerRelationByUserIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseTrainerRelation_>(
    "/api/trainerRelation/trainer/byUserId",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 根据培养人ID查询被培养人关联 GET /api/trainerRelation/trainRelation/byTrainerId */
export async function getTrainerRelationByTrainerIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTrainerRelationByTrainerIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListTrainerRelation_>(
    "/api/trainerRelation/trainRelation/byTrainerId",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
