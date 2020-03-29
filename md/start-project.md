## 将项目运行起来

### 下载项目

```sh
git clone http://github.com/all4you/walle
```

### 打包项目

```sh
cd walle
mvn clean package
```

打包时跳过测试：

```sh
mvn clean --DskipTests package
```

打好的包在这个目录：

```sh
./walle-core/target/walle-core-0.0.1.jar
```

### 启动项目

打包好之后可以直接通过命令行启动：

```sh
java -jar ./walle-core/target/walle-core-0.0.1.jar
```

或者也可以直接通过 mvn 指令启动项目：

```sh
cd walle-core
mvn spring-boot:run
```



### 数据库准备

创建相应的数据库，并将表结构创建好。

具体的脚本在: walle/walle-core/src/resources/sql/tables.sql

**需要注意的是，创建的数据库账号密码要和 application.properties 文件中保持一致。**



### 前端资源

该项目对应的前端页面是通过 vue 实现的，对应的前端项目是 [walle-web](https://github.com/all4you/walle-web)

修改页面之后需要将最新的资源打包后拷贝到 walle 的资源目录

**1.打包**

```sh
# 构建生产环境
npm run build:prod
```

**2.资源拷贝**

打包好的资源在 dist/ 目录下，将该目录下的所有文件都拷贝到 walle 的资源目录下：

```sh
rm -rf ~/walle/walle-core/src/main/resources/static/*
cp -R dist/* ~/walle/walle-core/src/main/resources/static/
```

