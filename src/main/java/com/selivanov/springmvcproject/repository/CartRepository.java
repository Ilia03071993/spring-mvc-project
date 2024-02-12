package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Cart;
import
        com.selivanov.springmvcproject.entity.CartElement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class CartRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public CartRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Cart> getAllCart() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<Cart> cartElements = entityManager
                    .createQuery("from Cart ", Cart.class)
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
    public List<CartElement> getAllCartElementsByClientName(String name) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<CartElement> cartElements = entityManager
                    .createQuery("""
                            select ce from CartElement ce
                            left join fetch Product p on ce.cart.id = p.id
                            left join fetch Cart c on ce.cart.id = c.id
                            left join fetch Client cl on cl.cart.id = c.id
                            where cl.name = :name
                            """, CartElement.class)
                    .setParameter("name",name)
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
    public Optional<Cart> getCartById(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Cart cart = entityManager
                    .createQuery("""
                            select c from CartElement c
                              where c.id = :id
                            """, Cart.class)
                    .setParameter("id", id)
                    .getSingleResult();

            entityManager.getTransaction().commit();
            return Optional.ofNullable(cart);
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

    public void saveCart(Cart cart) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            if (cart.getId() == null) {
                entityManager.persist(cart);
            } else {
                entityManager.merge(cart);
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

    public void removeCart(Integer id) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Cart cart = entityManager.find(Cart.class, id);
            if (cart != null) {
                entityManager.remove(cart);
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