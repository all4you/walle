package com.ngnis.walle.core.board;

import com.ngnis.walle.common.bean.BeanValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

/**
 * <p>
 * Address类
 * 可以根据name来获取Address
 * 可以通过url与机器人通信
 * </p>
 *
 * @author houyi.wh
 * @since 2018-09-14
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    /**
     * 该Address的条件表达式
     * 只有表达式的结果为true，
     * 才能将该Board投递过去
     */
    private String condition;

    /**
     * Address的名称
     */
    @NotBlank(message = "名称(name)不能为空")
    private String name;

    /**
     * Address的类型
     * group: 钉钉群
     */
    private String type;

    /**
     * 钉钉群的accessToken
     * 就是通过这个与钉钉群机器人通信
     */
    @NotBlank(message = "群机器人(groupAccessToken)不能为空")
    private String groupAccessToken;

    /**
     * 加密密钥
     */
    private String secret;


    public boolean valid() {
        return BeanValidator.validate(this).isSuccess();
    }

    public boolean matchCondition(Map<String, Object> data) {
        return true;
    }

}
