package com.sh.todoapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sh.todoapp.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Entity
@Table(name = "PROJECT")
@Data
public class Project implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID", columnDefinition = "bigint", updatable = false)
    private Long projectId;
    @Size(max = 100)
    @Column(name = "PROJECT_NAME", nullable = false, length = 30)
    private String projectName;
    @Size(max = 255)
    @Column(name = "DESCRIPTION", nullable = true, length = 256)
    private String description;

    @Size(max = 1)
    @Column(name = "STATUS", nullable = false, length = 1)
    private Integer status;

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

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
