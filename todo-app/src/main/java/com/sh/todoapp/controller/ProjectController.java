package com.sh.todoapp.controller;

import com.sh.todoapp.entity.Project;
import com.sh.todoapp.repository.ProjectRepo;
import com.telsoft.libcore.specification.CriteriaParser;
import com.telsoft.libcore.specification.SpecSearchCriteria;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Deque;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ProjectController {
    private final ProjectRepo projectRepo;

    @GetMapping("/test")
    public Deque<?> findAll(@RequestParam(name = "search") String search) {
        System.out.println(search);
        CriteriaParser parser = new CriteriaParser();
        Deque<?> result = parser.parse(search);

        return result;
    }
}
