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

