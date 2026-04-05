import { RouteRecordRaw } from "vue-router";
import NoAuthView from "@/views/NoAuthView.vue";
import HomeView from "@/views/HomeView.vue";
import UserLoginView from "@/views/user/UserLoginView.vue";
import ActivityView from "@/views/activity/ActivityView.vue";
import OrganizationView from "@/views/organization/OrganizationView.vue";
import TrainerRelationView from "@/views/trainerRelation/TrainerRelationView.vue";
import QuantifyView from "@/views/quantify/QuantifyView.vue";
import QuantifyManagerView from "@/views/quantify/QuantifyManagerView.vue";
import MaterialTemplateManagementView from "@/views/material/MaterialTemplateManagementView.vue";
import MaterialSubmissionReviewView from "@/views/material/MaterialSubmissionReviewView.vue";
import MaterialArchivingQueryView from "@/views/material/MaterialArchivingQueryView.vue";
import AnnouncementManagementView from "@/views/notice/AnnouncementManagementView.vue";
import AnnouncementListView from "@/views/notice/AnnouncementListView.vue";
import AnnouncementDetailsView from "@/views/notice/AnnouncementDetailsView.vue";
import BasicLayout from "@/layouts/BasicLayout.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/user/login",
  },
  {
    path: "/organization",
    name: "组织管理",
    component: OrganizationView,
  },
  {
    path: "/activity",
    name: "活动管理",
    component: ActivityView,
  },
  {
    path: "/trainerRelation",
    name: "发展阶段",
    component: TrainerRelationView,
  },
  // {
  //   path: "/",
  //   name: "签到管理",
  //   component: HomeView,
  //   meta: {
  //     hideInMenu: true,
  //   },
  // },
  {
    path: "/quantifyManager",
    name: "量化统计",
    component: QuantifyManagerView,
  },
  {
    path: "/quantify",
    name: "量化统计(隐藏)",
    component: QuantifyView,
    meta: {
      hideInMenu: true,
    },
  },

  {
    path: "/materialTemplateManagement",
    name: "材料管理",
    component: MaterialTemplateManagementView,
  },
  {
    path: "/materialSubmissionReview",
    name: "材料提交/审核",
    component: MaterialSubmissionReviewView,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/materialArchivingQuery",
    name: "材料归档/查询",
    component: MaterialArchivingQueryView,
    meta: {
      hideInMenu: true,
    },
  },

  {
    path: "/announcementManagement",
    name: "公告管理",
    component: AnnouncementManagementView,
  },
  {
    path: "/announcementList",
    name: "公告列表",
    component: AnnouncementListView,
  },
  {
    path: "/announcementDetails",
    name: "公告详情",
    component: AnnouncementDetailsView,
  },
  // {
  //   path: "/",
  //   name: "系统设置",
  //   component: HomeView,
  //   meta: {
  //     hideInMenu: true,
  //   },
  // },
  {
    path: "/user/login",
    name: "登录",
    component: UserLoginView,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/noAuth",
    name: "无权限",
    component: NoAuthView,
    meta: {
      hideInMenu: true,
    },
  },
  // {
  //   path: "/admin",
  //   name: "管理员可见",
  //   component: AdminView,
  //   meta: {
  //     access: ACCESS_ENUM.ADMIN,
  //   },
  // },
  // {
  //   path: "/about",
  //   name: "关于我们",
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  // },
];
