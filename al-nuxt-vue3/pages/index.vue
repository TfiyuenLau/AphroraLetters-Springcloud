<script setup lang="ts">
import {onBeforeMount} from 'vue';

onBeforeMount(() => {
  window.open('/home', '_self');
});
</script>

<template>
  <div style="background-color: #fff">
    <p class="h3 text-center">欢迎来到没有知识的荒原！</p>
    <a-skeleton :loading="true" :active="true" :paragraph="{ rows: 32 }"></a-skeleton>
  </div>
</template>

<style scoped>

</style>
