package cn.getech.wms.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    /**
     * 将指定date对象转换为指定的时间格式
     *
     * @param date    需要转格式的date对象
     * @param pattern pattern
     * @return
     */
    public static String getTransferedString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
