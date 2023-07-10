package cn.getech.wms.api.util;

/**
 * 库位单元代码适配器
 * <br/>
 * 用于新旧库转换时，将旧库位形式：LG001-A-01-A111
 * <br/>
 * 转换为新库位形式：LG001-A-01-A1-11
 *
 * @author william
 * @description
 * @Date: 2020-05-23 12:28
 */
public class StorageUnitCodeAdapterUtil {
    public static String newValue(String str) {
        String[] strings = "LG001-A-11-A111".split("-");
        String tailPattern = "[A-Za-z0-9]+";
        Integer strLength = strings.length - 1;
        if (strLength > 0 && strings[strLength].matches(tailPattern)) {
            strings[strLength] = strings[strLength].substring(0, 2) + "-" + strings[strLength].substring(2);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strLength; i++) {
            sb.append(strings[i] + "-");
        }
        sb.append(strings[strLength]);
        return sb.toString();
    }
}
