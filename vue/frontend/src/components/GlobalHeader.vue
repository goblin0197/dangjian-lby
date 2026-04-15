<template>
  <div id="globalHeader">
    <a-row
        id="globalHeader"
        :wrap="false"
        align="center"
        style="margin-bottom: 6px"
    >
      <a-col flex="auto">
        <a-menu
            :selected-keys="selectedKey"
            mode="horizontal"
            @menu-item-click="doMenuClick"
        >
          <a-menu-item
              key="0"
              :style="{ padding: 0, marginRight: '38px' }"
              disabled
          >
            <div class="title-bar">
              <img
                  id="systemLogo"
                  alt="logo"
                  class="logo"
                  src="../assets/dang.png"
              />
              <img
                  id="collegeLogo"
                  alt="logo"
                  class="logo"
                  src="../assets/dangjiansystem.png"
              />
              <img
                  id="imageLogo"
                  alt="logo"
                  class="logo"
                  src="../assets/dangjiansysystemmanager.png"
              />
              <div class="title">&nbsp;智慧党建综合管理系统</div>
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in visibleRoutes" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div>
          <!-- 这里有个问题，如果在 state 下定义 user属性，好像也是报 Unresolved variable user 错误，-->
          <!-- 但是控制台输出 state 有 user 属性-->
          <!-- 这里暂时用可选链操作符以及逻辑控制合并运算符，使得不抛出TypeError-->
          <a-space>
            <a-dropdown position="bl">
              <a-button
                  style="min-width: 100px; margin-right: 50px; border-radius: 8px"
              >{{ store.state.user?.loginUser?.userName ?? "未登录" }}
              </a-button>
              <template #content>
                <a-doption
                    v-if="store.state.user.loginUser.userName == '未登录'"
                    @click="toUserCenter"
                >
                  <template #icon>
                    <icon-user/>
                  </template>
                  登录
                </a-doption>
                <!--                <a-doption v-else @click="toUserCenter">-->
                <!--                  <template #icon>-->
                <!--                    <IconHome></IconHome>-->
                <!--                  </template>-->
                <!--                  量化统计-->
                <!--                </a-doption>-->
                <!--                <a-doption-->
                <!--                  v-if="-->
                <!--                    checkAccess(store.state.user.loginUser, ACCESS_ENUM.ADMIN)-->
                <!--                  "-->
                <!--                  @click="toManageAccountPage"-->
                <!--                >-->
                <!--                  <template #icon>-->
                <!--                    <icon-user-group />-->
                <!--                  </template>-->
                <!--                  材料管理-->
                <!--                </a-doption>-->
                <!--                <a-doption-->
                <!--                  v-if="-->
                <!--                    checkAccess(store.state.user.loginUser, ACCESS_ENUM.ADMIN)-->
                <!--                  "-->
                <!--                  @click="toManageCommunityPage"-->
                <!--                >-->
                <!--                  <template #icon>-->
                <!--                    <icon-user-group />-->
                <!--                  </template>-->
                <!--                  公告管理-->
                <!--                </a-doption>-->
                <a-doption
                    v-if="store.state.user.loginUser.userName != '未登录'"
                    @click="userLogOut"
                >
                  <template #icon>
                    <IconFullscreenExit></IconFullscreenExit>
                  </template>
                  注销登录
                </a-doption>
              </template>
            </a-dropdown>
          </a-space>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import {routes} from "@/router/routes";
import {useRouter} from "vue-router";
import {computed, reactive, ref} from "vue";
import {useStore} from "vuex";
import checkAccess from "@/access/checkAccess";
import ACCESS_ENUM from "@/access/accessEnum";

import {Service, UserLoginRequest} from "../../generated";
import {IconFullscreenExit, IconUser} from "@arco-design/web-vue/es/icon";
import message from "@arco-design/web-vue/es/message";

/**
 * 获取存储所有的状态信息
 */
const store = useStore();

const route = useRouter();

const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);

// 默认主页
const selectedKey = ref(["/"]);

/*获取路由实例的钩子函数*/
const router = useRouter();

/**
 * 记住下边这一句代码
 */
// const loginUser = store.state.user.loginUser;
// console.log(loginUser);

// 过滤隐藏页面的函数
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    // 是否是隐藏页面
    if (item.meta?.hideInMenu) return false;
    // 根据权限过滤菜单
    if (
        // 这里的第一个参数千万不要用获取的常量传入，否则监听到的数据不会变
        !checkAccess(store.state.user?.loginUser, item?.meta?.access as string)
    ) {
      return false;
    }
    return true;
  });
});

/**
 * 当用户点击菜单时，doMenuClick 函数会被调用，它接收一个字符串类型的参数 key
 * 然后使用 router.push 方法跳转到对应的路由
 * @param key 表示要跳转的路径
 * @author 纸飞机
 */
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

// 跳转路由后，更新选中的菜单项
router.beforeEach((to, from, next) => {
  selectedKey.value = [to.path];
  // router.push(to.path);
  /**
   * next 函数接收一个可选的参数，用于指示 Vue Router 如何进行路由导航：
   * 如果调用 next()，则表示继续进行路由导航，即允许当前路由发生变化。
   * 如果调用 next(false)，则表示中断当前的路由导航，即不允许当前路由发生变化。
   * 如果调用 next('/')，则表示取消当前的路由导航，并将浏览器重定向到指定的路由地址。
   *
   * @function next 表示继续进行路由导航，即允许当前路由发生变化。
   */
  next();
});

// console.log(store.state.user.loginUser.userName);
setTimeout(() => {
  /**
   * 调用 dispatch() 方法，触发一个action
   */
  store.dispatch("user/getLoginUser", {
    userName: "纸飞机管理员",
    userRole: ACCESS_ENUM.ADMIN,
  });
  // console.log(store.state.user?.loginUser);
}, 3000);

/**
 * 跳转到个人主页
 */
const toUserCenter = () => {
  // console.log(question);
  if (store.state.user.loginUser.userName === "未登录") {
    message.error("请先登录");
    router.push({
      path: "/user/login",
    });
    return;
  }
  router.push({
    path: "/user/center",
  });
};

const userLogOut = () => {
  if (store.state.user.loginUser.userName === "未登录") {
    message.error("未登录");
    return;
  }
  handleSubmit();
};

/**
 * 注销登录
 */
const handleSubmit = async () => {
  const res = await Service.userLogoutUsingPost();
  if (res.code === 0) {
    await store.dispatch("user/getLoginUser");
    await router.push({
      path: "/user/login",
    });
  } else {
    message.error("操作异常，" + res.message);
  }
};

const toManageAccountPage = () => {
  if (checkAccess(store.state.user.loginUser, ACCESS_ENUM.ADMIN)) {
    router.push({
      path: "/manage/account",
      replace: true,
    });
  }
};

const toManageCommunityPage = () => {
  if (checkAccess(store.state.user.loginUser, ACCESS_ENUM.ADMIN)) {
    router.push({
      path: "/manage/community",
      replace: true,
    });
  }
};

const toManageReportPage = () => {
  if (checkAccess(store.state.user.loginUser, ACCESS_ENUM.ADMIN)) {
    router.push({
      path: "/manage/report",
      replace: true,
    });
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#globalHeader {
  position: relative;
}

/* logo字体样式 */
.title-bar {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-family: "幼圆", serif;
  font-weight: bolder;
}

.title {
  color: #444;
}

.logo {
  height: 46px;
}

#systemLogo {
  height: 46px;
}

#collegeLogo {
  height: 46px;
}

#imageLogo {
  height: 36px;
}
</style>
