<script setup lang="ts">
import {ref, reactive, onMounted} from 'vue';
import {http} from '@/utils/http';
import {openErrorNotification, openSuccessNotification} from "@/utils/notification";
import type {UploadProps} from 'element-plus'

import axios from 'axios';
// Markdown编辑器
import {MdEditor} from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

defineOptions({
  name: "Article"
});

interface ArticleCategory {
  id: number;
  categoryName: string;
  createBy: string | null;
  modifiedBy: string | null;
}

interface Article {
  id: number;
  title: string;
  pictureUrl: string,
  content: string,
  summary: string;
  traffic: number;
  createBy: string;
  modifiedBy: string;
  isPassed: boolean;
  categoryList: ArticleCategory[] | null;
  time: string;
}

const pagination = ref({
  current: 1,
  size: 10,
  total: 0,
});

const editFormVisible = ref(false);
const drawer = ref(false);

const articleInsertFrom = reactive({
  title: undefined,
  summary: undefined,
  content: "# 请由此键入Markdown文本",
  pictureUrl: undefined,
})
const articleList = ref<Article[] | null>(null);
const articleInfoForm = reactive({
  id: undefined,
  title: undefined,
  pictureUrl: undefined,
  summary: undefined,
});
const articleContentForm = reactive({
  id: undefined,
  content: undefined,
});
const articleCategoryList = ref<ArticleCategory[] | null>(null);
const recommendCategoryList = ref<ArticleCategory[] | null>(null);
const categoryInput = ref("");

// 为修改抽屉栏传参
const handleInfoEdit = (row: any) => {
  drawer.value = true;

  try {
    articleInfoForm.id = row.id;
    articleInfoForm.pictureUrl = row.pictureUrl;
    articleInfoForm.title = row.title;
    articleInfoForm.summary = row.summary;

    articleCategoryList.value = row.categoryList;
  } catch (e) {
    editFormVisible.value = false;
    openErrorNotification("获取数据失败");
  }
};

// 为修改对话框传参
const handleContentEdit = (row: any) => {
  editFormVisible.value = true;

  try {
    articleContentForm.id = row.id;
    articleContentForm.content = row.content;
  } catch (e) {
    editFormVisible.value = false;
    openErrorNotification("获取数据失败！请检查服务器连接");
  }
};

// 请求分页数据
const getArticleListByPage = () => {
  http.request<any>('get', '/api/article/admin/getArticleListByPage/' + pagination.value.current).then(res => {
    articleList.value = res.data.records;
    pagination.value.total = res.data.total;
    pagination.value.size = res.data.size;
    pagination.value.current = res.data.current;
  }).catch(error => {
    openErrorNotification("数据获取错误：" + error);
  })
};

// 请求推荐标签
const getRecommendCategory = () => {
  http.request<any>('get', '/api/article/getRecommendCategory/' + categoryInput.value).then(res => {
    recommendCategoryList.value = res.data;
  }).catch(error => {
    if (categoryInput.value != "") {
      openErrorNotification("推荐标签请求错误：" + error);
    }
  });
};

// 新增文章
const insertArticle = () => {
  const param = {
    data: articleInsertFrom,
  }
  http.request<any>('post', "/api/article/admin/insertArticle", param).then(res => {
    openSuccessNotification(res.msg);
  }).catch(error => {
    openErrorNotification("Error:" + error);
  });
}

// MdEditor上传文件方法实现
const onUploadImg = async (files, callback) => {
  const res = await Promise.all(
      files.map((file) => {
        return new Promise((rev, rej) => {
          const form = new FormData();
          form.append('image', file);

          axios
              .post('/api/article/admin/uploadArticleImage', form, {
                headers: {
                  'Content-Type': 'multipart/form-data'
                }
              })
              .then((res) => {
                openSuccessNotification("图片上传成功");
                rev(res);
              })
              .catch((error) => {
                openErrorNotification("图片上传失败！请重试")
                rej(error);
              });
        });
      })
  );

  callback(res.map((item) => "/api/article/img/" + item.data.data));
};

// 插入新的标签并添加至对应文章
const insertArticleCategory = () => {
  const param = {
    data: {
      categoryName: categoryInput.value,
    }
  }
  http.request<any>('post', '/api/article/admin/insertArticleCategory', param).then(res => {
    insertClassification(res.data);
    openSuccessNotification("添加标签成功")
    getArticleListByPage();
  }).catch(error => {
    openErrorNotification("Error:" + error);
  })
};

// 为文章添加标签
const insertClassification = (categoryId: number) => {
  const param = reactive({
    data: {
      articleId: articleInfoForm.id,
      categoryId: categoryId,
    }
  });

  http.request<any>('post', '/api/article/admin/insertClassification', param).then(res => {
    openSuccessNotification(res.msg);
    getArticleListByPage();
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
};

// 发送更新文章信息请求
const updateArticleInfo = (updateFrom?: any) => {
  editFormVisible.value = false;

  const param = {
    data: updateFrom,
  };
  http.request<any>('put', '/api/article/admin/updateArticleInfo', param).then(() => {
    openSuccessNotification('文章信息更新成功');
    getArticleListByPage();
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
};

// 发送更新文章内容请求
const updateArticleContent = (updateFrom?: any) => {
  const param = {
    data: updateFrom, // 将 updateFrom 对象作为 data 属性的值
  };
  http.request<any>('put', '/api/article/admin/updateArticleContent', param).then(() => {
    openSuccessNotification('文章内容更新成功');
    getArticleListByPage();
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
};

// 文件上传校验
const beforeImageUpload: UploadProps['beforeUpload'] = (rawFile) => {
  const accessFile = ['image/jpeg', 'image/jpg', 'image/png']
  if (!accessFile.includes(rawFile.type)) {
    openErrorNotification('请选择正确的图片格式');
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    openErrorNotification('上传的图片大于2MB');
    return false
  }
  return true
}

// 题图文件上传反馈
const handleArticleImageSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  articleInsertFrom.pictureUrl = "img/" + response.data;
  openSuccessNotification("题图上传成功");
}

// 文章题图修改文件上传反馈
const handleImageSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  articleInfoForm.pictureUrl = response.data;
  openSuccessNotification("文章题图修改成功");
}

// 发送文章删除请求
const deleteArticleInfoById = (id: number) => {
  http.request<any>('delete', '/api/article/admin/deleteArticleInfoById/' + id).then(res => {
    openSuccessNotification(res.msg);
    getArticleListByPage();
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
};

// 发送文章标签删除请求
const deleteClassificationById = (articleId: number, categoryId: number) => {
  http.request<any>('delete', '/api/article/admin/deleteClassification/' + articleId + '/' + categoryId).then(res => {
    if (res.ok) {
      openSuccessNotification(res.msg);
      getArticleListByPage();
    }
  }).catch(error => {
    openErrorNotification('Error:' + error);
  });
};

const handleSizeChange = (pageSize: number) => {
  pagination.value.size = pageSize;
  getArticleListByPage();
};

const handleCurrentChange = (currentPage: number) => {
  pagination.value.current = currentPage;
  getArticleListByPage();
};

onMounted(() => {
  getArticleListByPage();
});

</script>

<template>
  <div>
    <!-- 文章编辑 -->
    <el-card class="box-border" shadow="always">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:text-box-edit" width="22"/>
          <span>新增文章</span>
          <el-popconfirm title="你确定要提交该文章吗？"
                         width="200"
                         confirm-button-text="确认"
                         cancel-button-text="不了"
                         @confirm="insertArticle">
            <template #reference>
              <el-button class="button ml-3" type="primary">提交文章</el-button>
            </template>
          </el-popconfirm>
        </div>
      </template>
      <div class="text item">
        <el-form label-position="top">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="文章题图" required>
                <el-upload class="upload-demo" action="/api/article/admin/uploadArticleImage" drag style="width: 100%"
                           :before-upload="beforeImageUpload" :on-success="handleArticleImageSuccess">
                  <IconifyIconOnline icon="mdi:folder-upload-outline" width="64" style="margin: 0 auto;"/>
                  <div class="el-upload__text">
                    Drop file here or <em>click to upload</em>
                  </div>
                  <template #tip>
                    <div class="el-upload__tip">
                      jpg/png files with a size less than 200kb
                    </div>
                  </template>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="文章标题" required>
                <el-input v-model="articleInsertFrom.title" placeholder="请输入文章标题"/>
              </el-form-item>
              <el-form-item label="文章简介" required>
                <el-input type="textarea" placeholder="请输入文章简介"
                          v-model="articleInsertFrom.summary" :autosize="{ minRows: 4, maxRows: 6 }"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="文章内容编辑" required>
            <MdEditor v-model="articleInsertFrom.content" @onUploadImg="onUploadImg"></MdEditor>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <br>

    <!-- 文章列表 -->
    <el-card class="box-card" shadow="always">
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:format-list-group" width="22"/>
          <span>文章列表</span>
        </div>
      </template>
      <div class="">
        <!-- 列表与分页 -->
        <el-table :data="articleList" border max-height="1024">
          <el-table-column prop="id" label="ID" width="64"></el-table-column>
          <el-table-column label="文章题图">
            <template #default="scope">
              <el-image :src="'/api/article/' + scope.row.pictureUrl" fit="scale-down" lazy/>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="文章标题"></el-table-column>
          <el-table-column prop="summary" label="文章简介" show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="content" label="内容" v-if="false"></el-table-column>
          <el-table-column prop="categoryList" label="分类标签">
            <template #default="scope">
              <el-space wrap>
                <el-tag v-for="category in scope.row.categoryList">
                  {{ category.categoryName }}
                </el-tag>
              </el-space>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="最后一次修改"></el-table-column>
          <el-table-column fixed="right" width="160" label="Operation">
            <template #default="scope">
              <el-space direction="vertical">
                <!-- 触发修改对话框 -->
                <el-button type="primary" size="small" @click="handleInfoEdit(scope.row)">编辑信息</el-button>
                <el-button type="primary" size="small" @click="handleContentEdit(scope.row)">编辑内容</el-button>
                <!-- 删除确认按钮 -->
                <el-popconfirm title="你确定要删除该文章吗？"
                               width="200"
                               confirm-button-text="确认删除"
                               cancel-button-text="不了"
                               @confirm="deleteArticleInfoById(scope.row.id)">
                  <template #reference>
                    <el-button type="danger" size="small">删除文章</el-button>
                  </template>
                </el-popconfirm>
              </el-space>
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
      </div>
    </el-card>
    <br>

    <!-- 文章信息编辑抽屉 -->
    <el-drawer v-model="drawer">
      <template #header>
        <h4>文章基本信息编辑</h4>
      </template>
      <template #default>
        <!-- Edit修改表单 -->
        <el-form label-position="top">
          <el-form :model="articleInfoForm" label-position="top">
            <el-form-item label="" required>
              <el-input v-model="articleInfoForm.id" v-if="false" disabled></el-input>
            </el-form-item>
            <el-form-item label="文章题图">
              <el-upload class="avatar-uploader" action="/api/article/admin/updateArticleImage" name="image"
                         :before-upload="beforeImageUpload" :on-success="handleImageSuccess"
                         :data="{id: articleInfoForm.id}">
                <el-image v-if="articleInfoForm.pictureUrl" :src="'/api/article/' + articleInfoForm.pictureUrl"
                          class="el-upload" style="max-width: 256px"/>
              </el-upload>
            </el-form-item>
            <el-form-item label="文章标题" required>
              <el-input v-model="articleInfoForm.title"></el-input>
            </el-form-item>
            <el-form-item label="文章简述" required>
              <el-input v-model="articleInfoForm.summary" type="textarea"
                        :autosize="{ minRows: 3, maxRows: 6 }"></el-input>
            </el-form-item>
            <el-form-item label="">
              <el-dropdown @command="insertClassification" trigger="click">
                <span class="el-dropdown-link">
                  <el-space wrap>
                      <el-input v-model="categoryInput" @input="getRecommendCategory"
                                placeholder="请输入要添加的标签"></el-input>
                      <el-button type="primary" @click="insertArticleCategory">添加</el-button>
                  </el-space>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item v-for="category in recommendCategoryList" :command="category.id">
                      {{ category.categoryName }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-space wrap>
                <el-tag v-for="category in articleCategoryList" size="large" closable
                        @close="deleteClassificationById(articleInfoForm.id, category.id)">
                  {{ category.categoryName }}
                </el-tag>
              </el-space>
            </el-form-item>
          </el-form>
        </el-form>
      </template>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="editFormVisible = false">取消</el-button>
          <el-button type="primary" @click="updateArticleInfo(articleInfoForm)">修改文章信息</el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 文章内容修改对话框 -->
    <el-dialog v-model="editFormVisible" title="文章内容编辑" width="75%" align-center>
      <el-row :gutter="20">
        <!-- 内容编辑 -->
        <el-col :span="24">
          <MdEditor v-model="articleContentForm.content" @onUploadImg="onUploadImg"
                    @save="updateArticleContent(articleContentForm)"/>
        </el-col>
      </el-row>
      <template #footer>
      <span class="dialog-footer">

      </span>
      </template>
    </el-dialog>

  </div>
</template>

<style scoped>

</style>
