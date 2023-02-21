package com.telsoft.nifiservice.service;

import com.telsoft.libcore.message.ResponseMsg;
import com.telsoft.libcore.util.HttpRequestUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.telsoft.nifiservice.constant.Const.NifiParam.PASSWORD;
import static com.telsoft.nifiservice.constant.Const.NifiParam.USERNAME;
import static com.telsoft.nifiservice.constant.Const.NifiUrl.ACCESS_TOKEN;

@Service
@Data
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final HttpRequestUtil httpRequestUtil;

    @Value("${link.url.nifi-service}")
    private String nifiServiceUrl;

    @Override
    public ResponseMsg createToken(Map<String, Object> value) throws Exception {
        String url = nifiServiceUrl + ACCESS_TOKEN;

        RequestBody formBody = new FormBody.Builder()
                .add(USERNAME, value.get(USERNAME).toString())
                .add(PASSWORD, value.get(PASSWORD).toString())
                .build();

        String responseText = httpRequestUtil.post(url, formBody, null);
        return ResponseMsg.newOKResponse(responseText);
    }
}
