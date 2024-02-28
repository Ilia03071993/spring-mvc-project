package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
public class OrderRepository {
    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public OrderRepository(EntityManagerFactory entityManagerFactory, SessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    public List<Order> getAllOrdersByClientName(String name) {
        Function<Session, List<Order>> getAllOrders = (session) -> {
            return session
                    .createQuery("""
                            from Order o
                            left join fetch o.client c
                            left join fetch o.orderItemList
                            where c.name = :name
                                    """, Order.class)
                    .setParameter("name", name)
                    .getResultList();
        };

        return executeInTransaction(getAllOrders);
    }

    public List<Order> getAllOrders() {
        Function<Session, List<Order>> getAllOrders = (session) -> {
            return session
                    .createQuery("from Order", Order.class)
                    .getResultList();
        };

        return executeInTransaction(getAllOrders);
    }

    public Optional<Order> getOrderById(Integer id) {
        Function<Session, Optional<Order>> getOrder = (session) -> {
            return Optional.ofNullable(session.find(Order.class, id));
        };

        return executeInTransaction(getOrder);
    }

    public void saveOrder(Order order) {
        Consumer<Session> saveOrder = (session) -> {
            session.persist(order);
        };

        executeInTransaction(saveOrder);
    }

    public void updateOrder(Order order, Integer id) {
        Consumer<Session> updateOrder = (session) -> {
            Order retrievedOrder = session.find(Order.class, id);

            if (retrievedOrder != null) {
                retrievedOrder.setStreet(order.getStreet());
            }
        };

        executeInTransaction(updateOrder);
    }

//    public List<Order> getAllProductsByOrderId(Integer orderId) {
//        EntityManager entityManager = null;
//        try {
//            entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
//
//            List<Order> orders = entityManager
//                    .createQuery("""
//                                      select o.products from Order o where o.id = :id
//                            """, Order.class)
//                    .setParameter("id", orderId)
//                    .getResultList();
//
//            entityManager.getTransaction().commit();
//            return orders;
//        } catch (Exception ex) {
//            if (entityManager != null) {
//                entityManager.getTransaction().rollback();
//            }
//            throw new RuntimeException(ex);
//        } finally {
//            if (entityManager != null) {
//                entityManager.close();
//            }
//        }
//    }

    public void removeOrder(Integer id) {
        Consumer<Session> removeOrder = (session) -> {
            Order order = session.find(Order.class, id);
            if (order != null) {
                session.remove(order);
            }
        };

        executeInTransaction(removeOrder);
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