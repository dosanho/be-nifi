package com.telsoft.nifiservice.service;

import com.telsoft.libcore.message.ResponseMsg;
import com.telsoft.nifiservice.dto.VariablesDTO;

public interface ProcessGroupService {
    ResponseMsg findAllByRootUID() throws Exception;

    ResponseMsg updateVariable(VariablesDTO variablesDTO) throws Exception;
}
