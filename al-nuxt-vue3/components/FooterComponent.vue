<script setup lang="ts">
import { onMounted, onBeforeUnmount } from 'vue';

/**
 * 移动端隐藏底部logo
 *
 * SSR没有window对象，须在生命周期中声明
 */
const hiddenImg = () => {
  let img = document.getElementById("displayImg");
  if (img) {
    img.style.display = document.documentElement.clientWidth > 768 ? "block" : "none";
  }
}

onMounted(() => {
  hiddenImg();
  window.addEventListener('resize', hiddenImg); // 使用onMounted来在组件挂载window对象
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', hiddenImg);
});
</script>

<template>
  <!-- 页脚 -->
  <footer class="bg-danger mt-3" style="position: relative;bottom: 0;width: 100%;">
    <div class="container text-center">
      <div class="row">
        <div class="col-12">
          <!-- logo图片 -->
          <div style="float: left;" id="displayImg">
            <img src="/img/newlogo.png" alt="网站logo" style="max-width: 83px">
          </div>
          <!-- 底部页脚文字 -->
          <div class="text-warning" style="font-size: 0.9rem">
            <strong>友情链接</strong>：
            <a class="btn" style="color: #FC8C23"
               href="https://www.marxists.org/" target="_blank"><strong>马克思主义中文网</strong></a>
            <span style="margin: 0 3px;">/</span>
            <a class="btn" style="color: #FC8C23"
               href="https://github.com/TfiyuenLau/AphroraLetters-Springcloud"
               target="_blank"
            >
              <strong>GitHub项目地址</strong>
            </a>
            <br>
            <strong>Copyright &copy; 2022-2023
              <a class="btn btn-lg" style="color: #FC8C23" href="https://www.cnblogs.com/tfiyuenlau/" target="_blank">
                <strong>@Tfiyuen Lau</strong></a>.</strong> All rights reserved.
          </div>
        </div>
      </div>

    </div>
  </footer>
</template>

<style scoped>

</style>
