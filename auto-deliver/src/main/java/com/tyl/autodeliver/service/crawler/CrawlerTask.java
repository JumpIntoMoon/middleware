package com.tyl.autodeliver.service.crawler;

import com.tyl.autodeliver.service.crawler.taojinge.BaijiaCrawlerService;
import com.tyl.autodeliver.service.crawler.taojinge.QieCrawlerService;
import com.tyl.autodeliver.service.crawler.taojinge.ToutiaoCrawlerService;
import com.tyl.autodeliver.service.crawler.taojinge.UcCrawlerService;
import com.tyl.autodeliver.utils.PrintUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-07 11:15
 **/
//@Component
public class CrawlerTask {

    static {
        ToutiaoCrawlerService.getTargetSites();
    }

    @Autowired
    private ToutiaoCrawlerService toutiaoCrawlerService;
    @Autowired
    private QieCrawlerService qieCrawlerService;
    @Autowired
    private BaijiaCrawlerService baijiaCrawlerService;
    @Autowired
    private UcCrawlerService ucCrawlerService;

    //@Scheduled(cron = "${schedule.cron}")
    public void toutiaoTask() {
        //爬虫获取头条文章
        try {
            toutiaoCrawlerService.crawler();
        } catch (Exception e) {
            PrintUtil.println("爬虫获取头条文章，持续寻找中···");
        }
    }

    //@Scheduled(cron = "${schedule.cron}")
    public void qieTask() {
        //爬虫获取企鹅文章
        try {
            qieCrawlerService.crawler();
        } catch (Exception e) {
            PrintUtil.println("爬虫获取企鹅文章，持续寻找中···");
        }
    }

    //@Scheduled(cron = "${schedule.cron}")
    public void baijiaTask() {
        //爬虫获取百家文章
        try {
            baijiaCrawlerService.crawler();
        } catch (Exception e) {
            PrintUtil.println("爬虫获取百家文章，持续寻找中···");
        }
    }

    //@Scheduled(cron = "${schedule.cron}")
    public void ucTask() {
        //爬虫获取UC文章
        try {
            ucCrawlerService.crawler();
        } catch (Exception e) {
            PrintUtil.println("爬虫获取UC文章，持续寻找中···");
        }
    }
}
