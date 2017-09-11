package com.reedsec.util;

import com.reedsec.Reedpay;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.*;

//import org.json.JSONObject;


public class ReedpaySignature {
    public static String sign(String data, String PEMEncodedPrivateKey, String charset) {
        PrivateKey privateKey = getPrivateKeyFromPEM(PEMEncodedPrivateKey);
        if (privateKey == null) {
            return null;
        }

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(charset));
            byte[] signBytes = signature.sign();

            return Base64.encodeBase64String(signBytes).replaceAll("\n|\r", "");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PrivateKey getPrivateKeyFromPEM(String PEMEncodedPrivateKey) {
        PEMEncodedPrivateKey = PEMEncodedPrivateKey
                .replaceAll("(-+BEGIN (RSA )?PRIVATE KEY-+\\r?\\n|-+END (RSA )?PRIVATE KEY-+\\r?\\n?)", "");
        byte[] privateKeyBytes = Base64.decodeBase64(PEMEncodedPrivateKey);

        try {
            return generatePrivateKeyWithPKCS8(privateKeyBytes);
        } catch (InvalidKeySpecException e) {
            if (Reedpay.DEBUG) {
                e.printStackTrace();
            }
            return generatePrivateKeyWithPKCS1(privateKeyBytes);
        }
    }

    public static PrivateKey generatePrivateKeyWithPKCS8(byte[] privateKeyBytes)
            throws InvalidKeySpecException {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey generatePrivateKeyWithPKCS1(byte[] privateKeyBytes) {
        try {
            DerInputStream derReader = new DerInputStream(privateKeyBytes);
            DerValue[] seq = derReader.getSequence(0);
            if (seq.length < 9) {
                System.out.println("Could not parse a PKCS1 private key.");
                return null;
            }
            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger prime1 = seq[4].getBigInteger();
            BigInteger prime2 = seq[5].getBigInteger();
            BigInteger exp1 = seq[6].getBigInteger();
            BigInteger exp2 = seq[7].getBigInteger();
            BigInteger crtCoef = seq[8].getBigInteger();
            RSAPrivateCrtKeySpec spec = new RSAPrivateCrtKeySpec(
                    modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(spec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解码
     * @param bytes
     * @return
     */
    public static byte[] decode(final byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    /**
     * 编码
     * 二进制数据编码为BASE64字符串
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * MD5加密 32位
     */
    public static String MD5_lowCase(String str){
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md5  = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            byte b[] = md5.digest();
            int i;
            for(int offset=0; offset<b.length; offset++){
                i = b[offset];
                if(i<0){
                    i+=256;
                }
                if(i<16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    /**
     * MD5验签
     */
    public static boolean MD5_verifyData(String dataString, String signatureString, String publicKey){
        dataString = dataString + publicKey;
        String my_sign =MD5_lowCase(dataString);
        if (my_sign.equals(signatureString)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *md5加密map里面的参数
     */
//    public static String get_sign(Map<String, Object> params) {
//        StringBuilder queryStringBuffer = new StringBuilder();
////        params = sortMap(params);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            if (queryStringBuffer.length() > 0) {
//                queryStringBuffer.append("&");
//            }
//            try {
//                queryStringBuffer.append(urlPair(entry.getKey(),
//                        entry.getValue().toString()));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        return queryStringBuffer.toString();
//    }

//    public static String urlPair(String k, String v)
//            throws UnsupportedEncodingException {
//        return String.format("%s=%s", k, v);
//    }

    /**
     * map 按字母排序
     * md5加密map里面的参数
     * @param map
     * @return
     */
    public static String get_signWithMap(final Map<String, Object> map) {

        final List<Map.Entry<String, Object>> infos = new ArrayList<Map.Entry<String, Object>>(map.entrySet());

        Collections.sort(infos, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return (o1.getKey().toString().compareTo(o2.getKey()));
            }
        });
        if(Reedpay.DEBUG){
            System.out.println("排序后的参数为："+infos);
        }
        StringBuilder queryStringBuffer = new StringBuilder();
        int length = infos.size();
        for (int i = 0; i < length; i++) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(infos.get(i));
        }
        queryStringBuffer.append(Reedpay.privateKey);
        if(Reedpay.DEBUG) {
            System.out.println("签名前原串：" + queryStringBuffer);
        }
        String sign = ReedpaySignature.MD5_lowCase(queryStringBuffer.toString());
        return sign;
    }

    /**
     * json 按key值排序
     */
    public static String get_signWithJson(JSONObject json){
        Map<String,Object> json_map = new HashMap<String, Object>();
//        int size = json.length();
//        if(json.has("data")){
//            //data去null
//            JSONObject data = json.getJSONObject("data");
//            Gson gson = new Gson();
//
//            NotifyData notifyData = gson.fromJson(data.toString(),NotifyData.class);
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            try {
//                String outJson = mapper.writeValueAsString(notifyData);
//                json_map.put("data",outJson);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//                json_map.put("timestamp",json.get("timestamp"));
//                json_map.put("retry",json.get("retry"));
//                json_map.put("type",json.get("type"));
//
//        }else{
            Iterator it = json.keys();
            while (it.hasNext())
            {
                String key = String.valueOf(it.next());
                Object value = (Object) json.get(key);
                json_map.put(key, value);
            }

//        }

        String sign = get_signWithMap(json_map);
        return  sign;
    }


    public static Map<String, Object> transBean2Map(Object obj) {

        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }


    public static boolean getsign(String boby){
        JSONObject json_data = new JSONObject(boby);
        String sign_type = json_data.getString("sign_type");
        String sign = json_data.getString("sign");
        if(Reedpay.DEBUG){
            System.out.println("签名类型为："+sign_type);
            System.out.println("签名为："+sign);
        }
        //去掉签名类型和签名字段
        json_data.remove("sign_type");
        json_data.remove("sign");

        String sign_varify = ReedpaySignature.get_signWithJson(json_data);
        if (Reedpay.DEBUG){
            System.out.println("数据加签后的签名："+sign_varify);
        }

        if (sign.equals(sign_varify)) {
            if (Reedpay.DEBUG) {
                System.out.println("验签结果：通过");
            }
            return true;
        } else {
            if (Reedpay.DEBUG) {
                System.out.println("验签结果：失败");
            }
            return  false;
        }
    }

}
