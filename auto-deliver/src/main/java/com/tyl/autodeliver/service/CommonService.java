package com.tyl.autodeliver.service;

import com.alibaba.fastjson.JSONObject;
import com.tyl.autodeliver.entity.Account;
import com.tyl.autodeliver.properties.ConfigProperties;
import com.tyl.autodeliver.properties.YunsuProperties;
import com.tyl.autodeliver.utils.ExcelUtil;
import com.tyl.autodeliver.utils.PrintUtil;
import com.tyl.autodeliver.utils.XmlUtil;
import com.tyl.autodeliver.utils.YunSuUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.internal.WrapsDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-25 13:16
 **/
@Component
public class CommonService {

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private YunsuProperties yunsuProperties;

    private static File createFile(String filePath) {
        //创建文件
        File file = new File(filePath);
        try {
            //若不存在，则创建
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void replaceColorAndSave(String imgUrl, String localPath) throws IOException {
        URL uri = new URL(imgUrl);
        BufferedImage bi = ImageIO.read(uri.openStream());
        if (bi == null) {
            return;
        }
        Color wColor = new Color(255, 255, 255);
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int color = bi.getRGB(i, j);
                Color oriColor = new Color(color);
                int red = oriColor.getRed();
                int greed = oriColor.getGreen();
                int blue = oriColor.getBlue();
                //粉色
                /*if (greed < 190 || blue < 190) {

                } else {
                    if (red == 255 && greed > 180 && blue > 180) {
                        bi.setRGB(i, j, wColor.getRGB());
                    }
                }*/
                /*if (i>bi.getWidth()-200 && j>bi.getHeight()-25) {
                    bi.setRGB(i, j, Color.white.getRGB());
                }*/
            }
        }
        String type = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
        if (StringUtils.isNotEmpty(type)
                && (type.equals("jpg") || type.equals("jpeg") || type.equals("png"))) {
            ImageIO.write(bi, type, createFile(localPath));
        } else {
            ImageIO.write(bi, "jpg", createFile(localPath));
        }
    }

    public static void gaosiHandler(String imgUrl, String localPath) throws IOException {
        float[] matrix = new float[400];
        for (int i = 0; i < 400; i++)
            matrix[i] = 1.0f / 400.0f;
        BufferedImageOp op = new ConvolveOp(new Kernel(20, 20, matrix), ConvolveOp.EDGE_NO_OP, null);
        BufferedImage img = ImageIO.read(createFile(localPath));
        BufferedImage dest = img.getSubimage(0, 0,
                20, 20);
        BufferedImage bi = op.filter(img, dest);
        String type = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
        if (StringUtils.isNotEmpty(type)
                && (type.equals("jpg") || type.equals("jpeg") || type.equals("png"))) {
            ImageIO.write(bi, type, createFile(localPath));
        } else {
            ImageIO.write(bi, "jpg", createFile(localPath));
        }
    }

    /**
     * @param imgUrl
     * @return
     */
//    public String tess4JParseIdentifyCode(String imgUrl, String localPath) {
//        String result = null;
//        try {
//            //保存验证码图片
//            saveRemoteImg(imgUrl, localPath);
//            File imageFile = new File(localPath);
//            //使用tess4J识别
//            Tesseract tesseract = new Tesseract();
//            //设置trainedData路径
//            File tessDataFolder = LoadLibs.extractTessResources(configProperties.trainedDataPath);
//            tesseract.setDatapath(tessDataFolder.getAbsolutePath());
//            tesseract.setLanguage(configProperties.trainedLanguage);
//            result = tesseract.doOCR(imageFile);
//        } catch (TesseractException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    public static void main(String[] args) throws IOException {
        String imgUrl = "https://timg01.bdimg.com/timg?pacompress&imgtype=1&sec=1439619614&autorotate=1&di=8f28cd22861463a3b2e1a79d73d25b18&quality=90&size=b870_10000&src=http%3A%2F%2Fpic.rmb.bdstatic.com%2F15279182854e9fa1eccbc33565f5001c37997c767c.jpeg";
        String localPath = "D:\\kuaichuan-auto-deliver-1.0\\data\\img\\img1.jpg";
        replaceColorAndSave(imgUrl, localPath);
    }

    /**
     * 获取系统真实路径
     *
     * @param javaPath
     * @return
     */
    /*public String getRealPath(String javaPath) {
        String line = File.separator;
        String path = ClassUtils.getDefaultClassLoader().getResource(javaPath).getPath();
        //windows下
        if ("\\".equals(line)) {
            path = path.replace("/", "\\");
        }
        //linux下
        if ("/".equals(line)) {
            path = path.replace("\\", "/");
        }
        return path;
    }*/

    public List<Account> getAccounts() {
        List<Account> list = ExcelUtil.parse(Account.class,
                configProperties.accountPath,
                configProperties.accountFileName);
        return list;
    }

    /**
     * 截屏
     *
     * @param driver
     * @param element
     * @param path
     */
    /*public void screenShotForElement(WebDriver driver,
                                     WebElement element, String path) {
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);
        try {
            Point point = element.getLocation();
            Dimension dimension = element.getSize();
            Rectangle rect = new Rectangle(point, dimension);
            BufferedImage img = ImageIO.read(scrFile);
            BufferedImage dest = img.getSubimage(point.getX(), point.getY(),
                    rect.width, rect.height);
            ImageIO.write(dest, "jpg", scrFile);
            FileUtils.copyFile(scrFile, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 根据验证码图片的url获取验证码
     *
     * @param url
     * @return
     */
    public void saveRemoteImg(String url, String localPath) throws MalformedURLException {
        URL uri = new URL(url);
        File file = createFile(localPath);
        try (InputStream inputStream = uri.openStream();
             BufferedInputStream bis = new BufferedInputStream(inputStream);
             FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int size;
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用云速api识别验证码
     *
     * @param path
     * @return
     */
    public String yunsuParseIdentifyCode(String path) {
        PrintUtil.println("开始识别验证码");
        String door = "";
        try {
            String imgRlt = YunSuUtil.createByPost(
                    yunsuProperties.account,
                    yunsuProperties.password,
                    yunsuProperties.typeId,
                    yunsuProperties.timeout,
                    yunsuProperties.softId,
                    yunsuProperties.softKey,
                    getImgBytes(path));
            JSONObject imgRltJson = XmlUtil.xml2Json(imgRlt);
            if ((imgRltJson.getString("Result") == null) || (imgRltJson.getString("Error_Code") != null)) {
                PrintUtil.println(imgRltJson);
                throw new Exception();
            }
            door = imgRltJson.getString("Result");
        } catch (Exception e) {
            PrintUtil.println("云识别验证码异常");
        }
        PrintUtil.println("云识别验证码结果为：" + door);
        return door;
    }

    /**
     * 图片转字节数组
     *
     * @param path
     * @return
     * @throws Exception
     */
    public byte[] getImgBytes(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.flush();
        return bos.toByteArray();
    }

    /**
     * 精确截屏
     *
     * @param driver
     * @param element
     * @param path
     * @return
     */
    public File getImgFileByScreenshot(WebDriver driver, WebElement element, String path) throws IOException {
        //等验证码加载完毕
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        //截取整个页面
        File scrFile = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage img = ImageIO.read(scrFile);
        //获取浏览器尺寸与截图的尺寸
        int browserWindowWidth = driver.manage().window().getSize().getWidth();
        int screenshotWidth = img.getWidth();
        Double scale = (double) browserWindowWidth / screenshotWidth;
        int eleWidth = element.getSize().getWidth();
        int eleHeight = element.getSize().getHeight();
        Point point = element.getLocation();
        //获得元素的坐标
        Long subImgX = Math.round(point.getX() / scale);
        Long subImgY = Math.round(point.getY() / scale);
        Long offsetX = Math.round(8 / scale);
        Long offsetY = Math.round(3 / scale);
        //获取元素的宽高
        Long subImgWight = Math.round(eleWidth / scale);
        Long subImgHeight = Math.round(eleHeight / scale);
        //精准的截取元素图片，
        BufferedImage dest = img.getSubimage(subImgX.intValue() + offsetX.intValue(), subImgY.intValue() + offsetY.intValue(), subImgWight.intValue(), subImgHeight.intValue());
        ImageIO.write(dest, "png", createFile(path));
        //FileUtils.copyFile(scrFile, createFile(path));
        return scrFile;
    }
}
