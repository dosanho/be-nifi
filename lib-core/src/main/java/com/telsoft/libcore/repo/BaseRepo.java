package com.telsoft.libcore.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepo<Bean, ID> extends JpaSpecificationExecutor<Bean>, CustomerRepo<Bean, ID> {
}
