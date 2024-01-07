package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.Product;
import com.selivanov.springmvcproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Product getProduct(Integer id) {
        return productRepository.getProductById(id).orElseThrow(() -> new NoSuchElementException(
                "Product with id = '%d' not found".formatted(id)));
    }

    public void saveProduct(Product product) {
        productRepository.saveProduct(product);
    }
    public void deleteProduct(Integer id){
        this.productRepository.removeProduct(id);
    }
    public void updateProduct (Product product,Integer id){
        this.productRepository.updateProduct(product,id);
    }

}
