# 工程简介

Aphrora Letters是一个为众多作者提供发表哲学社科文章平台，并存放哲学著作典籍的社区项目(Author: tfiyuenlau@foxmail.com)。

## 开源组件

* **JDK版本**: 11
* **后端构建管理工具**: Apache Maven
* **应用服务器**: Apache Tomcat, Nginx
* **后端开发框架**: SpringBoot
* **微服务框架**: SpringCloud(Fegin, Gateway), SpringCloud Alibaba(Nacos)
* **前端开发框架**: Vue.js v3, Nuxt.js v3
* **前端组件库**: Bootstrap, Element Plus, Ant Design vue
* **数据存储**: MySql, Redis
* **ORM框架**: MyBatis, Mybatis Plus
* **认证鉴权**: Sa-Token
* **接口文档工具**: Swagger2
* **性能测试**: Jmeter
* **容器技术**: Docker

## 部署文档
### Ⅰ、环境要求
* 开发平台OS：推荐Windows10
* 部署平台OS：推荐Linux发行版
* 内存：推荐8GB以上
* Git: 官方稳定版
* Java: 推荐JDK 11及以上
* Maven: 推荐3.8.5及以上
* Node.js: 推荐v18.16.0
* Docker、Docker Compose: 官方稳定版

### Ⅱ、步骤说明
1.使用 **git** 从 GitHub 拉取项目；
~~~shell
git clone https://github.com/TfiyuenLau/AphroraLetters-Springcloud.git
~~~

2.使用 **Maven** 对 SpringCloud 微服务子项目进行**打包**；
~~~shell
mvn clean
~~~
~~~shell
mvn package
~~~

3.将 gateway, article-service, library-service 打包生成的 `./#/target/#.jar` 文件从 `./#/target/` 复制到 `./#/` 目录下。

4.进入`al-nuxt-vue3`和`admin-pure`前后台项目，输入`npm install`安装依赖；

随后分别使用下列命令打包生成`.output`和`dist`构建文件。
~~~shell
# al-nuxt-vue3构建
npm run build
~~~
~~~shell
# admin-pure构建（可选，用以搭建开发环境）
pnpm run build 
~~~

5.进入主项目目录，**运行`docker-compose`一键打包部署**；
~~~shell
docker-compose up
~~~

6.进入项目前台页面 `http://localhost:80/home` 或项目后台面板 `http://localhost:80/admin` 。

# 延伸阅读
## 相关站点
**项目地址**：[点我进入](https://aphrora-letters.hk.cpolar.io/home)

**后台地址**：[点我进入](https://aphrora-letters.hk.cpolar.io/admin/#/login)

**项目开发博客**：[点我进入](https://www.cnblogs.com/tfiyuenlau/articles/17764385.html)

## 项目界面
![Home首页](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165446238-482846355.png)
![文章列表](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165513669-529013424.png)
![文章浏览页](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165525768-1384681825.png)
![关于](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165553809-1285658013.png)
![后台面板登录页](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165604701-1769792316.png)
![后台首页](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165618216-23715689.png)
![文章管理](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165636078-1548535643.png)
![文库管理1](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165652589-639771898.png)
![文库管理2](https://img2023.cnblogs.com/blog/3018774/202310/3018774-20231014165701261-66766400.png)
