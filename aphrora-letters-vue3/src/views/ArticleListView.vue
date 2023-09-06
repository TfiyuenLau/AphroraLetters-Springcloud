<script setup lang="ts">
import Navbar from "@/components/Navbar.vue";
import RecommendArticle from "@/components/RecommendArticleList.vue";
import ArticleList from "@/components/ArticleList.vue";
import axiosHttp from "@/axios.http";
import {ref, onMounted, reactive, computed} from 'vue';
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

const articleList = ref<ArticleList[]>();

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
    articleList.value = res.data.records;
    pagination.total = res.data.total;
    pagination.pages = res.data.pages;
    // console.log(res)
  }).catch(error => {
    console.log(error);
  });
};

onMounted(() => {
  getArticleListByPage();

  window.addEventListener('popstate', getArticleListByPage); // 监听浏览器的后退和前进事件

  document.title = 'Aphrora Letters | 文章列表';
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
            :article-list="articleList"
            :title="'文章列表'"
            :type="'最近'"
        >
        </article-list>
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

      </div>

      <!-- 右侧悬浮列表组 -->
      <div class="col-lg-3 rightIsShow">
        <recommend-article offsetTop="6"/>
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
