package com.tyl.autodeliver.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.Date;


public class PrintUtil {
    public static void println(String content) {
        if (StringUtils.isEmpty(content)) {
            System.out.println(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", new Date()));
        } else
            System.out.println(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", new Date()) + " - " + content);
    }

    public static void print(String content) {
        if (StringUtils.isEmpty(content)) {
            System.out.print("");
        } else
            System.out.print(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", new Date()) + " - " + content);
    }

    public static void println(JSONObject content) {
        System.out.println(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", new Date()) + " - " + content);
    }
}
