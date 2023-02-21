package com.telsoft.nifiservice.service;

import com.telsoft.libcore.message.ResponseMsg;

import java.util.Map;

public interface TokenService {
    ResponseMsg createToken(Map<String, Object> value) throws Exception;
}
