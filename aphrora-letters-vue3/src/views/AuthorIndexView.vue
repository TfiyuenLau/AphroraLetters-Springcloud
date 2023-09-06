<script setup lang="ts">
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
import axiosHttp from "@/axios.http";
import {ref, reactive, onMounted, computed} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import router from "@/router";

interface LibraryAuthor {
  id: number;
  characterName: string;
  picUrl: string;
  introduction: string;
  isEffective: number;
  authorIndices: number[];
}

interface AuthorIndex {
  articleId: number;
  title: string;
  pdfUrl: string;
  authorId: number;
  createBy: string;
  isEffective: boolean;
  libraryAuthor: LibraryAuthor | string;
}

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
  await axiosHttp.get('/api/library/getAuthorByAuthorId/' + authorId.value).then(res => {
    libraryAuthor = {...res.data};
    document.title = libraryAuthor!.characterName + ' | Aphrora Letters';
  }).catch(error => {
    console.log(error);
  });

  await axiosHttp.get('/api/library/getAuthorIndexListByAuthorId/' + authorId.value).then(res => {
    authorIndexList.value = res.data;
    // console.log(authorIndexList.value);
  }).catch(error => {
    console.log(error);
  });
};

onMounted(() => {
  getAuthorInfo();
});

</script>

<template>
  <Navbar/>
  <!-- 网站内容 -->
  <div class="container">

    <!-- 正文:背景透明 -->
    <div class="mt-3 rounded" id="bg">
      <div class="container">
        <!-- 卡片组 -->
        <div class="row" v-if="libraryAuthor">

          <div class="col-3 mt-3 changeColum">
            <div class="card" style="max-width: 300px">
              <div class="card-body">
                <h4 class="card-title text-danger bi bi-book" style="font-family: 华文新魏,serif;">
                  {{ libraryAuthor.characterName }}
                </h4>
                <p class="card-text lead">{{ libraryAuthor.introduction ? libraryAuthor.introduction : "" }}</p>
              </div>
              <img class="img-fluid card-img-bottom rounded" :src="'/api/library/' + libraryAuthor.picUrl" alt="Card image">
            </div>
          </div>

          <div class="col-9 mt-3 changeColum">
            <div class="card">
              <div class="card-body">
                <p class="btn btn-danger">文集列表</p>
                <div v-for="(authorIndex, index) in authorIndexList">
                  <a :href="'/literature/' + encodeURIComponent(authorIndex.pdfUrl) + '/'" style="text-decoration-line: none;" target="_blank">
                    <p class="lead text-danger">{{ authorIndex.title }}</p>
                  </a>
                </div>
              </div>
            </div>
          </div>

        </div>

      </div>

      <!-- 白框底部留白 -->
      <br>
      <br>
      <br>
    </div>
    <!-- 底部留白 -->
    <br>
    <br>

  </div>
  <Footer/>

</template>

<style scoped>

</style>
