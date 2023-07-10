package cn.getech.wms.api.dto;

import lombok.Data;

/**
 * @author bocong.zheng@getech.cn
 * @since 2022/12/1
 */
@Data
public class UserTokenInfo {

    private String Message;
    private String MessageCode;
    private int LoginResultType;
    private Context Context;

    @Data
    public static class Context{

        private String UserToken;
    }
}
