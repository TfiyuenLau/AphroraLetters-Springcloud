<script setup lang="ts">
import {ref, onBeforeMount} from 'vue'
import type {LibraryAuthor} from "../models/Library";
import {getAuthorList} from "../api/LibraryApi";

let router = useRouter();

const libraryAuthorList = ref<LibraryAuthor[]>();

onBeforeMount(() => {
  // 获取文库作者列表
  getAuthorList().then(res => {
    libraryAuthorList.value = res.data;
    document.title = "文库总览 | Aphrora Letters";
  });
})

// 打开作者对应的文章索引界面
const openAuthorIndex = (authorId: number) => {
  router.push({
    path: `/author_index/${authorId}`,
    params: {
      'id': authorId
    }
  });
};

</script>

<template>
  <navbar-component/>

  <div class="container">
    <a-card id="bg" class="mt-3 rounded">
      <a-skeleton :loading="!libraryAuthorList" :paragraph="{ rows: 16 }" active>
        <!-- 卡片组 -->
        <a-row :gutter="8" class="row" v-if="libraryAuthorList">
          <a-col :xs="12" :lg="6" class="mt-3 " v-for="(libraryAuthor, index) in libraryAuthorList">
            <div v-if="index % 2 == 1">
              <a-card hoverable>
                <template #title>
                  <div style="font-family: 华文新魏,serif;">
                    <span class="h4 text-danger bi bi-bookmark">{{ libraryAuthor.characterName }}</span>
                  </div>
                </template>
                <img class="img-fluid card-img-top rounded"
                     :src="'/api/library/' + libraryAuthor.picUrl"
                     alt="Card image">
                <p class="card-text lead">{{ libraryAuthor.introduction }}</p>
                <button class="btn btn-danger" @click="openAuthorIndex(libraryAuthor.id)">查看文集</button>
              </a-card>
            </div>

            <div v-if="index % 2 == 0">
              <a-card hoverable>
                <template #title>
                  <div style="font-family: 华文新魏,serif;">
                    <span class="h4 text-danger bi bi-book">{{ libraryAuthor.characterName }}</span>
                  </div>
                </template>
                <p class="card-text lead">{{ libraryAuthor.introduction }}</p>
                <button class="btn btn-danger" @click="openAuthorIndex(libraryAuthor.id)">查看文集</button>
                <img class="img-fluid card-img-bottom rounded mt-3"
                     :src="'/api/library/' + libraryAuthor.picUrl"
                     alt="Card image">
              </a-card>
            </div>
          </a-col>
        </a-row>
      </a-skeleton>
    </a-card>

    <!-- 白框底部留白 -->
    <br>
  </div>
  <!-- 底部留白 -->
  <br>

  <footer-component/>

</template>

<style scoped>

</style>
