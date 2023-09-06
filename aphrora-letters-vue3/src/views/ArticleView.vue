<script setup lang="ts">
import Navbar from "@/components/Navbar.vue"
import Footer from "@/components/Footer.vue"
import RecommendArticleList from "@/components/RecommendArticleList.vue"

import {notification} from 'ant-design-vue'
import {SmileOutlined, FrownOutlined, BarsOutlined} from '@ant-design/icons-vue'
import axiosHttp from "@/axios.http"
import {ref, onMounted, computed, reactive, h, nextTick} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {marked} from 'marked'
import '@/assets/markdown.css'

interface Article {
  id: number;
  title: string;
  pictureUrl: string;
  summary: string;
  content: string;
  createBy: string;
  modifiedBy: string;
  categoryList: Category[];
  articleCommentList: ArticleComment[];
  time: string;
}

interface Category {
  id: number;
  categoryName: string;
  createBy: string | null;
  modifiedBy: string | null;
  articleInfoList: Article[] | null;
  articleCategoryList: Category[] | null;
}

interface ArticleComment {
  id: number | null;
  articleId: number;
  email: string;
  comment: string;
  createBy: string | null;
  isEffective: boolean;
}

const router = useRouter()

// 获取当前路径参数
const route = useRoute();
const articleId = computed(() => route.params.id)

const article = ref<Article | null>(null)

// 获取文章信息
const getArticleById = async () => {
  axiosHttp.get('/api/article/getArticleById/' + articleId.value).then(res => {
    article.value = res.data

    if (null != article?.value?.content) {
      article.value.content = marked.parse(article.value.content) // 解析md文本为html
      document.title = article.value.title + ' | Aphrora Letters'
    } else {
      document.title = '文章详情 | Aphrora Letters'
    }
  }).catch(error => {
    console.log(error)
  })
}

// DOM加载完毕调用
onMounted(() => {
  getArticleById()
})

// 定义评论表单数据
const commentData = reactive<ArticleComment>({
  id: null,
  articleId: Number(articleId.value),
  email: '',
  comment: '',
  createBy: null,
  isEffective: true,
})

// 发送评论
const sendArticleComment = async () => {
  axiosHttp.post('/api/article/insertArticleComment', commentData).then(() => {
    getArticleById()
    nextTick(() => { // 下一次DOM加载完毕后调用反馈
      openSuccessfulNotification()
    })
  }).catch(error => {
    openErrorNotification()
    console.log(error)
  })
}

// 评论发送成功
const openSuccessfulNotification = () => {
  notification.open({
    message: '新通知',
    description:
        '评论已发送',
    duration: 3,
    icon: () => h(SmileOutlined, {style: 'color: #198754'}),
    top: '64px',
  })
}

// 评论发送失败
const openErrorNotification = () => {
  notification.open({
    message: '新通知',
    description:
        '评论发送失败！请重试...',
    duration: 3,
    icon: () => h(FrownOutlined, {style: 'color: #dc3545'}),
    top: '64px',
  })
}

// 打开文章标签分类列表页面
const openArticleCategory = (categoryId: number) => {
  let routeData = router.resolve({path: `/article_category/${categoryId}/1`, params: {'id': categoryId}});
  window.open(routeData.href, '_blank')
}

</script>

<template>
  <Navbar/>
  <!-- 网站内容 -->
  <div class="container" v-if="article != null">
    <!-- 文章题图 -->
    <div class="mt-3">
      <a-image class="img-thumbnail img-fluid mx-auto d-block"
               style="max-height: 320px;display: block;object-fit: cover;"
               alt="文章题图" width="100%" :src="'/api/article/' + article.pictureUrl"/>
    </div>

    <div class="row">
      <div class="col-lg-9">
        <div id="bg" class="container mt-3 rounded" v-if="true">
          <!-- 正文 -->
          <div class="markdown" v-html="article.content"></div>
          <br>
          <br>

          <!-- 发表评论的表单 -->
          <div class="container bg-light rounded shadow p-4 mb-4 bg-white" id="success">
            <form @submit.prevent="sendArticleComment" method="post">
              <input type="hidden" v-model="commentData.articleId" name="article_id">
              <div class="mb-3">
                <label for="email" class="form-label"><strong>电子邮件</strong>：</label>
                <input type="email" class="form-control" id="email" v-model="commentData.email"
                       placeholder="想匿名?试试我们的工作邮箱~(tfiquenlau@foxmail.com)" name="email" required>
              </div>
              <div class="mb-3">
                <label for="comment" class="form-label"><strong>评论</strong>：</label>
                <textarea class="form-control" rows="5" id="comment" name="comment" v-model="commentData.comment"
                          placeholder="请友好交流哦~" maxlength="512 required"></textarea>
              </div>
              <button id="send" type="submit" class="btn btn-danger">发送评论</button>
            </form>

          </div>
          <br>
          <br>
          <br>
          <!-- 评论区部分 -->
          <div class="container bg-light rounded shadow p-4 mb-4 bg-white">
            <p class="h3 text-center bi bi-messenger"
               style="font-family: 楷体,serif;color: #494D55;font-weight: bold;word-spacing: 8px">评 论 区</p>
            <hr>
            <div v-if="article.articleCommentList">
              <a-list class="comment-list" :header="`共 ${article.articleCommentList.length} 条评论`"
                      item-layout="horizontal" :data-source="article.articleCommentList">
                <!-- 评论主体列表 -->
                <template #renderItem="{ item : articleComment }">
                  <a-list-item>
                    <a-comment :author="articleComment.email"
                               :avatar="'/img/portrait/' + articleComment.id % 17 + '.jpg'">
                      <template #actions>
                        <span>回复</span>
                      </template>
                      <template #content>
                        <p>{{ articleComment.comment }}</p>
                      </template>
                      <template #datetime>
                        <a-tooltip :title="articleComment.createBy">
                          <span>{{ articleComment.createBy }}</span>
                        </a-tooltip>
                      </template>
                    </a-comment>
                  </a-list-item>
                </template>

              </a-list>
            </div>
          </div>
          <br>

        </div>

      </div>

      <!-- 右侧悬浮列表组 -->
      <div class="col-lg-3">
        <!-- 文章分类标签 -->
        <a-affix :offset-top="60">
          <div class="mt-3 rounded">
            <div class="list-group text-break">
              <div class="list-group-item list-group-item-action list-group-item-danger active bi bi-tags">
                分类标签
              </div>
              <div class="card rounded-0">
                <div class="card-body">
                            <span v-for="(category, index) in article.categoryList" :key="index">
                                <a class="btn btn-danger m-1" @click="openArticleCategory(category.id)"
                                   v-if="index % 3 == 1">{{ category.categoryName }}</a>
                                <a class="btn border-warning text-warning m-1" @click="openArticleCategory(category.id)"
                                   v-if="index % 3 == 2">{{ category.categoryName }}</a>
                                <a class="btn border-danger text-danger m-1" @click="openArticleCategory(category.id)"
                                   v-if="index % 3 == 0">{{ category.categoryName }}</a>
                            </span>
                </div>
              </div>
            </div>
          </div>
        </a-affix>

        <!-- 推荐文章 -->
        <RecommendArticleList offsetTop="20"/>

      </div>
    </div>
  </div>

  <!-- 文章目录索引 -->
  <a-float-button shape="square" type="primary" :style="{ right: '24px', bottom: '100px' }" data-bs-toggle="collapse" data-bs-target="#indexCollapse"
                  aria-expanded="false" aria-controls="collapseExample">
    <template #icon>
      <BarsOutlined/>
    </template>
  </a-float-button>

  <!-- 回到顶部 -->
  <a-back-top/>

  <Footer/>
</template>

<style scoped>

</style>