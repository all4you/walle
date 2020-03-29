## 接口调用

页面是进行模板管理的，最终发送消息还是要通过接口来操作的。

只需要两步即可通过接口发送消息：

1.引入 walle-api 模块：

```xml
<dependency>
    <groupId>com.alibaba.walle</groupId>
    <artifactId>walle-api</artifactId>
    <version>0.0.1</version>
</dependency>
```

2.创建 WalleClient 并发送消息：

```java
WalleConfig config = WalleConfig.builder()
        .endPoint("http://127.0.0.1:7001")
        .accessKey("m74hscNSZPVWo3tK")
        .secretKey("QsfigP8ibVhgFv5QmcPHkwsV")
        .build();
// 创建 WalleClient
WalleClient walleClient = new WalleHttpClient(config);
// 设置请求参数
GroupMessageDTO messageDTO = new GroupMessageDTO();
// 模板编号
messageDTO.setBoardCode("device_offline");
// 携带的数据
JSONObject data = new JSONObject();
data.put("level", "high");
messageDTO.setData(data);
// 发送请求
BaseResult result = walleClient.sendGroupMessage(messageDTO);
System.out.println(result);
```

其中 **accessKey** 和 **secretKey** 在【个人中心】-【安全设置】中查看。