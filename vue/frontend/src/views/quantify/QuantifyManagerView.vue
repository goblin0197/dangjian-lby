<template>
  <!-- 页面整体框架（延续系统风格） -->
  <div class="quantify-config-page">
    <!-- 视图切换栏 -->
    <a-card style="margin-bottom: 16px">
      <a-space size="large">
        <a-button
          type="primary"
          :status="activeView === 'config' ? 'primary' : 'normal'"
          @click="switchView('config')"
        >
          <icon-form />
          量化数据配置
        </a-button>
        <a-button
          :status="activeView === 'report' ? 'primary' : 'normal'"
          @click="switchView('report')"
        >
          <icon-chart />
          统计报表查看
        </a-button>
        <!-- 权限提示：仅管理员可见配置视图 -->
        <a-tag v-if="userRole !== 'admin'" color="orange" disabled>
          <icon-warning />
          仅管理员可配置量化指标
        </a-tag>
      </a-space>
    </a-card>

    <!-- 量化数据配置核心区域（仅管理员可见） -->
    <a-card v-if="activeView === 'config' && userRole === 'admin'">
      <!-- 筛选+操作区 -->
      <div class="filter-operation-bar" style="margin-bottom: 16px">
        <a-row :gutter="16" align="middle">
          <!-- 筛选条件 -->
          <a-col :span="18">
            <a-space size="middle">
              <a-form-item label="统计指标" label-col-flex="80px">
                <a-select
                  v-model="searchParams.indicator"
                  placeholder="全部"
                  style="width: 150px"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="activityRate">活动参与率</a-option>
                  <a-option value="signRate">签到率</a-option>
                  <a-option value="materialRate">材料完成率</a-option>
                </a-select>
              </a-form-item>

              <a-form-item label="组织层级" label-col-flex="80px">
                <a-select
                  v-model="searchParams.orgLevel"
                  placeholder="全部"
                  style="width: 150px"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="partyCommittees">党委</a-option>
                  <a-option value="branch">支部</a-option>
                </a-select>
              </a-form-item>

              <a-form-item label="数据状态" label-col-flex="80px">
                <a-select
                  v-model="searchParams.status"
                  placeholder="全部"
                  style="width: 150px"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="enable">启用</a-option>
                  <a-option value="disable">停用</a-option>
                </a-select>
              </a-form-item>

              <a-button type="outline" @click="handleSearch">
                <icon-search />
                筛选
              </a-button>
              <a-button type="outline" @click="resetSearch">
                <icon-refresh />
                重置
              </a-button>
            </a-space>
          </a-col>

          <!-- 操作按钮 -->
          <a-col :span="6" style="text-align: right">
            <a-space size="middle">
              <a-button type="primary" @click="openAddModal">
                <icon-plus />
                新增量化指标
              </a-button>
              <a-button
                type="primary"
                status="danger"
                @click="batchDelete"
                :disabled="selectedRowKeys.length === 0"
              >
                <icon-delete />
                批量删除
              </a-button>
              <a-button type="dashed" @click="importTemplate">
                <icon-upload />
                导入模板
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </div>

      <!-- 量化指标列表 -->
      <a-table
        :columns="columns"
        :data="indicatorList"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total: total,
        }"
        :row-selection="rowSelection"
        :selectedRowKeys="selectedRowKeys"
        row-key="id"
        @page-change="onPageChange"
        @selection-change="handleSelectionChange"
      >
        <!-- 状态列自定义渲染 -->
        <template #status="{ record }">
          <a-tag :color="record.status === 'enable' ? 'green' : 'orange'">
            {{ record.status === "enable" ? "启用" : "停用" }}
          </a-tag>
        </template>

        <!-- 操作列 -->
        <template #operation="{ record }">
          <a-space wrap>
            <a-button type="text" @click="openEditModal(record)">编辑</a-button>
            <a-button type="text" status="danger" @click="toggleStatus(record)">
              {{ record.status === "enable" ? "停用" : "启用" }}
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 非管理员提示 -->
    <a-card v-else-if="activeView === 'config' && userRole !== 'admin'">
      <div style="text-align: center; padding: 60px 0">
        <icon-warning-circle style="font-size: 48px; color: #faad14" />
        <div style="margin-top: 16px; font-size: 16px">
          暂无权限访问此页面，请切换为管理员账号登录
        </div>
      </div>
    </a-card>

    <!-- 新增/编辑量化指标模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? '编辑量化指标' : '新增量化指标'"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
      width="600px"
    >
      <a-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-col-flex="100px"
        wrapper-col-flex="auto"
      >
        <a-form-item field="name" label="指标名称">
          <a-input
            v-model="formData.name"
            placeholder="请输入指标名称（如：活动参与率）"
          />
        </a-form-item>

        <a-form-item field="rule" label="统计规则">
          <a-input
            v-model="formData.rule"
            placeholder="请输入统计规则（如：参与人数/报名人数）"
          />
          <div style="margin-top: 8px; font-size: 12px; color: #86909c">
            示例：参与人数/报名人数、签到人数/应到人数、已提交材料/需提交材料
          </div>
        </a-form-item>

        <a-form-item field="dimension" label="所属维度">
          <a-radio-group v-model="formData.dimension">
            <a-radio value="organization">组织（党委/支部）</a-radio>
            <a-radio value="personal">个人</a-radio>
            <a-radio value="both">组织+个人</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item field="orgLevel" label="适用组织层级">
          <a-select
            v-model="formData.orgLevel"
            placeholder="请选择适用组织层级"
            mode="multiple"
          >
            <a-option value="partyCommittees">党委</a-option>
            <a-option value="branch">支部</a-option>
          </a-select>
        </a-form-item>

        <a-form-item field="status" label="状态">
          <a-radio-group v-model="formData.status">
            <a-radio value="enable">启用</a-radio>
            <a-radio value="disable">停用</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import type { FormInstance } from "@arco-design/web-vue/es/form";
// 导入Arco图标
import {
  IconChart,
  IconDelete,
  IconForm,
  IconPlus,
  IconRefresh,
  IconSearch,
  IconUpload,
  IconWarning,
  IconWarningCircle,
} from "@arco-design/web-vue/es/icon";
import { useRouter } from "vue-router";

const router = useRouter();

// 模拟用户角色（实际项目中从登录信息获取）
const userRole = ref("admin"); // admin/teacher/student

// 视图切换
const activeView = ref("config"); // config/report
const switchView = (view: string) => {
  activeView.value = view;
  // 报表视图可在此处跳转或加载报表页面
  if (view === "report") {
    message.info("已切换至统计报表查看视图");
  }
  toShowQuantifyView();
};

// 搜索参数
const searchParams = reactive({
  current: 1,
  pageSize: 10,
  indicator: "all", // 统计指标
  orgLevel: "all", // 组织层级
  status: "all", // 数据状态
});
const total = ref(0);
const selectedRowKeys = ref<string[]>([]); // 表格选中行

// 表格选择配置
const rowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
const handleSelectionChange = (keys: string[]) => {
  selectedRowKeys.value = keys;
};

// 量化指标列表（模拟数据，实际从接口获取）
const indicatorList = ref([
  {
    id: "1",
    name: "活动参与率",
    rule: "参与人数/报名人数",
    dimension: "both",
    dimensionText: "组织/个人",
    orgLevel: ["partyCommittees", "branch"],
    status: "enable",
    updateTime: "2025-02-01",
  },
  {
    id: "2",
    name: "签到率",
    rule: "签到人数/应到人数",
    dimension: "both",
    dimensionText: "组织/个人",
    orgLevel: ["partyCommittees", "branch"],
    status: "enable",
    updateTime: "2025-02-01",
  },
  {
    id: "3",
    name: "材料完成率",
    rule: "已提交材料/需提交材料",
    dimension: "personal",
    dimensionText: "个人",
    orgLevel: ["branch"],
    status: "enable",
    updateTime: "2025-02-01",
  },
  {
    id: "4",
    name: "支部活动完成率",
    rule: "已开展活动/计划活动数",
    dimension: "organization",
    dimensionText: "组织",
    orgLevel: ["branch"],
    status: "disable",
    updateTime: "2025-01-15",
  },
]);
total.value = indicatorList.value.length;

// 表格列配置
const columns = [
  {
    title: "指标名称",
    dataIndex: "name",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "统计规则",
    dataIndex: "rule",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "所属维度",
    dataIndex: "dimensionText",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "状态",
    slotName: "status",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "操作",
    slotName: "operation",
    width: 180,
  },
];

// 分页事件
const onPageChange = (page: number) => {
  searchParams.current = page;
  // 实际项目中调用接口加载对应页数据
  loadIndicatorList();
};

// 加载量化指标列表（模拟接口调用）
const loadIndicatorList = () => {
  // 此处可根据searchParams筛选数据，模拟接口逻辑
  const filteredList = indicatorList.value.filter((item) => {
    const indicatorMatch =
      searchParams.indicator === "all" ||
      item.name.includes(
        searchParams.indicator === "activityRate"
          ? "活动参与率"
          : searchParams.indicator === "signRate"
          ? "签到率"
          : "材料完成率"
      );
    const orgLevelMatch =
      searchParams.orgLevel === "all" ||
      item.orgLevel.includes(searchParams.orgLevel);
    const statusMatch =
      searchParams.status === "all" || item.status === searchParams.status;
    return indicatorMatch && orgLevelMatch && statusMatch;
  });
  indicatorList.value = filteredList;
  total.value = filteredList.length;
};

// 筛选/重置
const handleSearch = () => {
  searchParams.current = 1;
  loadIndicatorList();
  message.success("筛选成功");
};
const resetSearch = () => {
  searchParams.indicator = "all";
  searchParams.orgLevel = "all";
  searchParams.status = "all";
  searchParams.current = 1;
  loadIndicatorList();
};

// 新增/编辑模态框
const modalVisible = ref(false);
const isEdit = ref(false);
const formRef = ref<FormInstance>();
const formData = reactive({
  id: "",
  name: "",
  rule: "",
  dimension: "both",
  orgLevel: [] as string[],
  status: "enable",
});
// 表单校验规则
const formRules = reactive({
  name: [{ required: true, message: "请输入指标名称" }],
  rule: [{ required: true, message: "请输入统计规则" }],
  dimension: [{ required: true, message: "请选择所属维度" }],
  orgLevel: [{ required: true, message: "请选择适用组织层级" }],
});

// 打开新增模态框
const openAddModal = () => {
  isEdit.value = false;
  // 重置表单
  formData.id = "";
  formData.name = "";
  formData.rule = "";
  formData.dimension = "both";
  formData.orgLevel = [];
  formData.status = "enable";
  modalVisible.value = true;
};

// 打开编辑模态框
const openEditModal = (record: any) => {
  isEdit.value = true;
  // 填充表单数据
  formData.id = record.id;
  formData.name = record.name;
  formData.rule = record.rule;
  formData.dimension = record.dimension;
  formData.orgLevel = record.orgLevel;
  formData.status = record.status;
  modalVisible.value = true;
};

// 模态框确认
const handleModalOk = async () => {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
    // 模拟接口调用
    if (isEdit.value) {
      // 编辑逻辑
      const index = indicatorList.value.findIndex(
        (item) => item.id === formData.id
      );
      if (index > -1) {
        indicatorList.value[index] = {
          ...indicatorList.value[index],
          name: formData.name,
          rule: formData.rule,
          dimension: formData.dimension,
          dimensionText:
            formData.dimension === "organization"
              ? "组织"
              : formData.dimension === "personal"
              ? "个人"
              : "组织/个人",
          orgLevel: formData.orgLevel,
          status: formData.status,
          updateTime: new Date().toLocaleDateString().replace(/\//g, "-"),
        };
        message.success("编辑成功");
      }
    } else {
      // 新增逻辑
      const newId = (Number(indicatorList.value.at(-1)?.id) + 1).toString();
      indicatorList.value.push({
        id: newId,
        name: formData.name,
        rule: formData.rule,
        dimension: formData.dimension,
        dimensionText:
          formData.dimension === "organization"
            ? "组织"
            : formData.dimension === "personal"
            ? "个人"
            : "组织/个人",
        orgLevel: formData.orgLevel,
        status: formData.status,
        updateTime: new Date().toLocaleDateString().replace(/\//g, "-"),
      });
      total.value = indicatorList.value.length;
      message.success("新增成功");
    }
    modalVisible.value = false;
    loadIndicatorList();
  } catch (error) {
    message.error("表单校验失败，请检查必填项");
  }
};

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false;
  formRef.value?.resetFields();
};

// 切换状态（启用/停用）
const toggleStatus = (record: any) => {
  const newStatus = record.status === "enable" ? "disable" : "enable";
  // 模拟接口调用
  const index = indicatorList.value.findIndex((item) => item.id === record.id);
  if (index > -1) {
    indicatorList.value[index].status = newStatus;
    message.success(
      `已${newStatus === "enable" ? "启用" : "停用"}【${record.name}】指标`
    );
  }
};

// 批量删除
const batchDelete = () => {
  // 确认删除
  message.confirm({
    title: "批量删除",
    content: `确定要删除选中的${selectedRowKeys.value.length}个量化指标吗？删除后不可恢复！`,
    onOk: () => {
      // 模拟删除逻辑
      indicatorList.value = indicatorList.value.filter(
        (item) => !selectedRowKeys.value.includes(item.id)
      );
      total.value = indicatorList.value.length;
      selectedRowKeys.value = [];
      message.success("批量删除成功");
    },
  });
};

// 导入模板
const importTemplate = () => {
  message.info("导入模板功能开发中，可对接Excel导入接口实现");
};

// 初始加载数据
watchEffect(() => {
  loadIndicatorList();
});

const toShowQuantifyView = () => {
  router.push("/quantify");
};
</script>

<style scoped>
/* 页面样式优化 */
.quantify-config-page {
  padding: 16px;
}

.filter-operation-bar {
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

/* 表格样式适配 */
:deep(.arco-table) {
  --arco-table-header-text-color: #1d2129;
  --arco-table-body-text-color: #4e5969;
}

/* 模态框表单样式 */
:deep(.arco-form-item) {
  margin-bottom: 16px;
}
</style>
