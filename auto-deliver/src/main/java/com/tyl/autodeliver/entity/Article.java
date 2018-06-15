package com.tyl.autodeliver.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-26 22:26
 **/
@Entity
@Table(name = "t_article")
@EntityListeners(AuditingEntityListener.class)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, name = "title")
    private String title;

    @Column(columnDefinition = "LONGTEXT", name = "content")
    private String content;

    @Column(columnDefinition = "TEXT", name = "img")//默认长度255
    private String img;

    @Column(length = 10, name = "category")
    private String category;

    @Column(name = "url")
    private String url;

    @Column(length = 10, name = "source")
    private String source;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "createDate")
    @CreatedDate
    private Date createDate;

    @Column(name = "isSent", length = 1)
    private Boolean isSent = false;

    @Column(name = "isOriginal", length = 1)
    private Boolean isOriginal = false;

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public Boolean getOriginal() {
        return isOriginal;
    }

    public void setOriginal(Boolean original) {
        isOriginal = original;
    }
}
