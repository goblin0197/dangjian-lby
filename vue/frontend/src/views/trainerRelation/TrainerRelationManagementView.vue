<template>
  <div class="trainer-relation-management-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>组织管理</a-breadcrumb-item>
          <a-breadcrumb-item>培养关系管理</a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <h1>培养关系管理</h1>
    </div>

    <!-- 统计卡片区域 -->
    <div v-if="!loading" class="stats-container">
      <div class="stat-card">
        <div class="stat-number">{{ stats.total }}</div>
        <div class="stat-label">总培养关系数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.active }}</div>
        <div class="stat-label">活跃培养关系</div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <a-card class="search-card">
      <a-form :model="searchParams" class="search-form" layout="inline">
        <a-form-item label="组织">
          <a-select v-model="searchParams.orgId" placeholder="请选择组织">
            <a-option value="">全部</a-option>
            <a-option v-for="org in orgList" :key="org.id" :value="org.id">
              {{ org.orgName }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="搜索">
          <a-input
            v-model="searchParams.keyword"
            allow-clear
            placeholder="请输入姓名/学号/工号..."
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button @click="handleReset">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 操作按钮区域 -->
    <div class="action-bar">
      <a-space size="large">
        <a-button type="primary" @click="showAddModal">
          <icon-plus />
          新增培养关系
        </a-button>
        <a-button
          type="primary"
          @click="handleBatchDelete"
          :disabled="selectedRowKeys.length === 0"
        >
          <icon-delete />
          批量删除
        </a-button>
      </a-space>
    </div>

    <!-- 数据列表区域 -->
    <a-card class="list-card">
      <template #loading>
        <div style="display: flex; justify-content: center; padding: 40px">
          <a-spin size="large" tip="加载中..." />
        </div>
      </template>
      <a-table
        :data="relationList"
        :loading="loading"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total,
          showSizeChanger: true,
          onChange: onPageChange,
          onShowSizeChange: onSizeChange,
        }"
        :row-selection="rowSelection"
        :selected-row-keys="selectedRowKeys"
        border
        pagination-position="bottom"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <template #columns>
          <a-table-column data-index="trainerName" title="培养人" width="150">
            <template #cell="{ record }">
              <a-tag color="blue">{{ record.trainerName }}</a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="userName" title="培养对象" width="150">
            <template #cell="{ record }">
              <a-tag color="green">{{ record.userName }}</a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="startDate" title="开始日期" width="180" />
          <a-table-column data-index="status" title="状态" width="100">
            <template #cell="{ record }">
              <a-tag :color="getStatusColor(record.status)">
                {{ getStatusText(record.status) }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column
            data-index="createTime"
            title="创建时间"
            width="180"
          />
          <a-table-column
            data-index="updateTime"
            title="更新时间"
            width="180"
          />
          <a-table-column title="操作" width="200" fixed="right">
            <template #cell="{ record }">
              <a-space size="small">
                <a-button
                  size="small"
                  type="primary"
                  @click="showDetailModal(record)"
                >
                  查看
                </a-button>
                <a-button size="small" danger @click="handleDelete(record.id)">
                  删除
                </a-button>
              </a-space>
            </template>
          </a-table-column>
        </template>
      </a-table>
    </a-card>

    <!-- 新增培养关系模态框 -->
    <a-modal
      v-model:visible="addModalVisible"
      title="新增培养关系"
      destroy-on-close
      width="600px"
      @cancel="handleAddModalCancel"
      @ok="handleAddModalOk"
    >
      <a-form
        ref="addFormRef"
        :model="addFormData"
        :rules="addFormRules"
        layout="vertical"
      >
        <a-form-item field="trainerId" label="培养人" required>
          <a-select
            v-model:value="addFormData.trainerId"
            placeholder="请选择培养人"
          >
            <a-option
              v-for="trainer in trainerList"
              :key="trainer.id"
              :value="trainer.id"
            >
              {{ trainer.userName }} ({{ trainer.userAccount }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="userId" label="培养对象" required>
          <a-select
            v-model:value="addFormData.userId"
            placeholder="请选择培养对象"
          >
            <a-option
              v-for="user in menteeList"
              :key="user.id"
              :value="user.id"
            >
              {{ user.userName }} ({{ user.userAccount }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="startDate" label="开始日期" required>
          <a-date-picker
            v-model:value="addFormData.startDate"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
            placeholder="请选择开始日期"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 查看详情模态框 -->
    <a-modal
      v-model:visible="detailModalVisible"
      title="培养关系详情"
      destroy-on-close
      width="600px"
      @cancel="handleDetailModalCancel"
    >
      <div v-if="detailRecord" class="detail-content">
        <div class="detail-item">
          <span class="label">培养人：</span>
          <span class="value">{{ detailRecord.trainerName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">培养对象：</span>
          <span class="value">{{ detailRecord.userName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">开始日期：</span>
          <span class="value">{{ detailRecord.startDate }}</span>
        </div>

        <div class="detail-item">
          <span class="label">状态：</span>
          <span class="value">{{ getStatusText(detailRecord.status) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">创建时间：</span>
          <span class="value">{{ detailRecord.createTime }}</span>
        </div>
        <div class="detail-item">
          <span class="label">更新时间：</span>
          <span class="value">{{ detailRecord.updateTime }}</span>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { IconDelete, IconPlus } from "@arco-design/web-vue/es/icon";
import * as peiyangrenguanlianguanli from "@/api/peiyangrenguanlianguanli";
import * as dangzuzhiguanli from "@/api/dangzuzhiguanli";
import * as yonghuguanli from "@/api/yonghuguanli";
import { message } from "ant-design-vue";

// 状态管理
const loading = ref(false);
const relationList = ref<any[]>([]);
const total = ref(0);
const selectedRowKeys = ref<string[]>([]);
const addModalVisible = ref(false);
const detailModalVisible = ref(false);

// 搜索参数
const searchParams = ref({
  current: 1,
  pageSize: 10,
  orgId: "",
  keyword: "",
});

// 统计数据
const stats = ref({
  total: 0,
  active: 0,
});

// 组织列表
const orgList = ref<any[]>([]);

// 培养人列表
const trainerList = ref<any[]>([]);

// 培养对象列表
const menteeList = ref<any[]>([]);

// 新增表单数据
const addFormData = ref({
  trainerId: "",
  userId: "",
  startDate: new Date().toISOString().slice(0, 19).replace("T", " "),
});

// 新增表单验证规则
const addFormRules = ref({
  trainerId: [{ required: true, message: "请选择培养人", trigger: "change" }],
  userId: [{ required: true, message: "请选择培养对象", trigger: "change" }],
  startDate: [{ required: true, message: "请选择开始日期", trigger: "change" }],
});

// 详情记录
const detailRecord = ref<any>(null);

// 表单引用
const addFormRef = ref();

// 表格行选择
const rowSelection = ref({
  selectedRowKeys: selectedRowKeys.value,
  onChange: (keys: string[]) => {
    selectedRowKeys.value = keys;
  },
});

// 处理选择变化
const handleSelectionChange = (selectedRowKeys: string[]) => {
  selectedRowKeys.value = selectedRowKeys;
};

// 分页变化
const onPageChange = (current: number) => {
  searchParams.value.current = current;
  loadRelationList();
};

// 每页条数变化
const onSizeChange = (current: number, pageSize: number) => {
  searchParams.value.current = current;
  searchParams.value.pageSize = pageSize;
  loadRelationList();
};

// 加载组织列表
const loadOrgList = async () => {
  try {
    const res = await dangzuzhiguanli.listOrganizationsUsingGet();
    if (res.data.code === 0) {
      orgList.value = res.data.data || [];
    }
  } catch (error) {
    console.error("获取组织列表失败:", error);
  }
};

// 加载培养人列表
const loadTrainerList = async () => {
  try {
    const res = await peiyangrenguanlianguanli.listAvailableTrainersUsingGet(
      {},
    );
    if (res.data.code === 0) {
      trainerList.value = res.data.data || [];
    }
  } catch (error) {
    console.error("获取培养人列表失败:", error);
  }
};

// 加载培养对象列表
const loadMenteeList = async () => {
  try {
    const res = await yonghuguanli.listUserVoUsingPost({});
    if (res.data.code === 0) {
      menteeList.value = res.data.data || [];
    }
  } catch (error) {
    console.error("获取培养对象列表失败:", error);
  }
};

// 加载培养关系列表
const loadRelationList = async () => {
  loading.value = true;
  try {
    let res;
    // 无论是否选择了组织，都尝试获取培养关系列表
    // 当orgId为空时，尝试获取所有培养关系
    res = await peiyangrenguanlianguanli.getTrainerRelationsByOrgIdUsingGet({
      orgId: searchParams.value.orgId ? Number(searchParams.value.orgId) : 1,
    });
    console.log(res);
    if (res.data.code === 0) {
      // 转换数据结构，将用户列表转换为培养关系列表
      const userList = res.data.data || [];
      // 这里需要根据实际业务逻辑转换数据
      // 由于接口返回的是用户列表，我们需要构建培养关系数据
      relationList.value = userList.map((user: any) => ({
        id: user.id,
        trainerId: user.id, // 假设当前用户是培养人
        trainerName: user.userName,
        userId: user.id, // 假设当前用户也是培养对象
        userName: user.userName,
        startDate: user.startDate, // 使用加入日期作为开始日期
        endDate: undefined, // 没有结束日期
        status: 1, // 假设状态为活跃
        createTime: user.createTime, // 创建时间
        updateTime: user.updateTime, // 更新时间
      }));
      total.value = relationList.value.length;
    }
    // 更新统计数据
    updateStats(relationList.value);
  } catch (error) {
    console.error("获取培养关系列表失败:", error);
    message.error("网络请求异常");
  } finally {
    loading.value = false;
  }
};

// 更新统计数据
const updateStats = (relations: any[]) => {
  const newStats = {
    total: relations.length,
    active: 0,
  };

  relations.forEach((relation) => {
    if (relation.status === 1) {
      newStats.active++;
    }
  });

  stats.value = newStats;
};

// 搜索
const handleSearch = () => {
  searchParams.value.current = 1;
  loadRelationList();
};

// 重置
const handleReset = () => {
  searchParams.value = {
    current: 1,
    pageSize: 10,
    orgId: "",
    keyword: "",
  };
  loadRelationList();
};

// 显示新增模态框
const showAddModal = () => {
  addFormData.value = {
    trainerId: "",
    userId: "",
    startDate: new Date().toISOString().slice(0, 19).replace("T", " "),
  };
  addModalVisible.value = true;
};

// 关闭新增模态框
const handleAddModalCancel = () => {
  addModalVisible.value = false;
  addFormRef.value?.resetFields();
};

// 提交新增模态框
const handleAddModalOk = async () => {
  if (await addFormRef.value?.validate()) {
    try {
      const res = await peiyangrenguanlianguanli.addTrainerRelationUsingPost({
        trainerId: addFormData.value.trainerId,
        userId: addFormData.value.userId,
        startDate: addFormData.value.startDate,
      });
      if (res.data.code === 0) {
        addModalVisible.value = false;
        loadRelationList();
        message.success("新增培养关系成功");
      } else {
        message.error(res.data.message || "新增培养关系失败");
      }
    } catch (error) {
      console.error("新增培养关系失败:", error);
      message.error("网络请求异常");
    }
  }
};

// 显示详情模态框
const showDetailModal = (record: any) => {
  detailRecord.value = record;
  detailModalVisible.value = true;
};

// 关闭详情模态框
const handleDetailModalCancel = () => {
  detailModalVisible.value = false;
  detailRecord.value = null;
};

// 删除培养关系
const handleDelete = async (id: string) => {
  try {
    const res = await peiyangrenguanlianguanli.deleteTrainerRelationUsingPost({
      id,
    });
    if (res.data.code === 0) {
      loadRelationList();
      message.success("删除成功");
    } else {
      message.error(res.data.message || "删除失败");
    }
  } catch (error) {
    console.error("删除失败:", error);
    message.error("网络请求异常");
  }
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRowKeys.value.length === 0) return;
  try {
    for (const id of selectedRowKeys.value) {
      await peiyangrenguanlianguanli.deleteTrainerRelationUsingPost({ id });
    }
    loadRelationList();
    selectedRowKeys.value = [];
    message.success("批量删除成功");
  } catch (error) {
    console.error("批量删除失败:", error);
    message.error("网络请求异常");
  }
};

// 获取状态文本
const getStatusText = (status: number) => {
  switch (status) {
    case 1:
      return "进行中";
    case 2:
      return "已完成";
    case 3:
      return "已终止";
    default:
      return "未知";
  }
};

// 获取状态颜色
const getStatusColor = (status: number) => {
  switch (status) {
    case 1:
      return "green";
    case 2:
      return "blue";
    case 3:
      return "red";
    default:
      return "default";
  }
};

// 页面加载时获取数据
onMounted(async () => {
  await loadOrgList();
  await loadTrainerList();
  await loadMenteeList();
  await loadRelationList();
});
</script>

<style scoped>
.trainer-relation-management-container {
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

/* 统计卡片 */
.stats-container {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 150px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.stat-number {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #1890ff;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.search-form {
  padding: 20px;
}

/* 操作按钮区域 */
.action-bar {
  margin-bottom: 20px;
}

/* 列表卡片 */
.list-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 详情内容 */
.detail-content {
  margin: 20px 0;
}

.detail-item {
  margin-bottom: 12px;
  display: flex;
}

.detail-item .label {
  width: 100px;
  font-weight: 600;
  color: #333;
}

.detail-item .value {
  flex: 1;
  color: #666;
}

/* 滚动条优化 */
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

/* 响应式设计 */
@media (max-width: 768px) {
  .trainer-relation-management-container {
    padding: 10px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .stats-container {
    flex-direction: column;
  }

  .stat-card {
    min-width: 100%;
  }

  .search-form {
    padding: 12px;
  }
}
</style>
