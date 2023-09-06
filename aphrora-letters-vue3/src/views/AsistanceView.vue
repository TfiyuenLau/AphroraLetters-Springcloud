<script setup lang="ts">

import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
import RecommendArticleList from "@/components/RecommendArticleList.vue";

document.title = '支持我们 | Aphrora Letters'
</script>

<template>
  <Navbar/>
  <!-- 网站内容 -->
  <div class="container">

    <div class="row">
      <div class="col-lg-9">
        <!-- 正文 -->
        <div class="container mt-3 rounded" id="bg">
          <br>
          <p class="h3 text-danger text-center">THANKS FOR YOUR HELP!</p>
          <hr class="text-danger">

          <div class="row">
            <div class="col-12 col-lg-4">
              <img src="/img/20221030141215.jpg" class="img-fluid img-thumbnail rounded" alt="捐赠">
            </div>
            <div class="col-12 col-lg-8">
              <div class="card">
                <div class="card-header text-danger">支持社区的工作</div>
                <div class="card-body lead" style="text-indent: 2em">
                  <p>
                    非常感谢您对我们网站的支持和认可。您的每一次点击和阅读都是对我们开发和维护工作的鼓励。
                  </p>
                  <p>
                    事实上，我们网站急需具备左翼流派哲学专业知识的作者和对文库进行归档和收集工作的管理员。如果您对此感兴趣，我们非常欢迎您与我们联系：
                    <strong>tfiquenlau@foxmail.com</strong>。
                  </p>
                  <p>
                    此外，为了确保网站的正常运行，服务器的运维也需要一些物质条件和费用支持。因此，我们欢迎任何形式的捐助来支持本社区的发展。如果您有兴趣进行捐助，请通过邮箱联系我们：
                    <strong>tfiquenlau@foxmail.com</strong>。
                  </p>
                  <p>
                    再次感谢您对我们网站的关注和支持。我们期待与更多热心的作者和捐助者合作，共同推动文库的发展。如果您有任何疑问或建议，也请随时与我们联系。
                  </p>
                </div>
                <div class="card-footer text-muted text-end">——网站管理员@TfieyuenLau</div>
              </div>
            </div>
          </div>

          <br>
          <br>
          <br>
        </div>
        <!-- 底部留白 -->
        <br>
        <br>
        <br>

      </div>

      <!-- 右侧悬浮列表组 -->
      <div class="col-lg-3 rightIsShow">
        <RecommendArticleList offsetTop="6"/>
      </div>

    </div>
  </div>

  <Footer/>
</template>

<style scoped>

</style>