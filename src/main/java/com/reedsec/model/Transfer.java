package com.reedsec.model;

/**
 * Created by sunkai on 15/5/11.
 */

import com.reedsec.Reedpay;
import com.reedsec.exception.*;
import com.reedsec.net.APIResource;
import com.reedsec.util.ReedpaySignature;

import java.util.Map;

/**
 * 代付
 */
public class Transfer extends APIResource {
    String timestamp;
    String result_code;
    String sign_type;
    String sign;
    String app_id;
    String transaction_id;
    String order_no;
    String mch_order_no;
    Integer amount;
    String description;
    String trade_type;
    String pay_mode;
    Boolean payee_check;
    String currency;
    String business_type;
    String trade_status;
    String channel_trade_no;
    String extra;
    String batch_count;

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

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public Boolean getPayee_check() {
        return payee_check;
    }

    public void setPayee_check(Boolean payee_check) {
        this.payee_check = payee_check;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
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

    public String getBatch_count() {
        return batch_count;
    }

    public void setBatch_count(String batch_count) {
        this.batch_count = batch_count;
    }

    /**
     * 创建 Transfer
     * @param params
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     */
    public static Transfer create(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        //创建代付需加入公共的签名
        String sign = ReedpaySignature.get_signWithMap(params);
        System.out.println("加密sign值："+sign);
        params.put("sign_type","MD5");
        params.put("sign", sign);//MD5加密后转为小写
        System.out.println("请求参数："+params);
        return request(RequestMethod.POST, Reedpay.getApiBase()+"transfer", params, Transfer.class);
    }

    /**
     * 查询 Transfer
     * @param id
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     */
    public static Transfer query(String id) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, Reedpay.getApiBase()+"transfer/"+id, null, Transfer.class);
    }

    /**
     * 查询 Transfer
     * @param params 分页参数等
     * @return TransferCollection
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     */
    public static TransferList retrieve(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, Reedpay.getApiBase()+"transfer", params, TransferList.class);
    }

}
