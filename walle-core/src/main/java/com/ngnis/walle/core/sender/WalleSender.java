package com.ngnis.walle.core.sender;

import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.SignatureUtil;
import com.ngnis.walle.core.board.Address;
import com.ngnis.walle.core.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 默认的内容发送器实现
 * 官方文档：https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalleSender implements Sender {

    private static final String DING_TALK_SEND_MESSAGE_API = "https://oapi.dingtalk.com/robot/send?access_token=";

    private final RestTemplate restTemplate;

    @Override
    public <T> PojoResult<T> send(Address address, Message message, Class<T> responseType) {
        // 参数检查
        check(address, message, responseType);
        // 请求实体，包括请求头和请求体
        HttpEntity<String> requestEntity = entity(message);
        String requestUrl = getUrl(address);
        // 通过UriComponentsBuilder创建URI对象，这样RestTemplate不会自动进行 URLEncode
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(requestUrl);
        URI uri = uriComponentsBuilder.build(true).toUri();
        // 发送POST请求
        ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseType);
        // 处理返回的Entity
        return parseResponse(responseEntity);
    }

    private void check(Address address, Message message, Class<?> responseType) {
        if (address == null || !address.valid()) {
            throw new IllegalArgumentException("address should not be empty");
        }
        if (message == null || !message.valid() || StringUtils.isEmpty(message.content())) {
            throw new IllegalArgumentException("message should not be empty");
        }
        if (responseType == null) {
            throw new IllegalArgumentException("responseType should not be null");
        }
    }

    private HttpEntity<String> entity(Message message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(message.content(), headers);
    }

    private String getUrl(Address address) {
        String url = DING_TALK_SEND_MESSAGE_API + address.getGroupAccessToken();
        String secret = address.getSecret();
        if (StrUtil.isNotBlank(secret)) {
            long timestamp = System.currentTimeMillis();
            String sign = SignatureUtil.sign(timestamp, secret);
            url += "&timestamp=" + timestamp + "&sign=" + sign;
        }
        return url;
    }

    private <T> PojoResult<T> parseResponse(ResponseEntity<T> responseEntity) {
        // 处理返回的结果
        PojoResult<T> senderResponse = new PojoResult<>();
        HttpStatus status = responseEntity.getStatusCode();
        boolean isSuccess = false;
        if (status == HttpStatus.OK) {
            isSuccess = true;
        }
        senderResponse.setSuccess(isSuccess);
        senderResponse.setErrorCode(status.value());
        senderResponse.setContent(responseEntity.getBody());
        return senderResponse;
    }

}
