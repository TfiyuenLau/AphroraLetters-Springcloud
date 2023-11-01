<script setup lang="ts">
import {onBeforeMount} from "vue";

let route = useRoute();
const url = decodeURIComponent(route.params.url as string);
// console.log("url:", url);

onBeforeMount(() => {
  if (url && url.split('/')[1]) {
    document.title = url.split('/')[1] + " | Aphrora Letters";
  }
});
</script>

<template>
  <pdf-frame-component :url="url"/>
</template>

<style scoped>

</style>
