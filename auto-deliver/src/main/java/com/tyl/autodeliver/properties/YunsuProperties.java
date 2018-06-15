package com.tyl.autodeliver.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-29 9:51
 **/
@Component
@PropertySource(value = {"classpath:config/yunsuConfig.properties"}, encoding = "UTF-8", name = "yunsuConfig")
public class YunsuProperties {

    @Value("${yunsu.account}")
    public String account;

    @Value("${yunsu.password}")
    public String password;

    @Value("${yunsu.typeId}")
    public String typeId;

    @Value("${yunsu.timeout}")
    public String timeout;

    @Value("${yunsu.softId}")
    public String softId;

    @Value("${yunsu.softKey}")
    public String softKey;
}
