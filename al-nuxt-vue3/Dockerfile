# 使用 Node.js 14 作为基础镜像
FROM node:18.16.0 as build-stage

# 镜像作者
MAINTAINER TfiyuenLau <tfiyuenlau@foxmail>

# 设置工作目录
WORKDIR /app

# 复制编译文件到工作目录
COPY .output ./.output/
COPY  ecosystem.config.cjs ./

# 安装 PM2 全局
RUN npm config set registry https://registry.npm.taobao.org/
RUN npm install pm2 -g

# 暴露端口
EXPOSE 3000

# 使用 PM2 启动应用
CMD ["pm2-runtime", "start", "./ecosystem.config.cjs"]
