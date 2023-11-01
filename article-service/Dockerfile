# 使用一个基础的Java镜像作为基础镜像
FROM openjdk:11

# 镜像作者
MAINTAINER TfiyuenLau <tfiyuenlau@foxmail>

# 在容器中创建一个新目录来存放应用程序
RUN mkdir /app

# 创建挂载目录
RUN mkdir /logs
RUN mkdir -p /resources/static
RUN mkdir -p /app/file:/app/al-article.jar!/BOOT-INF/classes!/static/img

# 将应用程序的.jar文件复制到容器中
COPY article-service-2.0.0-RELEASE.jar /app/al-article.jar

# 设置工作目录

WORKDIR /app

# 声明端口（如果您的应用程序监听了特定的端口，确保在这里声明）
EXPOSE 8081

# 定义容器启动时的命令
CMD ["java", "-jar", "al-article.jar"]
