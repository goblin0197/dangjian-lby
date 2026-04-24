<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user') && !route.path.startsWith('/userManagement')">
      <router-view/>
    </template>
    <template v-else>
      <!--      <HomeView />-->
      <BasicLayout/>
    </template>
  </div>
</template>

<style scoped>
#app {
}
</style>
<script lang="ts" setup>
// import { anchorInjectionKey } from "@arco-design/web-vue/es/anchor/context";
import {onMounted} from "vue";
import "@/access";
import {useRoute} from "vue-router";
import BasicLayout from "@/layouts/BasicLayout.vue";

const route = useRoute();

/**
 * 全局初始化函数，有全局单词调用的代码，都可以写到这里
 */
const doInit = () => {
  console.log("hello world");
};

onMounted(() => {
  doInit();
  const debounce = (callback: (...args: any[]) => void, delay: number) => {
    let tid: any;
    return function (...args: any[]) {
      const ctx = self;
      tid && clearTimeout(tid);
      tid = setTimeout(() => {
        callback.apply(ctx, args);
      }, delay);
    };
  };

  const _ = (window as any).ResizeObserver;
  (window as any).ResizeObserver = class ResizeObserver extends _ {
    constructor(callback: (...args: any[]) => void) {
      callback = debounce(callback, 2000);
      super(callback);
    }
  };
  onMounted(() => {
    // 在组件挂载后执行 ResizeObserver 的逻辑
    new (window as any).ResizeObserver(() => {
      // 在这里放置 ResizeObserver 的回调逻辑
    });
  });
});
</script>
