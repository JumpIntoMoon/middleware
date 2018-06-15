package com.tyl.autodeliver.entity;

public class Account {
    //用户名
    private String userName;
    //密码
    private String password;
    //昵称
    private String nickName;
    //领域
    private String categroy;
    //每日发文数量
    private String dailyPubNum;
    //是否搬运原创，1：搬运，0：不搬运
    private String isTrans;
    //固定开头语
    private String openingRemarks;
    //固定结束语
    private String closingRemarks;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCategroy() {
        return categroy;
    }

    public void setCategroy(String categroy) {
        this.categroy = categroy;
    }

    public String getDailyPubNum() {
        return dailyPubNum;
    }

    public void setDailyPubNum(String dailyPubNum) {
        this.dailyPubNum = dailyPubNum;
    }

    public String getIsTrans() {
        return isTrans;
    }

    public void setIsTrans(String isTrans) {
        this.isTrans = isTrans;
    }

    public String getOpeningRemarks() {
        return openingRemarks;
    }

    public void setOpeningRemarks(String openingRemarks) {
        this.openingRemarks = openingRemarks;
    }

    public String getClosingRemarks() {
        return closingRemarks;
    }

    public void setClosingRemarks(String closingRemarks) {
        this.closingRemarks = closingRemarks;
    }
}

