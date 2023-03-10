package com.sh.todoapp.specification;

import com.sh.todoapp.entity.Project;
import com.telsoft.libcore.specification.SpecSearchCriteria;
import com.telsoft.libcore.specification.SpecificationBase;

public class ProjectSpecification extends SpecificationBase<Project> {
    public ProjectSpecification(SpecSearchCriteria criteria) {
        super(criteria);
    }
}
