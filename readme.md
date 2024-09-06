# XNote

简体中文 | [English](readme-en.md) | [api-doc](./docs/api-doc.md)

## 简介

XNote 是一个开源 Web 端的 Markdown 笔记管理应用，支持在线编辑和预览 Markdown 笔记。

---

## 技术栈

1. [Vue3](https://github.com/vuejs/)
2. [Nuxt3](https://nuxt.com/)
3. [TypeScript](https://www.typescriptlang.org/)
4. [Vditor](https://github.com/Vanessa219/vditor)
5. [Spring Boot](https://spring.io/guides/gs/spring-boot)
6. [MyBatis-Plus](https://baomidou.com)
7. [MySQL](https://www.mysql.com/)
8. [MinIO](https://min.io/)
9. [Elasticsearch](https://www.elastic.co/cn/elasticsearch/)

---

## 数据库表

![数据库设计](./docs/images/database.png)

## git仓库

- 前端：[xnote-nuxt(github)](https://github.com/XCLHove/xnote-nuxt) | [xnote-nuxt(gitee)](https://gitee.com/xclhove/xnote-nuxt)

- 后端：[xnote-springboot(github)](https://github.com/XCLHove/xnote-springboot) | [xnote-springboot(gitee)](https://gitee.com/xclhove/xnote-springboot)

---

## 构建（后端）

需要安装 JDK 8 和 Maven。
1. 进入项目根目录，安装依赖：
    ```sh
    mvn clean install
    ```
2. 构建
    ```sh
    mvn package
    ```
3. 构建产物在`target`目录下，可直接部署到服务器：
    ```sh
    java -jar target/xnote-springboot-v<version>.jar
    ```

---

## 构建（前端）

需要安装 Node.js 和 Yarn。

1. 进入项目根目录，安装依赖：
    ```sh
    yarn install
    ```
2. 构建
    ```sh
    yarn build
    ```
3. 构建产物在`.output`目录下，可直接部署到服务器，启动`.output/server/index.mjs`即可：
   ```sh
   node ./output/server/index.mjs
   ```
   
## 环境变量（前端）

* `API_SERVER_URL`（必填）：后端 API 地址，如 “http://localhost:8080”。
* `IPC`（可选）： 备案号，如：“蜀IPC备-xxxxxxx号”。