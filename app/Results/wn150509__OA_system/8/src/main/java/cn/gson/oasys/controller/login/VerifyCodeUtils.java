package cn.gson.oasys.controller.login;
 import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
public class VerifyCodeUtils {

 public  String VERIFY_CODES;

 private  Random random;


public void shearX(Graphics g,int w1,int h1,Color color){
    int period = random.nextInt(2);
    boolean borderGap = true;
    int frames = 1;
    int phase = random.nextInt(2);
    for (int i = 0; i < h1; i++) {
        double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
        g.copyArea(0, i, w1, 1, (int) d, 0);
        if (borderGap) {
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }
    }
}


public String generateVerifyCode(int verifySize,String sources){
    if (sources == null || sources.length() == 0) {
        sources = VERIFY_CODES;
    }
    int codesLen = sources.length();
    Random rand = new Random(System.currentTimeMillis());
    StringBuilder verifyCode = new StringBuilder(verifySize);
    for (int i = 0; i < verifySize; i++) {
        verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
    }
    return verifyCode.toString();
}


public void shear(Graphics g,int w1,int h1,Color color){
    shearX(g, w1, h1, color);
    shearY(g, w1, h1, color);
}


public String outputVerifyImage(int w,int h,OutputStream os,int verifySize){
    String verifyCode = generateVerifyCode(verifySize);
    outputImage(w, h, os, verifyCode);
    return verifyCode;
}


public Color getRandColor(int fc,int bc){
    if (fc > 255)
        fc = 255;
    if (bc > 255)
        bc = 255;
    int r = fc + random.nextInt(bc - fc);
    int g = fc + random.nextInt(bc - fc);
    int b = fc + random.nextInt(bc - fc);
    return new Color(r, g, b);
}


public void outputImage(int w,int h,OutputStream os,String code){
    int verifySize = code.length();
    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Random rand = new Random();
    Graphics2D g2 = image.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Color[] colors = new Color[5];
    Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW };
    float[] fractions = new float[colors.length];
    for (int i = 0; i < colors.length; i++) {
        colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
        fractions[i] = rand.nextFloat();
    }
    Arrays.sort(fractions);
    // 设置边框色
    g2.setColor(Color.GRAY);
    g2.fillRect(0, 0, w, h);
    Color c = getRandColor(200, 250);
    // 设置背景色
    g2.setColor(c);
    g2.fillRect(0, 2, w, h - 4);
    // 绘制干扰线
    Random random = new Random();
    // 设置线条的颜色
    g2.setColor(getRandColor(160, 200));
    for (int i = 0; i < 20; i++) {
        int x = random.nextInt(w - 1);
        int y = random.nextInt(h - 1);
        int xl = random.nextInt(6) + 1;
        int yl = random.nextInt(12) + 1;
        g2.drawLine(x, y, x + xl + 40, y + yl + 20);
    }
    // 添加噪点
    // 噪声率
    float yawpRate = 0.05f;
    int area = (int) (yawpRate * w * h);
    for (int i = 0; i < area; i++) {
        int x = random.nextInt(w);
        int y = random.nextInt(h);
        int rgb = getRandomIntColor();
        image.setRGB(x, y, rgb);
    }
    // 使图片扭曲
    shear(g2, w, h, c);
    g2.setColor(getRandColor(100, 160));
    int fontSize = h - 4;
    Font font = new Font("Algerian", Font.ITALIC, fontSize);
    g2.setFont(font);
    char[] chars = code.toCharArray();
    for (int i = 0; i < verifySize; i++) {
        AffineTransform affine = new AffineTransform();
        affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verifySize) * i + fontSize / 2, h / 2);
        g2.setTransform(affine);
        g2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + fontSize / 2 - 10);
    }
    g2.dispose();
    ImageIO.write(image, "jpg", os);
}


public int getRandomIntColor(){
    int[] rgb = getRandomRgb();
    int color = 0;
    for (int c : rgb) {
        color = color << 8;
        color = color | c;
    }
    return color;
}


public int[] getRandomRgb(){
    int[] rgb = new int[3];
    for (int i = 0; i < 3; i++) {
        rgb[i] = random.nextInt(255);
    }
    return rgb;
}


public void shearY(Graphics g,int w1,int h1,Color color){
    // 50;
    int period = random.nextInt(40) + 10;
    boolean borderGap = true;
    int frames = 20;
    int phase = 7;
    for (int i = 0; i < w1; i++) {
        double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
        g.copyArea(i, 0, 1, h1, 0, (int) d);
        if (borderGap) {
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + h1, i, h1);
        }
    }
}


}