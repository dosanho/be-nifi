package com.telsoft.libcore.service;

import com.telsoft.libcore.message.Message;
import com.telsoft.libcore.repo.BaseRepo;
import com.telsoft.libcore.util.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Locale;


public abstract class AbstractCRUDService<Bean, ID, Repo extends BaseRepo<Bean, ID>> implements CRUDService<Bean, ID, Repo>, Loggable {

    @Autowired
    protected Repo repository;

    @Autowired
    Message message;

    public String getMessage(String key) {
        return message.getMessage(key);
    }

    public String getMessageUS(String key) {
        return message.getMessageUS(key);
    }

    public String getMessageVN(String key) {
        return message.getMessageVN(key);
    }

    public String getMessage(String key, Locale locale) {
        return message.getMessage(key, locale);
    }

    @Override
    public void refreshBean(Bean bean) {
        repository.refresh(bean);
    }

    @Override
    public Bean insert(Bean bean, ID id) throws Exception {
        return repository.insert(bean, id);
    }

    @Override
    public boolean insertBean(Bean bean, ID id) throws Exception {
        try {
            if (repository.insert(bean, id) != null) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    @Override
    public Bean save(Bean bean) throws Exception {
        return repository.save(bean);
    }

    @Override
    public Iterable<Bean> saveAll(Iterable<Bean> beanIterable) throws Exception {
        return repository.saveAll(beanIterable);
    }

    @Override
    public boolean saveBean(Bean bean) {
        try {
            if (repository.save(bean) != null) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    @Override
    public boolean saveAllBeans(List<Bean> beans) {
        try {
            if (repository.saveAll(beans).size() == beans.size()) {
                return true;
            }
        } catch (Exception e) {
            getLogger().error("saveAll2", e);
        }
        return false;
    }

    @Override
    public boolean deleteById2(ID id) {
        try {
            repository.deleteById(id);
            return true;

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean deleteBean(Bean bean) {
        try {
            repository.delete(bean);
            return true;

        } catch (Exception e) {
            getLogger().error("delete2", e);
            return false;
        }
    }

    @Override
    public boolean deleteAllBeans(List<? extends Bean> beans) {
        try {
            repository.deleteAll(beans);
            return true;
        } catch (Exception e) {
            getLogger().error("deleteAll2", e);
            return false;
        }
    }

    @Override
    public boolean deleteAll2() {
        try {
            repository.deleteAll();
            return true;
        } catch (Exception e) {
            getLogger().error("deleteAll2", e);
            return false;
        }
    }


    @Override
    public Bean findById(ID id) throws Exception {
        return repository.findById(id).get();
    }

    @Override
    public Bean findByIdSilently(ID id) throws Exception {
        try {
            return repository.findById(id).get();
        } catch (Exception e) {
            if (!e.getMessage().contains("No value present")) {
                throw e;
            }
        }

        return null;
    }

    @Override
    public List<Bean> findByObject(Bean bean) throws Exception {
        Example<Bean> example = Example.of(bean, ExampleMatcher.matching());
        return repository.findAll(example);
    }

    @Override
    public Page<Bean> findByObject(Bean bean, Pageable pageable) throws Exception {
        Example<Bean> example = Example.of(bean, ExampleMatcher.matching());
        return repository.findAll(example, pageable);
    }

    @Override
    public boolean existsById(ID id) throws Exception {
        return repository.existsById(id);
    }

    @Override
    public List<Bean> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<Bean> findAll(Pageable pageable) throws Exception {
        return repository.findAll(pageable);
    }

    @Override
    public List<Bean> findAllById(List<ID> idIterable) throws Exception {
        return repository.findAllById(idIterable);
    }

    @Override
    public long count() throws Exception {
        return repository.count();
    }

    @Override
    public void deleteById(ID id) throws Exception {
        repository.deleteById(id);

    }

    @Override
    public void delete(Bean bean) throws Exception {
        repository.delete(bean);

    }

    @Override
    public void deleteAll(Iterable<? extends Bean> beans) throws Exception {
        repository.deleteAll(beans);
    }

    @Override
    public void deleteAll() throws Exception {
        repository.deleteAll();

    }

    @Override
    public Bean findOne(Specification<Bean> beanSpecification) throws Exception {
        return repository.findOne(beanSpecification).get();
    }

    @Override
    public List<Bean> findAll(Specification<Bean> beanSpecification) throws Exception {
        return repository.findAll(beanSpecification);
    }

    @Override
    public Page<Bean> findAll(Specification<Bean> beanSpecification, Pageable pageable) throws Exception {
        return repository.findAll(beanSpecification, pageable);
    }

    @Override
    public List<Bean> findAll(Specification<Bean> beanSpecification, Sort sort) throws Exception {
        return repository.findAll(beanSpecification, sort);
    }

    @Override
    public long count(Specification<Bean> beanSpecification) throws Exception {
        return repository.count(beanSpecification);
    }

    @Override
    public List<Bean> findAll(Sort sort) {
        return repository.findAll(sort);
    }
}
