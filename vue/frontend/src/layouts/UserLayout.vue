<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer" style="color: #efefef">
        <p>
          Copyright&copy;2024 飞某人科技实验室
          <a href="http://feecode.icu">http://feecode.icu</a> All Rights
          Reserved.备案号: 粤ICP备xxx号-1
        </p>
        <p>邮箱: nizhifei.mail@qq.com</p>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#userLayout {
  text-align: center;
  //background: url("https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png")
  //  0% 0% / 100% 100%;
  background: url("../assets/Default1.jpg") 0% 0% / 100% 100%;
  //background: url("../assets/DefaultVague.jpg") 0% 0% / 100% 100%;
  //padding: 32px 0px 24px;
}

#userLayout .logo {
  width: 64px;
  height: 64px;
}

#userLayout .header {
  //background: red;
  margin-top: 16px;
  //margin-bottom: 10px;
  //box-shadow: #eee 1px 1px 5px;
}

#userLayout .content {
  //background: linear-gradient(to right, #bbb, #fff);
  margin-bottom: 16px;
  padding: 20px;
}

#userLayout .footer {
  //background: #efefef;
  //padding: 16px;
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
<script setup lang="ts"></script>
