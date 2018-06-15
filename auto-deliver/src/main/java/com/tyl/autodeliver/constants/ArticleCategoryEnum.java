package com.tyl.autodeliver.constants;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-01 15:12
 **/
public enum ArticleCategoryEnum {
    ALL("全部"),
    NEWS("新闻"),
    SOCIETY("社会"),
    FUNS("娱乐"),
    MOVIE("电影"),
    TECHNOLOGY("科技"),
    DIGITAL("数码"),
    CARS("汽车"),
    SPORT("体育"),
    FINANCE("财经"),
    MILITARY("军事"),
    INTERNATION("国际"),
    FASHION("时尚"),
    WONDER("奇葩"),
    GAME("游戏"),
    TRAVEL("旅游"),
    CHILDCARE("育儿"),
    SLIMMING("瘦身"),
    HEALTHCARE("养生"),
    FOOD("美食"),
    HISTORY("历史"),
    EXPLORE("探索"),
    STORY("故事"),
    GOODART("美文"),
    FELLING("情感"),
    HEALTH("健康"),
    EDUCATION("教育"),
    LIVE("家居"),
    HOUSE("房产"),
    FUNNY("搞笑"),
    CONSTELLATION("星座"),
    CULTURE("文化"),
    PETS("宠物"),
    LEGAL("法制"),
    JOB("职场"),
    CARTOON("漫画"),
    COMIC("动漫"),
    SCIENCE("科学"),
    DSIGN("设计"),
    PHOTO("摄影"),
    COLLECTION("收藏"),
    FARMER("三农"),
    MIND("心理"),
    MEDIA("传媒"),
    BEAUTY("美女"),
    OTHER("其他");

    private String value;

    ArticleCategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
