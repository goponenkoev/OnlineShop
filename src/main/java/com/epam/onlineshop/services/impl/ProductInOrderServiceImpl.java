package com.epam.onlineshop.services.impl;

import com.epam.onlineshop.entities.Order;
import com.epam.onlineshop.entities.ProductInOrder;
import com.epam.onlineshop.entities.User;
import com.epam.onlineshop.repository.OrderRepository;
import com.epam.onlineshop.repository.ProductInOrderRepository;
import com.epam.onlineshop.repository.ProductRepository;
import com.epam.onlineshop.services.ProductInOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.onlineshop.entities.Status.NEW;
import static com.epam.onlineshop.entities.Status.PREPAID;

@Service
@RequiredArgsConstructor
public class ProductInOrderServiceImpl implements ProductInOrderService {

    private final ProductRepository productRepository;
    private final ProductInOrderRepository productInOrderRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<ProductInOrder> findAllNewByUser(User user) {
        return productInOrderRepository.findAllNewOrderByUser(user);
    }

    @Override
    public List<ProductInOrder> findAllOrderedByUser(User user) {
        return productInOrderRepository.findAllOrderedByUser(user);
    }

    @Transactional
    @Override
    public void addOrderInCart(Long product_id, User user) {
        Optional<ProductInOrder> optionalProductInOrder = productInOrderRepository.findOneOrderInCartByUserAndProductId(product_id, user);
        if (optionalProductInOrder.isPresent()) {
            ProductInOrder productInOrder = optionalProductInOrder.get();
            productInOrder.setQuantity(productInOrder.getQuantity() + 1);
            productInOrderRepository.save(productInOrder);
        } else {
            productInOrderRepository.save(ProductInOrder.builder().order(orderRepository.save(Order.builder()
                    .status(NEW)
                    .user(user)
                    .build()))
                    .product(productRepository.getOne(product_id))
                    .quantity(1)
                    .build());
        }
    }

    @Override
    public void deleteById(Long id) {
        productInOrderRepository.deleteById(id);
    }

    @Override
    public void incrementCount(Long id) {
        Optional<ProductInOrder> optionalProduct = productInOrderRepository.findById(id);
        if (optionalProduct.isPresent()) {
            ProductInOrder product = optionalProduct.get();
            product.setQuantity(product.getQuantity() + 1);
            productInOrderRepository.save(product);
        } else {
            System.out.println("Product didn't find!");
        }

    }

    @Override
    public void decrementCount(Long id) {
        Optional<ProductInOrder> optionalProduct = productInOrderRepository.findById(id);
        if (optionalProduct.isPresent()) {
            ProductInOrder product = optionalProduct.get();
            product.setQuantity(product.getQuantity() - 1);
            productInOrderRepository.save(product);
        } else {
            System.out.println("Product didn't find!");
        }
    }

    @Override
    public Integer getCountById(Long id) {
        Optional<ProductInOrder> optionalProduct = productInOrderRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get().getQuantity();
        } else {
            System.out.println("Product didn't find!");
            return -1; // or null?
        }
    }

    @Override
    public void makeOrder(User user) {
        List<ProductInOrder> orders = productInOrderRepository.findAllNewOrderByUser(user);
        for (ProductInOrder product : orders) {
            product.getOrder().setStatus(PREPAID);
        }
        productInOrderRepository.saveAll(orders);
    }
}