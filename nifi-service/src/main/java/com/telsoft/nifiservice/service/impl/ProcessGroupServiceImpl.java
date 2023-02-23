package com.telsoft.nifiservice.service.impl;

import com.telsoft.libcore.message.ResponseMsg;
import com.telsoft.libcore.util.HttpRequestUtil;
import com.telsoft.nifiservice.dto.UserDTO;
import com.telsoft.nifiservice.dto.VariablesDTO;
import com.telsoft.nifiservice.service.ProcessGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.telsoft.nifiservice.constant.Const.NifiUrl.PROCESS_GROUP;
import static com.telsoft.nifiservice.constant.Const.ROOT_UID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessGroupServiceImpl implements ProcessGroupService {

    private final HttpRequestUtil httpRequestUtil;

    private final HttpServletRequest request;

    @Value("${link.url.nifi-service}")
    private String nifiServiceUrl;

    @Override
    public ResponseMsg findAllByRootUID() throws Exception {
        try {
            String url = nifiServiceUrl + String.format(PROCESS_GROUP, ROOT_UID);

            String token = request.getHeader(AUTHORIZATION);

            String responseText = httpRequestUtil.get(url, token);

            return ResponseMsg.newOKResponse(responseText);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseMsg updateVariable(VariablesDTO variablesDTO) throws Exception {
        System.out.println(variablesDTO);
        return null;
    }
}
