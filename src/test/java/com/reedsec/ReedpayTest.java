package com.reedsec;

import com.reedsec.exception.*;
import com.reedsec.model.Sale;
import com.reedsec.model.SaleList;
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

    // reedpay-a2 wechat direct
    String APP_ID = "app_58e74d9b58c448597cfcd19f";
    String  API_KEY = "sk_test_8c70c3b2f810732a68392a09ffe45171";

    // localhost tianxia indirect
//    String APP_ID = "app_58e884950aba5c7fc39f7384";
//    String API_KEY = "sk_test_c67e0207aac38cbbd7f20b63d5cb05d4";


    @Before
    public void init(){
        Reedpay.apiKey = API_KEY;
        Reedpay.appId = APP_ID;
        Reedpay.DEBUG = true;
    }

    /**
     * 测试支付
     */
    @Test
    public  void SaleCreateTest(){
        Sale sale = null;
        Map<String, Object> saleMap = new HashMap<String, Object>();
        saleMap.put("amount", 1);//订单总金额, 人民币单位：分
//        saleMap.put("currency", "CNY");
        saleMap.put("subject", "test");
        saleMap.put("detail", "test");
        String orderNo = ""+new Date().getTime();
        saleMap.put("mch_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
//        saleMap.put("trade_type", "WX_QRCODE");// 微信扫码支付（商户被扫）
        saleMap.put("trade_type", "WX_SCAN");// 微信刷卡支付（商户主扫）
//        saleMap.put("trade_type", "ALI_QRCODE");// 支付宝当面付扫码支付（商户被扫）
//        saleMap.put("trade_type", "ALI_SCAN");// 支付宝当面付条码支付（商户主扫）
//        saleMap.put("trade_type", "QQ_QRCODE");// QQ钱包扫码支付（商户被扫）
//        saleMap.put("trade_type", "QQ_SCAN");// QQ钱包刷卡支付（商户主扫）
//        saleMap.put("trade_type", "wx_jsapi");// 支付渠道 公众号支付

        saleMap.put("app_id", APP_ID);

        JSONObject extra_json = new JSONObject();
        extra_json.put("client_ip","127.0.0.1");// 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        extra_json.put("auth_code","130164458163232562");
//        extra_json.put("openid","ouNu5wBrEdsxnXBegLrZdDPhE3yY");// 公众号支付需要的openid

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
    public void SaleQueryTest(){
        String transaction_id = "pay_26ava3dp8eH1gvgxccek3";
//        String transaction_id = "pay_26ava3dyctT1gvwbc4nm9";

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
    public void SaleQueryListTest(){
        Map<String, Object> params = new HashMap<String, Object>();
        //查询参数
//        params.put("page","1");
        params.put("page_size","2");
        params.put("app_id","58e884950aba5c7fc39f7384");
//        params.put("trade_type","wx_qrcode");
        params.put("trade_status","PAID");

        JSONObject time_create_json = new JSONObject();
        //订单开始时间time_created 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
//        time_create_json.put("gt","20170407121230"); //yyyyMMddHHmmss
        params.put("time_created",time_create_json);

        //订单金额amount 1.eq 等于 2.gt大于 3.gte大于等于 4.lt小于 5.lte小于等于
        JSONObject amount_json = new JSONObject();
        amount_json.put("gte","1");
        params.put("amount",amount_json);

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

    @Test public void test(){
        Map<String, Object> saleMap = new HashMap<String, Object>();
        saleMap.put("amount", 1);//订单总金额, 人民币单位：分
//        saleMap.put("currency", "CNY");
        saleMap.put("subject", "test");
        saleMap.put("detail", "test");
        String orderNo = ""+new Date().getTime();
        saleMap.put("mch_order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        saleMap.put("trade_type", "WX_QRCODE");// 微信扫码支付（商户被扫）
        saleMap.put("app_id", APP_ID);
        JSONObject extra_json = new JSONObject();
        extra_json.put("client_ip","127.0.0.1");// 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        saleMap.put("extra", extra_json.toString());

        //排序后的参数
        String get_sign = ReedpaySignature.get_signWithMap(saleMap);

        StringBuffer sb = new StringBuffer(get_sign);
        sb.append(Reedpay.privateKey);
        String sign = ReedpaySignature.MD5_lowCase(sb.toString());
        System.out.println("第一种加密sign:"+sign);
        boolean s = ReedpaySignature.MD5_verifyData(get_sign,sign,Reedpay.privateKey);

        if (s) {
            System.out.println("验签结果：通过");
        } else {
            System.out.println("验签结果：失败");
            return;
        }

    }

}
