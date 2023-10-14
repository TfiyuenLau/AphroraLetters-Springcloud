# 使用Nginx镜像作为生产环境
FROM nginx:stable as production-stage

# 覆盖nginx配置文件
RUN rm -rf /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf

# 创建日志文件夹
RUN mkdir -p /etc/nginx/logs

# 设置时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 复制Vue.js项目构建后的静态资源到Nginx容器中
COPY --from=aphrora-letters-vue3:2.0.0-RELEASE /aphrora-letters-vue3/dist /usr/share/nginx/html/aphrora-letters-vue3
COPY --from=admin-pure:2.0.0-RELEASE /admin-pure/dist /usr/share/nginx/html/admin-pure

# 声明端口
EXPOSE 80

# 容器启动时运行Nginx
CMD ["nginx", "-g", "daemon off;"]
