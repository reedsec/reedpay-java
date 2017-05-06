package com.reedsec.model;

import com.reedsec.bean.ReedpayObject;

import java.io.Serializable;

public abstract class ReedpayList<T> extends ReedpayObject implements Serializable {

    /**
     * reedpay 列表基础参数
     */
    String timestamp;
    String result_code;
    String type;
    String  data;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
