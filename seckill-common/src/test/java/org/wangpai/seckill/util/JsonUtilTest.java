package org.wangpai.seckill.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

class JsonUtilTest {

    @Test
    void pojo2Json() throws JsonProcessingException {
        System.out.println(JsonUtil.pojo2Json(null));
    }

    @Test
    void json2Pojo() throws JsonProcessingException {
        System.out.println(JsonUtil.json2Pojo(null, JsonUtil.class));
    }
}