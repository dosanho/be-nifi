package com.sh.todoapp.controller;

import com.sh.todoapp.entity.Project;
import com.sh.todoapp.repository.ProjectRepo;
import com.sh.todoapp.service.abstractImpl.AbstractProjectService;
import com.sh.todoapp.specification.ProjectSpecification;
import com.telsoft.libcore.controller.BaseController;
import com.telsoft.libcore.message.ResponseMsg;
import com.telsoft.libcore.specification.CriteriaParser;
import com.telsoft.libcore.specification.GenericSpecificationsBuilder;
import com.telsoft.libcore.specification.SpecSearchCriteria;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Deque;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ProjectController extends BaseController<Project, Long, ProjectRepo, AbstractProjectService> {

    @CrossOrigin(origins = "/**")
    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseMsg getOfferBalanceInstances(@RequestParam(value = "search", required = false) String search, Pageable pageable) throws Exception {
        if (search == null) {
            return findAll(pageable);
        }

        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<Project> builder = new GenericSpecificationsBuilder<>();
        Specification<Project> spec = builder.build(parser.parse(search), ProjectSpecification::new);
        return findAll(spec, pageable);
    }

    @Override
    public String getBeanName() {
        return "Project";
    }

    @Override
    public void merge(Project newBean, Project currentBean) throws Exception {

    }
}
