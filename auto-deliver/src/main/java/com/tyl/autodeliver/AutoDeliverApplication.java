package com.tyl.autodeliver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootApplication
@EnableJpaAuditing //开启spring data jpa
@EnableScheduling //开启schedule注解定时任务
@EnableCaching //开启redis缓存
public class AutoDeliverApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoDeliverApplication.class, args);
    }

    /**
     * 开启方法参数校验注解
     * @return
     */
    @Bean
    public MethodValidationPostProcessor mvp() {
        return new MethodValidationPostProcessor();
    }
}
