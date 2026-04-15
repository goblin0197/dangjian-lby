<template>
  <div id="homeView">
    <!--登录组件-->
    <UserLoginView/>
  </div>
</template>

<script lang="ts" setup>
import UserLoginView from "@/views/user/UserLoginView.vue";
</script>

<style scoped>
#homeView {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 确保容器的高度为视口的高度 */
  width: 100vw; /* 确保容器的宽度为视口的宽度 */
  margin: 0; /* 去除默认的 margin */
}
</style>
