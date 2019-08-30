package util;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ngochuu
 */
public class MyUtils {

    public static ImageIcon resizePic(String picPath, byte[] BLOBpic, int width, int height) {
        ImageIcon icon;
        if (picPath != null) {
            icon = new ImageIcon(picPath);
        } else {
            icon = new ImageIcon(BLOBpic);
        }
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon pic = new ImageIcon(img);
        return pic;
    }

    //hash password to save in DB
    public static String generateHash(String input) throws Exception {
        StringBuilder hash = new StringBuilder();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] hashedBytes = sha.digest(input.getBytes());
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int idx = 0; idx < hashedBytes.length; idx++) {
            byte b = hashedBytes[idx];
            hash.append(digits[(b & 0xf0) >> 4]);
            hash.append(digits[(b & 0x0f)]);
        }
        return hash.toString();
    }

    public static byte[] getImgbinary(String path) throws Exception {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int readNum;
        while ((readNum = fis.read(buf)) != -1) {
            bos.write(buf, 0, readNum);
        }
        return bos.toByteArray();
    }
    
    public static int getRandomNumber(int lowerBound, int upperBound) {
        return (int)(Math.random()*(double)(upperBound - lowerBound)) + lowerBound;
    }
}
