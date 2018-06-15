package com.tyl.autodeliver.service.startup;

import com.tyl.autodeliver.service.publisher.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-25 12:37
 **/
@Component
public class StartUpRunner implements CommandLineRunner {
    @Autowired
    private PublishService publishService;

    @Override
    public void run(String... args) {
        try {
            //发布文章
            publishService.publish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
