<script setup lang="ts">
import {ref, onBeforeMount, onMounted, computed} from 'vue'
import {marked} from 'marked'
import {getAnnouncementById} from "../../api/AnnouncementApi";
import type {Announcement} from "../../models/Announcement";

// 获取当前路径参数
const route = useRoute();
let announcementId = computed(() => route.params.id);

const announcement = ref<Announcement>();

onBeforeMount(() => {
  getAnnouncementById(announcementId.value).then(res => {
    announcement.value = res.data
    if (announcement.value) {
      announcement.value.content = marked.parse(announcement.value.content);
      document.title = announcement.value.title + " | Aphrora Letters";
    } else {
      document.title = "公告栏 | Aphrora Letters";
    }
  });
})

onMounted(() => {

})

</script>

<template>
  <navbar-component/>
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
            <a-skeleton :loading="!announcement" active :paragraph="{ rows: 64 }">
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
                <!-- 公告正文 -->
                <div id="content" class="lead">
                  <span v-html="announcement.content"></span>
                </div>
              </div>
            </a-skeleton>

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
        <recommend-article-component :offsetTop="6"/>
      </div>

    </div>

  </div>
  <footer-component/>
</template>

<style lang="scss" scoped>
/* 公告消息正文css */
#content {
  :deep(p) {
    text-indent: 2em;
  }

  :deep(img) {
    /*设置图片居中显示*/
    display: block;
    margin: 0 auto;
    /*保持纵横比缩放图片,使图片的长边能完全显示出来*/
    width: 80%;
    object-fit: contain;
  }

  :deep(a) {
    color: #8E354A;
    font-weight: bold;
    padding: 0 2px;
    text-decoration: none;
  }

  :deep(blockquote) {
    border-left: 4px solid #CB1B45;
    padding: 10px 0 10px 15px;
    color: #34495e;
    background-color: rgba(245, 150, 170, .1);
  }
}
</style>
