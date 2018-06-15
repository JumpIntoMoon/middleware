package com.tyl.autodeliver.constants;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-01 14:54
 **/
public enum NavigationEnum {
    TOUTIAO("头条(免费)"),
    QIE("企鹅文章"),
    BAIJIA("百家文章"),
    UC("UC文章");

    private String value;

    NavigationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
