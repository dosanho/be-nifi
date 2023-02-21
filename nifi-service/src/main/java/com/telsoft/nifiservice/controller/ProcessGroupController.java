package com.telsoft.nifiservice.controller;

import com.telsoft.libcore.message.ResponseMsg;
import com.telsoft.nifiservice.service.ProcessGroupService;
import com.telsoft.nifiservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nifi/processGroup")
@CrossOrigin("/*")
public class ProcessGroupController {

    private final ProcessGroupService processGroupService;

    @GetMapping("/findAll")
    public ResponseMsg Nifi() throws Exception {
        return processGroupService.findAllByRootUID();
    }
}
