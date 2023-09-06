<script setup lang="ts">
const props = defineProps(['url'])
</script>

<template>
  <!-- 使用PDF.js插件跨域与分页懒加载实现pdf展示 -->
  <div>
    <iframe :src="'/pdfjs/web/viewer.html?file=' + props.url"
            style="width: 100%;height: 100%;position: absolute;"></iframe>
  </div>
</template>

<style scoped>

</style>