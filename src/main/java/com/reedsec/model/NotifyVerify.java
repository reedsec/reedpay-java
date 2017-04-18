package com.reedsec.model;

import com.reedsec.net.APIResource;
import com.reedsec.util.ReedpaySignature;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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

    //测试加签验签
    public static void main(String[] args) throws Exception {
        // 该数据由用户传入, 以下仅作为示例
        String webhooksRawPostData = getStringFromFile(eventPath);
        System.out.println("------- POST 原始数据 -------");
        System.out.println(webhooksRawPostData);

        JSONObject json_data = new JSONObject(webhooksRawPostData);
        String sign_type = json_data.getString("sign_type");
        System.out.println("签名类型为："+sign_type);
        String sign = json_data.getString("sign");
        System.out.println("签名为："+sign);
        //去掉签名类型和签名字段
        json_data.remove("sign_type");
        json_data.remove("sign");

        String sign_varify = ReedpaySignature.get_signWithJson(json_data);
        System.out.println("数据加签后的签名："+sign_varify);

        if (sign.equals(sign_varify)) {
            System.out.println("验签结果：通过");
        } else {
            System.out.println("验签结果：失败");
            return;
        }
    }
}
