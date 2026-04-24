// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** 将终审通过的材料归档 POST /api/api/material/archive/archive */
export async function archiveMaterialUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.archiveMaterialUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>("/api/api/material/archive/archive", {
    method: "POST",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 批量导出归档材料为压缩包 POST /api/api/material/archive/batchExport */
export async function batchExportMaterialArchiveUsingPost(
  body: API.MaterialArchiveBatchExportRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString_>(
    "/api/api/material/archive/batchExport",
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

/** 查看归档材料详情 GET /api/api/material/archive/detail */
export async function viewMaterialArchiveDetailUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.viewMaterialArchiveDetailUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseMaterialArchiveVO_>(
    "/api/api/material/archive/detail",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 下载归档材料文件 GET /api/api/material/archive/download */
export async function downloadMaterialArchiveUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadMaterialArchiveUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString_>(
    "/api/api/material/archive/download",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 导出单个归档材料 GET /api/api/material/archive/export */
export async function exportMaterialArchiveUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.exportMaterialArchiveUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<any>("/api/api/material/archive/export", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取归档材料列表 GET /api/api/material/archive/list */
export async function listMaterialArchiveUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listMaterialArchiveUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListMaterialArchiveVO_>(
    "/api/api/material/archive/list",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** 获取归档材料统计数据 GET /api/api/material/archive/stat */
export async function getMaterialArchiveStatUsingGet(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseMaterialArchiveStatVO_>(
    "/api/api/material/archive/stat",
    {
      method: "GET",
      ...(options || {}),
    }
  );
}
