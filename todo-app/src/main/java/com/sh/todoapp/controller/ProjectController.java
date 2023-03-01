package com.sh.todoapp.controller;

import com.sh.todoapp.entity.Project;
import com.sh.todoapp.repository.ProjectRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ProjectController {
    private final ProjectRepo projectRepo;

    @GetMapping("/test")
    public List<Project> findAll(){
        return projectRepo.findAll();
    }
}
