<script setup lang="ts">
import PdfFrame from "@/components/PdfFrame.vue";
import {ref, onMounted} from 'vue'
import {useRoute} from 'vue-router'

let route = useRoute()
const url = decodeURIComponent(route.params.url as string)
// console.log(url)

if (url != null && url.split('/')[2] != null) {
  document.title = url.split('/')[2] + ' | Aphrora Letters'
}

</script>

<template>
  <PdfFrame :url="url"/>
</template>

<style scoped>

</style>