<script setup lang="ts">
import {ref, reactive, onMounted} from 'vue';
import {http} from '@/utils/http';
import '@/utils/notification';
import {openErrorNotification, openSuccessNotification} from "@/utils/notification";

defineOptions({
  name: "Admin"
});

interface Admin {
  id: number | null;
  realname: string | null;
  username: string | null;
  password: string | null;
  description: string | null;
  authority: string | null;
}

const adminList = ref<Admin[] | null>(null);

const pagination = ref({
  current: 1,
  size: 10,
  total: 0,
});

// 管理员分页列表
const getAdminListByPage = async () => {
  http.get<any, any>('/api/article/admin/getAdminListByPage/' + pagination.value.current).then(res => {
    if (res.ok) {
      adminList.value = res.data.records;
      pagination.value.total = res.data.total;
      pagination.value.size = res.data.size;
      pagination.value.current = res.data.current;
    } else {
      // 处理错误逻辑
      openErrorNotification('遇到错误：' + res.msg);
    }
  })
}

const handleSizeChange = (pageSize: number) => {
  pagination.value.size = pageSize;
  getAdminListByPage();
};

const handleCurrentChange = (currentPage: number) => {
  pagination.value.current = currentPage;
  getAdminListByPage();
};

onMounted(() => {
  getAdminListByPage()
})

const drawer = ref(false)

const insertForm: Admin = reactive({
  id: null,
  realname: null,
  username: null,
  password: null,
  description: null,
  authority: null,
})

// 新增请求
const insertAdmin = (postFrom: Admin) => {
  drawer.value = false

  const param = {
    data: postFrom,
  };
  http.request('post', '/api/article/admin/insertAdmin', param).then(res => {
    openSuccessNotification('添加成功：' + res);
    getAdminListByPage()
  }).catch(error => {
    openErrorNotification('添加失败：' + error)
  });
}

const editFormVisible = ref(false)

const editForm: Admin = reactive({
  id: undefined,
  realname: undefined,
  username: undefined,
  password: undefined,
  description: undefined,
  authority: undefined,
})

// 控制Edit对话框属性
const handleEdit = (row: any) => {
  editFormVisible.value = true;

  editForm.id = row.id;
  editForm.realname = row.realname;
  editForm.username = row.username;
  editForm.description = row.description;
  editForm.authority = row.authority;
}

// 发送更新请求
const updateAdmin = (updateFrom?: Admin) => {
  editFormVisible.value = false;

  const param = {
    data: updateFrom, // 将 updateFrom 对象作为 data 属性的值
  };
  http.request<any>('put', '/api/article/admin/updateAdminById', param).then(res => {
    if (res.ok) {
      openSuccessNotification('资料更新成功');
      getAdminListByPage()
    }
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
}

const confirmDelete = (id: number) => {
  deleteAdmin(id);
};

// 发送删除请求
const deleteAdmin = (id: number) => {
  http.request<any>('delete', '/api/article/admin/deleteAdminById/' + id).then(res => {
    if (res.ok) {
      openSuccessNotification('删除管理员成功');
      getAdminListByPage()
    }
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
}

</script>

<template>
  <!-- 列表与分页 -->
  <div>
    <el-card class="box-card">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:account-box-multiple-outline" width="22" />
          <span>管理员操作</span>
        </div>
      </template>
      <div class="text item">
        <el-table :data="adminList" border height="512">
          <el-table-column prop="id" label="ID" width="64"></el-table-column>
          <el-table-column prop="realname" label="管理员昵称"></el-table-column>
          <el-table-column prop="username" label="账户名"></el-table-column>
          <el-table-column prop="authority" label="用户权限"></el-table-column>
          <el-table-column prop="description" label="个人描述"></el-table-column>
          <el-table-column fixed="right" width="160">
            <template #header>
              <el-button type="success" size="default" @click="drawer = true">Insert</el-button>
            </template>
            <template #default="scope">
              <!-- 触发修改对话框 -->
              <el-button type="primary" size="small" @click="handleEdit(scope.row)">Edit</el-button>
              <!-- 删除确认按钮 -->
              <el-popconfirm title="你确定要删除该管理员吗？"
                             width="180"
                             confirm-button-text="确认删除"
                             cancel-button-text="不了"
                             @confirm="confirmDelete(scope.row.id)">
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
          :page-sizes="[10, 20]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="sizes, prev, pager, next, jumper"
        ></el-pagination>
      </div>
    </el-card>

  <!-- 添加新的管理员抽屉 -->
  <el-drawer v-model="drawer">
    <template #header>
      <h4>新增管理员</h4>
    </template>
    <template #default>
      <el-form label-position="top">
        <el-form-item label="管理员昵称">
          <el-input v-model="insertForm.realname"></el-input>
        </el-form-item>
        <el-form-item label="账户名">
          <el-input v-model="insertForm.username"></el-input>
        </el-form-item>
        <el-form-item label="账户密码">
          <el-input v-model="insertForm.password"></el-input>
        </el-form-item>
        <el-form-item label="账户权限">
          <el-select v-model="insertForm.authority" class="m-2" placeholder="Select" size="default">
            <el-option label="super-admin" value="super-admin"/>
            <el-option label="admin" value="admin"/>
          </el-select>
        </el-form-item>
        <el-form-item label="个人描述">
          <el-input v-model="insertForm.description" type="textarea" :autosize="{ minRows: 3, maxRows: 6 }"/>
        </el-form-item>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="drawer = false">取消</el-button>
        <el-button type="primary" @click="insertAdmin(insertForm)">提交表单</el-button>
      </div>
    </template>
  </el-drawer>

  <!-- Edit修改表单 -->
  <el-dialog v-model="editFormVisible" title="更改管理员基本信息" width="30%" align-center>
    <el-form :model="editForm" label-position="top">
      <el-form-item label="ID">
        <el-input v-model="editForm.id" disabled></el-input>
      </el-form-item>
      <el-form-item label="管理员昵称">
        <el-input v-model="editForm.realname" placeholder="请输入您的昵称"></el-input>
      </el-form-item>
      <el-form-item label="账户名">
        <el-input v-model="editForm.username" placeholder="请输入登录所需的账户名"></el-input>
      </el-form-item>
      <el-form-item label="修改密码">
        <el-input v-model="editForm.password" placeholder="请输入新的密码"></el-input>
      </el-form-item>
      <el-form-item label="账户权限">
        <el-select v-model="editForm.authority" class="m-2" placeholder="Select" size="default">
          <el-option label="super-admin" value="super-admin"/>
          <el-option label="admin" value="admin"/>
        </el-select>
      </el-form-item>
      <el-form-item label="个人描述">
        <el-input v-model="editForm.description" :autosize="{ minRows: 3, maxRows: 6 }"
                  type="textarea" placeholder="请输入个人描述"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateAdmin(editForm)">提交</el-button>
      </span>
    </template>
  </el-dialog>

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
