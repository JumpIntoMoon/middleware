package com.tyl.autodeliver.constants;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-10 11:26
 **/
public enum FileTypeEnum {
    EXCEL_XLS("excel文件", ".xls"),
    EXCEL_XLSX("excel文件", ".xlsx"),
    CSV("csv文件", ".csv"),
    TXT("txt文件", ".txt");
    private String desc;
    private String suffix;

    FileTypeEnum(String desc, String suffix) {
        this.desc = desc;
        this.suffix = suffix;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
