<script setup lang="ts">
import {ref, onBeforeMount} from 'vue'
import {getRecommendArticles} from "../api/ArticleApi";
import type {ApiResult} from "../models/ApiResult";
import type {ArticleInfo} from "../models/Article";

// 为组件设置传递props
const props = defineProps(['offsetTop'])

const recommendArticleInfos = ref<ArticleInfo[]>();

onBeforeMount(() => {
  getRecommendArticles().then((res: ApiResult<ArticleInfo[]>) => {
    recommendArticleInfos.value = res.data;
  });
})

</script>

<template>

  <!-- 图钉固定在顶部 -->
  <a-affix :offset-top="props.offsetTop">
    <div class="mt-3 rounded">
      <a-skeleton :loading="!recommendArticleInfos" active :paragraph="{ rows: 16 }">
        <a-card :bodyStyle="{ padding: 0 }" :bordered="true">
          <!-- 卡片标题 -->
          <template #title>
            <span class="bi bi-globe text-danger">推荐文章</span>
          </template>
          <a-list :data-source="recommendArticleInfos" v-if="recommendArticleInfos">
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
      </a-skeleton>
    </div>
  </a-affix>

</template>

<style scoped>

</style>
