package com.ngnis.walle.common;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author houyi
 */
public class VelocityUtil {

    static {
        Velocity.init();
    }

    public static String render(String tag, String template, Map<String, Object> data) {
        if (StrUtil.isBlank(template)) {
            return template;
        }
        // 初始化并取得Velocity引擎
        VelocityEngine ve = new VelocityEngine();
        ve.init();

        VelocityContext context = new VelocityContext();
        for (String key : data.keySet()) {
            Object value = data.get(key);
            context.put(key, value);
        }
        // 1.先将#转义
        template = escape(template);
        StringWriter stringWriter = new StringWriter();
        // 2.渲染结果
        ve.evaluate(context, stringWriter, tag, template);
        // 3.在将转义后的#恢复回来
        String result = stringWriter.toString();
        result = restore(result);
        return result;
    }

    private static String escape(String originContent) {
        if (StrUtil.isBlank(originContent)) {
            return originContent;
        }
        // 1.先把velocity中的语法词转义掉
        String content = originContent
                .replace("#set", "?set?")
                .replace("#if", "?if?")
                .replace("#else", "?else?")
                .replace("#elseif", "?elseif?")
                .replace("#end", "?end?")
                .replace("#foreach", "?foreach?")
                .replace("#include", "?include?")
                .replace("#parse", "?parse?")
                .replace("#macro", "?macro?");
        // 2.将#转义，(如在markdown中的#表示标题)，否则会和velocity的语法冲突
        content = content.replace("#", "\\#");
        // 3.最后将转义后的velocity语法词再恢复回来
        content = content
                .replace("?set?", "#set")
                .replace("?if?", "#if")
                .replace("?else?", "#else")
                .replace("?elseif?", "#elseif")
                .replace("?end?", "#end")
                .replace("?foreach?", "#foreach")
                .replace("?include?", "#include")
                .replace("?parse?", "#parse")
                .replace("?macro?", "#macro");
        return content;
    }

    private static String restore(String originContent) {
        if (StrUtil.isBlank(originContent)) {
            return originContent;
        }
        return originContent.replace("\\#", "#");
    }


    public static void main(String[] args) {
        String template = "### 发现非绿码人员入园 \n\n> ### 触发来源信息 \n\n> 来源=${reportType} | 时间=$!{time} | 位置=${location} \n\n> ----------------- \n\n> ### 入园码信息 \n\n> 入园码颜色=${color}#if(${color}=='red')(高危)#end | 原因=测试";
        Map<String, Object> data = new HashMap<>();
        data.put("reportType" , "摄像头");
        data.put("color" , "red");
        String result = VelocityUtil.render("g", template, data);
        System.out.println("==========result==========\n" + result);
    }

}
