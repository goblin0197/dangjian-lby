<template>
  <!-- 页面整体框架 -->
  <div class="notice-detail-page">
    <!-- 顶部操作栏 -->
    <a-card style="margin-bottom: 16px">
      <a-space size="middle">
        <!-- 返回列表按钮 -->
        <a-button type="outline" @click="toShowAnnouncementListView">
          <icon-arrow-left/>
          返回公告列表
        </a-button>

        <!-- 角色差异化操作按钮 -->
        <a-button
            v-if="getReadStatus === 'unread'"
            :loading="markLoading"
            type="primary"
            @click="markAsRead"
        >
          <icon-check-circle/>
          标记为已读
        </a-button>
        <a-button type="outline" @click="printNotice">
          <icon-printer/>
          打印公告
        </a-button>
        <a-button
            v-if="userRole === 'admin'"
            type="outline"
            @click="shareNotice"
        >
          <icon-share-alt/>
          分享公告
        </a-button>
        <a-button
            v-if="userRole === 'admin'"
            status="warning"
            type="primary"
            @click="editNotice"
        >
          <icon-edit/>
          编辑公告
        </a-button>
        <a-button
            v-if="userRole === 'admin' && noticeData.status === 'published'"
            status="danger"
            type="primary"
            @click="offlineNotice"
        >
          <icon-down-circle/>
          下架公告
        </a-button>

        <!-- 阅读状态标签 -->
        <a-tag
            :color="getReadStatus === 'unread' ? 'orange' : 'green'"
            style="margin-left: auto"
        >
          {{ getReadStatus === "unread" ? "未读" : "已读" }}
        </a-tag>
      </a-space>
    </a-card>

    <!-- 公告详情核心区域（全角色可见） -->
    <a-card v-loading="loading">
      <!-- 公告基础信息区 -->
      <div
          class="notice-base-info"
          style="
          margin-bottom: 24px;
          padding-bottom: 20px;
          border-bottom: 1px solid #eee;
        "
      >
        <!-- 标题区 -->
        <h1
            style="
            font-size: 24px;
            font-weight: 600;
            color: #1d2129;
            margin: 0 0 16px 0;
            text-align: center;
          "
        >
          {{ noticeData.title }}
          <a-tag
              v-if="noticeData.isTop"
              color="red"
              size="small"
              style="margin-left: 8px"
          >置顶
          </a-tag>
        </h1>

        <!-- 元信息区 -->
        <div
            style="
            font-size: 14px;
            color: #86909c;
            text-align: center;
            line-height: 1.8;
          "
        >
          <span
          >公告类型：<a-tag :color="getTypeColor(noticeData.type)">{{
              getTypeName(noticeData.type)
            }}</a-tag></span
          >
          <span style="margin: 0 12px">|</span>
          <span>发布人：{{ noticeData.publisher }}</span>
          <span style="margin: 0 12px">|</span>
          <span>发布时间：{{ noticeData.publishTime }}</span>
          <span
              v-if="noticeData.validStart && noticeData.validEnd"
              style="margin: 0 12px"
          >|</span
          >
          <span v-if="noticeData.validStart && noticeData.validEnd"
          >生效时间：{{ noticeData.validStart }} -
            {{ noticeData.validEnd }}</span
          >
          <span style="margin: 0 12px">|</span>
          <span
          >阅读量：{{ noticeData.readCount + (pageJustOpened ? 1 : 0) }}</span
          >
        </div>
      </div>

      <!-- 公告正文区 -->
      <div
          class="notice-content"
          style="
          line-height: 2;
          font-size: 16px;
          color: #1d2129;
          margin-bottom: 32px;
        "
      >
        <div v-html="noticeData.content"></div>
      </div>

      <!-- 公告附件区 -->
      <div
          v-if="noticeData.attachments && noticeData.attachments.length"
          class="notice-attachments"
          style="
          margin-bottom: 32px;
          padding: 20px;
          background-color: #f9fafb;
          border-radius: 8px;
        "
      >
        <h3
            style="
            font-size: 18px;
            font-weight: 600;
            color: #1d2129;
            margin: 0 0 16px 0;
          "
        >
          📎 附件列表
        </h3>
        <a-space direction="vertical" style="width: 100%">
          <a-card
              v-for="(file, index) in noticeData.attachments"
              :key="index"
              hoverable
              style="border: 1px solid #e5e6eb"
          >
            <div
                style="
                display: flex;
                align-items: center;
                justify-content: space-between;
              "
            >
              <div style="display: flex; align-items: center">
                <icon-paper-clip
                    style="font-size: 18px; color: #165dff; margin-right: 12px"
                />
                <div>
                  <div
                      style="font-size: 14px; font-weight: 500; color: #1d2129"
                  >
                    {{ file.name }}
                  </div>
                  <div style="font-size: 12px; color: #86909c; margin-top: 4px">
                    格式：{{ file.name.split(".").pop() }} | 大小：{{
                      file.size || "未知"
                    }}
                  </div>
                </div>
              </div>
              <a-space size="middle">
                <a-button type="text" @click="previewAttachment(file)">
                  <icon-eye/>
                  预览
                </a-button>
                <a-button type="primary" @click="downloadAttachment(file)">
                  <icon-download/>
                  下载
                </a-button>
              </a-space>
            </div>
          </a-card>
        </a-space>
      </div>

      <!-- 操作按钮区（底部） -->
      <div
          class="notice-operation"
          style="display: flex; justify-content: center; gap: 16px"
      >
        <a-button type="outline" @click="goBack">
          <icon-arrow-left/>
          返回列表
        </a-button>
        <a-button
            v-if="getReadStatus === 'unread'"
            :loading="markLoading"
            type="primary"
            @click="markAsRead"
        >
          <icon-check-circle/>
          标记为已读
        </a-button>
        <a-button type="outline" @click="printNotice">
          <icon-printer/>
          打印公告
        </a-button>
        <a-button
            v-if="userRole === 'admin'"
            status="warning"
            type="primary"
            @click="editNotice"
        >
          <icon-edit/>
          编辑公告
        </a-button>
      </div>
    </a-card>

    <!-- 附件预览弹窗 -->
    <a-modal
        v-model:visible="previewModalVisible"
        :footer="null"
        :title="previewFile?.name || '附件预览'"
        height="80vh"
        width="90%"
        @cancel="previewModalVisible = false"
    >
      <div
          style="
          width: 100%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        "
      >
        <div v-if="previewFile" class="attachment-preview">
          <!-- PDF预览（模拟） -->
          <div
              v-if="previewFile.name.endsWith('.pdf')"
              style="width: 100%; height: 70vh; border: 1px solid #eee"
          >
            <div style="text-align: center; padding: 40px; color: #86909c">
              <icon-file-text style="font-size: 48px; margin-bottom: 16px"/>
              <div>PDF预览功能需集成PDF.js实现</div>
              <div style="margin-top: 8px">
                当前展示模拟预览界面，实际项目中可接入pdfjs-dist库
              </div>
              <a-button
                  style="margin-top: 16px"
                  type="primary"
                  @click="downloadAttachment(previewFile)"
              >
                <icon-download/>
                下载原文件
              </a-button>
            </div>
          </div>
          <!-- 文档预览（模拟） -->
          <div
              v-else-if="
              previewFile.name.endsWith('.docx') ||
              previewFile.name.endsWith('.doc')
            "
              style="width: 100%; height: 70vh; border: 1px solid #eee"
          >
            <div style="text-align: center; padding: 40px; color: #86909c">
              <icon-file-text style="font-size: 48px; margin-bottom: 16px"/>
              <div>Word文档预览功能需接入第三方预览服务（如kkfileview）</div>
              <a-button
                  style="margin-top: 16px"
                  type="primary"
                  @click="downloadAttachment(previewFile)"
              >
                <icon-download/>
                下载原文件
              </a-button>
            </div>
          </div>
          <!-- 表格预览（模拟） -->
          <div
              v-else-if="
              previewFile.name.endsWith('.xlsx') ||
              previewFile.name.endsWith('.xls')
            "
              style="width: 100%; height: 70vh; border: 1px solid #eee"
          >
            <div style="text-align: center; padding: 40px; color: #86909c">
              <icon-file-text style="font-size: 48px; margin-bottom: 16px"/>
              <div>Excel表格预览功能需接入第三方预览服务（如kkfileview）</div>
              <a-button
                  style="margin-top: 16px"
                  type="primary"
                  @click="downloadAttachment(previewFile)"
              >
                <icon-download/>
                下载原文件
              </a-button>
            </div>
          </div>
          <!-- 图片预览 -->
          <div
              v-else-if="
              previewFile.name.endsWith('.jpg') ||
              previewFile.name.endsWith('.png')
            "
              style="
              width: 100%;
              height: 70vh;
              display: flex;
              justify-content: center;
              align-items: center;
            "
          >
            <img
                :src="previewFile.url"
                alt="图片预览"
                style="max-width: 100%; max-height: 100%; object-fit: contain"
            />
          </div>
          <!-- 其他格式 -->
          <div v-else style="text-align: center; padding: 40px; color: #86909c">
            <icon-file-text style="font-size: 48px; margin-bottom: 16px"/>
            <div>暂不支持该格式预览，请下载后查看</div>
            <a-button
                style="margin-top: 16px"
                type="primary"
                @click="downloadAttachment(previewFile)"
            >
              <icon-download/>
              下载原文件
            </a-button>
          </div>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, onUnmounted, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router"; // 路由相关（实际项目需配置路由）
import message from "@arco-design/web-vue/es/message";
// 导入API
import * as gonggaoguanli from "@/api/gonggaoguanli";
import * as gonggaokuozhangongneng from "@/api/gonggaokuozhangongneng";
// 导入Arco图标
import {
  IconArrowLeft,
  IconCheckCircle,
  IconDownCircle,
  IconDownload,
  IconEdit,
  IconEye,
  IconFileText,
  IconPaperClip,
  IconPrinter,
  IconShareAlt,
} from "@arco-design/web-vue/es/icon";

// 1. 基础配置
// 模拟用户角色（实际从登录态获取：admin/teacher/student）
const userRole = ref("student"); // 可切换测试：admin/teacher/student

// 路由实例（实际项目需配置路由，如 /notice/detail/:id）
const route = useRoute();
const router = useRouter();
// 获取公告ID（从路由参数）
const noticeId = ref((route.params.id as string) || "1");

// 加载状态
const loading = ref(true);
const markLoading = ref(false);
// 页面刚打开标记（用于阅读量+1）
const pageJustOpened = ref(true);

// 2. 公告详情数据（模拟接口返回）
const noticeData = reactive({
  id: "",
  title: "",
  type: "",
  publisher: "",
  status: "",
  publishTime: "",
  isTop: false,
  validStart: "",
  validEnd: "",
  content: "",
  attachments: [] as Array<{
    name: string;
    url: string;
    size?: string;
  }>,
  readCount: 0,
});

// 3. 已读公告ID列表（实际从后端获取）
const readNoticeIds = ref<string[]>(["2"]); // 模拟用户已读公告ID

// 4. 附件预览相关
const previewModalVisible = ref(false);
const previewFile = ref<any>(null);

// 5. 辅助方法
// 获取公告类型名称
const getTypeName = (type: string) => {
  switch (type) {
    case "notice":
      return "通知类";
    case "activity":
      return "活动类";
    case "remind":
      return "提醒类";
    case "policy":
      return "政策类";
    default:
      return "未知类型";
  }
};

// 获取公告类型颜色
const getTypeColor = (type: string) => {
  switch (type) {
    case "notice":
      return "blue";
    case "activity":
      return "green";
    case "remind":
      return "orange";
    case "policy":
      return "purple";
    default:
      return "gray";
  }
};

// 获取当前公告阅读状态
const getReadStatus = () => {
  return readNoticeIds.value.includes(noticeData.id) ? "read" : "unread";
};

// 6. 核心方法
// 获取公告详情（调用真实API）
const getNoticeDetail = async () => {
  loading.value = true;
  try {
    // 调用API获取公告详情
    const res = await gonggaoguanli.getNoticeVoUsingGet({ id: noticeId.value });
    if (res.code === 0) {
      // 赋值到响应式数据
      Object.assign(noticeData, res.data);

      // 增加阅读量
      await gonggaokuozhangongneng.incrementReadCountUsingPut({ announcementId: noticeId.value });
      
      // 自动标记已读（打开详情页即标记）
      if (!readNoticeIds.value.includes(noticeData.id)) {
        await markAsRead(true); // 静默标记（不显示提示）
        // 阅读量+1
        noticeData.readCount += 1;
      }

      pageJustOpened.value = false;
    } else {
      message.error(res.message || "获取公告详情失败");
    }
  } catch (error) {
    message.error("获取公告详情失败，请刷新重试");
    console.error("获取公告详情失败：", error);
  } finally {
    loading.value = false;
  }
};

// 返回公告列表
const goBack = () => {
  // 实际项目中跳转到公告列表路由
  router.push("/notice/list").catch((err) => console.error(err));
  message.info("已返回公告列表");
};

// 标记为已读
const markAsRead = async (silent = false) => {
  if (getReadStatus() === "read") {
    !silent && message.info("该公告已标记为已读");
    return;
  }

  markLoading.value = true;
  try {
    // 调用API标记已读
    const res = await gonggaokuozhangongneng.markAsReadUsingPost({ announcementId: noticeData.id });
    if (res.code === 0) {
      // 标记已读
      readNoticeIds.value.push(noticeData.id);
      !silent && message.success("已成功标记为已读");
    } else {
      !silent && message.error(res.message || "标记已读失败");
    }
  } catch (error) {
    !silent && message.error("标记已读失败，请重试");
    console.error("标记已读失败：", error);
  } finally {
    markLoading.value = false;
  }
};

// 打印公告
const printNotice = () => {
  message.info("即将打印公告详情");

  // 模拟打印逻辑（实际项目中调用浏览器打印API）
  const printWindow = window.open("", "_blank");
  if (printWindow) {
    // 构建打印HTML
    printWindow.document.write(`
      <!DOCTYPE html>
      <html>
        <head>
          <meta charset="UTF-8">
          <title>${noticeData.title}</title>
          <style>
            body { font-family: "Microsoft YaHei", sans-serif; line-height: 2; font-size: 16px; padding: 40px; }
            h1 { text-align: center; font-size: 24px; font-weight: 600; margin-bottom: 20px; }
            .meta { text-align: center; font-size: 14px; color: #666; margin-bottom: 30px; border-bottom: 1px solid #eee; padding-bottom: 10px; }
            .content { font-size: 16px; line-height: 2; }
          </style>
        </head>
        <body>
          <h1>${noticeData.title}</h1>
          <div class="meta">
            公告类型：${getTypeName(noticeData.type)} | 发布人：${
        noticeData.publisher
    } | 发布时间：${noticeData.publishTime}
          </div>
          <div class="content">${noticeData.content}</div>
        </body>
      </html>
    `);
    printWindow.document.close();
    printWindow.print();
  }
};

// 分享公告（仅管理员）
const shareNotice = () => {
  if (userRole.value !== "admin") {
    message.warning("仅管理员可分享公告");
    return;
  }

  // 调用API生成分享链接
  message.confirm({
    title: "分享公告",
    content: `确定要分享【${noticeData.title}】吗？分享后将生成链接可发送给指定人员`,
    onOk: async () => {
      try {
        const res = await gonggaokuozhangongneng.generateShareUsingPost({ announcementId: noticeData.id });
        if (res.code === 0) {
          const shareUrl = `${window.location.origin}/notice/share/${res.data?.shareCode}`;
          // 复制到剪贴板
          navigator.clipboard
              .writeText(shareUrl)
              .then(() => {
                message.success(`分享链接已复制到剪贴板：${shareUrl}`);
              })
              .catch(() => {
                message.success(`分享链接：${shareUrl}（请手动复制）`);
              });
        } else {
          message.error(res.message || "生成分享链接失败");
        }
      } catch (error) {
        console.error("生成分享链接失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

// 编辑公告（仅管理员，跳转到管理视图）
const editNotice = () => {
  if (userRole.value !== "admin") {
    message.warning("仅管理员可编辑公告");
    return;
  }

  // 实际项目中跳转到公告编辑路由
  router
      .push(`/notice/manage/edit/${noticeData.id}`)
      .catch((err) => console.error(err));
  message.info("即将跳转到公告编辑页面");
};

// 下架公告（仅管理员）
const offlineNotice = () => {
  if (userRole.value !== "admin") {
    message.warning("仅管理员可下架公告");
    return;
  }

  message.confirm({
    title: "下架公告",
    content: `确定要下架【${noticeData.title}】吗？下架后仅管理员可见`,
    onOk: async () => {
      loading.value = true;
      try {
        // 调用API下架公告
        const res = await gonggaoguanli.offlineNoticeUsingPut({ id: noticeData.id });
        if (res.code === 0) {
          noticeData.status = "offline";
          message.success("公告已成功下架");
          // 跳回列表
          setTimeout(() => goBack(), 1000);
        } else {
          message.error(res.message || "下架公告失败");
        }
      } catch (error) {
        message.error("下架公告失败，请重试");
        console.error("下架公告失败：", error);
      } finally {
        loading.value = false;
      }
    },
  });
};

// 预览附件
const previewAttachment = async (file: any) => {
  try {
    const res = await gonggaokuozhangongneng.previewAnnouncementAttachmentUsingGet({ attachmentId: file.id });
    if (res.code === 0) {
      previewFile.value = file;
      previewModalVisible.value = true;
      message.info(`正在预览【${file.name}】`);
    } else {
      message.error(res.message || "预览附件失败");
    }
  } catch (error) {
    console.error("预览附件失败:", error);
    message.error("网络请求异常");
  }
};

// 下载附件
const downloadAttachment = async (file: any) => {
  try {
    const res = await gonggaokuozhangongneng.downloadAnnouncementAttachmentUsingGet({ attachmentId: file.id });
    if (res.code === 0) {
      message.success(`已开始下载【${file.name}】`);
      // 实际项目中处理下载
    } else {
      message.error(res.message || "下载附件失败");
    }
  } catch (error) {
    console.error("下载附件失败:", error);
    message.error("网络请求异常");
  }
};

// 7. 生命周期
onMounted(() => {
  // 初始化获取公告详情
  getNoticeDetail();
});

onUnmounted(() => {
  // 清理资源
  previewFile.value = null;
});

const toShowAnnouncementManagementView = () => {
  router.push("/announcementManagement");
};

const toShowAnnouncementListView = () => {
  router.push("/announcementList");
};
</script>

<style scoped>
/* 页面基础样式 */
.notice-detail-page {
  padding: 16px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 正文样式适配 */
.notice-content :deep(p) {
  margin: 0 0 16px 0;
}

.notice-content :deep(ul) {
  margin: 0 0 16px 20px;
  padding: 0;
}

.notice-content :deep(li) {
  margin: 0 0 8px 0;
}

/* 附件预览样式 */
.attachment-preview {
  width: 100%;
  height: 100%;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .notice-detail-page {
    padding: 8px;
  }

  .notice-content {
    font-size: 14px;
  }

  .notice-base-info h1 {
    font-size: 20px;
  }
}
</style>
