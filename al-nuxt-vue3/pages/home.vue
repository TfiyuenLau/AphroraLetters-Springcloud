<script setup lang="ts">
import {onBeforeMount, onMounted, ref} from 'vue';
import {getRecommendArticles} from "../api/ArticleApi";
import type {ApiResult} from "../models/ApiResult";
import type {ArticleInfo} from "../models/Article";
import type {Announcement} from "../models/Announcement";
import type {VersionLog} from "../models/VersionLog";
import {getAnnouncements, getAnnouncementList} from "../api/AnnouncementApi";
import {getVersionLogs, getVersionLogList} from "../api/VersionLogApi";

let router = useRouter();

// 声明实例数据
const recommendArticleInfos = ref<ArticleInfo[]>();
const announcements = ref<Announcement[]>();
const announcementList = ref<Announcement[]>();
const versionLogs = ref<VersionLog[]>();
const versionLogList = ref<VersionLog[]>();

// 调用后端服务器请求
onBeforeMount(() => {
  document.title = "Aphrora Letters | 首页";

  getRecommendArticles().then((res: ApiResult<ArticleInfo[]>) => {
    recommendArticleInfos.value = res.data;
  });

  getAnnouncements().then((res: ApiResult<Announcement[]>) => {
    announcements.value = res.data;
  });

  getVersionLogs().then((res: ApiResult<VersionLog[]>) => {
    versionLogs.value = res.data;
  });
});

// 在组件挂载到DOM后被调用
onMounted(() => {

});

// 发送异步请求并在按钮点击后获取全部公告消息
const handleAnnouncementList = async () => {
  getAnnouncementList().then((res: ApiResult<Announcement[]>) => {
    announcementList.value = res.data;
  });
};

// 发送异步请求并在按钮点击后获取全部版本日志消息
const handleVersionLogList = async () => {
  getVersionLogList().then((res: ApiResult<VersionLog[]>) => {
    versionLogList.value = res.data;
  });
};

// 使用路由打开公告页面
const openAnnouncement = (announcementId: number) => {
  router.push({path: `/announcement/${announcementId}`, params: {'id': announcementId}});
};

</script>

<template>
  <!-- 导航栏组件 -->
  <navbar-component/>

  <!-- 网站首页内容 -->
  <div class="container">
    <!-- 正文:背景透明 -->
    <div class="mt-3 rounded p-3" id="bg">

      <!-- 第一行 -->
      <a-row :gutter="16">
        <a-col :span="24">
          <div class="container mt-3 rounded">
            <!-- Jumbotron -->
            <div class="row mt-3 p-5 bg-danger text-white rounded">
              <h1>欢迎访问 Aphrora Letters ！</h1>
              <p class="lead">
                <strong>Aphrora Letters</strong>是一个<strong>分享哲学社科文章的个人社区</strong>，我们以致力于推广左翼进步文化为宗旨，刊载哲学社科类社评文章、国内外社会运动报导与其他文体；并对当今各主流学派的学术观点与经典著作进行收录整理。
              </p>
              <p>
                注意：本项目仍处于<strong>测试阶段</strong>，WEB应用内部的Logo、图片素材和部分转载文章内容等均来源于网络，<strong>仅用于个人学习用途</strong>，若侵犯了您的著作权请即使联系
                <strong>tfiyuenlau@foxmail.com</strong>
                进行删除。网站仍在测试阶段，并积极依照《网络安全法》《网络安全管理条例》相关法律条例进行运营，若存在黄赌毒、政治敏感言论请向网站管理员举报，谢谢配合。
              </p>
            </div>

            <div class="row mt-3 rounded">
              <img class="img-thumbnail img-fluid mx-auto d-block" src="/img/封面图.jpg" alt="封面图">
              <p class="h6 text-center text-muted" style="font-weight: bold">
                Aphrora Letters | 一个刊载社哲时评的文库社区
              </p>
            </div>
          </div>
        </a-col>
      </a-row>

      <!-- 第二行 -->
      <a-row :gutter="16">
        <div class="container rounded">
          <a-skeleton :loading="!recommendArticleInfos" active :paragraph="{ rows: 8 }">
            <a-row :gutter="16" style="display: flex;align-content: center;justify-content: center">
              <!-- 文章轮播图 -->
              <a-col :xs="24" :lg="17" class="mt-3">
                <a-card>
                  <template #title>
                    <span class="bi bi-graph-up-arrow text-danger">近日热文</span>
                  </template>
                  <a-carousel autoplay v-if="recommendArticleInfos">
                    <a :href="'/article/' + articleInfo.id"
                       v-for="articleInfo in recommendArticleInfos?.slice(0, 5)"
                       :key="articleInfo.id"
                       class="carousel-item"
                    >
                      <div class="carousel-image">
                        <img :src="'/api/article/' + articleInfo.pictureUrl" :alt="articleInfo.title"/>
                      </div>
                      <h3>{{ articleInfo.title }}</h3>
                    </a>
                  </a-carousel>
                </a-card>
              </a-col>

              <!-- 文章推荐 -->
              <a-col :xs="24" :lg="7" class="mt-3">
                <a-card>
                  <template #title>
                    <span class="bi bi-globe text-danger">推荐文章</span>
                  </template>
                  <a-list bordered :data-source="recommendArticleInfos" v-if="recommendArticleInfos">
                    <template #renderItem="{ item }">
                      <a-list-item :key="item.id">
                        <nuxt-link :to="'/article/' + item.id"
                           style="text-decoration: none;font-size: larger;font-family: 楷体,serif;color: #8E354A">
                          {{ item.title }}
                        </nuxt-link>
                      </a-list-item>
                    </template>
                  </a-list>
                </a-card>
              </a-col>
            </a-row>
          </a-skeleton>
        </div>
      </a-row>

      <!-- 第三行 -->
      <div class="container row">
        <div class="card border-warning shadow mt-3">
          <div class="card-body">
            <a-row :gutter="16">
              <a-col :span="24">
                <div style="float: left;">
                  <img class="img-fluid" style="max-width: 48px" src="/img/公告栏.png" alt="公告栏">
                </div>
                <div class="h1 text-center"
                     style="font-family: 楷体,serif;color: #494D55;font-weight: bold;word-spacing: 8px">
                  公 告 栏
                </div>
              </a-col>
            </a-row>
            <hr>
            <a-row :gutter="16">
              <!-- 最新消息 -->
              <a-col :xs="24" :lg="16">
                <h3 class="border-start border-warning text-danger"
                    style="font-family: 楷体,serif;font-weight: bold;">最新消息</h3>
                <a-skeleton :loading="!announcements" active :paragraph="{ rows: 8 }">
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item" v-for="announcement in announcements" style="cursor: pointer">
                      <a class="text-black text-decoration-none lead" @click="openAnnouncement(announcement.id)">
                        {{ announcement.title }}
                      </a>
                      <span class="badge bg-secondary">{{ announcement.time }}</span>
                    </li>
                  </ul>
                </a-skeleton>
                <div class="text-end">
                  <button class="btn btn-sm btn-danger" data-bs-toggle="offcanvas" @click="handleAnnouncementList"
                          data-bs-target="#announcementOffcanvas" aria-controls="offcanvasExample">
                    显示更多
                  </button>
                </div>
              </a-col>

              <!-- 版本日志 -->
              <a-col :xs="24" :lg="8">
                <h3 class="border-start border-warning text-danger"
                    style="font-family: 楷体,serif;font-weight: bold;">版本日志</h3>
                <a-skeleton :loading="!versionLogs" active :paragraph="{ rows: 8 }">
                  <a-timeline>
                    <a-timeline-item v-for="(versionLog, index) in versionLogs" :key="index">
                      <span class="badge bg-danger">{{ 'VERSION: ' + versionLog.version }}</span>
                      <div>
                        <span>{{ versionLog.log }}</span>
                        <span class="badge bg-secondary">{{ versionLog.time }}</span>
                      </div>
                    </a-timeline-item>
                  </a-timeline>
                </a-skeleton>
                <div class="text-end">
                  <button class="btn btn-sm btn-danger" type="button" data-bs-toggle="offcanvas"
                          @click="handleVersionLogList"
                          data-bs-target="#versionOffcanvas" aria-controls="offcanvasExample">
                    显示更多
                  </button>
                </div>
              </a-col>
            </a-row>

          </div>
        </div>
      </div>

      <!-- 公共消息的滑动侧边栏 -->
      <div class="offcanvas offcanvas-start" tabindex="-1" id="announcementOffcanvas"
           data-bs-scroll="true" aria-labelledby="offcanvasExampleLabel1">
        <div class="offcanvas-header">
          <h3 class="offcanvas-title border-start border-warning text-danger"
              id="offcanvasExampleLabel1" style="font-family: 楷体,serif;font-weight: bold;">最新消息</h3>
          <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body">
          <ul class="list-group list-group-flush">
            <li class="list-group-item" v-for="announcement in announcementList" style="cursor: pointer">
              <a class="text-black text-decoration-none lead" @click="openAnnouncement(announcement.id)">
                {{ announcement.title }}
              </a>
              <span class="badge bg-secondary">{{ announcement.time }}</span>
            </li>
          </ul>
        </div>
      </div>
      <!-- 版本日志的滑动侧边栏 -->
      <div class="offcanvas offcanvas-end" tabindex="-1" id="versionOffcanvas"
           data-bs-scroll="true" aria-labelledby="offcanvasExampleLabel2">
        <div class="offcanvas-header">
          <h3 class="offcanvas-title border-start border-warning text-danger"
              id="offcanvasExampleLabel2" style="font-family: 楷体,serif;font-weight: bold;">版本日志</h3>
          <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body">
          <a-timeline>
            <a-timeline-item v-for="(versionLog, index) in versionLogList" :key="index">
              <span class="badge bg-danger">{{ 'VERSION: ' + versionLog.version }}</span>
              <div>
                <span>{{ versionLog.log }}</span>
                <span class="badge bg-secondary">{{ versionLog.time }}</span>
              </div>
            </a-timeline-item>
          </a-timeline>
        </div>
      </div>
      <br>
    </div>
    <!-- 底部留白 -->
    <br>
  </div>

  <!-- 页脚 -->
  <footer-component/>
</template>

<style scoped>
/* 取消bootstrap边距，解决移动端右划白边 */
.row {
  margin: 0;
}

/* 轮播图样式 */
.carousel-item {
  position: relative;
}

.carousel-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 让图片自然充满容器 */
}

.carousel-image {
  overflow: hidden;
}

.carousel-item h3 {
  position: absolute;
  bottom: 32px;
  width: 100%;
  text-align: center;
  color: #fff;
  background: rgba(0, 0, 0, 0.25); /* 添加半透明背景以提升标题可读性 */
  padding: 10px;
  box-sizing: border-box;
  font-size: x-large;
}

</style>
