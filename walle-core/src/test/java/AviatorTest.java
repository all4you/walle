import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author houyi
 */
public class AviatorTest {

    @Test
    public void test() {
        String condition = "userId == 2";
        boolean isMatch = true;
        Map<String, Object> data = new HashMap<>();
        data.put("userId1", 21);
        if (StrUtil.isNotBlank(condition)) {
            try {
                Object executeResult = AviatorEvaluator.execute(condition, data);
                isMatch = BooleanUtil.isTrue((Boolean) executeResult);
            } catch (Exception e) {
                e.printStackTrace();
                isMatch = false;
            }
        }
        System.out.println("isMatch=" + isMatch);
    }

}
