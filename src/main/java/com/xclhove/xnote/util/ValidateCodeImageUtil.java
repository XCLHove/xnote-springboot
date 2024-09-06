package com.xclhove.xnote.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * @author xclhove
 */
public class ValidateCodeImageUtil {
    private static final Random RANDOM = new Random();
    /**
     * 验证码的宽
     */
    private static final int DEFAULT_WIDTH = 165;
    /**
     * 验证码的高
     */
    private static final int DEFAULT_HEIGHT = 45;
    /**
     * 验证码中夹杂的干扰线数量
     */
    private static final int DEFAULT_LINE_SIZE = 30;
    /**
     * 字体
     */
    private static final Font DEFAULT_FONT = new Font("Times New Roman", Font.ROMAN_BASELINE, 40);
    /**
     * 图片的contentType
     */
    public static final String CONTENT_TYPE = "image/png";
    
    /**
     * 随机颜色
     */
    private static Color generateRandomColor(int foregroundColor, int backgroundColor) {
        foregroundColor = Math.min(foregroundColor, 255);
        backgroundColor = Math.min(backgroundColor, 255);
        
        int r = foregroundColor + RANDOM.nextInt(backgroundColor - foregroundColor - 16);
        int g = foregroundColor + RANDOM.nextInt(backgroundColor - foregroundColor - 14);
        int b = foregroundColor + RANDOM.nextInt(backgroundColor - foregroundColor - 12);
        
        return new Color(r, g, b);
    }
    
    /**
     * 干扰线的绘制
     */
    private static void drawLine(Graphics graphics) {
        int x = RANDOM.nextInt(DEFAULT_WIDTH);
        int y = RANDOM.nextInt(DEFAULT_HEIGHT);
        int xl = RANDOM.nextInt(20);
        int yl = RANDOM.nextInt(10);
        graphics.drawLine(x, y, x + xl, y + yl);
        
    }
    
    /**
     * 字符串的绘制
     */
    private static void drawChar(Graphics graphics, char charOfCode, int charIndex) {
        graphics.setFont(DEFAULT_FONT);
        graphics.setColor(generateRandomColor(108, 190));
        graphics.translate(RANDOM.nextInt(3), RANDOM.nextInt(6));
        graphics.drawString(String.valueOf(charOfCode), 40 * charIndex + 10, 25);
    }
    
    
    /**
     * 生成随机图片
     */
    public static BufferedImage generateImage(String code){
        BufferedImage bufferedImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics imageGraphics = bufferedImage.getGraphics();
        imageGraphics.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        imageGraphics.setColor(generateRandomColor(105, 189));
        imageGraphics.setFont(DEFAULT_FONT);
        
        // 干扰线
        for (int i = 0; i < DEFAULT_LINE_SIZE; i++) {
            drawLine(imageGraphics);
        }
        // 依次绘制字符
        for (int i = 0; i < code.length(); i++) {
            drawChar(imageGraphics, code.charAt(i), i);
        }
        imageGraphics.dispose();
        
        return bufferedImage;
    }
    
    
    /**
     * 生成随机图片的base64编码字符串
     */
    public static String generateImageBase64(String code) throws IOException {
        BufferedImage image = generateImage(code);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        
        byte[] bytes = byteArrayOutputStream.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();
        String imageBase64 = "data:" + CONTENT_TYPE + ";base64," + encoder.encodeToString(bytes);
        return imageBase64;
    }
}
