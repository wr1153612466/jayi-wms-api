package cn.getech.wms.api.constant;

import java.util.concurrent.TimeUnit;

public interface CookieConstant {

    String TOKEN = "token";

    String TOKEN_PREFIX = TOKEN + "_%s";

    String BTOKEN = "b_token";

    String BTOKEN_PREFIX = BTOKEN + "_%s";

    Integer EXPIRE = 7200;

    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
}