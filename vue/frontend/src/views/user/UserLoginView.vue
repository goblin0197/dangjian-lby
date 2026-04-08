<template>
  <div class="login-container">
    <!-- 背景渐变 -->
    <div class="bg-gradient"></div>
    <!-- 背景网格 -->
    <div class="bg-grid"></div>
    <!-- 背景动态粒子 -->
    <div class="particles">
      <div
        v-for="i in 50"
        :key="i"
        class="particle"
        :style="{ animationDelay: `${i * 0.1}s` }"
      ></div>
    </div>

    <!-- 登录卡片 -->
    <div class="card">
      <div class="card-header">
        <div class="logo">
          <div class="logo-icon">
            <icon-shield class="icon" />
          </div>
          <h1 class="logo-text">智慧党建系统</h1>
        </div>
        <h2 class="login-title">用户登录</h2>
        <p class="login-subtitle">请输入您的账号和密码</p>
      </div>

      <form @submit.prevent="handleSubmit">
        <!-- 浮动标签输入框 -->
        <div class="form-group">
          <div class="input-wrapper">
            <icon-user class="input-icon" />
            <input
              id="username"
              v-model="form.userAccount"
              required
              type="text"
              autocomplete="off"
              placeholder="请输入用户名"
            />
          </div>
        </div>

        <div class="form-group">
          <div class="input-wrapper">
            <icon-lock class="input-icon" />
            <input
              id="password"
              v-model="form.userPassword"
              required
              type="password"
              autocomplete="off"
              placeholder="请输入密码"
            />
          </div>
        </div>

        <!-- 测试账号信息 -->
        <div class="test-account">
          <icon-info-circle class="info-icon" />
          <span>测试账号：superadmin</span>
          <span>测试密码：123456</span>
        </div>

        <!-- 动态错误提示 -->
        <transition name="shake">
          <div v-if="errorMessage" class="error-message">
            <icon-exclamation-circle />
            <span>{{ errorMessage }}</span>
          </div>
        </transition>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn" :disabled="isSubmitting">
          <span class="btn-text">{{
            isSubmitting ? "登录中..." : "立即登录"
          }}</span>
          <div class="btn-effect"></div>
        </button>
      </form>

      <!-- 底部装饰 -->
      <div class="card-footer">
        <div class="footer-links">
          <a href="#" class="footer-link">忘记密码？</a>
          <a href="#" class="footer-link">联系管理员</a>
        </div>
        <div class="copyright">© 2026 智慧党建系统. 版权所有</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import * as yonghuguanli from "@/api/yonghuguanli";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import {
  IconLock,
  IconUser,
  IconExclamationCircle,
  IconInfoCircle,
  IconShield,
} from "@arco-design/web-vue/es/icon";

const form = reactive({
  userAccount: "",
  userPassword: "",
});

const errorMessage = ref(null);
const isSubmitting = ref(false);
const router = useRouter();
const store = useStore();

const handleSubmit = async () => {
  try {
    isSubmitting.value = true;
    const res = await yonghuguanli.userLoginUsingPost(form);
    if (res.data.code === 0) {
      console.log("登录成功，准备获取用户信息");
      await store.dispatch("user/getLoginUser");
      console.log("获取用户信息完成，准备跳转");
      router.push("/organization");
      console.log("跳转指令已发出");
    } else {
      showError(res.data.message || "登录失败");
    }
  } catch (error) {
    showError("网络请求异常");
  } finally {
    isSubmitting.value = false;
  }
};

const showError = (msg) => {
  errorMessage.value = msg;
  setTimeout(() => (errorMessage.value = null), 3000);
};
</script>

<style scoped>
/* 基础布局 */
.login-container {
  min-height: 100vh;
  min-width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  position: relative;
  animation: pageEnter 1s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 背景渐变 */
.bg-gradient {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  z-index: 0;
}

/* 背景网格 */
.bg-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(
      rgba(255, 255, 255, 0.05) 1px,
      transparent 1px
    ),
    linear-gradient(90deg, rgba(255, 255, 255, 0.05) 1px, transparent 1px);
  background-size: 50px 50px;
  z-index: 1;
}

/* 动态粒子背景 */
.particles {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 2;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: float 20s infinite linear;
}

@keyframes float {
  0% {
    transform: translateY(100vh) scale(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100%) scale(1);
    opacity: 0;
  }
}

/* 登录卡片 */
.card {
  position: relative;
  width: 420px;
  padding: 50px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 20px;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  z-index: 10;
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);
}

/* 卡片头部 */
.card-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  box-shadow: 0 10px 30px rgba(79, 172, 254, 0.4);
  animation: logoPulse 3s infinite;
}

@keyframes logoPulse {
  0%,
  100% {
    box-shadow: 0 10px 30px rgba(79, 172, 254, 0.4);
  }
  50% {
    box-shadow: 0 15px 40px rgba(79, 172, 254, 0.6);
  }
}

.logo-icon .icon {
  font-size: 40px;
  color: white;
}

.logo-text {
  font-size: 28px;
  font-weight: 700;
  color: white;
  margin: 0;
  letter-spacing: 2px;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: white;
  margin: 0 0 10px 0;
}

.login-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
}

/* 输入框组 */
.form-group {
  margin-bottom: 25px;
}

.input-wrapper {
  position: relative;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.input-wrapper:focus-within {
  border-color: rgba(79, 172, 254, 0.5);
  box-shadow: 0 0 0 3px rgba(79, 172, 254, 0.1);
}

.input-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, 0.6);
  font-size: 18px;
}

input {
  width: 100%;
  height: 55px;
  background: transparent;
  border: none;
  outline: none;
  color: white;
  font-size: 16px;
  padding: 0 50px 0 45px;
}

input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

/* 测试账号信息 */
.test-account {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 12px 15px;
  margin: 20px 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.test-account .info-icon {
  color: #4facfe;
  font-size: 16px;
}

.test-account span {
  flex: 1;
  text-align: center;
}

/* 错误提示 */
.error-message {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #ff6b6b;
  margin: 15px 0;
  padding: 12px 15px;
  background: rgba(255, 107, 107, 0.1);
  border-radius: 8px;
  border-left: 4px solid #ff6b6b;
  animation: shake 0.6s cubic-bezier(0.36, 0.07, 0.19, 0.97);
}

@keyframes shake {
  10%,
  90% {
    transform: translateX(-2px);
  }
  20%,
  80% {
    transform: translateX(3px);
  }
  30%,
  50%,
  70% {
    transform: translateX(-5px);
  }
  40%,
  60% {
    transform: translateX(5px);
  }
}

/* 提交按钮 */
.submit-btn {
  position: relative;
  width: 100%;
  height: 55px;
  border: none;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  margin-top: 20px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(79, 172, 254, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-text {
  position: relative;
  z-index: 2;
}

.btn-effect {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transform: rotate(45deg);
  transition: all 0.6s ease;
  z-index: 1;
}

.submit-btn:hover .btn-effect {
  animation: btnShine 1.5s infinite;
}

@keyframes btnShine {
  0% {
    transform: translateX(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) rotate(45deg);
  }
}

/* 卡片底部 */
.card-footer {
  margin-top: 40px;
  text-align: center;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 20px;
}

.footer-link {
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}

.footer-link:hover {
  color: #4facfe;
}

.copyright {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
}

/* 响应式设计 */
@media (max-width: 480px) {
  .card {
    width: 90%;
    padding: 30px;
  }

  .logo-text {
    font-size: 24px;
  }

  .login-title {
    font-size: 20px;
  }
}

/* 页面入场动画 */
@keyframes pageEnter {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
