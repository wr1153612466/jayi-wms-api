//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.getech.wms.api.context;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class PorosContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public PorosContextHolder() {
    }

    public static <T> T getBean(Class<T> cla) {
        return applicationContext.getBean(cla);
    }

    public static <T> T getBean(String name, Class<T> cal) {
        return applicationContext.getBean(name, cal);
    }

    public static String getProperty(String key) {
        return ((Environment)applicationContext.getBean(Environment.class)).getProperty(key);
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return null == attributes ? null : attributes.getRequest();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (PorosContextHolder.applicationContext == null) {
            PorosContextHolder.applicationContext = applicationContext;
        }

    }
}
