<script setup lang="ts">
import Navbar from "@/components/Navbar.vue"
import RecommendArticle from "@/components/RecommendArticleList.vue";
import axiosHttp from "@/axios.http";
import {ref, onMounted, reactive, computed} from 'vue'
import {useRouter, useRoute} from 'vue-router';

interface ArticleList {
  id: number;
  title: string;
  pictureUrl: string,
  summary: string;
  isPassed: boolean;
  traffic: number;
  createBy: string;
  modifiedBy: string;
  categoryList: null | any[];
  time: string;
}

const router = useRouter();

// 获取当前路径参数
const route = useRoute();
const page = computed(() => Number(route.params.page));

const articleList = ref<ArticleList[] | null>(null);

// antdv pagination分页对象
const pagination = reactive({
  total: 0,
  pages: 1,
  current: page.value,
  pageSize: 10,
  onChange: (page: number, pageSize: number) => { // 分页状态改变
    pagination.current = page // 更新当前页为路由参数page
    router.push({path: `/article_list/${page}`, params: {'page': page}}) // 使用router.push方法打开指定路由的页面

    // 请求新分页数据
    getArticleListByPage()
    document.documentElement.scrollTo(0, 0) // 回到顶部
  },
  showTotal: function (total: number, range: any) {
    if (range[0] == range[1])
      return `第${range[0]}条/总共${total}条`;
    else
      return `第${range[0]}-${range[1]}条/总共${total}条`;
  }
});

// 获取文章分页数据
const getArticleListByPage = async () => {
  axiosHttp.get('/api/article/getArticleListByPage/' + pagination.current).then(res => {
    articleList.value = res.data.records
    pagination.total = res.data.total
    pagination.pages = res.data.pages
    // console.log(res)
  }).catch(error => {
    console.log(error)
  });
};

onMounted(() => {
  getArticleListByPage();

  window.addEventListener('popstate', getArticleListByPage); // 监听浏览器的后退和前进事件

  document.title = 'Aphrora Letters | 文章列表';
});

// 使用路由打开文章页面
const openArticle = (articleId: number) => {
  let routeData = router.resolve({path: `/article/${articleId}`, params: {'id': articleId}});
  window.open(routeData.href, '_blank'); // 打开新窗口
};

</script>

<template>
  <Navbar/>

  <!-- 网站内容 -->
  <div class="container" id="mobile_device">

    <div class="row">
      <div class="col-lg-9">
        <!-- 正文 -->
        <div class="container-fluid mt-3 rounded">
          <ul class="list-group rounded">
            <!-- 列表表头 -->
            <div class="list-group-item list-group-item-action list-group-item-danger active bi bi-newspaper">
              文章列表-第 {{ pagination.current }} 页
            </div>
            <!-- 迭代生成文章列表 -->
            <div>
              <li v-for="article in articleList" class="list-group-item"
                  style="height: 100%;width: 100%;overflow: hidden;padding: 0!important;">
                <div class="row">
                  <!-- 文章题图 -->
                  <div class="col-lg-3" style="max-height: 128px;padding-right: 0;cursor: pointer;"
                       @click="openArticle(article.id)">
                    <img style="height: 100%;width: 100%;object-fit: cover" :src="'/api/article/' + article.pictureUrl"
                         class="img-thumbnail img-fluid mx-auto d-block" alt="文章题图"/>
                  </div>
                  <!-- 文章标题与地址索引 -->
                  <div class="col-lg-9" style="position: relative;padding-left: 0;">
                    <!-- 滚动条 -->
                    <div class="noScroll" style="width: 100%;overflow-x: scroll;">
                      <!-- 标题 -->
                      <div style="white-space: nowrap;">
                        <a class="btn btn-default btn-lg" style="padding-left: 0.2rem" @click="openArticle(article.id)">
                          {{ article.title }}
                        </a>
                        <span style="margin-left: 3px" class="badge bg-danger" v-for="category in article.categoryList">
                          {{ category.categoryName }}</span>
                      </div>
                    </div>
                    <!-- 简介 -->
                    <p class="h6 text-muted"
                       style="font-family: 等线,serif;text-indent:1em;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;text-overflow: ellipsis;overflow: hidden;">
                      {{ article.summary }}
                    </p>
                    <br>
                    <!-- 发表时间 -->
                    <p class="h6 text-end" style="position: absolute;bottom: 0;right: 6px;color: #BC9F77">
                      发布时间：{{ article.time }}
                    </p>
                  </div>
                </div>
              </li>
            </div>
          </ul>
        </div>
        <br>

        <!-- 分页导航栏 -->
        <div class="pagination justify-content-end me-lg-3">
          <a-pagination v-model:current="pagination.current" v-model:pageSize="pagination.pageSize"
                        :total="pagination.total" :showTotal="pagination.showTotal" @change="pagination.onChange"/>
        </div>
        <br>
        <br>

      </div>

      <!-- 右侧悬浮列表组 -->
      <div class="col-lg-3 rightIsShow">
        <RecommendArticle offsetTop="6"/>
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

<script lang="ts">
//移动设备隐藏标签
let change = function () {
  let div = document.getElementById("mobile_device")
  if (document.documentElement.clientWidth < 768) {
    div!.className = ""
  }
};
window.addEventListener('onresize', change);
</script>