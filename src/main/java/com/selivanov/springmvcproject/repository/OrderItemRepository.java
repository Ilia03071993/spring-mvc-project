package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderItemRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public OrderItemRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<OrderItem> getAllOrderItems() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<OrderItem> orderItemList = entityManager
                    .createQuery("from OrderItem", OrderItem.class)
                    .getResultList();

            entityManager.getTransaction().commit();
            return orderItemList;
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

    public Optional<OrderItem> getOrderItemById(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            OrderItem orderItem = entityManager.find(OrderItem.class, id);

            entityManager.getTransaction().commit();
            return Optional.ofNullable(orderItem);
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

    public void saveOrderItem(OrderItem orderItem) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(orderItem);

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

    public void updateOrderItem(OrderItem orderItem, Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            OrderItem updateOrderItem = entityManager.find(OrderItem.class, id);

            BigDecimal totalPrice = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getAmount()));

            if (updateOrderItem != null) {
                updateOrderItem.setPrice(orderItem.getPrice());
                updateOrderItem.setAmount(orderItem.getAmount());
                updateOrderItem.setTotalPrice(totalPrice);
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
    public List<OrderItem> getAllProductsByOrderItemId(Integer orderId) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<OrderItem> orders = entityManager
                    .createQuery("""
                                      select o.products from OrderItem o where o.id = :id
                            """, OrderItem.class)
                    .setParameter("id", orderId)
                    .getResultList();

            entityManager.getTransaction().commit();
            return orders;
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

    public void removeOrderItem(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            OrderItem deleteOrderItem = entityManager.find(OrderItem.class, id);
            if (deleteOrderItem != null) {
                entityManager.remove(deleteOrderItem);
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
