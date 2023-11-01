<script setup lang="ts">
import {ref, onBeforeMount, reactive, computed} from 'vue';
import type {ArticleInfo, Category} from "../../models/Article";
import {getArticleCategoryById, getArticleListByCategoryIdAndPage} from "../../api/ArticleApi";
import type {ApiResult, Pagination} from "../../models/ApiResult";

const router = useRouter();

// 获取当前路径参数：categoryId、page
const route = useRoute();
const categoryId = computed(() => Number(route.params.categoryId));
const page = computed(() => Number(route.params.page));

const category = ref<Category>();
const articleList = ref<ArticleInfo[]>();

onBeforeMount(() => {
  // 获取当前分类标签的标签信息
  getArticleCategoryById(categoryId.value).then((res: ApiResult<Category>) => {
    category.value = res.data;

    if (category.value) {
      document.title = `${category.value.categoryName} | Aphrora Letters`;
    }
  })

  // 封装标签文章列表和分页数据
  getArticleListByCategoryIdAndPage(pagination.current, categoryId.value).then((res: ApiResult<Pagination<ArticleInfo>>) => {
    articleList.value = res.data.records;
    pagination.total = res.data.total;
    pagination.pages = res.data.pages;
  });
});

// 分页响应式对象
const pagination = reactive({
  total: 0,
  pages: 1,
  current: page.value,
  pageSize: 10,
  onChange: (page: number, pageSize: number) => { // 分页状态改变
    pagination.current = page // 更新当前页为路由参数page
    router.push({
      path: `/article_category/${categoryId.value}/${page}`,
      params: {'categoryId': categoryId.value, 'page': page},
    });

    // 请求新分页数据
    getArticleListByCategoryIdAndPage(pagination.current, categoryId.value).then((res: ApiResult<Pagination<ArticleInfo>>) => {
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
              <!-- 文章列表 -->
              <article-list-component
                  v-if="category"
                  :article-list="articleList"
                  :title="'标签分类'"
                  :type="category.categoryName"
              >
              </article-list-component>
              <br>

              <!-- 分页导航栏 -->
              <div class="pagination justify-content-end me-lg-3">
                <a-pagination
                    v-model:current="pagination.current"
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
