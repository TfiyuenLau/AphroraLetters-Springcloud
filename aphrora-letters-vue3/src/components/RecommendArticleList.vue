<script setup lang="ts">
import axiosHttp from "@/axios.http";
import {ref, onMounted} from 'vue'

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

const recommendArticleInfos = ref<ArticleInfo[] | null>(null)

onMounted(() => {
  axiosHttp.get('/api/article/getRecommendArticles').then(res => {
    recommendArticleInfos.value = res.data
  })
})

// 为组件设置传递props
const props = defineProps(['offsetTop'])

</script>

<template>
  <!-- 图钉固定在顶部 -->
  <a-affix :offset-top="props.offsetTop">
    <div class="mt-3 rounded">
      <div class="list-group text-break" style="word-break: break-all;width: 100%">
        <div class="list-group-item list-group-item-action list-group-item-danger active bi bi-card-list">推荐文章</div>
        <div v-for="articleInfo in recommendArticleInfos">
          <a :href="'/article/' + articleInfo.id" class="list-group-item list-group-item-action">
            {{ articleInfo.title }}
          </a>
        </div>
      </div>
    </div>
  </a-affix>
</template>

<style scoped>

</style>