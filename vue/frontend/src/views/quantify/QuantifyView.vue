<template>
  <div class="quantify-view-container">
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
          @click="switchView('config')"
        >
          <icon-form />
          量化数据配置
        </a-button>
        <a-button
          :status="activeView === 'report' ? 'primary' : 'normal'"
          type="primary"
          @click="switchView('report')"
        >
          <icon-chart />
          统计报表查看
        </a-button>
      </a-space>
    </div>

    <!-- 统计报表核心区域 -->
    <a-card class="list-card">
      <!-- 高级筛选区 -->
      <div class="filter-bar">
        <a-form :model="filterParams" class="filter-form" layout="inline">
          <a-row :gutter="16" align="middle">
            <a-col :span="4">
              <a-form-item label="时间范围">
                <a-select
                  v-model="filterParams.timeRange"
                  style="width: 100%"
                  @change="refreshData"
                >
                  <a-option value="3month">近3个月</a-option>
                  <a-option value="6month">近6个月</a-option>
                  <a-option value="1year">近1年</a-option>
                  <a-option value="custom">自定义</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="4">
              <a-form-item label="组织层级">
                <a-select
                  v-model="filterParams.orgLevel"
                  style="width: 100%"
                  @change="refreshData"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="党委">党委</a-option>
                  <a-option value="党总支">党总支</a-option>
                  <a-option value="党支部">党支部</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="4">
              <a-form-item label="统计维度">
                <a-select
                  v-model="filterParams.dimension"
                  style="width: 100%"
                  @change="refreshData"
                >
                  <a-option value="organization">组织</a-option>
                  <a-option value="personal">个人</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="4">
              <a-form-item label="统计指标">
                <a-select
                  v-model="filterParams.indicator"
                  style="width: 100%"
                  @change="refreshData"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="activityRate">活动参与率</a-option>
                  <a-option value="signRate">签到率</a-option>
                  <a-option value="materialRate">材料完成率</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="8" style="text-align: right">
              <a-space size="middle">
                <a-button type="outline" @click="refreshData">
                  <icon-refresh />
                  刷新数据
                </a-button>
                <a-button type="primary" @click="exportReport">
                  <icon-download />
                  导出报表
                </a-button>
                <!-- 图表类型切换（仅组织维度显示） -->
                <a-select
                  v-if="filterParams.dimension === 'organization'"
                  v-model="chartType"
                  style="width: 120px"
                  @change="updateChart"
                >
                  <a-option value="bar">柱状图（对比）</a-option>
                  <a-option value="line">折线图（趋势）</a-option>
                  <a-option value="pie">饼图（占比）</a-option>
                </a-select>
              </a-space>
            </a-col>
          </a-row>

          <!-- 自定义时间范围（按需显示） -->
          <a-row
            v-if="filterParams.timeRange === 'custom'"
            style="margin-top: 16px"
          >
            <a-col :span="12">
              <a-range-picker
                v-model="filterParams.customTime"
                style="width: 100%"
              />
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 可视化图表区 -->
      <a-row :gutter="24" style="margin-bottom: 24px">
        <!-- 左侧：核心指标卡片 -->
        <a-col :span="6">
          <div class="indicator-card-container">
            <a-card class="indicator-card" hoverable>
              <div class="indicator-title">
                <icon-line-chart style="margin-right: 8px; color: #165dff" />
                活动参与率
              </div>
              <div class="indicator-value">
                {{ coreIndicators.activityRate }}
              </div>
              <div
                :class="coreIndicators.activityTrend"
                class="indicator-trend"
              >
                <span
                  v-if="coreIndicators.activityTrend === 'up'"
                  class="trend-icon"
                  >↑</span
                >
                <span v-else class="trend-icon">↓</span>
                {{
                  coreIndicators.activityTrend === "up"
                    ? "环比增长"
                    : "环比下降"
                }}
                2.3%
              </div>
            </a-card>

            <a-card class="indicator-card" hoverable style="margin-top: 16px">
              <div class="indicator-title">
                <icon-check-circle style="margin-right: 8px; color: #00b42a" />
                签到率
              </div>
              <div class="indicator-value">{{ coreIndicators.signRate }}</div>
              <div :class="coreIndicators.signTrend" class="indicator-trend">
                <span
                  v-if="coreIndicators.signTrend === 'up'"
                  class="trend-icon"
                  >↑</span
                >
                <span v-else class="trend-icon">↓</span>
                {{
                  coreIndicators.signTrend === "up" ? "环比增长" : "环比下降"
                }}
                1.8%
              </div>
            </a-card>

            <a-card class="indicator-card" hoverable style="margin-top: 16px">
              <div class="indicator-title">
                <icon-file-text style="margin-right: 8px; color: #ff7d00" />
                材料完成率
              </div>
              <div class="indicator-value">
                {{ coreIndicators.materialRate }}
              </div>
              <div
                :class="coreIndicators.materialTrend"
                class="indicator-trend"
              >
                <span
                  v-if="coreIndicators.materialTrend === 'up'"
                  class="trend-icon"
                  >↑</span
                >
                <span v-else class="trend-icon">↓</span>
                {{
                  coreIndicators.materialTrend === "up"
                    ? "环比增长"
                    : "环比下降"
                }}
                4.5%
              </div>
            </a-card>
          </div>
        </a-col>

        <!-- 右侧：可视化图表 -->
        <a-col :span="18">
          <a-card style="height: 400px" hoverable>
            <template #title>
              <span style="font-weight: 600; color: #1890ff">{{
                chartTitle
              }}</span>
            </template>
            <div
              v-loading="loading"
              ref="chartRef"
              style="width: 100%; height: 340px"
            ></div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 数据明细区 -->
      <a-card hoverable>
        <template #title>
          <span style="font-weight: 600; color: #1890ff">
            {{
              filterParams.dimension === "organization"
                ? "组织数据明细"
                : "个人数据明细"
            }}
          </span>
        </template>
        <a-table
          :columns="detailColumns"
          :data="detailData"
          :loading="loading"
          :pagination="{
            showTotal: true,
            pageSize: 10,
            current: 1,
            total: detailData.length,
            showSizeChanger: true,
          }"
          border
          pagination-position="bottom"
          row-key="id"
        >
          <!-- 状态色值渲染 -->
          <template #activityRate="{ record }">
            <a-tag :color="getRateColor(record.activityRate)">{{
              record.activityRate
            }}</a-tag>
          </template>
          <template #signRate="{ record }">
            <a-tag :color="getRateColor(record.signRate)">{{
              record.signRate
            }}</a-tag>
          </template>
          <template #materialRate="{ record }">
            <a-tag :color="getRateColor(record.materialRate)">{{
              record.materialRate
            }}</a-tag>
          </template>
          <!-- 操作列 -->
          <template #operation="{ record }">
            <a-button size="small" type="primary" @click="viewDetail(record)">
              <icon-eye />
              查看详情
            </a-button>
          </template>
        </a-table>
      </a-card>
    </a-card>

    <!-- 详情弹窗 -->
    <a-modal
      v-model:visible="detailModalVisible"
      title="数据详情"
      width="800px"
      @cancel="detailModalVisible = false"
    >
      <div v-if="currentDetailData">
        <h4 style="margin: 0 0 16px 0; color: #1890ff; font-weight: 600">
          {{
            filterParams.dimension === "organization" ? "支部详情" : "个人详情"
          }}：{{ currentDetailData.name }}
        </h4>
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="统计周期">{{
            currentDetailData.period
          }}</a-descriptions-item>
          <a-descriptions-item label="活动参与率">{{
            currentDetailData.activityRate
          }}</a-descriptions-item>
          <a-descriptions-item label="签到率">{{
            currentDetailData.signRate
          }}</a-descriptions-item>
          <a-descriptions-item label="材料完成率">{{
            currentDetailData.materialRate
          }}</a-descriptions-item>
          <a-descriptions-item
            v-if="filterParams.dimension === 'personal'"
            label="参与活动数"
          >
            {{ currentDetailData.activityCount }}
          </a-descriptions-item>
          <a-descriptions-item
            v-if="filterParams.dimension === 'personal'"
            label="未完成材料数"
          >
            {{ currentDetailData.unfinishedMaterial }}
          </a-descriptions-item>
          <a-descriptions-item
            v-if="filterParams.dimension === 'organization'"
            label="支部人数"
          >
            {{ currentDetailData.personCount }}
          </a-descriptions-item>
          <a-descriptions-item
            v-if="filterParams.dimension === 'organization'"
            label="平均参与率"
          >
            {{ currentDetailData.averageRate }}
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, onUnmounted, reactive, ref, watchEffect } from "vue";
import * as echarts from "echarts";
import message from "@arco-design/web-vue/es/message";
// 导入Arco图标
import {
  IconChart,
  IconDownload,
  IconForm,
  IconRefresh,
  IconLineChart,
  IconCheckCircle,
  IconFileText,
  IconEye,
} from "@arco-design/web-vue/es/icon";
import { useRouter } from "vue-router";
// 导入API
import * as lianghuashuju from "@/api/lianghuashuju";

const router = useRouter();

// 1. 基础配置
// 模拟用户角色（实际从登录态获取：admin/teacher/student）
const userRole = ref("admin");
// 视图切换
const activeView = ref("report");
const switchView = (view: string) => {
  activeView.value = view;
  if (view === "config") {
    // 跳转到配置视图（实际项目中可路由跳转）
    message.info("已切换至量化数据配置视图");
  }
  toShowQuantifyManagerView();
};

// 2. 筛选参数
const filterParams = reactive({
  timeRange: "3month", // 时间范围
  orgLevel: "all", // 组织层级
  dimension: "organization", // 统计维度
  indicator: "all", // 统计指标
  customTime: [], // 自定义时间
});

// 3. 图表配置
const chartRef = ref<HTMLDivElement>();
let chartInstance: echarts.ECharts | null = null;
const chartType = ref("bar"); // 图表类型：bar/line/pie
const chartTitle = ref("各支部核心指标对比（近3个月）");

// 4. 核心指标数据
const coreIndicators = reactive({
  activityRate: "0%",
  activityTrend: "up",
  signRate: "0%",
  signTrend: "up",
  materialRate: "0%",
  materialTrend: "up",
});

// 5. 明细数据
const orgDetailData = ref<any[]>([]);
const personDetailData = ref<any[]>([]);

// 权限过滤后的明细数据
const detailData = ref<any[]>([]);
// 详情弹窗
const detailModalVisible = ref(false);
const currentDetailData = ref<any>(null);

// 6. 表格列配置
const detailColumns = ref([
  {
    title: "名称",
    dataIndex: "name",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "活动参与率",
    slotName: "activityRate",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "签到率",
    slotName: "signRate",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "材料完成率",
    slotName: "materialRate",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "统计周期",
    dataIndex: "period",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "操作",
    slotName: "operation",
    width: 100,
  },
]);

// 7. 初始化图表
const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value);
    updateChart();
  }
};

// 8. 更新图表数据
const updateChart = () => {
  if (!chartInstance) return;

  // 筛选图表数据
  let xData: string[] = [];
  let yData1: number[] = []; // 活动参与率
  let yData2: number[] = []; // 签到率
  let yData3: number[] = []; // 材料完成率

  // 组织维度
  if (filterParams.dimension === "organization") {
    xData = orgDetailData.value.map((item) => item.name);
    yData1 = orgDetailData.value.map((item) =>
      Number(item.activityRate.replace("%", "")),
    );
    yData2 = orgDetailData.value.map((item) =>
      Number(item.signRate.replace("%", "")),
    );
    yData3 = orgDetailData.value.map((item) =>
      Number(item.materialRate.replace("%", "")),
    );
  } else {
    // 个人维度（默认折线图）
    xData = personDetailData.value.map((item) => item.name);
    yData1 = personDetailData.value.map((item) =>
      Number(item.activityRate.replace("%", "")),
    );
    yData2 = personDetailData.value.map((item) =>
      Number(item.signRate.replace("%", "")),
    );
    yData3 = personDetailData.value.map((item) =>
      Number(item.materialRate.replace("%", "")),
    );
  }

  // 图表配置项
  let option = {};

  // 柱状图（对比）
  if (chartType.value === "bar") {
    option = {
      tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
      legend: { data: ["活动参与率", "签到率", "材料完成率"] },
      xAxis: { type: "category", data: xData },
      yAxis: { type: "value", max: 100, unit: "%" },
      series: [
        {
          name: "活动参与率",
          type: "bar",
          data: yData1,
          itemStyle: { color: "#165dff" },
        },
        {
          name: "签到率",
          type: "bar",
          data: yData2,
          itemStyle: { color: "#00b42a" },
        },
        {
          name: "材料完成率",
          type: "bar",
          data: yData3,
          itemStyle: { color: "#ff7d00" },
        },
      ],
    };
    chartTitle.value = `${
      filterParams.timeRange === "3month"
        ? "近3个月"
        : filterParams.timeRange === "6month"
        ? "近6个月"
        : "近1年"
    }各${
      filterParams.dimension === "organization" ? "支部" : "党员"
    }核心指标对比`;
  }

  // 折线图（趋势）
  if (chartType.value === "line" || filterParams.dimension === "personal") {
    option = {
      tooltip: { trigger: "axis" },
      legend: { data: ["活动参与率", "签到率", "材料完成率"] },
      xAxis: { type: "category", data: xData },
      yAxis: { type: "value", max: 100, unit: "%" },
      series: [
        {
          name: "活动参与率",
          type: "line",
          data: yData1,
          smooth: true,
          itemStyle: { color: "#165dff" },
        },
        {
          name: "签到率",
          type: "line",
          data: yData2,
          smooth: true,
          itemStyle: { color: "#00b42a" },
        },
        {
          name: "材料完成率",
          type: "line",
          data: yData3,
          smooth: true,
          itemStyle: { color: "#ff7d00" },
        },
      ],
    };
    chartTitle.value = `${
      filterParams.timeRange === "3month"
        ? "近3个月"
        : filterParams.timeRange === "6month"
        ? "近6个月"
        : "近1年"
    }各${
      filterParams.dimension === "organization" ? "支部" : "党员"
    }核心指标趋势`;
  }

  // 饼图（占比）
  if (chartType.value === "pie" && filterParams.dimension === "organization") {
    option = {
      tooltip: { trigger: "item" },
      legend: { orient: "vertical", left: "left", data: xData },
      series: [
        {
          name: "活动参与率",
          type: "pie",
          radius: ["40%", "70%"],
          data: xData.map((name, index) => ({ name, value: yData1[index] })),
          label: { show: true, formatter: "{b}: {c}%" },
        },
      ],
    };
    chartTitle.value = `各支部活动参与率占比（${
      filterParams.timeRange === "3month"
        ? "近3个月"
        : filterParams.timeRange === "6month"
        ? "近6个月"
        : "近1年"
    }）`;
  }

  chartInstance.setOption(option);
};

// 9. 刷新数据（筛选后）
const refreshData = async () => {
  try {
    // 获取核心指标数据
    const coreRes = await lianghuashuju.getCoreIndicatorsUsingGet({
      timeRange: filterParams.timeRange,
      orgLevel:
        filterParams.orgLevel === "all" ? undefined : filterParams.orgLevel,
      dimension: filterParams.dimension,
    });
    // 注意：res 是整个响应对象，res.data 才是响应体
    const coreResponseData = coreRes.data;
    if (coreResponseData.code === 0) {
      coreIndicators.activityRate = coreResponseData.data?.activityRate || "0%";
      coreIndicators.signRate = coreResponseData.data?.signRate || "0%";
      coreIndicators.materialRate = coreResponseData.data?.materialRate || "0%";
      // 更新趋势
      coreIndicators.activityTrend =
        coreResponseData.data?.activityTrend === "up" ? "up" : "down";
      coreIndicators.signTrend =
        coreResponseData.data?.signTrend === "up" ? "up" : "down";
      coreIndicators.materialTrend =
        coreResponseData.data?.materialTrend === "up" ? "up" : "down";
    }

    // 获取组织或个人统计数据
    let detailRes;
    if (filterParams.dimension === "organization") {
      detailRes = await lianghuashuju.getOrganizationStatisticsUsingGet({
        timeRange: filterParams.timeRange,
        orgLevel:
          filterParams.orgLevel === "all" ? undefined : filterParams.orgLevel,
        indicator:
          filterParams.indicator === "all" ? undefined : filterParams.indicator,
      });
    } else {
      detailRes = await lianghuashuju.getPersonalStatisticsUsingGet({
        timeRange: filterParams.timeRange,
        orgLevel:
          filterParams.orgLevel === "all" ? undefined : filterParams.orgLevel,
        indicator:
          filterParams.indicator === "all" ? undefined : filterParams.indicator,
      });
    }

    // 注意：res 是整个响应对象，res.data 才是响应体
    const detailResponseData = detailRes.data;
    if (detailResponseData.code === 0) {
      detailData.value = detailResponseData.data || [];
      // 更新图表数据
      if (filterParams.dimension === "organization") {
        orgDetailData.value = detailResponseData.data || [];
      } else {
        personDetailData.value = detailResponseData.data || [];
      }
    }

    // 更新图表
    updateChart();
    message.success("数据刷新成功");
  } catch (error) {
    console.error("获取量化数据失败:", error);
    message.error("网络请求异常");
  }
};

// 10. 辅助方法
// 比率颜色类
const getRateClass = (rate: string) => {
  const num = Number(rate.replace("%", ""));
  if (num >= 90) return "rate-high";
  if (num >= 80) return "rate-middle";
  return "rate-low";
};

// 比率颜色
const getRateColor = (rate: string) => {
  const num = Number(rate.replace("%", ""));
  if (num >= 90) return "success";
  if (num >= 80) return "warning";
  return "danger";
};

// 查看详情
const viewDetail = async (record: any) => {
  try {
    const res = await lianghuashuju.getQuantifyDataDetailUsingGet({
      id: record.id,
      dimension: filterParams.dimension,
    });
    // 注意：res 是整个响应对象，res.data 才是响应体
    const responseData = res.data;
    if (responseData.code === 0) {
      currentDetailData.value = responseData.data || record;
      detailModalVisible.value = true;
    } else {
      message.error(responseData.message || "获取详情失败");
    }
  } catch (error) {
    console.error("获取详情失败:", error);
    // 失败时使用本地数据
    currentDetailData.value = record;
    detailModalVisible.value = true;
  }
};

// 导出报表
const exportReport = async () => {
  try {
    const res = await lianghuashuju.exportQuantifyReportUsingGet({
      timeRange: filterParams.timeRange,
      orgLevel:
        filterParams.orgLevel === "all" ? undefined : filterParams.orgLevel,
      dimension: filterParams.dimension,
      indicator:
        filterParams.indicator === "all" ? undefined : filterParams.indicator,
    });
    // 注意：res 是整个响应对象，res.data 才是响应体
    const responseData = res.data;
    if (responseData.code === 0) {
      message.success(
        `已导出${
          filterParams.dimension === "organization" ? "组织" : "个人"
        }统计报表（Excel格式）`,
      );
      // 实际项目中处理下载
      if (responseData.data) {
        // 创建下载链接
        const link = document.createElement("a");
        link.href = responseData.data;
        link.download = `${
          filterParams.dimension === "organization" ? "组织" : "个人"
        }统计报表.xlsx`;
        link.click();
      }
    } else {
      message.error(responseData.message || "导出报表失败");
    }
  } catch (error) {
    console.error("导出报表失败:", error);
    message.error("网络请求异常");
  }
};

// 11. 生命周期
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

const toShowQuantifyManagerView = () => {
  router.push("/quantifyManager");
};
</script>

<style scoped>
/* 页面基础样式 */
.quantify-view-container {
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
  padding: 20px;
}

/* 列表卡片 */
.list-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 指标卡片样式 */
.indicator-card-container {
  height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.indicator-card {
  text-align: center;
  padding: 20px 0;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.indicator-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.indicator-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.indicator-value {
  font-size: 32px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 8px;
}

.indicator-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.indicator-trend.up {
  color: #00b42a;
}

.indicator-trend.down {
  color: #f53f3f;
}

.trend-icon {
  font-size: 14px;
  font-weight: 600;
}

/* 比率颜色 */
.rate-high {
  color: #00b42a;
  font-weight: 600;
}

.rate-middle {
  color: #ff7d00;
  font-weight: 600;
}

.rate-low {
  color: #f53f3f;
  font-weight: 600;
}

/* 表格样式适配 */
:deep(.arco-table) {
  --arco-table-header-text-color: #1d2129;
  --arco-table-body-text-color: #4e5969;
}

/* 响应式适配 */
@media (max-width: 1200px) {
  :deep(.arco-row) {
    --arco-grid-col-span-6: 12;
    --arco-grid-col-span-18: 12;
  }
  
  .indicator-card-container {
    height: auto;
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .quantify-view-container {
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
  
  .indicator-card {
    padding: 16px 0;
  }
  
  .indicator-value {
    font-size: 24px;
  }
}
</style>
