package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Client;
import com.selivanov.springmvcproject.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public ClientRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Client> getAllClients() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<Client> clients = entityManager
                    .createQuery("from Client", Client.class)
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

    public Optional<Client> getClientById(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Client client = entityManager
                    .createQuery("""
                            select c from Client c
                             left join fetch c.orders
                              where c.id = :id
                            """, Client.class)
                    .setParameter("id", id)
                    .getSingleResult();

            entityManager.getTransaction().commit();
            return Optional.ofNullable(client);
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

    public Optional<Client> getClientByName(String name) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Client client = entityManager
                    .createQuery("""
                            select c from Client c
                             left join fetch c.orders
                              where c.name = :name
                            """, Client.class)
                    .setParameter("name", name)
                    .getSingleResult();

            entityManager.getTransaction().commit();
            return Optional.ofNullable(client);
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
    public Optional<Client> getClientWithCart(String name) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Client client = entityManager
                    .createQuery("""
                            select c from Client c
                             left join fetch c.cart
                             left join fetch c.cart.cartElementList
                              where c.name = :name
                            """, Client.class)
                    .setParameter("name", name)
                    .getSingleResult();

            entityManager.getTransaction().commit();
            return Optional.ofNullable(client);
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
    public Optional<Client> getClientWithOrder(String name) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Client client = entityManager
                    .createQuery("""
                            select c from Client c
                             left join fetch c.orders
                             left join fetch c.o
                              where c.name = :name
                            """, Client.class)
                    .setParameter("name", name)
                    .getSingleResult();

            entityManager.getTransaction().commit();
            return Optional.ofNullable(client);
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
    public void saveClient(Client client) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            if (client.getId() == null) {
                entityManager.persist(client);
            } else {
                entityManager.merge(client);
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

    public List<Order> getAllOrdersByClientName(String name) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<Order> orders = entityManager
                    .createQuery("""
                                      select c.orders from Client c where c.name = :name
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
}