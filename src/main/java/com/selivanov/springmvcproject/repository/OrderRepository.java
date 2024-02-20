package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public OrderRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Order> getAllOrdersByClientName(String name) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<Order> orders = entityManager
                    .createQuery("""
                            from Order o
                            left join fetch o.client c
                            left join fetch o.orderItemList
                            where c.name = :name
                                    """, Order.class)
                    .setParameter("name", name)
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

    public List<Order> getAllOrders() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<Order> orders = entityManager
                    .createQuery("from Order", Order.class)
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

            if (updateOrder != null) {
                updateOrder.setStreet(order.getStreet());
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
