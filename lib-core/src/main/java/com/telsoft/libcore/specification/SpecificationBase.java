package com.telsoft.libcore.specification;

import com.telsoft.libcore.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SpecificationBase<U> implements Specification<U> {
    private SpecSearchCriteria criteria;

    public SpecificationBase(final SpecSearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public SpecSearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(final Root<U> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        boolean isNullValue = criteria.getValue() == null || criteria.getValue().toString().trim().equals("''");
        if (criteria.getValue() != null && criteria.getValue() instanceof String) {
            criteria.setValue(criteria.getValue().toString().replace("@sp@", " "));
        }

        switch (criteria.getOperation()) {
            case EQUALITY:
                if (isNullValue) {
                    return builder.isNull(root.get(criteria.getKey()));
                }

                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    return builder.equal(root.get(criteria.getKey()).as(Timestamp.class), criteria.getValue());
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
            case NEGATION:
                if (isNullValue) {
                    return builder.isNotNull(root.get(criteria.getKey()));
                }

                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    return builder.notEqual(root.get(criteria.getKey()).as(Timestamp.class), criteria.getValue());
                } else {
                    return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
                }
            case GREATER_THAN:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    return builder.greaterThan(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                } else {
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case GREATER_THAN_EQUALS:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                } else {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    return builder.lessThan(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                } else {
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN_EQUALS:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                } else {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LIKE:
                return builder.like(builder.upper(root.get(criteria.getKey())), criteria.getValue().toString().toUpperCase());
            case STARTS_WITH:
                return builder.like(builder.upper(root.get(criteria.getKey())), criteria.getValue().toString().toUpperCase() + "%");
            case NOT_STARTS_WITH:
                return builder.notLike(builder.upper(root.get(criteria.getKey())), criteria.getValue().toString().toUpperCase() + "%");
            case ENDS_WITH:
                return builder.like(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase());
            case NOT_ENDS_WITH:
                return builder.notLike(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase());
            case CONTAINS:
                return builder.like(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase() + "%");
            case NOT_CONTAINS:
                return builder.notLike(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase() + "%");
            case IN:
            case NOT_IN:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    Date endDate = DateUtil.addDay((Date) criteria.getValue(), 1);
                    Predicate p1, p2;
                    if (criteria.getOperation().equals(SearchOperation.IN)) {
                        p1 = builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                        p2 = builder.lessThan(root.get(criteria.getKey()).as(Timestamp.class), endDate);
                        return builder.and(p1, p2);
                    } else {
                        p1 = builder.lessThan(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                        p2 = builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), endDate);
                        return builder.or(p1, p2);
                    }

                } else {
                    String[] listString = StringUtils.split(criteria.getValue().toString(), ',');
                    if (listString.length != 0) {
                        List listIn = new ArrayList();
                        for (String s : listString) {
                            if (root.get(criteria.getKey()).getJavaType().equals(String.class)) {
                                listIn.add(s);
                            } else if (root.get(criteria.getKey()).getJavaType().equals(Long.class)) {
                                listIn.add(new Long(s));
                            } else if (root.get(criteria.getKey()).getJavaType().equals(Integer.class)) {
                                listIn.add(new Integer(s));
                            }
                        }

                        if (criteria.getOperation().equals(SearchOperation.IN)) {
                            return builder.in(root.get(criteria.getKey())).value(castToRequiredType(root.get(criteria.getKey()).getJavaType(), listIn));
                        } else {
                            return builder.in(root.get(criteria.getKey())).value(castToRequiredType(root.get(criteria.getKey()).getJavaType(), listIn)).not();
                        }
                    }
                    return null;
                }

            default:
                return null;
        }
    }

    private Object castToRequiredType(Class fieldType, Object value) {
        List<Object> lists = new ArrayList<>();
        if (value instanceof ArrayList) {
            ArrayList list = (ArrayList) value;
            for (Object o : list) {
                lists.add(castToRequiredType(fieldType, String.valueOf(o)));
            }
        } else {
            lists.add(castToRequiredType(fieldType, String.valueOf(value)));
        }
        return lists;
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.isAssignableFrom(Long.class)) {
            return Long.valueOf(value);
        } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
            return BigDecimal.valueOf(Long.parseLong(value));
        } else if (fieldType.isAssignableFrom(Short.class)) {
            return Short.valueOf(value);
        } else if (fieldType.isAssignableFrom(Byte.class)) {
            return Byte.valueOf(value);
        } else if (fieldType.isAssignableFrom(Enum.class)) {
            return Enum.valueOf(fieldType, value);
        }
        return value;
    }
}

