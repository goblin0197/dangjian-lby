import {createApp} from "vue";
import App from "./App.vue";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import router from "./router";
import store from "./store";
import "@/plugins/axios";

// 全局捕获 ResizeObserver 循环错误
const originalResizeObserver = window.ResizeObserver;
if (originalResizeObserver) {
  window.ResizeObserver = class ResizeObserver extends originalResizeObserver {
    constructor(callback: ResizeObserverCallback) {
      super((entries, observer) => {
        try {
          callback(entries, observer);
        } catch (error) {
          console.warn('ResizeObserver error:', error);
        }
      });
    }
  };
}

createApp(App).use(ArcoVue).use(store).use(router).mount("#app");
