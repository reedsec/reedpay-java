package com.reedsec;

/**
 * Reedpay Base class
 */
public abstract class Reedpay {
    /**
     * 内测域名
     */
//    public static final String LIVE_API_BASE = "https://reedpay-a2.reedsec.com";
    /**
     * 外侧域名
     */
//    public static final String LIVE_API_BASE = "https://paydev.reedsec.com";
    /**
     * 正式域名
     */
    public static final String LIVE_API_BASE = "https://pay.reedsec.com";
    /**
     * interface url
     */
    public static  String INTERFACE_URL = "/secapi/v2/";

    /**
     * version
     */
    public static final String VERSION = "1.0.0";
    /**
     * api key
     */
    public static volatile String apiKey;

    public static volatile String apiVersion = VERSION;

    public static volatile String appId;

    public static String AcceptLanguage = "zh-CN";

    private static volatile boolean verifySSL = true;
    private static volatile String apiBase = LIVE_API_BASE+INTERFACE_URL;

    public static volatile String privateKey = "reedsec_secert";

    public static Boolean DEBUG = false;

    public static void overrideApiBase(final String overriddenApiBase) {
        apiBase = overriddenApiBase;
    }

    public static void setVerifySSL(boolean verify) {
        verifySSL = verify;
    }


    public static boolean getVerifySSL() {
        return verifySSL;
    }


    public static String getApiBase() {
        return apiBase;
    }


    public static void setApiBase(String apiBase) {
        Reedpay.apiBase = apiBase;
    }

    public static String getInterfaceUrl() {
        return INTERFACE_URL;
    }

    public static void setInterfaceUrl(String interfaceUrl) {
        INTERFACE_URL = interfaceUrl;
    }
}
