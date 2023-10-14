# 使用Node.js镜像作为构建环境
FROM node:18.16.0 as build-stage

# 设置工作目录
WORKDIR /aphrora-letters-vue3

# 复制package.json和package-lock.json文件到容器中
COPY package*.json ./

# 安装依赖
RUN npm install --force

# 复制所有源代码到容器中
COPY . .

# 构建Vue.js项目
RUN npm run build
