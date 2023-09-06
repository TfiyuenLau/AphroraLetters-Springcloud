<script setup lang="ts">
import Navbar from "@/components/Navbar.vue";
import RecommendArticle from "@/components/RecommendArticleList.vue";
import ArticleList from "@/components/ArticleList.vue";
import axiosHttp from "@/axios.http";
import {ref, onMounted, reactive, computed} from 'vue';
import {useRouter, useRoute} from 'vue-router';
import RecommendArticleList from "@/components/RecommendArticleList.vue";

interface ArticleList {
  id: number;
  title: string;
  pictureUrl: string,
  summary: string;
  isTop: boolean;
  traffic: number;
  createBy: string;
  modifiedBy: string;
  categoryList?: string[];
  time: string;
}

const router = useRouter();

// 获取当前路径参数：content、page
const route = useRoute();
const content = computed(() => String(route.params.content));
const page = computed(() => Number(route.params.page));

const articleList = ref<ArticleList[]>();

onMounted(() => {
  axiosHttp.get("/api/article/searchArticleByContent?content=" + content.value + '&page=' + pagination.current).then(res => {
    articleList.value = res.data.records;
    pagination.total = res.data.total;
    pagination.pages = res.data.pages;
  }).catch(error => {
    console.log(error);
  })

  if (content.value) {
    document.title = `'${content.value}'搜索结果 | Aphrora Letters`;
  }
});

const pagination = reactive({
  total: 0,
  pages: 1,
  current: page.value,
  pageSize: 10,
  onChange: (page: number, pageSize: number) => { // 分页状态改变
    pagination.current = page; // 更新当前页为路由参数page
    router.push({
      path: `/article_search/${content.value}/${page}`,
      params: {'content': content.value, 'page': page},
    });

    // 请求新分页数据
    axiosHttp.get("/api/article/searchArticleByContent?content=" + content.value + '&page=' + pagination.current).then(res => {
      articleList.value = res.data.records;
    }).catch(error => {
      console.log('没有找到该页面：' + error);
    });

    document.documentElement.scrollTo(0, 0); // 回到顶部
  },
  showTotal: function (total: number, range: any) {
    if (range[0] == range[1])
      return `第${range[0]}条/总共${total}条`;
    else
      return `第${range[0]}-${range[1]}条/总共${total}条`;
  }
});

</script>

<template>
  <Navbar/>

  <!-- 网站内容 -->
  <div class="container" id="mobile_device">

    <div class="row">
      <div class="col-lg-9">
        <!-- 正文 -->
        <article-list
            v-if="content"
            :article-list="articleList"
            :title="'字段搜索'"
            :type="content"
        >
        </article-list>
        <br>
        <!-- 分页导航栏 -->
        <div class="pagination justify-content-end me-lg-3">
          <a-pagination
              :current="pagination.current"
              :pageSize="pagination.pageSize"
              :total="pagination.total"
              :showTotal="pagination.showTotal"
              @change="pagination.onChange"
          />
        </div>
        <br>
        <br>

      </div>

      <!-- 右侧悬浮列表组 -->
      <div class="col-lg-3 rightIsShow">
        <recommend-article-list offsetTop="6"/>
      </div>

    </div>

  </div>
</template>

<style scoped>
/* 取消移动端右划白边 */
.row {
  margin: 0;
}

/* 隐藏滚动条 */
.noScroll::-webkit-scrollbar {
  width: 0 !important;
  height: 0 !important;
}
</style>
