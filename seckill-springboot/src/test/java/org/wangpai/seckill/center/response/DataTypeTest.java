package org.wangpai.seckill.center.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.wangpai.seckill.util.JsonUtil;

public class DataTypeTest {
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(JsonUtil.pojo2Json(DataType.STRING));
        System.out.println(JsonUtil.pojo2Json(DataType.STRING.name()));
        System.out.println(JsonUtil.pojo2Json(DataType.class));
    }

}

