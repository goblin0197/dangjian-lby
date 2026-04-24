<template>
  <div class="organization-relation-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>组织管理</a-breadcrumb-item>
          <a-breadcrumb-item>组织关系转移</a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <h1>组织关系转移</h1>
    </div>

    <!-- 统计卡片区域 -->
    <div v-if="!loading" class="stats-container">
      <div class="stat-card">
        <div class="stat-number">{{ stats.total }}</div>
        <div class="stat-label">总申请数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.pending }}</div>
        <div class="stat-label">待审批</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.approved }}</div>
        <div class="stat-label">已通过</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.rejected }}</div>
        <div class="stat-label">已拒绝</div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <a-card class="search-card">
      <a-form :model="searchParams" class="search-form" layout="inline">
        <a-form-item label="组织">
          <a-select v-model:value="searchParams.orgId" placeholder="请选择组织">
            <a-option value="">全部</a-option>
            <a-option v-for="org in orgList" :key="org.id" :value="org.id">
              {{ org.orgName }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态">
          <a-select
            v-model:value="searchParams.status"
            placeholder="请选择状态"
          >
            <a-option value="">全部</a-option>
            <a-option value="0">待审批</a-option>
            <a-option value="1">已通过</a-option>
            <a-option value="2">已拒绝</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="搜索">
          <a-input
            v-model:value="searchParams.keyword"
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
          新增转移申请
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
          <a-table-column data-index="userId" title="申请人ID" width="120">
            <template #cell="{ record }">
              <a-tag color="blue">{{ record.userId }}</a-tag>
            </template>
          </a-table-column>
          <a-table-column
            data-index="fromOrgId"
            title="原党组织ID"
            width="150"
          />
          <a-table-column data-index="fromOrgName" title="原组织" width="150" />
          <a-table-column
            data-index="toOrgId"
            title="目标党组织ID"
            width="150"
          />
          <a-table-column data-index="toOrgName" title="目标组织" width="150" />
          <a-table-column
            data-index="transferReason"
            title="转移原因"
            width="200"
          />
          <a-table-column
            data-index="transferTime"
            title="申请时间"
            width="180"
          />
          <a-table-column data-index="approveStatus" title="状态" width="100">
            <template #cell="{ record }">
              <a-tag :color="getStatusColor(record.approveStatus)">
                {{ getStatusName(record.approveStatus) }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column
            data-index="approveUserId"
            title="审批人ID"
            width="150"
          />
          <a-table-column
            data-index="approveUserName"
            title="审批人姓名"
            width="150"
          />
          <a-table-column
            data-index="approveTime"
            title="审批时间"
            width="180"
          />
          <a-table-column
            data-index="approveComment"
            title="审批意见"
            width="200"
          />
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
                <a-button
                  v-if="record.approveStatus === 1"
                  size="small"
                  type="primary"
                  @click="showApproveModal(record)"
                >
                  审批
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

    <!-- 新增转移申请模态框 -->
    <a-modal
      v-model:visible="addModalVisible"
      title="新增转移申请"
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
        <a-form-item field="userId" label="申请人" required>
          <a-select
            v-model="addFormData.userId"
            placeholder="请选择申请人"
            @change="handleUserIdChange"
          >
            <a-option v-for="user in userList" :key="user.id" :value="user.id">
              {{ user.userName }} ({{ user.userAccount }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="fromOrgId" label="原组织" required>
          <a-select v-model="addFormData.fromOrgId" placeholder="请选择原组织">
            <a-option v-for="org in orgList" :key="org.id" :value="org.id">
              {{ org.orgName }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="原组织名称" v-if="addFormData.fromOrgName">
          <a-input
            v-model="addFormData.fromOrgName"
            readonly
            placeholder="原组织名称"
          />
        </a-form-item>
        <a-form-item field="toOrgId" label="目标组织" required>
          <a-select v-model="addFormData.toOrgId" placeholder="请选择目标组织">
            <a-option v-for="org in orgList" :key="org.id" :value="org.id">
              {{ org.orgName }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="reason" label="转移原因" required>
          <a-textarea
            v-model="addFormData.reason"
            placeholder="请输入转移原因"
            rows="4"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 审批转移申请模态框 -->
    <a-modal
      v-model:visible="approveModalVisible"
      title="审批转移申请"
      destroy-on-close
      width="600px"
      @cancel="handleApproveModalCancel"
      @ok="handleApproveModalOk"
    >
      <div v-if="approveRecord" class="approve-detail">
        <div class="approve-item">
          <span class="label">申请人ID：</span>
          <span class="value">{{ approveRecord.userId }}</span>
        </div>
        <div class="approve-item">
          <span class="label">原组织：</span>
          <span class="value">{{ approveRecord.fromOrgName }}</span>
        </div>
        <div class="approve-item">
          <span class="label">目标组织：</span>
          <span class="value">{{ approveRecord.toOrgName }}</span>
        </div>
        <div class="approve-item">
          <span class="label">申请时间：</span>
          <span class="value">{{ approveRecord.transferTime }}</span>
        </div>
        <div class="approve-item">
          <span class="label">转移原因：</span>
          <span class="value">{{ approveRecord.transferReason }}</span>
        </div>
        <a-form-item field="approveStatus" label="审批结果" required>
          <a-radio-group v-model="approveFormData.approveStatus">
            <a-radio value="2">通过</a-radio>
            <a-radio value="3">拒绝</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item field="approveRemark" label="审批意见">
          <a-textarea
            v-model="approveFormData.approveRemark"
            placeholder="请输入审批意见"
            rows="4"
          />
        </a-form-item>
      </div>
    </a-modal>

    <!-- 查看详情模态框 -->
    <a-modal
      v-model:visible="detailModalVisible"
      title="转移申请详情"
      destroy-on-close
      width="800px"
      @cancel="handleDetailModalCancel"
    >
      <div v-if="detailRecord" class="detail-content">
        <div class="detail-item">
          <span class="label">申请人ID：</span>
          <span class="value">{{ detailRecord.userId }}</span>
        </div>
        <div class="detail-item">
          <span class="label">原党组织ID：</span>
          <span class="value">{{ detailRecord.fromOrgId }}</span>
        </div>
        <div class="detail-item">
          <span class="label">原组织：</span>
          <span class="value">{{ detailRecord.fromOrgName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">目标党组织ID：</span>
          <span class="value">{{ detailRecord.toOrgId }}</span>
        </div>
        <div class="detail-item">
          <span class="label">目标组织：</span>
          <span class="value">{{ detailRecord.toOrgName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">转移原因：</span>
          <span class="value">{{ detailRecord.transferReason }}</span>
        </div>
        <div class="detail-item">
          <span class="label">申请时间：</span>
          <span class="value">{{ detailRecord.transferTime }}</span>
        </div>
        <div class="detail-item">
          <span class="label">审批状态：</span>
          <span class="value">{{
            getStatusName(detailRecord.approveStatus)
          }}</span>
        </div>
        <div class="detail-item" v-if="detailRecord.approveUserId">
          <span class="label">审批人ID：</span>
          <span class="value">{{ detailRecord.approveUserId }}</span>
        </div>
        <div class="detail-item" v-if="detailRecord.approveUserName">
          <span class="label">审批人姓名：</span>
          <span class="value">{{ detailRecord.approveUserName }}</span>
        </div>
        <div class="detail-item" v-if="detailRecord.approveTime">
          <span class="label">审批时间：</span>
          <span class="value">{{ detailRecord.approveTime }}</span>
        </div>
        <div class="detail-item" v-if="detailRecord.approveComment">
          <span class="label">审批意见：</span>
          <span class="value">{{ detailRecord.approveComment }}</span>
        </div>
        <div class="detail-item" v-if="detailRecord.createTime">
          <span class="label">创建时间：</span>
          <span class="value">{{ detailRecord.createTime }}</span>
        </div>
        <div class="detail-item" v-if="detailRecord.updateTime">
          <span class="label">更新时间：</span>
          <span class="value">{{ detailRecord.updateTime }}</span>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive } from "vue";
import { IconPlus, IconDelete } from "@arco-design/web-vue/es/icon";
import * as zuzhiguanxizhuanyiguanli from "@/api/zuzhiguanxizhuanyiguanli";
import * as yonghuguanli from "@/api/yonghuguanli";
import * as dangzuzhiguanli from "@/api/dangzuzhiguanli";
import { Message } from "ant-design-vue";

// 状态管理
const loading = ref(false);
const relationList = ref<any[]>([]);
const total = ref(0);
const selectedRowKeys = ref<string[]>([]);
const addModalVisible = ref(false);
const approveModalVisible = ref(false);
const detailModalVisible = ref(false);

// 搜索参数
const searchParams = ref({
  current: 1,
  pageSize: 10,
  orgId: "",
  status: "",
  keyword: "",
});

// 统计数据
const stats = ref({
  total: 0,
  pending: 0,
  approved: 0,
  rejected: 0,
});

// 组织列表
const orgList = ref<any[]>([]);

// 用户列表
const userList = ref<any[]>([]);

// 新增表单数据
const addFormData = ref({
  userId: "",
  fromOrgId: "",
  toOrgId: "",
  reason: "",
});

// 新增表单验证规则
const addFormRules = ref({
  userId: [{ required: true, message: "请选择申请人", trigger: "change" }],
  fromOrgId: [{ required: true, message: "请选择原组织", trigger: "change" }],
  toOrgId: [{ required: true, message: "请选择目标组织", trigger: "change" }],
  reason: [{ required: true, message: "请输入转移原因", trigger: "blur" }],
});

// 审批表单数据
const approveFormData = ref({
  approveStatus: "1",
  approveRemark: "",
});

// 审批记录
const approveRecord = ref<any>(null);

// 详情记录
const detailRecord = ref<any>(null);

// 表单引用
const addFormRef = ref();

// 表格行选择
const rowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});

// 处理选择变化
const handleSelectionChange = (keys: any) => {
  selectedRowKeys.value = keys;
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

// 加载用户列表
const loadUserList = async () => {
  try {
    const res = await yonghuguanli.listUserVoUsingPost({});
    if (res.data.code === 0) {
      userList.value = res.data.data || [];
    }
  } catch (error) {
    console.error("获取用户列表失败:", error);
  }
};

// 加载组织关系转移列表
const loadRelationList = async () => {
  loading.value = true;
  try {
    const res = await zuzhiguanxizhuanyiguanli.pageOrgRelationTransfersUsingGet(
      {
        pageNum: searchParams.value.current,
        pageSize: searchParams.value.pageSize,
        orgId: searchParams.value.orgId || undefined,
        status: searchParams.value.status || undefined,
        keyword: searchParams.value.keyword || undefined,
      },
    );
    if (res.data.code === 0) {
      relationList.value = res.data.data?.records || [];
      total.value = res.data.data?.total || 0;
      // 更新统计数据
      updateStats(relationList.value);
    } else {
      Message.error(res.data.message || "获取组织关系转移列表失败");
    }
  } catch (error) {
    console.error("获取组织关系转移列表失败:", error);
    Message.error("网络请求异常");
  } finally {
    loading.value = false;
  }
};

// 更新统计数据
const updateStats = (relations: any[]) => {
  const newStats = {
    total: relations.length,
    pending: 0,
    approved: 0,
    rejected: 0,
  };

  relations.forEach((relation) => {
    switch (relation.approveStatus) {
      case 1:
        newStats.pending++;
        break;
      case 2:
        newStats.approved++;
        break;
      case 3:
        newStats.rejected++;
        break;
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
    status: "",
    keyword: "",
  };
  loadRelationList();
};

// 显示新增模态框
const showAddModal = () => {
  addFormData.value = {
    userId: "",
    fromOrgId: "",
    fromOrgName: "",
    toOrgId: "",
    reason: "",
  };
  addModalVisible.value = true;
};

// 关闭新增模态框
const handleAddModalCancel = () => {
  addModalVisible.value = false;
  addFormRef.value?.resetFields();
};

// 处理申请人选择变化
const handleUserIdChange = (userId: string) => {
  // 查找选中的用户
  const selectedUser = userList.value.find((user) => user.id === userId);
  if (selectedUser && selectedUser.orgId) {
    // 自动设置原组织ID
    addFormData.value.fromOrgId = selectedUser.orgId.toString();
    // 查找原组织名称
    const selectedOrg = orgList.value.find(
      (org) => org.id === selectedUser.orgId,
    );
    if (selectedOrg) {
      // 自动设置原组织名称
      addFormData.value.fromOrgName = selectedOrg.orgName;
    } else {
      addFormData.value.fromOrgName = "";
    }
  } else {
    // 如果找不到用户或用户没有组织，清空原组织
    addFormData.value.fromOrgId = "";
    addFormData.value.fromOrgName = "";
  }
};

// 提交新增模态框
const handleAddModalOk = async () => {
  if (await addFormRef.value?.validate()) {
    try {
      const res =
        await zuzhiguanxizhuanyiguanli.addOrgRelationTransferUsingPost({
          userId: addFormData.value.userId,
          fromOrgId: addFormData.value.fromOrgId,
          toOrgId: addFormData.value.toOrgId,
          reason: addFormData.value.reason,
        });
      if (res.data.code === 0) {
        addModalVisible.value = false;
        loadRelationList();
        Message.success("新增转移申请成功");
      } else {
        Message.error(res.data.message || "新增转移申请失败");
      }
    } catch (error) {
      console.error("新增转移申请失败:", error);
      Message.error("网络请求异常");
    }
  }
};

// 显示审批模态框
const showApproveModal = (record: any) => {
  approveRecord.value = record;
  approveFormData.value = {
    approveStatus: "1",
    approveRemark: "",
  };
  approveModalVisible.value = true;
};

// 关闭审批模态框
const handleApproveModalCancel = () => {
  approveModalVisible.value = false;
  approveRecord.value = null;
};

// 提交审批模态框
const handleApproveModalOk = async () => {
  if (!approveRecord.value) return;
  try {
    const res =
      await zuzhiguanxizhuanyiguanli.approveOrgRelationTransferUsingPost({
        id: Number(approveRecord.value.id),
        approveStatus: Number(approveFormData.value.approveStatus),
        approveComment: approveFormData.value.approveRemark,
      });
    if (res.data.code === 0) {
      approveModalVisible.value = false;
      approveRecord.value = null;
      loadRelationList();
      Message.success("审批成功");
    } else {
      Message.error(res.data.message || "审批失败");
    }
  } catch (error) {
    console.error("审批失败:", error);
    Message.error("网络请求异常");
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

// 删除组织关系转移记录
const handleDelete = async (id: string) => {
  try {
    const res =
      await zuzhiguanxizhuanyiguanli.deleteOrgRelationTransferUsingPost({ id });
    if (res.data.code === 0) {
      loadRelationList();
      Message.success("删除成功");
    } else {
      Message.error(res.data.message || "删除失败");
    }
  } catch (error) {
    console.error("删除失败:", error);
    Message.error("网络请求异常");
  }
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRowKeys.value.length === 0) return;
  try {
    for (const id of selectedRowKeys.value) {
      await zuzhiguanxizhuanyiguanli.deleteOrgRelationTransferUsingPost({ id });
    }
    loadRelationList();
    selectedRowKeys.value = [];
    Message.success("批量删除成功");
  } catch (error) {
    console.error("批量删除失败:", error);
    Message.error("网络请求异常");
  }
};

// 获取状态颜色
const getStatusColor = (status: number) => {
  switch (status) {
    case 1:
      return "blue";
    case 2:
      return "green";
    case 3:
      return "red";
    default:
      return "gray";
  }
};

// 获取状态名称
const getStatusName = (status: number) => {
  switch (status) {
    case 1:
      return "待审批";
    case 2:
      return "已通过";
    case 3:
      return "已拒绝";
    default:
      return "未知";
  }
};

// 页面加载时获取数据
onMounted(async () => {
  await loadOrgList();
  await loadUserList();
  await loadRelationList();
});
</script>

<style scoped>
.organization-relation-container {
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

/* 审批详情 */
.approve-detail {
  margin: 20px 0;
}

.approve-item {
  margin-bottom: 12px;
  display: flex;
}

.approve-item .label {
  width: 100px;
  font-weight: 600;
  color: #333;
}

.approve-item .value {
  flex: 1;
  color: #666;
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
  .organization-relation-container {
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
