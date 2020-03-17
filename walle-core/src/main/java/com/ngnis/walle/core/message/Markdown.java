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
public class Markdown {
    /**
     * 标题
     */
    @NotEmpty(message = "title should not be empty")
    private String title;
    /**
     * 正文内容，需要自己定义markdown的格式
     */
    @NotEmpty(message = "text should not be empty")
    private String text;

    public static JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("title","");
        json.put("text","");
        return json;
    }
}
