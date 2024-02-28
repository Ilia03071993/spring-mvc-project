package com.selivanov.springmvcproject.repository;

import com.selivanov.springmvcproject.entity.Product;
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
public class ProductRepository {

    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;

    @Autowired
    public ProductRepository(EntityManagerFactory entityManagerFactory, SessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    public List<Product> getAllProducts() {
        Function<Session, List<Product>> getAllProducts = (session) -> {
            return session
                    .createQuery("from Product", Product.class)
                    .getResultList();
        };

        return executeInTransaction(getAllProducts);
    }

    public Optional<Product> getProductById(Integer id) {
        Function<Session, Optional<Product>> getProduct = (session) -> {
            return Optional.ofNullable(session
                    .createQuery("""
                            select p from Product p
                              where p.id = :id
                            """, Product.class)
                    .setParameter("id", id)
                    .getSingleResult());
        };

        return executeInTransaction(getProduct);
    }

    public void saveProduct(Product product) {
        Consumer<Session> saveProduct = (session) -> {
            if (product.getId() == null) {
                session.persist(product);
            } else {
                session.merge(product);
            }
        };

        executeInTransaction(saveProduct);
    }

    public void updateProduct(Product product, Integer id) {
        Consumer<Session> updateProduct = (session) -> {
            Product retrievedProduct = session.find(Product.class, id);
            if (retrievedProduct != null) {
                retrievedProduct.setName(product.getName());
                retrievedProduct.setCategory(product.getCategory());
                retrievedProduct.setPrice(product.getPrice());
            }
        };

        executeInTransaction(updateProduct);
    }

    public void removeProduct(Integer id) {
        Consumer<Session> removeProduct = (session) -> {
            Product retrievedProduct = session.find(Product.class, id);
            if (retrievedProduct != null) {
                session.remove(retrievedProduct);
            }
        };

        executeInTransaction(removeProduct);
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