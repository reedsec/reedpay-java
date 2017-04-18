package com.reedsec.model;

import com.reedsec.bean.ReedpayObject;

import java.util.List;

public abstract class ReedpayList<T> extends ReedpayObject {

    /**
     * reedpay 列表基础参数
     */
    String timestamp;
    String result_code;
    String type;
    List<T> data;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
