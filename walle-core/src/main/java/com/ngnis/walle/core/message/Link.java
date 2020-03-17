package com.ngnis.walle.core.message;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author houyi.wh
 * @since 2018-11-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    /**
     * 标题
     */
    @NotEmpty(message = "title should not be empty")
    private String title;
    /**
     * 摘要
     */
    @NotEmpty(message = "text should not be empty")
    private String text;
    /**
     * 链接跳转url
     */
    @NotEmpty(message = "messageUrl should not be empty")
    private String messageUrl;
    /**
     * 预览图url
     */
    private String picUrl;


    public static JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("title","");
        json.put("text","");
        json.put("messageUrl","");
        json.put("picUrl","");
        return json;
    }
}
