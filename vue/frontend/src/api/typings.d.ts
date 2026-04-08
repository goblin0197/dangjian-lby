declare namespace API {
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

  type ActivityEnrollSignInRequest = {
    /** 活动ID */
    activityId: number;
    /** 用户ID */
    userId: number;
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

  type BaseResponseListActivityVO_ = {
    code?: number;
    data?: ActivityVO[];
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

  type deleteOrganizationUsingPOSTParams = {
    /** deleteRequest */
    deleteRequest: string;
  };

  type DeleteRequest = {
    id?: number;
  };

  type DevelopmentStage = {
    assessmentContent?: string;
    assessmentResult?: number;
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

  type downloadFileUsingGETParams = {
    /** 文件URL:材料:/material/{userId}/{filename}，模板：/template/{filename} */
    fileUrl: string;
  };

  type endActivityUsingPOSTParams = {
    /** id */
    id?: number;
  };

  type generateOrgQuantifyDataUsingPOSTParams = {
    /** orgId */
    orgId: number;
  };

  type generateUserQuantifyDataUsingPOSTParams = {
    /** userId */
    userId: number;
  };

  type getActivityByIdUsingGETParams = {
    /** id */
    id?: number;
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

  type listAvailableTrainersUsingGETParams = {
    /** 组织ID */
    orgId?: number;
    /** 用户类型 */
    userType?: string;
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
    id?: number;
    updateTime?: string;
    userAvatar?: string;
    userName?: string;
    userProfile?: string;
    userRole?: string;
  };

  type MapStringString_ = true;

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

  type publishActivityUsingPOSTParams = {
    /** id */
    id?: number;
  };

  type publishNoticeUsingPOSTParams = {
    /** id */
    id: number;
  };

  type removeOrgMemberUsingPOSTParams = {
    /** orgId */
    orgId: number;
    /** userId */
    userId: number;
  };

  type submitForAuditUsingPOSTParams = {
    /** 考察内容 */
    assessmentContent?: string;
    /** 发展阶段ID */
    id: number;
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

  type withdrawNoticeUsingPOSTParams = {
    /** id */
    id: number;
  };
}
