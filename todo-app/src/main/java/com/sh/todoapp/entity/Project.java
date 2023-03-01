package com.sh.todoapp.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sh.todoapp.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "PROJECT")
@JsonPropertyOrder({"projectd" })
public class Project extends BaseEntity implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID",columnDefinition = "bigint",updatable = false)
    private Long projectId;
    @Size(max = 100)
    @Column(name = "PROJECT_NAME", nullable = false, length = 30)
//    @JsonDeserialize(using = TrimSpace.class)
    private String projectName;
    @Size(max = 255)
    @Column(name = "DESCRIPTION", nullable = true, length = 256)
    private String description;

    @Size(max = 1)
    @Column(name = "STATUS", nullable = false, length = 1)
    private Integer status;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
