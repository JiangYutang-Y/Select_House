## 搜房网项目

一个房屋租赁平台，房地产的三级市场。

## 技术选型

* 构建系统： Maven
* 后台框架： Spring Boot 全家桶
* 前端技术： Nodejs、Vue3

## 项目目录结构
采用 java standard layout 目录结构
```
src -- 后端代码根目录
    main -- 代码主目录
        java -- java 源代码主目录
        # kotlin -- kotlin 源代码主目录
        resources -- 静态资源目录
    test -- 单元测试主目录
        java -- java 源代码主目录
        # kotlin -- kotlin 源代码主目录
        resources -- 静态资源目录
frontend -- 前端代码根目录
target -- 最初是没有的，是编译运行过程中的临时目录
dev -- 开发中间件定制目录
.mvn -- maven wrapper 目录，实现本地无需安装 maven
mvnw -- maven wrapper 目录，实现本地无需安装 maven for linux
mvnw.cmd -- maven wrapper 目录，实现本地无需安装 maven for windows
docker-compose.yaml -- docker 容器编排文件，实现本地一键拉起开发所需的中间件
```

## 开发

启动开发环境

```shell
docker-compose up -d --build
```

启动后端服务

```shell
./mvnw spring-boot:run
```

启动前端服务

> 前置步骤
> 1. `cd frontend` 进入到前端目录
> 2. 执行 `npm install` 安装依赖，
     >       推荐设置国内源，安装命令：`npm install cnpm -g --registry=https://registry.npmmirror.com`，把所有的 npm 替换为 cnpm 指令
     >       例如： `npm install` -> `cnpm install`

```shell
cd frontend
```

```shell
npm run dev
```

#### 推荐设置国内源

配置文件地址： Windows/MacOS/Linux： ~/.m2/settings.xml
> 如果没有此文件，创建一个即可

配置如下信息（如果已经有内容，则确保 mirrors 标签下内容相同即可）：

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <mirrors>
    <mirror>
      <id>nexus-tencentyun</id>
      <mirrorOf>*</mirrorOf>
      <name>Nexus tencentyun</name>
      <url>http://mirrors.cloud.tencent.com/nexus/repository/maven-public/</url>
    </mirror>
  </mirrors>
</settings>
```

## 附录信息

### Maven

Maven 是一个 Java 的构建系统。

版本选择： 3.9.0

maven 采用 Java 源码标准布局(src 部分)：

```
├── pom.xml                     # Maven 的项目描述文件，包含项目信息、依赖管理，代码构建等信息，会被 maven 所读取使用
├── README.md                   # 项目源代码主文档，包含了项目说明、技术信息、负责人（Owner）信息
└── src                         # 项目源代码目录
    ├── main                    # 项目源代码主目录
    │   ├── java                # 项目 java 源代码目录
    │   └── resources           # 项目资源目录
    └── test                    # 项目单元测试目录
        ├── java                # java 源代码单元测试目录
        └── resources           # java 测试资源目录
```

Maven wrapper： 用于 Maven 版本管理
> 使用 `mvn wrapper:wrapper` 命令

所有的 maven 命令都应该使用 `./mvnw` （MacOS/Linux） 或者 `.\mvnw.cmd` （Windows）

示例： `./mvnw clean` 用于清除编译缓存

### Spring Boot

Spring Boot 是一个来自于 Spring 的脚手架框架，基于 “约定大于配置” 的理念。

版本选择： 2.7.9

#Git

#### 本地暂存区
是代码提交的中间阶段

```shell
git commit -m "提交信息" 
```

>提交信息用于描述开发者本次代码提交的主要内容
sample `git commit -m '    '`

### 推送到远程仓库
```shell
git push origin main
```

origin:是默认远程仓库(约定俗成)，对应的是一个远程仓库名
main:默认远程代码分支，对应一个本地代码分支

>根据 Git 版本以及远程仓库的约定，默认分支可能是 master 或者 main
### 如没有，则需创建分支
```shell
git checkout -b main
```
