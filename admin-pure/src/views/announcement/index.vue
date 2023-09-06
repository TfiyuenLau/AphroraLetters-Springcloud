<script setup lang="ts">
import {ref, reactive, onMounted} from 'vue';
import {http} from '@/utils/http';
import {openErrorNotification, openSuccessNotification} from "@/utils/notification";

// Markdown编辑器
import {MdEditor} from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

defineOptions({
  name: "Announcement"
});

interface VersionLog {
  id: number;
  version: string;
  log: string;
  createBy: string;
  time: string;
}

interface Announcement {
  id: number;
  publisher: string;
  title: string;
  content: string;
  createBy: string;
}

const versionLogList = ref<VersionLog[] | null>(null)
const announcements = ref<Announcement[] | null>(null);

const pagination = ref({
  current: 1,
  size: 10,
  total: 0,
});

const getVersionLogList = () => {
  http.request<any>('get', '/api/article/getVersionLogList').then(res => {
    versionLogList.value = res.data;
  });
};

const getAnnouncementByPage = () => {
  http.request<any>('get', '/api/article/admin/getAnnouncementByPage/' + pagination.value.current).then(res => {
    announcements.value = res.data.records;
    pagination.value.total = res.data.total;
    pagination.value.size = res.data.size;
    pagination.value.current = res.data.current;
  }).catch(error => {
    openErrorNotification('公告栏数据获取失败：' + error);
  });
}

const handleSizeChange = (pageSize: number) => {
  pagination.value.size = pageSize;
  getAnnouncementByPage();
};

const handleCurrentChange = (currentPage: number) => {
  pagination.value.current = currentPage;
  getAnnouncementByPage();
};

onMounted(() => {
  getVersionLogList()
  getAnnouncementByPage()
});

const versionLogForm: VersionLog = reactive({
  id: null,
  version: null,
  log: null,
  createBy: null,
  time: null,
});

const insertVersionLog = () => {
  const param = {
    data: versionLogForm
  };

  http.request<any>('post', '/api/article/admin/insertVersionLog', param).then(res => {
    if (res.ok) {
      openSuccessNotification("已新增版本日志");
      getVersionLogList();
    }
  }).catch(error => {
    openErrorNotification("遇到未知错误：" + error);
  });
};

const announcementForm: Announcement = reactive({
  id: null,
  title: null,
  publisher: "Aphrora Letters",
  content: "# 请在此键入markdown公告文本内容",
  createBy: null,
});

// 发送新增请求
const insertAnnouncement = () => {
  const param = {
    data: announcementForm,
  };

  http.request<any>('put', '/api/article/admin/insertAnnouncement', param).then(res => {
    if (res.ok) {
      openSuccessNotification('公告发布成功');
      getAnnouncementByPage();
    }
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
}

const editFormVisible = ref(false);

const editForm: Announcement = reactive({
  id: null,
  title: null,
  publisher: null,
  content: null,
  createBy: null,
});

// 控制Edit对话框属性
const handleEdit = (row: any) => {
  editFormVisible.value = true;
  editForm.id = row.id;
  editForm.title = row.title;
  editForm.publisher = row.publisher;
  editForm.content = row.content;
};

// 发送更新请求
const updateAnnouncement = (updateFrom?: Announcement) => {
  editFormVisible.value = false;

  const param = {
    data: updateFrom, // 将 updateFrom 对象作为 data 属性的值
  };
  http.request<any>('put', '/api/article/admin/updateAnnouncement', param).then(res => {
    if (res.ok) {
      openSuccessNotification('资料更新成功');
      getAnnouncementByPage();
    }
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
}

// 发送删除请求
const deleteAnnouncement = (id: number) => {
  http.request<any>('delete', '/api/article/admin/deleteAnnouncementById/' + id).then(res => {
    if (res.ok) {
      openSuccessNotification('该公告已被删除');
      getAnnouncementByPage();
    }
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
}

</script>

<template>
  <div>
    <!-- 版本日志操作栏 -->
    <el-row :gutter="20">
      <!-- 日志表单 -->
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header" style="display: flex;align-items: center;">
              <IconifyIconOnline icon="mdi:application-edit-outline" />
              <span>新增日志</span>
            </div>
          </template>
          <div class="text item">
            <el-form :model="versionLogForm" label-position="top">
              <el-form-item label="版本号">
                <el-input v-model="versionLogForm.version" placeholder="请输入规范的版本号，如1.2"></el-input>
              </el-form-item>
              <el-form-item label="版本信息">
                <el-input v-model="versionLogForm.log" placeholder="版本更新的描述"></el-input>
              </el-form-item>

              <el-popconfirm title="确认发布该版本？"
                             width="180"
                             confirm-button-text="确认"
                             cancel-button-text="取消"
                             @confirm="insertVersionLog">
                <template #reference>
                  <el-button type="primary">发布</el-button>
                </template>
              </el-popconfirm>
            </el-form>
          </div>
        </el-card>
      </el-col>

      <!-- 日志显示 -->
      <el-col :span="12">
        <el-scrollbar max-height="294px">
          <el-card class="box-card">
            <template #header>
              <div class="card-header" style="display: flex;align-items: center;">
                <IconifyIconOnline icon="mdi:sort-clock-descending-outline" width="22" />
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
    </el-row>
    <br>

    <!-- 发布公告栏 -->
    <el-card class="box-card" shadow="always">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:content-save-edit" width="22" />
          <span>发布公告</span>
          <el-popconfirm title="确认发布该公告？"
                         width="180"
                         confirm-button-text="确认"
                         cancel-button-text="取消"
                         @confirm="insertAnnouncement">

            <template #reference>
              <el-button type="primary" size="small">发布</el-button>
            </template>
          </el-popconfirm>
        </div>
      </template>
      <div class="text item">
        <el-col>
          <!-- Edit修改表单 -->
          <el-form :model="announcementForm" label-position="right">
            <el-form-item label="公告标题">
              <el-input v-model="announcementForm.title" placeholder="请输入公告标题" aria-required="true"></el-input>
            </el-form-item>
            <el-form-item label="发布者">
              <el-input v-model="announcementForm.publisher" aria-required="true"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col>
          <MdEditor v-model="announcementForm.content"/>
        </el-col>
      </div>
    </el-card>
    <br>

    <!-- 公告列表 -->
    <el-card class="box-card" shadow="always">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:format-list-group" width="22" />
          <span>公告列表</span>
        </div>
      </template>
      <div class="text item">
        <el-row :gutter="20">
          <el-col :span="24">
            <!-- 列表与分页 -->
            <el-table :data="announcements" border max-height="512">
              <el-table-column prop="id" label="ID" width="64"></el-table-column>
              <el-table-column prop="title" label="公告栏标题"></el-table-column>
              <el-table-column prop="publisher" label="发布者"></el-table-column>
              <el-table-column prop="createBy" label="发布时间"></el-table-column>
              <el-table-column prop="content" label="内容" v-if="false"></el-table-column>
              <el-table-column fixed="right" width="160" label="Operation">
                <template #default="scope">
                  <!-- 触发修改对话框 -->
                  <el-button type="primary" size="small" @click="handleEdit(scope.row)">Edit</el-button>
                  <!-- 删除确认按钮 -->
                  <el-popconfirm title="你确定要删除该公告吗？"
                                 width="180"
                                 confirm-button-text="确认删除"
                                 cancel-button-text="不了"
                                 @confirm="deleteAnnouncement(scope.row.id)">
                    <template #reference>
                      <el-button type="danger" size="small">Delete</el-button>
                    </template>
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>
            <br>
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pagination.current"
              :page-sizes="Array.from({ length: (1000 - 10) / 10 + 1 }, (_, index) => (index + 1) * 10).filter((pageSize) => pageSize <= pagination.size)"
              :page-size="pagination.size"
              :total="pagination.total"
              layout="sizes, prev, pager, next, jumper"
            ></el-pagination>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <br>

    <!-- 公告修改对话框 -->
    <el-dialog v-model="editFormVisible" title="更改公告内容" width="75%" align-center>
      <el-row :gutter="20">
        <!-- 基本信息 -->
        <el-col :span="8">
          <!-- Edit修改表单 -->
          <el-form :model="editForm" label-position="top">
            <el-form-item label="ID">
              <el-input v-model="editForm.id" disabled></el-input>
            </el-form-item>
            <el-form-item label="公告标题">
              <el-input v-model="editForm.title"></el-input>
            </el-form-item>
            <el-form-item label="发布者">
              <el-input v-model="editForm.publisher"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <!-- 内容编辑 -->
        <el-col :span="16">
          <MdEditor v-model="editForm.content"/>
        </el-col>
      </el-row>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="editFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateAnnouncement(editForm)">提交</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<style scoped>

</style>

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
