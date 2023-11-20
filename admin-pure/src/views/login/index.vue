<script setup lang="ts">
import Motion from "./utils/motion";
import {useRouter} from "vue-router";
import {message} from "@/utils/message";
import {loginRules} from "./utils/rule";
import {useNav} from "@/layout/hooks/useNav";
import type {FormInstance} from "element-plus";
import {useLayout} from "@/layout/hooks/useLayout";
import {useUserStoreHook} from "@/store/modules/user";
import {initRouter, getTopMenu, addPathMatch} from "@/router/utils";
import {bg, avatar, illustration} from "./utils/static";
import {useRenderIcon} from "@/components/ReIcon/src/hooks";
import {ref, reactive, toRaw, onMounted, onBeforeUnmount} from "vue";
import {useDataThemeChange} from "@/layout/hooks/useDataThemeChange";

import dayIcon from "@/assets/svg/day.svg?component";
import darkIcon from "@/assets/svg/dark.svg?component";
import Lock from "@iconify-icons/ri/lock-fill";
import User from "@iconify-icons/ri/user-3-fill";
import {usePermissionStoreHook} from "@/store/modules/permission";
import {setToken} from "@/utils/auth";
import {getLogin} from "@/api/user";

defineOptions({
  name: "Login"
});
const router = useRouter();
const loading = ref(false);
const ruleFormRef = ref<FormInstance>();

const {initStorage} = useLayout();
initStorage();

const {dataTheme, dataThemeChange} = useDataThemeChange();
dataThemeChange();
const {title} = useNav();

const ruleForm = reactive({
  username: "",
  password: "",
  code: "",
});

const onLogin = async (formEl: FormInstance | undefined) => {
  loading.value = true;
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      // 全部采取静态路由模式
      usePermissionStoreHook().handleWholeMenus([]);
      addPathMatch();
      getLogin({username: ruleForm.username, password: ruleForm.password, code: ruleForm.code}).then(res => {
        if (res.code == 200) {
          setToken({
            realname: res.data.realname,
            username: res.data.username,
            roles: res.data.roles,
            accessToken: "eyJhbGciOiJIUzUxMiJ9.admin"
          } as any);
          router.push("/");
          message(res.msg, {type: "success"});
        } else {
          message(res.msg, {type: "error"});
        }
      })
    } else {
      loading.value = false;
      return fields;
    }
  });
};

const tipsDialogVisible = ref(true); // 消息提示对话框

/** 使用公共函数，避免`removeEventListener`失效 */
function onkeypress({code}: KeyboardEvent) {
  if (code === "Enter") {
    onLogin(ruleFormRef.value);
  }
}

/** 改变验证图案 */
const changeImageCode = () => {
  let image = document.getElementById("imageCode");
  if (image) {
    image.setAttribute("src", "/api/article/admin/generateValidateCode?i=" + Math.random());
  }
}

onMounted(() => {
  window.document.addEventListener("keypress", onkeypress);
});

onBeforeUnmount(() => {
  window.document.removeEventListener("keypress", onkeypress);
});
</script>

<template>
  <div class="select-none">
    <img :src="bg" class="wave"/>
    <div class="flex-c absolute right-5 top-3">
      <!-- 主题 -->
      <el-switch
        v-model="dataTheme"
        inline-prompt
        :active-icon="dayIcon"
        :inactive-icon="darkIcon"
        @change="dataThemeChange"
      />
    </div>
    <div class="login-container">
      <div class="img">
        <component :is="toRaw(illustration)"/>
      </div>
      <div class="login-box">
        <div class="login-form">
          <avatar class="avatar"/>
          <Motion>
            <h2 class="outline-none">{{ title }}</h2>
          </Motion>

          <el-form
            ref="ruleFormRef"
            :model="ruleForm"
            :rules="loginRules"
            size="large"
          >
            <Motion :delay="100">
              <el-form-item
                :rules="[
                  {
                    required: true,
                    message: '请输入账号',
                    trigger: 'blur'
                  }
                ]"
                prop="username"
              >
                <el-input
                  clearable
                  v-model="ruleForm.username"
                  placeholder="账号"
                  :prefix-icon="useRenderIcon(User)"
                />
              </el-form-item>
            </Motion>

            <Motion :delay="150">
              <el-form-item prop="password">
                <el-input
                  clearable
                  show-password
                  v-model="ruleForm.password"
                  placeholder="密码"
                  :prefix-icon="useRenderIcon(Lock)"
                />
              </el-form-item>
            </Motion>

            <Motion :delay="150">
              <el-form-item prop="code">
                <el-row :gutter="4">
                  <el-col :span="12">
                    <el-input
                      clearable
                      v-model="ruleForm.code"
                      placeholder="验证码"
                    />
                  </el-col>
                  <el-col :span="12">
                    <el-button @click="changeImageCode">
                      <el-image
                        id="imageCode"
                        src="/api/article/admin/generateValidateCode">
                      </el-image>
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </Motion>

            <Motion :delay="250">
              <el-button
                class="w-full mt-4"
                size="default"
                type="primary"
                :loading="loading"
                @click="onLogin(ruleFormRef)"
              >
                登录
              </el-button>
            </Motion>
          </el-form>
        </div>
      </div>
    </div>
  </div>

  <!-- 提示信息对话框 -->
  <el-dialog v-model="tipsDialogVisible" title="AL Panel Tips" width="45%" draggable>
    <div class="flex items-center justify-center">
      <div class="bg-white p-8 rounded-lg shadow-lg text-center w-full">
        <h1 class="text-2xl mb-4 text-left">欢迎偶然闯入AL社区的管理面板！ 🌟</h1>
        <p class="text-gray-700 mb-6 text-left">
          如果你突然发现已不知自己身在何处，或者想要加入我们热闹非凡的社区，那么请你立即行动：
        </p>
        <ul class="text-left mb-6">
          <li class="mb-2 flex items-start">
            <span class="mr-2 text-green-600">&#x1F4E7;</span>
            <span>如果你想要<strong>加入Aphrora Letters社区的管理者行列</strong>，请写封电子邮件给我们的网站开发者：
              <a href="mailto:tfiyuenlau@foxmail.com" class="text-red-600 font-bold">tfiyuenlau@foxmail.com</a>
            </span>
          </li>
          <li class="mb-2 flex items-start">
            <span class="mr-2 text-green-600">&#x1F680;</span>
            <span>如果你是一个神秘的旅行者，迷失在这片互联网的海洋中，别担心！我们为你准备了一条<strong>安全的逃生通道</strong>：
              <a href="https://aphrora-letters.cpolar.io/home" class="text-red-600 font-bold">点击逃离</a>
            </span>
          </li>
        </ul>
        <p class="text-gray-700 mb-6 text-left">
          我们热爱思考、分享笑声，也渴望与你共度美好的实践时光。让我们一起来创造奇迹吧！ ✨
        </p>
        <p class="text-gray-700 font-bold text-right">愿你有一个愉快的一天！ 🌈</p>
        <p class="text-gray-700 font-bold text-right">热情洋溢的AL社区团队 🎉</p>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-space :size="8">
          <el-button @click="tipsDialogVisible = false">取消</el-button>
        </el-space>
        <a href="https://aphrora-letters.cpolar.io/home" target="_blank">
          <el-button type="primary" @click="">
              跳转主站
          </el-button>
        </a>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
@import url("@/style/login.css");
</style>

<style lang="scss" scoped>
:deep(.el-input-group__append, .el-input-group__prepend) {
  padding: 0;
}
</style>
