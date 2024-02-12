package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.repository.CartElementRepository;
import com.selivanov.springmvcproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartElementService {
    private final CartElementRepository cartElementRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartElementService(CartElementRepository cartElementRepository, ProductRepository productRepository) {
        this.cartElementRepository = cartElementRepository;
        this.productRepository = productRepository;
    }

//    public List<CartElement> getAllCartElementsByCart(Integer id) {
//        return cartElementRepository.getAllCartElementsByCartId(id);
//
//    }

    public List<CartElement> getAllCartElements() {
        return cartElementRepository.getAllCartElements();
    }

    public CartElement getCartElement(Integer id) {
        return cartElementRepository.getCartElementById(id).orElseThrow(() -> new NoSuchElementException(
                "CartElement with id = '%d' not found".formatted(id)));
    }

    public void saveCartElement(CartElement cartElement) {
        cartElementRepository.saveCartElement(cartElement);
    }

    public void deleteCartElement(Integer id) {
        this.cartElementRepository.removeCartElement(id);
    }

    public void updateCartElement(CartElement cartElement, Integer id) {
        this.cartElementRepository.updateCartElement(cartElement, id);
    }

}
