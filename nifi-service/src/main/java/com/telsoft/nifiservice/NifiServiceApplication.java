package com.telsoft.nifiservice;

import com.telsoft.libcore.message.Message;
import com.telsoft.libcore.util.HttpRequestUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NifiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NifiServiceApplication.class, args);
    }

    @Bean
    public HttpRequestUtil httpRequestUtil(){
        return new HttpRequestUtil();
    }

    @Bean
    public Message Message() {
        return new Message();
    }



}
