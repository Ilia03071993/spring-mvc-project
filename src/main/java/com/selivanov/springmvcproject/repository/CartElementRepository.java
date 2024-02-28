package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.CartElement;
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
public class CartElementRepository {

    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public CartElementRepository(EntityManagerFactory entityManagerFactory, SessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    public List<CartElement> getAllCartElements() {
        Function<Session, List<CartElement>> getCarElements = (session) -> {
            return session.createQuery("from CartElement", CartElement.class)
                    .getResultList();
        };
        return executeInTransaction(getCarElements);
    }

//    public List<CartElement> getAllCartElementsByClientName(String name) {
//        EntityManager entityManager = null;
//        try {
//            entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
//
//            List<CartElement> cartElements = entityManager
//                    .createQuery("""
//                            select ce from CartElement ce
//                            left join fetch ce.cart c
//                            left join fetch c.client cl
//                            where cl.name = :name
//                            """, CartElement.class)
//                    .setParameter("name", name)
//                    .getResultList();
//
//            entityManager.getTransaction().commit();
//            return cartElements;
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

    public Optional<CartElement> getCartElementById(Integer id) {
        Function<Session, Optional<CartElement>> getCartElement = (session) -> {
            return Optional.ofNullable(session.createQuery("""
                            select c from CartElement c
                              where c.id = :id
                            """, CartElement.class)
                    .setParameter("id", id)
                    .getSingleResult());
        };
        return executeInTransaction(getCartElement);
    }

    public void saveCartElement(CartElement cartElement) {
        Consumer<Session> saveCartElement = (session) -> {
            if (cartElement.getId() == null) {
                session.persist(cartElement);
            } else {
                session.merge(cartElement);
            }
        };
        executeInTransaction(saveCartElement);
    }

    public void updateCartElement(CartElement cartElement, Integer id) {
        Consumer<Session> updateCartElement = (session) -> {
            CartElement retrieved = session.find(CartElement.class, id);
            if (retrieved != null) {
                retrieved.setAmount(cartElement.getAmount());
                retrieved.setPrice(cartElement.getPrice());
            }
        };
        executeInTransaction(updateCartElement);
    }

    public void removeCartElement(Integer id) {
        Consumer<Session> removeCartElement = (session) -> {
            CartElement cartElement = session.find(CartElement.class, id);
            if (cartElement != null) {
                session.remove(cartElement);
            }
        };
        executeInTransaction(removeCartElement);
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