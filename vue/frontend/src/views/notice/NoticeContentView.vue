<template>
  <a-layout class="notice-timeline-container">
    <a-layout-header class="search-header">
      <!--      <a-space :size="16">-->
      <!--        <a-input-->
      <!--          placeholder="搜索公告标题"-->
      <!--          v-model="searchParams.title"-->
      <!--          allow-clear-->
      <!--          @press-enter="tohandleSearch"-->
      <!--          class="search-input"-->
      <!--        >-->
      <!--          <template #prefix>-->
      <!--            <icon-search />-->
      <!--          </template>-->
      <!--        </a-input>-->
      <!--        <a-input-->
      <!--          placeholder="搜索公告内容"-->
      <!--          v-model="searchParams.content"-->
      <!--          allow-clear-->
      <!--          @press-enter="tohandleSearch"-->
      <!--          class="search-input"-->
      <!--        >-->
      <!--          <template #prefix>-->
      <!--            <icon-search />-->
      <!--          </template>-->
      <!--        </a-input>-->
      <!--        <a-button type="primary" @click="tohandleSearch">搜索</a-button>-->
      <!--      </a-space>-->
    </a-layout-header>

    <a-layout-content>
      <a-timeline>
        <a-timeline-item
          v-for="item in dataList"
          :key="item.id"
          :label="moment(item.createTime).format('YYYY-MM-DD')"
          :dot-color="statusColorMap[item.status]"
        >
          <div class="timeline-card">
            <div class="card-header">
              <a-tag :color="statusColorMap[item.status]" size="small">
                {{ statusMap[item.status] }}
              </a-tag>
              <span class="title">{{ item.title }}</span>
              <span class="publisher">{{
                item.publisherId || "系统管理员"
              }}</span>
            </div>
            <div class="content">{{ item.content }}</div>
          </div>
        </a-timeline-item>

        <!-- 空状态提示 -->
        <a-empty v-if="dataList?.length === 0" description="暂无公告" />
      </a-timeline>

      <!-- 分页组件 -->
      <div class="pagination-wrapper">
        <a-pagination
          :total="total"
          :current="searchParams.current"
          :page-size="searchParams.pageSize"
          show-total
          @change="handlePageChange"
          @page-size-change="handlePageSizeChange"
        />
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import dayjs from "dayjs";
import { onMounted, reactive, ref, computed, watchEffect } from "vue";
import { IconSearch, IconRight } from "@arco-design/web-vue/es/icon";
import message from "@arco-design/web-vue/es/message";
import moment from "moment";
import { Service, NoticeVO } from "../../../generated";
import { useStore } from "vuex";
import { IconExclamationCircleFill } from "@arco-design/web-vue/es/icon";

// 状态映射
const statusMap = { 0: "草稿", 1: "已发布" };
const statusColorMap = { 0: "#165DFF", 1: "#FF7D00" };

// 响应式数据
const searchText = ref("");
const total = ref(0);
const dataList = ref([]);
const searchParams = ref<NoticeVO>({
  title: "",
});

onMounted(() => {
  loadData();
});

const loadData = async () => {
  const res = await Service.listNoticeVoByPageUsingGet();
  console.log(res);
  if (res.code === 0) {
    // dataList.value = res.data.records;
    // total.value = res.data.total;
    dataList.value = res.data;
    total.value = res.data.length;
  } else {
    message.error("加载失败" + res.message);
  }
};

const tohandleSearch = () => {
  loadData();
};

// 分页变化
const handlePageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

// 页容量变化
const handlePageSizeChange = (size: number) => {
  searchParams.value.pageSize = size;
  searchParams.value.current = 1; // 切换页容量时重置到第一页
};

// 搜索处理
const handleSearch = () => {
  searchParams.value.current = 1; // 搜索时回到第一页
  searchParams.value.title = searchText.value;
  searchParams.value.content = searchText.value;
};

// 监听分页参数变化自动加载数据
watchEffect(() => {
  loadData();
});
</script>

<style scoped>
/* 分页样式优化 */
.pagination-wrapper {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

/* 加载状态样式 */
.loading-wrapper {
  padding: 40px 0;
  text-align: center;
}

/* 空状态容器 */
.empty-wrapper {
  padding: 40px 0;
}

.notice-timeline-container {
  padding: 20px;
  background: var(--color-bg-2);
  /* background-color: aqua; */
}

.search-header {
  background: transparent;
  padding: 0 0 20px 0;
  border-bottom: 1px solid var(--color-border);
  margin-bottom: 30px;
}

.timeline-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.timeline-card {
  /* width: 250px; */
  background: var(--color-bg-3);
  border-radius: 6px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s;

  &:hover {
    transform: translateX(5px);
  }
}

.card-header {
  /* background-color: aqua; */
  /* width: 250px; */
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;

  .title {
    font-weight: 500;
    font-size: 16px;
    color: var(--color-text-1);
  }

  .publisher {
    color: var(--color-text-3);
    font-size: 12px;
    margin-left: auto;
  }
}

.content {
  color: var(--color-text-2);
  line-height: 1.6;
  white-space: pre-wrap;
}

.detail-btn {
  margin-top: 12px;
  padding: 0;
  color: var(--color-primary);

  &:hover {
    background: transparent;
  }
}

.pagination-wrapper {
  margin: 40px 0 20px;
  display: flex;
  justify-content: center;
}

/* 时间线样式调整 */
:deep(.arco-timeline-item-label) {
  font-size: 12px;
  color: var(--color-text-3);
  width: 160px;
  text-align: center;
}

:deep(.arco-timeline-item-content) {
  margin-left: 40px;
  margin-right: 40px;
}
</style>
