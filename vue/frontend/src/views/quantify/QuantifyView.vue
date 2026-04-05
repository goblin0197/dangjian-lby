<template>
  <!-- 页面整体框架 -->
  <div class="quantify-report-page">
    <!-- 视图切换栏 -->
    <a-card style="margin-bottom: 16px">
      <a-space size="large">
        <a-button
          :status="activeView === 'config' ? 'primary' : 'normal'"
          @click="switchView('config')"
        >
          <icon-form />
          量化数据配置
        </a-button>
        <a-button
          type="primary"
          :status="activeView === 'report' ? 'primary' : 'normal'"
          @click="switchView('report')"
        >
          <icon-chart />
          统计报表查看
        </a-button>
      </a-space>
    </a-card>

    <!-- 统计报表核心区域 -->
    <a-card>
      <!-- 高级筛选区 -->
      <div
        class="filter-bar"
        style="
          margin-bottom: 20px;
          padding-bottom: 16px;
          border-bottom: 1px solid #eee;
        "
      >
        <a-row :gutter="16" align="middle">
          <a-col :span="4">
            <a-form-item label="时间范围" label-col-flex="80px">
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
            <a-form-item label="组织层级" label-col-flex="80px">
              <a-select
                v-model="filterParams.orgLevel"
                style="width: 100%"
                @change="refreshData"
              >
                <a-option value="all">全部</a-option>
                <a-option value="partyCommittees">党委</a-option>
                <a-option value="branch1">教师一支部</a-option>
                <a-option value="branch2">教师二支部</a-option>
                <a-option value="branch3">学生一支部</a-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="4">
            <a-form-item label="统计维度" label-col-flex="80px">
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
            <a-form-item label="统计指标" label-col-flex="80px">
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
      </div>

      <!-- 可视化图表区 -->
      <a-row :gutter="24" style="margin-bottom: 24px">
        <!-- 左侧：核心指标卡片 -->
        <a-col :span="6">
          <div class="indicator-card-container">
            <a-card class="indicator-card" hoverable>
              <div class="indicator-title">📈 活动参与率</div>
              <div class="indicator-value">
                {{ coreIndicators.activityRate }}
              </div>
              <div
                class="indicator-trend"
                :class="coreIndicators.activityTrend"
              >
                {{
                  coreIndicators.activityTrend === "up"
                    ? "↑ 环比增长"
                    : "↓ 环比下降"
                }}2.3%
              </div>
            </a-card>

            <a-card class="indicator-card" hoverable style="margin-top: 16px">
              <div class="indicator-title">📝 签到率</div>
              <div class="indicator-value">{{ coreIndicators.signRate }}</div>
              <div class="indicator-trend" :class="coreIndicators.signTrend">
                {{
                  coreIndicators.signTrend === "up"
                    ? "↑ 环比增长"
                    : "↓ 环比下降"
                }}1.8%
              </div>
            </a-card>

            <a-card class="indicator-card" hoverable style="margin-top: 16px">
              <div class="indicator-title">📎 材料完成率</div>
              <div class="indicator-value">
                {{ coreIndicators.materialRate }}
              </div>
              <div
                class="indicator-trend"
                :class="coreIndicators.materialTrend"
              >
                {{
                  coreIndicators.materialTrend === "up"
                    ? "↑ 环比增长"
                    : "↓ 环比下降"
                }}4.5%
              </div>
            </a-card>
          </div>
        </a-col>

        <!-- 右侧：可视化图表 -->
        <a-col :span="18">
          <a-card style="height: 400px">
            <template #title>
              {{ chartTitle }}
            </template>
            <div ref="chartRef" style="width: 100%; height: 340px"></div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 数据明细区 -->
      <a-card>
        <template #title>
          {{
            filterParams.dimension === "organization"
              ? "组织数据明细"
              : "个人数据明细"
          }}
        </template>
        <a-table
          :columns="detailColumns"
          :data="detailData"
          :pagination="{
            showTotal: true,
            pageSize: 10,
            current: 1,
            total: detailData.length,
          }"
          row-key="id"
        >
          <!-- 状态色值渲染 -->
          <template #activityRate="{ record }">
            <span :class="getRateClass(record.activityRate)">{{
              record.activityRate
            }}</span>
          </template>
          <template #signRate="{ record }">
            <span :class="getRateClass(record.signRate)">{{
              record.signRate
            }}</span>
          </template>
          <template #materialRate="{ record }">
            <span :class="getRateClass(record.materialRate)">{{
              record.materialRate
            }}</span>
          </template>
          <!-- 操作列 -->
          <template #operation="{ record }">
            <a-button type="text" @click="viewDetail(record)"
              >查看详情
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
        <h4 style="margin: 0 0 16px 0">
          {{
            filterParams.dimension === "organization" ? "支部详情" : "个人详情"
          }}：{{ currentDetailData.name }}
        </h4>
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="统计周期"
            >{{ currentDetailData.period }}
          </a-descriptions-item>
          <a-descriptions-item label="活动参与率"
            >{{ currentDetailData.activityRate }}
          </a-descriptions-item>
          <a-descriptions-item label="签到率"
            >{{ currentDetailData.signRate }}
          </a-descriptions-item>
          <a-descriptions-item label="材料完成率"
            >{{ currentDetailData.materialRate }}
          </a-descriptions-item>
          <a-descriptions-item
            label="参与活动数"
            v-if="filterParams.dimension === 'personal'"
          >
            {{ currentDetailData.activityCount }}
          </a-descriptions-item>
          <a-descriptions-item
            label="未完成材料数"
            v-if="filterParams.dimension === 'personal'"
          >
            {{ currentDetailData.unfinishedMaterial }}
          </a-descriptions-item>
          <a-descriptions-item
            label="支部人数"
            v-if="filterParams.dimension === 'organization'"
          >
            {{ currentDetailData.personCount }}
          </a-descriptions-item>
          <a-descriptions-item
            label="平均参与率"
            v-if="filterParams.dimension === 'organization'"
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
} from "@arco-design/web-vue/es/icon";
import { useRouter } from "vue-router";

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
  activityRate: "92%",
  activityTrend: "up",
  signRate: "88%",
  signTrend: "up",
  materialRate: "95%",
  materialTrend: "up",
});

// 5. 明细数据（模拟不同角色的数据源）
const orgDetailData = ref([
  {
    id: "1",
    name: "教师一支部",
    activityRate: "92%",
    signRate: "88%",
    materialRate: "95%",
    period: "2025.01-03",
    personCount: 45,
    averageRate: "91.7%",
  },
  {
    id: "2",
    name: "教师二支部",
    activityRate: "89%",
    signRate: "85%",
    materialRate: "93%",
    period: "2025.01-03",
    personCount: 38,
    averageRate: "89%",
  },
  {
    id: "3",
    name: "学生一支部",
    activityRate: "85%",
    signRate: "82%",
    materialRate: "88%",
    period: "2025.01-03",
    personCount: 120,
    averageRate: "85%",
  },
]);

const personDetailData = ref([
  {
    id: "1",
    name: "张三（教师党员）",
    activityRate: "98%",
    signRate: "100%",
    materialRate: "100%",
    period: "2025.01-03",
    activityCount: 12,
    unfinishedMaterial: 0,
  },
  {
    id: "2",
    name: "李四（培养联系人）",
    activityRate: "95%",
    signRate: "98%",
    materialRate: "95%",
    period: "2025.01-03",
    activityCount: 10,
    unfinishedMaterial: 1,
  },
  {
    id: "3",
    name: "王五（学生党员）",
    activityRate: "80%",
    signRate: "75%",
    materialRate: "85%",
    period: "2025.01-03",
    activityCount: 8,
    unfinishedMaterial: 2,
  },
]);

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
      Number(item.activityRate.replace("%", ""))
    );
    yData2 = orgDetailData.value.map((item) =>
      Number(item.signRate.replace("%", ""))
    );
    yData3 = orgDetailData.value.map((item) =>
      Number(item.materialRate.replace("%", ""))
    );
  } else {
    // 个人维度（默认折线图）
    xData = personDetailData.value.map((item) => item.name);
    yData1 = personDetailData.value.map((item) =>
      Number(item.activityRate.replace("%", ""))
    );
    yData2 = personDetailData.value.map((item) =>
      Number(item.signRate.replace("%", ""))
    );
    yData3 = personDetailData.value.map((item) =>
      Number(item.materialRate.replace("%", ""))
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
const refreshData = () => {
  // 权限过滤数据
  if (userRole.value === "admin") {
    // 管理员：全量数据
    detailData.value =
      filterParams.dimension === "organization"
        ? orgDetailData.value
        : personDetailData.value;
  } else if (userRole.value === "teacher") {
    // 培养联系人：仅教师一支部+对接党员
    detailData.value =
      filterParams.dimension === "organization"
        ? orgDetailData.value.filter((item) => item.name === "教师一支部")
        : personDetailData.value.filter((item) => item.name.includes("教师"));
  } else {
    // 普通党员：仅个人数据
    detailData.value = personDetailData.value.filter(
      (item) => item.name === "王五（学生党员）"
    );
  }

  // 更新核心指标（取平均值）
  if (detailData.value.length > 0) {
    const avgActivity =
      (
        detailData.value.reduce(
          (sum, item) => sum + Number(item.activityRate.replace("%", "")),
          0
        ) / detailData.value.length
      ).toFixed(0) + "%";
    const avgSign =
      (
        detailData.value.reduce(
          (sum, item) => sum + Number(item.signRate.replace("%", "")),
          0
        ) / detailData.value.length
      ).toFixed(0) + "%";
    const avgMaterial =
      (
        detailData.value.reduce(
          (sum, item) => sum + Number(item.materialRate.replace("%", "")),
          0
        ) / detailData.value.length
      ).toFixed(0) + "%";

    coreIndicators.activityRate = avgActivity;
    coreIndicators.signRate = avgSign;
    coreIndicators.materialRate = avgMaterial;
  }

  // 更新图表
  updateChart();
  message.success("数据刷新成功");
};

// 10. 辅助方法
// 比率颜色类
const getRateClass = (rate: string) => {
  const num = Number(rate.replace("%", ""));
  if (num >= 90) return "rate-high";
  if (num >= 80) return "rate-middle";
  return "rate-low";
};

// 查看详情
const viewDetail = (record: any) => {
  currentDetailData.value = record;
  detailModalVisible.value = true;
};

// 导出报表
const exportReport = () => {
  message.success(
    `已导出${
      filterParams.dimension === "organization" ? "组织" : "个人"
    }统计报表（Excel格式）`
  );
  // 实际项目中对接后端导出接口，此处模拟
};

// 11. 生命周期
onMounted(() => {
  initChart();
  refreshData();
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
.quantify-report-page {
  padding: 16px;
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
}

.indicator-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.indicator-value {
  font-size: 32px;
  font-weight: bold;
  color: #1d2129;
  margin-bottom: 4px;
}

.indicator-trend.up {
  color: #00b42a;
  font-size: 12px;
}

.indicator-trend.down {
  color: #f53f3f;
  font-size: 12px;
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

/* 响应式适配 */
@media (max-width: 1200px) {
  :deep(.arco-row) {
    --arco-grid-col-span-6: 12;
    --arco-grid-col-span-18: 12;
  }
}
</style>
