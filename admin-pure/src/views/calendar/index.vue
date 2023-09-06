<script setup lang="ts">
import {ref} from 'vue'

defineOptions({
  name: "Calendar"
});
const value = ref(new Date())
</script>

<template>
  <div>
    <el-card class="box-card">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:calendar-clock-outline" width="22"/>
          <span>Calendar</span>
        </div>
      </template>
      <div>
        <el-calendar v-model="value"/>
      </div>
    </el-card>
  </div>
</template>

<style scoped>

</style>
