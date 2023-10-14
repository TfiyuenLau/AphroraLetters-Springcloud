<script setup lang="ts">
import '@/assets/index.css';
import NavbarComponent from '@/components/Navbar.vue';
import Footer from "@/components/Footer.vue";
import axiosHttp from "@/axios.http";
import {ref, onMounted, computed} from 'vue';
import router from "@/router";

interface ArticleInfo {
  id: number;
  title: string;
  pictureUrl: string,
  summary: string;
  isTop: boolean;
  traffic: number;
  createBy: string;
  modifiedBy: string;
  isEffective: boolean;
  categoryList: null | string[];
  time: string;
}

interface Announcement {
  id: number;
  publisher: string;
  title: string;
  content: string;
  createBy: string;
  isEffective: boolean;
  time: string;
}

interface VersionLog {
  id: number;
  version: string;
  log: string;
  createBy: string;
  time: string;
}

// 声明实例数据
const recommendArticleInfos = ref<ArticleInfo[]>();
const announcements = ref<Announcement[]>();
const announcementList = ref<Announcement[]>();
const versionLogs = ref<VersionLog[]>();
const versionLogList = ref<VersionLog[]>();

// 钩子函数：在组件挂载到DOM后被调用
onMounted(() => {
  document.title = "Aphrora Letters | 首页";

  getRecommendArticles();

  axiosHttp.get('/api/article/getAnnouncements').then(res => {
    announcements.value = res.data
  });
  axiosHttp.get('/api/article/getVersionLogs').then(res => {
    versionLogs.value = res.data
  });
});

// 获取推荐文章列表
const getRecommendArticles = async () => {
  axiosHttp.get('/api/article/getRecommendArticles').then(res => {
    recommendArticleInfos.value = res.data;
  });
};

// 发送异步请求并在按钮点击后获取全部公告消息
const getAnnouncementList = async () => {
  axiosHttp.get('/api/article/getAnnouncementList').then(res => {
    announcementList.value = res.data;
  });
};

const getVersionLogList = async () => {
  axiosHttp.get('/api/article/getVersionLogList').then(res => {
    versionLogList.value = res.data
  });
};

// 使用路由打开公告页面
const openAnnouncement = (announcementId: number) => {
  router.push({path: `/announcement/${announcementId}`, params: {'id': announcementId}});
};

</script>

<template>
  <!-- 导航栏组件 -->
  <NavbarComponent/>

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
              <p class="h6 text-center text-muted">
                密涅瓦的猫头鹰只有在夜幕降临的时候才开始飞翔。
              </p>
            </div>
          </div>
        </a-col>
      </a-row>

      <!-- 第二行 -->
      <a-row :gutter="16">
        <div class="container rounded">
          <a-row :gutter="16" style="display: flex;align-content: center;justify-content: center">
            <!-- 文章轮播图 -->
            <a-col :xs="24" :lg="17" class="mt-3">
              <a-card title="近日热文">
                <a-carousel autoplay>
                  <a :href="'/article/' + articleInfo.id"
                     v-for="articleInfo in recommendArticleInfos?.slice(0, 5)"
                     :key="articleInfo.id"
                     target="_blank"
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
              <a-card title="推荐文章">
                <a-list bordered :data-source="recommendArticleInfos">
                  <template #renderItem="{ item }">
                    <a-list-item :key="item.id">
                      <a :href="'/article/' + item.id" target="_blank"
                         style="text-decoration: none;font-size: larger;font-family: 楷体,serif;color: #8E354A">
                        {{ item.title }}
                      </a>
                    </a-list-item>
                  </template>
                </a-list>
              </a-card>
            </a-col>
          </a-row>
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
                <ul class="list-group list-group-flush">
                  <li class="list-group-item" v-for="announcement in announcements" style="cursor: pointer">
                    <a class="text-black text-decoration-none lead" target="_blank"
                       @click="openAnnouncement(announcement.id)">
                      {{ announcement.title }}
                    </a>
                    <span class="badge bg-secondary">发布时间：{{ announcement.time }}</span>
                  </li>
                </ul>
                <div class="text-end">
                  <button class="btn btn-sm btn-danger" data-bs-toggle="offcanvas" @click="getAnnouncementList"
                          data-bs-target="#announcementOffcanvas" aria-controls="offcanvasExample">
                    显示更多
                  </button>
                </div>
              </a-col>

              <!-- 版本日志 -->
              <a-col :xs="24" :lg="8">
                <h3 class="border-start border-warning text-danger"
                    style="font-family: 楷体,serif;font-weight: bold;">版本日志</h3>
                <a-timeline>
                  <a-timeline-item v-for="(versionLog, index) in versionLogs" :key="index">
                    <span class="badge bg-danger">{{ 'VERSION: ' + versionLog.version }}</span>
                    <div>
                      <span>{{ versionLog.log }}</span>
                      <span class="badge bg-secondary">{{ versionLog.time }}</span>
                    </div>
                  </a-timeline-item>
                </a-timeline>
                <div class="text-end">
                  <button class="btn btn-sm btn-danger" type="button" data-bs-toggle="offcanvas"
                          @click="getVersionLogList"
                          data-bs-target="#versionOffcanvas" aria-controls="offcanvasExample">
                    显示更多
                  </button>
                </div>
              </a-col>
            </a-row>

          </div>
        </div>
      </div>

      <!-- 公告栏消息的滑动侧边栏 -->
      <div class="offcanvas offcanvas-start" tabindex="-1" id="announcementOffcanvas"
           data-bs-scroll="true" aria-labelledby="offcanvasExampleLabel1">
        <div class="offcanvas-header">
          <h3 class="offcanvas-title border-start border-warning text-danger"
              id="offcanvasExampleLabel1" style="font-family: 楷体,serif;font-weight: bold;">最新消息</h3>
          <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body">
          <ul class="list-group list-group-flush">
            <li class="list-group-item" v-for="announcement in announcementList">
              <a class="text-black text-decoration-none lead" target="_blank"
                 @click="openAnnouncement(announcement.id)">
                {{ announcement.title }}
              </a>
              <span class="badge bg-secondary">发布时间：{{ announcement.time }}</span>
            </li>
          </ul>
        </div>
      </div>
      <!-- 公告栏版本日志的滑动侧边栏 -->
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
    <br>

  </div>

  <!-- 页脚 -->
  <Footer/>
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
  max-height: 72vh;
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
