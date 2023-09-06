<script setup lang="ts">
import {ref, reactive, onMounted, computed} from 'vue';
import {http} from '@/utils/http';
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

const dialogVisible = ref(false);

const versionLogList = ref<VersionLog[] | null>(null);
const adminInfo = ref<Admin | null>(null);
const adminUpdateForm = reactive({
  id: undefined,
  realname: undefined,
  username: undefined,
  password: null,
  description: undefined,
});

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

const updateAdmin = () => {
  dialogVisible.value = false

  const param = {
    data: adminUpdateForm,
  }
  http.request<any>("put", "/api/article/admin/updateAdminById", param).then(res => {
    getLoginAdmin()
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification("用户信息修改失败" + error);
  });
};

onMounted(() => {
  getVersionLogList();
  getLoginAdmin();
});

const handleAdminInfo = () => {
  dialogVisible.value = true;

  adminUpdateForm.id = adminInfo.value.id;
  adminUpdateForm.realname = adminInfo.value.realname;
  adminUpdateForm.username = adminInfo.value.username;
  adminUpdateForm.description = adminInfo.value.description;
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
            <div class="text item">
              <div v-for="(versionLog, index) in versionLogList" :key="index">
                <div class="box" :class=" { 'firstOne': index === 0 } ">
                  <el-tag>{{ versionLog.version }}</el-tag>
                  <div>
                    <el-text>{{ versionLog.log }}</el-text>
                    <el-tag type="info">{{ versionLog.time }}</el-tag>
                  </div>
                </div>
              </div>
            </div>
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
          <div class="text item">
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
          </div>
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
      <div class="text item">

      </div>
    </el-card>

  </div>
</template>

<!-- 版本日志css样式-->
<style scoped>
.box {
  border-left: 2px solid #d1d7de;
  padding: 0 0 10px 15px;
  position: relative;
  pointer-events: none;
}

.box::before {
  position: absolute;
  left: -8px;
  content: '';
  width: 12px;
  height: 12px;
  border-radius: 50ch;
  background-color: #d1d7de;
}

.box:nth-child(1)::before {
  width: 16px;
  height: 16px;
  left: -9px;
  cursor: pointer;
  pointer-events: all;
}
</style>
