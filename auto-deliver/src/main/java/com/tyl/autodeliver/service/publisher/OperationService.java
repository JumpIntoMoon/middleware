package com.tyl.autodeliver.service.publisher;

import com.alibaba.fastjson.JSONObject;
import com.tyl.autodeliver.entity.Account;
import com.tyl.autodeliver.entity.Article;
import com.tyl.autodeliver.exception.LoginException;
import com.tyl.autodeliver.exception.PublishException;
import com.tyl.autodeliver.exception.SwitchAccountException;
import com.tyl.autodeliver.properties.ConfigProperties;
import com.tyl.autodeliver.repository.ArticleRepository;
import com.tyl.autodeliver.service.CommonService;
import com.tyl.autodeliver.utils.PrintUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-26 11:02
 **/
@Component
public class OperationService {

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ArticleRepository articleRepository;

    private WebDriver webDriver;

    private WebDriverWait webDriverWait;

    private String identifyCode;

    public OperationService initialWebDriver() throws Exception{
        //配置webDriver,必须与当前使用的浏览器版本相对应，且与jar包版本对应
        System.getProperties().setProperty(configProperties.chromeDriverName, configProperties.chromeDriverPath);
        //System.getProperties().setProperty(commonConfig.firefoxDriverName, commonConfig.firefoxDriverPath);
        //开启WebDriver进程
        webDriver = new ChromeDriver();
        //webDriver = new FirefoxDriver();
        return this;
    }

    public OperationService openWebsite() throws Exception{
        //设置窗口大小，窗口最大化：webDriver.manage().window().maximize();
        resetWindow(webDriver);
        //全局隐式等待，等待2秒，针对所有页面元素有效：任何元素的查询时限都是10秒，全局生效，只需要配置一次。
        implicitlyWait(webDriver, 10);
        //设定网址
        webDriver.get("http://kuaichuan.360.cn");
        PrintUtil.println("打开网址：http://kuaichuan.360.cn");
        return this;
    }

    public OperationService login(String userName, String password) throws LoginException {
        try {
            PrintUtil.println("开始登录：用户名-->" + userName + "  密码-->" + password);
            //设置显式等待
            webDriverWait = new WebDriverWait(webDriver, 5);
            //点击登录按钮,未找到登录按钮时，等待按钮加载出来再执行点击事件
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"wrap_box\"]/header/div/div[2]/a[2]"))).click();
            //选择普通登录
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[1]/div/p/a[2]"))).click();
            //输入用户名
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[1]/span/input"))).clear();
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[1]/span/input"))).sendKeys(userName);
            //输入密码
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[2]/span/input"))).clear();
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[2]/span/input"))).sendKeys(password);
            //点击登录
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[5]/input"))).click();
            Thread.sleep(3000);
            //如果密码错误
            if (domExist(new WebDriverWait(webDriver, 3), By.xpath("//label[contains(text(),'登录密码错误，请重新输入')]"))) {
                throw new LoginException("登录密码错误，切换下一个账号···");
            }
            //如果出现验证码
            if (domExist(new WebDriverWait(webDriver, 3), By.xpath("//label[contains(text(),'验证码')]"))) {
                try {
                    generateIdentifyCode(webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[3]/img"))));
                } catch (IOException e) {
                    throw new LoginException("解析验证码出错！！！");
                }
                //输入验证码
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[3]/span/input"))).clear();
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[3]/span/input"))).sendKeys(identifyCode);
                //点击登录
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[5]/input"))).click();
                //如果验证码登录失败，则重新获取验证码，并登录
                while (domExist(webDriverWait, By.xpath("//p[contains(text(),'验证码错误请重新输入')]")) && domExist(webDriverWait, By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[5]/input"))) {
                    PrintUtil.println("验证码错误，重新获取验证码");
                    identifyCode = null;
                    try {
                        generateIdentifyCode(webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[3]/img"))));
                    } catch (IOException e) {
                        throw new LoginException("解析验证码出错！！！");
                    }
                    //输入验证码
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[3]/span/input"))).clear();
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[3]/span/input"))).sendKeys(identifyCode);
                    //点击登录
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div[2]/div/div[2]/form/p[5]/input"))).click();
                    threadWait(5000);
                }
                PrintUtil.println("登录成功！！！");
            }
        } catch (Exception e) {
            throw new LoginException("登录该账号：" + userName + " 出错，切换账号中···");
        }
        return this;
    }

    public OperationService publishAndCheck(List<Article> articles, Account account) throws PublishException {

        for (Article article : articles) {
            try {
                //刷新页面
                webDriver.navigate().refresh();
                PrintUtil.println("开始发表第" + (articles.indexOf(article) +1) + "篇文章···");
                //下载文章图片到本地，等待后续上传
                saveArticleImg(article);
                //点击发表按钮,未找到发表按钮时，等待按钮加载出来再执行点击事件
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"sideBar\"]/ul[2]/li/a"))).click();
                //输入文章内容，选择分类。。。
                inputArticle(article, account);
                //发表文章
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pub-content-wrapper\"]/div[2]/div[1]/div[9]/div[1]"))).click();
                //确认发表
                //webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[8]/div/div/div[3]/div/div/button"))).click();
                threadWait(1000);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'确定')]"))).click();
                if (domExist(webDriverWait, By.xpath("//div[contains(text(),'达到今日提交上限，文章已为您保存到草稿箱')]"))) {
                    //webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[8]/div/div/div[3]/div/div/button"))).click();
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'确定')]"))).click();
                    //webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"open-news\"]/div[1]/div/div/div/div[2]/div/div[1]/div[1]/div/div[1]/div/span/span[2]"))).click();
                    threadWait(5000);
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"open-news\"]/div[1]/div/div/div/div[2]/div/div[1]/div[1]/div/div[1]/div/span/span[2]"))).click();
                    webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'确定')]"))).click();
                    throw new PublishException("该账号今日发文数量已到上限，无法继续发文");
                }
                //发表成功则更新该文章的发表状态为已发表
                articleRepository.updateSentStatus(1, article.getId());
                PrintUtil.println("发表成功！！！");
            } catch (PublishException e) {
                throw new PublishException("该账号今日发文数量已到上限，无法继续发文");
            } catch (Exception e) {
                PrintUtil.println("发表该文章失败，开始发表下一篇");
            }

        }
        return this;
    }

    private void retryClick(By deleteSelector, int maxTimes) {
        int times = 0;
        while (times <= maxTimes) {
            times ++;
            try {
                webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteSelector)).click();
            } catch (Exception e) {
                PrintUtil.println("未获取到当前页面元素，重新获取！");
            }
        }
    }

    /**
     * 切换账号
     *
     * @param isFirstLogin
     * @return
     */
    public OperationService switchAccount(boolean isFirstLogin) throws SwitchAccountException {
        try {
            if (!isFirstLogin) {
                PrintUtil.println("切换下一个账号···");
                webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"open-news\"]/header/div/div[1]/div[1]"))).click();
                webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"open-news\"]/header/div/div[1]/div[3]/div/p[2]/a"))).click();
                PrintUtil.println("切换成功！！！");
            }
        } catch (Exception e) {
            throw new SwitchAccountException("切换账号出错！！！");
        }
        return this;
    }

    public OperationService close() {
        webDriver.close();
        return this;
    }

    public OperationService quit() {
        webDriver.quit();
        return this;
    }

    private void inputArticle(Article article, Account account) throws Exception {
        //输入标题
        PrintUtil.println("输入文章标题：" + article.getTitle());
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"title\"]"))).sendKeys(article.getTitle());
        //编辑内容
        PrintUtil.println("编辑内容中···");
        WebElement contentDom = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"editor-body\"]/div[2]/div[3]/div[2]")));
        PrintUtil.println("输入固定开头语：" + account.getOpeningRemarks());
        contentDom.sendKeys(account.getOpeningRemarks() + "\n");
        String imgUrls = article.getImg();
        String content = article.getContent();
        JSONObject imgUrlsJson = JSONObject.parseObject(imgUrls);
        //分有图片和无图片两种处理方式
        if (imgUrlsJson.size() > 0) {
            int startIndex = 0;
            int endIndex;
            //定义keySet排序规则
            TreeSet<String> keySet = new TreeSet<>((o1, o2) -> {
                int index1 = Integer.parseInt(o1.substring(3));
                int index2 = Integer.parseInt(o2.substring(3));
                return index1 - index2;
            });
            keySet.addAll(imgUrlsJson.keySet());
            for (String key : keySet) {
                String placeholder = "${" + key + "}";
                endIndex = content.indexOf(placeholder);
                String partialContent = content.substring(startIndex, endIndex);
                startIndex = endIndex + placeholder.length();
                //输入文字
                contentDom.sendKeys(partialContent);
                //点击图片上传按钮
                threadWait(2000);
                webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"editor-body\"]/div[2]/div[2]/div[4]"))).click();
                //获取图片本地路径
                String localPath = configProperties.articleImgFolder + key + ".jpg";
                //点击输入框
                WebElement imgInput = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"upload-body\"]/div[4]/div[1]/div/div[2]/input")));
                imgInput.sendKeys(localPath);
                //确认上传
                threadWait(3000);
                WebElement confirmDom = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"upload-model\"]/div[3]/div[1]")));
                confirmDom.click();
            }
            //当所有图片上传完毕，如果还有剩余内容，写入剩余内容
            if (startIndex < content.length()) {
                contentDom.sendKeys(content.substring(startIndex));
            }
        } else {
            contentDom.sendKeys(content);
        }
        PrintUtil.println("输入固定结束语：" + account.getClosingRemarks());
        contentDom.sendKeys("\n" + account.getClosingRemarks());
        //选择分类
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"pub-content-wrapper\"]/div[2]/div[1]/div[6]/div[2]/div"))).click();
        String category = transform(article.getCategory());
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[text()='" + category + "']"))).click();
        PrintUtil.println("选择文章分类：" + category);
    }

    private String transform(String category) {
        switch (category) {
            case "育儿" : return "母婴";
            default:return category;
        }
    }

    private void saveArticleImg(Article article) throws MalformedURLException {
        String imgUrls = article.getImg();
        JSONObject imgJson = JSONObject.parseObject(imgUrls);
        for (String key : imgJson.keySet()) {
            String imgUrl = imgJson.getString(key);
            String localPath = configProperties.articleImgFolder + key + ".jpg";
            //如果是百家和uc的图片，需要去水印
            /*if (article.getCategory().equals(NavigationEnum.UC.name()) || article.getCategory().equals(NavigationEnum.BAIJIA.name())) {
                commonService.replaceColorAndSave(imgUrl,localPath);
            } else {
                commonService.saveRemoteImg(imgUrl, localPath);
            }*/
            commonService.saveRemoteImg(imgUrl, localPath);
        }
    }

    public boolean domExist(WebDriverWait webDriverWait, By selector) {
        try {
            WebElement e = webDriverWait.until((ExpectedCondition<WebElement>) driver -> driver.findElement(selector));
            if (e == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void implicitlyWait(WebDriver driver, int time) {
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    public void resetWindow(WebDriver webDriver) {
        webDriver.manage().window().setPosition(new Point(10, 10));
        webDriver.manage().window().maximize();
    }

    private void generateIdentifyCode(WebElement imgDom) throws IOException {
        while (StringUtils.isEmpty(identifyCode)) {
            //重新设置窗口位置和大小，确保验证码位置不偏移，截屏，保存图片
            //resetWindow(webDriver);
            //截图之前等待几秒，让验证码加载完毕
            threadWait(2000);
            commonService.getImgFileByScreenshot(webDriver, imgDom, configProperties.identifyCodeLocalPath);
            //解析验证码
            identifyCode = commonService.yunsuParseIdentifyCode(configProperties.identifyCodeLocalPath);
        }
    }

    public void threadWait(long mill) {
        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
