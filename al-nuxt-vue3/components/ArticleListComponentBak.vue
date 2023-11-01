<script setup lang="ts">
// 获取传递的参数
const {articleList, title, type} = defineProps(["articleList", "title", "type"]);

// 使用路由打开文章页面
const openArticle = (articleId: number) => {
  window.open(`/article/${articleId}`, "_blank");
};
</script>

<template>
  <div class="container-fluid mt-3 rounded">
    <a-skeleton :loading="!articleList" active :paragraph="{ rows: 16 }">
      <ul class="list-group rounded">
        <!-- 列表表头 -->
        <div class="list-group-item list-group-item-action list-group-item-danger active bi bi-newspaper">
          <span>{{ type }}-{{ title }}</span>
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
    </a-skeleton>
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
