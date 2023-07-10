package cn.getech.wms.api.config;

import com.kingdee.bos.webapi.entity.IdentifyInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/11/23
 */
@Data
@ConfigurationProperties(prefix = "kingdee-identify")
@Component
public class K3IdentifyConfig extends IdentifyInfo {

    private String warehouseUrl;

    private String plmDownLoadUrl;

}
