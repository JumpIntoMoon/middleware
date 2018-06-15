package com.tyl.autodeliver.service.crawler.taojinge;

import com.tyl.autodeliver.constants.NavigationEnum;
import com.tyl.autodeliver.entity.Article;
import com.tyl.autodeliver.header.Header;
import com.tyl.autodeliver.repository.ArticleRepository;
import com.tyl.autodeliver.service.crawler.TaoJinGeCrawlerService;
import com.tyl.autodeliver.utils.HttpUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-08 13:39
 **/
@Component
public class QieCrawlerService extends TaoJinGeCrawlerService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void crawler() throws Exception {
        String pageUrl = targetSites.get(NavigationEnum.QIE.getValue());
        Thread.sleep(1000);
        String pageHtml = HttpUtil.doGet(pageUrl, new HashMap<>(), Header.defaultHeader(), cookieStore);
        TagNode pageNode = new HtmlCleaner().clean(pageHtml);
        getArticlesByLinks(getLinksOfArticles(pageNode));
    }

    public List<Article> getLinksOfArticles(TagNode pageNode) {
        List<Article> articles = new ArrayList<>();
        //按属性取
        TagNode tableNode = pageNode.findElementByName("table", true);
        if (tableNode != null) {
            TagNode tbodyNode = tableNode.findElementByName("tbody", false);
            //寻找table里面的tr元素，不递归
            List<TagNode> trNodes = (List<TagNode>) tbodyNode.getElementListByName("tr", false);
            if (CollectionUtils.isNotEmpty(trNodes)) {
                for (TagNode trNode : trNodes) {
                    if (trNode != null) {
                        //寻找tr里面的td元素，不递归
                        List<TagNode> tdNodes = (List<TagNode>) trNode.getElementListByName("td", false);
                        if (CollectionUtils.isNotEmpty(tdNodes)) {
                            Article article = new Article();
                            article.setSource(NavigationEnum.QIE.name());
                            //获取url和标题
                            TagNode tdNode1 = tdNodes.get(1);
                            TagNode articleLinkNode = tdNode1.getAllElements(false)[0];
                            String title = articleLinkNode.getText().toString();
                            String articleUrl = articleLinkNode.getAttributeByName("href");
                            article.setTitle(title);
                            article.setUrl(articleUrl);
                            //获取分类
                            TagNode tdNode4 = tdNodes.get(4);
                            article.setCategory(tdNode4.getText().toString());
                            //判断该文章url在数据库中是否已经存在，若存在则不保存
                            List<Article> dbArticles = articleRepository.findAllBySourceUrl(article.getUrl(), article.getSource());
                            if (CollectionUtils.isEmpty(dbArticles)) {
                                articles.add(article);
                            }
                        }
                    }
                }
            }
        }
        return articles;
    }

    public void getArticlesByLinks(List<Article> articles) throws Exception {
        for (Article article : articles) {
            String articleUrl = article.getUrl();
            Thread.sleep(1000);
            String articleHtml = HttpUtil.doGet(articleUrl, new HashMap<>(), Header.defaultHeader(), cookieStore);
            if (StringUtils.isNotEmpty(articleHtml)) {
                HtmlCleaner cleaner = new HtmlCleaner();
                TagNode rootNode = cleaner.clean(articleHtml);
                //判断原创
                TagNode orgNode = rootNode.findElementByAttValue("class", "org_tag", true, true);
                if (orgNode != null && orgNode.getText() !=null) {
                    String originalStr = orgNode.getText().toString();
                    if ("原创".equals(originalStr)) {
                        article.setOriginal(true);
                    }
                }
                TagNode contentNode = rootNode.findElementByAttValue("class", "content-box", true, true);
                List<TagNode> contentChildren = (List<TagNode>) contentNode.getAllElementsList(true);
                getAndSaveContent(contentChildren, article);
            }
        }
    }
}
