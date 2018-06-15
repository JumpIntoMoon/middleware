package com.tyl.autodeliver.service.crawler;

import com.alibaba.fastjson.JSONObject;
import com.tyl.autodeliver.entity.Article;
import com.tyl.autodeliver.header.Header;
import com.tyl.autodeliver.repository.ArticleRepository;
import com.tyl.autodeliver.utils.HttpUtil;
import com.tyl.autodeliver.utils.PrintUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 51淘金阁，爬虫入口类
 * @author: tangYiLong
 * @create: 2018-06-01 10:05
 **/
@Component
public abstract class TaoJinGeCrawlerService {

    public static CookieStore cookieStore;
    public static String taoJinGeHtml;
    public static Map<String, String> targetSites = new HashMap<>();
    private static String indexUrl = "http://www.51taojinge.com/index.php";
    @Autowired
    private ArticleRepository articleRepository;

    //获取爬虫目标网站
    public static void getTargetSites() {
        if (targetSites.size() == 0) {
            cookieStore = new BasicCookieStore();
            taoJinGeHtml = HttpUtil.doGet(indexUrl, new HashMap<>(), Header.defaultHeader(), cookieStore);
            TagNode node = new HtmlCleaner().clean(taoJinGeHtml);
            //按属性取
            Object[] ns = node.getElementsByAttValue("class", "span1", true, true);
            for (Object on : ns) {
                TagNode n = (TagNode) on;
                TagNode a = (TagNode) n.getAllChildren().get(1);
                targetSites.put(a.getText().toString().trim(), a.getAttributeByName("href"));
            }
            PrintUtil.println("获取爬虫目标网站：" + targetSites.toString());
        }
    }

    public abstract void crawler() throws Exception;

    public void getAndSaveContent(List<TagNode> contentChildren, Article article) {
        String formattedContent = "";
        JSONObject imgPaths = new JSONObject(new LinkedHashMap<>());
        int imgSuffix = 0;
        for (TagNode node : contentChildren) {
            if ("img".equals(node.getName())) {
                String imgPath = node.getAttributeByName("src");
                if (StringUtils.isNotEmpty(imgPath)) {
                    String imgKey = "img" + imgSuffix;
                    formattedContent += "${" + imgKey + "}" + "\n";
                    imgPaths.put(imgKey, imgPath);
                    imgSuffix++;
                }
            } else {
                String text = node.getText().toString().trim();
                if (StringUtils.isNotEmpty(text)) {
                    formattedContent += text + "\n";
                }
            }
        }
        article.setContent(formattedContent);
        article.setImg(imgPaths.toString());
        if (StringUtils.isNotEmpty(article.getTitle()) && StringUtils.isNotEmpty(article.getContent())) {
            articleRepository.save(article);
        }
    }
}
