package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
public class OrderItemRepository {
    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public OrderItemRepository(EntityManagerFactory entityManagerFactory, SessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    public List<OrderItem> getAllOrderItems() {
        Function<Session, List<OrderItem>> getAllOrderItems =
                (session) -> {
                    return session.createQuery("from OrderItem", OrderItem.class)
                            .getResultList();
                };

        return executeInTransaction(getAllOrderItems);
    }

    public Optional<OrderItem> getOrderItemById(Integer id) {
        Function<Session, Optional<OrderItem>> getOrderItem = (session) -> {
            return Optional.ofNullable(session.find(OrderItem.class, id));
        };
        return executeInTransaction(getOrderItem);
    }

    public void saveOrderItem(OrderItem orderItem) {
        Consumer<Session> saveOrderItem = (session) -> {
            session.persist(orderItem);
        };
        executeInTransaction(saveOrderItem);
    }

    public void updateOrderItem(OrderItem orderItem, Integer id) {
        Consumer<Session> updateItem = (session) -> {
            OrderItem updateOrderItem = session.find(OrderItem.class, id);
            BigDecimal totalPrice = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getAmount()));

            if (updateOrderItem != null) {
                updateOrderItem.setPrice(orderItem.getPrice());
                updateOrderItem.setAmount(orderItem.getAmount());
                updateOrderItem.setTotalPrice(totalPrice);
            }
        };

        executeInTransaction(updateItem);
    }

    public List<OrderItem> getAllProductsByOrderItemId(Integer orderId) {
        Function<Session, List<OrderItem>> getAllProducts = (session) -> {
            return session.createQuery("select o.product from OrderItem o where o.id = :id", OrderItem.class)
                    .setParameter("id", orderId)
                    .getResultList();
        };

        return executeInTransaction(getAllProducts);
    }

    public BigDecimal totalPrice(String name) {
        Function<Session, BigDecimal> totalPrice =
                (session -> {
                    return session.createQuery("""
                                               select sum(o.price) from OrderItem o
                                               left join o.order or
                                              left join or.client c
                                              where c.name = :name
                                    """, BigDecimal.class)
                            .setParameter("name", name)
                            .getSingleResult();
                });
        return executeInTransaction(totalPrice);
    }

    public void removeOrderItem(Integer id) {
        Consumer<Session> deleteOrderItem = (session -> {
            OrderItem orderItem = session.find(OrderItem.class, id);
            if (orderItem != null) {
                session.remove(orderItem);
            }
        });
    }

    private void executeInTransaction(Consumer<Session> consumer) {
        Function<Session, Void> func = (session -> {
            consumer.accept(session);
            return null;
        });
        executeInTransaction(func);
    }

    private <T> T executeInTransaction(Function<Session, T> func) {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            session.getTransaction().begin();

            T result = func.apply(session);

            session.getTransaction().commit();

            return result;
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback(); // 0/4 - success
            }
            throw new RuntimeException(ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}