<script setup lang="ts">
import {useRouter} from 'vue-router';
import {ref} from 'vue'

const router = useRouter();

// 搜索请求对象
const content = ref<string>('')

// 使用路由打开文章页面
const openArticleSearch = (content: string, page: number) => {
  let routeData = router.resolve({path: `/article_search/${content}/${page}`, params: {'content': content, 'page': page}});
  window.open(routeData.href, '_blank') // 打开新窗口
}
</script>

<template id="navbar">

  <!-- 导航栏 -->
  <nav class="navbar navbar-expand-lg bg-danger navbar-dark sticky-top">

    <div class="container-fluid">
      <!-- 设置Logo -->
      <a class="navbar-brand" href="#" style="font-weight: bold">
        <img src="/img/newlogo.png" alt="Logo" width="32" height="32" class="d-inline-block align-text-top">
        Aphrora Letters
      </a>

      <!-- 设置小屏按钮折叠导航栏 -->
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <!-- 后面的标签向右对齐 -->
        <div class="me-auto"></div>

        <ul class="navbar-nav" style="font-weight: bold">
          <li class="nav-item">
            <RouterLink to="/home" class="nav-link bi bi-house-door">首页</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/article_list/1" class="nav-link bi bi-newspaper">社论</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/library" class="nav-link bi bi-book">文库</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/assistance" class="nav-link bi bi-bookmark-plus">支持我们</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/about" class="nav-link bi bi-card-heading">关于</RouterLink>
          </li>
        </ul>

        <!-- 搜索功能表单 -->
        <form class="d-flex" method="get" @submit.prevent="openArticleSearch(content, 1)">
          <input v-model="content" class="form-control me-2" type="text" name="content_like" placeholder="Enter the words" required>
          <input class="" type="hidden" name="page" value="1">

          <button class="btn btn-dark bi bi-search" type="submit"></button>
        </form>

      </div>
    </div>

  </nav>

</template>

<style scoped>

</style>