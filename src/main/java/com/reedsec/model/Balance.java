package com.reedsec.model;

/**
 * Created by lik@reedsec.com on 2017/5/19 0019.
 */

import com.reedsec.Reedpay;
import com.reedsec.exception.*;
import com.reedsec.net.APIResource;

import java.util.Map;

/**
 * 余额查询
 */
public class Balance extends APIResource {

    String result_code;
    String timestamp;
    String app_id;
    String account_status;
    String balance;
    String quota;
    String err_code;
    String err_msg;
    String channel;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 查询 Balance
     */
    public static Balance BalanceQuery(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, Reedpay.getApiBase()+"balance", params, Balance.class);
    }

}
