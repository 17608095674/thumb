package com.quhei.thumb;

import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThumbApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testThumb() throws IOException {

        String filePath = "C:\\Users\\tao\\Desktop\\test.jpg";

        File file = new File(filePath);
        /**
         * 指定大小进行缩放
         * 若图片横比200小，高比300小，不变
         * 若图片横比200小，高比300大，高缩小到300，图片比例不变
         * 若图片横比200大，高比300小，横缩小到200，图片比例不变
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
         */
//        Thumbnails.of(filePath)
//                .size(20, 30)
//                .toFile(file.getAbsolutePath() + "_200x300.jpg");

        /**
         * 按照比例进行缩放
         * scale(比例)
         * */
//        Thumbnails.of(filePath)
//                .scale(0.25f)
//                .toFile(file.getAbsolutePath() + "_25%.jpg");

        /**
         *  不按照比例，指定大小进行缩放
         *  keepAspectRatio(false) 默认是按照比例缩放的
         * */
        Thumbnails.of(filePath)
                .size(10, 20)
                .keepAspectRatio(false)
                .toFile(file.getAbsolutePath() + "_200x200.jpg");

        /**
         *  输出图片到流对象
         *
         * */
        OutputStream os = new FileOutputStream(file.getAbsolutePath() + "_OutputStream.png");
        Thumbnails.of(filePath)
                .size(1280, 1024)
                .toOutputStream(os);

//        /**
//         *  输出图片到BufferedImage
//         * **/
//        BufferedImage thumbnail = Thumbnails.of(filePath)
//                .size(1280, 1024)
//                .asBufferedImage();
//        ImageIO.write(thumbnail, "jpg", new File(file.getAbsolutePath()+"_BufferedImage.jpg"));

    }




}
