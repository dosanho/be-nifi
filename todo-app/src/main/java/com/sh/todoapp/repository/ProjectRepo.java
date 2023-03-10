package com.sh.todoapp.repository;

import com.sh.todoapp.entity.Project;
import com.telsoft.libcore.repo.BaseRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends BaseRepo<Project, Long> {
}
