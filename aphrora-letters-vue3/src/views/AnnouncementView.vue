<script setup lang="ts">
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
import RecommendArticleList from "@/components/RecommendArticleList.vue";

import axiosHttp from "@/axios.http"
import {ref, onMounted, computed} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {marked} from 'marked'

interface Announcement {
  id: number;
  publisher: string;
  title: string;
  content: string;
  createBy: string;
  isEffective: boolean;
  time: string;
}

// 获取当前路径参数
const route = useRoute();
let announcementId = computed(() => route.params.id);

const announcement = ref<Announcement | null>(null)
const getAnnouncementById = async () => {
  axiosHttp.get("/api/article/getAnnouncementById/" + announcementId.value).then(res => {
    announcement.value = res.data
    if (announcement.value != null) {
      announcement.value.content = marked.parse(announcement.value.content);
      document.title = announcement.value.title + ' | Aphrora Letters'
    } else {
      document.title = '公告栏 | Aphrora Letters'
    }
  }).catch(error => {
    console.log(error)
  })
}

onMounted(() => {
  getAnnouncementById()
})

</script>

<template>
  <Navbar/>
  <!-- 网站内容 -->
  <div class="container">

    <div class="row">
      <div class="col-lg-9">
        <!-- 主容器 -->
        <div class="container mt-3 rounded">
          <div class="card border-danger active">
            <div class="card-header bg-danger text-light bi bi-newspaper" style="font-weight: bold;word-spacing: 4px">
              公告栏 | 最新消息
            </div>
            <div class="card-body" v-if="announcement">
              <!-- 公告通用图片 -->
              <div>
                <img src="/img/格拉西莫夫-革命大本营.jpg" style="width: 100%;max-height: 256px;object-fit: cover;"
                     class="img-thumbnail" alt="公告通用图片">
              </div>
              <!-- 公告标题 -->
              <h3 class="text-center" style="font-weight: bold">
                {{ announcement.title }}
              </h3>
              <hr>
              <!-- 发布者与发布时间 -->
              <div class="text-center">
                <span class="text-center text-muted">
                    发布者：{{ announcement.publisher }}
                </span>
                <span class="text-center text-muted">
                  发布时间：{{ announcement.time }}
                </span>
              </div>
              <br>
              <!-- 公告正文——th:utext支持html语法解析 -->
              <div id="content" class="lead" v-html="announcement.content"></div>
            </div>

            <!-- 公告声明 -->
            <div class="card-footer text-end" style="font-weight: bold">素材来自网络，侵删。</div>
          </div>

        </div>
        <!-- 底部留白 -->
        <br>
        <br>
        <br>

      </div>

      <!-- 右侧悬浮列表组 -->
      <div class="col-lg-3 rightIsShow">
        <RecommendArticleList offsetTop="6"/>
      </div>

    </div>

  </div>
  <Footer/>
</template>

<style>
/* 公告消息正文css */
#content p {
  /*段落缩进2em*/
  text-indent: 2em;
}

#content img {
  /*设置图片居中显示*/
  display: block;
  margin: 0 auto;
  /*保持纵横比缩放图片,使图片的长边能完全显示出来*/
  width: 80%;
  object-fit: contain;
}

#content a {
  /*a标签文本颜色*/
  color: #8E354A;
  font-weight: bold;
  padding: 0px 2px;
  text-decoration: none;
}

#content blockquote {
  border-left: 4px solid #CB1B45;
  padding: 10px 0px 10px 15px;
  color: #34495e;
  background-color: rgba(245, 150, 170, .1);
}
</style>