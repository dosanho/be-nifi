package com.telsoft.libcore.specification;

import com.telsoft.libcore.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;


@Data
public class SpecSearchCriteria {

    private String key;
    private String type;
    private SearchOperation operation;
    private Object value;
    private boolean orPredicate;

    public SpecSearchCriteria() {
    }
    public SpecSearchCriteria(String key, String operation, String prefix, String value, String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        Date dateValue = DateUtil.getSqlDate(value);
        boolean isDate = dateValue != null;

        if (op != null) {
            if (op == SearchOperation.EQUALITY || op == SearchOperation.NEGATION) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    if(value.startsWith("!")) {
                        op = SearchOperation.NOT_CONTAINS;
                        value = value.substring(1);
                    }
                    else {
                        op = op == SearchOperation.EQUALITY ? SearchOperation.CONTAINS : SearchOperation.NOT_CONTAINS;
                    }
                } else if (startWithAsterisk) {
                    op = op == SearchOperation.EQUALITY ? SearchOperation.ENDS_WITH : SearchOperation.NOT_ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = op == SearchOperation.EQUALITY ? SearchOperation.STARTS_WITH : SearchOperation.NOT_STARTS_WITH;
                }
            }
        }

        this.key = key;
        this.operation = op;
        this.type = isDate ? "date" : "";

        if (isDate) {
            this.value = dateValue;
        } else {
            this.value = value;
        }
    }
}
