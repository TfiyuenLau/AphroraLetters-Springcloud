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
          <p class="h2 text-danger text-center" style="font-family: 楷体,serif;font-weight: bold;word-spacing: 8px">
            关 于 我 们
          </p>
          <hr class="text-danger">

          <div class="row mt-3 rounded">
            <img class="img-thumbnail img-fluid mx-auto d-block" src="/img/封面图.jpg" alt="导师">
            <p class="h6 text-center text-muted">
              图中从左到右依次为——马克思、马赫诺、列宁、毛泽东、阿尔都塞、齐泽克.
            </p>
          </div>

          <!-- 问题集合 -->
          <div class="mt-3">
            <div id="accordion">

              <div class="card">
                <div class="card-header">
                  <a class="btn" data-bs-toggle="collapse" href="#collapseOne">
                    一. Aphrora Letters社区为何开发？由谁开发？
                  </a>
                </div>
                <div id="collapseOne" class="collapse show" data-bs-parent="#accordion">
                  <div class="card-body lead" style="text-indent: 2em">
                    <p>
                      本文库是一个致力于推广左翼进步文化的平台，我们的目标是通过刊载哲学社科类社评文章、国内外左翼运动报导以及其他文体作品，向读者传递多元的观点和思想。我们收录了来自当今各主流学派的学术观点和经典著作，旨在为读者提供一个广阔的知识和思想交流的空间。
                    </p>
                    <p>
                      在我们的文库中，读者可以发现各种涉及哲学、社会科学、人文学科等领域的文章和评论。这些文章旨在深入探讨社会和政治问题、经济和阶级斗争、性别和民权等重要议题。我们希望通过这些内容，为读者提供批判性思考和理解社会现象的工具，促进社会进步和正义。
                    </p>
                    <p>
                      除了学术观点和社论文章，我们也鼓励投稿其他文体作品，例如小说、诗歌、散文等。我们相信文学艺术是传递情感和思想的重要媒介，通过文学的力量，我们可以更深刻地理解和表达社会的各个层面。
                    </p>
                    <p>
                      本文库的开发和维护由
                      <strong>@TfiyuenLau</strong>
                      负责，并将开源至GitHub，这意味着我们欢迎广大读者和社区成员的参与和贡献。我们希望通过开源的方式，吸引更多的人共同打造一个开放、包容和充满创造力的平台，为左翼进步文化的推广贡献力量。
                    </p>
                    <p style="text-indent: 0"><strong>项目开发文档：</strong>
                      <a href="https://www.cnblogs.com/tfiyuenlau/articles/16927271.html"
                         class="h5 text-danger" target="_blank">点我进入</a>
                    </p>
                    <p style="text-indent: 0"><strong>GitHub项目地址：</strong>
                      <a href="https://github.com/TfiyuenLau/AphroraLetters-Springcloud"
                         class="h5 text-danger" target="_blank">点我进入</a>
                    </p>
                  </div>
                </div>
              </div>

              <div class="card">
                <div class="card-header">
                  <a class="collapsed btn" data-bs-toggle="collapse" href="#collapseTwo">
                    二.投稿的文章应该符合哪些条件？如何投稿文章？
                  </a>
                </div>
                <div id="collapseTwo" class="collapse" data-bs-parent="#accordion">
                  <div class="card-body lead" style="text-indent: 2em">
                    <p>
                      为了保持本文库的内容质量和准确性，我们对投稿作品进行了一定的审核和筛选。我们希望投稿的作品能够涉及泛左翼（安那其、康米）相关的社会批判、文艺评论等主题，并在立场上保持先进性、开放性、创新性和革命性。
                    </p>
                    <p>
                      为了减少审核的工作量，我们网站暂未开放自主上传稿件后审核的模块。如果您有一篇合适的著作作品，希望向我们投稿，请直接发送稿件和摘要至
                      <strong>tfiyuenlau@foxmail.com。</strong>
                      我们将认真审核您的投稿，并在审核通过后将其发布在我们的博客文章和推荐板块上。
                    </p>
                    <p>
                      我们相信，通过精心筛选和审核的投稿作品，我们可以为读者提供高质量的内容，推动左翼进步思潮的传播和发展。如果您有任何问题或意见，也欢迎随时与我们联系。感谢您对本文库的支持和贡献！
                    </p>
                  </div>
                </div>
              </div>

              <div class="card">
                <div class="card-header">
                  <a class="collapsed btn" data-bs-toggle="collapse" href="#collapseThree">
                    三.社区由谁管理？如何成为管理员？
                  </a>
                </div>
                <div id="collapseThree" class="collapse" data-bs-parent="#accordion">
                  <div class="card-body lead" style="text-indent: 2em">
                    <p>
                      为了确保网站的正常运行和内容的管理，我们设立了一个后台管理面板，由特定的管理员通过账号和密码登录进行操作。后台管理面板提供了对各个模块的管理功能，包括对投稿文章的审核和发布、整理相关文献、管理游客的言论等。
                    </p>
                    <p>
                      管理员是网站的关键角色，他们负责处理投稿文章的审核和编辑，确保内容的质量和准确性。他们还会积极收集和整理与泛左翼相关的文献资料，为读者提供更全面的知识和信息。此外，管理员还负责管理和维护网站的日常运营，包括备份数据、优化网站性能等工作。
                    </p>
                    <p>
                      如果您对上述工作感兴趣，欢迎联系我们的管理员，邮箱为
                      <strong>tfiyuenlau@foxmail.com</strong>
                      。我们欢迎有才华和热情的个人加入我们的团队，共同推动本文库的发展和壮大。
                    </p>
                    <p style="text-indent: 0">
                      <strong>后台管理面板：</strong><a href="https://aphrora-letters.hk.cpolar.io/admin" class="h5 text-danger" target="_blank">点我进入</a>
                    </p>
                  </div>
                </div>
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
