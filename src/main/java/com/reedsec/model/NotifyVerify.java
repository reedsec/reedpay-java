package com.reedsec.model;

import com.reedsec.Reedpay;
import com.reedsec.net.APIResource;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static com.reedsec.util.ReedpaySignature.get_signWithMap;

/**
 * Created by lik@reedsec.com on 2017/4/13 0013.
 */
public class NotifyVerify extends APIResource{

    private static String eventPath = "res/webhooks_raw_post_data.json";

    /**
     * 通知参数
     */

    String timestamp;
    String sign_type;
    String sign;
    String retry;
    String type;
    JSONObject data;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    /**
     * 读取文件, 部署 web 程序的时候, 签名和验签内容需要从 request 中获得
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getStringFromFile(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
        BufferedReader bf = new BufferedReader(inReader);
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = bf.readLine();
            if (line != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } while (line != null);

        return sb.toString();
    }


    /**
     * 消息异步通知验签
     * @param body
     */
    public static boolean verify_signature(String body){

        JSONObject json_body = new JSONObject(body);
        String sign_type = json_body.getString("sign_type");
        String sign = json_body.getString("sign");
        if(Reedpay.DEBUG){
            System.out.println("签名类型为："+sign_type);
            System.out.println("签名为："+sign);
        }
        Map<String,Object> json_map = new HashMap<String, Object>();

            json_map.put("data",json_body.get("data"));
            json_map.put("timestamp",json_body.get("timestamp"));
            json_map.put("retry",json_body.get("retry"));
            json_map.put("type",json_body.get("type"));


        String sign_varify = get_signWithMap(json_map);
        if (sign.equals(sign_varify)) {
            if (Reedpay.DEBUG){
                System.out.println("验签结果：通过");
            }
            return true;
        } else {
            if (Reedpay.DEBUG) {
                System.out.println("验签结果：失败");
            }
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        String webhooksRawPostData = getStringFromFile(eventPath);
        System.out.println("------- POST 原始数据 -------");
        System.out.println(webhooksRawPostData);
        boolean b = verify_signature(webhooksRawPostData);
        System.out.println(b);

//        String sign_sb = "data={\"transaction_id\":\"pay_26ava3dy66V1gxk5xprm9\",\"app_id\":\"app_58eb5a9d5b3e556a816a1b94\",\"order_no\":\"1020170427337062636653872001\",\"mch_order_no\":\"1493276271325\",\"amount\":1,\"subject\":\"test\",\"trade_type\":\"WX_QRCODE\",\"currency\":\"CNY\",\"channel_trade_no\":\"1021800776625170427000042645\",\"trade_status\":\"PAID\",\"time_created\":\"20170427145752\",\"time_expire\":\"20170428145752\",\"time_success\":\"20170427145817\",\"amount_refunded\":0,\"extra\":{\"client_ip\":\"127.0.0.1\",\"notify_url\":\"https://reedpay-a2.reedsec.com/api/v2/webhooks/reedpay\"},\"refunded\":false,\"paid\":false}&retry=0&timestamp=1493276297&type=SALETester01Secret";
//        String sign = ReedpaySignature.MD5_lowCase(sign_sb);
//        System.out.println(sign);
    }
}
