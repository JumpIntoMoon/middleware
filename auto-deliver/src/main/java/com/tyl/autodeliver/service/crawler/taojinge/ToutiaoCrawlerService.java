package com.tyl.autodeliver.service.crawler.taojinge;

import com.tyl.autodeliver.constants.NavigationEnum;
import com.tyl.autodeliver.entity.Article;
import com.tyl.autodeliver.header.Header;
import com.tyl.autodeliver.properties.ConfigProperties;
import com.tyl.autodeliver.repository.ArticleRepository;
import com.tyl.autodeliver.service.crawler.TaoJinGeCrawlerService;
import com.tyl.autodeliver.utils.HttpUtil;
import com.tyl.autodeliver.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 头条爬虫操作类
 * @author: tangYiLong
 * @create: 2018-06-01 10:07
 **/
@Component
public class ToutiaoCrawlerService extends TaoJinGeCrawlerService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ConfigProperties configProperties;

    @Override
    public void crawler() throws Exception {
        String pageUrl = targetSites.get(NavigationEnum.TOUTIAO.getValue());
        for (int i = 1; i <= configProperties.pageCount; i++) {
            pageUrl += "?page=" + i;
            Thread.sleep(1000);
            String pageHtml = HttpUtil.doGet(pageUrl, new HashMap<>(), Header.defaultHeader(), cookieStore);
            TagNode pageNode = new HtmlCleaner().clean(pageHtml);
            getArticlesByLinks(getLinksOfArticles(pageNode));
        }
    }


    public List<Article> getLinksOfArticles(TagNode pageNode) {
        List<Article> articles = new ArrayList<>();
        //按属性取
        TagNode[] ns = pageNode.getElementsByAttValue("data-toggle", "tooltip", true, true);
        for (TagNode on : ns) {
            if (on != null) {
                Article article = new Article();
                article.setTitle(on.getText().toString());
                article.setSource(NavigationEnum.TOUTIAO.name());
                TagNode tr = on.getParent().getParent();
                TagNode ctgA = tr.getElementsByName("a", true)[1];
                article.setCategory(ctgA.getText().toString());
                article.setUrl(on.getAttributeByName("href"));
                //判断该文章url在数据库中是否已经存在，若存在则不保存
                List<Article> dbArticles = articleRepository.findAllBySourceUrl(article.getUrl(), article.getSource());
                if (CollectionUtils.isEmpty(dbArticles)) {
                    articles.add(article);
                }
            }
        }
        return articles;
    }

    public void getArticlesByLinks(List<Article> articles) throws Exception {
        String subBegin;
        String subEnd;
        for (Article article : articles) {
            String articleUrl = article.getUrl();
            Thread.sleep(1000);
            String articleHtml = HttpUtil.doGet(articleUrl, new HashMap<>(), Header.defaultHeader(), cookieStore);
            if (StringUtils.isNotEmpty(articleHtml)) {
                HtmlCleaner cleaner = new HtmlCleaner();
                TagNode rootNode = cleaner.clean(articleHtml);
                TagNode bodyNode = (TagNode) rootNode.getAllChildren().get(1);
                TagNode funcNode = (TagNode) bodyNode.getAllChildren().get(5);
                if (funcNode.getAllChildren() != null) {
                    ContentNode contNode = (ContentNode) funcNode.getAllChildren().get(0);
                    String longContent = contNode.getContent();
                    subBegin = "articleInfo: {";
                    subEnd = "commentInfo: {";
                    String articleInfo = "";
                    if (StringUtil.contains(longContent, subBegin, subEnd)) {
                        articleInfo = longContent.substring(longContent.indexOf(subBegin), longContent.indexOf(subEnd));
                        //获取原创标志
                        subBegin = "isOriginal:";
                        subEnd = "source:";
                        String isOriginalStr = articleInfo.substring(articleInfo.indexOf(subBegin),articleInfo.indexOf(subEnd));
                        if (StringUtils.isNotEmpty(isOriginalStr) && isOriginalStr.contains("true")) {
                            article.setOriginal(true);
                        }
                        //获取内容
                        subBegin = "content:";
                        subEnd = "groupId:";
                        if (StringUtil.contains(articleInfo, subBegin, subEnd)) {
                            String bfContent = articleInfo.substring(articleInfo.indexOf(subBegin), articleInfo.indexOf(subEnd)).trim();
                            if (StringUtils.isNotEmpty(bfContent)) {
                                String content = bfContent.substring(subBegin.length(), bfContent.lastIndexOf(","));
                                if (StringUtils.isNotEmpty(content)) {
                                    String contentHtml = StringEscapeUtils.unescapeHtml4(content);
                                    TagNode conBody = (TagNode) new HtmlCleaner().clean(contentHtml).getAllChildren().get(1);
                                    List<TagNode> contentChildren = (List<TagNode>) conBody.getAllElementsList(true);
                                    getAndSaveContent(contentChildren, article);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
