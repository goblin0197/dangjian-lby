<template>
  <!-- 页面整体框架 -->
  <div class="material-archive-page">
    <!-- 视图切换栏 -->
    <a-card style="margin-bottom: 16px">
      <a-space size="large">
        <a-button
            :disabled="userRole !== 'admin'"
            :status="activeView === 'template' ? 'primary' : 'normal'"
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
            type="primary"
            @click="switchView('archive')"
        >
          <icon-folder/>
          材料归档查询
        </a-button>
      </a-space>
    </a-card>

    <!-- 材料归档/查询核心区域（全角色可见） -->
    <a-card>
      <!-- 精准筛选区 -->
      <div
          class="filter-bar"
          style="
          margin-bottom: 20px;
          padding-bottom: 16px;
          border-bottom: 1px solid #eee;
        "
      >
        <a-row :gutter="16" align="middle">
          <a-col :span="6">
            <a-form-item label="姓名" label-col-flex="60px">
              <a-input
                  v-model="filterParams.userName"
                  allow-clear
                  placeholder="请输入党员姓名（模糊查询）"
                  @change="refreshData"
              >
                <template #prefix>
                  <icon-search/>
                </template>
              </a-input>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="材料名称" label-col-flex="80px">
              <a-select
                  v-model="filterParams.materialName"
                  allow-clear
                  style="width: 100%"
                  @change="refreshData"
              >
                <a-option value="all">全部</a-option>
                <a-option value="思想汇报">思想汇报</a-option>
                <a-option value="政审表">政审表</a-option>
                <a-option value="转正申请书">转正申请书</a-option>
                <a-option value="入党志愿书">入党志愿书</a-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="所属阶段" label-col-flex="80px">
              <a-select
                  v-model="filterParams.stage"
                  allow-clear
                  style="width: 100%"
                  @change="refreshData"
              >
                <a-option value="all">全部</a-option>
                <a-option value="activist">积极分子</a-option>
                <a-option value="developmentObject">发展对象</a-option>
                <a-option value="probationaryPartyMember">预备党员</a-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="归档时间" label-col-flex="80px">
              <a-range-picker
                  v-model="filterParams.archiveTime"
                  style="width: 100%"
                  @change="refreshData"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 操作按钮 -->
        <a-row style="margin-top: 8px">
          <a-col :span="24" style="text-align: right">
            <a-space size="middle">
              <a-button type="outline" @click="resetFilter">
                <icon-refresh/>
                重置筛选
              </a-button>
              <a-button
                  :disabled="archiveList.length === 0"
                  type="primary"
                  @click="batchExport"
              >
                <icon-download/>
                批量导出
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </div>

      <!-- 归档统计区 -->
      <div class="archive-stat" style="margin-bottom: 24px">
        <a-row :gutter="24">
          <!-- 归档总数卡片 -->
          <a-col :span="6">
            <a-card class="stat-card" hoverable>
              <div class="stat-title">📚 归档材料总数</div>
              <div class="stat-value">{{ archiveStat.totalCount }}</div>
              <div class="stat-desc">
                较上月
                <span class="stat-trend up"
                >↑{{ archiveStat.monthGrowth }}%</span
                >
              </div>
            </a-card>
          </a-col>

          <!-- 各阶段占比饼图 -->
          <a-col :span="18">
            <a-card>
              <template #title> 各发展阶段归档材料占比</template>
              <div ref="chartRef" style="width: 100%; height: 200px"></div>
            </a-card>
          </a-col>
        </a-row>
      </div>

      <!-- 归档材料列表 -->
      <a-table
          :columns="tableColumns"
          :data="archiveList"
          :loading="loading"
          :pagination="{
          showTotal: true,
          pageSize: 10,
          current: 1,
          total: archiveList.length,
        }"
          row-key="id"
      >
        <!-- 所属支部列自定义渲染 -->
        <template #orgName="{ record }">
          <span>
            {{
              record.orgLevel === "branch1"
                  ? "教师一支部"
                  : record.orgLevel === "branch2"
                      ? "教师二支部"
                      : record.orgLevel === "branch3"
                          ? "学生一支部"
                          : "学生二支部"
            }}
          </span>
        </template>

        <!-- 发展阶段列自定义渲染 -->
        <template #stage="{ record }">
          <span>
            {{
              record.stage === "activist"
                  ? "积极分子"
                  : record.stage === "developmentObject"
                      ? "发展对象"
                      : "预备党员"
            }}
          </span>
        </template>

        <!-- 操作列 -->
        <template #operation="{ record }">
          <a-space wrap>
            <a-button type="text" @click="viewArchive(record)">查看</a-button>
            <a-button type="text" @click="downloadArchive(record)"
            >下载
            </a-button>
            <a-button
                v-if="userRole === 'admin'"
                status="primary"
                type="text"
                @click="exportSingle(record)"
            >
              导出
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 归档详情弹窗 -->
    <a-modal
        v-model:visible="detailModalVisible"
        :footer="null"
        title="归档材料详情"
        width="700px"
        @cancel="detailModalVisible = false"
    >
      <div v-if="currentArchive" class="archive-detail">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item :span="2" label="姓名">
            {{ currentArchive.userName }}
          </a-descriptions-item>
          <a-descriptions-item label="所属支部">
            {{
              currentArchive.orgLevel === "branch1"
                  ? "教师一支部"
                  : currentArchive.orgLevel === "branch2"
                      ? "教师二支部"
                      : currentArchive.orgLevel === "branch3"
                          ? "学生一支部"
                          : "学生二支部"
            }}
          </a-descriptions-item>
          <a-descriptions-item label="发展阶段">
            {{
              currentArchive.stage === "activist"
                  ? "积极分子"
                  : currentArchive.stage === "developmentObject"
                      ? "发展对象"
                      : "预备党员"
            }}
          </a-descriptions-item>
          <a-descriptions-item :span="2" label="材料名称">
            {{ currentArchive.materialName }}
          </a-descriptions-item>
          <a-descriptions-item label="提交时间">
            {{ currentArchive.uploadTime }}
          </a-descriptions-item>
          <a-descriptions-item label="审核人">
            {{ currentArchive.auditor }}
          </a-descriptions-item>
          <a-descriptions-item label="审核时间">
            {{ currentArchive.auditTime }}
          </a-descriptions-item>
          <a-descriptions-item label="归档人">
            {{ currentArchive.archiveUser }}
          </a-descriptions-item>
          <a-descriptions-item :span="2" label="归档时间">
            {{ currentArchive.archiveTime }}
          </a-descriptions-item>
          <a-descriptions-item :span="2" label="归档备注">
            {{ currentArchive.archiveRemark || "无" }}
          </a-descriptions-item>
        </a-descriptions>

        <div style="margin-top: 20px; text-align: center">
          <a-button type="primary" @click="downloadArchive(currentArchive)">
            <icon-download/>
            下载材料文件
          </a-button>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, onUnmounted, reactive, ref, watchEffect} from "vue";
import * as echarts from "echarts";
import message from "@arco-design/web-vue/es/message";
// 导入Arco图标
import {
  IconDownload,
  IconFileText,
  IconFolder,
  IconRefresh,
  IconSearch,
  IconUpload,
} from "@arco-design/web-vue/es/icon";
import {useRouter} from "vue-router";
// 导入API
import * as cailiaoguidangchaxun from "@/api/cailiaoguidangchaxun";

const router = useRouter();

// 1. 基础配置
// 模拟用户角色（实际从登录态获取：admin/teacher/student）
const userRole = ref("student"); // 可切换测试：admin/teacher/student

// 视图切换
const activeView = ref("archive"); // template/submit/archive
const switchView = (view: string) => {
  activeView.value = view;
  if (view === "template") {
    message.info("已切换至材料模板管理视图（仅管理员可见）");
    toShowMaterialTemplateManagementView();
  } else if (view === "submit") {
    message.info("已切换至材料提交审核视图");
    toShowMaterialSubmissionReviewView();
  }
};

// 2. 筛选参数
const filterParams = reactive({
  userName: "", // 姓名模糊查询
  materialName: "all", // 材料名称
  stage: "all", // 发展阶段
  archiveTime: [] as any[], // 归档时间范围
});

// 3. 加载状态
const loading = ref(false);

// 4. 原始归档数据（模拟全量数据）
const rawArchiveList = ref([
  {
    id: "1",
    userName: "张三（教师党员）",
    orgLevel: "branch1",
    stage: "probationaryPartyMember",
    materialName: "转正申请书",
    uploadTime: "2025-02-15",
    auditor: "赵六（培养联系人）",
    auditTime: "2025-02-20",
    archiveUser: "管理员-王五",
    archiveTime: "2025-02-25",
    archiveRemark: "转正材料齐全，符合归档要求",
    fileUrl: "/archive/张三-转正申请书.pdf",
  },
  {
    id: "2",
    userName: "李四（教师党员）",
    orgLevel: "branch1",
    stage: "activist",
    materialName: "思想汇报",
    uploadTime: "2025-01-20",
    auditor: "赵六（培养联系人）",
    auditTime: "2025-01-25",
    archiveUser: "管理员-王五",
    archiveTime: "2025-01-30",
    archiveRemark: "月度思想汇报，内容完整",
    fileUrl: "/archive/李四-思想汇报.pdf",
  },
  {
    id: "3",
    userName: "赵六（教师党员）",
    orgLevel: "branch2",
    stage: "developmentObject",
    materialName: "政审表",
    uploadTime: "2025-01-18",
    auditor: "钱七（培养联系人）",
    auditTime: "2025-01-22",
    archiveUser: "管理员-王五",
    archiveTime: "2025-01-28",
    archiveRemark: "政审材料完整，无异常记录",
    fileUrl: "/archive/赵六-政审表.pdf",
  },
  {
    id: "4",
    userName: "王五（学生党员）",
    orgLevel: "branch3",
    stage: "developmentObject",
    materialName: "政审表",
    uploadTime: "2025-02-10",
    auditor: "孙八（培养联系人）",
    auditTime: "2025-02-18",
    archiveUser: "管理员-王五",
    archiveTime: "2025-02-20",
    archiveRemark: "学生政审材料，学校审核通过",
    fileUrl: "/archive/王五-政审表.pdf",
  },
  {
    id: "5",
    userName: "王五（学生党员）",
    orgLevel: "branch3",
    stage: "activist",
    materialName: "思想汇报",
    uploadTime: "2025-01-10",
    auditor: "孙八（培养联系人）",
    auditTime: "2025-01-15",
    archiveUser: "管理员-王五",
    archiveTime: "2025-01-20",
    archiveRemark: "学生思想汇报，符合要求",
    fileUrl: "/archive/王五-思想汇报.pdf",
  },
]);

// 权限过滤后的归档列表
const archiveList = ref<any[]>([]);

// 5. 归档统计数据
const archiveStat = reactive({
  totalCount: 0, // 总数
  monthGrowth: 12.5, // 月度增长率
  stageData: {
    activist: 0, // 积极分子
    developmentObject: 0, // 发展对象
    probationaryPartyMember: 0, // 预备党员
  },
});

// 6. 表格列配置
const tableColumns = ref([
  {
    title: "姓名",
    dataIndex: "userName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "所属支部",
    slotName: "orgName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "发展阶段",
    slotName: "stage",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "材料名称",
    dataIndex: "materialName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "归档时间",
    dataIndex: "archiveTime",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "操作",
    slotName: "operation",
    width: 200,
  },
]);

// 7. 图表配置
const chartRef = ref<HTMLDivElement>();
let chartInstance: echarts.ECharts | null = null;

// 8. 详情弹窗
const detailModalVisible = ref(false);
const currentArchive = ref<any>(null);

// 9. 核心方法
// 初始化图表
const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value);
    updateChart();
  }
};

// 更新图表数据
const updateChart = () => {
  if (!chartInstance) return;

  const option = {
    tooltip: {trigger: "item"},
    legend: {
      orient: "horizontal",
      bottom: 0,
      left: "center",
    },
    series: [
      {
        name: "归档材料占比",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        label: {
          show: true,
          position: "inside",
          formatter: "{b}: {c} ({d}%)",
        },
        data: [
          {
            value: archiveStat.stageData.activist,
            name: "积极分子",
            itemStyle: {color: "#165dff"},
          },
          {
            value: archiveStat.stageData.developmentObject,
            name: "发展对象",
            itemStyle: {color: "#00b42a"},
          },
          {
            value: archiveStat.stageData.probationaryPartyMember,
            name: "预备党员",
            itemStyle: {color: "#ff7d00"},
          },
        ],
      },
    ],
  };

  chartInstance.setOption(option);
};

// 刷新数据（按角色+筛选条件过滤）
const refreshData = async () => {
  loading.value = true;
  try {
    const res = await cailiaoguidangchaxun.listMaterialArchiveUsingGet({
      userName: filterParams.userName,
      materialName: filterParams.materialName === "all" ? undefined : filterParams.materialName,
      stage: filterParams.stage === "all" ? undefined : filterParams.stage,
      archiveTime: filterParams.archiveTime.length === 2 ? `${filterParams.archiveTime[0]}~${filterParams.archiveTime[1]}` : undefined,
      page: 1,
      pageSize: 100,
    });
    if (res.code === 0) {
      archiveList.value = res.data?.records || [];
      // 更新统计数据
      await calculateStat();
      // 更新图表
      updateChart();
    } else {
      message.error(res.message || "获取归档材料列表失败");
    }
  } catch (error) {
    console.error("获取归档材料列表失败:", error);
    message.error("网络请求异常");
  } finally {
    loading.value = false;
  }
};

// 计算归档统计数据
const calculateStat = async () => {
  try {
    const res = await cailiaoguidangchaxun.getMaterialArchiveStatUsingGet();
    if (res.code === 0) {
      archiveStat.totalCount = res.data?.totalCount || 0;
      archiveStat.monthGrowth = res.data?.monthGrowth || 0;
      archiveStat.stageData.activist = res.data?.stageData?.activist || 0;
      archiveStat.stageData.developmentObject = res.data?.stageData?.developmentObject || 0;
      archiveStat.stageData.probationaryPartyMember = res.data?.stageData?.probationaryPartyMember || 0;
    } else {
      message.error(res.message || "获取归档统计数据失败");
      // 失败时使用本地计算作为 fallback
      archiveStat.totalCount = archiveList.value.length;
      archiveStat.stageData.activist = archiveList.value.filter(item => item.stage === "activist").length;
      archiveStat.stageData.developmentObject = archiveList.value.filter(item => item.stage === "developmentObject").length;
      archiveStat.stageData.probationaryPartyMember = archiveList.value.filter(item => item.stage === "probationaryPartyMember").length;
    }
  } catch (error) {
    console.error("获取归档统计数据失败:", error);
    // 失败时使用本地计算作为 fallback
    archiveStat.totalCount = archiveList.value.length;
    archiveStat.stageData.activist = archiveList.value.filter(item => item.stage === "activist").length;
    archiveStat.stageData.developmentObject = archiveList.value.filter(item => item.stage === "developmentObject").length;
    archiveStat.stageData.probationaryPartyMember = archiveList.value.filter(item => item.stage === "probationaryPartyMember").length;
  }
};

// 重置筛选条件
const resetFilter = async () => {
  filterParams.userName = "";
  filterParams.materialName = "all";
  filterParams.stage = "all";
  filterParams.archiveTime = [];
  await refreshData();
  message.success("筛选条件已重置");
};

// 查看归档详情
const viewArchive = async (record: any) => {
  try {
    const res = await cailiaoguidangchaxun.viewMaterialArchiveDetailUsingGet({ id: record.id });
    if (res.code === 0) {
      currentArchive.value = res.data;
      detailModalVisible.value = true;
    } else {
      message.error(res.message || "查看归档详情失败");
    }
  } catch (error) {
    console.error("查看归档详情失败:", error);
    message.error("网络请求异常");
  }
};

// 下载单个归档材料
const downloadArchive = async (record: any) => {
  try {
    const res = await cailiaoguidangchaxun.downloadMaterialArchiveUsingGet({ id: record.id });
    if (res.code === 0) {
      // 实际项目中处理下载
      message.success(`已开始下载【${record.userName}-${record.materialName}】归档文件`);
    } else {
      message.error(res.message || "下载归档材料失败");
    }
  } catch (error) {
    console.error("下载归档材料失败:", error);
    message.error("网络请求异常");
  }
};

// 导出单个归档材料
const exportSingle = async (record: any) => {
  try {
    const res = await cailiaoguidangchaxun.exportMaterialArchiveUsingGet({ id: record.id });
    if (res.code === 0) {
      message.success(`已导出【${record.userName}-${record.materialName}】归档材料（PDF格式）`);
    } else {
      message.error(res.message || "导出归档材料失败");
    }
  } catch (error) {
    console.error("导出归档材料失败:", error);
    message.error("网络请求异常");
  }
};

// 批量导出归档材料
const batchExport = () => {
  if (archiveList.value.length === 0) {
    message.warning("暂无可导出的归档材料");
    return;
  }

  message.confirm({
    title: "批量导出",
    content: `确定要导出选中的${archiveList.value.length}份归档材料吗？导出后将生成压缩包下载`,
    onOk: async () => {
      try {
        const res = await cailiaoguidangchaxun.batchExportMaterialArchiveUsingPost({
          ids: archiveList.value.map(item => item.id),
        });
        if (res.code === 0) {
          message.success(`已开始批量导出${archiveList.value.length}份归档材料，压缩包生成后将自动下载`);
        } else {
          message.error(res.message || "批量导出失败");
        }
      } catch (error) {
        console.error("批量导出失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

// 生命周期
onMounted(async () => {
  initChart();
  await refreshData();
  // 自适应窗口大小
  window.addEventListener("resize", () => chartInstance?.resize());
});

onUnmounted(() => {
  window.removeEventListener("resize", () => chartInstance?.resize());
  chartInstance?.dispose();
});

// 初始加载
watchEffect(() => {
  if (chartInstance) {
    updateChart();
  }
});

const toShowMaterialTemplateManagementView = () => {
  router.push("/materialTemplateManagement");
};

const toShowMaterialSubmissionReviewView = () => {
  router.push("/materialSubmissionReview");
};
</script>

<style scoped>
/* 页面基础样式 */
.material-archive-page {
  padding: 16px;
}

/* 统计卡片样式 */
.archive-stat {
  margin-bottom: 24px;
}

.stat-card {
  text-align: center;
  padding: 20px 0;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.stat-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #1d2129;
  margin-bottom: 4px;
}

.stat-desc {
  font-size: 12px;
  color: #86909c;
}

.stat-trend.up {
  color: #00b42a;
}

.stat-trend.down {
  color: #f53f3f;
}

/* 归档详情样式 */
.archive-detail {
  padding: 8px 0;
}

:deep(.arco-descriptions-item-label) {
  font-weight: 600;
}

/* 响应式适配 */
@media (max-width: 1200px) {
  :deep(.arco-row) {
    --arco-grid-col-span-6: 12;
    --arco-grid-col-span-18: 12;
  }
}
</style>
