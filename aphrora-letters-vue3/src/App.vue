<script setup lang="ts">
import {RouterView} from 'vue-router'
</script>

<template>
  <!-- 配置Antdv4全局主题 -->
  <a-config-provider :theme="{
      token: {
        colorPrimary: '#dc3545',
        colorLink: '#8E354A',
        colorSuccess: '#198754',
        colorWarning: '#ffc107',
      },
    }"
  >
    <RouterView/>
  </a-config-provider>
</template>

<style scoped>

</style>
