package com.tyl.autodeliver.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-28 10:38
 **/
public class ImageUtil {

    /**
     * 根据文件流判断图片类型
     *
     * @param in
     * @return jpg/png/gif/bmp
     */
    public static String getPicType(InputStream in) {
        String type = "";
        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(in);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
            if (null != iter && iter.hasNext()) {
                ImageReader reader = iter.next();
                //获得图片的类型
                type = reader.getFormatName().toLowerCase();
                reader.setInput(imageInputStream, true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

}
