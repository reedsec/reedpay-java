package com.reedsec.model;

import com.reedsec.Reedpay;
import com.reedsec.exception.*;
import com.reedsec.net.APIResource;
import com.reedsec.util.ReedpaySignature;

import java.util.Map;

/**
 * Created by lik@reedsec.com on 2017/5/31 0031.
 */
public class Refund extends APIResource{

    String timestamp;
    String result_code;
    String app_id;
    String transaction_id;
    String refund_id;
    String amount;
    String refund_amount;
    String description;
    String refund_status;
    String order_no;
    String refund_no;
    String mch_order_no;
    String channel_trade_no;
    String channel_refund_id;
    String refunded;
    String sign_type;
    String sign;
    //result_code = "FAIL"时有以下参数
    String err_code;//错误码
    String err_msg;//错误信息

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

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getRefund_no() {
        return refund_no;
    }

    public void setRefund_no(String refund_no) {
        this.refund_no = refund_no;
    }

    public String getMch_order_no() {
        return mch_order_no;
    }

    public void setMch_order_no(String mch_order_no) {
        this.mch_order_no = mch_order_no;
    }

    public String getChannel_trade_no() {
        return channel_trade_no;
    }

    public void setChannel_trade_no(String channel_trade_no) {
        this.channel_trade_no = channel_trade_no;
    }

    public String getChannel_refund_id() {
        return channel_refund_id;
    }

    public void setChannel_refund_id(String channel_refund_id) {
        this.channel_refund_id = channel_refund_id;
    }

    public String getRefunded() {
        return refunded;
    }

    public void setRefunded(String refunded) {
        this.refunded = refunded;
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

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    /**
     * 创建退款
     * @param params
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static Refund create(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        //创建退款需加入公共的签名
        String sign = ReedpaySignature.get_signWithMap(params);
        params.put("sign_type","MD5");
        params.put("sign", sign);//MD5加密后转为小写
        if(Reedpay.DEBUG){
            System.out.println("加密sign值："+sign);
            System.out.println("请求参数："+params);
        }

        //获取交易id
        String transaction_id = params.get("transaction_id").toString();
        return request(APIResource.RequestMethod.POST, Reedpay.getApiBase()+"sale/"+transaction_id+"/refund", params, Refund.class);
    }

    /**
     *  查询退款
     * @param transaction_id
     * @param refund_id
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static Refund query(String transaction_id,String refund_id) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, Reedpay.getApiBase()+"sale/"+transaction_id+"/refund/"+refund_id, null, Refund.class);
    }

    /**
     *  查询退款列表
     * @param params （分页参数）
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static RefundList retrieve(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        String transaction_id = params.get("transaction_id").toString();
        return request(RequestMethod.GET, Reedpay.getApiBase()+"sale/"+transaction_id+"/refund", params, RefundList.class);
    }
}
