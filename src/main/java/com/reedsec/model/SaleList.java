package com.reedsec.model;

public class SaleList extends ReedpayList<Sale> {

    String total;
    String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
