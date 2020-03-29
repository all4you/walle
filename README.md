# 瓦力

瓦力是一个轻量级的钉钉群消息发送助手，通过瓦力你只需要配置一个发送消息的模板(支持多个地址，且可以在运行时动态修改)，即可快速实现钉钉群消息的发送功能。



### 特性

- [x] **模板管理**: 群消息模板管理，目前支持 TEXT、MARKDOWN、LINK 三种类型群消息
- [x] **多群匹配**: 同一个模板支持同时发送至多个钉钉群，且支持条件表达式进行群路由
- [x] **JWT凭证管理**: 通过 JWT 进行用户凭证的管理
- [x] **开放接口**: 对外暴露 REST 接口供用户触发群消息
- [x] **扩展点**: 面向接口设计，用户可自行实现各个扩展点



### 快速开始

- 首先我们需要先 [将项目运行起来](md/start-project.md)。
- 项目部署成功之后，接着 [创建账号和模板](md/web-operation.md)。
- 账号和模板都创建好之后，就可以进行 [接口调用](md/api-invoke.md)。



### 用户凭证: JWT

用户凭证的生成与校验：[JWT](md/jwt.md)。



### 扩展点: **Center

目前瓦力的设计是面向接口的，核心的账号、模板、消息管理都定义了标准接口，并提供了默认的实现类。

用户可以根据实际需求确定使用默认的实现类还是自定义自己的实现类，并注入到Spring容器中。

- **AccountCenter**: 账号中心，可通过 `@EnableAccountCenter` 引入默认实现类
- **GroupBoardCenter**: 模板中心，可通过 `@EnableGroupBoardCenter` 引入默认实现类
- **MessageCenter**: 消息中心，可通过 `@EnableMessageCenter` 引入默认实现类

或者也可以通过 `@EnableWalle` 引入所有的默认实现类。



### 联系我

欢迎联系我讨论更多细节 

> wh_all4you#hotmail.com

欢迎关注公众号

![CodeChaaaser](md/CodeChaaaser.jpg)



