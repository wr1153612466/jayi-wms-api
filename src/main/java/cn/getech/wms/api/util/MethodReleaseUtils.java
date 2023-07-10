//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.getech.wms.api.util;

import java.util.Optional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class MethodReleaseUtils {
    public MethodReleaseUtils() {
    }

    public static void setInvokeToken(String token, long start) {
        RequestAttributes sra = RequestContextHolder.getRequestAttributes();
        sra.setAttribute("methodInvokeToken", token, 0);
        sra.setAttribute("methodInvokeStart", start, 0);
    }

    public static String getInvokeToken() {
        RequestAttributes sra = RequestContextHolder.getRequestAttributes();
        return (String)sra.getAttribute("methodInvokeToken", 0);
    }

    public static long getInvokeStart() {
        RequestAttributes sra = RequestContextHolder.getRequestAttributes();
        return (Long)Optional.ofNullable((Long)sra.getAttribute("methodInvokeStart", 0)).orElse(System.currentTimeMillis());
    }

    public static long getInvokeElapsed() {
        return System.currentTimeMillis() - getInvokeStart();
    }
}
