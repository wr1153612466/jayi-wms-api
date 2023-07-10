package cn.getech.wms.api.util;

import java.util.Random;

/**
 * 字符串处理工具类
 *
 * @author 陈泽胜
 */
public class StringUtil {
    // 获取随机数
    public static String getRandom(int model, int length) {
        String strBase = "";
        switch (model) {
            case 1:
                strBase = "0123456789";
                break;
            case 2:
                strBase = "abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ";
                break;
            case 3:
                strBase = "0123456789abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ";
                break;
            default:
                return "";
        }
        Random random = new Random();
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int n = random.nextInt(strBase.length());
            String oneString = strBase.substring(n, n + 1);
            str.append(oneString);
        }
        return str.toString();
    }
}
