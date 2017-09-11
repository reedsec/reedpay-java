package com.reedsec.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lik@reedsec.com on 2017/5/20 0020.
 */
public class Utils {
    /**
     * 获取当前的系统时间 （yyyyMMddHHmmss）
     */
    public static String getCurrentTime() {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr;
    }
    public static String getCurrentDate() {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr;
    }


    public static void main(String[] args) {
        System.out.println(getCurrentTime());
    }
}
