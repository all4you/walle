package com.ngnis.walle.api;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author houyi
 */
@Data
public class GroupMessageDTO {

    private String boardCode;

    private JSONObject data;

}
