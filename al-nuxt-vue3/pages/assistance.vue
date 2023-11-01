<script setup lang="ts">
import { onBeforeMount } from 'vue';

onBeforeMount(() => {
  document.title = '支持我们 | Aphrora Letters';
})
</script>

<template>
  <navbar-component/>
  <!-- 网站内容 -->
  <div class="container">

    <a-row :gutter="32">
      <a-col :xs="24" :lg="18">
        <!-- 正文 -->
        <div class="container mt-3 rounded" id="bg">
          <br>
          <p class="h3 text-danger text-center">THANKS FOR YOUR HELP!</p>
          <hr class="text-danger">

          <a-row :gutter="8">
            <a-col :xs="24" :lg="8">
              <img src="/img/20221030141215.jpg" class="img-fluid img-thumbnail rounded" alt="捐赠">
            </a-col>
            <a-col :xs="24" :lg="16">
              <div class="card">
                <div class="card-header h6">支持社区的工作</div>
                <div class="card-body lead" style="text-indent: 2em">
                  <p>
                    非常感谢您对我们网站的支持和认可。您的每一次点击和阅读都是对我们开发和维护工作的鼓励。
                  </p>
                  <p>
                    事实上，我们网站急需具备左翼流派哲学专业知识的作者和对文库进行归档和收集工作的管理员。如果您对此感兴趣，我们非常欢迎您与我们联系：
                    <strong>tfiyuenlau@foxmail.com</strong>。
                  </p>
                  <p>
                    此外，为了确保网站的正常运行，服务器的运维也需要一些物质条件和费用支持。因此，我们欢迎任何形式的捐助来支持本社区的发展。如果您有兴趣进行捐助，请通过邮箱联系我们：
                    <strong>tfiyuenlau@foxmail.com</strong>。
                  </p>
                  <p>
                    再次感谢您对我们网站的关注和支持。我们期待与更多热心的作者和捐助者合作，共同推动文库的发展。如果您有任何疑问或建议，也请随时与我们联系。
                  </p>
                </div>
                <div class="card-footer lead text-muted text-end">——开发者@TfiyuenLau</div>
              </div>
            </a-col>
          </a-row>
          <br>
          <br>
          <br>

        </div>
        <!-- 底部留白 -->
        <br>
        <br>
        <br>

      </a-col>

      <!-- 右侧悬浮列表组 -->
      <a-col :xs="24" :lg="6">
        <recommend-article-component :offsetTop="6"/>
      </a-col>

    </a-row>
  </div>

  <footer-component/>
</template>

<style scoped>

</style>
