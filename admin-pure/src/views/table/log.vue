<script setup lang="ts">
import {ref, reactive, onMounted} from 'vue';
import {http} from '@/utils/http'
import {openErrorNotification, openSuccessNotification} from "@/utils/notification";

defineOptions({
  name: "Log"
});

interface SysLog {
  id: number;
  ip: string;
  createBy: string;
  remark: string;
  operateParam: string;
  operateUrl: string;
  operateBy: string;
  isEffective: boolean;
}

const logList = ref<SysLog[] | null>(null);

const pagination = ref({
  current: 1,
  size: 10,
  total: 0,
});

const handleSizeChange = (pageSize: number) => {
  pagination.value.size = pageSize;
  getSysLogByPage();
};

const handleCurrentChange = (currentPage: number) => {
  pagination.value.current = currentPage;
  getSysLogByPage();
};

const getSysLogByPage = async () => {
  http.get<any, any>('/api/article/admin/getSysLogByPage/' + pagination.value.current).then(res => {
    if (res.ok) {
      logList.value = res.data.records;
      pagination.value.total = res.data.total;
      pagination.value.size = res.data.size;
      pagination.value.current = res.data.current;
    } else {
      // 处理错误逻辑
      openErrorNotification('遇到错误：' + res.msg);
    }
  });
}

onMounted(() => {
  getSysLogByPage()
});

</script>

<template>
  <div>
    <el-card class="box-card">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:cloud-cog-outline" width="22" />
          <span>日志列表</span>
        </div>
      </template>
      <div class="text item">
        <!-- 列表与分页 -->
        <el-table :data="logList" border height="512">
          <el-table-column prop="id" label="ID" width="64"></el-table-column>
          <el-table-column prop="ip" label="用户IP地址"></el-table-column>
          <el-table-column prop="operateUrl" label="请求路径"></el-table-column>
          <el-table-column prop="operateParam" label="请求参数"></el-table-column>
          <el-table-column prop="operateBy" label="操作设备"></el-table-column>
          <el-table-column prop="createBy" label="操作时间"></el-table-column>
        </el-table>
        <br>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="sizes, prev, pager, next, jumper"
        ></el-pagination>
      </div>
    </el-card>

  </div>
</template>

<style scoped>
.example-pagination-block + .example-pagination-block {
  margin-top: 10px;
}

.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
