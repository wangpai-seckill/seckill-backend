package org.wangpai.seckill.persistence.origin.mapper;

import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.seckill.persistence.origin.domain.Product;
import org.wangpai.seckill.resourcesread.ResourcesRead;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProductMapperTest {
    @Autowired
    private ProductMapper productMapper;

    @Test
    void search() {
    }

    @Test
    void save() throws IOException {
        try {
            this.saveFishing();
            this.saveHelpingChildren();
            this.saveLuckyBox();
        } catch (Exception exception) {
            System.out.println("-----exception------");
            System.out.println(exception);
            System.out.println("**********exception***********");
            throw exception;
        }
    }

    @Test
    void save_normal() {
        String id = "1";
        byte[] image = null;
        String description = "test";
        int price = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        int inventoryQuantity = 123456;

        var product = new Product()
                .setId(id)
                .setTitle("abc")
                .setImage(image)
                .setDescription(description)
                .setPrice(price)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setInventoryQuantity(inventoryQuantity);

        try {
            System.out.println("更新行数：" + this.productMapper.save(product));
        } catch (Exception exception) {
            System.out.println("-----exception------");
            System.out.println(exception);
            System.out.println("**********exception***********");
            throw exception;
        }
    }

    public void saveFishing() throws IOException {
        String id = "1";
        String title = "吃亏上当";
        byte[] image = ResourcesRead.readImage2ByteArray("img/pic/product_fishing-gif.gif");
        String description = "谁说一块钱你买不了吃亏，买不了上当。一块钱，愿者上钩。早买教训，早成长";
        int price = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        int inventoryQuantity = 0;

        var product = new Product()
                .setId(id)
                .setTitle(title)
                .setImage(image)
                .setDescription(description)
                .setPrice(price)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setInventoryQuantity(inventoryQuantity);

        try {
            System.out.println("更新行数：" + this.productMapper.save(product));
        } catch (Exception exception) {
            System.out.println("-----exception------");
            System.out.println(exception);
            System.out.println("**********exception***********");
            throw exception;
        }
    }

    public void saveHelpingChildren() throws IOException {
        String id = "2";
        String title = "世界上最贵的东西";
        byte[] image = ResourcesRead.readImage2ByteArray("img/pic/product_helpingChildren-1.png");
        String description = "聚沙成塔，滴水成泉，您的每一次行动、每一份爱心，都在改变着他们的生活";
        int price = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        int inventoryQuantity = 10000;

        var product = new Product()
                .setId(id)
                .setTitle(title)
                .setImage(image)
                .setDescription(description)
                .setPrice(price)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setInventoryQuantity(inventoryQuantity);

        try {
            System.out.println("更新行数：" + this.productMapper.save(product));
        } catch (Exception exception) {
            System.out.println("-----exception------");
            System.out.println(exception);
            System.out.println("**********exception***********");
            throw exception;
        }
    }

    public void saveLuckyBox() throws IOException {
        String id = "3";
        String title = "幸运盲盒";
        byte[] image = ResourcesRead.readImage2ByteArray("img/pic/product_luckyBox-1.jpg");
        String description = "你想体验怦然心动的感觉吗";
        int price = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        int inventoryQuantity = 99;

        var product = new Product()
                .setId(id)
                .setTitle(title)
                .setImage(image)
                .setDescription(description)
                .setPrice(price)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setInventoryQuantity(inventoryQuantity);

        try {
            System.out.println("更新行数：" + this.productMapper.save(product));
        } catch (Exception exception) {
            System.out.println("-----exception------");
            System.out.println(exception);
            System.out.println("**********exception***********");
            throw exception;
        }
    }
}
