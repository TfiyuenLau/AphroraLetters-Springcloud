<script setup lang="ts">
import {BarsOutlined} from '@ant-design/icons-vue'
import {ref, onMounted, onBeforeMount, computed, reactive, nextTick} from 'vue'
import {marked} from 'marked'
import '@/assets/css/markdown.css'
import type {Article, ArticleComment, ArticleInfo} from "../../models/Article";
import type {ApiResult} from "../../models/ApiResult";
import {getArticleById, getRecommendArticles, sendArticleComment} from "../../api/ArticleApi";

interface HeadingIndex {
  key: number;
  href: string;
  title: string;
  children?: HeadingIndex[];
}

const router = useRouter();

// 获取当前路径参数
const route = useRoute();
const articleId = computed(() => route.params.id);

const article = ref<Article>();
const headings = ref<HeadingIndex[]>([]);

const recommendArticleInfos = ref<ArticleInfo[]>();

/**
 * 调用API获取文章内容
 */
function handleArticle() {
  getArticleById(articleId.value).then((res: ApiResult<Article>) => {
    article.value = res.data;

    if (article?.value?.content) {
      // 解析md文本为html
      article.value.content = marked.parse(article.value.content);

      // 提取标题信息以生成目录索引
      let htmlContent = article.value.content;
      htmlContent.replace(/<h(\d) id="(.*?)">(.*?)<\/h\d>/g, (match, level, id, text): any => {
        const key = headings.value.length + 1;
        const href = `#${id}`;
        const title = text;

        const headingInfo: HeadingIndex = {key, href, title, children: []};

        if (level != "1" && headings.value.length > 0) {
          headings.value[headings.value.length - 1].children!.push(headingInfo);
        } else {
          headings.value.push(headingInfo);
        }
      });
      // console.log(headings.value);
    }
  });
}

onBeforeMount(() => {
  handleArticle();

  getRecommendArticles().then((res: ApiResult<ArticleInfo[]>) => {
    recommendArticleInfos.value = res.data;
  });
})

onMounted(() => {
  if (article?.value?.content) {
    document.title = article.value.title + " | Aphrora Letters"
  } else {
    document.title = "文章详情 | Aphrora Letters";
  }
})

// 定义评论表单数据
const commentData = reactive<ArticleComment>({
  id: null,
  articleId: Number(articleId.value),
  email: 'example@foxmail.com',
  comment: '',
  createBy: null,
  isEffective: true,
})

// 发送评论
const handleArticleComment = async (commentData: ArticleComment) => {
  sendArticleComment(commentData).then((res: ApiResult<any>) => {
    handleArticle();
    nextTick(() => { // 下一次DOM加载完毕后调用反馈
      openSuccessfulNotification(res.data, "评论发送成功");
    });
  });
}

// 打开文章标签分类列表页面
const openArticleCategory = (categoryId: number) => {
  // 新窗口打开
  // let routeData = router.resolve({path: `/article_category/1-${categoryId}`, params: {'id': categoryId}});
  // window.open(routeData.href);

  // 路由push当前窗口打开
  router.push({path: `/article_category/1-${categoryId}`, params: {'id': categoryId}});
}

</script>

<template>
  <navbar-component/>

  <!-- 网站内容 -->
  <a-skeleton :loading="!article" active :paragraph="{ rows: 128 }">
    <div class="container-fluid" v-if="article">
      <!-- 文章题图 -->
      <div class="mt-3">
        <a-image class="img-thumbnail img-fluid mx-auto d-block"
                 style="max-height: 512px;display: block;object-fit: cover;"
                 alt="文章题图" width="100%" :src="'/api/article/' + article.pictureUrl"/>
      </div>

      <a-row :gutter="16">
        <a-col :xs="24" :lg="18">
          <div id="bg" class="container mt-3 rounded">
            <!-- 正文 -->
            <div class="lead markdown" v-html="article.content"></div>
            <br>
            <br>

            <!-- 发表评论的表单 -->
            <div class="container bg-light rounded shadow p-4 mb-4 bg-white" id="success">
              <form @submit.prevent="handleArticleComment(commentData)" method="post">
                <input type="hidden" v-model="commentData.articleId" name="article_id">
                <div class="mb-3">
                  <label for="email" class="form-label"><strong>电子邮件</strong>：</label>
                  <input type="email" class="form-control" id="email" v-model="commentData.email"
                         placeholder="想匿名?试试我们的工作邮箱~(tfiyuenlau@foxmail.com)" name="email" required>
                </div>
                <div class="mb-3">
                  <label for="comment" class="form-label"><strong>消息内容</strong>：</label>
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
              <p class="h2 text-center text-danger bi bi-messenger"
                 style="font-family: 楷体,serif;color: #494D55;font-weight: bold;word-spacing: 8px">
                评 论 区
              </p>
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
        </a-col>

        <!-- 右侧悬浮列表组 -->
        <a-col :xs="24" :lg="6">
          <!-- 文章分类标签 -->
          <a-affix :offset-top="2">
            <div class="mt-3 rounded">
              <a-card class="list-group text-break">
                <template #title>
                  <span class="text-danger bi bi-tags">分类标签</span>
                </template>
                <div>
                  <span v-for="(category, index) in article.categoryList" :key="index">
                      <a class="btn btn-danger m-1" @click="openArticleCategory(category.id)"
                         v-if="index % 3 == 1">{{ category.categoryName }}</a>
                      <a class="btn border-warning text-warning m-1" @click="openArticleCategory(category.id)"
                         v-if="index % 3 == 2">{{ category.categoryName }}</a>
                      <a class="btn border-danger text-danger m-1" @click="openArticleCategory(category.id)"
                         v-if="index % 3 == 0">{{ category.categoryName }}</a>
                  </span>
                </div>
              </a-card>
            </div>
          </a-affix>

          <!-- 推荐文章 -->
          <a-affix :offset-top="172">
            <div class="mt-3 rounded">
              <a-skeleton :loading="!recommendArticleInfos" active :paragraph="{ rows: 16 }">
                <div class="overflow-list">
                  <a-card :bodyStyle="{ padding: 0 }" :bordered="true">
                    <!-- 卡片标题 -->
                    <template #title>
                      <span class="bi bi-globe text-danger">推荐文章</span>
                    </template>
                    <a-list :data-source="recommendArticleInfos" v-if="recommendArticleInfos" style="z-index: 2">
                      <template #renderItem="{ item }">
                        <a-list-item :key="item.id">
                          <a :href="'/article/' + item.id"
                             style="text-decoration: none;font-size: larger;font-family: 楷体,serif;color: #8E354A">
                            {{ item.title }}
                          </a>
                        </a-list-item>
                      </template>
                    </a-list>
                  </a-card>
                </div>
              </a-skeleton>
            </div>
          </a-affix>
        </a-col>
      </a-row>
    </div>

    <!-- 文章目录索引 -->
    <a-affix :style="{ position: 'fixed', bottom: '100px', right: '24px'}" style="z-index: 4">
      <a-popover title="目录索引" trigger="click" placement="topRight" :overlayStyle="{width: '256px'}">
        <template #content style="text-decoration: none;">
          <a-anchor :affix="true" :items="headings" :target-offset="64"></a-anchor>
        </template>
        <a-button type="primary" size="large" shape="default"
                  style="display: flex;flex-direction: row;justify-content: center;align-items: center;z-index: 4">
          <template #icon>
            <BarsOutlined style="font-size: large"/>
          </template>
        </a-button>
      </a-popover>
    </a-affix>

    <!-- 回到顶部 -->
    <a-back-top/>
  </a-skeleton>

  <footer-component/>
</template>

<style scoped>
.overflow-list :deep(.ant-list) {
  max-height: 435px !important;
  overflow: auto !important;
}
</style>
