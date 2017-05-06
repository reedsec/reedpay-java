package com.reedsec.model;

public class TransferList extends ReedpayList<Transfer> {

    int total;
    int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
