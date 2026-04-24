declare namespace API {
  type accessShareUsingGETParams = {
    /** shareCode */
    shareCode: string;
  };

  type Activity = {
    activityContent?: string;
    activityName?: string;
    activityType?: number;
    actualParticipant?: number;
    createTime?: string;
    currentNum?: number;
    endTime?: string;
    enrollDeadline?: string;
    id?: number;
    isDelete?: number;
    location?: string;
    maxNum?: number;
    orgId?: number;
    reviewContent?: string;
    signRate?: number;
    startTime?: string;
    status?: number;
    totalParticipant?: number;
    updateTime?: string;
    userId?: number;
  };

  type ActivityAddRequest = {
    /** 活动描述 */
    activityContent?: string;
    /** 活动名称 */
    activityName: string;
    /** 活动类型:1.会议/2.志愿活动/3.学习/4.其他 */
    activityType: number;
    /** 结束时间 */
    endTime: string;
    /** 报名截止时间 */
    enrollDeadline?: string;
    /** 活动地点 */
    location: string;
    /** 最大参与人数 */
    maxNum: number;
    /** 所属党组织ID */
    orgId: number;
    /** 开始时间 */
    startTime: string;
    /** 活动状态:1.待发布/2.已发布/3.进行中/4.已结束 */
    status: number;
  };

  type ActivityEnrollAddRequest = {
    activityId?: number;
    userId?: number;
  };

  type ActivityEnrollCancelRequest = {
    activityId?: number;
    userId?: number;
  };

  type ActivityEnrollListRequest = {
    activityId?: number;
  };

  type ActivityEnrollSignInRequest = {
    /** 活动ID */
    activityId: number;
    /** 用户ID */
    userId: number;
  };

  type ActivityEnrollUserVO = {
    activityId?: number;
    createTime?: string;
    enrollId?: number;
    enrollTime?: string;
    isSign?: number;
    isSignName?: string;
    orgId?: number;
    orgName?: string;
    participantStatus?: number;
    participantStatusName?: string;
    phone?: string;
    politicalStatus?: string;
    userAccount?: string;
    userAvatar?: string;
    userId?: number;
    userName?: string;
    userType?: string;
  };

  type ActivityQueryRequest = {
    activityName?: string;
    activityType?: number;
    current?: number;
    endTime?: string;
    id?: number;
    location?: string;
    orgId?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    startTime?: string;
    status?: number;
    userId?: number;
  };

  type ActivityUpdateRequest = {
    /** 活动描述 */
    activityContent?: string;
    /** 活动名称 */
    activityName?: string;
    /** 活动类型:1.会议/2.志愿活动/3.学习/4.其他 */
    activityType?: number;
    /** 结束时间 */
    endTime?: string;
    /** 报名截止时间 */
    enrollDeadline?: string;
    /** 活动ID */
    id: number;
    /** 活动地点 */
    location?: string;
    /** 最大参与人数 */
    maxNum?: number;
    /** 所属党组织ID */
    orgId?: number;
    /** 活动总结 */
    reviewContent?: string;
    /** 开始时间 */
    startTime?: string;
    /** 活动状态:1.待发布/2.已发布/3.进行中/4.已结束 */
    status?: number;
  };

  type ActivityVO = {
    activityContent?: string;
    activityName?: string;
    activityType?: number;
    activityTypeName?: string;
    actualParticipant?: number;
    createTime?: string;
    currentNum?: number;
    endTime?: string;
    enrollDeadline?: string;
    id?: number;
    location?: string;
    maxNum?: number;
    orgId?: number;
    orgName?: string;
    reviewContent?: string;
    signRate?: number;
    startTime?: string;
    status?: number;
    statusName?: string;
    totalParticipant?: number;
    updateTime?: string;
    userId?: number;
    userName?: string;
  };

  type AnnouncementAttachment = {
    announcementId?: number;
    createTime?: string;
    id?: number;
    isDelete?: number;
    name?: string;
    size?: string;
    type?: string;
    url?: string;
  };

  type AnnouncementReadRecord = {
    announcementId?: number;
    createTime?: string;
    id?: number;
    readTime?: string;
    userId?: number;
  };

  type AnnouncementShare = {
    announcementId?: number;
    createTime?: string;
    expireTime?: string;
    id?: number;
    isDelete?: number;
    shareCode?: string;
    shareTime?: string;
    shareUrl?: string;
    shareUserId?: number;
    shareUserName?: string;
    viewCount?: number;
  };

  type archiveMaterialUsingPOSTParams = {
    /** archiveRemark */
    archiveRemark?: string;
    /** submissionId */
    submissionId: number;
  };

  type auditDevelopmentStageUsingPOSTParams = {
    /** 考察内容 */
    assessmentContent?: string;
    /** 审核结果：1合格/0不合格 */
    assessmentResult: number;
    /** 审核意见 */
    auditRemark?: string;
    /** 发展阶段ID */
    id: number;
  };

  type BaseResponseActivity_ = {
    code?: number;
    data?: Activity;
    message?: string;
  };

  type BaseResponseActivityVO_ = {
    code?: number;
    data?: ActivityVO;
    message?: string;
  };

  type BaseResponseAnnouncementShare_ = {
    code?: number;
    data?: AnnouncementShare;
    message?: string;
  };

  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseDevelopmentStage_ = {
    code?: number;
    data?: DevelopmentStage;
    message?: string;
  };

  type BaseResponseInt_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseListActivityEnrollUserVO_ = {
    code?: number;
    data?: ActivityEnrollUserVO[];
    message?: string;
  };

  type BaseResponseListActivityVO_ = {
    code?: number;
    data?: ActivityVO[];
    message?: string;
  };

  type BaseResponseListAnnouncementAttachment_ = {
    code?: number;
    data?: AnnouncementAttachment[];
    message?: string;
  };

  type BaseResponseListAnnouncementReadRecord_ = {
    code?: number;
    data?: AnnouncementReadRecord[];
    message?: string;
  };

  type BaseResponseListAnnouncementShare_ = {
    code?: number;
    data?: AnnouncementShare[];
    message?: string;
  };

  type BaseResponseListDevelopmentStage_ = {
    code?: number;
    data?: DevelopmentStage[];
    message?: string;
  };

  type BaseResponseListDevelopmentStageLog_ = {
    code?: number;
    data?: DevelopmentStageLog[];
    message?: string;
  };

  type BaseResponseListDevelopmentStageVO_ = {
    code?: number;
    data?: DevelopmentStageVO[];
    message?: string;
  };

  type BaseResponseListMaterialArchiveVO_ = {
    code?: number;
    data?: MaterialArchiveVO[];
    message?: string;
  };

  type BaseResponseListMaterialSubmissionVO_ = {
    code?: number;
    data?: MaterialSubmissionVO[];
    message?: string;
  };

  type BaseResponseListMaterialTemplateVO_ = {
    code?: number;
    data?: MaterialTemplateVO[];
    message?: string;
  };

  type BaseResponseListMyFile_ = {
    code?: number;
    data?: MyFile[];
    message?: string;
  };

  type BaseResponseListNoticeVO_ = {
    code?: number;
    data?: NoticeVO[];
    message?: string;
  };

  type BaseResponseListOrganizationVO_ = {
    code?: number;
    data?: OrganizationVO[];
    message?: string;
  };

  type BaseResponseListOrgRelationTransfer_ = {
    code?: number;
    data?: OrgRelationTransfer[];
    message?: string;
  };

  type BaseResponseListQuantifyData_ = {
    code?: number;
    data?: QuantifyData[];
    message?: string;
  };

  type BaseResponseListQuantifyIndicator_ = {
    code?: number;
    data?: QuantifyIndicator[];
    message?: string;
  };

  type BaseResponseListQuantifyStatisticsVO_ = {
    code?: number;
    data?: QuantifyStatisticsVO[];
    message?: string;
  };

  type BaseResponseListTrainerRelation_ = {
    code?: number;
    data?: TrainerRelation[];
    message?: string;
  };

  type BaseResponseListUser_ = {
    code?: number;
    data?: User[];
    message?: string;
  };

  type BaseResponseListUserVO_ = {
    code?: number;
    data?: UserVO[];
    message?: string;
  };

  type BaseResponseLoginUserVO_ = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseMapStringString_ = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponseMaterialArchiveStatVO_ = {
    code?: number;
    data?: MaterialArchiveStatVO;
    message?: string;
  };

  type BaseResponseMaterialArchiveVO_ = {
    code?: number;
    data?: MaterialArchiveVO;
    message?: string;
  };

  type BaseResponseMaterialSubmissionVO_ = {
    code?: number;
    data?: MaterialSubmissionVO;
    message?: string;
  };

  type BaseResponseMaterialTodoCountVO_ = {
    code?: number;
    data?: MaterialTodoCountVO;
    message?: string;
  };

  type BaseResponseNoticeVO_ = {
    code?: number;
    data?: NoticeVO;
    message?: string;
  };

  type BaseResponseOrganizationGradedVO_ = {
    code?: number;
    data?: OrganizationGradedVO;
    message?: string;
  };

  type BaseResponseOrganizationVO_ = {
    code?: number;
    data?: OrganizationVO;
    message?: string;
  };

  type BaseResponseOrgQuantify_ = {
    code?: number;
    data?: OrgQuantify;
    message?: string;
  };

  type BaseResponseOrgRelationTransfer_ = {
    code?: number;
    data?: OrgRelationTransfer;
    message?: string;
  };

  type BaseResponsePageActivity_ = {
    code?: number;
    data?: PageActivity_;
    message?: string;
  };

  type BaseResponsePageOrganizationVO_ = {
    code?: number;
    data?: PageOrganizationVO_;
    message?: string;
  };

  type BaseResponsePageOrgRelationTransfer_ = {
    code?: number;
    data?: PageOrgRelationTransfer_;
    message?: string;
  };

  type BaseResponsePageUser_ = {
    code?: number;
    data?: PageUser_;
    message?: string;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
  };

  type BaseResponseQuantifyCoreIndicatorVO_ = {
    code?: number;
    data?: QuantifyCoreIndicatorVO;
    message?: string;
  };

  type BaseResponseQuantifyData_ = {
    code?: number;
    data?: QuantifyData;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseTrainerRelation_ = {
    code?: number;
    data?: TrainerRelation;
    message?: string;
  };

  type BaseResponseUser_ = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserQuantify_ = {
    code?: number;
    data?: UserQuantify;
    message?: string;
  };

  type BaseResponseUserVO_ = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type checkEnrolledUsingGETParams = {
    /** activityId */
    activityId?: number;
    /** userId */
    userId?: number;
  };

  type deleteAttachmentUsingDELETEParams = {
    /** id */
    id: number;
  };

  type deleteIndicatorUsingDELETEParams = {
    /** id */
    id: number;
  };

  type DeleteRequest = {
    id?: number;
  };

  type DevelopmentStage = {
    assessmentContent?: string;
    assessmentResult?: number;
    attachment?: string;
    auditRemark?: string;
    auditTime?: string;
    auditUserId?: number;
    createTime?: string;
    id?: number;
    isDelete?: number;
    stageEndTime?: string;
    stageName?: string;
    stageStartTime?: string;
    stageStatus?: number;
    trainerId?: number;
    updateTime?: string;
    userId?: number;
  };

  type DevelopmentStageAddRequest = {
    /** 考察内容 */
    assessmentContent?: string;
    /** 阶段名称 */
    stageName: string;
    /** 阶段开始时间 */
    stageStartTime: string;
    /** 培养人ID */
    trainerId: number;
    /** 用户ID */
    userId: number;
  };

  type DevelopmentStageLog = {
    afterData?: string;
    beforeData?: string;
    createTime?: string;
    id?: number;
    operationType?: string;
    operatorId?: number;
    operatorName?: string;
    remark?: string;
    stageId?: number;
    userId?: number;
  };

  type DevelopmentStageQueryRequest = {
    assessmentResult?: number;
    auditTime?: string;
    auditUserId?: number;
    createTime?: string;
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    stageEndTime?: string;
    stageName?: string;
    stageStartTime?: string;
    stageStatus?: number;
    trainerId?: number;
    updateTime?: string;
    userId?: number;
  };

  type DevelopmentStageUpdateRequest = {
    /** 考察内容 */
    assessmentContent?: string;
    /** 考察结果 */
    assessmentResult?: number;
    /** id */
    id: number;
    /** 阶段结束时间 */
    stageEndTime?: string;
    /** 阶段状态 */
    stageStatus?: number;
  };

  type DevelopmentStageVO = {
    assessmentContent?: string;
    assessmentResult?: number;
    auditRemark?: string;
    auditTime?: string;
    auditUserId?: number;
    createTime?: string;
    files?: MyFile[];
    id?: number;
    isDelete?: number;
    stageEndTime?: string;
    stageName?: string;
    stageStartTime?: string;
    stageStatus?: number;
    trainerId?: number;
    updateTime?: string;
    userId?: number;
  };

  type downloadAnnouncementAttachmentUsingGETParams = {
    /** id */
    id: number;
  };

  type downloadFileUsingGETParams = {
    /** 文件URL:材料:/material/{userId}/{filename}，模板：/template/{filename} */
    fileUrl: string;
  };

  type downloadMaterialArchiveUsingGETParams = {
    /** id */
    id: number;
  };

  type downloadMaterialTemplateUsingGETParams = {
    /** id */
    id: number;
  };

  type downloadMaterialUsingGETParams = {
    /** id */
    id: number;
  };

  type endActivityUsingPOSTParams = {
    /** id */
    id?: number;
  };

  type exportMaterialArchiveUsingGETParams = {
    /** id */
    id: number;
  };

  type exportQuantifyReportUsingGETParams = {
    /** dimension */
    dimension?: string;
    /** indicator */
    indicator?: string;
    /** orgLevel */
    orgLevel?: string;
    /** timeRange */
    timeRange?: string;
  };

  type generateOrgQuantifyDataUsingPOSTParams = {
    /** orgId */
    orgId: number;
  };

  type generateShareUsingPOSTParams = {
    /** announcementId */
    announcementId: number;
    /** expireTime */
    expireTime?: string;
  };

  type generateUserQuantifyDataUsingPOSTParams = {
    /** userId */
    userId: number;
  };

  type getActivityByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getAnnouncementShareListUsingGETParams = {
    /** announcementId */
    announcementId: number;
  };

  type getAnnouncementTimelineUsingGETParams = {
    /** current */
    current?: number;
    /** pageSize */
    pageSize?: number;
    /** status */
    status?: number;
    /** timeRange */
    timeRange?: string;
  };

  type getByTargetUsingGETParams = {
    /** period */
    period: string;
    /** targetId */
    targetId: number;
    /** targetType */
    targetType: string;
  };

  type getCoreIndicatorsUsingGETParams = {
    /** dimension */
    dimension?: string;
    /** orgLevel */
    orgLevel?: string;
    /** timeRange */
    timeRange?: string;
  };

  type getDevelopmentStageByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type getDevelopmentStagesByOrgIdUsingGETParams = {
    /** orgId */
    orgId: number;
  };

  type getDevelopmentStagesByUserIdUsingGETParams = {
    /** userId */
    userId: number;
  };

  type getLogsByStageIdUsingGETParams = {
    /** 发展阶段ID */
    stageId: number;
  };

  type getLogsByUserIdUsingGETParams = {
    /** 用户ID */
    userId: number;
  };

  type getMaterialListUsingGETParams = {
    /** userId */
    userId: number;
  };

  type getNoticeVOUsingGETParams = {
    /** id */
    id: number;
  };

  type getOrganizationByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type getOrganizationGradedByIdUsingGETParams = {
    /** orgId */
    orgId: number;
  };

  type getOrganizationStatisticsUsingGETParams = {
    /** indicator */
    indicator?: string;
    /** orgLevel */
    orgLevel?: string;
    /** timeRange */
    timeRange?: string;
  };

  type getOrgQuantifyDataUsingGETParams = {
    /** orgId */
    orgId: number;
  };

  type getOrgRelationTransferByIdUsingGETParams = {
    /** id */
    id: number;
  };

  type getOrgRelationTransfersByOrgIdUsingGETParams = {
    /** orgId */
    orgId: number;
  };

  type getOrgRelationTransfersByUserIdUsingGETParams = {
    /** userId */
    userId: number;
  };

  type getPersonalStatisticsUsingGETParams = {
    /** indicator */
    indicator?: string;
    /** orgLevel */
    orgLevel?: string;
    /** timeRange */
    timeRange?: string;
  };

  type getQuantifyDataDetailUsingGETParams = {
    /** dimension */
    dimension?: string;
    /** id */
    id: number;
  };

  type getTemplateListUsingGETParams = {
    /** orgId */
    orgId: number;
  };

  type getTrainerRelationByTrainerIdUsingGETParams = {
    /** trainerId */
    trainerId: number;
  };

  type getTrainerRelationByUserIdUsingGETParams = {
    /** userId */
    userId: number;
  };

  type getTrainerRelationsByOrgIdUsingGETParams = {
    /** orgId */
    orgId: number;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getUserQuantifyDataUsingGETParams = {
    /** userId */
    userId: number;
  };

  type getUserVOByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type incrementReadCountUsingPUTParams = {
    /** announcementId */
    announcementId: number;
  };

  type listAttachmentUsingGETParams = {
    /** announcementId */
    announcementId: number;
  };

  type listAvailableTrainersUsingGETParams = {
    /** 组织ID */
    orgId?: number;
    /** 用户类型 */
    userType?: string;
  };

  type listDataUsingGETParams = {
    /** period */
    period?: string;
  };

  type listIndicatorUsingGETParams = {
    /** status */
    status?: string;
  };

  type listMaterialArchiveUsingGETParams = {
    /** 页码 */
    current?: number;
    /** 材料名称（模糊查询） */
    materialName?: string;
    /** 组织层级 */
    orgLevel?: string;
    /** 每页大小 */
    pageSize?: number;
    /** 发展阶段 */
    stage?: string;
    /** 用户姓名（模糊查询） */
    userName?: string;
  };

  type listMaterialSubmissionUsingGETParams = {
    /** 审核状态 */
    auditStatus?: string;
    /** 页码 */
    current?: number;
    /** 材料名称（模糊查询） */
    materialName?: string;
    /** 组织层级 */
    orgLevel?: string;
    /** 每页大小 */
    pageSize?: number;
    /** 发展阶段 */
    stage?: string;
    /** 提交状态 */
    submitStatus?: string;
    /** 用户 ID */
    userId?: number;
    /** 用户姓名（模糊查询） */
    userName?: string;
  };

  type listMaterialTemplateUsingGETParams = {
    /** 页码 */
    current?: number;
    /** 模板名称（模糊查询） */
    name?: string;
    /** 每页大小 */
    pageSize?: number;
    /** 所属发展阶段 */
    stage?: string;
    /** 模板状态：enable-启用/disable-停用 */
    status?: string;
    /** 材料类型 */
    type?: string;
  };

  type listNoticeVOByPageUsingGETParams = {
    /** 过期时间结束 */
    expireTimeEnd?: string;
    /** 过期时间开始 */
    expireTimeStart?: string;
    /** 公告ID */
    id?: number;
    /** 是否置顶:0-否,1-是,默认全部 */
    isTop?: number;
    /** 所属党组织ID,默认全部 */
    orgId?: number;
    /** 发布时间结束 */
    publishTimeEnd?: string;
    /** 发布时间开始 */
    publishTimeStart?: string;
    /** 发布人ID */
    publisherId?: number;
    /** 状态:0-草稿/1-已发布,默认全部 */
    status?: number;
    /** 公告标题 */
    title?: string;
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type LoginUserVO = {
    createTime?: string;
    email?: string;
    id?: number;
    joinDate?: string;
    orgId?: number;
    phone?: string;
    politicalStatus?: string;
    positiveDate?: string;
    status?: number;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    userType?: string;
  };

  type MapStringString_ = true;

  type markAnnouncementAsReadUsingPOSTParams = {
    /** announcementId */
    announcementId: number;
  };

  type markAsReadUsingPOSTParams = {
    /** announcementId */
    announcementId: number;
  };

  type MaterialArchiveBatchExportRequest = {
    /** 归档 ID 列表 */
    ids: number[];
  };

  type MaterialArchiveStatVO = {
    /** 月度增长率 */
    growthRate?: number;
    /** 本月归档数 */
    monthCount?: number;
    /** 各阶段归档统计 */
    stageStats?: Record<string, any>;
    /** 归档总数 */
    totalCount?: number;
  };

  type MaterialArchiveVO = {
    /** 归档备注 */
    archiveRemark?: string;
    /** 归档时间 */
    archiveTime?: string;
    /** 归档人 */
    archiveUser?: string;
    /** 归档人 ID */
    archiveUserId?: number;
    /** 审核时间 */
    auditTime?: string;
    /** 审核人 */
    auditor?: string;
    /** 创建时间 */
    createTime?: string;
    /** 文件大小 */
    fileSize?: number;
    /** 文件大小格式化 */
    fileSizeFormatted?: string;
    /** 归档文件路径 */
    fileUrl?: string;
    /** 归档 ID */
    id?: number;
    /** 材料名称 */
    materialName?: string;
    /** 组织层级 */
    orgLevel?: string;
    /** 发展阶段 */
    stage?: string;
    /** 提交 ID */
    submissionId?: number;
    /** 更新时间 */
    updateTime?: string;
    /** 上传时间 */
    uploadTime?: string;
    /** 用户 ID */
    userId?: number;
    /** 用户姓名 */
    userName?: string;
  };

  type MaterialSubmissionAuditRequest = {
    /** 审核意见 */
    auditOpinion?: string;
    /** 审核结果：approved-通过/rejected-退回 */
    auditResult: string;
    /** 提交 ID */
    id: number;
  };

  type MaterialSubmissionFinalAuditRequest = {
    /** 终审意见 */
    auditOpinion?: string;
    /** 终审结果：approved-通过/rejected-退回 */
    auditResult: string;
    /** 提交 ID */
    id: number;
  };

  type MaterialSubmissionSubmitRequest = {
    /** 文件大小（字节） */
    fileSize?: number;
    /** 材料文件路径 */
    fileUrl: string;
    /** 材料名称 */
    materialName: string;
    /** 备注 */
    remark?: string;
    /** 发展阶段 */
    stage: string;
  };

  type MaterialSubmissionVO = {
    /** 审核意见 */
    auditOpinion?: string;
    /** 审核状态 */
    auditStatus?: string;
    /** 审核状态名称 */
    auditStatusName?: string;
    /** 审核时间 */
    auditTime?: string;
    /** 审核人 */
    auditor?: string;
    /** 审核人 ID */
    auditorId?: number;
    /** 创建时间 */
    createTime?: string;
    /** 文件大小 */
    fileSize?: number;
    /** 文件大小格式化 */
    fileSizeFormatted?: string;
    /** 材料文件路径 */
    fileUrl?: string;
    /** 终审意见 */
    finalAuditOpinion?: string;
    /** 终审时间 */
    finalAuditTime?: string;
    /** 终审人 */
    finalAuditor?: string;
    /** 终审人 ID */
    finalAuditorId?: number;
    /** 提交 ID */
    id?: number;
    /** 材料名称 */
    materialName?: string;
    /** 组织层级 */
    orgLevel?: string;
    /** 发展阶段 */
    stage?: string;
    /** 提交状态 */
    submitStatus?: string;
    /** 提交状态名称 */
    submitStatusName?: string;
    /** 更新时间 */
    updateTime?: string;
    /** 上传时间 */
    uploadTime?: string;
    /** 用户 ID */
    userId?: number;
    /** 用户姓名 */
    userName?: string;
  };

  type MaterialTemplateAddRequest = {
    /** 文件大小（字节） */
    fileSize?: number;
    /** 模板文件路径 */
    fileUrl: string;
    /** 模板名称 */
    name: string;
    /** 模板说明 */
    remark?: string;
    /** 所属发展阶段 */
    stage: string;
    /** 模板状态：enable-启用/disable-停用 */
    status?: string;
    /** 材料类型 */
    type: string;
  };

  type MaterialTemplateBatchRequest = {
    /** 模板 ID 列表 */
    ids: number[];
    /** 目标状态 */
    targetStatus?: string;
  };

  type MaterialTemplateUpdateRequest = {
    /** 文件大小（字节） */
    fileSize?: number;
    /** 模板文件路径 */
    fileUrl?: string;
    /** 模板 ID */
    id: number;
    /** 模板名称 */
    name?: string;
    /** 模板说明 */
    remark?: string;
    /** 所属发展阶段 */
    stage?: string;
    /** 模板状态：enable-启用/disable-停用 */
    status?: string;
    /** 材料类型 */
    type?: string;
  };

  type MaterialTemplateVO = {
    /** 创建时间 */
    createTime?: string;
    /** 文件大小 */
    fileSize?: number;
    /** 文件大小格式化 */
    fileSizeFormatted?: string;
    /** 模板文件路径 */
    fileUrl?: string;
    /** 模板 ID */
    id?: number;
    /** 模板名称 */
    name?: string;
    /** 模板说明 */
    remark?: string;
    /** 所属发展阶段 */
    stage?: string;
    /** 模板状态 */
    status?: string;
    /** 状态名称 */
    statusName?: string;
    /** 材料类型 */
    type?: string;
    /** 更新时间 */
    updateTime?: string;
    /** 上传用户 ID */
    uploadUserId?: number;
    /** 上传用户姓名 */
    uploadUserName?: string;
  };

  type MaterialTodoCountVO = {
    /** 待审核数量 */
    toAuditCount?: number;
    /** 待终审数量 */
    toFinalAuditCount?: number;
    /** 待提交数量 */
    toSubmitCount?: number;
  };

  type MyFile = {
    createTime?: string;
    fileName?: string;
    fileSize?: number;
    fileUrl?: string;
    id?: number;
    isDelete?: number;
    isTemplate?: number;
    orgId?: number;
    originFileName?: string;
    stageId?: number;
    updateTime?: string;
    userId?: number;
  };

  type NoticeAddRequest = {
    /** 公告内容 */
    content: string;
    /** 过期时间 */
    expireTime?: string;
    /** 是否置顶:0-否,1-是 */
    isTop?: number;
    /** 所属党组织ID(null表示系统公告) */
    orgId?: number;
    /** 发布时间 */
    publishTime?: string;
    /** 状态:0-草稿/1-已发布 */
    status?: number;
    /** 公告标题 */
    title: string;
  };

  type NoticeUpdateRequest = {
    /** 公告内容 */
    content?: string;
    /** 过期时间 */
    expireTime?: string;
    /** 公告ID */
    id: number;
    /** 是否置顶:0-否,1-是 */
    isTop?: number;
    /** 状态：0-草稿，1-已发布 */
    status?: number;
    /** 公告标题 */
    title?: string;
  };

  type NoticeVO = {
    content?: string;
    createTime?: string;
    expireTime?: string;
    id?: number;
    isTop?: number;
    isTopName?: string;
    orgId?: number;
    orgName?: string;
    publishTime?: string;
    publisherId?: number;
    publisherName?: string;
    status?: number;
    statusName?: string;
    title?: string;
    updateTime?: string;
  };

  type offlineNoticeUsingPUTParams = {
    /** id */
    id: number;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type OrganizationAddRequest = {
    address?: string;
    description?: string;
    leaderId?: number;
    /** 组织编码 */
    orgCode: string;
    /** 组织级别 */
    orgLevel: number;
    /** 组织名称 */
    orgName: string;
    /** 组织类型：党委/党总支/党支部 */
    orgType: string;
    /** 父组织ID */
    parentId: number;
  };

  type OrganizationBindRequest = {
    /** 负责人ID */
    leaderId: number;
    /** 党组织ID */
    orgId: number;
  };

  type OrganizationGradedVO = {
    address?: string;
    createTime?: string;
    description?: string;
    id?: number;
    leaderId?: number;
    orgCode?: string;
    orgLevel?: number;
    orgName?: string;
    orgType?: string;
    parentId?: number;
    subOrgList?: OrganizationGradedVO[];
    updateTime?: string;
  };

  type OrganizationQueryRequest = {
    address?: string;
    createTime?: string;
    current?: number;
    description?: string;
    id?: number;
    isDelete?: number;
    leaderId?: number;
    orgCode?: string;
    orgLevel?: number;
    orgName?: string;
    orgType?: string;
    pageSize?: number;
    parentId?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type OrganizationUpdateRequest = {
    address?: string;
    description?: string;
    /** id */
    id: number;
    isDelete?: number;
    leaderId?: number;
    orgCode?: string;
    orgLevel?: number;
    orgName?: string;
    orgType?: string;
    parentId?: number;
  };

  type OrganizationVO = {
    address?: string;
    createTime?: string;
    description?: string;
    id?: number;
    leader?: User;
    orgCode?: string;
    orgLevel?: number;
    orgName?: string;
    orgType?: string;
    parentId?: number;
    updateTime?: string;
  };

  type OrgMemberAddRequest = {
    /** 组织ID */
    orgId: number;
    /** 用户ID */
    userId: number;
  };

  type OrgMemberQueryRequest = {
    current?: number;
    /** 组织ID */
    orgId: number;
    pageSize?: number;
    /** 政治面貌 */
    politicalStatus?: string;
    sortField?: string;
    sortOrder?: string;
    /** 状态 */
    status?: number;
    /** 用户账号 */
    userAccount?: string;
    /** 用户姓名 */
    userName?: string;
    /** 用户角色 */
    userRole?: string;
    /** 用户类型 */
    userType?: string;
  };

  type OrgMemberUpdateRoleRequest = {
    /** 组织ID */
    orgId: number;
    /** 用户ID */
    userId: number;
    /** 用户角色 */
    userRole: string;
  };

  type OrgQuantify = {
    activityCount?: number;
    avgParticipant?: number;
    avgSign?: number;
    createTime?: string;
    id?: number;
    isDelete?: number;
    orgId?: number;
    statDate?: string;
    totalParticipant?: number;
    totalSign?: number;
    updateTime?: string;
  };

  type OrgRelationTransfer = {
    approveComment?: string;
    approveStatus?: number;
    approveTime?: string;
    approveUserId?: number;
    approveUserName?: string;
    createTime?: string;
    fromOrgId?: number;
    fromOrgName?: string;
    id?: number;
    isDelete?: number;
    toOrgId?: number;
    toOrgName?: string;
    transferReason?: string;
    transferTime?: string;
    updateTime?: string;
    userId?: number;
  };

  type OrgRelationTransferAddRequest = {
    /** 原党组织ID,无组织时为0 */
    fromOrgId: number;
    /** 目标党组织ID */
    toOrgId: number;
    transferReason?: string;
    /** 用户ID（党员ID） */
    userId: number;
  };

  type OrgRelationTransferApproveRequest = {
    /** 审批意见 */
    approveComment?: string;
    /** 审批状态：1-待审批/2-已通过/3-已拒绝 */
    approveStatus: number;
    /** 组织转移记录id */
    id: number;
  };

  type OrgRelationTransferUpdateRequest = {
    /** 原党组织ID */
    fromOrgId: number;
    /** id */
    id: number;
    /** 目标党组织ID */
    toOrgId: number;
    transferReason?: string;
    transferTime?: string;
    /** 用户ID（党员ID） */
    userId: number;
  };

  type PageActivity_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: Activity[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageOrganizationVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: OrganizationVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageOrgRelationTransfer_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: OrgRelationTransfer[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type pageOrgRelationTransfersUsingGETParams = {
    /** pageNum */
    pageNum?: number;
    /** pageSize */
    pageSize?: number;
  };

  type PageUser_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: User[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type previewAnnouncementAttachmentUsingGETParams = {
    /** id */
    id: number;
  };

  type previewMaterialTemplateUsingGETParams = {
    /** id */
    id: number;
  };

  type publishActivityUsingPOSTParams = {
    /** id */
    id?: number;
  };

  type publishNoticeUsingPOSTParams = {
    /** id */
    id: number;
  };

  type QuantifyCoreIndicatorVO = {
    activityRate?: number;
    materialRate?: number;
    orgLevel?: string;
    signRate?: number;
    timeRange?: string;
  };

  type QuantifyData = {
    activityRate?: number;
    createTime?: string;
    id?: number;
    indicatorId?: number;
    materialRate?: number;
    period?: string;
    signRate?: number;
    targetId?: number;
    targetType?: string;
    updateTime?: string;
    value?: number;
  };

  type QuantifyIndicator = {
    createTime?: string;
    dimension?: string;
    id?: number;
    isDelete?: number;
    name?: string;
    orgLevel?: string;
    rule?: string;
    status?: string;
    updateTime?: string;
  };

  type QuantifyStatisticsVO = {
    indicator?: string;
    period?: string;
    rank?: number;
    targetId?: number;
    targetName?: string;
    value?: number;
  };

  type rejectMaterialUsingPUTParams = {
    /** auditOpinion */
    auditOpinion: string;
    /** id */
    id: number;
  };

  type removeOrgMemberUsingPOSTParams = {
    /** orgId */
    orgId: number;
    /** userId */
    userId: number;
  };

  type republishNoticeUsingPUTParams = {
    /** id */
    id: number;
  };

  type searchAnnouncementTimelineUsingGETParams = {
    /** current */
    current?: number;
    /** keyword */
    keyword: string;
    /** pageSize */
    pageSize?: number;
    /** timeRange */
    timeRange?: string;
  };

  type submitForAuditUsingPOSTParams = {
    /** 考察内容 */
    assessmentContent?: string;
    /** 发展阶段ID */
    id: number;
  };

  type toggleMaterialTemplateStatusUsingPUTParams = {
    /** id */
    id: number;
  };

  type toggleStatusUsingPUTParams = {
    /** id */
    id: number;
    /** status */
    status: string;
  };

  type topNoticeUsingPUTParams = {
    /** id */
    id: number;
    /** isTop */
    isTop: number;
  };

  type TrainerRelation = {
    createTime?: string;
    endDate?: string;
    id?: number;
    isDelete?: number;
    startDate?: string;
    status?: number;
    trainerId?: number;
    trainerName?: string;
    updateTime?: string;
    userId?: number;
    userName?: string;
  };

  type TrainerRelationAddRequest = {
    /** 开始日期 */
    startDate?: string;
    /** 培养人ID */
    trainerId: number;
    /** 用户ID（被培养人） */
    userId: number;
  };

  type updateActivityStatisticsUsingPOSTParams = {
    /** id */
    id?: number;
  };

  type updateAttachmentByIdUsingPOSTParams = {
    /** attachment */
    attachment: string;
    /** id */
    id: number;
  };

  type uploadAnnouncementAttachmentUsingPOSTParams = {
    /** announcementId */
    announcementId: number;
  };

  type uploadFileUsingPOSTParams = {
    /** 业务:material-材料,template-模板 */
    biz: string;
    /** 党组织ID,业务为template时必填 */
    orgId?: number;
    /** 发展阶段ID,用户上传自己的材料时必填 */
    stageId?: number;
  };

  type User = {
    createTime?: string;
    email?: string;
    id?: number;
    isDelete?: number;
    joinDate?: string;
    orgId?: number;
    phone?: string;
    politicalStatus?: string;
    positiveDate?: string;
    status?: number;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    userType?: string;
  };

  type UserAddRequest = {
    /** 账号 */
    userAccount: string;
    userAvatar?: string;
    userName?: string;
    /** 用户角色, super_admin, org_admin, org_member, activist_development */
    userRole?: string;
    /** 用户类型, 教师/学生 */
    userType?: string;
  };

  type UserLoginRequest = {
    /** 账号 */
    userAccount: string;
    /** 密码 */
    userPassword: string;
  };

  type UserQuantify = {
    createTime?: string;
    fileCount?: number;
    id?: number;
    isDelete?: number;
    participateActivity?: number;
    participateRate?: number;
    signActivity?: number;
    signRate?: number;
    statDate?: string;
    totalActivity?: number;
    updateTime?: string;
    userId?: number;
  };

  type UserQueryRequest = {
    current?: number;
    email?: string;
    id?: number;
    joinDate?: string;
    orgId?: number;
    pageSize?: number;
    phone?: string;
    politicalStatus?: string;
    positiveDate?: string;
    sortField?: string;
    sortOrder?: string;
    status?: number;
    userAccount?: string;
    userName?: string;
    userRole?: string;
    userType?: string;
  };

  type UserUpdateMyRequest = {
    email?: string;
    phone?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
  };

  type UserUpdateRequest = {
    email?: string;
    /** 用户ID */
    id: number;
    joinDate?: string;
    orgId?: number;
    phone?: string;
    politicalStatus?: string;
    positiveDate?: string;
    status?: number;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    userType?: string;
  };

  type UserVO = {
    createTime?: string;
    email?: string;
    id?: number;
    joinDate?: string;
    orgId?: number;
    orgName?: string;
    phone?: string;
    politicalStatus?: string;
    positiveDate?: string;
    status?: number;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
    userType?: string;
  };

  type viewMaterialArchiveDetailUsingGETParams = {
    /** id */
    id: number;
  };

  type viewMaterialUsingGETParams = {
    /** id */
    id: number;
  };

  type withdrawNoticeUsingPOSTParams = {
    /** id */
    id: number;
  };
}
