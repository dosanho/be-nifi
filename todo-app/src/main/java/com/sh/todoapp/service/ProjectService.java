package com.sh.todoapp.service;

import com.sh.todoapp.entity.Project;
import com.sh.todoapp.repository.ProjectRepo;
import com.telsoft.libcore.service.CRUDService;

public interface ProjectService extends CRUDService<Project, Long, ProjectRepo> {
}
