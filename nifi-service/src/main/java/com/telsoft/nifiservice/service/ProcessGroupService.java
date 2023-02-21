package com.telsoft.nifiservice.service;

import com.telsoft.libcore.message.ResponseMsg;

public interface ProcessGroupService {
    ResponseMsg findAllByRootUID() throws Exception;
}
