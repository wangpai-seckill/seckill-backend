package org.wangpai.seckill.center.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.wangpai.seckill.persistence.origin.domain.Product;
import org.wangpai.seckill.persistence.origin.mapper.ProductMapper;

/**
 * @since 2022-2-27
 */
@Service
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product getProduct(String productId) {
        return this.productMapper.search(productId);
    }

    /**
     * @since 2022-2-27
     */
    public List<Product> getProducts() {
        return null; // 敬请期待
    }
}
