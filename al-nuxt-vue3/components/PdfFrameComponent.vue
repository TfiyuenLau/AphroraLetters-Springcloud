<script setup lang="ts">
const props = defineProps(["url"]);
// console.log("props:", props);
</script>

<template>
  <!-- 使用PDF.js插件跨域与分页懒加载实现pdf展示 -->
  <a-skeleton :loading="!props.url">
    <iframe :src="'/pdfjs/web/viewer.html?file=/api/library/' + props.url"
            style="width: 100%;height: 100%;position: absolute;"></iframe>
  </a-skeleton>
</template>

<style scoped>

</style>
