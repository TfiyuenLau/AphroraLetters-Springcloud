<script setup lang="ts">
// 获取传递的参数
const {articleList, title, type} = defineProps(["articleList", "title", "type"]);

// 使用路由打开文章页面
const router = useRouter();
const openArticle = (articleId: number) => {
  router.push({path: `/article/${articleId}`});
};
</script>

<template>
  <div class="container-fluid mt-3 rounded">
    <a-skeleton :loading="!articleList" active :paragraph="{ rows: 64 }">
      <a-card :bodyStyle="{ padding: 0 }">
        <!-- 卡片标题 -->
        <template #title>
          <span class="bi bi-box-seam text-danger">{{ type }}-{{ title }}</span>
        </template>
        <!-- 迭代生成文章列表 -->
        <div v-for="article in articleList">
          <a-card
              class="list-group-item"
              :bodyStyle="{ padding: 1 }"
              style="height: 100%;width: 100%;overflow: hidden;"
          >
            <a-row :gutter="16">
              <!-- 文章题图 -->
              <a-col :xs="24" :lg="7" style="max-height: 256px;padding-right: 1;cursor: pointer;"
                   @click="openArticle(article.id)">
                <img style="height: 100%;width: 100%;object-fit: cover" :src="'/api/article/' + article.pictureUrl"
                     class="img-thumbnail img-fluid mx-auto d-block" alt="文章题图"/>
              </a-col>
              <!-- 文章标题与地址索引 -->
              <a-col :xs="24" :lg="17" style="position: relative;padding-left: 1;">
                <!-- 滚动条 -->
                <div class="noScroll" style="width: 100%;overflow-x: auto;">
                  <!-- 标题 -->
                  <div style="white-space: nowrap;">
                    <a class="btn btn-default btn-lg" style="padding-left: 0.2rem" @click="openArticle(article.id)">
                      <span class="h4">{{ article.title }}</span>
                    </a>
                    <span style="margin-left: 3px" class="badge bg-danger" v-for="category in article.categoryList">
                          {{ category.categoryName }}
                    </span>
                  </div>
                </div>
                <!-- 简介 -->
                <p class="h5 text-muted"
                   style="font-family: 等线,serif;text-indent:1em;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;text-overflow: ellipsis;overflow: hidden;">
                  {{ article.summary }}
                </p>
                <br>
                <!-- 发表时间 -->
                <p class="h6 text-end" style="position: absolute;bottom: 0;right: 6px;color: #BC9F77">
                  <span class="badge bg-secondary"> 发布于 {{ article.time }}</span>
                </p>
              </a-col>
            </a-row>
          </a-card>

          <a-divider></a-divider>
        </div>

      </a-card>
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
