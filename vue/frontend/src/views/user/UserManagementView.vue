<template>
  <div class="user-management-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>用户管理</a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <h1>用户管理</h1>
    </div>

    <!-- 统计卡片区域 -->
    <div v-if="!loading" class="stats-container">
      <div class="stat-card">
        <div class="stat-number">{{ stats.total }}</div>
        <div class="stat-label">总用户数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.teacher }}</div>
        <div class="stat-label">教师用户</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.student }}</div>
        <div class="stat-label">学生用户</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.admin }}</div>
        <div class="stat-label">管理员用户</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.member }}</div>
        <div class="stat-label">党员用户</div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <a-card class="search-card">
      <a-form :model="searchParams" class="search-form" layout="inline">
        <a-form-item label="用户类型">
          <a-select
            v-model="searchParams.userType"
            placeholder="请选择用户类型"
          >
            <a-option value="">全部</a-option>
            <a-option value="教师">教师</a-option>
            <a-option value="学生">学生</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="用户角色">
          <a-select
            v-model="searchParams.userRole"
            placeholder="请选择用户角色"
          >
            <a-option value="">全部</a-option>
            <a-option value="super_admin">超级管理员</a-option>
            <a-option value="org_admin">组织管理员</a-option>
            <a-option value="org_member">党员</a-option>
            <a-option value="activist_development">积极分子/发展对象</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="搜索">
          <a-input
            v-model="searchParams.keyword"
            allow-clear
            placeholder="请输入姓名/账号/手机号..."
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
          新增用户
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
        :data="userList"
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
          <a-table-column data-index="userName" title="姓名" width="120">
            <template #cell="{ record }">
              <a-tag color="blue">{{ record.userName }}</a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="userAccount" title="账号" width="150" />
          <a-table-column data-index="userType" title="用户类型" width="100">
            <template #cell="{ record }">
              <a-tag :color="record.userType === '教师' ? 'green' : 'orange'">
                {{ record.userType === "教师" ? "教师" : "学生" }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="userRole" title="用户角色" width="150">
            <template #cell="{ record }">
              <a-tag :color="getRoleColor(record.userRole)">
                {{ getRoleName(record.userRole) }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="phone" title="手机号" width="150" />
          <a-table-column data-index="email" title="邮箱" width="200" />
          <a-table-column
            data-index="politicalStatus"
            title="政治面貌"
            width="120"
          />
          <a-table-column data-index="orgId" title="所属党组织ID" width="150" />
          <a-table-column data-index="status" title="状态" width="100">
            <template #cell="{ record }">
              <a-tag :color="record.status === 0 ? 'green' : 'red'">
                {{ record.status === 0 ? "正常" : "禁用" }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column
            data-index="joinDate"
            title="申请入党日期"
            width="180"
          />
          <a-table-column
            data-index="positiveDate"
            title="转正日期"
            width="180"
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
                  @click="showEditModal(record)"
                >
                  编辑
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

    <!-- 新增/编辑用户模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      destroy-on-close
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
        <a-form-item field="userAccount" label="账号" required>
          <a-input v-model="formData.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item field="userName" label="姓名" required>
          <a-input v-model="formData.userName" placeholder="请输入姓名" />
        </a-form-item>
        <a-form-item field="password" label="密码" :required="!isEdit">
          <a-input-password
            v-model:value="formData.password"
            placeholder="请输入密码"
          />
        </a-form-item>
        <a-form-item field="phone" label="手机号">
          <a-input v-model="formData.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item field="userType" label="用户类型" required>
          <a-select v-model="formData.userType" placeholder="请选择用户类型">
            <a-option value="教师">教师</a-option>
            <a-option value="学生">学生</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="userRole" label="用户角色" required>
          <a-select v-model="formData.userRole" placeholder="请选择用户角色">
            <a-option value="super_admin">超级管理员</a-option>
            <a-option value="org_admin">组织管理员</a-option>
            <a-option value="org_member">党员</a-option>
            <a-option value="activist_development">积极分子/发展对象</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="phone" label="手机号">
          <a-input v-model="formData.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item field="email" label="邮箱">
          <a-input v-model="formData.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item field="politicalStatus" label="政治面貌">
          <a-select
            v-model="formData.politicalStatus"
            placeholder="请选择政治面貌"
          >
            <a-option value="共青团员">共青团员</a-option>
            <a-option value="党员">党员</a-option>
            <a-option value="预备党员">预备党员</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="orgId" label="所属党组织ID">
          <a-input
            v-model="formData.orgId"
            placeholder="请输入所属党组织ID"
            type="number"
          />
        </a-form-item>
        <a-form-item field="status" label="状态" required>
          <a-select v-model="formData.status" placeholder="请选择状态">
            <a-option value="0">正常</a-option>
            <a-option value="1">禁用</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="joinDate" label="申请入党日期">
          <a-date-picker
            v-model="formData.joinDate"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
            placeholder="请选择申请入党日期"
          />
        </a-form-item>
        <a-form-item field="positiveDate" label="转正日期">
          <a-date-picker
            v-model="formData.positiveDate"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
            placeholder="请选择转正日期"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { IconPlus, IconDelete } from "@arco-design/web-vue/es/icon";
import * as yonghuguanli from "@/api/yonghuguanli";

// 状态管理
const loading = ref(false);
const userList = ref<any[]>([]);
const total = ref(0);
const selectedRowKeys = ref<string[]>([]);
const modalVisible = ref(false);
const isEdit = ref(false);

// 统计数据
const stats = ref({
  total: 0,
  teacher: 0,
  student: 0,
  admin: 0,
  member: 0,
});

// 搜索参数
const searchParams = ref({
  current: 1,
  pageSize: 10,
  keyword: "",
  userType: "",
  userRole: "",
});

// 表单数据
const formData = ref({
  id: "",
  userAccount: "",
  userName: "",
  password: "",
  phone: "",
  email: "",
  politicalStatus: "",
  orgId: "",
  status: "0",
  joinDate: null,
  positiveDate: null,
  userType: "学生",
  userRole: "org_member",
});

// 表单验证规则
const formRules = ref({
  userAccount: [{ required: true, message: "请输入账号", trigger: "blur" }],
  userName: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  userType: [{ required: true, message: "请选择用户类型", trigger: "change" }],
  userRole: [{ required: true, message: "请选择用户角色", trigger: "change" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }],
});

// 表单引用
const formRef = ref();

// 角色映射
const roleMap = {
  super_admin: "超级管理员",
  org_admin: "组织管理员",
  org_member: "党员",
  activist_development: "积极分子/发展对象",
};

// 角色颜色
const roleColorMap = {
  super_admin: "#f5222d",
  org_admin: "#1890ff",
  org_member: "#52c41a",
  activist_development: "#faad14",
};

// 获取角色名称
const getRoleName = (role: string) => {
  return roleMap[role] || role;
};

// 获取角色颜色
const getRoleColor = (role: string) => {
  return roleColorMap[role] || "#999";
};

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
  loadUserList();
};

// 每页条数变化
const onSizeChange = (current: number, pageSize: number) => {
  searchParams.value.current = current;
  searchParams.value.pageSize = pageSize;
  loadUserList();
};

// 加载用户列表
const loadUserList = async () => {
  loading.value = true;
  try {
    const res = await yonghuguanli.listUserVoByPageUsingPost({
      pageNum: searchParams.value.current,
      pageSize: searchParams.value.pageSize,
      keyword: searchParams.value.keyword,
      userType: searchParams.value.userType,
      userRole: searchParams.value.userRole,
    });
    if (res.data.code === 0) {
      userList.value = res.data.data?.records || [];
      total.value = res.data.data?.total || 0;
      // 更新统计数据
      updateStats(userList.value);
    } else {
      console.error("获取用户列表失败:", res.data.message);
    }
  } catch (error) {
    console.error("获取用户列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 更新统计数据
const updateStats = (users: any[]) => {
  const newStats = {
    total: users.length,
    teacher: 0,
    student: 0,
    admin: 0,
    member: 0,
  };

  users.forEach((user) => {
    // 统计用户类型
    if (user.userType === "教师") {
      newStats.teacher++;
    } else if (user.userType === "学生") {
      newStats.student++;
    }

    // 统计用户角色
    if (user.userRole === "super_admin" || user.userRole === "org_admin") {
      newStats.admin++;
    }

    // 统计党员用户（包括党员和预备党员）
    if (
      user.politicalStatus === "党员" ||
      user.politicalStatus === "预备党员"
    ) {
      newStats.member++;
    }
  });

  stats.value = newStats;
};

// 搜索
const handleSearch = () => {
  searchParams.value.current = 1;
  loadUserList();
};

// 重置
const handleReset = () => {
  searchParams.value = {
    current: 1,
    pageSize: 10,
    keyword: "",
    userType: "",
    userRole: "",
  };
  loadUserList();
};

// 显示新增模态框
const showAddModal = () => {
  isEdit.value = false;
  formData.value = {
    id: "",
    userAccount: "",
    userName: "",
    password: "",
    phone: "",
    userType: "student",
    userRole: "org_member",
  };
  modalVisible.value = true;
};

// 显示编辑模态框
const showEditModal = (record: any) => {
  isEdit.value = true;
  formData.value = {
    id: record.id,
    userAccount: record.userAccount,
    userName: record.userName,
    password: "",
    phone: record.phone || "",
    email: record.email || "",
    politicalStatus: record.politicalStatus || "",
    orgId: record.orgId || "",
    status: record.status?.toString() || "0",
    joinDate: record.joinDate || null,
    positiveDate: record.positiveDate || null,
    userType: record.userType,
    userRole: record.userRole,
  };
  modalVisible.value = true;
};

// 关闭模态框
const handleModalCancel = () => {
  modalVisible.value = false;
  formRef.value?.resetFields();
};

// 提交模态框
const handleModalOk = async () => {
  if (await formRef.value?.validate()) {
    try {
      if (isEdit.value) {
        // 编辑用户
        const res = await yonghuguanli.updateUserUsingPost({
          id: formData.value.id,
          userAccount: formData.value.userAccount,
          userName: formData.value.userName,
          password: formData.value.password || undefined,
          phone: formData.value.phone,
          email: formData.value.email,
          politicalStatus: formData.value.politicalStatus,
          orgId: formData.value.orgId,
          status: formData.value.status,
          joinDate: formData.value.joinDate,
          positiveDate: formData.value.positiveDate,
          userType: formData.value.userType,
          userRole: formData.value.userRole,
        });
        if (res.data.code === 0) {
          modalVisible.value = false;
          loadUserList();
          console.log("编辑用户成功");
        } else {
          console.error("编辑用户失败:", res.data.message);
        }
      } else {
        // 新增用户
        const res = await yonghuguanli.addUserUsingPost({
          userAccount: formData.value.userAccount,
          userName: formData.value.userName,
          password: formData.value.password,
          phone: formData.value.phone,
          email: formData.value.email,
          politicalStatus: formData.value.politicalStatus,
          orgId: formData.value.orgId,
          status: formData.value.status,
          joinDate: formData.value.joinDate,
          positiveDate: formData.value.positiveDate,
          userType: formData.value.userType,
          userRole: formData.value.userRole,
        });
        if (res.data.code === 0) {
          modalVisible.value = false;
          loadUserList();
          console.log("新增用户成功");
        } else {
          console.error("新增用户失败:", res.data.message);
        }
      }
    } catch (error) {
      console.error("操作失败:", error);
    }
  }
};

// 删除用户
const handleDelete = async (id: string) => {
  try {
    const res = await yonghuguanli.deleteUserUsingPost({ id });
    if (res.data.code === 0) {
      loadUserList();
      console.log("删除用户成功");
    } else {
      console.error("删除用户失败:", res.data.message);
    }
  } catch (error) {
    console.error("删除用户失败:", error);
  }
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRowKeys.value.length === 0) return;
  try {
    for (const id of selectedRowKeys.value) {
      await yonghuguanli.deleteUserUsingPost({ id });
    }
    loadUserList();
    selectedRowKeys.value = [];
    console.log("批量删除成功");
  } catch (error) {
    console.error("批量删除失败:", error);
  }
};

// 页面加载时获取用户列表
onMounted(() => {
  loadUserList();
});
</script>

<style scoped>
.user-management-container {
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
  .user-management-container {
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
