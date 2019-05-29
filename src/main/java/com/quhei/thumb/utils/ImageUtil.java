package com.quhei.thumb.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * 图片压缩工具类
 *
 * @Author: hukun
 * @Date: 2019/5/28 11:44
 */
public class ImageUtil {

    // 图片默认缩放比率
    private static final double DEFAULT_SCALE = 0.25d;
    // 图片默认缩放质量
    private static final double DEFAULT_QUALITY = 0.6d;
    // 默认图片大小
    private static final int DEFAULT_SIZE = 250;


    /**
     * @param file     缩放文件
     * @param pathname 缩略图保存目录
     * @param scale    缩放比例   0-1之间
     * @param quality  图片质量   0-1之间   1最好
     * @return
     */
    public static boolean generateThumbnailByScale(String file, String pathname, double scale, double quality) {
        boolean flag = false;
        try {
            Thumbnails.of(file)
                    // 图片缩放率，不能和size()一起使用
                    .scale(scale)
                    // 图片缩放质量
                    .outputQuality(quality)
                    // 缩略图保存目录,该目录需存在，否则报错
                    .outputFormat("jpg")
                    .toFile(new File(pathname + scale + "缩放" + quality + "质量" + getFileName(file)));
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * @param file     缩放文件
     * @param pathname 缩略图保存目录
     * @return
     */
    public static boolean generateThumbnailByScale(String file, String pathname) {
        boolean flag = false;
        try {
            Thumbnails.of(file)
                    // 图片缩放质量
                    .outputQuality(DEFAULT_QUALITY)
                    // 图片缩放率，不能和size()一起使用
                    .scale(DEFAULT_SCALE)
                    // 缩略图保存目录,该目录需存在，否则报错
                    .outputFormat("jpg")
                    .toFile(new File(pathname + getFileName(file)));
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * @param file        缩放文件
     * @param pathname    缩略图保存目录
     * @param width       宽度
     * @param height      高度
     * @param quality     缩放质量
     * @param aspectRatio 是否按照比例
     * @return
     */
    public static boolean generateThumbnailBySize(String file, String pathname, int width, int height, double quality, boolean aspectRatio) {
        boolean flag = false;
        try {
            Thumbnails.of(file)
                    // 图片缩放率，不能和size()一起使用
                    .size(width, height)
                    // 图片缩放质量
                    .outputQuality(quality)
                    //keepAspectRatio   指定大小进行缩放(默认按照比例缩放)
                    .keepAspectRatio(aspectRatio)
                    // 缩略图保存目录,该目录需存在，否则报错
                    .outputFormat("jpg")
                    .toFile(new File(pathname + getFileName(file)));
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 按照固定大小缩略
     *
     * @param file     缩放文件
     * @param pathname 缩略图保存目录
     * @return
     */
    public static boolean generateThumbnailBySize(String file, String pathname) {
        boolean flag = false;
        try {
            Thumbnails.of(file)
                    // 图片缩放率，不能和size()一起使用
                    .size(DEFAULT_SIZE, DEFAULT_SIZE)
                    // 图片缩放质量
                    .outputQuality(1.0d)
                    // 缩略图保存目录,该目录需存在，否则报错
                    .outputFormat("jpg")
                    .toFile(new File(pathname + getFileName(file)));
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 根据文件扩展名判断文件是否图片格式
     *
     * @param fileName
     * @return
     */
    public static String getFileExtention(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("\\") + 1);
        return extension;

    }

    /**
     * 获取文件名(不带后缀名)
     *
     * @param file
     * @return
     */
    public static String getFileName(String file) {
        String caselsh = file.substring(file.lastIndexOf("\\") + 1);
        return caselsh.substring(0, caselsh.lastIndexOf("."));
    }

    public static byte[] uploadPicture(MultipartFile multipartfile) throws Exception {

        String suffixName;

        //获取图片后缀名,判断如果是png的话就不进行格式转换,因为Thumbnails存在转png->jpg图片变红bug
        String suffixNameOrigin = getExtensionName(multipartfile.getOriginalFilename());

        if ("png".equals(suffixNameOrigin)) {
            suffixName = "png";
        } else {
            suffixName = "jpg";
        }


        /*
         * size(width,height) 若图片横比1920小，高比1080小，不变
         * 若图片横比1920小，高比1080大，高缩小到1080，图片比例不变 若图片横比1920大，高比1080小，横缩小到1920，图片比例不变
         * 若图片横比1920大，高比1080大，图片按比例缩小，横为1920或高为1080
         * 图片格式转化为jpg,质量不变
         */
        BufferedImage image = ImageIO.read(multipartfile.getInputStream());

//        ImageWrapper imageWrapper = ImageReadHelper.read(multipartfile.getInputStream());


            if (!"png".equals(suffixName)) {
                image = Thumbnails.of(image).outputQuality(1f).scale(0.2f).outputFormat("jpg").imageType(BufferedImage.TYPE_INT_ARGB).asBufferedImage();
            } else {
                image = Thumbnails.of(image).outputQuality(1f).scale(0.2f).imageType(BufferedImage.TYPE_INT_ARGB).asBufferedImage();
            }

        image = apply(image);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, suffixName, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();

    }


    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    public static MultipartFile createImg(String url){
        try {
            // File转换成MutipartFile
            File file = new File(url);
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
            //注意这里面填啥，MultipartFile里面对应的参数就有啥，比如我只填了name，则
            //MultipartFile.getName()只能拿到name参数，但是originalFilename是空。
            return multipartFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage apply(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = newImage.createGraphics();
        graphic.setColor(Color.white);//背景设置为白色
        graphic.fillRect(0, 0, w, h);
        graphic.drawRenderedImage(img, null);
        graphic.dispose();
        return newImage;
    }

    /**
     *
     * @param data
     * @param path
     */
    public static void byte2image(byte[] data,String path){
        if(data.length<3||path.equals("")) return;
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }





    public static void main(String[] args) {
//        for (int i = 1; i < 10; i++) {
//            generateThumbnailByScale("C:\\Users\\tao\\Desktop\\测试图片\\长图\\test9.png", "C:\\Users\\tao\\Desktop\\测试图片\\长图\\", i / 10.0, 0.4d);
//            generateThumbnailByScale("C:\\Users\\tao\\Desktop\\测试图片\\长图\\test9.png", "C:\\Users\\tao\\Desktop\\测试图片\\长图\\", i / 10.0, 0.5d);
//            generateThumbnailByScale("C:\\Users\\tao\\Desktop\\测试图片\\长图\\test9.png", "C:\\Users\\tao\\Desktop\\测试图片\\长图\\", i / 10.0, 0.6d);
//        }
        String str = "C:\\Users\\tao\\Desktop\\test";
        for (int i = 1; i < 16; i++) {
            String img = "pngTest"+i+".png";
            MultipartFile file = createImg(str+"\\PNG原图\\"+img);
            try {
                long startTime = System.currentTimeMillis();
                byte[] bytes =uploadPicture(file);
                System.err.println(System.currentTimeMillis()-startTime);
                byte2image(bytes,str+"\\PNG缩略图\\"+img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
