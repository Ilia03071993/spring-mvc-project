package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Cart;
import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartElementRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public CartElementRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<CartElement> getAllCartElements() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<CartElement> cartElements = entityManager
                    .createQuery("from CartElement ", CartElement.class)
                    .getResultList();

            entityManager.getTransaction().commit();
            return cartElements;
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

    public Optional<CartElement> getCartElementById(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            CartElement cartElement = entityManager
                    .createQuery("""
                            select c from CartElement c
                              where c.id = :id
                            """, CartElement.class)
                    .setParameter("id", id)
                    .getSingleResult();

            entityManager.getTransaction().commit();
            return Optional.ofNullable(cartElement);
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

    public void saveCartElement(CartElement cartElement) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            if (cartElement.getId() == null) {
                entityManager.persist(cartElement);
            } else {
                entityManager.merge(cartElement);
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
    public void updateCartElement(CartElement cartElement, Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            CartElement updateCartElement = entityManager.find(CartElement.class, id);
            if (updateCartElement != null) {
                updateCartElement.setAmount(cartElement.getAmount());
                updateCartElement.setPrice(cartElement.getPrice());
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
    public void removeCartElement(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            CartElement cartElement = entityManager.find(CartElement.class, id);
            if (cartElement != null) {
                entityManager.remove(cartElement);
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