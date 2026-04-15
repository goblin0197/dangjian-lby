<template>
  <div class="organization-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>组织管理</h1>
      <div class="header-actions">
        <a-button type="primary" @click="showAddOrganizationModal">
          <IconPlus />
          新增组织
        </a-button>
      </div>
    </div>

    <!-- 主要内容区 -->
    <a-row :gutter="20">
      <!-- 左侧组织架构树 -->
      <a-col :span="6">
        <a-card class="organization-tree-card" title="组织架构">
          <div class="tree-container">
            <a-tree
              v-model:selected-keys="selectedOrgKeys"
              :checkable="false"
              :data="organizationTree"
              :default-expand-all="true"
              @select="handleOrgSelect"
            >
            </a-tree>
          </div>
        </a-card>
      </a-col>

      <!-- 右侧组织详情和成员管理 -->
      <a-col :span="18">
        <a-card v-if="selectedOrg" :title="selectedOrg.title">
          <div class="org-info">
            <a-descriptions :column="2" title="组织信息">
              <a-descriptions-item label="组织ID">
                {{ selectedOrg.id }}
              </a-descriptions-item>
              <a-descriptions-item label="组织类型">
                {{ selectedOrg.type }}
              </a-descriptions-item>
              <a-descriptions-item label="成立时间">
                {{ formatDate(selectedOrg.createTime) }}
              </a-descriptions-item>
              <a-descriptions-item label="负责人">
                {{ leaderInfo?.userName || selectedOrg.leaderId || "未设置" }}
              </a-descriptions-item>
              <a-descriptions-item label="成员数量">
                {{ memberList.length }}
              </a-descriptions-item>
              <a-descriptions-item label="联系电话">
                {{ leaderInfo?.phone || selectedOrg.contactPhone || "未设置" }}
              </a-descriptions-item>
              <a-descriptions-item :span="2" label="地址">
                {{ selectedOrg.address || "未设置" }}
              </a-descriptions-item>
              <a-descriptions-item :span="2" label="组织描述">
                {{ selectedOrg.description || "未设置" }}
              </a-descriptions-item>
            </a-descriptions>
            <div class="org-actions" style="margin-top: 16px">
              <a-button type="primary" @click="handleEditOrg(selectedOrg.id)">
                <IconEdit />
                编辑组织
              </a-button>
              <a-button
                style="margin-left: 8px"
                type="danger"
                @click="handleDeleteOrg(selectedOrg.id)"
              >
                <IconDelete />
                删除组织
              </a-button>
            </div>
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
                  v-model:value="searchMemberText"
                  placeholder="搜索成员"
                  style="width: 300px"
                  @search="handleSearchMember"
                />
                <a-button type="primary" @click="showAddMemberModal">
                  <IconPlus />
                  添加成员
                </a-button>
              </div>

              <a-table
                :columns="memberColumns"
                :data="memberList"
                :pagination="pagination"
                row-key="id"
                @page-change="handlePageChange"
                @page-size-change="handlePageSizeChange"
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
                      danger
                      size="small"
                      type="text"
                      @click="handleRemoveMember(record.id)"
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
                  <IconPlus />
                  添加对接关系
                </a-button>
              </div>

              <a-table
                :columns="relationColumns"
                :data="mentorRelationList"
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
                    danger
                    size="small"
                    type="text"
                    @click="handleRemoveRelation(record.id)"
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
      @cancel="handleOrgModalCancel"
      @ok="handleOrgModalOk"
    >
      <a-form
        ref="orgFormRef"
        :model="orgForm"
        :rules="orgFormRules"
        layout="vertical"
      >
        <a-form-item field="orgName" label="组织名称" required>
          <a-input v-model="orgForm.orgName" placeholder="请输入组织名称" />
        </a-form-item>
        <a-form-item field="orgCode" label="组织编码" required>
          <a-input v-model="orgForm.orgCode" placeholder="请输入组织编码" />
        </a-form-item>
        <a-form-item field="parentId" label="父组织ID" required>
          <a-select v-model="orgForm.parentId" placeholder="请选择父组织">
            <a-option
              v-for="org in flattenedOrganizationList"
              :key="org.id"
              :value="org.id"
            >
              {{ org.title }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="orgType" label="组织类型" required>
          <a-select
            v-model="orgForm.orgType"
            placeholder="请选择组织类型"
            @change="handleOrgTypeChange"
          >
            <a-option value="党委">党委</a-option>
            <a-option value="党总支">党总支</a-option>
            <a-option value="党支部">党支部</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="orgLevel" label="组织级别" required>
          <a-select v-model="orgForm.orgLevel" placeholder="请选择组织级别">
            <a-option :value="1">党委</a-option>
            <a-option :value="2">党总支</a-option>
            <a-option :value="3">党支部</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="leaderId" label="负责人">
          <a-select
            v-model="orgForm.leaderId"
            placeholder="请选择负责人"
            show-search
          >
            <a-option v-for="user in userList" :key="user.id" :value="user.id">
              {{ user.name }} ({{ user.username }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="address" label="地址">
          <a-input v-model="orgForm.address" placeholder="请输入地址" />
        </a-form-item>
        <a-form-item field="description" label="组织描述">
          <a-textarea
            v-model="orgForm.description"
            :rows="4"
            placeholder="请输入组织描述"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 添加成员弹窗 -->
    <a-modal
      v-model:visible="memberModalVisible"
      title="添加组织成员"
      @cancel="handleMemberModalCancel"
      @ok="handleMemberModalOk"
    >
      <a-form
        ref="memberFormRef"
        :model="memberForm"
        :rules="memberFormRules"
        layout="vertical"
      >
        <a-form-item field="orgName" label="加入组织">
          <a-input v-model="memberForm.orgName" />
        </a-form-item>
        <a-form-item field="userId" label="选择成员" required>
          <a-select
            v-model="memberForm.userId"
            :filter-option="true"
            placeholder="请选择成员"
            show-search
          >
            <a-option v-for="user in userList" :key="user.id" :value="user.id">
              {{ user.name }} ({{ user.username }})
            </a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 组织关系转移弹窗 -->
    <a-modal
      v-model:visible="transferModalVisible"
      title="组织关系转移"
      @cancel="handleTransferModalCancel"
      @ok="handleTransferModalOk"
    >
      <a-form
        ref="transferFormRef"
        :model="transferForm"
        :rules="transferFormRules"
        layout="vertical"
      >
        <a-form-item disabled field="memberName" label="成员">
          <a-input v-model:value="transferForm.memberName" />
        </a-form-item>
        <a-form-item disabled field="sourceOrg" label="原组织">
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
            :rows="3"
            placeholder="请输入转移原因"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 添加培养人对接关系弹窗 -->
    <a-modal
      v-model:visible="relationModalVisible"
      title="添加培养人对接关系"
      @cancel="handleRelationModalCancel"
      @ok="handleRelationModalOk"
    >
      <a-form
        ref="relationFormRef"
        :model="relationForm"
        :rules="relationFormRules"
        layout="vertical"
      >
        <a-form-item field="mentorId" label="培养人" required>
          <a-select
            v-model="relationForm.mentorId"
            :filter-option="true"
            placeholder="请选择培养人"
            show-search
          >
            <a-option
              v-for="user in mentorList"
              :key="user.id"
              :value="user.id"
            >
              {{ user.name }} ({{ user.username }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="menteeId" label="培养对象" required>
          <a-select
            v-model="relationForm.menteeId"
            :filter-option="true"
            placeholder="请选择培养对象"
            show-search
          >
            <a-option
              v-for="user in menteeList"
              :key="user.id"
              :value="user.id"
            >
              {{ user.name }} ({{ user.username }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="startDate" label="开始日期" required>
          <a-date-picker
            v-model="relationForm.startDate"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, onMounted, ref } from "vue";
import { IconDelete, IconEdit, IconPlus } from "@arco-design/web-vue/es/icon";
import type { TableColumn } from "@arco-design/web-vue/es/table";
import TableProps from "@arco-design/web-vue/es/table";
import type { FormInstance } from "@arco-design/web-vue/es/form";
import { Message, Tree as aTree } from "@arco-design/web-vue";
import * as dangzuzhiguanli from "@/api/dangzuzhiguanli";
import * as zuzhichengyuanguanli from "@/api/zuzhichengyuanguanli";
import * as zuzhiguanxizhuanyiguanli from "@/api/zuzhiguanxizhuanyiguanli";
import * as yonghuguanli from "@/api/yonghuguanli";
import * as peiyangrenguanlianguanli from "@/api/peiyangrenguanlianguanli";

// 类型定义
interface Organization {
  id: string;
  title: string;
  type: string;
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
  userAccount: string;
  userName: string;
  userAvatar: string;
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
const selectedOrg = ref<any | null>(null);
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
const orgForm = ref({
  id: "",
  orgName: "",
  orgCode: "",
  orgType: "党支部",
  orgLevel: 3,
  parentId: "",
  leaderId: "",
  address: "",
  description: "",
});

const memberForm = ref({
  userId: "",
  orgId: "",
  orgName: "",
});

const transferForm = ref({
  memberId: "",
  memberName: "",
  sourceOrg: "",
  targetOrgId: "",
  transferDate: new Date(),
  reason: "",
});

const relationForm = ref({
  mentorId: "",
  menteeId: "",
  startDate: new Date().toISOString().split("T")[0],
  orgId: "",
});

// 表单验证规则
const orgFormRules = ref({
  orgName: [{ required: true, message: "请输入组织名称", trigger: "blur" }],
  orgCode: [{ required: true, message: "请输入组织编码", trigger: "blur" }],
  parentId: [{ required: true, message: "请选择父组织", trigger: "change" }],
  orgType: [{ required: true, message: "请选择组织类型", trigger: "change" }],
  orgLevel: [{ required: true, message: "请选择组织级别", trigger: "change" }],
});

const memberFormRules = ref({
  userId: [{ required: true, message: "请选择成员", trigger: "change" }],
});

const transferFormRules = ref({
  targetOrgId: [
    { required: true, message: "请选择目标组织", trigger: "change" },
  ],
  transferDate: [
    { required: true, message: "请选择转移日期", trigger: "change" },
  ],
  reason: [{ required: true, message: "请输入转移原因", trigger: "blur" }],
});

const relationFormRules = ref({
  mentorId: [{ required: true, message: "请选择培养人", trigger: "change" }],
  menteeId: [{ required: true, message: "请选择培养对象", trigger: "change" }],
  startDate: [{ required: true, message: "请选择开始日期", trigger: "change" }],
});

// 映射关系（完全适配后端返回的中文类型）
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

// 组织树数据
const organizationTree = ref<any[]>([]);

const memberList = ref<Member[]>([]);
const mentorRelationList = ref<MentorRelation[]>([]);

// 负责人信息
const leaderInfo = ref<any>(null);

// 模拟用户列表
const userList = ref([
  { id: "u1", name: "王教授", username: "wangjiaoshou" },
  { id: "u2", name: "李老师", username: "lilaoshi" },
  { id: "u3", name: "张同学", username: "zhangtongxue" },
  { id: "u4", name: "刘同学", username: "liutongxue" },
  { id: "u5", name: "孙同学", username: "suntongxue" },
]);

const mentorList = ref<any[]>([]);

// 加载培养人列表（该组织的教师党员）
const loadMentorList = async (orgId: string) => {
  try {
    const res = await peiyangrenguanlianguanli.listAvailableTrainersUsingGet({
      orgId: Number(orgId),
      userType: "教师",
    });
    if (res.data.code === 0) {
      mentorList.value = res.data.data.map((user: any) => ({
        id: user.id,
        name: user.userName,
        username: user.userAccount,
      }));
    } else {
      Message.error(res.data.message || "获取培养人列表失败");
    }
  } catch (error) {
    console.error("获取培养人列表失败:", error);
    Message.error("网络请求异常");
  }
};

const menteeList = ref<any[]>([]);

// 加载培养对象列表（该组织的成员）
const loadMenteeList = async (orgId: string) => {
  try {
    const res = await zuzhichengyuanguanli.listOrgMemberByPageUsingPost({
      orgId,
      pageNum: 1,
      pageSize: 100, // 加载足够多的成员
    });
    if (res.data.code === 0) {
      // 过滤出适合作为培养对象的成员
      // 排除党员用户，只保留非党员
      menteeList.value =
        res.data.data?.records
          .filter((member: any) => {
            // 排除党员用户
            const politicalStatus =
              member.politicalStatus || member.political_status;
            return (
              politicalStatus !== "党员" &&
              politicalStatus !== "正式党员" &&
              politicalStatus !== "formalPartyMember"
            );
          })
          .map((member: any) => ({
            id: member.id,
            name: member.userName,
            username: member.userAccount,
          })) || [];
      console.log("menteeList:", menteeList.value);
    } else {
      Message.error(res.data.message || "获取培养对象列表失败");
    }
  } catch (error) {
    console.error("获取培养对象列表失败:", error);
    Message.error("网络请求异常");
  }
};

// 表格列配置
const memberColumns = ref<TableColumn<Member>[]>([
  { title: "姓名", dataIndex: "userName", width: 120 },
  { title: "用户名", dataIndex: "userAccount", width: 150 },
  { title: "角色", dataIndex: "userRole", width: 120, slotName: "role" },
  { title: "加入日期", dataIndex: "joinDate", width: 150 },
  { title: "职务", dataIndex: "userType", width: 150 },
  { title: "操作", width: 200, slotName: "actions" },
]);

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

// 生命周期
onMounted(async () => {
  await loadOrganizationTree();
  await loadUserList();
  if (organizationTree.value.length === 0) {
    Message.info("暂无组织数据，请先新增组织");
  }
});

// 加载用户列表
const loadUserList = async () => {
  try {
    const res = await yonghuguanli.listUserVoUsingPost({});
    if (res.data.code === 0) {
      userList.value = res.data.data.map((user: any) => ({
        id: user.id,
        name: user.userName,
        username: user.userAccount,
      }));
    } else {
      Message.error(res.data.message || "获取用户列表失败");
    }
  } catch (error) {
    console.error("获取用户列表失败:", error);
    Message.error("网络请求异常");
  }
};

// 加载组织树（完全适配你的接口）
const loadOrganizationTree = async () => {
  try {
    // 直接调用分级接口，根组织ID固定为1
    const gradedRes = await dangzuzhiguanli.getOrganizationGradedByIdUsingGet({
      orgId: "1",
    });
    if (gradedRes.data.code === 0) {
      const gradedOrg = gradedRes.data.data;
      if (gradedOrg) {
        // 转换为树结构
        const treeData = transformGradedOrg(gradedOrg);
        organizationTree.value = [treeData];

        // 生成扁平化组织列表
        flattenedOrganizationList.value = flattenOrganizationTree(
          organizationTree.value,
        );
        console.log("扁平化组织列表:", flattenedOrganizationList.value);

        // 确保转换后的数据有效
        if (organizationTree.value.length > 0 && organizationTree.value[0]) {
          // 绑定key字段，a-tree要求selectedKeys对应节点的key
          selectedOrgKeys.value = [organizationTree.value[0].key];
          selectedOrg.value = organizationTree.value[0];
          await loadMemberList(organizationTree.value[0].id);
        }
      }
    } else {
      Message.error(gradedRes.data.message || "获取组织树结构失败");
    }
  } catch (error) {
    console.error("获取组织树失败:", error);
    Message.error("网络请求异常");
  }
};

// 核心修复：转换分级组织数据为树结构（完全适配你的接口字段）
const transformGradedOrg = (org: any): any => {
  // 确保每个节点都有唯一的key
  const node = {
    key: org.id, // a-tree要求的唯一标识，必须和selectedKeys绑定
    id: org.id,
    title: org.orgName || "未知组织", // 直接用接口返回的组织名称，添加默认值
    label: org.orgName || "未知组织",
    orgName: org.orgName || "",
    orgCode: org.orgCode || "",
    type: org.orgType || "", // 直接用接口返回的中文类型（党委/党总支）
    orgType: org.orgType || "",
    orgLevel: org.orgLevel || 3,
    parentId: org.parentId,
    leaderId: org.leaderId || "",
    leader: org.leader || "",
    address: org.address || "",
    contactPhone: org.contactPhone || "",
    createTime: org.createTime || "",
    description: org.description || "",
    // 确保children是数组，如果没有subOrgList或为空，则设为空数组
    children: [],
  };

  // 递归转换子组织
  if (
    org.subOrgList &&
    Array.isArray(org.subOrgList) &&
    org.subOrgList.length > 0
  ) {
    node.children = org.subOrgList.map((child: any) =>
      transformGradedOrg(child),
    );
  }

  return node;
};

// 扁平化组织树，用于下拉框
const flattenedOrganizationList = ref<any[]>([]);

const flattenOrganizationTree = (tree: any[], prefix = "") => {
  const result: any[] = [];
  tree.forEach((node) => {
    result.push({
      id: node.id,
      title: prefix + node.title,
      orgName: node.orgName,
    });
    if (node.children && node.children.length > 0) {
      result.push(...flattenOrganizationTree(node.children, prefix + "  "));
    }
  });
  return result;
};

// 组织选择
const handleOrgSelect = async (keys: string[]) => {
  if (keys.length === 0) {
    selectedOrg.value = null;
    return;
  }
  const findOrg = (tree: any[], id: string): any | null => {
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
  if (selectedOrg.value) {
    // 获取负责人信息
    if (selectedOrg.value.leaderId) {
      leaderInfo.value = await getUserInfo(selectedOrg.value.leaderId);
    } else {
      leaderInfo.value = null;
    }
    await loadMemberList(selectedOrg.value.id);
    await loadMentorRelationList(selectedOrg.value.id);
  } else {
    leaderInfo.value = null;
  }
};

// 加载成员列表（支持分页）
const loadMemberList = async (orgId: string, pageNum = 1, pageSize = 10) => {
  try {
    const res = await zuzhichengyuanguanli.listOrgMemberByPageUsingPost({
      orgId,
      pageNum,
      pageSize,
    });
    if (res.data.code === 0) {
      memberList.value = res.data.data?.records || [];
      pagination.value.total = res.data.data?.total || 0;
      pagination.value.current = pageNum;
      pagination.value.pageSize = pageSize;
    } else {
      Message.error(res.data.message || "获取成员列表失败");
    }
  } catch (error) {
    Message.error("网络请求异常");
  }
};

// 加载培养人对接关系列表
const loadMentorRelationList = async (orgId: string) => {
  try {
    const res =
      await peiyangrenguanlianguanli.getTrainerRelationsByOrgIdUsingGet({
        orgId: Number(orgId),
      });
    if (res.data.code === 0) {
      const relations = res.data.data || [];
      // 转换数据格式以适配前端显示
      mentorRelationList.value = await Promise.all(
        relations.map(async (relation: any) => {
          // 获取培养人信息
          const mentorInfo = await getUserInfo(relation.trainerId);
          // 获取培养对象信息
          const menteeInfo = await getUserInfo(relation.userId);

          return {
            id: relation.id,
            mentor: {
              id: relation.trainerId,
              name: mentorInfo?.userName || "未知",
              avatar: mentorInfo?.userAvatar || "",
            },
            mentee: {
              id: relation.userId,
              name: menteeInfo?.userName || "未知",
              avatar: menteeInfo?.userAvatar || "",
            },
            menteeType: relation.menteeType || "active",
            startDate: relation.createTime || new Date().toISOString(),
          };
        }),
      );
    } else {
      Message.error(res.message || "获取培养人对接关系失败");
    }
  } catch (error) {
    console.error("获取培养人对接关系失败:", error);
    Message.error("网络请求异常");
  }
};

// 分页事件
const handlePageChange = async (current: number) => {
  if (!selectedOrg.value) return;
  pagination.value.current = current;
  await loadMemberList(
    selectedOrg.value.id,
    current,
    pagination.value.pageSize,
  );
};

const handlePageSizeChange = async (size: number) => {
  if (!selectedOrg.value) return;
  pagination.value.pageSize = size;
  pagination.value.current = 1;
  await loadMemberList(selectedOrg.value.id, 1, size);
};

// 组织操作
// 组织类型变更处理
const handleOrgTypeChange = (value: string) => {
  switch (value) {
    case "党委":
      orgForm.value.orgLevel = 1;
      break;
    case "党总支":
      orgForm.value.orgLevel = 2;
      break;
    case "党支部":
      orgForm.value.orgLevel = 3;
      break;
    default:
      orgForm.value.orgLevel = 3;
  }
};

const showAddOrganizationModal = () => {
  orgModalTitle.value = "新增组织";
  Object.assign(orgForm.value, {
    id: "",
    orgName: "",
    orgCode: "",
    orgType: "党支部",
    orgLevel: 3,
    parentId: selectedOrg.value?.id || "",
    leaderId: "",
    address: "",
    description: "",
  });
  orgModalVisible.value = true;
  nextTick(() => {
    orgFormRef.value?.resetFields();
  });
};

const handleAddSubOrg = (parentId: string | undefined) => {
  if (!parentId) return;
  orgModalTitle.value = "新增子组织";
  Object.assign(orgForm.value, {
    id: "",
    orgName: "",
    orgCode: "",
    orgType: "党支部",
    orgLevel: 3,
    parentId,
    leaderId: "",
    address: "",
    description: "",
  });
  orgModalVisible.value = true;
};

const handleEditOrg = (id: string | undefined) => {
  if (!id) return;
  orgModalTitle.value = "编辑组织";
  const findOrgById = (tree: any[], id: string): any | null => {
    for (const item of tree) {
      if (item.id === id) return item;
      if (item.children) {
        const child = findOrgById(item.children, id);
        if (child) return child;
      }
    }
    return null;
  };
  const org = findOrgById(organizationTree.value, id);
  if (org) {
    Object.assign(orgForm.value, {
      id: org.id,
      orgName: org.title || org.orgName || "",
      orgCode: org.orgCode || "",
      orgType: org.type || org.orgType || "党支部",
      orgLevel: org.orgLevel || 3,
      parentId: org.parentId || "",
      leaderId: org.leaderId || "",
      address: org.address || "",
      contactPhone: org.contactPhone || "",
      description: org.description || "",
    });
    orgModalVisible.value = true;
  }
};

const handleOrgModalOk = async () => {
  // if (await orgFormRef.value?.validate()) {
  try {
    const params = {
      orgName: orgForm.value.orgName,
      orgCode: orgForm.value.orgCode,
      orgType: orgForm.value.orgType,
      orgLevel: Number(orgForm.value.orgLevel),
      parentId: Number(orgForm.value.parentId),
      leaderId: orgForm.value.leaderId ? orgForm.value.leaderId : undefined,
      address: orgForm.value.address,
      description: orgForm.value.description,
    };

    if (orgForm.value.id) {
      const res = await dangzuzhiguanli.updateOrganizationUsingPost({
        id: orgForm.value.id,
        ...params,
      });
      if (res.data.code === 0) {
        Message.success("编辑组织成功");
        orgModalVisible.value = false;
        await loadOrganizationTree();
      } else {
        Message.error(res.data.message || "编辑组织失败");
      }
    } else {
      const res = await dangzuzhiguanli.addOrganizationUsingPost(params);
      if (res.data.code === 0) {
        Message.success("新增组织成功");
        orgModalVisible.value = false;
        await loadOrganizationTree();
      } else {
        Message.error(res.data.message || "新增组织失败");
      }
    }
  } catch (error) {
    Message.error("网络请求异常");
  }
  // }else{
  //   console.log("333");
  // }
};

const handleOrgModalCancel = () => {
  orgFormRef.value?.resetFields();
  orgModalVisible.value = false;
};

// 删除组织
const handleDeleteOrg = async (id: any) => {
  if (!id) return;
  // const param = {
  //     id: id
  // } as deleteOrganizationUsingPOSTParams;
  try {
    const res = await dangzuzhiguanli.deleteOrganizationUsingPost({
      id: id,
    });
    if (res.data.code === 0) {
      Message.success("删除组织成功");
      await loadOrganizationTree();
      selectedOrg.value = null;
      selectedOrgKeys.value = [];
    } else {
      Message.error(res.data.message || "删除组织失败");
    }
  } catch (error) {
    Message.error("网络请求异常");
  }
};

// 成员搜索
const handleSearchMember = (value: string) => {
  console.log("搜索成员:", value);
};

// 成员管理
const showAddMemberModal = () => {
  Object.assign(memberForm.value, {
    userId: "",
    orgId: selectedOrg.value?.id || "",
    orgName: selectedOrg.value?.title || selectedOrg.value?.orgName || "",
  });
  memberModalVisible.value = true;
};

const handleMemberModalOk = async () => {
  if (!selectedOrg.value) {
    Message.error("请先选择组织");
    return;
  }
  // 从模拟用户列表中获取用户对象
  const selectedUser = userList.value.find(
    (user) => user.id === memberForm.value.userId,
  );
  if (!selectedUser) {
    console.log("memberForm.value.userId:", memberForm.value.userId);
    Message.error("请选择有效的成员");
    return;
  }
  const res = await zuzhichengyuanguanli.addOrgMemberUsingPost({
    orgId: Number(memberForm.value.orgId),
    userId: selectedUser.id,
  });
  if (res.data.code === 0) {
    Message.success("添加成员成功");
    memberModalVisible.value = false;
    await loadMemberList(memberForm.value.orgId);
  } else {
    Message.error(res.data.message || "添加成员失败");
  }
};

const handleMemberModalCancel = () => {
  memberFormRef.value?.resetFields();
  memberModalVisible.value = false;
};

const handleEditMember = (record: Member) => {
  console.log("编辑成员", record);
};

const handleRemoveMember = async (memberId: string) => {
  try {
    if (!selectedOrg.value) {
      Message.error("请先选择组织");
      return;
    }
    const res = await zuzhichengyuanguanli.removeOrgMemberUsingPost({
      orgId: selectedOrg.value.id,
      userId: memberId,
    });
    if (res.data.code === 0) {
      Message.success("移除成员成功");
      await loadMemberList(selectedOrg.value.id);
    } else {
      Message.error(res.data.message || "移除成员失败");
    }
  } catch (error) {
    Message.error("网络请求异常");
  }
};

// 组织关系转移
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
    try {
      if (!selectedOrg.value) {
        Message.error("请先选择组织");
        return;
      }
      const res =
        await zuzhiguanxizhuanyiguanli.addOrgRelationTransferUsingPost({
          userId: transferForm.value.memberId,
          sourceOrgId: selectedOrg.value.id, // 修复笔误
          targetOrgId: transferForm.value.targetOrgId,
          transferDate: transferForm.value.transferDate,
          reason: transferForm.value.reason,
        });
      if (res.data.code === 0) {
        Message.success("组织关系转移申请成功");
        transferModalVisible.value = false;
        await loadMemberList(selectedOrg.value.id);
      } else {
        Message.error(res.data.message || "组织关系转移申请失败");
      }
    } catch (error) {
      Message.error("网络请求异常");
    }
  }
};

const handleTransferModalCancel = () => {
  transferFormRef.value?.resetFields();
  transferModalVisible.value = false;
};

// 培养人关系
const showAddRelationModal = async () => {
  if (!selectedOrg.value) {
    Message.error("请先选择组织");
    return;
  }

  Object.assign(relationForm.value, {
    mentorId: "",
    menteeId: "",
    startDate: new Date(),
    orgId: selectedOrg.value?.id || "",
  });

  // 加载该组织的教师党员作为培养人列表
  await loadMentorList(selectedOrg.value.id);
  // 加载该组织的成员作为培养对象列表
  await loadMenteeList(selectedOrg.value.id);

  relationModalVisible.value = true;
};

const handleRelationModalOk = async () => {
  try {
    if (!selectedOrg.value) {
      Message.error("请先选择组织");
      return;
    }
    console.log("relationForm:", relationForm.value);
    const res = await peiyangrenguanlianguanli.addTrainerRelationUsingPost({
      trainerId: relationForm.value.mentorId,
      userId: relationForm.value.menteeId,
      startDate: relationForm.value.startDate,
    });

    if (res.code === 0) {
      Message.success("添加培养人对接关系成功");
      relationModalVisible.value = false;
      await loadMentorRelationList(selectedOrg.value.id);
    } else {
      Message.error(res.message || "添加培养人对接关系失败");
    }
  } catch (error) {
    console.error("添加培养人对接关系失败:", error);
    Message.error("网络请求异常");
  }
};

const handleRelationModalCancel = () => {
  relationFormRef.value?.resetFields();
  relationModalVisible.value = false;
};

const handleRemoveRelation = async (relationId: string) => {
  try {
    const res = await peiyangrenguanlianguanli.deleteTrainerRelationUsingPost({
      id: relationId,
    });
    if (res.code === 0) {
      mentorRelationList.value = mentorRelationList.value.filter(
        (r) => r.id !== relationId,
      );
      Message.success("解除培养人对接关系成功");
    } else {
      Message.error(res.message || "解除培养人对接关系失败");
    }
  } catch (error) {
    console.error("解除培养人对接关系失败:", error);
    Message.error("网络请求异常");
  }
};

// 日期格式化
const formatDate = (dateStr: string) => {
  if (!dateStr) return "-";
  // 适配接口返回的"2025-12-27 16:20:27"格式
  const date = new Date(dateStr.replace(" ", "T"));
  return date.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
};

// 获取用户信息
const getUserInfo = async (userId: string) => {
  if (!userId) return null;
  try {
    const res = await yonghuguanli.getUserByIdUsingGet({ id: Number(userId) });
    if (res.data.code === 0) {
      return res.data.data;
    }
  } catch (error) {
    console.error("获取用户信息失败:", error);
  }
  return null;
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
