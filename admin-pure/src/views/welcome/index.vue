<script setup lang="ts">
import {ref, reactive, onMounted, onUnmounted, computed, Ref, watchEffect} from 'vue';
import {http} from '@/utils/http';
import {useDark, useECharts, type EchartOptions} from "@pureadmin/utils";
import {openErrorNotification, openSuccessNotification} from "@/utils/notification";

defineOptions({
  name: "Welcome"
});

interface Admin {
  id: number,
  realname: string,
  username: string,
  description: string,
  authority: string,
}

interface VersionLog {
  id: number;
  version: string;
  log: string;
  createBy: string;
  time: string;
}

interface ServerBasicInfo {
  os: string;
  processors: number;
  diskTotal: number;
  diskFreeSpace: number;
  time: string;
}

interface ServerLoadInfo {
  totalMemory: number;
  freeMemory: number;
  cpuUsage: number;
  time: string;
}

const dialogVisible = ref(false);

const versionLogList = ref<VersionLog[]>();
const adminInfo = ref<Admin>();
let adminUpdateForm = reactive({
  id: undefined,
  realname: undefined,
  username: undefined,
  password: undefined,
  description: undefined,
});
let serverBasicInfo = reactive<ServerBasicInfo>({
  os: '',
  processors: 0,
  diskTotal: 0,
  diskFreeSpace: 0,
  time: "",
});
let serverLoadInfo = reactive<ServerLoadInfo>({
  totalMemory: 0,
  freeMemory: 0,
  cpuUsage: 0,
  time: "",
});

const {isDark} = useDark();
let theme: EchartOptions["theme"] = computed(() => {
  return isDark.value ? "dark" : "default";
});

// 磁盘饼图配置项
const diskPieChart = ref<HTMLDivElement | null>(null);
const {setOptions: setDiskPieOptions, getInstance: getDiskPieInstance} = useECharts(
  diskPieChart as Ref<HTMLDivElement>,
  {theme},
);
watchEffect(() => { // 在数据更新时重绘磁盘饼图
  if (getDiskPieInstance) {
    setDiskPieOptions({
      title: {
        text: "服务器主机磁盘空间饼图",
        left: "center",
        bottom: "bottom",
      },
      legend: {
        orient: 'vertical', // 'horizontal' or 'vertical'
        right: 'right',
        top: 'top',
        data: ['已用存储', '可用存储'],
      },
      series: [
        {
          type: "pie",
          label: {
            show: true,
            formatter: function (params) {
              return `${params.name}: ${(params.value as number).toFixed(1)} (${params.percent}%)`;
            },
          },
          data: [
            {
              value: serverBasicInfo.diskTotal - serverBasicInfo.diskFreeSpace,
              name: '已用存储',
            },
            {
              value: serverBasicInfo.diskFreeSpace,
              name: '可用存储',
            },
          ],
          radius: '80%'
        }
      ],
    });
  }
});

// CPU&内存利用率折线图配置
const cpuMemoryLineChart = ref<HTMLDivElement | null>(null);
const {setOptions: setCpuMemoryLineOptions, getInstance: getCpuMemoryLineInstance} = useECharts(
  cpuMemoryLineChart as Ref<HTMLDivElement>,
  {theme},
);
const xAxisData = ref<string[]>([]); // 折线图x轴数据
const cpuUsageData = ref<number[]>([]); // CPU占用率
const memoryUsageData = ref<number[]>([]); // 内存占用率

const getVersionLogList = () => {
  http.request<any>('get', '/api/article/getVersionLogList').then(res => {
    versionLogList.value = res.data;
  }).catch(error => {
    openErrorNotification("Error:" + error);
  });
};

const getLoginAdmin = () => {
  http.request<any>('get', '/api/article/admin/getLoginAdmin').then(res => {
    adminInfo.value = res.data;
  }).catch(error => {
    openErrorNotification("Error:" + error);
  });
};

const getServerBasicInfo = () => {
  http.request<any>('get', '/api/article/admin/getServerBasicInfo').then(res => {
    Object.assign(serverBasicInfo, res.data);
  }).catch(error => {
    openErrorNotification("Error:" + error);
  });
};

// 获取服务器负载信息
const getServerLoadInfo = () => {
  http.request<any>('get', '/api/article/admin/getServerLoadInfo').then(res => {
    // 封装后端数据
    Object.assign(serverLoadInfo, res.data);

    // 向Options队列中添加数据
    addToQueue(xAxisData.value, serverLoadInfo.time);
    addToQueue(cpuUsageData.value, serverLoadInfo.cpuUsage);
    let memoryUsage = parseFloat((serverLoadInfo.freeMemory / serverLoadInfo.totalMemory).toFixed(4));
    addToQueue(memoryUsageData.value, memoryUsage * 100);

    // 更新折线图图表
    if (getCpuMemoryLineInstance()) {
      setCpuMemoryLineOptions({
        title: {
          text: "服务器主机CPU与内存利用率折线图",
          left: "center",
          bottom: "bottom",
        },
        legend: {
          orient: "vertical", // 'horizontal' or 'vertical'
          right: "right",
          top: "top",
          data: ["CPU利用率", "内存占用率"],
        },
        xAxis: {
          data: xAxisData.value,
          name: "时间",
          axisLine: {
            show: true,
            lineStyle: {
              width: 4,
            },
          },
          axisTick: {
            show: true,
            alignWithLabel: true,
          },
        },
        yAxis: {
          type: 'value',
          name: '占用率',
          axisLine: {
            show: true,
            lineStyle: {
              width: 4,
            },
          },
          axisTick: {
            show: true,
            alignWithLabel: true,
          },
        },
        series: [
          {
            name: "CPU利用率",
            data: cpuUsageData.value,
            type: "line",
            stack: "x",
            areaStyle: {},
            smooth: true,
            label: {
              show: true,
              position: 'bottom',
            },
          },
          {
            name: "内存占用率",
            data: memoryUsageData.value,
            type: "line",
            stack: "x",
            areaStyle: {},
            smooth: true,
            label: {
              show: true,
              position: "bottom",
            },
          },
        ]
      });
    }
  }).catch(error => {
    openErrorNotification("Error:" + error);
  });
};

const updateAdmin = () => {
  dialogVisible.value = false

  const param = {
    data: adminUpdateForm,
  }
  http.request<any>("put", "/api/article/admin/updateAdminById", param).then(res => {
    getLoginAdmin();
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification("用户信息修改失败" + error);
  });
};

const timerId = ref(null);

onMounted(() => {
  getVersionLogList();
  getLoginAdmin();
  getServerBasicInfo();
  getServerLoadInfo();

  // 配置定时器轮询CPU内存数据
  timerId.value = setInterval(getServerLoadInfo, 5000);
});

onUnmounted(() => {
  // 在页面停止聚焦时清除定时器
  clearInterval(timerId.value);
});

const handleAdminInfo = () => {
  dialogVisible.value = true;

  adminUpdateForm.id = adminInfo.value.id;
  adminUpdateForm.realname = adminInfo.value.realname;
  adminUpdateForm.username = adminInfo.value.username;
  adminUpdateForm.description = adminInfo.value.description;
}

// 在添加数据时，若超出最大值则删除最早的元素
function addToQueue<T>(arr: T[], value: T) {
  const MAX_LENGTH = 8;
  if (arr.length >= MAX_LENGTH) {
    arr.shift(); // 删除数组第一个元素（最早入队列的元素）
  }
  arr.push(value);
}

</script>

<template>
  <div>
    <el-row :gutter="20">
      <!-- 版本日志信息 -->
      <el-col :span="12">
        <el-scrollbar max-height="315px">
          <el-card class="box-card">
            <template #header>
              <div class="card-header" style="display: flex;align-items: center;">
                <IconifyIconOnline icon="mdi:sort-clock-descending-outline" width="22"/>
                <span>版本日志</span>
              </div>
            </template>
            <el-timeline class="text item">
              <el-timeline-item v-for="(versionLog, index) in versionLogList" :key="index" :timestamp="versionLog.time">
                <el-card>
                  <el-tag :size="'large'" style="font-weight: bolder">
                    {{ 'VERSION: ' + versionLog.version }}
                  </el-tag>
                  <div>
                    <el-text>{{ versionLog.log }}</el-text>
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-scrollbar>
      </el-col>

      <!-- 个人账户信息 -->
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header" style="display: flex;align-items: center;">
              <IconifyIconOnline icon="mdi:account-badge" width="22"/>
              <span>个人账户</span>
            </div>
          </template>
          <el-descriptions
            class="margin-top"
            direction="vertical"
            title="登录账户信息"
            :column="3"
            border
            v-if="adminInfo"
          >
            <template #extra>
              <el-button type="primary" @click="handleAdminInfo">修改信息</el-button>
            </template>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item" style="display: flex;align-items: center;">
                  <IconifyIconOnline icon="mdi:account" width="18"/>
                  账户昵称
                </div>
              </template>
              {{ adminInfo.realname }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item" style="display: flex;align-items: center;">
                  <IconifyIconOnline icon="mdi:card-account-details-outline" width="18"/>
                  账户名
                </div>
              </template>
              {{ adminInfo.username }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item" style="display: flex;align-items: center;">
                  <IconifyIconOnline icon="mdi:badge-account-horizontal-outline" width="18"/>
                  用户权限
                </div>
              </template>
              {{ adminInfo.authority }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item" style="display: flex;align-items: center;">
                  <IconifyIconOnline icon="mdi:text-box-edit-outline" width="18"/>
                  用户描述
                </div>
              </template>
              {{ adminInfo.description }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
    <br>

    <!-- 用户信息修改对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="修改当前用户信息"
      width="30%"
    >
      <el-form label-position="top" :model="adminUpdateForm">
        <el-form-item label="管理员ID">
          <el-input v-model="adminUpdateForm.id" disabled/>
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input v-model="adminUpdateForm.realname"/>
        </el-form-item>
        <el-form-item label="账户名">
          <el-input v-model="adminUpdateForm.username"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="adminUpdateForm.password" placeholder="请输入一个新的密码串" show-password/>
        </el-form-item>
        <el-form-item label="个人描述">
          <el-input v-model="adminUpdateForm.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4 }"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateAdmin">提交</el-button>
      </span>
      </template>
    </el-dialog>

    <!-- 监控面板 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:cloud-print-outline" width="22"/>
          <span>监控面板</span>
        </div>
      </template>
      <el-row :gutter="16">
        <!-- 服务器状态信息 -->
        <el-col :span="6">
          <el-statistic title="OS" :value="null">
            <template #prefix>
              <p v-text="serverBasicInfo.os"></p>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="核心数" :value="serverBasicInfo.processors">
            <template #suffix>
              <p>核心</p>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="磁盘总空间" :value="serverBasicInfo.diskTotal">
            <template #suffix>
              <p>GB</p>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="磁盘剩余空间" :value="serverBasicInfo.diskFreeSpace">
            <template #suffix>
              <p>GB</p>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
      <br>
      <!-- ECharts图表 -->
      <el-row :gutter="32">
        <el-col :span="12">
          <div ref="diskPieChart" style="width: 100%; height: 42vh"/>
        </el-col>
        <el-col :span="12">
          <div ref="cpuMemoryLineChart" style="width: 100%; height: 42vh"/>
        </el-col>
      </el-row>
    </el-card>

  </div>
</template>

<style scoped>

</style>
