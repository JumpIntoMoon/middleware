package com.tyl.autodeliver.utils;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-08 15:35
 **/
public class StringUtil {

    public static boolean contains(String source, String... strings) {
        for (String str : strings) {
            if (!source.contains(str)) return false;
        }
        return true;
    }
}
