package com.telsoft.libcore.service;

import com.telsoft.libcore.repo.BaseRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CRUDService<Bean, ID, Repo extends BaseRepo<Bean, ID>> {

    void refreshBean(Bean bean);

    Bean insert(Bean bean, ID id) throws Exception;

    boolean insertBean(Bean bean, ID id) throws Exception;

    Bean save(Bean bean) throws Exception;

    Iterable<Bean> saveAll(Iterable<Bean> beanIterable) throws Exception;

    boolean saveBean(Bean bean);

    boolean saveAllBeans(List<Bean> beans);

    boolean deleteById2(ID id);

    boolean deleteBean(Bean bean);

    boolean deleteAllBeans(List<? extends Bean> beans);

    boolean deleteAll2();

    Bean findById(ID id) throws Exception;

    Bean findByIdSilently(ID id) throws Exception;

    boolean existsById(ID id) throws Exception;

    List<Bean> findAll() throws Exception;

    List<Bean> findByObject(Bean bean) throws Exception;

    Page<Bean> findAll(Pageable pageable) throws Exception;

    Page<Bean> findByObject(Bean bean, Pageable pageable) throws Exception;

    List<Bean> findAllById(List<ID> ids) throws Exception;

    long count() throws Exception;

    void deleteById(ID id) throws Exception;

    void delete(Bean bean) throws Exception;

    void deleteAll(Iterable<? extends Bean> beans) throws Exception;

    void deleteAll() throws Exception;

    List<Bean> findAll(Sort sort);

    // using java predicate to find data
    Bean findOne(Specification<Bean> beanSpecification) throws Exception;

    List<Bean> findAll(Specification<Bean> beanSpecification) throws Exception;

    Page<Bean> findAll(Specification<Bean> beanSpecification, Pageable pageable) throws Exception;

    List<Bean> findAll(Specification<Bean> beanSpecification, Sort sort) throws Exception;

    long count(Specification<Bean> beanSpecification) throws Exception;
}
