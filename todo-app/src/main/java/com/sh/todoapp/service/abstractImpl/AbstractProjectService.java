package com.sh.todoapp.service.abstractImpl;

import com.sh.todoapp.entity.Project;
import com.sh.todoapp.repository.ProjectRepo;
import com.sh.todoapp.service.ProjectService;
import com.telsoft.libcore.service.AbstractCRUDService;
import com.telsoft.libcore.service.CRUDService;
import org.springframework.stereotype.Service;

public abstract class AbstractProjectService extends AbstractCRUDService<Project, Long, ProjectRepo> implements ProjectService {
}
