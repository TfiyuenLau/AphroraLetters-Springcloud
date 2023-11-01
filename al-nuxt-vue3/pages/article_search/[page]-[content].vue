<script setup lang="ts">
import {ref, onMounted, onBeforeMount, reactive, computed} from 'vue';
import {searchArticleByContent} from "../../api/ArticleApi";
import type {ApiResult, Pagination} from "../../models/ApiResult";
import type {ArticleInfo} from "../../models/Article";

const router = useRouter();

// 获取当前路径参数：content、page
const route = useRoute();
const content = computed(() => String(route.params.content));
const page = computed(() => Number(route.params.page));

const articleList = ref<ArticleInfo[]>();

onBeforeMount(() => {
  // 获取搜索结果
  searchArticleByContent(content.value, pagination.current).then((res: ApiResult<Pagination<ArticleInfo>>) => {
    articleList.value = res.data.records;
    pagination.total = res.data.total;
    pagination.pages = res.data.pages;
  });
})

onMounted(() => {
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
    searchArticleByContent(content.value, pagination.current).then((res: ApiResult<Pagination<ArticleInfo>>) => {
      articleList.value = res.data.records;
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
  <navbar-component/>

  <a-skeleton :loading="!articleList" :active="true" :paragraph="{ rows: 64 }">
    <a-layout>
      <a-layout-content>
        <div class="container-fluid">
          <a-row :gutter="16">
            <a-col :xs="24" :lg="18">
              <!-- 文章搜索结果列表 -->
              <article-list-component
                  v-if="content"
                  :article-list="articleList"
                  :title="'字段搜索'"
                  :type="content"
              >
              </article-list-component>
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

            </a-col>

            <!-- 右侧悬浮列表组 -->
            <a-col :xs="24" :lg="6">
              <recommend-article-component :offsetTop="6"/>
            </a-col>

          </a-row>
        </div>
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
