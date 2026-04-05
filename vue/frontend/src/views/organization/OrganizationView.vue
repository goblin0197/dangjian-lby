<template>
  <div class="organization-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>组织管理</h1>
      <div class="header-actions">
        <a-button type="primary" @click="showAddOrganizationModal">
          <icon-plus />
          新增组织
        </a-button>
      </div>
    </div>

    <!-- 主要内容区 -->
    <a-row :gutter="20">
      <!-- 左侧组织架构树 -->
      <a-col :span="6">
        <a-card title="组织架构" class="organization-tree-card">
          <div class="tree-container">
            <a-tree
                v-model:selectedKeys="selectedOrgKeys"
                :tree-data="organizationTree"
                :checkable="false"
                :default-expand-all="true"
                @select="handleOrgSelect"
            >
              <template #title="{ data }">
                <div class="tree-node">
                  <span>{{ data.title }}</span>
                  <a-dropdown :trigger="['hover']" placement="right">
                    <a-icon
                        key="more"
                        icon="icon-more"
                        class="node-action-icon"
                    />
                    <template #content>
                      <a-menu>
                        <a-menu-item @click="handleAddSubOrg(data.id)">
                          <icon-plus />
                          新增子组织
                        </a-menu-item>
                        <a-menu-item @click="handleEditOrg(data.id)">
                          <icon-edit />
                          编辑组织
                        </a-menu-item>
                        <a-menu-item @click="handleDeleteOrg(data.id)" danger>
                          <icon-delete />
                          删除组织
                        </a-menu-item>
                      </a-menu>
                    </template>
                  </a-dropdown>
                </div>
              </template>
            </a-tree>
          </div>
        </a-card>
      </a-col>

      <!-- 右侧组织详情和成员管理 -->
      <a-col :span="18">
        <a-card v-if="selectedOrg" :title="selectedOrg.title">
          <div class="org-info">
            <a-descriptions title="组织信息" :column="3">
              <a-descriptions-item label="组织ID"
              >{{ selectedOrg.id }}
              </a-descriptions-item>
              <a-descriptions-item label="组织类型"
              >{{ orgTypeMap[selectedOrg.type] }}
              </a-descriptions-item>
              <a-descriptions-item label="成立时间"
              >{{ formatDate(selectedOrg.createTime) }}
              </a-descriptions-item>
              <a-descriptions-item label="负责人"
              >{{ selectedOrg.leader || "未设置" }}
              </a-descriptions-item>
              <a-descriptions-item label="成员数量"
              >{{ memberList.length }}
              </a-descriptions-item>
              <a-descriptions-item label="联系电话"
              >{{ selectedOrg.contactPhone || "未设置" }}
              </a-descriptions-item>
            </a-descriptions>
          </div>

          <!-- 成员管理标签页 -->
          <a-tabs
              v-model:active-key="activeTabKey"
              class="member-tabs"
              style="margin-top: 20px"
          >
            <a-tab-pane key="member-list" title="成员列表">
              <div class="member-list-header">
                <a-input-search
                    placeholder="搜索成员"
                    v-model:value="searchMemberText"
                    style="width: 300px"
                    @search="handleSearchMember"
                />
                <a-button type="primary" @click="showAddMemberModal">
                  <icon-plus />
                  添加成员
                </a-button>
              </div>

              <a-table
                  :data="memberList"
                  :columns="memberColumns"
                  :pagination="pagination"
                  row-key="id"
              >
                <template #cell(role)="{ record }">
                  <a-tag :color="roleColorMap[record.role]">
                    {{ roleMap[record.role] }}
                  </a-tag>
                </template>
                <template #cell(actions)="{ record }">
                  <a-space size="small">
                    <a-button
                        size="small"
                        type="text"
                        @click="handleEditMember(record)"
                    >
                      编辑
                    </a-button>
                    <a-button
                        size="small"
                        type="text"
                        @click="handleRemoveMember(record.id)"
                        danger
                    >
                      移除
                    </a-button>
                    <a-button
                        size="small"
                        type="text"
                        @click="showTransferModal(record)"
                    >
                      组织关系转移
                    </a-button>
                  </a-space>
                </template>
              </a-table>
            </a-tab-pane>

            <a-tab-pane key="mentor-relation" title="培养人对接">
              <div class="relation-header">
                <a-button type="primary" @click="showAddRelationModal">
                  <icon-plus />
                  添加对接关系
                </a-button>
              </div>

              <a-table
                  :data="mentorRelationList"
                  :columns="relationColumns"
                  :pagination="false"
                  row-key="id"
              >
                <template #cell(mentor)="{ record }">
                  <div class="user-info">
                    <a-avatar :src="record.mentor.avatar" size="small" />
                    <span>{{ record.mentor.name }}</span>
                  </div>
                </template>
                <template #cell(mentee)="{ record }">
                  <div class="user-info">
                    <a-avatar :src="record.mentee.avatar" size="small" />
                    <span>{{ record.mentee.name }}</span>
                  </div>
                </template>
                <template #cell(menteeType)="{ record }">
                  <a-tag>{{ menteeTypeMap[record.menteeType] }}</a-tag>
                </template>
                <template #cell(actions)="{ record }">
                  <a-button
                      size="small"
                      type="text"
                      @click="handleRemoveRelation(record.id)"
                      danger
                  >
                    解除
                  </a-button>
                </template>
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <div v-else class="no-org-selected">
          <a-empty description="请选择一个组织查看详情" />
        </div>
      </a-col>
    </a-row>

    <!-- 新增/编辑组织弹窗 -->
    <a-modal
        v-model:visible="orgModalVisible"
        :title="orgModalTitle"
        @ok="handleOrgModalOk"
        @cancel="handleOrgModalCancel"
    >
      <a-form
          ref="orgFormRef"
          :model="orgForm"
          :rules="orgFormRules"
          layout="vertical"
      >
        <a-form-item field="title" label="组织名称" required>
          <a-input v-model:value="orgForm.title" placeholder="请输入组织名称" />
        </a-form-item>
        <a-form-item field="type" label="组织类型" required>
          <a-select v-model:value="orgForm.type" placeholder="请选择组织类型">
            <a-select-option value="party_committee">党委</a-select-option>
            <a-select-option value="general_branch">党总支</a-select-option>
            <a-select-option value="branch">党支部</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item field="leader" label="负责人">
          <a-input
              v-model:value="orgForm.leader"
              placeholder="请输入负责人姓名"
          />
        </a-form-item>
        <a-form-item field="contactPhone" label="联系电话">
          <a-input
              v-model:value="orgForm.contactPhone"
              placeholder="请输入联系电话"
          />
        </a-form-item>
        <a-form-item field="description" label="组织描述">
          <a-textarea
              v-model:value="orgForm.description"
              placeholder="请输入组织描述"
              :rows="4"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 添加成员弹窗 -->
    <a-modal
        v-model:visible="memberModalVisible"
        title="添加组织成员"
        @ok="handleMemberModalOk"
        @cancel="handleMemberModalCancel"
    >
      <a-form
          ref="memberFormRef"
          :model="memberForm"
          :rules="memberFormRules"
          layout="vertical"
      >
        <a-form-item field="userId" label="选择成员" required>
          <a-select
              v-model:value="memberForm.userId"
              placeholder="请选择成员"
              show-search
              filter-option="contains"
          >
            <a-select-option
                v-for="user in userList"
                :key="user.id"
                :value="user.id"
            >
              {{ user.name }} ({{ user.username }})
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item field="role" label="成员角色" required>
          <a-select
              v-model:value="memberForm.role"
              placeholder="请选择成员角色"
          >
            <a-select-option value="leader">负责人</a-select-option>
            <a-select-option value="member">普通成员</a-select-option>
            <a-select-option value="mentor">培养联系人</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item field="joinDate" label="加入日期" required>
          <a-date-picker v-model:value="memberForm.joinDate" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 组织关系转移弹窗 -->
    <a-modal
        v-model:visible="transferModalVisible"
        title="组织关系转移"
        @ok="handleTransferModalOk"
        @cancel="handleTransferModalCancel"
    >
      <a-form
          ref="transferFormRef"
          :model="transferForm"
          :rules="transferFormRules"
          layout="vertical"
      >
        <a-form-item field="memberName" label="成员" disabled>
          <a-input v-model:value="transferForm.memberName" />
        </a-form-item>
        <a-form-item field="sourceOrg" label="原组织" disabled>
          <a-input v-model:value="transferForm.sourceOrg" />
        </a-form-item>
        <a-form-item field="targetOrgId" label="目标组织" required>
          <a-tree-select
              v-model:value="transferForm.targetOrgId"
              :tree-data="organizationTree"
              placeholder="请选择目标组织"
              style="width: 100%"
          />
        </a-form-item>
        <a-form-item field="transferDate" label="转移日期" required>
          <a-date-picker v-model:value="transferForm.transferDate" />
        </a-form-item>
        <a-form-item field="reason" label="转移原因" required>
          <a-textarea
              v-model:value="transferForm.reason"
              placeholder="请输入转移原因"
              :rows="3"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 添加培养人对接关系弹窗 -->
    <a-modal
        v-model:visible="relationModalVisible"
        title="添加培养人对接关系"
        @ok="handleRelationModalOk"
        @cancel="handleRelationModalCancel"
    >
      <a-form
          ref="relationFormRef"
          :model="relationForm"
          :rules="relationFormRules"
          layout="vertical"
      >
        <a-form-item field="mentorId" label="培养人" required>
          <a-select
              v-model:value="relationForm.mentorId"
              placeholder="请选择培养人"
              show-search
              filter-option="contains"
          >
            <a-select-option
                v-for="user in mentorList"
                :key="user.id"
                :value="user.id"
            >
              {{ user.name }} ({{ user.username }})
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item field="menteeId" label="培养对象" required>
          <a-select
              v-model:value="relationForm.menteeId"
              placeholder="请选择培养对象"
              show-search
              filter-option="contains"
          >
            <a-select-option
                v-for="user in menteeList"
                :key="user.id"
                :value="user.id"
            >
              {{ user.name }} ({{ user.username }})
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item field="menteeType" label="培养对象类型" required>
          <a-select
              v-model:value="relationForm.menteeType"
              placeholder="请选择培养对象类型"
          >
            <a-select-option value="active">积极分子</a-select-option>
            <a-select-option value="developing">发展对象</a-select-option>
            <a-select-option value="probationary">预备党员</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item field="startDate" label="开始日期" required>
          <a-date-picker v-model:value="relationForm.startDate" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { IconDelete, IconEdit, IconPlus } from "@arco-design/web-vue/es/icon";
import type { TableColumn, TableProps } from "@arco-design/web-vue/es/table";
import type { FormInstance } from "@arco-design/web-vue/es/form";
import { Service } from "../../../generated";
import { Message } from "@arco-design/web-vue"; // 请根据实际路径调整

// 类型定义
interface Organization {
  id: string;
  title: string;
  type: "party_committee" | "general_branch" | "branch";
  parentId?: string;
  leader?: string;
  contactPhone?: string;
  createTime: string;
  description?: string;
  children?: Organization[];
}

interface Member {
  id: string;
  userId: string;
  name: string;
  username: string;
  avatar: string;
  role: "leader" | "member" | "mentor";
  joinDate: string;
  position?: string;
}

interface MentorRelation {
  id: string;
  mentor: {
    id: string;
    name: string;
    avatar: string;
  };
  mentee: {
    id: string;
    name: string;
    avatar: string;
  };
  menteeType: "active" | "developing" | "probationary";
  startDate: string;
}

// 状态管理
const selectedOrgKeys = ref<string[]>([]);
const selectedOrg = ref<Organization | null>(null);
const activeTabKey = ref("member-list");
const searchMemberText = ref("");
const pagination = ref<TableProps["pagination"]>({
  pageSize: 10,
  current: 1,
  total: 0,
});

// 弹窗状态
const orgModalVisible = ref(false);
const orgModalTitle = ref("新增组织");
const memberModalVisible = ref(false);
const transferModalVisible = ref(false);
const relationModalVisible = ref(false);

// 表单引用
const orgFormRef = ref<FormInstance>();
const memberFormRef = ref<FormInstance>();
const transferFormRef = ref<FormInstance>();
const relationFormRef = ref<FormInstance>();

// 表单数据
const orgForm = reactive({
  id: "",
  title: "",
  type: "branch",
  parentId: "",
  leader: "",
  contactPhone: "",
  description: "",
});

const memberForm = reactive({
  userId: "",
  role: "member",
  joinDate: new Date(),
});

const transferForm = reactive({
  memberId: "",
  memberName: "",
  sourceOrg: "",
  targetOrgId: "",
  transferDate: new Date(),
  reason: "",
});

const relationForm = reactive({
  mentorId: "",
  menteeId: "",
  menteeType: "active",
  startDate: new Date(),
});

// 表单验证规则
const orgFormRules = reactive({
  title: [{ required: true, message: "请输入组织名称", trigger: "blur" }],
  type: [{ required: true, message: "请选择组织类型", trigger: "change" }],
});

const memberFormRules = reactive({
  userId: [{ required: true, message: "请选择成员", trigger: "change" }],
  role: [{ required: true, message: "请选择成员角色", trigger: "change" }],
  joinDate: [{ required: true, message: "请选择加入日期", trigger: "change" }],
});

// 表单验证规则补全
const transferFormRules = reactive({
  targetOrgId: [
    { required: true, message: "请选择目标组织", trigger: "change" },
  ],
  transferDate: [
    { required: true, message: "请选择转移日期", trigger: "change" },
  ],
  reason: [{ required: true, message: "请输入转移原因", trigger: "blur" }],
});

const relationFormRules = reactive({
  mentorId: [{ required: true, message: "请选择培养人", trigger: "change" }],
  menteeId: [{ required: true, message: "请选择培养对象", trigger: "change" }],
  menteeType: [
    { required: true, message: "请选择培养对象类型", trigger: "change" },
  ],
  startDate: [{ required: true, message: "请选择开始日期", trigger: "change" }],
});

// 映射关系：枚举值转显示文本/样式
const orgTypeMap = {
  party_committee: "党委",
  general_branch: "党总支",
  branch: "党支部",
};

const roleMap = {
  leader: "负责人",
  member: "普通成员",
  mentor: "培养联系人",
};

const roleColorMap = {
  leader: "red",
  member: "blue",
  mentor: "green",
};

const menteeTypeMap = {
  active: "积极分子",
  developing: "发展对象",
  probationary: "预备党员",
};

const orgData = ref<Record<number, string>>({});
const orgOptions = ref<{ label: string; value: number }[]>([]);

// 模拟数据（实际项目中从API获取）
const organizationTree = ref<Organization[]>([
  {
    id: "1",
    title: "学校党委",
    type: "party_committee",
    createTime: "2023-01-01",
    leader: "张书记",
    contactPhone: "13800138000",
    children: [
      {
        id: "1-1",
        title: "计算机学院党总支",
        type: "general_branch",
        createTime: "2023-02-15",
        leader: "李院长",
        contactPhone: "13900139000",
        children: [
          {
            id: "1-1-1",
            title: "计算机学院教师党支部",
            type: "branch",
            createTime: "2023-03-01",
            leader: "王教授",
            contactPhone: "13700137000",
          },
          {
            id: "1-1-2",
            title: "计算机学院学生第一党支部",
            type: "branch",
            createTime: "2023-03-02",
            leader: "赵老师",
          },
        ],
      },
      {
        id: "1-2",
        title: "马克思主义学院党总支",
        type: "general_branch",
        createTime: "2023-02-20",
        leader: "陈院长",
      },
    ],
  },
]);

const memberList = ref<Member[]>([
  {
    id: "m1",
    userId: "u1",
    name: "王教授",
    username: "wangjiaoshou",
    avatar: "https://picsum.photos/id/1005/200",
    role: "leader",
    joinDate: "2023-03-01",
    position: "支部书记",
  },
  {
    id: "m2",
    userId: "u2",
    name: "李老师",
    username: "lilaoshi",
    avatar: "https://picsum.photos/id/1012/200",
    role: "mentor",
    joinDate: "2023-03-10",
    position: "组织委员",
  },
  {
    id: "m3",
    userId: "u3",
    name: "张同学",
    username: "zhangtongxue",
    avatar: "https://picsum.photos/id/1027/200",
    role: "member",
    joinDate: "2023-04-01",
    position: "学生党员",
  },
]);

const mentorRelationList = ref<MentorRelation[]>([
  {
    id: "r1",
    mentor: {
      id: "u2",
      name: "李老师",
      avatar: "https://picsum.photos/id/1012/200",
    },
    mentee: {
      id: "u4",
      name: "刘同学",
      avatar: "https://picsum.photos/id/1025/200",
    },
    menteeType: "active",
    startDate: "2023-05-10",
  },
  {
    id: "r2",
    mentor: {
      id: "u2",
      name: "李老师",
      avatar: "https://picsum.photos/id/1012/200",
    },
    mentee: {
      id: "u5",
      name: "孙同学",
      avatar: "https://picsum.photos/id/1074/200",
    },
    menteeType: "developing",
    startDate: "2023-06-01",
  },
]);

// 模拟用户列表（用于添加成员/培养关系）
const userList = ref([
  { id: "u1", name: "王教授", username: "wangjiaoshou" },
  { id: "u2", name: "李老师", username: "lilaoshi" },
  { id: "u3", name: "张同学", username: "zhangtongxue" },
  { id: "u4", name: "刘同学", username: "liutongxue" },
  { id: "u5", name: "孙同学", username: "suntongxue" },
]);

// 培养人列表（筛选教师党员）
const mentorList = computed(() =>
    userList.value.filter(
        (u) => u.name.includes("老师") || u.name.includes("教授")
    )
);

// 培养对象列表（筛选积极分子/发展对象/预备党员）
const menteeList = computed(() =>
    userList.value.filter((u) => u.name.includes("同学"))
);

// 成员表格列配置
const memberColumns = ref<TableColumn<Member>[]>([
  { title: "姓名", dataIndex: "name", width: 120 },
  { title: "用户名", dataIndex: "username", width: 150 },
  { title: "角色", dataIndex: "role", width: 120, slotName: "role" },
  { title: "加入日期", dataIndex: "joinDate", width: 150 },
  { title: "职务", dataIndex: "position", width: 150 },
  { title: "操作", width: 200, slotName: "actions" },
]);

// 培养关系表格列配置
const relationColumns = ref<TableColumn<MentorRelation>[]>([
  { title: "培养人", dataIndex: "mentor", slotName: "mentor", width: 180 },
  { title: "培养对象", dataIndex: "mentee", slotName: "mentee", width: 180 },
  {
    title: "对象类型",
    dataIndex: "menteeType",
    slotName: "menteeType",
    width: 120,
  },
  { title: "开始日期", dataIndex: "startDate", width: 150 },
  { title: "操作", width: 100, slotName: "actions" },
]);

// 生命周期：页面加载时默认选中第一个组织
onMounted(() => {
  if (organizationTree.value.length > 0) {
    selectedOrgKeys.value = [organizationTree.value[0].id];
    selectedOrg.value = organizationTree.value[0];
  }
});

// 组织选择事件
const handleOrgSelect = (keys: string[]) => {
  if (keys.length === 0) {
    selectedOrg.value = null;
    return;
  }
  // 从组织树中查找选中的组织（实际项目中可通过递归或后端接口获取详情）
  const findOrg = (tree: Organization[], id: string): Organization | null => {
    for (const org of tree) {
      if (org.id === id) return org;
      if (org.children) {
        const child = findOrg(org.children, id);
        if (child) return child;
      }
    }
    return null;
  };
  selectedOrg.value = findOrg(organizationTree.value, keys[0]) || null;
  // 实际项目中：加载该组织的成员列表和培养关系列表
};

// 新增/编辑组织弹窗
const showAddOrganizationModal = () => {
  orgModalTitle.value = "新增组织";
  Object.assign(orgForm, {
    id: "",
    title: "",
    type: "branch",
    parentId: selectedOrg.value?.id || "",
    leader: "",
    contactPhone: "",
    description: "",
  });
  orgModalVisible.value = true;
};

const handleAddSubOrg = (parentId: string) => {
  orgModalTitle.value = "新增子组织";
  Object.assign(orgForm, {
    id: "",
    title: "",
    type: "branch",
    parentId,
    leader: "",
    contactPhone: "",
    description: "",
  });
  orgModalVisible.value = true;
};

const handleEditOrg = (id: string) => {
  orgModalTitle.value = "编辑组织";
  // 查找组织信息（实际项目中从后端获取）
  const org = organizationTree.value.find((item) => item.id === id);
  if (org) {
    Object.assign(orgForm, { ...org });
    orgModalVisible.value = true;
  }
};

const handleOrgModalOk = async () => {
  if (await orgFormRef.value?.validate()) {
    // 实际项目中：调用新增/编辑组织接口
    console.log("组织表单提交:", orgForm);
    orgModalVisible.value = false;
    // 提交成功后刷新组织树
  }
};

const handleOrgModalCancel = () => {
  orgFormRef.value?.resetFields();
  orgModalVisible.value = false;
};

// 成员搜索
const handleSearchMember = (value: string) => {
  // 实际项目中：调用搜索接口，这里模拟过滤
  console.log("搜索成员:", value);
};

// 添加成员弹窗
const showAddMemberModal = () => {
  Object.assign(memberForm, {
    userId: "",
    role: "member",
    joinDate: new Date(),
  });
  memberModalVisible.value = true;
};

const handleMemberModalOk = async () => {
  if (await memberFormRef.value?.validate()) {
    // 实际项目中：调用添加成员接口
    console.log("添加成员:", memberForm);
    memberModalVisible.value = false;
  }
};

const handleMemberModalCancel = () => {
  memberFormRef.value?.resetFields();
  memberModalVisible.value = false;
};

// 移除成员
const handleRemoveMember = (memberId: string) => {
  // 实际项目中：调用删除接口，这里模拟删除
  memberList.value = memberList.value.filter((m) => m.id !== memberId);
};

// 组织关系转移弹窗
const showTransferModal = (member: Member) => {
  Object.assign(transferForm, {
    memberId: member.id,
    memberName: member.name,
    sourceOrg: selectedOrg.value?.title || "",
    targetOrgId: "",
    transferDate: new Date(),
    reason: "",
  });
  transferModalVisible.value = true;
};

const handleTransferModalOk = async () => {
  if (await transferFormRef.value?.validate()) {
    // 实际项目中：调用组织关系转移接口
    console.log("组织关系转移:", transferForm);
    transferModalVisible.value = false;
  }
};

const handleTransferModalCancel = () => {
  transferFormRef.value?.resetFields();
  transferModalVisible.value = false;
};

// 添加培养人对接关系弹窗
const showAddRelationModal = () => {
  Object.assign(relationForm, {
    mentorId: "",
    menteeId: "",
    menteeType: "active",
    startDate: new Date(),
  });
  relationModalVisible.value = true;
};

const handleRelationModalOk = async () => {
  if (await relationFormRef.value?.validate()) {
    // 实际项目中：调用添加对接关系接口
    console.log("添加培养关系:", relationForm);
    relationModalVisible.value = false;
  }
};

const handleRelationModalCancel = () => {
  relationFormRef.value?.resetFields();
  relationModalVisible.value = false;
};

// 解除培养关系
const handleRemoveRelation = (relationId: string) => {
  // 实际项目中：调用删除接口，这里模拟删除
  mentorRelationList.value = mentorRelationList.value.filter(
      (r) => r.id !== relationId
  );
};

// 日期格式化工具
const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
};
</script>

<style scoped>
.organization-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.tree-container {
  max-height: 600px;
  overflow-y: auto;
  padding: 10px 0;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.node-action-icon {
  opacity: 0;
  transition: opacity 0.2s;
}

.tree-node:hover .node-action-icon {
  opacity: 1;
}

.org-info {
  margin-bottom: 20px;
}

.member-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.relation-header {
  margin-bottom: 16px;
  text-align: right;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.no-org-selected {
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  border-radius: 4px;
}
</style>
