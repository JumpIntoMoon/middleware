package com.tyl.autodeliver.service.publisher;

import com.tyl.autodeliver.entity.Account;
import com.tyl.autodeliver.entity.Article;
import com.tyl.autodeliver.exception.LoginException;
import com.tyl.autodeliver.exception.PublishException;
import com.tyl.autodeliver.exception.SwitchAccountException;
import com.tyl.autodeliver.repository.ArticleRepository;
import com.tyl.autodeliver.service.CommonService;
import com.tyl.autodeliver.utils.PrintUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-26 16:05
 **/
@Component
public class PublishService {

    @Autowired
    private CommonService commonService;
    @Autowired
    private OperationService operationService;
    @Autowired
    private ArticleRepository articleRepository;

    public void publish() throws InterruptedException {
        List<Account> accounts = commonService.getAccounts();
        if (CollectionUtils.isEmpty(accounts)) return;
        //开始发布文章
        for (Account account : accounts) {
            //获取dailyPubNum条文章
            Integer dailyPubNum = Integer.valueOf(account.getDailyPubNum());
            List<Article> articles = articleRepository.findNotSentByCategoryLimit(account.getCategroy(), dailyPubNum);
            while (CollectionUtils.isEmpty(articles) || articles.size() < dailyPubNum) {
                PrintUtil.println("该分类下的文章已经全部发送完毕，等待爬虫工具获取新数据···");
                Thread.sleep(1 * 60 * 1000);
                articles = articleRepository.findNotSentByCategoryLimit(account.getCategroy(), dailyPubNum);
            }
            try {
                //登录并发布
                operationService
                        .initialWebDriver()
                        .openWebsite()
                        .login(account.getUserName(), account.getPassword())
                        .publishAndCheck(articles, account);
            } catch (Exception e) {
                PrintUtil.println(e.getMessage());
            } finally {
                operationService.close();
            }
        }
    }
}
