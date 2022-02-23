package org.wangpai.seckill.persistence.complex.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.seckill.persistence.complex.daodomain.SearchRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DealDaoTest {
    @Autowired
    private DealDao dealDao;

    @Test
    void search() {
        String userPhone = "123";
        int begin = 0;
        int num = 500;
        var searchRequest = new SearchRequest()
                .setUserPhone(userPhone)
                .setNum(num);
        try {
            var orderList = this.dealDao.search(searchRequest);
            for (var order : orderList) {
                System.out.println(order);
            }
        } catch (Exception exception) {
            System.out.println("-----exception------");
            System.out.println(exception);
            System.out.println("**********exception***********");
            throw exception;
        }
    }
}