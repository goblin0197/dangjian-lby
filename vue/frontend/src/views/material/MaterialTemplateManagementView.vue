<template>
  <!-- 页面整体框架 -->
  <div class="material-template-page">
    <!-- 视图切换栏 -->
    <a-card style="margin-bottom: 16px">
      <a-space size="large">
        <a-button
            :status="activeView === 'template' ? 'primary' : 'normal'"
            type="primary"
            @click="switchView('template')"
        >
          <icon-file-text/>
          材料模板管理
        </a-button>
        <a-button
            :status="activeView === 'submit' ? 'primary' : 'normal'"
            @click="switchView('submit')"
        >
          <icon-upload/>
          材料提交审核
        </a-button>
        <a-button
            :status="activeView === 'archive' ? 'primary' : 'normal'"
            @click="switchView('archive')"
        >
          <icon-folder/>
          材料归档查询
        </a-button>
        <!-- 权限提示：仅管理员可见模板管理 -->
        <a-tag v-if="userRole !== 'admin'" color="orange" disabled>
          <icon-warning/>
          仅管理员可配置材料模板
        </a-tag>
      </a-space>
    </a-card>

    <!-- 材料模板管理核心区域（仅管理员可见） -->
    <a-card v-if="activeView === 'template' && userRole === 'admin'">
      <!-- 筛选+操作区 -->
      <div
          class="filter-operation-bar"
          style="
          margin-bottom: 16px;
          padding-bottom: 8px;
          border-bottom: 1px solid #eee;
        "
      >
        <a-row :gutter="16" align="middle">
          <!-- 筛选条件 -->
          <a-col :span="18">
            <a-space size="middle">
              <a-form-item label="发展阶段" label-col-flex="80px">
                <a-select
                    v-model="searchParams.stage"
                    placeholder="全部"
                    style="width: 150px"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="activist">积极分子</a-option>
                  <a-option value="developmentObject">发展对象</a-option>
                  <a-option value="probationaryPartyMember">预备党员</a-option>
                  <a-option value="formalPartyMember">正式党员</a-option>
                </a-select>
              </a-form-item>

              <a-form-item label="材料类型" label-col-flex="80px">
                <a-select
                    v-model="searchParams.type"
                    placeholder="全部"
                    style="width: 150px"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="audit">审核类</a-option>
                  <a-option value="daily">日常类</a-option>
                </a-select>
              </a-form-item>

              <a-form-item label="模板状态" label-col-flex="80px">
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
                <icon-search/>
                筛选
              </a-button>
              <a-button type="outline" @click="resetSearch">
                <icon-refresh/>
                重置
              </a-button>
            </a-space>
          </a-col>

          <!-- 操作按钮 -->
          <a-col :span="6" style="text-align: right">
            <a-space size="middle">
              <a-button type="primary" @click="openAddModal">
                <icon-plus/>
                新增模板
              </a-button>
              <a-button
                  :disabled="selectedRowKeys.length === 0"
                  status="danger"
                  type="primary"
                  @click="batchDelete"
              >
                <icon-delete/>
                批量删除
              </a-button>
              <a-dropdown @select="batchToggleStatus">
                <a-button type="dashed">
                  {{ batchStatusText }}
                  <icon-down/>
                </a-button>
                <template #content>
                  <a-doption value="enable">批量启用</a-doption>
                  <a-doption value="disable">批量停用</a-doption>
                </template>
              </a-dropdown>
            </a-space>
          </a-col>
        </a-row>
      </div>

      <!-- 材料模板列表 -->
      <a-table
          :columns="columns"
          :data="templateList"
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
        <!-- 所属阶段列自定义渲染 -->
        <template #stage="{ record }">
          <span>
            {{
              record.stage === "activist"
                  ? "积极分子"
                  : record.stage === "developmentObject"
                      ? "发展对象"
                      : record.stage === "probationaryPartyMember"
                          ? "预备党员"
                          : "正式党员"
            }}
          </span>
        </template>

        <!-- 材料类型列自定义渲染 -->
        <template #type="{ record }">
          <a-tag :color="record.type === 'audit' ? 'blue' : 'purple'">
            {{ record.type === "audit" ? "审核类" : "日常类" }}
          </a-tag>
        </template>

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
            <a-button type="text" @click="previewTemplate(record)"
            >预览
            </a-button>
            <a-button type="text" @click="downloadTemplate(record)"
            >下载
            </a-button>
            <a-button status="danger" type="text" @click="toggleStatus(record)">
              {{ record.status === "enable" ? "停用" : "启用" }}
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 非管理员提示 -->
    <a-card v-else-if="activeView === 'template' && userRole !== 'admin'">
      <div style="text-align: center; padding: 60px 0">
        <icon-warning-circle style="font-size: 48px; color: #faad14"/>
        <div style="margin-top: 16px; font-size: 16px">
          暂无权限访问此页面，请切换为管理员账号登录
        </div>
      </div>
    </a-card>

    <!-- 新增/编辑模板模态框 -->
    <a-modal
        v-model:visible="modalVisible"
        :title="isEdit ? '编辑材料模板' : '新增材料模板'"
        width="700px"
        @cancel="handleModalCancel"
        @ok="handleModalOk"
    >
      <a-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-col-flex="100px"
          wrapper-col-flex="auto"
      >
        <a-form-item field="name" label="模板名称">
          <a-input
              v-model="formData.name"
              placeholder="请输入模板名称（如：政审表、思想汇报）"
          />
        </a-form-item>

        <a-form-item field="stage" label="所属发展阶段">
          <a-select v-model="formData.stage" placeholder="请选择所属发展阶段">
            <a-option value="activist">积极分子</a-option>
            <a-option value="developmentObject">发展对象</a-option>
            <a-option value="probationaryPartyMember">预备党员</a-option>
            <a-option value="formalPartyMember">正式党员</a-option>
          </a-select>
        </a-form-item>

        <a-form-item field="type" label="材料类型">
          <a-radio-group v-model="formData.type">
            <a-radio value="audit">审核类（如政审表、转正申请书）</a-radio>
            <a-radio value="daily">日常类（如思想汇报、学习心得）</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item :required="!isEdit" field="file" label="模板文件">
          <a-upload
              v-model="formData.fileList"
              :file-list-max="1"
              :multiple="false"
              :show-file-list="true"
              accept=".docx,.doc,.pdf"
              action="/api/upload/template"
              @change="handleFileChange"
          >
            <a-button type="dashed">
              <icon-upload/>
              点击上传模板文件（Word/PDF）
            </a-button>
          </a-upload>
          <div style="margin-top: 8px; font-size: 12px; color: #86909c">
            支持格式：docx/doc/pdf，单文件大小≤20MB
          </div>
          <!-- 编辑时显示已上传文件 -->
          <div v-if="isEdit && formData.fileUrl" style="margin-top: 8px">
            <a-link @click="downloadTemplate(formData)"
            >{{ formData.name }}.pdf
            </a-link>
          </div>
        </a-form-item>

        <a-form-item field="status" label="模板状态">
          <a-radio-group v-model="formData.status">
            <a-radio value="enable">启用（对应阶段党员可见）</a-radio>
            <a-radio value="disable">停用（对应阶段党员不可见）</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item field="remark" label="模板说明">
          <a-textarea
              v-model="formData.remark"
              :rows="3"
              placeholder="请输入模板填写说明、注意事项等（可选）"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 模板预览弹窗 -->
    <a-modal
        v-model:visible="previewVisible"
        :footer="null"
        title="模板预览"
        width="800px"
        @cancel="previewVisible = false"
    >
      <div v-if="previewTemplateData" style="padding: 16px; text-align: center">
        <div style="margin-bottom: 16px; font-size: 16px; font-weight: 600">
          {{ previewTemplateData.name }}
        </div>
        <div style="border: 1px solid #eee; padding: 24px; min-height: 400px">
          <icon-file-text style="font-size: 64px; color: #ccc"/>
          <div style="margin-top: 16px; color: #86909c">
            模板文件在线预览效果（实际项目中对接PDF/Word预览组件）
          </div>
          <div style="margin-top: 24px; text-align: left; line-height: 1.8">
            {{ previewTemplateData.remark || "暂无模板说明" }}
          </div>
        </div>
        <a-button
            style="margin-top: 16px"
            type="primary"
            @click="downloadTemplate(previewTemplateData)"
        >
          <icon-download/>
          下载原文件
        </a-button>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import {reactive, ref, watchEffect} from "vue";
import message from "@arco-design/web-vue/es/message";
import type {FormInstance} from "@arco-design/web-vue/es/form";
// 导入Arco图标
import {
  IconDelete,
  IconDown,
  IconFileText,
  IconFolder,
  IconPlus,
  IconRefresh,
  IconSearch,
  IconUpload,
  IconWarning,
  IconWarningCircle,
} from "@arco-design/web-vue/es/icon";
import {useRouter} from "vue-router";
// 导入API
import * as cailiaomobanguanli from "@/api/cailiaomobanguanli";

const router = useRouter();

// 1. 基础配置
// 模拟用户角色（实际项目中从登录信息获取）
const userRole = ref("admin"); // admin/teacher/student

// 视图切换
const activeView = ref("template"); // template/submit/archive
const switchView = (view: string) => {
  activeView.value = view;
  // 其他视图可在此处跳转或加载对应页面
  if (view === "submit") {
    message.info("已切换至材料提交审核视图");
    toShowMaterialSubmissionReviewView();
  } else if (view === "archive") {
    message.info("已切换至材料归档查询视图");
    toShowMaterialArchivingQueryView();
  }
};

// 2. 搜索参数
const searchParams = reactive({
  current: 1,
  pageSize: 10,
  stage: "all", // 发展阶段
  type: "all", // 材料类型
  status: "all", // 模板状态
});
const total = ref(0);
const selectedRowKeys = ref<string[]>([]); // 表格选中行
const batchStatusText = ref("批量操作");

// 表格选择配置
const rowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
const handleSelectionChange = (keys: string[]) => {
  selectedRowKeys.value = keys;
  // 更新批量操作按钮文本
  if (keys.length === 0) {
    batchStatusText.value = "批量操作";
  } else {
    const firstStatus = templateList.value.find(
        (item) => item.id === keys[0],
    )?.status;
    const allSameStatus = keys.every(
        (key) =>
            templateList.value.find((item) => item.id === key)?.status ===
            firstStatus,
    );
    batchStatusText.value = allSameStatus
        ? firstStatus === "enable"
            ? "批量停用"
            : "批量启用"
        : "批量操作";
  }
};

// 3. 材料模板列表（模拟数据，实际从接口获取）
const templateList = ref([
  {
    id: "1",
    name: "政审表",
    stage: "developmentObject",
    type: "audit",
    status: "enable",
    fileUrl: "/templates/政审表.pdf",
    uploadTime: "2025-01-10",
    remark: "政审表需填写家庭成员信息、社会关系、无犯罪记录等，需加盖支部公章",
  },
  {
    id: "2",
    name: "思想汇报",
    stage: "activist",
    type: "daily",
    status: "enable",
    fileUrl: "/templates/思想汇报.docx",
    uploadTime: "2025-01-10",
    remark:
        "思想汇报每月提交1份，需结合当月学习内容、思想动态撰写，字数不少于800字",
  },
  {
    id: "3",
    name: "转正申请书",
    stage: "probationaryPartyMember",
    type: "audit",
    status: "enable",
    fileUrl: "/templates/转正申请书.pdf",
    uploadTime: "2025-01-10",
    remark:
        "转正申请书需在预备期满前1个月提交，需总结预备期表现、不足及改进方向",
  },
  {
    id: "4",
    name: "入党志愿书",
    stage: "formalPartyMember",
    type: "audit",
    status: "disable",
    fileUrl: "/templates/入党志愿书.pdf",
    uploadTime: "2025-01-05",
    remark: "新版入党志愿书已更新，旧版停用，待上级党委下发新版后重新启用",
  },
]);
total.value = templateList.value.length;

// 4. 表格列配置
const columns = [
  {
    title: "模板名称",
    dataIndex: "name",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "所属阶段",
    slotName: "stage",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "材料类型",
    slotName: "type",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "状态",
    slotName: "status",
  },
  {
    title: "上传时间",
    dataIndex: "uploadTime",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "操作",
    slotName: "operation",
    width: 280,
  },
];

// 5. 分页事件
const onPageChange = async (page: number) => {
  searchParams.current = page;
  await loadTemplateList();
};

// 6. 加载模板列表（调用真实接口）
const loadTemplateList = async () => {
  try {
    const res = await cailiaomobanguanli.listMaterialTemplateUsingGet({
      stage: searchParams.stage === "all" ? undefined : searchParams.stage,
      type: searchParams.type === "all" ? undefined : searchParams.type,
      status: searchParams.status === "all" ? undefined : searchParams.status,
      page: searchParams.current,
      pageSize: searchParams.pageSize,
    });
    if (res.code === 0) {
      templateList.value = res.data?.records || [];
      total.value = res.data?.total || 0;
    } else {
      message.error(res.message || "获取模板列表失败");
    }
  } catch (error) {
    console.error("获取模板列表失败:", error);
    message.error("网络请求异常");
  }
};

// 7. 筛选/重置
const handleSearch = async () => {
  searchParams.current = 1;
  await loadTemplateList();
  message.success("筛选成功");
};
const resetSearch = async () => {
  searchParams.stage = "all";
  searchParams.type = "all";
  searchParams.status = "all";
  searchParams.current = 1;
  await loadTemplateList();
};

// 8. 新增/编辑模态框
const modalVisible = ref(false);
const isEdit = ref(false);
const formRef = ref<FormInstance>();
const formData = reactive({
  id: "",
  name: "",
  stage: "",
  type: "audit",
  fileList: [] as any[],
  fileUrl: "",
  status: "enable",
  remark: "",
});
// 表单校验规则
const formRules = reactive({
  name: [{required: true, message: "请输入模板名称"}],
  stage: [{required: true, message: "请选择所属发展阶段"}],
  type: [{required: true, message: "请选择材料类型"}],
  file: [
    {
      required: (): boolean => !isEdit.value,
      message: "请上传模板文件",
    },
  ],
});

// 文件上传回调
const handleFileChange = (fileList: any[]) => {
  formData.fileList = fileList;
  // 模拟上传成功后返回文件地址
  if (fileList.length > 0 && fileList[0].status === "done") {
    formData.fileUrl = `/templates/${formData.name}.pdf`;
  }
};

// 打开新增模态框
const openAddModal = () => {
  isEdit.value = false;
  // 重置表单
  formData.id = "";
  formData.name = "";
  formData.stage = "";
  formData.type = "audit";
  formData.fileList = [];
  formData.fileUrl = "";
  formData.status = "enable";
  formData.remark = "";
  modalVisible.value = true;
};

// 打开编辑模态框
const openEditModal = (record: any) => {
  isEdit.value = true;
  // 填充表单数据
  formData.id = record.id;
  formData.name = record.name;
  formData.stage = record.stage;
  formData.type = record.type;
  formData.fileList = [];
  formData.fileUrl = record.fileUrl;
  formData.status = record.status;
  formData.remark = record.remark;
  modalVisible.value = true;
};

// 模态框确认
const handleModalOk = async () => {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
    
    if (isEdit.value) {
      // 编辑逻辑
      const res = await cailiaomobanguanli.updateMaterialTemplateUsingPut({
        id: formData.id,
        name: formData.name,
        stage: formData.stage,
        type: formData.type,
        status: formData.status,
        fileUrl: formData.fileUrl || templateList.value.find(item => item.id === formData.id)?.fileUrl,
        remark: formData.remark,
      });
      if (res.code === 0) {
        message.success("编辑模板成功");
        modalVisible.value = false;
        await loadTemplateList();
      } else {
        message.error(res.message || "编辑模板失败");
      }
    } else {
      // 新增逻辑
      const res = await cailiaomobanguanli.addMaterialTemplateUsingPost({
        name: formData.name,
        stage: formData.stage,
        type: formData.type,
        status: formData.status,
        fileUrl: formData.fileUrl,
        remark: formData.remark,
      });
      if (res.code === 0) {
        message.success("新增模板成功");
        modalVisible.value = false;
        await loadTemplateList();
      } else {
        message.error(res.message || "新增模板失败");
      }
    }
  } catch (error) {
    console.error("操作模板失败:", error);
    message.error("网络请求异常");
  }
};

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false;
  formRef.value?.resetFields();
};

// 9. 模板预览/下载
const previewVisible = ref(false);
const previewTemplateData = ref<any>(null);

// 预览模板
const previewTemplate = (record: any) => {
  previewTemplateData.value = record;
  previewVisible.value = true;
};

// 下载模板
const downloadTemplate = (record: any) => {
  message.success(`已开始下载【${record.name}】模板文件`);
  // 实际项目中调用文件下载接口
  // window.open(record.fileUrl, '_blank');
};

// 10. 状态切换（启用/停用）
const toggleStatus = async (record: any) => {
  const newStatus = record.status === "enable" ? "disable" : "enable";
  try {
    const res = await cailiaomobanguanli.toggleMaterialTemplateStatusUsingPut({
      id: record.id,
      status: newStatus,
    });
    if (res.code === 0) {
      const index = templateList.value.findIndex((item) => item.id === record.id);
      if (index > -1) {
        templateList.value[index].status = newStatus;
        message.success(
            `已${newStatus === "enable" ? "启用" : "停用"}【${record.name}】模板`,
        );
        // 更新批量操作按钮文本
        handleSelectionChange(selectedRowKeys.value);
      }
    } else {
      message.error(res.message || "切换模板状态失败");
    }
  } catch (error) {
    console.error("切换模板状态失败:", error);
    message.error("网络请求异常");
  }
};

// 批量切换状态
const batchToggleStatus = async (value: string) => {
  if (selectedRowKeys.value.length === 0) {
    message.warning("请先选择要操作的模板");
    return;
  }
  try {
    const res = await cailiaomobanguanli.batchToggleMaterialTemplateStatusUsingPut({
      ids: selectedRowKeys.value,
      status: value,
    });
    if (res.code === 0) {
      templateList.value = templateList.value.map((item) => {
        if (selectedRowKeys.value.includes(item.id)) {
          return {...item, status: value};
        }
        return item;
      });
      message.success(
          `已${value === "enable" ? "批量启用" : "批量停用"}选中的模板`,
      );
      selectedRowKeys.value = [];
      batchStatusText.value = "批量操作";
      await loadTemplateList();
    } else {
      message.error(res.message || "批量切换模板状态失败");
    }
  } catch (error) {
    console.error("批量切换模板状态失败:", error);
    message.error("网络请求异常");
  }
};

// 11. 批量删除
const batchDelete = () => {
  // 确认删除
  message.confirm({
    title: "批量删除",
    content: `确定要删除选中的${selectedRowKeys.value.length}个材料模板吗？删除后不可恢复！`,
    onOk: async () => {
      try {
        const res = await cailiaomobanguanli.batchDeleteMaterialTemplateUsingDelete({
          ids: selectedRowKeys.value,
        });
        if (res.code === 0) {
          templateList.value = templateList.value.filter(
              (item) => !selectedRowKeys.value.includes(item.id),
          );
          total.value = templateList.value.length;
          selectedRowKeys.value = [];
          batchStatusText.value = "批量操作";
          message.success("批量删除成功");
          await loadTemplateList();
        } else {
          message.error(res.message || "批量删除失败");
        }
      } catch (error) {
        console.error("批量删除失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

// 初始加载数据
watchEffect(() => {
  (async () => {
    await loadTemplateList();
  })();
});

const toShowMaterialSubmissionReviewView = () => {
  router.push("/materialSubmissionReview");
};

const toShowMaterialArchivingQueryView = () => {
  router.push("/materialArchivingQuery");
};
</script>

<style scoped>
/* 页面样式优化 */
.material-template-page {
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

/* 预览弹窗样式 */
:deep(.arco-modal-body) {
  padding: 0;
}
</style>
