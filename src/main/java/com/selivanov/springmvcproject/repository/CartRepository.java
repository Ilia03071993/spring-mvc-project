package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Cart;
import
        com.selivanov.springmvcproject.entity.CartElement;
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
public class CartRepository {

    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public CartRepository(EntityManagerFactory entityManagerFactory, SessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    public List<Cart> getAllCart() {
        Function<Session, List<Cart>> getAllCart = (session) -> {
            return session
                    .createQuery("from Cart ", Cart.class)
                    .getResultList();
        };

        return executeInTransaction(getAllCart);
    }

    public List<CartElement> getAllCartElementsByClientName(String name) {
        Function<Session, List<CartElement>> getAllCartElements = (session) -> {
            return session
                    .createQuery("""
                            select ce from CartElement ce
                            left join fetch ce.product cep
                            left join ce.cart cec
                            where cec.client.name = :name
                            """, CartElement.class)
                    .setParameter("name", name)
                    .getResultList();
        };

        return executeInTransaction(getAllCartElements);
    }

    public Optional<Cart> getCartById(Integer id) {
        Function<Session, Optional<Cart>> getCart = (session) -> {
            return Optional.ofNullable(session
                    .createQuery("""
                            select c from CartElement c
                              where c.id = :id
                            """, Cart.class)
                    .setParameter("id", id)
                    .getSingleResult());
        };

        return executeInTransaction(getCart);
    }

    public Optional<Integer> getCartIdByClientName(String name) {
        Function<Session, Optional<Integer>> getCartId = (session) -> {
            return Optional.ofNullable(session
                    .createQuery("""
                            select c.id from Cart c
                            left join c.client cl
                            where cl.name = :name
                            """, Integer.class)
                    .setParameter("name", name)
                    .getSingleResult());
        };
        return executeInTransaction(getCartId);
    }

    //        public Integer getCartIdByClientName(String name) {
//        EntityManager entityManager = null;
//        try {
//            entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
//
//            Integer id = entityManager
//                    .createQuery("""
//                            select c.id from Cart c
//                            left join c.client cl
//                            where cl.name = :name
//                            """, Integer.class)
//                    .setParameter("name", name)
//                    .getSingleResult().getId();
//
//            entityManager.getTransaction().commit();
//            return id;
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
    public void saveCart(Cart cart) {
        Consumer<Session> saveCart = (session) -> {
            if (cart.getId() == null) {
                session.persist(cart);
            } else {
                session.merge(cart);
            }
        };
        executeInTransaction(saveCart);
    }

    public void removeCart(Integer id) {
        Consumer<Session> removeCart = (session) -> {
            Cart cart = session.find(Cart.class, id);
            if (cart != null) {
                session.remove(cart);
            }
        };
        executeInTransaction(removeCart);
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