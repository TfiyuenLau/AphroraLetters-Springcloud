<script setup lang="ts">
import '@/assets/index.css'
import NavbarComponent from '@/components/Navbar.vue'
import Footer from "@/components/Footer.vue";
import axiosHttp from "@/axios.http";
import {ref, onMounted} from 'vue';
import router from "@/router";

interface ArticleInfo {
  id: number;
  title: string;
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
const recommendArticleInfos = ref<ArticleInfo[] | null>(null)
const announcements = ref<Announcement[] | null>(null)
const announcementList = ref<Announcement[] | null>(null)
const versionLogs = ref<VersionLog[] | null>(null)
const versionLogList = ref<VersionLog[] | null>(null)

// 钩子函数：在组件挂载到DOM后被调用
onMounted(() => {
  axiosHttp.get('/api/article/getRecommendArticles').then(res => {
    recommendArticleInfos.value = res.data
  })
  axiosHttp.get('/api/article/getAnnouncements').then(res => {
    announcements.value = res.data
  })
  axiosHttp.get('/api/article/getVersionLogs').then(res => {
    versionLogs.value = res.data
  })

  document.title = 'Aphrora Letters | 首页'
});

// 发送异步请求并在按钮点击后获取全部公告消息
const getAnnouncementList = async () => {
  axiosHttp.get('/api/article/getAnnouncementList').then(res => {
    announcementList.value = res.data
  })
}

const getVersionLogList = async () => {
  axiosHttp.get('/api/article/getVersionLogList').then(res => {
    versionLogList.value = res.data
  })
}

// 使用路由打开公告页面
const openAnnouncement = (announcementId: number) => {
  router.push({path: `/announcement/${announcementId}`, params: {'id': announcementId}});
}

</script>

<template>
  <!-- 导航栏组件 -->
  <NavbarComponent/>

  <!-- 网站首页内容 -->
  <div class="container">

    <!-- 正文:背景透明 -->
    <div class="mt-3 rounded p-3" id="bg">

      <!-- 第一行 -->
      <div class="row">
        <div class="col-12">
          <div class="container mt-3 rounded">
            <!-- Jumbotron -->
            <div class="row mt-3 p-5 bg-danger text-white rounded">
              <h1>欢迎访问 Aphrora Letters ！</h1>
              <p>
                本文库致力于<strong>左翼进步文化</strong>的推广，刊载哲学社科类社评文章、国内外左翼运动报导与其他文体；收录有当今各主流学派的学术观点与，<strong>经典著作</strong>。
              </p>
              <p>
                注：本项目处于<strong>测试阶段</strong>，WEB应用内部的Logo、图片素材和部分转载文章内容等均来源于网络，<strong>仅用于个人学习用途</strong>，若侵犯了您的著作权请即使联系
                <strong>tfiyuenlau@foxmail.com</strong>
                进行删除。网站仍在测试阶段，未部署域名解析与Https协议；网站积极依照《网络安全法》《网络安全管理条例》相关法律条例进行运营，若存在黄赌毒、政治敏感言论请向网站管理员举报，谢谢配合。
              </p>
            </div>

            <div class="row mt-3 rounded">
              <img class="img-thumbnail img-fluid mx-auto d-block" src="/img/封面图.jpg" alt="导师">
              <p class="h6 text-center text-muted">
                图中从左到右依次为——马克思、马赫诺、列宁、毛泽东、阿尔都塞、齐泽克.</p>
            </div>

          </div>
        </div>
      </div>

      <!-- 第二行 -->
      <div class="row">
        <div class="container mt-3 rounded">
          <div class="row">

            <!-- 正文内容 -->
            <div class="lead col-lg-9 mt-3" style="text-indent: 2em">
              <p>
                左翼一词一说起源于法国大革命时期：革命家将象征封建制度的、代表落后生产力而阻挠资产阶级民主的皇帝及其保皇派簇拥们称为<strong>右派（或右翼、反动派）</strong>，将显出勃勃生机并羽翼渐丰满的资产阶级革命者称之为<strong>左派（左翼）</strong>。在其百年后的近代，这些词的内涵并未改变，右翼仍代表着统治阶级的<strong>专权与压迫</strong>、<strong>狭隘的民族视角</strong>、对已然<strong>腐化之现状</strong>的捍卫；左翼则以实践争取着底层即多数人的最大利益，探讨与找寻<strong>社会公平的实现方式</strong>，以温和改革或激进革命的方式对暴露弊病的现实进行<strong>否定</strong>。
              </p>
              <p>
                然而，恪守左翼进步主义的知识分子早已不是<strong>资产阶级</strong>与<strong>威权建制派</strong>们。他们或是<strong>享受着资本扩张</strong>的愉悦，以传播<strong>消费主义</strong>与占据<strong>房地产及其附属金融产业（房产抵押贷款）</strong>近乎法西斯式地控制着普罗大众（The
                Proletariat）的生产与消费行为；或是<strong>背弃</strong>无产阶级，官商勾结，无视父辈们为劳动者们制定的法律，以已然蜕变为技术官僚统治工具的行政权力<strong>为己谋私利</strong>。左翼的大旗则落到了吸收有马克思主义思想内核、与继承了安那其主义反叛传统的思想流派。
              </p>
              <p>
                事实上，我们都知道，左翼各流派完全不是一团和气的。至少在当今网络环境上，<strong>原初马克思主义、安那其主义、列宁主义、工团主义、马赫诺主义、斯派、托派、毛主义、西马及法兰克福学派、社民、民社</strong>相互“开除左籍”、互扣帽子，这样的<strong>路线之争造成了理论混乱</strong>，“同僚”们在斗争空耗斗志。我们需得在<strong>进步之大旗</strong>下集结，结成统一战线，在哲学理论斗争的相互诘难中共同趋向成熟，在实践斗争中互济互助。
              </p>
            </div>

            <!-- 文章推荐 -->
            <div class="col-lg-3">
              <div class="mt-3 rounded">
                <div class="list-group text-break" style="width: 100%">
                  <div class="list-group-item list-group-item-action list-group-item-danger active bi bi-card-list">
                    推荐文章
                  </div>
                  <div v-for="articleInfo in recommendArticleInfos">
                    <a :href="'/article/' + articleInfo.id" class="list-group-item list-group-item-action">
                      {{ articleInfo.title }}
                    </a>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>

      <!-- 第三行 -->
      <div class="container row">
        <div class="card border-warning shadow mt-3">
          <div class="card-body">
            <div class="row">
              <div class="col-12">
                <div style="float: left;">
                  <img class="img-fluid" style="max-width: 48px" src="/img/公告栏.png" alt="公告栏">
                </div>
                <div class="h1 text-center"
                     style="font-family: 楷体,serif;color: #494D55;font-weight: bold;word-spacing: 8px">
                  公 告 栏
                </div>
              </div>
            </div>
            <hr>
            <div class="row">
              <!-- 最新消息 -->
              <div class="col-12 col-lg-8">
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
              </div>

              <!-- 版本日志 -->
              <div class="col-12 col-lg-4">
                <h3 class="border-start border-warning text-danger"
                    style="font-family: 楷体,serif;font-weight: bold;">版本日志</h3>
                <a-timeline>
                  <a-timeline-item v-for="(versionLog, index) in versionLogs" :key="index">
                    <span class="badge bg-danger">{{ versionLog.version }}</span>
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
              </div>
            </div>

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
              <span class="badge bg-danger">{{ versionLog.version }}</span>
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

<!-- 取消bootstrap边距，解决移动端右划白边 -->
<style scoped>
/**/
.row {
  margin: 0;
}
</style>
