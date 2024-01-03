package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Client;
import com.selivanov.springmvcproject.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public OrderRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Order> getAllOrders() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<Order> clients = entityManager
                    .createQuery("from Order", Order.class)
                    .getResultList();

            entityManager.getTransaction().commit();
            return clients;
        } catch (Exception ex) {
            if (entityManager != null) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(ex);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public Optional<Order> getOrderById(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Order order = entityManager.find(Order.class, id);

            entityManager.getTransaction().commit();
            return Optional.ofNullable(order);
        } catch (Exception ex) {
            if (entityManager != null) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(ex);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public void saveOrder(Order order) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(order);

            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            if (entityManager != null) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(ex);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public void updateOrder(Order order, Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Order updateOrder = entityManager.find(Order.class, id);

            BigDecimal totalPrice = order.getPrice()
                    .multiply(BigDecimal.valueOf(order.getAmount()));

            if (updateOrder != null) {
                updateOrder.setProduct(order.getProduct());
                updateOrder.setPrice(order.getPrice());
                updateOrder.setAmount(order.getAmount());
                updateOrder.setTotalPrice(totalPrice);
            }

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager != null) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(ex);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public void removeOrder(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Order deleteOrder = entityManager.find(Order.class, id);
            if (deleteOrder != null) {
                entityManager.remove(deleteOrder);
            }

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager != null) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(ex);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}
