<template>
  <div class="notice-list-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>公告管理</a-breadcrumb-item>
          <a-breadcrumb-item>{{
            activeView === "manage"
              ? "公告管理"
              : activeView === "stat"
              ? "公告统计"
              : "公告列表"
          }}</a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <h1>公告管理</h1>
    </div>

    <!-- 视图切换栏 -->
    <div class="view-switch-bar">
      <a-space size="large">
        <a-button
          :disabled="userRole !== 'super_admin'"
          :status="activeView === 'manage' ? 'primary' : 'normal'"
          @click="switchView('manage')"
        >
          <icon-edit />
          公告管理
        </a-button>
        <a-button
          :status="activeView === 'list' ? 'primary' : 'normal'"
          type="primary"
          @click="switchView('list')"
        >
          <icon-list />
          公告列表
        </a-button>
        <a-button
          :status="activeView === 'stat' ? 'primary' : 'normal'"
          @click="switchView('stat')"
        >
          <icon-chart />
          公告统计
          <a-tag
            v-if="userRole !== 'super_admin'"
            color="orange"
            disabled
            size="small"
            style="margin-left: 4px"
          >
            仅管理员
          </a-tag>
        </a-button>
      </a-space>
    </div>

    <!-- 公告列表核心区域（全角色可见） -->
    <a-card class="list-card">
      <!-- 高级筛选区 -->
      <div class="filter-bar">
        <a-form :model="filterParams" class="filter-form" layout="inline">
          <a-row :gutter="16" align="middle">
            <a-col :span="5">
              <a-form-item label="公告类型">
                <a-select
                  v-model="filterParams.type"
                  allow-clear
                  placeholder="全部"
                  style="width: 100%"
                  @change="refreshNoticeList"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="notice">通知类</a-option>
                  <a-option value="activity">活动类</a-option>
                  <a-option value="remind">提醒类</a-option>
                  <a-option value="policy">政策类</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="5">
              <a-form-item label="时间范围">
                <a-select
                  v-model="filterParams.timeRange"
                  allow-clear
                  placeholder="全部"
                  style="width: 100%"
                  @change="refreshNoticeList"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="7day">近7天</a-option>
                  <a-option value="30day">近30天</a-option>
                  <a-option value="3month">近3个月</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="5">
              <a-form-item label="阅读状态">
                <a-select
                  v-model="filterParams.readStatus"
                  allow-clear
                  placeholder="全部"
                  style="width: 100%"
                  @change="refreshNoticeList"
                >
                  <a-option value="all">全部</a-option>
                  <a-option value="unread">未读</a-option>
                  <a-option value="read">已读</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="9">
              <a-form-item label="关键词">
                <a-input
                  v-model="filterParams.keyword"
                  allow-clear
                  placeholder="请输入公告标题/内容关键词"
                  style="width: 100%"
                  @press.enter="refreshNoticeList"
                >
                  <template #prefix>
                    <icon-search />
                  </template>
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <!-- 筛选操作按钮 -->
          <a-row style="margin-top: 8px">
            <a-col :span="24" style="text-align: right">
              <a-space size="middle">
                <a-button type="outline" @click="resetFilter">
                  <icon-refresh />
                  重置筛选
                </a-button>
                <a-button type="primary" @click="refreshNoticeList">
                  <icon-search />
                  筛选公告
                </a-button>
              </a-space>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 未读提醒区 -->
      <div
        v-if="unreadCount > 0"
        class="unread-remind"
        style="margin-bottom: 20px"
      >
        <a-alert
          :message="`🚨 您有【${unreadCount}】条未读公告，请及时查看`"
          closable
          type="warning"
          @close="unreadRemindClosed = true"
        />
      </div>
      <div
        v-else-if="!unreadRemindClosed && noticeList.length > 0"
        class="read-all-remind"
        style="margin-bottom: 20px"
      >
        <a-alert
          closable
          message="✅ 您已阅读全部公告"
          type="success"
          @close="unreadRemindClosed = true"
        />
      </div>

      <!-- 置顶公告区 -->
      <div
        v-if="topNoticeList.length > 0"
        class="top-notice-area"
        style="margin-bottom: 24px"
      >
        <div style="font-size: 16px; font-weight: 600; margin-bottom: 12px">
          📌 置顶公告
        </div>
        <a-space direction="vertical" style="width: 100%">
          <a-card
            v-for="item in topNoticeList"
            :key="item.id"
            class="top-notice-card"
            hoverable
            @click="openDetailModal(item)"
          >
            <div class="notice-header">
              <div class="notice-title">
                {{ item.title }}
                <a-tag color="red" size="small" style="margin-left: 8px"
                  >置顶
                </a-tag>
                <a-tag
                  :color="getTypeColor(item.type)"
                  size="small"
                  style="margin-left: 4px"
                >
                  {{ getTypeName(item.type) }}
                </a-tag>
              </div>
              <div
                v-if="getReadStatus(item.id) === 'unread'"
                class="notice-read-tag"
              >
                <a-tag color="orange">未读</a-tag>
              </div>
              <div v-else class="notice-read-tag">
                <a-tag color="gray" size="small">已读</a-tag>
              </div>
            </div>
            <div
              class="notice-meta"
              style="margin: 8px 0; font-size: 12px; color: #86909c"
            >
              发布人：{{ item.publisher }} | 发布时间：{{ item.publishTime }} |
              阅读量：{{ item.readCount }}
            </div>
            <div
              class="notice-summary"
              style="color: #4e5969; line-height: 1.6"
            >
              {{ getSummaryContent(item.content) }}
              <a
                style="color: #165dff; margin-left: 8px"
                @click.stop="openDetailModal(item)"
                >查看全文</a
              >
            </div>
          </a-card>
        </a-space>
      </div>

      <!-- 普通公告列表 -->
      <div class="normal-notice-area">
        <div
          v-if="normalNoticeList.length > 0"
          style="font-size: 16px; font-weight: 600; margin-bottom: 12px"
        >
          📢 最新公告
        </div>
        <a-space
          v-if="normalNoticeList.length > 0"
          direction="vertical"
          style="width: 100%"
        >
          <a-card
            v-for="item in normalNoticeList"
            :key="item.id"
            :class="{ 'unread-notice': getReadStatus(item.id) === 'unread' }"
            class="normal-notice-card"
            hoverable
            @click="openDetailModal(item)"
          >
            <div class="notice-header">
              <div class="notice-title">
                {{ item.title }}
                <a-tag
                  :color="getTypeColor(item.type)"
                  size="small"
                  style="margin-left: 4px"
                >
                  {{ getTypeName(item.type) }}
                </a-tag>
              </div>
              <div
                v-if="getReadStatus(item.id) === 'unread'"
                class="notice-read-tag"
              >
                <a-tag color="orange">未读</a-tag>
              </div>
              <div v-else class="notice-read-tag">
                <a-tag color="gray" size="small">已读</a-tag>
              </div>
            </div>
            <div
              class="notice-meta"
              style="margin: 8px 0; font-size: 12px; color: #86909c"
            >
              发布人：{{ item.publisher }} | 发布时间：{{ item.publishTime }} |
              阅读量：{{ item.readCount }}
            </div>
            <div
              class="notice-summary"
              style="color: #4e5969; line-height: 1.6"
            >
              {{ getSummaryContent(item.content) }}
              <a
                style="color: #165dff; margin-left: 8px"
                @click.stop="openDetailModal(item)"
                >查看全文</a
              >
            </div>
          </a-card>
        </a-space>

        <!-- 空状态 -->
        <div
          v-else
          class="empty-notice"
          style="text-align: center; padding: 60px 0"
        >
          <icon-file-text style="font-size: 48px; color: #ccc" />
          <div style="margin-top: 16px; font-size: 16px; color: #86909c">
            暂无符合条件的公告
          </div>
        </div>
      </div>

      <!-- 分页区 -->
      <div class="pagination-area" style="margin-top: 24px; text-align: right">
        <a-pagination
          :current="filterParams.current"
          :page-size="filterParams.pageSize"
          :total="total"
          show-total
          @change="handlePageChange"
        />
      </div>
    </a-card>

    <!-- 公告详情弹窗 -->
    <a-modal
      v-model:visible="detailModalVisible"
      :footer="null"
      :title="currentNotice?.title || '公告详情'"
      width="800px"
      @cancel="detailModalVisible = false"
    >
      <div v-if="currentNotice" class="notice-detail">
        <!-- 详情头部 -->
        <div
          class="detail-header"
          style="
            border-bottom: 1px solid #eee;
            padding-bottom: 16px;
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
            <div>
              <a-tag :color="getTypeColor(currentNotice.type)" size="small">
                {{ getTypeName(currentNotice.type) }}
              </a-tag>
              <a-tag
                v-if="currentNotice.isTop"
                color="red"
                size="small"
                style="margin-left: 4px"
              >
                置顶
              </a-tag>
            </div>
            <div style="font-size: 12px; color: #86909c">
              阅读量：{{ currentNotice.readCount + (detailJustOpened ? 1 : 0) }}
            </div>
          </div>
          <div style="font-size: 12px; color: #86909c; line-height: 1.6">
            发布人：{{ currentNotice.publisher }} | 发布时间：{{
              currentNotice.publishTime
            }}
            <span
              v-if="currentNotice.validStart && currentNotice.validEnd"
              style="margin-left: 8px"
            >
              | 生效时间：{{ currentNotice.validStart }} -
              {{ currentNotice.validEnd }}
            </span>
          </div>
        </div>

        <!-- 详情正文 -->
        <div
          class="detail-content"
          style="line-height: 1.8; font-size: 14px; margin-bottom: 24px"
          v-html="currentNotice.content"
        ></div>

        <!-- 详情附件 -->
        <div
          v-if="currentNotice.attachments && currentNotice.attachments.length"
          class="detail-attachments"
        >
          <div style="font-size: 14px; font-weight: 600; margin-bottom: 8px">
            📎 附件列表：
          </div>
          <a-space direction="vertical" style="width: 100%">
            <div
              v-for="(file, index) in currentNotice.attachments"
              :key="index"
              style="
                display: flex;
                align-items: center;
                padding: 8px;
                border: 1px solid #eee;
                border-radius: 4px;
              "
            >
              <icon-paper-clip style="margin-right: 8px; color: #165dff" />
              <span style="flex: 1">{{ file.name }}</span>
              <a-button type="text" @click="downloadAttachment(file)">
                <icon-download />
                下载
              </a-button>
            </div>
          </a-space>
        </div>

        <!-- 操作区 -->
        <div
          class="detail-operation"
          style="margin-top: 24px; text-align: right"
        >
          <a-space>
            <a-button @click="printNotice">
              <icon-printer />
              打印
            </a-button>
            <a-button
              v-if="getReadStatus(currentNotice.id) === 'unread'"
              type="primary"
              @click="markAsRead"
            >
              <icon-check-circle />
              标记已读
            </a-button>
            <!-- 管理员额外操作 -->
            <a-button
              v-if="userRole === 'admin'"
              status="warning"
              type="primary"
              @click="handleAdminEdit"
            >
              <icon-edit />
              编辑公告
            </a-button>
          </a-space>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import dayjs from "dayjs";
// 导入Arco图标
import {
  IconChart,
  IconCheckCircle,
  IconDownload,
  IconEdit,
  IconFileText,
  IconList,
  IconPaperClip,
  IconPrinter,
  IconRefresh,
  IconSearch,
} from "@arco-design/web-vue/es/icon";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
// 导入API
import * as gonggaoguanli from "@/api/gonggaoguanli";
import * as gonggaokuozhangongneng from "@/api/gonggaokuozhangongneng";

const router = useRouter();
const store = useStore();

// 1. 基础配置
// 从登录态获取用户角色
const userRole = ref("student"); // 初始值，后续会从store更新

// 获取用户信息
const getUserInfo = async () => {
  await store.dispatch("user/getLoginUser");
  const loginUser = store.state.user.loginUser;
  if (loginUser && loginUser.userRole) {
    userRole.value = loginUser.userRole;
  }
};

// 页面加载时获取用户信息
getUserInfo();

// 视图切换
const activeView = ref("list"); // manage/list/stat
const switchView = (view: string) => {
  activeView.value = view;
  if (view === "manage") {
    if (userRole.value === "super_admin") {
      message.info("已切换至公告管理视图");
      toShowAnnouncementManagementView();
    } else {
      message.warning("仅管理员可访问公告管理视图");
    }
  } else if (view === "stat") {
    if (userRole.value === "super_admin") {
      message.info("已切换至公告统计视图");
      toShowAnnouncementDetailsView();
    } else {
      message.warning("仅管理员可访问公告统计视图");
    }
  }
};

// 2. 筛选参数
const filterParams = reactive({
  current: 1,
  pageSize: 10,
  type: "all", // 公告类型
  timeRange: "all", // 时间范围
  readStatus: "all", // 阅读状态
  keyword: "", // 关键词
});
const total = ref(0);

// 未读提醒关闭状态
const unreadRemindClosed = ref(false);
// 详情弹窗相关
const detailModalVisible = ref(false);
const currentNotice = ref<any>(null);
const detailJustOpened = ref(false); // 标记详情刚打开，用于阅读量+1

// 3. 已读公告ID列表（从后端获取）
const readNoticeIds = ref<string[]>([]);

// 4. 公告数据（从后端获取）

// 筛选后的公告列表
const noticeList = ref<any[]>([]);
// 置顶公告列表
const topNoticeList = ref<any[]>([]);
// 普通公告列表
const normalNoticeList = ref<any[]>([]);

// 5. 计算未读数量
const unreadCount = ref(0);
const calculateUnreadCount = async () => {
  try {
    const res = await gonggaoguanli.getUnreadCountUsingGet1();
    if (res.code === 0) {
      unreadCount.value = res.data || 0;
    } else {
      console.error("获取未读数量失败:", res.message);
    }
  } catch (error) {
    console.error("获取未读数量失败:", error);
  }
};

// 6. 辅助方法
// 获取公告类型名称
const getTypeName = (type: string) => {
  switch (type) {
    case "notice":
      return "通知类";
    case "activity":
      return "活动类";
    case "remind":
      return "提醒类";
    case "policy":
      return "政策类";
    default:
      return "未知类型";
  }
};

// 获取公告类型颜色
const getTypeColor = (type: string) => {
  switch (type) {
    case "notice":
      return "blue";
    case "activity":
      return "green";
    case "remind":
      return "orange";
    case "policy":
      return "purple";
    default:
      return "gray";
  }
};

// 获取阅读状态
const getReadStatus = (id: string) => {
  return readNoticeIds.value.includes(id) ? "read" : "unread";
};

// 提取正文摘要（去除HTML标签，截取前100字）
const getSummaryContent = (content: string) => {
  // 去除HTML标签
  const plainText = content.replace(/<[^>]+>/g, "");
  // 截取前100字，超出加省略号
  return plainText.length > 100
    ? `${plainText.substring(0, 100)}...`
    : plainText;
};

// 7. 核心方法
// 刷新公告列表
const refreshNoticeList = async () => {
  try {
    const res = await gonggaoguanli.listNoticeVoByPageUsingGet({
      type: filterParams.type === "all" ? undefined : filterParams.type,
      status: "published", // 仅获取已发布的公告
      keyword: filterParams.keyword || undefined,
      current: filterParams.current,
      pageSize: filterParams.pageSize,
    });
    if (res.code === 0) {
      noticeList.value = res.data?.records || [];
      total.value = res.data?.total || 0;

      // 拆分置顶/普通公告
      topNoticeList.value = noticeList.value.filter((item) => item.isTop);
      normalNoticeList.value = noticeList.value.filter((item) => !item.isTop);

      // 计算未读数量
      await calculateUnreadCount();
    } else {
      message.error(res.message || "获取公告列表失败");
    }
  } catch (error) {
    console.error("获取公告列表失败:", error);
    message.error("网络请求异常");
  }
};

// 重置筛选条件
const resetFilter = async () => {
  filterParams.type = "all";
  filterParams.timeRange = "all";
  filterParams.readStatus = "all";
  filterParams.keyword = "";
  filterParams.current = 1;
  unreadRemindClosed.value = false;
  await refreshNoticeList();
  message.success("筛选条件已重置");
};

// 分页切换
const handlePageChange = async (page: number) => {
  filterParams.current = page;
  await refreshNoticeList();
};

// 打开详情弹窗
const openDetailModal = async (item: any) => {
  currentNotice.value = item;
  detailModalVisible.value = true;
  detailJustOpened.value = true;

  try {
    // 增加阅读量
    await gonggaokuozhangongneng.incrementReadCountUsingPut({
      announcementId: item.id,
    });

    // 标记为已读
    await gonggaokuozhangongneng.markAsReadUsingPost({
      announcementId: item.id,
    });

    // 重新计算未读数量
    await calculateUnreadCount();
    await refreshNoticeList();
  } catch (error) {
    console.error("处理公告阅读状态失败:", error);
  }
};

// 下载附件
const downloadAttachment = async (file: any) => {
  try {
    const res =
      await gonggaokuozhangongneng.downloadAnnouncementAttachmentUsingGet({
        attachmentId: file.id,
      });
    if (res.code === 0) {
      message.success(`已开始下载【${file.name}】`);
      // 实际项目中处理下载
    } else {
      message.error(res.message || "下载附件失败");
    }
  } catch (error) {
    console.error("下载附件失败:", error);
    message.error("网络请求异常");
  }
};

// 打印公告
const printNotice = () => {
  message.info("即将打印公告详情（实际项目中调用浏览器打印API）");
  // 模拟打印逻辑
  const printWindow = window.open("", "_blank");
  if (printWindow) {
    printWindow.document.write(`
      <html>
        <head><title>${currentNotice.value.title}</title></head>
        <body>
          <h1 style="text-align: center">${currentNotice.value.title}</h1>
          <div style="font-size: 12px; color: #86909c; text-align: center; margin-bottom: 20px">
            发布人：${currentNotice.value.publisher} | 发布时间：${currentNotice.value.publishTime}
          </div>
          <div style="line-height: 1.8; font-size: 14px">${currentNotice.value.content}</div>
        </body>
      </html>
    `);
    printWindow.document.close();
    printWindow.print();
  }
};

// 手动标记已读
const markAsRead = async () => {
  if (!currentNotice.value) return;
  try {
    const res = await gonggaokuozhangongneng.markAsReadUsingPost({
      announcementId: currentNotice.value.id,
    });
    if (res.code === 0) {
      await calculateUnreadCount();
      await refreshNoticeList();
      message.success("已标记为已读");
    } else {
      message.error(res.message || "标记已读失败");
    }
  } catch (error) {
    console.error("标记已读失败:", error);
    message.error("网络请求异常");
  }
};

// 管理员编辑公告（跳转到管理视图）
const handleAdminEdit = () => {
  if (userRole.value === "admin") {
    detailModalVisible.value = false;
    activeView.value = "manage";
    message.info("请在公告管理视图中编辑该公告");
  } else {
    message.warning("仅管理员可编辑公告");
  }
};

// 初始加载
onMounted(async () => {
  await refreshNoticeList();
});

// 监听筛选参数变化，自动刷新
watchEffect(async () => {
  await refreshNoticeList();
});

const toShowAnnouncementManagementView = () => {
  router.push("/announcementManagement");
};

const toShowAnnouncementDetailsView = () => {
  router.push("/announcementDetails");
};
</script>

<style scoped>
/* 页面样式 */
.notice-list-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

/* 页面头部样式 */
.page-header {
  margin-bottom: 24px;
}

.breadcrumb {
  margin-bottom: 8px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
  color: #333;
}

/* 视图切换栏样式 */
.view-switch-bar {
  margin-bottom: 24px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 卡片样式 */
.list-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 筛选栏样式 */
.filter-bar {
  margin-bottom: 16px;
  padding: 16px;
  background: #fafafa;
  border-radius: 4px;
}

.filter-form {
  width: 100%;
}

/* 置顶公告卡片样式 */
.top-notice-card {
  border: 2px solid #f53f3f !important;
}

/* 普通公告卡片样式 */
.normal-notice-card {
  border: 1px solid #e5e6eb !important;
  transition: all 0.2s;
}

.normal-notice-card:hover {
  border-color: #165dff !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 未读公告样式 */
.unread-notice {
  background-color: #fff7e6 !important;
}

/* 公告头部样式 */
.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.notice-title {
  font-size: 16px;
  font-weight: 600;
  color: #1d2129;
}

/* 空状态样式 */
.empty-notice {
  text-align: center;
  padding: 60px 0;
}

/* 详情弹窗样式 */
.notice-detail {
  padding: 8px 0;
}

.detail-content {
  line-height: 1.8;
}

/* 分页样式 */
.pagination {
  margin-top: 16px;
  text-align: right;
  padding: 16px;
  background: #fafafa;
  border-top: 1px solid #e8e8e8;
}

/* 响应式适配 */
@media (max-width: 1200px) {
  :deep(.arco-row) {
    --arco-grid-col-span-5: 12;
    --arco-grid-col-span-9: 12;
  }
}
</style>
