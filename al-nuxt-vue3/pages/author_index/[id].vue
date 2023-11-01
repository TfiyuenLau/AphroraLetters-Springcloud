<script setup lang="ts">
import {ref, reactive, onBeforeMount, onMounted, computed} from 'vue'
import type {AuthorIndex, LibraryAuthor} from "../../models/Library";
import {getAuthorByAuthorId, getAuthorIndexListByAuthorId} from "../../api/LibraryApi";
import type {ApiResult} from "../../models/ApiResult";

const route = useRoute();
let authorId = computed(() => Number(route.params.id));

let libraryAuthor = reactive<LibraryAuthor>({
  id: 0,
  characterName: "",
  picUrl: "",
  introduction: "",
  isEffective: 1,
  authorIndices: [] as number[],
});
const authorIndexList = ref<AuthorIndex[]>();

const getAuthorInfo = async () => {
  getAuthorByAuthorId(authorId.value).then((res: ApiResult<LibraryAuthor>) => {
    Object.assign(libraryAuthor, res.data);
  });

  getAuthorIndexListByAuthorId(authorId.value).then((res: ApiResult<AuthorIndex[]>) => {
    authorIndexList.value = res.data;
    // console.log(authorIndexList.value);
  });
};

onBeforeMount(() => {
  getAuthorInfo();
});

onMounted(() => {

})

</script>

<template>
  <navbar-component/>
  <!-- 网站内容 -->
  <div class="container">

    <!-- 正文:背景透明 -->
    <div class="mt-3 rounded" id="bg">
      <div class="container">
        <!-- 卡片组 -->
        <a-row :gutter="16" v-if="libraryAuthor">
          <a-col :xs="24" :lg="6" class="mt-3">
            <a-card hoverable :bordered="true">
              <template #title>
                <div style="font-family: 华文新魏,serif;">
                  <span class="h4 text-danger bi bi-book">{{ libraryAuthor.characterName }}</span>
                </div>
              </template>
              <p class="lead">{{ libraryAuthor.introduction ? libraryAuthor.introduction : "" }}</p>
              <img class="img-fluid card-img-bottom rounded"
                   :src="'/api/library/' + libraryAuthor.picUrl"
                   alt="Card image"
              >
            </a-card>
          </a-col>

          <a-col :xs="24" :lg="18" class="mt-3 changeColum">
            <a-card hoverable>
              <p class="btn btn-danger">文集列表</p>
              <div v-for="(authorIndex, index) in authorIndexList">
                <a :href="'/literature/' + encodeURIComponent(authorIndex.pdfUrl) + '/'"
                   style="text-decoration-line: none;" target="_blank">
                  <p class="lead text-danger">{{ authorIndex.title }}</p>
                </a>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </div>

      <!-- 白框底部留白 -->
      <br>
      <br>
      <br>
    </div>
    <!-- 底部留白 -->
    <br>

  </div>
  <footer-component/>

</template>

<style scoped>

</style>
