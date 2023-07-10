package cn.getech.wms.api.config;

import com.kingdee.bos.webapi.sdk.K3CloudApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/23
 */
@Component
public class K3Configuration {

    @Autowired
    private K3IdentifyConfig config;

    @Bean
    public K3CloudApi initK3CloudApi(){
        return new K3CloudApi(config);
    }
}
