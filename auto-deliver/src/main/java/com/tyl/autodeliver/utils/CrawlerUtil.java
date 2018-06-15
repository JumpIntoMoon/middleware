package com.tyl.autodeliver.utils;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-01 10:53
 **/
public class CrawlerUtil {
    /**
     * @param html
     * @param xpath
     * @return
     * @throws Exception
     */
    public static Object fetchNode(String html, String xpath) throws Exception {
        HtmlCleaner hc = new HtmlCleaner();
        TagNode tn = hc.clean(html);
        Document dom = new DomSerializer(new CleanerProperties()).createDOM(tn);
        XPath xPath = XPathFactory.newInstance().newXPath();
        Object result = xPath.evaluate(xpath, dom, XPathConstants.NODESET);
        return result;
    }

    /**
     * 获取xpath下的a标签的文本值及href属性值 /**
     *
     * @param html
     * @param xpath
     * @return
     * @throws Exception
     */
    public static Map<String, String> fecthByMap(String html, String xpath) throws Exception {
        Map<String, String> nodeMap = new LinkedHashMap<>();
        Object result = fetchNode(html, xpath);
        if (result instanceof NodeList) {
            NodeList nodeList = (NodeList) result;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node == null) {
                    continue;
                }
                nodeMap.put(node.getTextContent(), node.getFirstChild().getAttributes().getNamedItem("href") != null ? node.getAttributes().getNamedItem("href").getTextContent() : "");
                System.out.println(node.getTextContent() + " : " + node.getAttributes().getNamedItem("href"));
            }
        }
        return nodeMap;
    }

    /**
     * 获取xpath下的某个属性值 /**
     *
     * @param html
     * @param xpath
     * @param attr
     * @return
     * @throws Exception
     */
    public static List<String> fecthAttr(String html, String xpath, String attr) throws Exception {
        List<String> list = new ArrayList<>();
        Object result = fetchNode(html, xpath);
        if (result instanceof NodeList) {
            NodeList nodeList = (NodeList) result;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node == null) {
                    continue;
                }
                list.add(node.getAttributes().getNamedItem(attr).getTextContent());
                //System.out.println(node.getTextContent() + " : " + node.getAttributes().getNamedItem("href"));
            }
        }
        return list;
    }


}
