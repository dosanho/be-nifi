package com.telsoft.nifiservice.controller;

import com.telsoft.libcore.message.ResponseMsg;
import com.telsoft.libcore.util.HttpRequestUtil;
import com.telsoft.nifiservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nifi")
@CrossOrigin("/*")
public class NifiController {

    private final TokenService tokenService;
    @PostMapping("/createToken")
    public ResponseMsg Nifi(@RequestBody Map<String, Object> value) throws Exception {
        return tokenService.createToken(value);
    }
}
