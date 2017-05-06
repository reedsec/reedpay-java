package com.reedsec.model;

import com.reedsec.Reedpay;
import com.reedsec.exception.*;
import com.reedsec.net.APIResource;
import com.reedsec.util.ReedpaySignature;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by lik@reedsec.com on 2017/4/7 0007.
 */
public class Sale extends APIResource implements Serializable {

    String timestamp ;
    String result_code;
    String sign_type;
    String sign;
    String app_id;
    String transaction_id;
    String order_no;
    String mch_order_no;
    Integer amount;
    String trade_type;
    String subject;
    String currency;
    String detail;
    String mch_extra;
    String time_created;
    String time_expire;
    String time_success;
    String trade_status;
    String channel_trade_no;
    String amount_refunded;
    String refunds;
    String paid;
    String refunded;

    String  extra;
//    Map<String ,Object> credential;
    String  credential;


    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getAmount_refunded() {
        return amount_refunded;
    }

    public void setAmount_refunded(String amount_refunded) {
        this.amount_refunded = amount_refunded;
    }

    public String getRefunds() {
        return refunds;
    }

    public void setRefunds(String refunds) {
        this.refunds = refunds;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getRefunded() {
        return refunded;
    }

    public void setRefunded(String refunded) {
        this.refunded = refunded;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
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

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getMch_order_no() {
        return mch_order_no;
    }

    public void setMch_order_no(String mch_order_no) {
        this.mch_order_no = mch_order_no;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMch_extra() {
        return mch_extra;
    }

    public void setMch_extra(String mch_extra) {
        this.mch_extra = mch_extra;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getTime_success() {
        return time_success;
    }

    public void setTime_success(String time_success) {
        this.time_success = time_success;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getChannel_trade_no() {
        return channel_trade_no;
    }

    public void setChannel_trade_no(String channel_trade_no) {
        this.channel_trade_no = channel_trade_no;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    /**
     * 创建 Sale
     *
     * @param params
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static Sale create(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        //创建支付需加入公共的签名
        String sign = ReedpaySignature.get_signWithMap(params);
        params.put("sign_type","MD5");
        params.put("sign", sign);//MD5加密后转为小写
        if(Reedpay.DEBUG){
            System.out.println("加密sign值："+sign);
            System.out.println("请求参数："+params);
        }
        return request(RequestMethod.POST, Reedpay.getApiBase()+"sale", params, Sale.class);
    }



    /**
     * 查询 Sale
     *
     * @param id
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static Sale query(String id) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, Reedpay.getApiBase()+"sale/"+id, null, Sale.class);
    }


    /**
     * 查询 Sale 列表
     *
     * @param params
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static SaleList retrieve(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, Reedpay.getApiBase()+"sale", params, SaleList.class);
        }

}
