# 使用一个基础的Java镜像作为基础镜像
FROM openjdk:11

# 在容器中创建一个新目录来存放应用程序
RUN mkdir /gateway

# 将应用程序的.jar文件复制到容器中
COPY target/gateway-1.0.1-RELEASE.jar /gateway/gateway-1.0.1-RELEASE.jar

# 设置工作目录
WORKDIR /gateway

# 定义容器启动时的命令
CMD ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]

# UTF-8编码
ENV LANG C.UTF-8
