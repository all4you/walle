package com.ngnis.walle.core.message;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houyi.wh
 * @since 2018-11-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class At {
    /**
     * @ 的用户手机号
     */
    private List<String> atMobiles;
    /**
     * 是否 @ 所有人
     */
    private boolean isAtAll;

    public static JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("atMobiles",new ArrayList<>());
        json.put("isAtAll",false);
        return json;
    }
}
