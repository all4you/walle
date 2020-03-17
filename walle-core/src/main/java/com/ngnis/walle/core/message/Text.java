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
public class Text {
    /**
     * 正文内容
     */
    @NotEmpty(message = "content should not be empty")
    private String content;

    public static JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("content","");
        return json;
    }

}
