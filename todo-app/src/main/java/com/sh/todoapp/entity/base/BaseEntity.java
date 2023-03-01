package com.sh.todoapp.entity.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = true)
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE", nullable = true)
    private LocalDateTime modifiedDate;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = true)
    private Long createdBy;

    @Column(name = "MODIFIED_BY", nullable = true)
    private Long modifiedBy;
}
