import router from "@/router";
import store from "@/store";
import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";

// router.beforeEach(async (to, from, next) => {
//   // console.log("登录用户信息", store.state.user.loginUser);
//   console.log("导航到:", to.fullPath);
//   let loginUser = store.state.user.loginUser;
//   console.log("当前用户:", loginUser);
//   // 如果之前没登陆过，自动登录
//   if (!loginUser || !loginUser.userRole) {
//     // 加 await 是为了等用户登录成功之后，再执行后续的代码
//     await store.dispatch("user/getLoginUser");
//     loginUser = store.state.user.loginUser;
//     console.log("获取用户后:", loginUser);
//   }
//   const needAccess = (to.meta?.access ?? ACCESS_ENUM.NOT_LOGIN) as string;
//   // 要跳转的页面必须要登录
//   if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
//     // 如果没登录，跳转到登录页面
//     if (
//       !loginUser ||
//       !loginUser.userRole ||
//       loginUser.userRole === ACCESS_ENUM.NOT_LOGIN
//     ) {
//       next(`/user/login?redirect=${to.fullPath}`);
//       return;
//     }
//     // 如果已经登录了，但是权限不足，那么跳转到无权限页面
//     if (!checkAccess(loginUser, needAccess)) {
//       next("noAuth");
//       return;
//     }
//   }
//   // console.log(to.meta?.access);
//   console.log("最终决定:", to.fullPath);
//   next();
// });

router.beforeEach(async (to, from, next) => {
  console.log("导航到:", to.fullPath);
  let loginUser = store.state.user.loginUser;
  console.log("当前用户:", loginUser);
  if (!loginUser || !loginUser.userRole) {
    await store.dispatch("user/getLoginUser");
    loginUser = store.state.user.loginUser;
    console.log("获取用户后:", loginUser);
  }
  const needAccess = (to.meta?.access ?? ACCESS_ENUM.NOT_LOGIN) as string;
  console.log("needAccess:", needAccess);
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    console.log("登录状态检查:", {
      noUser: !loginUser,
      noRole: !loginUser?.userRole,
      isNotLogin: loginUser?.userRole === ACCESS_ENUM.NOT_LOGIN,
    });
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === ACCESS_ENUM.NOT_LOGIN
    ) {
      console.log("未登录，重定向到登录页");
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    if (!checkAccess(loginUser, needAccess)) {
      console.log("权限不足，跳转到无权限页");
      next("noAuth");
      return;
    }
  }
  console.log("最终决定: 放行");
  next();
});
