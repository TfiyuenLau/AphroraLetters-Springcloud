<script setup lang="ts">
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
import axiosHttp from "@/axios.http";
import {ref, onMounted} from 'vue'
import {useRouter} from 'vue-router'

interface LibraryAuthor {
  id: number;
  characterName: string;
  picUrl: string;
  introduction: string;
  isEffective: number;
  authorIndices: number[];
}

let router = useRouter();

const libraryAuthorList = ref<LibraryAuthor[] | null>(null)
const getAuthorList = async () => {
  axiosHttp.get('/api/library/getAuthorList').then(res => {
    libraryAuthorList.value = res.data
    document.title = '文库总览 | Aphrora Letters'
  }).catch(error => {
    console.log(error)
  })
}

onMounted(() => {
  getAuthorList()
})

// 打开作者对应的文章索引界面
const openAuthorIndex = (authorId: number) => {
  router.push({
    path: `/author_index/${authorId}`,
    params: {
      'id': authorId
    }
  })
}

</script>

<template>
  <Navbar/>
  <!-- 网站内容 -->
  <div class="container">

    <!-- 正文:背景透明 -->
    <div id="bg" class="mt-3 rounded">
      <div class="container">
        <!-- 卡片组一 -->
        <div class="row" v-if="libraryAuthorList">
          <div class="col-lg-3 col-6 mt-3 changeColum" v-for="(libraryAuthor, index) in libraryAuthorList">
            <div v-if="index % 2 == 1">
              <div class="card" style="max-width: 300px">
                <img class="img-fluid card-img-top rounded" :src="'/api/library/' + libraryAuthor.picUrl" alt="Card image">
                <div class="card-body">
                  <h4 class="card-title text-danger bi bi-book" style="font-family: 华文新魏,serif;">
                    {{ libraryAuthor.characterName }}
                  </h4>
                  <p class="card-text lead">{{ libraryAuthor.introduction }}</p>
                  <button class="btn btn-danger" @click="openAuthorIndex(libraryAuthor.id)">查看文集列表</button>
                </div>
              </div>
            </div>
            <div v-if="index % 2 == 0">
              <div class="card" style="max-width: 300px">
                <div class="card-body">
                  <h4 class="card-title text-danger bi bi-book" style="font-family: 华文新魏,serif">
                    {{ libraryAuthor.characterName }}
                  </h4>
                  <p class="card-text lead">{{ libraryAuthor.introduction }}</p>
                  <button class="btn btn-danger" @click="openAuthorIndex(libraryAuthor.id)">查看文集列表</button>
                </div>
                <img class="img-fluid card-img-bottom rounded" :src="'/api/library/' + libraryAuthor.picUrl" alt="Card image">
              </div>
            </div>
          </div>

        </div>

      </div>
      <!-- 卡片组二 -->
      <div class="row">

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


<script lang="ts">
//隐藏logo
let hid = function () {
  let img = document.getElementById('displayImg');

  if (img != null) {
    img.style.display = document.documentElement.clientWidth > 768 ? 'block' : 'none'
  }
}
window.addEventListener('DOMContentLoaded', hid)
window.addEventListener('resize', hid)
</script>
