<template>
  <div class="quantify-manager-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>量化管理</a-breadcrumb-item>
          <a-breadcrumb-item>{{
            activeView === "config" ? "指标配置" : "统计报表"
          }}</a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <h1>量化管理</h1>
    </div>

    <!-- 视图切换栏 -->
    <div class="view-switch-bar">
      <a-space size="large">
        <a-button
          :status="activeView === 'config' ? 'primary' : 'normal'"
          type="primary"
          @click="switchView('config')"
        >
          <icon-form />
          量化指标配置
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
    </div>

    <!-- 量化数据配置核心区域（仅管理员可见） -->
    <a-card
      v-if="activeView === 'config' && userRole === 'admin'"
      class="list-card"
      hoverable
    >
      <!-- 筛选+操作区 -->
      <div class="filter-operation-bar" style="margin-bottom: 16px">
        <a-row :gutter="16" align="middle">
          <!-- 筛选条件 -->
          <a-col :span="18">
            <a-form :model="searchParams" class="filter-form" layout="inline">
              <a-form-item label="数据状态">
                <a-select
                  v-model="searchParams.status"
                  placeholder="全部"
                  style="width: 150px"
                  @change="handleSearch"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="enable">启用</a-option>
                  <a-option value="disable">停用</a-option>
                </a-select>
              </a-form-item>

              <a-form-item>
                <a-button type="primary" @click="handleSearch">
                  <icon-search />
                  筛选
                </a-button>
                <a-button @click="resetSearch">
                  <icon-refresh />
                  重置
                </a-button>
              </a-form-item>
            </a-form>
          </a-col>

          <!-- 操作按钮 -->
          <a-col :span="6" style="text-align: right">
            <a-space size="middle">
              <a-button type="primary" @click="openAddModal">
                <icon-plus />
                新增量化指标
              </a-button>
              <a-button
                :disabled="selectedRowKeys.length === 0"
                status="danger"
                type="primary"
                @click="batchDelete"
              >
                <icon-delete />
                批量删除
              </a-button>
              <a-button type="dashed" @click="downloadTemplate">
                <icon-download />
                下载模板
              </a-button>
              <a-button type="dashed" @click="importTemplate">
                <icon-upload />
                导入数据
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </div>

      <!-- 量化指标列表 -->
      <template #loading>
        <div style="display: flex; justify-content: center; padding: 40px">
          <a-spin size="large" tip="加载中..." />
        </div>
      </template>
      <a-table
        :columns="columns"
        :data="indicatorList"
        :loading="loading"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total: total,
          showSizeChanger: true,
          onShowSizeChange: onSizeChange,
        }"
        :row-selection="rowSelection"
        :selectedRowKeys="selectedRowKeys"
        border
        pagination-position="bottom"
        row-key="id"
        @page-change="onPageChange"
        @selection-change="handleSelectionChange"
      >
        <!-- 状态列自定义渲染 -->
        <template #status="{ record }">
          <a-tag :color="record.status === 'enable' ? 'success' : 'warning'">
            {{ record.status === "enable" ? "启用" : "停用" }}
          </a-tag>
        </template>

        <!-- 操作列 -->
        <template #operation="{ record }">
          <a-space wrap>
            <a-button
              size="small"
              type="primary"
              @click="openEditModal(record)"
            >
              <icon-edit />
              编辑
            </a-button>
            <a-button
              size="small"
              :status="record.status === 'enable' ? 'danger' : 'success'"
              type="primary"
              @click="toggleStatus(record)"
            >
              <icon-switch />
              {{ record.status === "enable" ? "停用" : "启用" }}
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 非管理员提示 -->
    <a-card
      v-else-if="activeView === 'config' && userRole !== 'admin'"
      class="list-card"
      hoverable
    >
      <div style="text-align: center; padding: 60px 0">
        <icon-warning-circle style="font-size: 48px; color: #faad14" />
        <div style="margin-top: 16px; font-size: 16px; color: #666">
          暂无权限访问此页面，请切换为管理员账号登录
        </div>
      </div>
    </a-card>

    <!-- 新增/编辑量化指标模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :ok-loading="buttonLoading.save"
      :title="isEdit ? '编辑量化指标' : '新增量化指标'"
      width="600px"
      @cancel="handleModalCancel"
      @ok="handleModalOk"
    >
      <a-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        layout="vertical"
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
            <a-radio value="organization">组织</a-radio>
            <a-radio value="personal">个人</a-radio>
            <a-radio value="both">两者</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item field="orgLevel" label="适用组织层级">
          <a-select
            v-model="formData.orgLevel"
            mode="multiple"
            placeholder="请选择适用组织层级"
          >
            <a-option value="党委">党委</a-option>
            <a-option value="党总支">党总支</a-option>
            <a-option value="党支部">党支部</a-option>
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

    <!-- 批量删除模态框 -->
    <a-modal
      v-model:visible="batchDeleteModalVisible"
      title="批量删除"
      width="400px"
      @cancel="() => (batchDeleteModalVisible = false)"
      @ok="confirmBatchDelete"
    >
      <div style="padding: 20px 0">
        <p style="color: #666">
          确定要删除选中的{{
            selectedRowKeys.length
          }}个量化指标吗？删除后不可恢复！
        </p>
      </div>
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
  IconDownload,
  IconEdit,
  IconForm,
  IconPlus,
  IconRefresh,
  IconSearch,
  IconSwitch,
  IconUpload,
  IconWarning,
  IconWarningCircle,
} from "@arco-design/web-vue/es/icon";
import { useRouter } from "vue-router";
// 导入API
import * as lianghuazhibiao from "@/api/lianghuazhibiao";

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
  status: "all", // 数据状态
});
const total = ref(0);
const selectedRowKeys = ref<string[]>([]); // 表格选中行

// 加载状态
const loading = ref(false);

// 按钮加载状态
const buttonLoading = reactive({
  save: false,
  delete: false,
  import: false,
});

// 表格选择配置
const rowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
const handleSelectionChange = (keys: string[]) => {
  selectedRowKeys.value = keys;
};

// 量化指标列表（从接口获取）
const indicatorList = ref<any[]>([]);

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
    title: "组织层级",
    dataIndex: "orgLevelText",
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
const onPageChange = async (page: number) => {
  searchParams.current = page;
  // 实际项目中调用接口加载对应页数据
  await loadIndicatorList();
};

// 分页大小变化
const onSizeChange = (pageSize: number) => {
  searchParams.pageSize = pageSize;
  searchParams.current = 1;
  loadIndicatorList();
};

// 加载量化指标列表（调用真实API）
const loadIndicatorList = async () => {
  try {
    loading.value = true;
    const res = await lianghuazhibiao.listIndicatorUsingGet({
      current: searchParams.current,
      pageSize: searchParams.pageSize,
      status: searchParams.status === "all" ? undefined : searchParams.status,
    });
    // 注意：res 是整个响应对象，res.data 才是响应体
    const responseData = res.data;
    if (responseData.code === 0) {
      // 处理返回数据，添加 dimensionText 和 orgLevelText 字段
      indicatorList.value = (responseData.data || []).map((item: any) => ({
        ...item,
        dimensionText:
          item.dimension === "both"
            ? "两者"
            : item.dimension === "organization"
            ? "组织"
            : "个人",
        orgLevelText: item.orgLevel,
      }));
      total.value = responseData.data?.length || 0;
    } else {
      message.error(responseData.message || "获取量化指标列表失败");
    }
  } catch (error) {
    console.error("获取量化指标列表失败:", error);
    message.error("网络请求异常");
  } finally {
    loading.value = false;
  }
};

// 筛选/重置
const handleSearch = async () => {
  searchParams.current = 1;
  await loadIndicatorList();
  message.success("筛选成功");
};
const resetSearch = async () => {
  searchParams.status = "all";
  searchParams.current = 1;
  await loadIndicatorList();
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
  // 确保 orgLevel 是数组格式
  formData.orgLevel = Array.isArray(record.orgLevel)
    ? record.orgLevel
    : [record.orgLevel];
  formData.status = record.status;
  modalVisible.value = true;
};

// 模态框确认
const handleModalOk = async () => {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
    buttonLoading.save = true;

    if (isEdit.value) {
      // 编辑量化指标
      const res = await lianghuazhibiao.updateIndicatorUsingPut({
        id: formData.id,
        name: formData.name,
        rule: formData.rule,
        dimension: formData.dimension,
        orgLevel: formData.orgLevel.join(","),
        status: formData.status,
      });
      const responseData = res.data;
      if (responseData.code === 0) {
        message.success("编辑成功");
        modalVisible.value = false;
        await loadIndicatorList();
      } else {
        message.error(responseData.message || "编辑失败");
      }
    } else {
      // 新增量化指标
      const res = await lianghuazhibiao.addIndicatorUsingPost({
        name: formData.name,
        rule: formData.rule,
        dimension: formData.dimension,
        orgLevel: formData.orgLevel.join(","),
        status: formData.status,
      });
      const responseData = res.data;
      if (responseData.code === 0) {
        message.success("新增成功");
        modalVisible.value = false;
        await loadIndicatorList();
      } else {
        message.error(responseData.message || "新增失败");
      }
    }
  } catch (error) {
    console.error("操作量化指标失败:", error);
    message.error("网络请求异常");
  } finally {
    buttonLoading.save = false;
  }
};

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false;
  formRef.value?.resetFields();
};

// 切换状态（启用/停用）
const toggleStatus = async (record: any) => {
  const newStatus = record.status === "enable" ? "disable" : "enable";
  try {
    loading.value = true;
    const res = await lianghuazhibiao.toggleStatusUsingPut({
      id: record.id,
      status: newStatus,
    });
    const responseData = res.data;
    if (responseData.code === 0) {
      message.success(
        `已${newStatus === "enable" ? "启用" : "停用"}【${record.name}】指标`,
      );
      await loadIndicatorList();
    } else {
      message.error(responseData.message || "切换状态失败");
    }
  } catch (error) {
    console.error("切换状态失败:", error);
    message.error("网络请求异常");
  } finally {
    loading.value = false;
  }
};

// 批量删除模态框
const batchDeleteModalVisible = ref(false);

// 批量删除
const batchDelete = () => {
  // 确保 selectedRowKeys 不为空
  if (selectedRowKeys.value.length === 0) {
    message.warning("请先选择要删除的量化指标");
    return;
  }
  // 打开批量删除模态框
  batchDeleteModalVisible.value = true;
};

// 执行批量删除
const confirmBatchDelete = async () => {
  try {
    buttonLoading.delete = true;
    const res = await lianghuazhibiao.batchDeleteIndicatorsUsingPost(
      selectedRowKeys.value.map(Number),
    );
    const responseData = res.data;
    if (responseData.code === 0) {
      message.success("批量删除成功");
      selectedRowKeys.value = [];
      await loadIndicatorList();
    } else {
      message.error(responseData.message || "批量删除失败");
    }
  } catch (error) {
    console.error("批量删除失败:", error);
    message.error("网络请求异常");
  } finally {
    // 关闭模态框
    batchDeleteModalVisible.value = false;
    buttonLoading.delete = false;
  }
};

// 下载导入模板
const downloadTemplate = () => {
  // 创建模板数据
  const templateData = [
    ["指标名称", "统计规则", "所属维度", "适用组织层级", "状态"],
    ["活动参与率", "参与人数/报名人数", "组织", "党委,党总支,党支部", "启用"],
    ["签到率", "签到人数/应到人数", "个人", "党委,党总支,党支部", "启用"],
    [
      "材料完成率",
      "已提交材料/需提交材料",
      "两者",
      "党委,党总支,党支部",
      "启用",
    ],
  ];

  // 转换为CSV格式
  const csvContent = templateData.map((row) => row.join(",")).join("\n");

  // 创建Blob对象
  const blob = new Blob(["\ufeff" + csvContent], {
    type: "text/csv;charset=utf-8;",
  });

  // 创建下载链接
  const link = document.createElement("a");
  const url = URL.createObjectURL(blob);
  link.setAttribute("href", url);
  link.setAttribute("download", "量化指标导入模板.csv");
  link.style.visibility = "hidden";
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);

  message.success("模板下载成功");
};

// 导入模板
const importTemplate = () => {
  // 创建文件输入框
  const input = document.createElement("input");
  input.type = "file";
  input.accept = ".xlsx,.xls";
  input.onchange = async (e) => {
    const file = (e.target as HTMLInputElement).files?.[0];
    if (file) {
      try {
        buttonLoading.import = true;
        const res =
          await lianghuazhibiao.importQuantifyIndicatorTemplateUsingPost(
            {},
            file,
          );
        const responseData = res.data;
        if (responseData.code === 0) {
          message.success("导入模板成功");
          await loadIndicatorList();
        } else {
          message.error(responseData.message || "导入模板失败");
        }
      } catch (error) {
        console.error("导入模板失败:", error);
        message.error("网络请求异常");
      } finally {
        buttonLoading.import = false;
      }
    }
  };
  input.click();
};

// 初始加载数据
watchEffect(async () => {
  await loadIndicatorList();
});

const toShowQuantifyView = () => {
  router.push("/quantify");
};
</script>

<style scoped>
/* 页面样式优化 */
.quantify-manager-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

/* 页面标题和面包屑 */
.page-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin: 0;
  margin-top: 8px;
}

.breadcrumb {
  margin-bottom: 8px;
}

/* 视图切换栏 */
.view-switch-bar {
  margin-bottom: 20px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 筛选表单 */
.filter-form {
  padding: 0;
}

/* 列表卡片 */
.list-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .quantify-manager-container {
    padding: 10px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .view-switch-bar {
    padding: 12px;
  }

  .filter-form {
    padding: 12px;
  }

  .a-table {
    font-size: 12px;
  }

  .a-table-column {
    min-width: 100px;
  }
}
</style>
