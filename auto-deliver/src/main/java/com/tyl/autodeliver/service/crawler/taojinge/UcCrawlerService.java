package com.tyl.autodeliver.service.crawler.taojinge;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-08 13:40
 **/
@Component
public class UcCrawlerService extends TaoJinGeCrawlerService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void crawler() throws Exception {
        String pageUrl = targetSites.get(NavigationEnum.UC.getValue());
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
                            article.setSource(NavigationEnum.UC.name());
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
            //uc到dayu的重定向网址
            String redirectUrl = "http://ff.dayu.com/contents/origin/{wm_id}?biz_id=1002&_fetch_author=1&_fetch_incrs=1";
            String articleUrl = article.getUrl();
            Thread.sleep(1000);
            String articleHtml = HttpUtil.doGet(articleUrl, new HashMap<>(), Header.defaultHeader(), cookieStore);
            if (StringUtils.isNotEmpty(articleHtml)) {
                TagNode rootNode = new HtmlCleaner().clean(articleHtml);
                //组装新的跳转请求
                String wmId = articleUrl.substring(articleUrl.lastIndexOf("=") + 1);
                redirectUrl = redirectUrl.replace("{wm_id}", wmId);
                Thread.sleep(1000);
                String redirectResponse = HttpUtil.doGet(redirectUrl, new HashMap<>(), Header.defaultHeader(), cookieStore);
                JSONObject rootJson = JSONObject.parseObject(redirectResponse);
                JSONObject dataJson = JSONObject.parseObject(rootJson.getString("data"));
                JSONObject bodyJson = JSONObject.parseObject(dataJson.getString("body"));
                //判断原创
                JSONObject authorJson = JSONObject.parseObject(dataJson.getString("_author"));
                JSONObject extraJson = JSONObject.parseObject(authorJson.getString("extra_map"));
                Integer isOriginal = extraJson.getInteger("is_original");
                if (isOriginal == 1) {
                    article.setOriginal(true);
                }
                //获取内容
                String text = bodyJson.getString("text");
                JSONArray innerImgs = JSONArray.parseArray(bodyJson.getString("inner_imgs"));
                JSONObject imgPaths = new JSONObject(new LinkedHashMap<>());
                int imgSuffix = 0;
                for (Object o : innerImgs) {
                    JSONObject jso = (JSONObject) o;
                    String imgKey = "img" + imgSuffix;
                    String imgUrl = jso.getString("url");
                    imgPaths.put(imgKey, imgUrl);
                    text = text.replace("<!--{img:" + imgSuffix + "}-->", "${" + imgKey + "}" + "\n");
                    text = text.replaceAll("</[^>]+>", "\n");
                    imgSuffix++;
                }
                String content = text.replaceAll("<[^>]+>", "");
                article.setImg(imgPaths.toString());
                article.setContent(content);
                if (StringUtils.isNotEmpty(article.getTitle()) && StringUtils.isNotEmpty(article.getContent())) {
                    articleRepository.save(article);
                }
            }
        }
    }
}
