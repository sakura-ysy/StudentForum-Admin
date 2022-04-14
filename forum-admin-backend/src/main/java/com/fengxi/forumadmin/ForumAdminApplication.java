package com.fengxi.forumadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.fengxi.forumadmin.mapper")
@SpringBootApplication
public class ForumAdminApplication extends SpringBootServletInitializer {

    @Override  // 重写 configure 固定格式
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ForumAdminApplication.class) ;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ForumAdminApplication.class, args);
        // 打印容器中的所有组件
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            // System.out.println(beanDefinitionName);
        }
    }

}
