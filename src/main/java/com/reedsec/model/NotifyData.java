package com.reedsec.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by lik@reedsec.com on 2017/4/13 0013.
 */
public class NotifyData implements Serializable {
    /**
     *消息通知 data 字段
     */
    String transaction_id;
    String order_no;
    String mch_extra;
    int amount;
    String time_expire;
    String subject;
    String time_success;
    String channel_trade_no;
    int amount_refunded;
    String refunds;
    String trade_status;
    Map<String, Object>  credential;
    Map<String, Object> extra;
    boolean paid;
    String trade_type;
    String time_created;
    String currency;
    boolean refunded;
    String detail;
    String app_id;
    String mch_order_no;

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

    public String getMch_extra() {
        return mch_extra;
    }

    public void setMch_extra(String mch_extra) {
        this.mch_extra = mch_extra;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount_refunded() {
        return amount_refunded;
    }

    public void setAmount_refunded(int amount_refunded) {
        this.amount_refunded = amount_refunded;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime_success() {
        return time_success;
    }

    public void setTime_success(String time_success) {
        this.time_success = time_success;
    }

    public String getChannel_trade_no() {
        return channel_trade_no;
    }

    public void setChannel_trade_no(String channel_trade_no) {
        this.channel_trade_no = channel_trade_no;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public String getRefunds() {
        return refunds;
    }

    public void setRefunds(String refunds) {
        this.refunds = refunds;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public Map<String, Object> getCredential() {
        return credential;
    }

    public void setCredential(Map<String, Object> credential) {
        this.credential = credential;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
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

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMch_order_no() {
        return mch_order_no;
    }

    public void setMch_order_no(String mch_order_no) {
        this.mch_order_no = mch_order_no;
    }
}
