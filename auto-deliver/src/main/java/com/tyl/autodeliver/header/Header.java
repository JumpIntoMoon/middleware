package com.tyl.autodeliver.header;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-02 23:14
 **/
public class Header {
    public static Map<String, String> defaultHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        header.put("accept-encoding", "gzip, deflate");
        header.put("accept-language", "zh-CN,zh;q=0.9");
        header.put("cache-control", "max-age=0");
        header.put("Connection", "keep-alive");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        return header;
    }
}
