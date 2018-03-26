package com.glens.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ConvertUtils {

    // 常用数据类型转换
    public static Integer objToInteger(Object obj) {
        try {
            return Integer.valueOf(String.valueOf(obj));
        } catch (Exception e) {
            return 0;
        }
    }

    public static Long objToLong(Object obj) {
        try {
            return Math.round(Double.valueOf(String.valueOf(obj)));
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Double objToDouble(Object obj) {
        try {
            return Math.rint(Double.valueOf(String.valueOf(obj)));
        } catch (Exception e) {
            return 0d;
        }
    }

    public static BigDecimal objToBigDecimal(Object obj) {
        try {
            return new BigDecimal(String.valueOf(obj));
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    public static double objToDouble(Object value, int ws) {
        StringBuilder sb = new StringBuilder();
        if (ws <= 0) {
            sb.append("######0");
        } else {
            sb.append("######0.");
            for (int i = 0; i < ws; i++) {
                sb.append("0");
            }
        }
        try {
            DecimalFormat df = new DecimalFormat(sb.toString());
            String val = df.format(Double.valueOf(String.valueOf(value)));
            return Double.valueOf(val);
        } catch (Exception e) {
            return 0.00;
        }
    }
    //int  转换成  bigDecimal类型
    public static BigDecimal intToBigDecimal(int b){
        return new BigDecimal(b);
    }
}
