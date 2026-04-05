<template>
  <a-card>
    <a-form :model="searchParams">
      <a-row :gutter="16">
        <a-col :span="4">
          <a-form-item field="orgLevel" label="组织层级" label-col-flex="100px">
            <a-dropdown :popup-max-height="false" @select="cropTypeSelect">
              <a-button>
                {{ cropTypeDefaultValue }}
                <icon-down />
              </a-button>
              <template #content>
                <a-doption value="partyCommittees">党委</a-doption>
                <a-doption value="branch">支部</a-doption>
              </template>
            </a-dropdown>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item
            field="developStage"
            label="发展阶段"
            label-col-flex="80px"
          >
            <a-dropdown :popup-max-height="false" @select="developStageSelect">
              <a-button>
                {{ developStageDefaultValue }}
                <icon-down />
              </a-button>
              <template #content>
                <a-doption value="all">全部</a-doption>
                <a-doption value="activist">积极分子</a-doption>
                <a-doption value="developmentObject">发展对象</a-doption>
                <a-doption value="probationaryPartyMember">预备党员</a-doption>
                <a-doption value="formalPartyMember">正式党员</a-doption>
              </template>
            </a-dropdown>
          </a-form-item>
        </a-col>
        <a-col :span="4">
          <a-form-item
            field="partyMemberType"
            label="党员类型"
            label-col-flex="80px"
          >
            <a-dropdown
              :popup-max-height="false"
              @select="partyMemberTypeSelect"
            >
              <a-button>
                {{ partyMemberTypeDefaultValue }}
                <icon-down />
              </a-button>
              <template #content>
                <a-doption value="teacher">教师</a-doption>
                <a-doption value="student">学生</a-doption>
              </template>
            </a-dropdown>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="搜索" label-col-flex="80px">
            <a-input
              v-model="searchParams.cropName"
              placeholder="请输入姓名/学号/工号..."
            />
          </a-form-item>
        </a-col>
        <a-col :span="1">
          <a-button long type="outline" @click="handlesumbit">搜索</a-button>
        </a-col>
      </a-row>
    </a-form>
  </a-card>

  <a-card style="margin-top: 16px">
    <a-space style="margin-bottom: 1vh">
      <a-button type="primary" @click="AddData">新增发展记录</a-button>
      <a-button type="primary" @click="delData">批量导出</a-button>
      <a-button type="primary" @click="modifyData(1)">批量审核</a-button>
    </a-space>
    <a-space direction="vertical" fill size="large">
      <a-table
        :columns="columns"
        :data="dataList"
        :filter-icon-align-left="alignLeft"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total,
        }"
        :row-selection="rowSelection"
        :selectedRowKeys="selectedRowKeys"
        row-key="id"
        @page-change="onPageChange"
        @selection-change="handleSelectionChange"
      >
        <template #userName="{ record }">
          <a-space>
            <a-button type="text">{{ record.userName }}</a-button>
          </a-space>
        </template>
        <template #orgId="{ record }">
          <a-space>
            <a-button type="text">{{ record.orgId }}</a-button>
          </a-space>
        </template>
        <template #politicalStatus="{ record }">
          <a-space>
            <a-button type="text">{{ record.politicalStatus }}</a-button>
          </a-space>
        </template>
        <template #operation="{ record }">
          <a-space wrap>
            <a-button
              type="dashed"
              status="success"
              @click="toShowInfo(record)"
              size="medium"
            >
              详情
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-space>
  </a-card>

  <!-- 详情/编辑模态框 - 完整布局 -->
  <a-modal
    width="1400px"
    v-model:visible="infoVisible"
    @ok="handleInfoOk"
    @cancel="handleInfoCancel"
    :footer="null"
  >
    <!-- 党员基础信息卡片 -->
    <div
      style="
        background: #fdf2f2;
        padding: 16px;
        border-radius: 8px;
        margin-bottom: 16px;
      "
    >
      <div
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;
        "
      >
        <div style="display: flex; gap: 24px; font-size: 14px">
          <span><b>姓名：</b>{{ addFormData.name }}</span>
          <span><b>身份：</b>{{ addFormData.identity }}</span>
          <span><b>支部：</b>{{ addFormData.branch }}</span>
          <span><b>当前阶段：</b>{{ addFormData.currentStage }}</span>
          <span><b>培养联系人：</b>{{ addFormData.trainer }}</span>
        </div>
        <div style="display: flex; gap: 8px">
          <a-button type="text" @click="goBackList">
            <icon-left />
            返回列表
          </a-button>
          <a-button type="text" @click="toggleEdit">
            <icon-edit />
            编辑
          </a-button>
          <a-button type="text" @click="saveForm">
            <icon-save />
            保存
          </a-button>
          <a-button type="text" @click="printInfo">
            <icon-printer />
            打印
          </a-button>
        </div>
      </div>
    </div>

    <!-- 核心内容：左侧时间轴 + 右侧详情表单 -->
    <a-row :gutter="24">
      <!-- 左侧：发展阶段时间轴 -->
      <a-col :span="6">
        <div
          style="
            border-right: 1px solid #eee;
            padding-right: 16px;
            height: 600px;
          "
        >
          <a-timeline>
            <a-timeline-item
              color="green"
              :dot="h('icon-check', { style: { color: '#00b42a' } })"
            >
              <div style="font-weight: 600">积极分子</div>
              <div style="font-size: 12px; color: #666">已完成</div>
              <div style="font-size: 12px; margin-top: 4px">
                完成时间：2025-01-10
              </div>
            </a-timeline-item>
            <a-timeline-item
              color="blue"
              :dot="h('icon-circle-fill', { style: { color: '#165dff' } })"
            >
              <div style="font-weight: 600">发展对象</div>
              <div style="font-size: 12px; color: #666">进行中</div>
              <div style="font-size: 12px; margin-top: 4px">
                进入时间：2025-01-10
              </div>
              <div style="font-size: 12px">预计完成：2025-06-30</div>
            </a-timeline-item>
            <a-timeline-item
              color="gray"
              :dot="h('icon-circle', { style: { color: '#86909c' } })"
            >
              <div style="font-weight: 600">预备党员</div>
              <div style="font-size: 12px; color: #666">未开始</div>
            </a-timeline-item>
            <a-timeline-item
              color="gray"
              :dot="h('icon-circle', { style: { color: '#86909c' } })"
            >
              <div style="font-weight: 600">正式党员</div>
              <div style="font-size: 12px; color: #666">未开始</div>
            </a-timeline-item>
          </a-timeline>
        </div>
      </a-col>

      <!-- 右侧：当前阶段详情表单 -->
      <a-col :span="18">
        <div style="height: 600px; overflow-y: auto; padding-left: 16px">
          <!-- 1. 阶段基础信息 -->
          <div style="margin-bottom: 24px">
            <h4 style="margin: 0 0 16px 0; font-size: 16px">📌 阶段信息</h4>
            <a-form :model="stageForm" :disabled="!isEdit">
              <a-row :gutter="12">
                <a-col :span="12">
                  <a-form-item label="进入时间" label-col-flex="80px">
                    <a-date-picker
                      v-model="stageForm.enterTime"
                      style="width: 100%"
                    />
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item label="预计完成时间" label-col-flex="100px">
                    <a-date-picker
                      v-model="stageForm.expectTime"
                      style="width: 100%"
                    />
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </div>

          <!-- 2. 考察记录（富文本） -->
          <div style="margin-bottom: 24px">
            <h4 style="margin: 0 0 16px 0; font-size: 16px">📝 考察记录</h4>
            <a-textarea
              v-model="stageForm.inspectRecord"
              placeholder="请输入思想表现、学习情况、谈话记录等..."
              :rows="6"
              style="width: 100%"
              :disabled="!isEdit"
            />
          </div>

          <!-- 3. 阶段材料 -->
          <div style="margin-bottom: 24px">
            <h4 style="margin: 0 0 16px 0; font-size: 16px">📎 阶段材料</h4>
            <a-space direction="vertical" style="width: 100%">
              <a-space>
                <icon-file-text style="color: #165dff" />
                <span>政审材料</span>
                <a-button type="text" size="small" @click="previewFile"
                  >预览
                </a-button>
                <a-button type="text" size="small" @click="downloadFile"
                  >下载
                </a-button>
              </a-space>
              <a-space>
                <icon-file-text style="color: #165dff" />
                <span>思想汇报</span>
                <a-upload
                  v-if="isEdit"
                  action="/api/upload"
                  :show-file-list="false"
                  @change="handleUpload"
                >
                  <a-button type="text" size="small">上传</a-button>
                </a-upload>
                <span v-else>未上传</span>
              </a-space>
            </a-space>
          </div>

          <!-- 4. 审核记录 -->
          <div>
            <h4 style="margin: 0 0 16px 0; font-size: 16px">✅ 审核记录</h4>
            <div
              style="border: 1px solid #eee; padding: 12px; border-radius: 4px"
            >
              <div style="margin-bottom: 8px"><b>审核人：</b>王五</div>
              <div style="margin-bottom: 8px"><b>审核时间：</b>2025-02-01</div>
              <div><b>审核意见：</b>同意继续培养</div>
            </div>
          </div>
        </div>
      </a-col>
    </a-row>

    <!-- 底部操作按钮 -->
    <div
      style="
        margin-top: 24px;
        display: flex;
        justify-content: center;
        gap: 16px;
      "
    >
      <a-button @click="saveForm">💾 保存</a-button>
      <a-button type="primary" @click="submitAudit">✅ 提交审核</a-button>
      <a-button @click="handleInfoCancel">← 返回</a-button>
      <a-button type="dashed" @click="viewChangeLog">📜 查看变更日志</a-button>
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { Service } from "../../../generated";
import { UserQueryRequest } from "../../../generated/models";
import { h, reactive, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
// 正确导入Arco图标
import {
  IconDown,
  IconEdit,
  IconFileText,
  IconLeft,
  IconPrinter,
  IconSave,
} from "@arco-design/web-vue/es/icon";

// 下拉框默认值
const cropTypeDefaultValue = ref("全部");
const developStageDefaultValue = ref("全部");
const partyMemberTypeDefaultValue = ref("全部");

// 搜索参数
const searchParams = ref<UserQueryRequest>({
  current: 1,
  pageSize: 10,
  cropName: "", // 补充搜索字段默认值
});
const total = ref(0);
const dataList = ref([]);
const alignLeft = ref(false);

// 表格选择相关
const rowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
const selectedRowKeys = ref([]);
const handleSelectionChange = (keys: any) => {
  selectedRowKeys.value = keys;
};

// 详情模态框相关
const infoVisible = ref(false);
const isEdit = ref(false); // 编辑状态标识
// 党员基础信息表单
const addFormData = reactive({
  name: "张三",
  identity: "教师党员",
  branch: "教师一支部",
  currentStage: "发展对象",
  trainer: "李四（教师党员）",
});
// 阶段详情表单
const stageForm = reactive({
  enterTime: "2025-01-10",
  expectTime: "2025-06-30",
  inspectRecord:
    "思想表现良好，积极参与支部学习活动，谈话过程中对党的理论认知清晰，符合发展对象标准。",
});

// 模态框事件
const handleInfoOk = async () => {
  infoVisible.value = false;
};
const handleInfoCancel = () => {
  infoVisible.value = false;
  isEdit.value = false; // 关闭时重置编辑状态
};
const toShowInfo = (record: any) => {
  // 模拟赋值（实际项目中从接口获取）
  addFormData.name = record.userName || "张三";
  addFormData.identity =
    record.userType === "teacher" ? "教师党员" : "学生党员";
  addFormData.branch = record.orgId || "教师一支部";
  addFormData.currentStage = record.politicalStatus || "发展对象";
  infoVisible.value = true;
};

// 下拉框选择事件
const cropTypeSelect = (value: string) => {
  cropTypeDefaultValue.value = value === "partyCommittees" ? "党委" : "支部";
};
const developStageSelect = (value: string) => {
  const stageMap = {
    all: "全部",
    activist: "积极分子",
    developmentObject: "发展对象",
    probationaryPartyMember: "预备党员",
    formalPartyMember: "正式党员",
  };
  developStageDefaultValue.value = stageMap[value] || "全部";
};
const partyMemberTypeSelect = (value: string) => {
  partyMemberTypeDefaultValue.value = value === "teacher" ? "教师" : "学生";
};

// 表格列配置
const columns = [
  {
    title: "姓名",
    dataIndex: "userName",
    sortable: { sortDirections: ["ascend", "descend"] },
    ellipsis: true,
    tooltip: { position: "left" },
  },
  {
    title: "用户类型",
    dataIndex: "userType",
    sortable: { sortDirections: ["ascend", "descend"] },
    ellipsis: true,
    tooltip: { position: "left" },
  },
  {
    title: "所属支部",
    slotName: "orgId",
    sortable: { sortDirections: ["ascend", "descend"] },
    ellipsis: true,
    tooltip: { position: "left" },
  },
  {
    title: "政治面貌",
    slotName: "politicalStatus",
    sortable: { sortDirections: ["ascend", "descend"] },
    ellipsis: true,
    tooltip: { position: "left" },
  },
  {
    title: "培养人",
    dataIndex: "trainerName",
    sortable: { sortDirections: ["ascend", "descend"] },
    ellipsis: true,
    tooltip: { position: "left" },
  },
  {
    title: "申请入党日期",
    dataIndex: "joinDate",
    sortable: { sortDirections: ["ascend", "descend"] },
    ellipsis: true,
    tooltip: { position: "left" },
  },
  {
    title: "转正日期",
    dataIndex: "positiveDate",
    sortable: { sortDirections: ["ascend", "descend"] },
    ellipsis: true,
    tooltip: { position: "left" },
  },
  {
    title: "操作",
    slotName: "operation",
  },
];

// 加载数据
const loadData = async () => {
  try {
    // 先获取用户列表
    const userRes = await Service.listUserByPageUsingPost({
      ...searchParams.value,
    });
    if (userRes.code === 0) {
      const users = userRes.data.records;
      total.value = userRes.data.total;

      // 为每个用户获取培养人信息
      const usersWithTrainer = await Promise.all(
        users.map(async (user) => {
          try {
            const trainerRes = await Service.getTrainerRelationByUserIdUsingGet(
              user.id,
            );
            if (trainerRes.code === 0 && trainerRes.data) {
              return {
                ...user,
                trainerName: trainerRes.data.trainerName,
              };
            }
            return user;
          } catch (error) {
            console.error(`获取用户${user.userName}的培养人信息失败:`, error);
            return user;
          }
        }),
      );

      dataList.value = usersWithTrainer;
    } else {
      message.error("加载失败：" + userRes.message);
    }
  } catch (error) {
    message.error("网络异常，加载失败");
    console.error(error);
  }
};

// 分页事件
const onPageChange = (page: number) => {
  searchParams.value.current = page;
  loadData(); // 分页切换时重新加载数据
};

// 搜索事件
const handlesumbit = () => {
  searchParams.value.current = 1; // 搜索时重置页码
  loadData();
};

// 详情页操作方法
const goBackList = () => {
  infoVisible.value = false;
};
const toggleEdit = () => {
  isEdit.value = !isEdit.value;
  message.info(isEdit.value ? "进入编辑模式" : "退出编辑模式");
};
const saveForm = () => {
  if (!isEdit.value) {
    message.warning("请先进入编辑模式");
    return;
  }
  // 实际项目中调用保存接口
  message.success("保存成功");
  isEdit.value = false;
};
const printInfo = () => {
  message.info("打印功能开发中...");
};
const previewFile = () => {
  message.info("预览政审材料");
};
const downloadFile = () => {
  message.info("下载政审材料成功");
};
const handleUpload = () => {
  message.success("思想汇报上传成功");
};
const submitAudit = () => {
  message.success("提交审核成功，等待管理员审批");
  infoVisible.value = false;
};
const viewChangeLog = () => {
  message.info("查看变更日志功能开发中...");
};

// 新增发展记录
const AddData = () => {
  // 这里可以打开一个模态框让用户填写培养人信息
  // 然后调用addTrainerRelationUsingPost API
  message.info("新增发展记录功能开发中...");
  // 示例代码：
  /*
  const trainerRelationData = {
    userId: 1, // 被培养人ID
    trainerId: 2, // 培养人ID
    startDate: new Date().toISOString().split('T')[0],
    status: 1 // 1.进行中
  };
  Service.addTrainerRelationUsingPost(trainerRelationData)
    .then(res => {
      if (res.code === 0) {
        message.success("新增发展记录成功");
        loadData();
      } else {
        message.error("新增失败：" + res.message);
      }
    })
    .catch(error => {
      message.error("网络异常，新增失败");
      console.error(error);
    });
  */
};
const delData = () => {
  message.info("批量导出功能开发中...");
};
const modifyData = (type: number) => {
  message.info("批量审核功能开发中...");
};

// 初始加载数据
watchEffect(() => {
  loadData();
});
</script>

<style scoped>
/* 下拉框箭头旋转 */
.arco-dropdown-open .arco-icon-down {
  transform: rotate(180deg);
  transition: transform 0.2s;
}

/* 时间轴样式优化 */
:deep(.arco-timeline-item-content) {
  padding-bottom: 24px !important;
}

/* 模态框滚动条优化 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-thumb {
  border-radius: 3px;
  background-color: #e5e6eb;
}

::-webkit-scrollbar-track {
  background-color: transparent;
}
</style>
