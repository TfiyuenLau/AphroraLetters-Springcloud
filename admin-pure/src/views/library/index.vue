<script setup lang="ts">
import {ref, reactive, onMounted} from 'vue';
import {http} from "@/utils/http";
import type {UploadProps} from 'element-plus'

import {openErrorNotification, openSuccessNotification} from "@/utils/notification";

defineOptions({
  name: "Library"
});

interface AuthorIndex {
  articleId: number;
  title: string;
  pdfUrl: string;
  authorId: number;
  createBy: string;
  isEffective: boolean;
  libraryAuthor: any;
}

interface AuthorInfo {
  id: number;
  characterName: string;
  picUrl: string;
  introduction: string;
  isEffective: number;
  authorIndices: AuthorIndex[];
}

interface AuthorParams {
  id: number;
  characterName: string;
  picUrl: string;
  introduction: string;
}

interface LiteratureParam {
  articleId: number;
  title: string;
  pdfUrl: string;
  authorId: number;
}

let authorInsertForm = reactive<AuthorParams>({
  id: undefined,
  characterName: "",
  picUrl: "",
  introduction: "",
});
let authorUpdateForm = reactive<AuthorParams>({
  id: undefined,
  characterName: "",
  picUrl: "",
  introduction: "",
});
let literatureInsertFrom = reactive<LiteratureParam>({
  articleId: undefined,
  title: "",
  pdfUrl: "",
  authorId: undefined,
});
let literatureUpdateFrom = reactive<LiteratureParam>({
  articleId: undefined,
  title: "",
  pdfUrl: "",
  authorId: undefined,
});

const libraryList = ref<AuthorInfo[]>(null);
let authorUpdateDialogVisible = ref<boolean>(false);
let literatureUpdateDialogVisible = ref<boolean>(false);

// 获取文库所有作者对应的文章
const getAuthorList = () => {
  http.request<any>("get", "/api/library/getAuthorList").then(res => {
    libraryList.value = res.data;
  }).catch(error => {
    openErrorNotification("Error: " + error);
  });
}

// 收录新的文库作者
const insertAuthor = () => {
  const param = {
    data: authorInsertForm,
  };
  http.request<any>("post", "/api/library/admin/insertAuthor", param).then(res => {
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification(error);
  });
}

// 收录新增一篇文库文章
const insertLiterature = () => {
  const param = {
    data: literatureInsertFrom,
  };
  http.request<any>("post", "/api/library/admin/insertLiterature", param).then(res => {
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification(error);
  });
}

// 图片文件上传校验
const beforeImageUpload: UploadProps['beforeUpload'] = (rawFile) => {
  const accessFile = ['image/jpeg', 'image/jpg', 'image/png'];
  if (!accessFile.includes(rawFile.type)) {
    openErrorNotification('请选择正确的图片格式');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    openErrorNotification('上传的图片大于2MB');
    return false;
  }
  return true;
}

// PDF文件上传校验
const beforePdfUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'application/pdf') {
    openErrorNotification('请选择正确的PDF文件');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 64) {
    openErrorNotification('上传的文件大于64MB');
    return false;
  }
  return true;
}

// 题图文件上传反馈
const handleAuthorAvatarSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  authorInsertForm.picUrl = "img/" + response.data;
  authorUpdateForm.picUrl = "img/" + response.data;
  openSuccessNotification("头像上传成功");
}

// 文章PDF文件上传反馈
const handleLiteratureSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  authorInsertForm.picUrl = "pdf/" + response.data;
  openSuccessNotification("头像上传成功");
}

// 向Form传输作者旧的数据
const handleAuthorUpdateForm = (row: AuthorInfo): void => {
  authorUpdateDialogVisible.value = true;

  authorUpdateForm.id = row.id;
  authorUpdateForm.characterName = row.characterName;
  authorUpdateForm.picUrl = row.picUrl;
  authorUpdateForm.introduction = row.introduction;
};

// 更新ID对应的作者信息
const updateAuthor = (): void => {
  const param = {
    data: authorUpdateForm,
  };
  http.request<any>("put", "/api/library/admin/updateAuthor", param).then(res => {
    getAuthorList();
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification(error);
  });

  authorUpdateDialogVisible.value = false;
};

// 向Form传输文库文章旧的数据
const handleLiteratureUpdateForm = (row: LiteratureParam): void => {
  literatureUpdateDialogVisible.value = true;

  literatureUpdateFrom.articleId = row.articleId;
  literatureUpdateFrom.title = row.title;
  literatureUpdateFrom.pdfUrl = row.pdfUrl;
  literatureUpdateFrom.authorId = row.authorId;
};

// 更新ID对应的文库文章信息
const updateLiterature = (): void => {
  const param = {
    data: literatureUpdateFrom,
  };
  http.request<any>("put", "/api/library/admin/updateLiterature", param).then(res => {
    getAuthorList();
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification(error);
  });

  literatureUpdateDialogVisible.value = false;
};

// 删除对应ID的作者
const deleteAuthor = (id: number) => {
  http.request<any>("delete", "/api/library/admin/deleteAuthor?id=" + id).then(res => {
    getAuthorList();
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification(error);
  });
}

// 删除文库对应ID的文章
const deleteLiterature = (articleId: number) => {
  http.request<any>("delete", "/api/library/admin/deleteLiterature?articleId=" + articleId).then(res => {
    getAuthorList();
    openSuccessNotification(res.data);
  }).catch(error => {
    openErrorNotification(error);
  });
}

onMounted(() => {
  getAuthorList();
});

</script>

<template>
  <div>
    <el-row :gutter="16">
      <!-- 作者收录表单 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header" style="display: flex;align-items: center;">
              <IconifyIconOnline icon="mdi:account-cowboy-hat-outline" width="22"/>
              <span>作者收录</span>
              <el-popconfirm title="请确保作者信息已填写完成。"
                             width="192"
                             confirm-button-text="确认"
                             cancel-button-text="不了"
                             @confirm="insertAuthor">
                <template #reference>
                  <el-button class="button ml-3" type="primary">收录</el-button>
                </template>
              </el-popconfirm>
            </div>
          </template>
          <el-form label-position="top">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="作者头像" required>
                  <el-upload action="/api/library/admin/uploadAuthorImage"
                             style="width: 100%;"
                             name="image" drag
                             :before-upload="beforeImageUpload"
                             :on-success="handleAuthorAvatarSuccess"
                  >
                    <el-upload-dragger style="padding: 48px 10px">
                      <el-avatar v-if="authorInsertForm.picUrl"
                                 :size="'large'"
                                 :shape="'square'"
                                 :src="'/api/library/' + authorInsertForm.picUrl"
                      />
                      <IconifyIconOnline v-else icon="mdi:folder-upload-outline" width="64" style="margin: 0 auto;"/>
                    </el-upload-dragger>
                    <div class="el-upload__text">
                      Drop file here or <em>click to upload</em>
                    </div>
                    <template #tip>
                      <div class="el-upload__tip">
                        jpg/png files with a size less than 2mb
                      </div>
                    </template>
                  </el-upload>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作者名" required>
                  <el-input v-model="authorInsertForm.characterName" placeholder="请输入待收录的作者名"></el-input>
                </el-form-item>
                <el-form-item label="简介" required>
                  <el-input
                      type="textarea"
                      v-model="authorInsertForm.introduction"
                      :rows="8"
                      placeholder="请输入对该作者的简单介绍"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>

      <!-- 文章收录表单 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header" style="display: flex;align-items: center;">
              <IconifyIconOnline icon="mdi:notebook-edit-outline" width="22"/>
              <span>文章收录</span>
              <el-popconfirm title="请确保文章信息已填写完成。"
                             width="192"
                             confirm-button-text="确认"
                             cancel-button-text="不了"
                             @confirm="insertLiterature">
                <template #reference>
                  <el-button class="button ml-3" type="primary">收录</el-button>
                </template>
              </el-popconfirm>
            </div>
          </template>
          <el-form label-position="top">
            <el-row :gutter="16">
              <el-col :span="16">
                <el-form-item label="文章标题" required>
                  <el-input v-model="literatureInsertFrom.title" placeholder="请输入待收录的文章标题"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="所属作者" required>
                  <el-select v-model="literatureInsertFrom.authorId">
                    <el-option
                        v-for="authorInfo in libraryList"
                        :value="authorInfo.id"
                        :label="authorInfo.characterName"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="文章PDF" required>
              <el-upload action="/api/library/admin/uploadAuthorPdf" drag style="width: 100%" name="pdf"
                         :before-upload="beforePdfUpload" :on-success="handleLiteratureSuccess">
                <IconifyIconOnline icon="mdi:folder-upload-outline" width="64" style="margin: 0 auto;"/>
                <div class="el-upload__text">
                  Drop file here or <em>click to upload</em>
                </div>
                <template #tip>
                  <div class="el-upload__tip">
                    pdf files with a size less than 64mb
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <br>

    <!-- 文库文章列表 -->
    <el-card>
      <template #header>
        <div class="card-header" style="display: flex;align-items: center;">
          <IconifyIconOnline icon="mdi:format-list-group" width="22"/>
          <span>文库列表</span>
        </div>
      </template>
      <el-table :data="libraryList" border max-height="1024">
        <el-table-column type="expand">
          <template #default="props">
            <el-table :data="props.row.authorIndices" :border="false">
              <el-table-column prop="articleId" label="文章ID" width="128"/>
              <el-table-column prop="title" label="文章标题"/>
              <el-table-column prop="pdfUrl" label="文件URL"/>
              <el-table-column prop="createBy" label="发布时间"/>
              <el-table-column fixed="right" width="128" label="Operation">
                <template #default="scope">
                  <el-space direction="vertical">
                    <!-- 触发修改对话框 -->
                    <el-button type="primary" size="small" @click="handleLiteratureUpdateForm(scope.row)" plain>修改</el-button>
                    <!-- 删除确认按钮 -->
                    <el-popconfirm title="你确定要删除该文章吗？"
                                   width="200"
                                   confirm-button-text="确认删除"
                                   cancel-button-text="不了"
                                   @confirm="deleteLiterature(scope.row.articleId)">
                      <template #reference>
                        <el-button type="danger" size="small" plain>删除</el-button>
                      </template>
                    </el-popconfirm>
                  </el-space>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="ID" width="64"></el-table-column>
        <el-table-column prop="characterName" label="作者" width="128">
          <template #default="scope">
            <div>
              <el-avatar :src="'/api/library/' + scope.row.picUrl" :size="64"></el-avatar>
              <p>{{ scope.row.characterName }}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="introduction" label="简介" show-overflow-tooltip="true"></el-table-column>
        <el-table-column fixed="right" width="128" label="Operation">
          <template #default="scope">
            <el-space direction="vertical">
              <!-- 触发修改对话框 -->
              <el-button type="primary" size="small" @click="handleAuthorUpdateForm(scope.row)">修改</el-button>
              <!-- 删除确认按钮 -->
              <el-popconfirm title="你确定要删除该作者吗？"
                             width="200"
                             confirm-button-text="确认删除"
                             cancel-button-text="不了"
                             @confirm="deleteAuthor(scope.row.id)">
                <template #reference>
                  <el-button type="danger" size="small">删除</el-button>
                </template>
              </el-popconfirm>
            </el-space>
          </template>
        </el-table-column>
      </el-table>

      <!-- 文库作者信息修改对话框 -->
      <el-dialog v-model="authorUpdateDialogVisible" title="文库作者信息修改">
        <el-form label-position="top">
          <el-row :gutter="16">
            <el-col :span="8">
              <el-form-item label="ID" required>
                <el-input v-model="authorUpdateForm.id" disabled></el-input>
              </el-form-item>
              <el-form-item label="作者头像" required>
                <el-upload action="/api/library/admin/uploadAuthorImage"
                           style="width: 100%;"
                           name="image" drag
                           :before-upload="beforeImageUpload"
                           :on-success="handleAuthorAvatarSuccess"
                >
                  <el-upload-dragger style="padding: 48px 10px">
                    <el-avatar v-if="authorUpdateForm.picUrl"
                               :size="'large'"
                               :shape="'square'"
                               :src="'/api/library/' + authorUpdateForm.picUrl"
                    />
                    <IconifyIconOnline v-else icon="mdi:folder-upload-outline" width="64" style="margin: 0 auto;"/>
                  </el-upload-dragger>
                  <div class="el-upload__text">
                    Drop file here or <em>click to upload</em>
                  </div>
                  <template #tip>
                    <div class="el-upload__tip">
                      jpg/png files with a size less than 2mb
                    </div>
                  </template>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="作者名" required>
                <el-input v-model="authorUpdateForm.characterName" placeholder="请输入要修改的作者名"></el-input>
              </el-form-item>
              <el-form-item label="简介" required>
                <el-input
                    type="textarea"
                    v-model="authorUpdateForm.introduction"
                    :rows="8"
                    placeholder="请输入对该作者的简单介绍"
                >
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="authorUpdateDialogVisible = false" plain>取消</el-button>
            <el-button type="primary" @click="updateAuthor">提交</el-button>
          </div>
        </template>
      </el-dialog>

      <!-- 文库文章信息修改对话框 -->
      <el-dialog v-model="literatureUpdateDialogVisible" title="文库文章信息修改">
        <el-form label-position="top">
          <el-row :gutter="16">
            <el-col :span="16">
              <el-form-item label="文章标题" required>
                <el-input v-model="literatureUpdateFrom.title" placeholder="请输入待收录的文章标题"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属作者" required>
                <el-select v-model="literatureUpdateFrom.authorId">
                  <el-option
                      v-for="authorInfo in libraryList"
                      :value="authorInfo.id"
                      :label="authorInfo.characterName"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="文章PDF" required>
            <el-upload action="/api/library/admin/uploadAuthorPdf" drag style="width: 100%" name="pdf"
                       :before-upload="beforePdfUpload" :on-success="handleLiteratureSuccess">
              <IconifyIconOnline icon="mdi:folder-upload-outline" width="64" style="margin: 0 auto;"/>
              <div class="el-upload__text">
                Drop file here or <em>click to upload</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  pdf files with a size less than 64mb
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="authorUpdateDialogVisible = false" plain>取消</el-button>
            <el-button type="primary" @click="updateLiterature">提交</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<style scoped>

</style>
