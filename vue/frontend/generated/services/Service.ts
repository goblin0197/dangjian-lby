/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ActivityAddRequest } from '../models/ActivityAddRequest';
import type { ActivityEnrollAddRequest } from '../models/ActivityEnrollAddRequest';
import type { ActivityEnrollCancelRequest } from '../models/ActivityEnrollCancelRequest';
import type { ActivityEnrollSignInRequest } from '../models/ActivityEnrollSignInRequest';
import type { ActivityQueryRequest } from '../models/ActivityQueryRequest';
import type { ActivityUpdateRequest } from '../models/ActivityUpdateRequest';
import type { BaseResponse_Activity_ } from '../models/BaseResponse_Activity_';
import type { BaseResponse_ActivityVO_ } from '../models/BaseResponse_ActivityVO_';
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_List_ActivityVO_ } from '../models/BaseResponse_List_ActivityVO_';
import type { BaseResponse_List_MyFile_ } from '../models/BaseResponse_List_MyFile_';
import type { BaseResponse_List_NoticeVO_ } from '../models/BaseResponse_List_NoticeVO_';
import type { BaseResponse_List_OrganizationVO_ } from '../models/BaseResponse_List_OrganizationVO_';
import type { BaseResponse_List_OrgRelationTransfer_ } from '../models/BaseResponse_List_OrgRelationTransfer_';
import type { BaseResponse_List_TrainerRelation_ } from '../models/BaseResponse_List_TrainerRelation_';
import type { BaseResponse_LoginUserVO_ } from '../models/BaseResponse_LoginUserVO_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Map_string_string_ } from '../models/BaseResponse_Map_string_string_';
import type { BaseResponse_NoticeVO_ } from '../models/BaseResponse_NoticeVO_';
import type { BaseResponse_OrganizationVO_ } from '../models/BaseResponse_OrganizationVO_';
import type { BaseResponse_OrgQuantify_ } from '../models/BaseResponse_OrgQuantify_';
import type { BaseResponse_OrgRelationTransfer_ } from '../models/BaseResponse_OrgRelationTransfer_';
import type { BaseResponse_Page_Activity_ } from '../models/BaseResponse_Page_Activity_';
import type { BaseResponse_Page_OrganizationVO_ } from '../models/BaseResponse_Page_OrganizationVO_';
import type { BaseResponse_Page_OrgRelationTransfer_ } from '../models/BaseResponse_Page_OrgRelationTransfer_';
import type { BaseResponse_Page_User_ } from '../models/BaseResponse_Page_User_';
import type { BaseResponse_Page_UserVO_ } from '../models/BaseResponse_Page_UserVO_';
import type { BaseResponse_string_ } from '../models/BaseResponse_string_';
import type { BaseResponse_TrainerRelation_ } from '../models/BaseResponse_TrainerRelation_';
import type { BaseResponse_User_ } from '../models/BaseResponse_User_';
import type { BaseResponse_UserQuantify_ } from '../models/BaseResponse_UserQuantify_';
import type { BaseResponse_UserVO_ } from '../models/BaseResponse_UserVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { NoticeAddRequest } from '../models/NoticeAddRequest';
import type { NoticeUpdateRequest } from '../models/NoticeUpdateRequest';
import type { OrganizationAddRequest } from '../models/OrganizationAddRequest';
import type { OrganizationBindRequest } from '../models/OrganizationBindRequest';
import type { OrganizationQueryRequest } from '../models/OrganizationQueryRequest';
import type { OrganizationUpdateRequest } from '../models/OrganizationUpdateRequest';
import type { OrgRelationTransferAddRequest } from '../models/OrgRelationTransferAddRequest';
import type { OrgRelationTransferApproveRequest } from '../models/OrgRelationTransferApproveRequest';
import type { OrgRelationTransferUpdateRequest } from '../models/OrgRelationTransferUpdateRequest';
import type { TrainerRelationAddRequest } from '../models/TrainerRelationAddRequest';
import type { UserAddRequest } from '../models/UserAddRequest';
import type { UserLoginRequest } from '../models/UserLoginRequest';
import type { UserQueryRequest } from '../models/UserQueryRequest';
import type { UserRegisterRequest } from '../models/UserRegisterRequest';
import type { UserUpdateMyRequest } from '../models/UserUpdateMyRequest';
import type { UserUpdateRequest } from '../models/UserUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class Service {
    /**
     * 添加活动（仅管理员）
     * @param activityAddRequest activityAddRequest
     * @returns BaseResponse_Activity_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addActivityUsingPost(
        activityAddRequest: ActivityAddRequest,
    ): CancelablePromise<BaseResponse_Activity_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/add',
            body: activityAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 删除活动（仅管理员）
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteActivityUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 结束活动（仅管理员）
     * @param id id
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static endActivityUsingPost(
        id?: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/end',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 报名活动
     * @param activityEnrollAddRequest activityEnrollAddRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static enrollActivityUsingPost(
        activityEnrollAddRequest: ActivityEnrollAddRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/enroll/add',
            body: activityEnrollAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 取消报名
     * @param activityEnrollCancelRequest activityEnrollCancelRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static cancelEnrollUsingPost(
        activityEnrollCancelRequest: ActivityEnrollCancelRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/enroll/cancel',
            body: activityEnrollCancelRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 检查是否已报名
     * @param activityId activityId
     * @param userId userId
     * @returns BaseResponse_boolean_ OK
     * @throws ApiError
     */
    public static checkEnrolledUsingGet(
        activityId?: number,
        userId?: number,
    ): CancelablePromise<BaseResponse_boolean_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/activity/enroll/check',
            query: {
                'activityId': activityId,
                'userId': userId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 签到
     * @param activityEnrollSignInRequest activityEnrollSignInRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static signInActivityUsingPost(
        activityEnrollSignInRequest: ActivityEnrollSignInRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/enroll/signIn',
            body: activityEnrollSignInRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 获取活动详情
     * @param id id
     * @returns BaseResponse_ActivityVO_ OK
     * @throws ApiError
     */
    public static getActivityByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_ActivityVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/activity/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 分页获取活动列表
     * @param activityQueryRequest activityQueryRequest
     * @returns BaseResponse_Page_Activity_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listActivityByPageUsingPost(
        activityQueryRequest: ActivityQueryRequest,
    ): CancelablePromise<BaseResponse_Page_Activity_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/list/page',
            body: activityQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 分页获取活动VO列表
     * @param activityQueryRequest activityQueryRequest
     * @returns BaseResponse_List_ActivityVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listActivityVoByPageUsingPost(
        activityQueryRequest: ActivityQueryRequest,
    ): CancelablePromise<BaseResponse_List_ActivityVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/list/page/vo',
            body: activityQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 发布活动（仅管理员）
     * @param id id
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static publishActivityUsingPost(
        id?: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/publish',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新指定活动的参与和签到情况统计（仅管理员）
     * @param id id
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateActivityStatisticsUsingPost(
        id?: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/statistics/update',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新所有活动的参与和签到情况统计（仅管理员）
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateAllActivityStatisticsUsingPost(): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/statistics/update/all',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新活动（仅管理员）
     * @param activityUpdateRequest activityUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateActivityUsingPost(
        activityUpdateRequest: ActivityUpdateRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/activity/update',
            body: activityUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 文件删除
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteFileUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/file/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 文件下载
     * @param fileUrl 文件URL:材料:/material/{userId}/{filename}，模板：/template/{filename}
     * @returns any OK
     * @throws ApiError
     */
    public static downloadFileUsingGet(
        fileUrl: string,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/file/download',
            query: {
                'fileUrl': fileUrl,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 材料文件列表(没有校验权限)
     * @param userId userId
     * @returns BaseResponse_List_MyFile_ OK
     * @throws ApiError
     */
    public static getMaterialListUsingGet(
        userId: number,
    ): CancelablePromise<BaseResponse_List_MyFile_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/file/material/list',
            query: {
                'userId': userId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 模板文件列表(没有校验权限)
     * @param orgId orgId
     * @returns BaseResponse_List_MyFile_ OK
     * @throws ApiError
     */
    public static getTemplateListUsingGet(
        orgId: number,
    ): CancelablePromise<BaseResponse_List_MyFile_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/file/template/list',
            query: {
                'orgId': orgId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 文件上传
     * @param biz 业务:material-材料,template-模板
     * @param file file
     * @param orgId 党组织ID,业务为template时必填
     * @returns BaseResponse_string_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static uploadFileUsingPost(
        biz: string,
        file: Blob,
        orgId?: number,
    ): CancelablePromise<BaseResponse_string_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/file/upload',
            query: {
                'biz': biz,
                'orgId': orgId,
            },
            formData: {
                'file': file,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 添加公告
     * @param noticeAddRequest noticeAddRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addNoticeUsingPost(
        noticeAddRequest: NoticeAddRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/notice/add',
            body: noticeAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 删除公告
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteNoticeUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/notice/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 获取公告详情
     * @param id id
     * @returns BaseResponse_NoticeVO_ OK
     * @throws ApiError
     */
    public static getNoticeVoUsingGet(
        id: number,
    ): CancelablePromise<BaseResponse_NoticeVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/notice/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 分页获取公告列表
     * @param expireTimeEnd 过期时间结束
     * @param expireTimeStart 过期时间开始
     * @param id 公告ID
     * @param isTop 是否置顶:0-否,1-是,默认全部
     * @param orgId 所属党组织ID,默认全部
     * @param publishTimeEnd 发布时间结束
     * @param publishTimeStart 发布时间开始
     * @param publisherId 发布人ID
     * @param status 状态:0-草稿/1-已发布,默认全部
     * @param title 公告标题
     * @param current
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @returns BaseResponse_List_NoticeVO_ OK
     * @throws ApiError
     */
    public static listNoticeVoByPageUsingGet(
        expireTimeEnd?: string,
        expireTimeStart?: string,
        id?: number,
        isTop?: number,
        orgId?: number,
        publishTimeEnd?: string,
        publishTimeStart?: string,
        publisherId?: number,
        status?: number,
        title?: string,
        current?: number,
        pageSize?: number,
        sortField?: string,
        sortOrder?: string,
    ): CancelablePromise<BaseResponse_List_NoticeVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/notice/list/page',
            query: {
                'expireTimeEnd': expireTimeEnd,
                'expireTimeStart': expireTimeStart,
                'id': id,
                'isTop': isTop,
                'orgId': orgId,
                'publishTimeEnd': publishTimeEnd,
                'publishTimeStart': publishTimeStart,
                'publisherId': publisherId,
                'status': status,
                'title': title,
                'current': current,
                'pageSize': pageSize,
                'sortField': sortField,
                'sortOrder': sortOrder,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 发布公告
     * @param id id
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static publishNoticeUsingPost(
        id: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/notice/publish',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新公告
     * @param noticeUpdateRequest noticeUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateNoticeUsingPost(
        noticeUpdateRequest: NoticeUpdateRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/notice/update',
            body: noticeUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 撤回公告
     * @param id id
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static withdrawNoticeUsingPost(
        id: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/notice/withdraw',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 创建组织关系转移申请
     * @param orgRelationTransferAddRequest orgRelationTransferAddRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addOrgRelationTransferUsingPost(
        orgRelationTransferAddRequest: OrgRelationTransferAddRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/orgRelationTransfer/add',
            body: orgRelationTransferAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 审批组织关系转移申请
     * @param orgRelationTransferApproveRequest orgRelationTransferApproveRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static approveOrgRelationTransferUsingPost(
        orgRelationTransferApproveRequest: OrgRelationTransferApproveRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/orgRelationTransfer/approve',
            body: orgRelationTransferApproveRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据党组织ID查询组织关系转移记录
     * @param orgId orgId
     * @returns BaseResponse_List_OrgRelationTransfer_ OK
     * @throws ApiError
     */
    public static getOrgRelationTransfersByOrgIdUsingGet(
        orgId: number,
    ): CancelablePromise<BaseResponse_List_OrgRelationTransfer_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/orgRelationTransfer/byOrgId',
            query: {
                'orgId': orgId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据党员ID查询组织关系转移记录
     * @param userId userId
     * @returns BaseResponse_List_OrgRelationTransfer_ OK
     * @throws ApiError
     */
    public static getOrgRelationTransfersByUserIdUsingGet(
        userId: number,
    ): CancelablePromise<BaseResponse_List_OrgRelationTransfer_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/orgRelationTransfer/byUserId',
            query: {
                'userId': userId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 删除组织关系转移记录
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteOrgRelationTransferUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/orgRelationTransfer/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据ID查询组织关系转移记录详情
     * @param id id
     * @returns BaseResponse_OrgRelationTransfer_ OK
     * @throws ApiError
     */
    public static getOrgRelationTransferByIdUsingGet(
        id: number,
    ): CancelablePromise<BaseResponse_OrgRelationTransfer_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/orgRelationTransfer/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 查询所有组织关系转移记录
     * @returns BaseResponse_List_OrgRelationTransfer_ OK
     * @throws ApiError
     */
    public static listOrgRelationTransfersUsingGet(): CancelablePromise<BaseResponse_List_OrgRelationTransfer_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/orgRelationTransfer/list',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 分页查询组织关系转移记录
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @returns BaseResponse_Page_OrgRelationTransfer_ OK
     * @throws ApiError
     */
    public static pageOrgRelationTransfersUsingGet(
        pageNum: number = 1,
        pageSize: number = 10,
    ): CancelablePromise<BaseResponse_Page_OrgRelationTransfer_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/orgRelationTransfer/page',
            query: {
                'pageNum': pageNum,
                'pageSize': pageSize,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新组织关系转移信息
     * @param orgRelationTransferUpdateRequest orgRelationTransferUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateOrgRelationTransferUsingPost(
        orgRelationTransferUpdateRequest: OrgRelationTransferUpdateRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/orgRelationTransfer/update',
            body: orgRelationTransferUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 创建党组织
     * @param organizationAddRequest organizationAddRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addOrganizationUsingPost(
        organizationAddRequest: OrganizationAddRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/organization/add',
            body: organizationAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 党组织绑定负责人
     * @param organizationBindRequest organizationBindRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static bindOrganizationLeaderUsingPost(
        organizationBindRequest: OrganizationBindRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/organization/bind',
            body: organizationBindRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 删除党组织
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteOrganizationUsingPost(
        deleteRequest: string,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/organization/delete',
            query: {
                'deleteRequest': deleteRequest,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据ID查询党组织
     * @param id id
     * @returns BaseResponse_OrganizationVO_ OK
     * @throws ApiError
     */
    public static getOrganizationByIdUsingGet(
        id: number,
    ): CancelablePromise<BaseResponse_OrganizationVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/organization/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 查询所有党组织
     * @returns BaseResponse_List_OrganizationVO_ OK
     * @throws ApiError
     */
    public static listOrganizationsUsingGet(): CancelablePromise<BaseResponse_List_OrganizationVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/organization/list',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据条件分页查询党组织
     * @param organizationQueryRequest organizationQueryRequest
     * @returns BaseResponse_Page_OrganizationVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static queryOrganizationsUsingPost(
        organizationQueryRequest: OrganizationQueryRequest,
    ): CancelablePromise<BaseResponse_Page_OrganizationVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/organization/list/page',
            body: organizationQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新党组织信息
     * @param organizationUpdateRequest organizationUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateOrganizationUsingPost(
        organizationUpdateRequest: OrganizationUpdateRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/organization/update',
            body: organizationUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 生成所有量化统计数据（仅管理员）
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static generateAllQuantifyDataUsingPost(): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/quantify/generate/all',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 生成所有组织的量化统计数据（仅管理员）
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static generateAllOrgQuantifyDataUsingPost(): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/quantify/org/generate/all',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 生成指定组织的量化统计数据（仅管理员）
     * @param orgId orgId
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static generateOrgQuantifyDataUsingPost(
        orgId: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/quantify/org/generate/{orgId}',
            path: {
                'orgId': orgId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 获取指定组织的量化统计数据（仅管理员）
     * @param orgId orgId
     * @returns BaseResponse_OrgQuantify_ OK
     * @throws ApiError
     */
    public static getOrgQuantifyDataUsingGet(
        orgId: number,
    ): CancelablePromise<BaseResponse_OrgQuantify_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/quantify/org/{orgId}',
            path: {
                'orgId': orgId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 生成所有用户的量化统计数据（仅管理员）
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static generateAllUserQuantifyDataUsingPost(): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/quantify/user/generate/all',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 生成指定用户的量化统计数据（仅管理员）
     * @param userId userId
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static generateUserQuantifyDataUsingPost(
        userId: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/quantify/user/generate/{userId}',
            path: {
                'userId': userId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 获取指定用户的量化统计数据（仅管理员）
     * @param userId userId
     * @returns BaseResponse_UserQuantify_ OK
     * @throws ApiError
     */
    public static getUserQuantifyDataUsingGet(
        userId: number,
    ): CancelablePromise<BaseResponse_UserQuantify_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/quantify/user/{userId}',
            path: {
                'userId': userId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 添加培养人关联
     * @param trainerRelationAddRequest trainerRelationAddRequest
     * @returns BaseResponse_TrainerRelation_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTrainerRelationUsingPost(
        trainerRelationAddRequest: TrainerRelationAddRequest,
    ): CancelablePromise<BaseResponse_TrainerRelation_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/trainerRelation/add',
            body: trainerRelationAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据培养人ID查询被培养人关联
     * @param trainerId trainerId
     * @returns BaseResponse_List_TrainerRelation_ OK
     * @throws ApiError
     */
    public static getTrainerRelationByTrainerIdUsingGet(
        trainerId: number,
    ): CancelablePromise<BaseResponse_List_TrainerRelation_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/trainerRelation/trainRelation/byTrainerId',
            query: {
                'trainerId': trainerId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据用户ID查询被培养情况
     * @param userId userId
     * @returns BaseResponse_TrainerRelation_ OK
     * @throws ApiError
     */
    public static getTrainerRelationByUserIdUsingGet(
        userId: number,
    ): CancelablePromise<BaseResponse_TrainerRelation_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/trainerRelation/trainer/byUserId',
            query: {
                'userId': userId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 创建用户（仅管理员）
     * @param userAddRequest userAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addUserUsingPost(
        userAddRequest: UserAddRequest,
    ): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/add',
            body: userAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 删除用户（仅管理员）
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteUserUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据 id 获取用户（仅管理员）
     * @param id id
     * @returns BaseResponse_User_ OK
     * @throws ApiError
     */
    public static getUserByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_User_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/user/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 获取当前登录用户
     * @returns BaseResponse_LoginUserVO_ OK
     * @throws ApiError
     */
    public static getLoginUserUsingGet(): CancelablePromise<BaseResponse_LoginUserVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/user/get/login',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 根据 id 获取用户包装类）
     * @param id id
     * @returns BaseResponse_UserVO_ OK
     * @throws ApiError
     */
    public static getUserVoByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_UserVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/user/get/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 分页获取用户列表（仅管理员）
     * @param userQueryRequest userQueryRequest
     * @returns BaseResponse_Page_User_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listUserByPageUsingPost(
        userQueryRequest: UserQueryRequest,
    ): CancelablePromise<BaseResponse_Page_User_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/list/page',
            body: userQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 分页获取用户封装列表
     * @param userQueryRequest userQueryRequest
     * @returns BaseResponse_Page_UserVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listUserVoByPageUsingPost(
        userQueryRequest: UserQueryRequest,
    ): CancelablePromise<BaseResponse_Page_UserVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/list/page/vo',
            body: userQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 获取所有角色
     * @returns BaseResponse_Map_string_string_ OK
     * @throws ApiError
     */
    public static listRoleUsingGet(): CancelablePromise<BaseResponse_Map_string_string_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/user/list/role',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 用户登录
     * @param userLoginRequest userLoginRequest
     * @returns BaseResponse_LoginUserVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static userLoginUsingPost(
        userLoginRequest: UserLoginRequest,
    ): CancelablePromise<BaseResponse_LoginUserVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/login',
            body: userLoginRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 用户注销
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static userLogoutUsingPost(): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/logout',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 用户注册
     * @param userRegisterRequest userRegisterRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static userRegisterUsingPost(
        userRegisterRequest: UserRegisterRequest,
    ): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/register',
            body: userRegisterRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新用户（仅管理员）
     * @param userUpdateRequest userUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateUserUsingPost(
        userUpdateRequest: UserUpdateRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/update',
            body: userUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * 更新个人信息
     * @param userUpdateMyRequest userUpdateMyRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateMyUserUsingPost(
        userUpdateMyRequest: UserUpdateMyRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/user/update/my',
            body: userUpdateMyRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
}
