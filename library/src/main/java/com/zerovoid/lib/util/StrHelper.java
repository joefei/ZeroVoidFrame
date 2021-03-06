package com.zerovoid.lib.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 操作字符串的工具类
 *
 * @version 1.0.20131206
 *
 * @author chenxiang
 *
 */
public class StrHelper {

    /**
     * 将生成字符串的SHA散列
     *
     * @param sourceStr
     *            要生成散列的字符串
     */
    public static String getStringSHA(String sourceStr) {
        byte[] strBytes = StrHelper.convertHexString(sourceStr);
        MessageDigest messagedigest;

        try {
            messagedigest = MessageDigest.getInstance("SHA-1");
            messagedigest.update(strBytes, 0, strBytes.length);

            return StrHelper.toHexString(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将字符串转成十六进制字节数组
     *
     *
     * @param str
     *            {@link String} 字符串
     * @return
     */
    public static byte[] convertHexString(String str) {

        byte digest[] = new byte[str.length() / 2];

        for (int i = 0; i < digest.length; i++) {
            String byteString = str.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    /**
     * 将十六进制字节数组转成字符串
     *
     *
     * @param b
     *            {@link byte[]} 字节数组
     * @return
     */
    public static String toHexString(byte b[]) {

        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);

            if (plainText.length() < 2)
                plainText = "0" + plainText;

            hexString.append(plainText);
        }

        return hexString.toString();
    }


    /**
     * 将HTML转义，转换为正确符号
     *
     *
     * @param htmlStr
     *            {@link String} HTML字符串
     * @return
     */
    public static String htmlDecode(String htmlStr) {
        String str = htmlStr;

        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&apos;", "'");
        str = str.replaceAll("&apos;", "'");
        str = str.replaceAll("&quot;", "\"");
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");

        return str;
    }

}
