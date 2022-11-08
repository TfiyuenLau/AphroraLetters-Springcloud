# 工程简介

**Leftwing Community是一个类似博客的，为众多作者提供发表哲学社科文章平台并存放左翼著作典籍的WEB项目(Author: @MatikaneSpartakusbund)。**

**其后端基于SpringBoot与MybatisPlus框架、前端使用Bootstrap组件与Thymeleaf模板并使用Tomcat服务器进行部署**

### 开源组件说明

* 项目构建管理工具：Apache Maven
* 服务器：Apache Tomcat
* 后端框架：SpringBoot
* 数据库：MySql
* 数据库框架：MyBatis、MybatisPlus
* 数据库连接池：Alibaba Druid
* 前端组件库：Bootstrap5
* 后台组件：AdminLTE-3.1.0
* 页面模板引擎：Thymeleaf
* MD2HTML工具：FlexMark
* PDF插件：PDF.js
* 接口文档工具：Swagger2

### 项目文件结构

~~~

LeftwingCommunity-Springboot:
├───.mvn
│   └───wrapper
├───src  # java源程序
│   ├───main
│   │   ├───java  # 代码层
│   │   │   └───com
│   │   │       └───marxist
│   │   │           └───leftwing_community
│   │   │               ├───config  # 各配置类
│   │   │               ├───controller  # 前端控制器
│   │   │               ├───dao  # 数据持久层Mapper接口
│   │   │               ├───domain  # 部分实体类
│   │   │               ├───entity  # 实体类
│   │   │               ├───generator  # 生成器
│   │   │               ├───interceptor # 拦截器
│   │   │               ├───service  # Web服务层接口
│   │   │               │   └───impl  # 实现类
│   │   │               └───util  #工具类
│   │   └───resources  # 资源文件
│   │       ├───mappers  # 存放mybatis映射文件
│   │       ├───static  # 存放静态资源文件
│   │       │   ├───adminLTE # adminLTE资源文件(存放AdminLTE-3.1.0-rc/dist与plugins下的资源文件)
│   │       │   │   ├───dist
│   │       │   │   └───plugins
│   │       │   ├───bootstrap-5.1.3-dist  # bootstrap组件库
│   │       │   │   ├───css
│   │       │   │   └───js
│   │       │   ├───css
│   │       │   ├───img  # 存放项目图片资源
│   │       │   ├───js
│   │       │   ├───md  # 存放markdown资源文件
│   │       │   ├───page  # 存放静态博客页面
│   │       │   ├───pdf # 存放文库阅读所需的pdf文件
│   │       │   └───pdfjs # 存放开源插件pdf.js
│   │       │       ├───bulid
│   │       │       └───web
│   │       └───templates  # 存放thymeleaf模板文件
│   │           └───adminLTE # thymeleaf后台页面
│   └───test  # 测试类
│       ├───java
│       │   └───com
│       │       └───marxist
│       │           └───leftwing_community
│       └───resources
└───pom.xml  # Maven的pom文件

~~~

# 延伸阅读

### 项目地址：[点我进入](http://8.130.39.9:8080/home)
### 后台地址：[点我进入](http://8.130.39.9:8080/admin/starter)
