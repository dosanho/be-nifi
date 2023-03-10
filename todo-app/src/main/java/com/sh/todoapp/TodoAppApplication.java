package com.sh.todoapp;

import com.telsoft.libcore.message.Message;
import com.telsoft.libcore.repo.CustomerRepoImpl;
import com.telsoft.libcore.util.HttpRequestUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomerRepoImpl.class)
public class TodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
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
