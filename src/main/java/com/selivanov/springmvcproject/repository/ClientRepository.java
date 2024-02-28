package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Client;
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
public class ClientRepository {
    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public ClientRepository(EntityManagerFactory entityManagerFactory, SessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    public List<Client> getAllClients() {
        Function<Session, List<Client>> getAllClients = (session) -> {
            return session
                    .createQuery("from Client", Client.class)
                    .getResultList();
        };

        return executeInTransaction(getAllClients);
    }

    public Optional<Client> getClientById(Integer id) {
        Function<Session, Optional<Client>> getClient = (session) -> {
            return Optional.ofNullable(session
                    .createQuery("""
                            select c from Client c
                             left join fetch c.orders
                              where c.id = :id
                            """, Client.class)
                    .setParameter("id", id)
                    .getSingleResult());
        };

        return executeInTransaction(getClient);
    }

    public Optional<Client> getClientByName(String name) {
        Function<Session, Optional<Client>> getClient = (session) -> {
            return Optional.ofNullable(session
                    .createQuery("""
                            select c from Client c
                             left join fetch c.orders
                              where c.name = :name
                            """, Client.class)
                    .setParameter("name", name)
                    .getSingleResult());
        };

        return executeInTransaction(getClient);
    }

    public Optional<Client> getClientWithCart(String name) {
        Function<Session, Optional<Client>> getClient = (session) -> {
            return Optional.ofNullable(session
                    .createQuery("""
                            select c from Client c
                             left join fetch c.cart
                             left join fetch c.cart.cartElementList
                              where c.name = :name
                            """, Client.class)
                    .setParameter("name", name)
                    .getSingleResult());
        };

        return executeInTransaction(getClient);
    }

//    public Optional<Client> getClientWithOrder(String name) {
//        EntityManager entityManager = null;
//        try {
//            entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
//
//            Client client = entityManager
//                    .createQuery("""
//                            select c from Client c
//                             left join fetch c.orders
//                             left join fetch c.o
//                              where c.name = :name
//                            """, Client.class)
//                    .setParameter("name", name)
//                    .getSingleResult();
//
//            entityManager.getTransaction().commit();
//            return Optional.ofNullable(client);
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

    public void saveClient(Client client) {
        Consumer<Session> saveClient = (session) -> {
            if (client.getId() == null) {
                session.persist(client);
            } else {
                session.merge(client);
            }
        };

        executeInTransaction(saveClient);
    }

    public List<Order> getAllOrdersByClientName(String name) {
        Function<Session, List<Order>> getAllOrders = (session) -> {
            return session
                    .createQuery("""
                                      select c.orders from Client c where c.name = :name
                            """, Order.class)
                    .setParameter("name", name)
                    .getResultList();
        };

        return executeInTransaction(getAllOrders);
    }
// public List<Order> getAllOrdersByClientId(Integer clientId) {
//        EntityManager entityManager = null;
//        try {
//            entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
//
//            List<Order> orders = entityManager
//                    .createQuery("""
//                                      select c.orders from Client c where c.id = :id
//                            """, Order.class)
//                    .setParameter("id", clientId)
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