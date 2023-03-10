package com.telsoft.libcore.controller;

import com.telsoft.libcore.constant.ResultCode;
import com.telsoft.libcore.message.Message;
import com.telsoft.libcore.message.ResponseMsg;
import com.telsoft.libcore.repo.BaseRepo;
import com.telsoft.libcore.service.CRUDService;
import com.telsoft.libcore.util.Loggable;
import com.telsoft.libcore.util.Utils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public abstract class BaseController<Bean, ID, Repo extends BaseRepo<Bean, ID>, Service extends CRUDService<Bean,ID, Repo>> implements Loggable {
    protected static int MAX_PAGE_RESULT = 100000;
    protected static String PAR_TYPE_EXCLUDE_FIELDS = "EXCLUDE_FIELDS";
    protected static String PAR_INTERNAL_REQUEST_PWD = "SYSTEM.INTERNAL_REQUEST_PWD";

    protected Class<Bean> clazz = (Class<Bean>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Autowired
    protected Service service;

    @Autowired
    protected Message message;

//    @Autowired
//    protected HazelcastInstance hazelcastInstance;

    @Autowired
    protected HttpServletRequest request;

//    @Autowired
//    protected ValidationEntityService validationEntityService;

    public abstract String getBeanName();

    public void filterFields(List<Bean> listBean) throws Exception {
        // Get include fields
//        Map includeFields = Utils.splitStringToMap(request.getParameter("includeFields"), ",");
//
//        // Get exclude fields
//        Map excludeFields = new HashMap();
//        String internalRequestPwd = Utils.fixNull(request.getHeader("Internal-Request-Pwd"));
//        if (!internalRequestPwd.equals(hazelcastInstance.getMap(Const.CACHE_CATALOG.ET_AP_PARAM_CODE).get(PAR_INTERNAL_REQUEST_PWD))) {
//            // Get fields
//            String strExcludeFields = (String) hazelcastInstance.getMap(Const.CACHE_CATALOG.ET_AP_PARAM_CODE).get(PAR_TYPE_EXCLUDE_FIELDS + "." + clazz.getSimpleName());
//            excludeFields = Utils.splitStringToMap(strExcludeFields, ",");
//        }
//
//        if (!includeFields.isEmpty() || !excludeFields.isEmpty()) {
//            Field[] allFields = FieldUtils.getAllFields(clazz);
//            for (Bean bean : listBean) {
//                for (Field f : allFields) {
//                    if ((!includeFields.isEmpty() && !includeFields.containsKey(f.getName())) || (!excludeFields.isEmpty() && excludeFields.containsKey(f.getName()))) {
//                        f.setAccessible(true);
//                        f.set(bean, null);
//                    }
//                }
//            }
//        }
    }

    public void enrichResult(List<Bean> listBean) throws Exception {
    }

    /**
     * Validate model.
     *
     * @param object the object
     * @throws Exception the exception
     */
    public void validateModel(Object object) throws Exception {
//        validationEntityService.validateModel(object);
    }

    /**
     * Validate model recursive.
     *
     * @param object the object
     * @throws Exception the exception
     */
    public void validateModelRecursive(Object object) throws Exception {
//        validationEntityService.validateModel(object, true);
    }

    /**
     * Create response msg.
     *
     * @param id   the id
     * @param bean the bean
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg create(Bean bean, ID id) throws Exception {
        try {
            if (id != null && getService().existsById(id)) {
                return ResponseMsg.newResponse(ResultCode.OBJECT_EXISTED);
            } else {
                if (getService().insertBean(bean, id)) {
                    filterFields(Collections.singletonList(bean));
                    enrichResult(Collections.singletonList(bean));
                    return ResponseMsg.newOKResponse(bean.getClass().getSimpleName(), bean);
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Create new bean.
     *
     * @param id   the id
     * @param bean the bean
     * @return the bean
     */
    @SneakyThrows
    public Bean createNew(ID id, Bean bean) {
        try {
            if (id != null && getService().existsById(id)) {
                throw new Exception(String.valueOf(ResultCode.OBJECT_EXISTED));
            } else {
                if (getService().saveBean(bean)) {
                    filterFields(Collections.singletonList(bean));
                    enrichResult(Collections.singletonList(bean));
                    return bean;
                } else {
                    throw new Exception(String.valueOf(ResultCode.INTERNAL_SERVER_ERROR));
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Update new bean.
     *
     * @param id      the id
     * @param bean    the bean
     * @param oldBean the old bean
     * @return the bean
     * @throws Exception the exception
     */
    public Bean updateNew(ID id, Bean bean, Bean oldBean) throws Exception {
        if (id != null && getService().existsById(id)) {
            Bean currentBean = getService().findById(id);
            merge(currentBean, oldBean);
            merge(bean, currentBean);
            getService().saveBean(currentBean);

            filterFields(Collections.singletonList(currentBean));
            enrichResult(Collections.singletonList(currentBean));
            return currentBean;

        } else {
            throw new Exception(String.valueOf(ResultCode.OBJECT_NOT_EXIST));
        }
    }


    /**
     * Create new bean.
     *
     * @param bean the bean
     * @return the bean
     */
    @SneakyThrows
    public Bean createNew(Bean bean) {
        if (service.saveBean(bean)) {
            filterFields(Collections.singletonList(bean));
            enrichResult(Collections.singletonList(bean));
            return bean;
        } else {
            throw new Exception(String.valueOf(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Delete new response msg.
     *
     * @param id   the id
     * @param bean the bean
     * @return the response msg
     */
    @SneakyThrows
    public ResponseMsg deleteNew(ID id, Bean bean) {
        try {
            if (id != null && getService().existsById(id)) {
                Bean beanCurrent = getService().findById(id);
                merge(beanCurrent, bean);
                if (getService().deleteById2(id)) {
                    return ResponseMsg.newOKResponse();
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }
            } else {
                return ResponseMsg.newResponse(ResultCode.OBJECT_NOT_EXIST);
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Create response msg.
     *
     * @param bean          the bean
     * @param noRefreshBean the no refresh bean
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg create(Bean bean, boolean noRefreshBean) throws Exception {
        if (service.saveBean(bean)) {
            if (!noRefreshBean) {
                service.refreshBean(bean);    // exception: No row with the given identifier exists: [this instance does not yet exist as a row in the database#150]
            }
            filterFields(Collections.singletonList(bean));
            enrichResult(Collections.singletonList(bean));
            return ResponseMsg.newOKResponse(bean.getClass().getSimpleName(), bean);
        } else {
            return ResponseMsg.new500ErrorResponse();
        }
    }

    /**
     * Create response msg.
     *
     * @param bean the bean
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg create(Bean bean) throws Exception {
        return create(bean, false);
    }

    /**
     * Hidden response msg.
     *
     * @param id the id
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg hidden(ID id) throws Exception {
        return hidden(id, "status");
    }

    /**
     * Hidden response msg.
     *
     * @param id          the id
     * @param statusField the status field
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg hidden(ID id, String statusField) throws Exception {
        try {
            if (id != null && getService().existsById(id)) {
                Bean bean = getService().findById(id);

                Field[] allFields = FieldUtils.getAllFields(bean.getClass());
                for (Field field : allFields) {
                    if (field.getName().equals(statusField)) {
                        field.setAccessible(true);
                        if (field.getType().equals(Long.class)) {
                            field.set(bean, (long) -2);
                        } else if (field.getType().equals(Integer.class)) {
                            field.set(bean, -2);
                        } else if (field.getType().equals(String.class)) {
                            field.set(bean, "-2");
                        }
                        break;
                    }
                }

                if (getService().saveBean(bean)) {
                    return ResponseMsg.newOKResponse();
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }

            } else {
                return ResponseMsg.newResponse(ResultCode.OBJECT_NOT_EXIST);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Delete response msg.
     *
     * @param id the id
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg delete(ID id) throws Exception {
        try {
            if (id != null && getService().existsById(id)) {
                if (getService().deleteById2(id)) {
                    return ResponseMsg.newOKResponse();
                } else {
                    return ResponseMsg.new500ErrorResponse();
                }
            } else {
                return ResponseMsg.newResponse(ResultCode.OBJECT_NOT_EXIST);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Find all response msg.
     *
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg findAll() throws Exception {
        List<Bean> list = getService().findAll();
        filterFields(list);
        enrichResult(list);
        return ResponseMsg.newOKResponse(getBeanName(), list);
    }

    /**
     * Find all response msg.
     *
     * @param pageable the pageable
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg findAll(Pageable pageable) throws Exception {
        pageable = fixPageable(pageable);

        Page<Bean> page = getService().findAll(pageable);
        List<Bean> list = new ArrayList<>(page.getContent());

        filterFields(list);
        enrichResult(list);
        page = new PageImpl<>(list, pageable, page.getTotalElements());

        return ResponseMsg.newOKResponse(getBeanName(), page);
    }

    /**
     * Find all response msg.
     *
     * @param beanSpecification the bean specification
     * @param pageable          the pageable
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg findAll(Specification<Bean> beanSpecification, Pageable pageable) throws Exception {
        pageable = fixPageable(pageable);

        Page<Bean> page = getService().findAll(beanSpecification, pageable);
        List<Bean> list = new ArrayList<>(page.getContent());

        filterFields(list);
        enrichResult(list);
        page = new PageImpl<>(list, pageable, page.getTotalElements());

        return ResponseMsg.newOKResponse(getBeanName(), page);
    }

    /**
     * Find all response msg.
     *
     * @param beanSpecification the bean specification
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg findAll(Specification<Bean> beanSpecification) throws Exception {
        List<Bean> list = getService().findAll(beanSpecification);
        filterFields(list);
        enrichResult(list);
        return ResponseMsg.newOKResponse(getBeanName(), list);
    }

    /**
     * Find all response msg.
     *
     * @param sort the sort
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg findAll(Sort sort) throws Exception {
        List<Bean> list = getService().findAll(sort);
        filterFields(list);
        enrichResult(list);
        return ResponseMsg.newOKResponse(getBeanName(), list);
    }

    /**
     * Find by object response msg.
     *
     * @param bean the bean
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg findByObject(Bean bean) throws Exception {
        List<Bean> list = getService().findByObject(bean);
        filterFields(list);
        enrichResult(list);
        return ResponseMsg.newOKResponse(getBeanName(), list);
    }

    /**
     * Find by object response msg.
     *
     * @param bean     the bean
     * @param pageable the pageable
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg findByObject(Bean bean, Pageable pageable) throws Exception {
        Page<Bean> page = getService().findByObject(bean, fixPageable(pageable));
        filterFields(page.getContent());
        enrichResult(page.getContent());
        return ResponseMsg.newOKResponse(getBeanName(), page);
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     * @throws Exception the exception
     */
    public ResponseMsg getById(ID id) throws Exception {
        Bean ett = getService().findById(id);
        filterFields(Collections.singletonList(ett));
        enrichResult(Collections.singletonList(ett));
        return ResponseMsg.newOKResponse(getBeanName(), ett);
    }

    /**
     * Merge.
     *
     * @param newBean     the new bean
     * @param currentBean the current bean
     * @throws Exception the exception
     */
    public abstract void merge(Bean newBean, Bean currentBean) throws Exception;

    /**
     * Update response msg.
     *
     * @param id   the id
     * @param bean the bean
     * @return the response msg
     * @throws Exception the exception
     */
    public ResponseMsg update(ID id, Bean bean) throws Exception {
        if (id != null && getService().existsById(id)) {
            Bean currentBean = getService().findById(id);
            merge(bean, currentBean);
            if (getService().saveBean(currentBean)) {
                service.refreshBean(currentBean);
                filterFields(Collections.singletonList(currentBean));
                enrichResult(Collections.singletonList(currentBean));
                return ResponseMsg.newOKResponse(currentBean.getClass().getSimpleName(), currentBean);
            } else {
                return ResponseMsg.new500ErrorResponse();
            }
        } else {
            return ResponseMsg.newResponse(ResultCode.OBJECT_NOT_EXIST);
        }
    }

    /**
     * Gets token.
     *
     * @return the token
     * @throws Exception the exception
     */
//    protected String getToken() throws Exception {
//        return Utils.getToken(request);
//    }

    /**
     * Gets cache.
     *
     * @param <T>      the type parameter
     * @param cacheKey the cache key
     * @param dataKey  the data key
     * @param clazz    the clazz
     * @return the cache
     */
//    protected <T> T getCache(String cacheKey, String dataKey, Class clazz) {
//        return (T) Utils.getCache(hazelcastInstance, cacheKey, dataKey, clazz);
//    }

    /**
     * Gets cache.
     *
     * @param cacheKey the cache key
     * @param dataKey  the data key
     * @return the cache
     */
//    protected String getCache(String cacheKey, String dataKey) {
//        if (!Utils.isNullOrEmpty(cacheKey)) {
//            return (String) hazelcastInstance.getMap(dataKey).get(cacheKey);
//        }
//        return null;
//    }

    /**
     * Gets cache ap domain.
     *
     * @param type the type
     * @param code the code
     * @return the cache ap domain
     */
//    protected String getCacheApDomain(String type, String code) {
//        if (!Utils.isNullOrEmpty(type) && !Utils.isNullOrEmpty(code)) {
//            return (String) hazelcastInstance.getMap(Const.CACHE_CATALOG.ET_AP_DOMAIN_CODE).get(type + "_" + code);
//        }
//        return null;
//    }

    /**
     * Gets curr timestamp.
     *
     * @return the curr timestamp
     */
    protected Timestamp getCurrTimestamp() {
        java.util.Date date = new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * Gets curr sql timestamp.
     *
     * @return the curr sql timestamp
     */
    protected Timestamp getCurrSqlTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp;
    }

    protected Pageable fixPageable(Pageable pageable) throws Exception {
        return pageable != null ? pageable : PageRequest.of(0, MAX_PAGE_RESULT);
    }

    /**
     * Getters, Setters
     */
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
