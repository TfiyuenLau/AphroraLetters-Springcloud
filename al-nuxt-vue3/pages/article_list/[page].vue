<script setup lang="ts">
import {ref, onBeforeMount, onMounted, reactive, computed} from 'vue';
import type {ArticleInfo} from "../../models/Article";
import type {ApiResult, Pagination} from "../../models/ApiResult";
import {getArticleListByPage} from "../../api/ArticleApi";

const router = useRouter();

// 获取当前路径参数
const route = useRoute();
const page = computed(() => Number(route.params.page));

const articleList = ref<ArticleInfo[]>();

// antdv pagination分页对象
const pagination = reactive({
  total: 0,
  pages: 1,
  current: page.value,
  pageSize: 10,
  onChange: (page: number, pageSize: number) => { // 分页状态改变
    pagination.current = page; // 更新当前页为路由参数page
    router.push({path: `/article_list/${page}`, params: {'page': page}}); // 使用router.push方法打开指定路由的页面

    // 请求新分页数据
    handleArticleListByPage();
    document.documentElement.scrollTo(0, 0); // 回到顶部
  },
  showTotal: function (total: number, range: any) {
    if (range[0] == range[1])
      return `第${range[0]}条/总共${total}条`;
    else
      return `第${range[0]}-${range[1]}条/共${total}条`;
  }
});

// 获取文章分页数据
const handleArticleListByPage = async () => {
  getArticleListByPage(pagination.current).then((res: ApiResult<Pagination<ArticleInfo>>) => {
    articleList.value = res.data.records;
    pagination.total = res.data.total;
    pagination.pages = res.data.pages;
    // console.log(res);
  });
};

onBeforeMount(() => {
  handleArticleListByPage();

  window.addEventListener("popstate", handleArticleListByPage); // 监听浏览器的后退和前进事件
});

onMounted(() => {
  document.title = "Aphrora Letters | 文章列表";
})

</script>

<template>
  <navbar-component/>

  <a-skeleton :loading="!articleList" :active="true" :paragraph="{ rows: 64 }">
    <a-layout>
      <a-layout-content>
        <div class="container-fluid">
          <a-row :gutter="16">
            <a-col :xs="24" :lg="18">
              <!-- 正文 -->
              <article-list-component
                  :article-list="articleList"
                  :title="'文章列表'"
                  :type="'最近'"
              >
              </article-list-component>
              <br>

              <!-- 分页导航栏 -->
              <div class="pagination justify-content-end me-lg-3">
                <a-pagination v-model:current="pagination.current"
                              v-model:pageSize="pagination.pageSize"
                              :total="pagination.total"
                              :showTotal="pagination.showTotal"
                              @change="pagination.onChange"
                />
              </div>
              <br>
              <br>

            </a-col>

            <!-- 右侧悬浮列表组 -->
            <a-col :xs="24" :lg="6">
              <recommend-article-component :offsetTop="6"/>
            </a-col>

          </a-row>

        </div>
        <br>
      </a-layout-content>

    </a-layout>
  </a-skeleton>
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
