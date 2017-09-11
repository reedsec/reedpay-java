package com.reedsec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reedsec.exception.*;
import com.reedsec.model.*;
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
     * a2
     */
//    String APP_ID = "app_5929445bca802217a60b6344";
//    String API_KEY = "sk_test_5172bdcdfc4f8cbcca76c497e9f3892c";
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
//    String APP_ID = "app_590c525ee92b8365b209d959";
//    String API_KEY = "sk_test_9a75a2403672109f1c371fcf6d11b2f1";
//    String key = "Tester01Secret";

    /**
     * 瑞赛正式账号
     */
//     String APP_ID = "app_5916bfea0e6af151b43f5ed4";
//     String API_KEY = "sk_live_cc3af5255c48dd376d0b23127f5586c5";
//     String key = "tT3g%8Edk";

    /**
     * 有屋正式账号
     */
    String APP_ID = "app_5916d093b66c445ef51df1bd";
    String API_KEY = "sk_live_2ec95611a0d3ac05f4d8e9321f932eaa";
    String key = "mr9e$Xh6E";


    /**
     * a2上面配置的瑞赛正式账号测试退款
     */
//    String APP_ID = "app_5948e17cf424990ab8caaf8c";
//    String API_KEY = "sk_live_2f245c3a97c17ff0d7b327327e7ccb9f";
//    String key = "reedsec_secret";

    /**
     * a2平安通道测试账号
     */
//    String APP_ID = "app_5954cf40a6e9e311b98b1404";
//    String API_KEY = "sk_test_04a21ca26c610758fd35c483e18aefa1";
//    String key = "123456";

    /**
     * a2平安通道-瑞赛正式账号
     */
//    String APP_ID = "app_598825fb37c7ac02ee6edc4d";
//    String API_KEY = "sk_test_08848ac5f2aeeefd80d31e9fb549b859";
//    String key = "123456";

    /**
     * a2平安通道-儒山正式账号
     */
//    String APP_ID = "app_598917c237c7ac02ee6edc4e";
//    String API_KEY = "sk_live_168f6a294fa32c7ba096f38f05d7be17";
//    String key = "123456";

    /**
     * 瑞赛微猫-公众号直连
     */
//    String APP_ID = "app_5964c4d5bdc49a7a05620ea0";
//    String API_KEY = "sk_test_daaa0a957768290a73dfe8a8c672a96c";
//    String key = "reedsec_secret";

    /**
     * paydev 风控测试demo
     */
//    String APP_ID = "app_597aa3164809914e4787d252";
//    String API_KEY = "sk_test_ea626a0c6244210dd0c83c91a12924a4";
//    String key = "reedsec_secret";

    /**
     * 瑞赛-qq直连-a2
     */
//    String APP_ID = "app_599e4a4e5389804d74847b14";
//    String API_KEY = "sk_live_50c4d13e36d1af93feace3e93f0a9c70";
//    String key = "reedsec_secret";

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
        saleMap.put("subject", "支付测试");
//        saleMap.put("detail", "瑞赛");
        String orderNo = "" + new Date().getTime();
        saleMap.put("mch_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
//        saleMap.put("trade_type", "WX_QRCODE");// 微信扫码支付（商户被扫）
//        saleMap.put("trade_type", "WX_SCAN");// 微信刷卡支付（商户主扫）
//        saleMap.put("trade_type", "ALI_QRCODE");// 支付宝当面付扫码支付（商户被扫）
//        saleMap.put("trade_type", "ALI_SCAN");// 支付宝当面付条码支付（商户主扫）
//        saleMap.put("trade_type", "ALI_JSAPI");// 支付宝服务窗
//        saleMap.put("trade_type", "QQ_QRCODE");// QQ钱包扫码支付（商户被扫）
//        saleMap.put("trade_type", "QQ_SCAN");// QQ钱包刷卡支付（商户主扫）
        saleMap.put("trade_type", "WX_JSAPI");// 支付渠道 公众号支付
//        saleMap.put("trade_type", "QQ_APP");// QQ钱包APP支付
//        saleMap.put("trade_type", "WX_APP");// QQ钱包APP支付

        saleMap.put("app_id", APP_ID);

        JSONObject extra_json = new JSONObject();
        extra_json.put("client_ip", "127.0.0.1");// 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        extra_json.put("openid","oXnePxJ1820N_OvUitT6GNDt2fMA");// 公众号支付需要的openid
//        extra_json.put("auth_code","0017BQa02dpuiX0Q3hb02QuFa027BQai");
//        extra_json.put("auth_token","031bRCFC1r6A220BewEC19oNFC1bRCFh");
//        extra_json.put("openid","oCe4HwqIB58kvvpm0egQZ-K2qZi8");// 公众号支付需要的openid
//        extra_json.put("openid","oBu8c0VDKzSsfxIxL31YO0d68M9k");// 小程序支付需要的openid
//        extra_json.put("notify_url","https://paydev.reedsec.com/api/v2/webhooks/reedpay");

        //获取公众号授权code  https://portal.reedsec.com/api/reedsec/wechat
        // dev-portal.reedsec.com
//        try {
//            String code = WxpubOAuth.createOauthUrlForCode("wx31909c0dc402f1e9","https://dev-portal.reedsec.com",false);
//            String openid = WxpubOAuth.getOpenId("wx31909c0dc402f1e9","425c0f261aaa07ee0cd3b95ede67c7fd",code);
//            extra_json.put("openid",openid);// 公众号支付需要的openid
//            extra_json.put("show_url","https://reedsec/show_url");// 平安公众号支付需要的show_url
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//        extra_json.put("notify_url", "http://likangchun.cn/reedsec/testnotify");
//        extra_json.put("notify_url", "http://123456/reedsec/testnotify");
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
        String transaction_id = "pay_3y0o9uy7c3Z1h51znij8e"; //  pay_26ava3dpieB1gzjqjelo7
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
//        params.put("page", "1");
//        params.put("page_size", "5");
        params.put("app_id",APP_ID);
//        params.put("trade_type","wx_qrcode");
//        params.put("trade_status","PAID");

        JSONObject time_create_json = new JSONObject();
//        //订单开始时间time_created 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
//        time_create_json.put("lte", "20170609235959"); //yyyyMMddHHmmss
//        time_create_json.put("gte", "20170609000000"); //yyyyMMddHHmmss
//        params.put("time_created", time_create_json);

        //订单金额amount 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
//        JSONObject amount_json = new JSONObject();
//        amount_json.put("gte", "1");
//        params.put("amount", amount_json);

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

    /**
     * 余额查询
     */
    @Test
    public void BalanceQuery(){
        Map<String, Object> params = new HashMap<String, Object>();
        //查询参数
        params.put("app_id",APP_ID);
        //渠道参数  "WECHATPAY" 微信支付；"ALIPAY"支付宝；"TENPAY"财付通；"CMCB" 民生银行；"TIANXIAPAY"天下支付；"ALLINPAY"通联支付；"PINGAN"平安银行
        params.put("channel","TIANXIAPAY");
        //本查询交易的发起时间；同时日期可作为垫资可用额度的查询条件
//        params.put("timestamp", Utils.getCurrentTime());//时间格式 yyyyMMddHHmmss
        //BALANCE: 只查余额;ADVANCE: 查询垫资可用额度;QUOTA: 查询垫资限额
        params.put("fund_type","BALANCE");
        try {
            Balance balance = Balance.BalanceQuery(params);
            System.out.println(balance);
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
     * 单笔代付
     */
    @Test
    public void TransferCreate(){
        Map<String, Object> transferMap = new HashMap<String, Object>();
        transferMap.put("app_id", APP_ID);
        transferMap.put("amount", 1);//订单总金额, 人民币单位：分
        transferMap.put("description", "余额代付测试");
        String orderNo = "" + new Date().getTime();
        transferMap.put("mch_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        transferMap.put("trade_type", "B2C_TXPAY");//B2C_TXPAY-天下支付B2C单笔代付
        transferMap.put("pay_mode", "BALANCE"); //BALANCE - 余额支付（默认）;EBANK - 企业网银;LOAN - 垫资支付

        JSONObject extra = new JSONObject();
        extra.put("client_ip","127.0.0.1");
        extra.put("payee_name","黎康");
        extra.put("payee_account_id","6214837841635158");
        extra.put("payee_account_type","DEBIT"); //DEBIT 借记卡;CREDIT 贷记卡/信用卡;BUSINESS 对公账号
//        extra.put("bank_branch_code","308584000013");
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

    /**
     * 单笔代付查询
     */
    @Test
    public void TransferQuery(){
        String transaction_id = "pay_3y0o9uyifrO1h5e6eoflv";

        Transfer transfer = null;
        try {
            transfer = Transfer.query(transaction_id);
            String saleString = transfer.toString();
            System.out.println(saleString);
        }catch (RSException e){
            e.printStackTrace();
        }
    }

    @Test
    public void TransferQueryList() {
        Map<String, Object> params = new HashMap<String, Object>();
        //查询参数
        params.put("page", "1");
        params.put("page_size", "3");
//        params.put("app_id","58e884950aba5c7fc39f7384");
//        params.put("trade_type","wx_qrcode");
//        params.put("trade_status","PAID");

//        JSONObject time_create_json = new JSONObject();
        //订单开始时间time_created 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
//        time_create_json.put("gt","20170407121230"); //yyyyMMddHHmmss
//        params.put("time_created", time_create_json);

        //订单金额amount 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
//        JSONObject amount_json = new JSONObject();
//        amount_json.put("gte", "1");
//        params.put("amount", amount_json);

        System.out.println(params);
        TransferList transferList = null;
        try {
            transferList = Transfer.retrieve(params);
            System.out.println(transferList);
        } catch (RSException e) {
            e.printStackTrace();
        }
    }

    //创建退款
    @Test
    public void RefundCreate(){
        Map<String, Object> refundMap = new HashMap<String, Object>();
        refundMap.put("app_id", APP_ID);
        refundMap.put("transaction_id", "pay_26ava3e1xhE1h5uuvc8id");//交易id
        refundMap.put("refund_amount", 1);//退款金额 人民币单位：分
        refundMap.put("description", "退款");
        try {
            Refund refund = Refund.create(refundMap);
            if(Reedpay.DEBUG){
                System.out.println(refund.toString());
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            try {
                String outJson = mapper.writeValueAsString(refund);
                System.out.println(outJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (RSException e) {
            e.printStackTrace();
        }
    }

    //单笔退款查询
    @Test
    public void RefundQuery(){
        String transaction_id = "pay_26ava3dneaY1h3nlzquhh";
        String refund_id = "rf_26ava3dkifK1h4xhr47t2";
        Refund refund = null;
        try {
            refund = Refund.query(transaction_id,refund_id);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            try {
                String outJson = mapper.writeValueAsString(refund);

//                 outJson = getPrettyPrintGson().toJson(this);
                System.out.println(outJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }catch (RSException e){
            e.printStackTrace();
        }
    }

    //退款列表查询
    @Test
    public void RefundQueryList(){
        Map<String, Object> params = new HashMap<String, Object>();
        //查询参数
        params.put("transaction_id", "pay_26ava3dky2N1h1yufxot2");
        RefundList refundList = null;
        try {
            refundList = Refund.retrieve(params);
            System.out.println(refundList);

        }catch (RSException e){
            e.printStackTrace();
        }

    }
}
