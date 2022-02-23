package org.wangpai.seckill.center.response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import org.wangpai.seckill.util.JsonUtil;

public class RspUtil {
    public static void setResponse(HttpServletResponse response, RspMsg rspMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        var msg = JsonUtil.pojo2Json(rspMsg);
        var out = response.getOutputStream();
        out.write(msg.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
