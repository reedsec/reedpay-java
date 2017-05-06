package com.reedsec;

import com.reedsec.exception.*;
import com.reedsec.model.NotifyVerify;
import com.reedsec.model.Sale;
import com.reedsec.model.SaleList;
import com.reedsec.model.Transfer;
import com.reedsec.util.ReedpaySignature;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/6 0006.
 */
public class ReedpayTest {

    /**
     * 内测账号
     */
    // reedpay-a2 wechat direct
//    String APP_ID = "app_58fa1b4123f2645b86ad6e4c";
//    String  API_KEY = "sk_test_13c3c30c8732d7ea472296a21b362605";
//    String key = "reedsec_secert";

//    String API_KEY ="sk_test_03375e7abcc3b35b5b7e60bbe9fc9d2c";
//    String  APP_ID = "app_58f4ad669c29fa34e65d4ba6";

    // localhost tianxia indirect
//    String APP_ID = "app_58e884950aba5c7fc39f7384";
//    String API_KEY = "sk_test_c67e0207aac38cbbd7f20b63d5cb05d4";
//    String key = "reedsec_secert";

    /**
     * 外测账号
     */
//    String APP_ID = "app_58eb5a9d5b3e556a816a1b94";
//    String API_KEY = "sk_test_8a21a27fc35bc96694656f1213baae2d";
//    String key = "Tester01Secret";


//    微信公众号支付（WX_JSAPI）测试账号
//    String APP_ID = "app_58eb5aed5b3e556a816a1b96";
//    String API_KEY = "sk_test_8166314a2a315f25eddc035023f9acfa";
//    String  key = "Tester02Secret";

    /**
     * a2代付账号
     */
//    String APP_ID = "app_590a92b7da1f4c40e2598981";
//    String API_KEY = "sk_test_4126bcba3b191733c7e4f97a6bd2482e";
//    String key = "reedsec_secret";

    /**
     * dev代付账号
     */
    String APP_ID = "app_590c525ee92b8365b209d959";
    String API_KEY = "sk_test_9a75a2403672109f1c371fcf6d11b2f1";
    String key = "Tester01Secret";

    @Before
    public void init() {
        Reedpay.apiKey = API_KEY;
        Reedpay.appId = APP_ID;
        Reedpay.DEBUG = true;
        Reedpay.privateKey = key;

    }

    /**
     * 测试支付
     */
    @Test
    public void SaleCreateTest() {
        Sale sale = null;
        Map<String, Object> saleMap = new HashMap<String, Object>();
        saleMap.put("amount", 1);//订单总金额, 人民币单位：分
        saleMap.put("currency", "CNY");
        saleMap.put("subject", "test");
        saleMap.put("detail", "test");
        String orderNo = "" + new Date().getTime();
        saleMap.put("mch_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        saleMap.put("trade_type", "WX_QRCODE");// 微信扫码支付（商户被扫）
//        saleMap.put("trade_type", "WX_SCAN");// 微信刷卡支付（商户主扫）
//        saleMap.put("trade_type", "ALI_QRCODE");// 支付宝当面付扫码支付（商户被扫）
//        saleMap.put("trade_type", "ALI_SCAN");// 支付宝当面付条码支付（商户主扫）
//        saleMap.put("trade_type", "QQ_QRCODE");// QQ钱包扫码支付（商户被扫）
//        saleMap.put("trade_type", "QQ_SCAN");// QQ钱包刷卡支付（商户主扫）
//        saleMap.put("trade_type", "WX_JSAPI");// 支付渠道 公众号支付

        saleMap.put("app_id", APP_ID);

        JSONObject extra_json = new JSONObject();
        extra_json.put("client_ip", "127.0.0.1");// 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
//        extra_json.put("auth_code","130125439162719888");
//        extra_json.put("openid","oCe4HwqIB58kvvpm0egQZ-K2qZi8");// 公众号支付需要的openid
//        extra_json.put("notify_url","https://paydev.reedsec.com/api/v2/webhooks/reedpay");

        //获取公众号授权code  https://portal.reedsec.com/api/reedsec/wechat
        // dev-portal.reedsec.com
//        try {
//            String code = WxpubOAuth.createOauthUrlForCode("wx31909c0dc402f1e9","https://dev-portal.reedsec.com",false);
//            String openid = WxpubOAuth.getOpenId("wx31909c0dc402f1e9","425c0f261aaa07ee0cd3b95ede67c7fd",code);
//            extra_json.put("openid",openid);// 公众号支付需要的openid
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        extra_json.put("notify_url", "http://likangchun.cn/reedsec/testnotify");
        saleMap.put("extra", extra_json.toString());
        try {
            // 发起 sale 创建请求
            sale = Sale.create(saleMap);
            // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
            String saleString = sale.toString();
            System.out.println(saleString);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试单个订单查询
     */
    @Test
    public void SaleQueryTest() {
        String transaction_id = "pay_26ava3dr4oQ1gyb7rfj40";
//        String transaction_id = "pay_26ava3dyctT1gvwbc4nm9";pay_26ava3dyv5F1gwvk6nycd

        Sale sale = null;
        try {
            sale = Sale.query(transaction_id);
            String saleString = sale.toString();
            System.out.println(saleString);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试订单列表查询
     */
    @Test
    public void SaleQueryListTest() {
        Map<String, Object> params = new HashMap<String, Object>();
        //查询参数
        params.put("page", "1");
        params.put("page_size", "5");
//        params.put("app_id","58e884950aba5c7fc39f7384");
//        params.put("trade_type","wx_qrcode");
//        params.put("trade_status","PAID");

        JSONObject time_create_json = new JSONObject();
        //订单开始时间time_created 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
//        time_create_json.put("gt","20170407121230"); //yyyyMMddHHmmss
        params.put("time_created", time_create_json);

        //订单金额amount 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
        JSONObject amount_json = new JSONObject();
        amount_json.put("gte", "1");
        params.put("amount", amount_json);

        System.out.println(params);
        SaleList saleList = null;
        try {
            saleList = Sale.retrieve(params);
            System.out.println(saleList);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Map<String, Object> saleMap = new HashMap<String, Object>();
        saleMap.put("amount", 1);//订单总金额, 人民币单位：分
//        saleMap.put("currency", "CNY");
        saleMap.put("subject", "test");
        saleMap.put("detail", "test");
        String orderNo = "" + new Date().getTime();
        saleMap.put("mch_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        saleMap.put("trade_type", "WX_QRCODE");// 微信扫码支付（商户被扫）
        saleMap.put("app_id", APP_ID);
        JSONObject extra_json = new JSONObject();
        extra_json.put("client_ip", "127.0.0.1");// 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        saleMap.put("extra", extra_json.toString());

        //排序后的参数
        String get_sign = ReedpaySignature.get_signWithMap(saleMap);

        StringBuffer sb = new StringBuffer(get_sign);
        sb.append(Reedpay.privateKey);
        String sign = ReedpaySignature.MD5_lowCase(sb.toString());
        System.out.println("第一种加密sign:" + sign);
        boolean s = ReedpaySignature.MD5_verifyData(get_sign, sign, Reedpay.privateKey);

        if (s) {
            System.out.println("验签结果：通过");
        } else {
            System.out.println("验签结果：失败");
            return;
        }

    }

    @Test
    public void Notify() throws Exception {
        String eventPath = "res/webhooks_raw_post_data.json";
        String webhooksRawPostData = NotifyVerify.getStringFromFile(eventPath);
        System.out.println("------- POST 原始数据 -------");
        System.out.println(webhooksRawPostData);
        boolean b = NotifyVerify.verify_signature(webhooksRawPostData);
        System.out.println(b);

//        boolean sign_istrue = ReedpaySignature.getsign(webhooksRawPostData);

//        String str ="data={\"transaction_id\":\"pay_26ava3dopjN1gxl4zfkt0\",\"app_id\":\"app_58eb5a9d5b3e556a816a1b94\",\"order_no\":\"1020170427337061410375064004\",\"mch_order_no\":\"1493297498083\",\"amount\":1,\"subject\":\"test\",\"detail\":\"test\",\"trade_type\":\"WX_QRCODE\",\"currency\":\"CNY\",\"channel_trade_no\":\"1021800776625170427000042890\",\"trade_status\":\"PAID\",\"time_created\":\"20170427205104\",\"time_expire\":\"20170428205104\",\"time_success\":\"20170427205155\",\"amount_refunded\":0,\"extra\":{\"client_ip\":\"127.0.0.1\",\"notify_url\":\"http://likangchun.cn/reedsec/testnotify\"},\"refunded\":false,\"paid\":false}&retry=0&timestamp=1493297516&type=SALETester01Secret";
//        String sign = ReedpaySignature.MD5_lowCase(str);
//        System.out.println(sign);
    }

    @Test
    public void TransferCreate(){
        Map<String, Object> transferMap = new HashMap<String, Object>();
        transferMap.put("app_id", APP_ID);
        transferMap.put("amount", 1);//订单总金额, 人民币单位：分
        transferMap.put("description", "test");
        String orderNo = "" + new Date().getTime();
        transferMap.put("mch_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        transferMap.put("trade_type", "b2c_txpay");

        JSONObject extra = new JSONObject();
        extra.put("client_ip","127.0.0.1");
        extra.put("payee_name","国彩支付");
        extra.put("payee_account_id","6225887805085784");
        extra.put("payee_account_type","BUSINESS");
        extra.put("bank_branch_code","308584000013");
//        extra.put("bank_name","");
//        extra.put("bank_branch_name","");

       transferMap.put("extra", extra.toString());
        try {
            Transfer transfer = Transfer.create(transferMap);
            String transferString = transfer.toString();
            System.out.println(transferString);
        } catch (RSException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TransferQuery(){

    }
}
