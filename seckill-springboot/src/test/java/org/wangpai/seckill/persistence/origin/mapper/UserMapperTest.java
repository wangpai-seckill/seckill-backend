package org.wangpai.seckill.persistence.origin.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.seckill.persistence.origin.domain.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void searchById() {
    }

    @Test
    void searchByPhone() {
    }

    @Test
    void save() {
        String id = "2";
        String phone = "123456";
        String userName = "abc";
        String password = "test";

        var user = new User()
                .setId(id)
                .setPhone(phone)
                .setUserName(userName)
                .setPassword(password);

        try {
            System.out.println("更新行数：" + this.userMapper.save(user));
        } catch (Exception exception) {
            System.out.println("-----exception------");
            System.out.println(exception);
            System.out.println("**********exception***********");
            throw exception;
        }

    }
}
