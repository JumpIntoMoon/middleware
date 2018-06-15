package com.tyl.autodeliver.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * value值是设置需要加载的属性文件，可以一次性加载多个
 *
 * @author: tangYiLong
 * @create: 2018-05-26 16:22
 **/
@Component
@PropertySource(value = {"classpath:config/config.properties"}, encoding = "UTF-8", name = "commonConfig")
public class ConfigProperties {

    @Value("${driver.chrome.name}")
    public String chromeDriverName;

    @Value("${driver.chrome.path}")
    public String chromeDriverPath;

    @Value("${account.path}")
    public String accountPath;

    @Value("${account.fileName}")
    public String accountFileName;

    @Value("${traineddata.path}")
    public String trainedDataPath;

    @Value("${traineddata.language}")
    public String trainedLanguage;

    @Value("${identifyCode.local}")
    public String identifyCodeLocalPath;

    @Value("${article.imgFolder}")
    public String articleImgFolder;

    @Value("${crawler.pageCount}")
    public Integer pageCount;
}
