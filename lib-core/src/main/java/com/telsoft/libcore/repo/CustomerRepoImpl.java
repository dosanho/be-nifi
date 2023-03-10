package com.telsoft.libcore.repo;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.io.Serializable;

public class CustomerRepoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomerRepo<T, ID> {

    private final EntityManager entityManager;

    public CustomerRepoImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    public void refresh(T t) {
        entityManager.refresh(t);
    }

    @Override
    @Transactional
    public T insert(T entity, ID id) {
        T exists = entityManager.find(this.getDomainClass(), id);
        if (exists == null) {
            entityManager.persist(entity);
            return entity;

        } else {
            throw (new IllegalStateException("duplicate"));
        }
    }
}
