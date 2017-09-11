package com.reedsec.net;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reedsec.Reedpay;
import com.reedsec.bean.ReedpayObject;
import com.reedsec.exception.*;
import com.reedsec.model.Refund;
import com.reedsec.model.Sale;
import com.reedsec.model.Transfer;
import com.reedsec.util.ReedpaySignature;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * extends the abstract class when you need requset anything from reedpay
 */
public abstract class APIResource extends ReedpayObject {
    /**
     * URLEncoder charset
     */
    public static final String CHARSET = "UTF-8";

    private static final String REQUEST_TIME_KEY = "Reedpay-Request-Timestamp";

    public static int CONNECT_TIMEOUT = 30;
    public static int READ_TIMEOUT = 80;

    /**
     * Http requset method
     */
    protected enum RequestMethod {
        GET, POST, DELETE, PUT
    }

    /**
     * Gson object use to transform json string to Sale object
     */
    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//            .registerTypeAdapter(Sale.class, new ChargeDeserializer())
//            .registerTypeAdapter(RedEnvelope.class, new RedEnvelopeDeserializer())
//            .registerTypeAdapter(Transfer.class, new TransferDeserializer())
//            .registerTypeAdapter(ChargeRefundCollection.class, new ChargeRefundCollectionDeserializer())
//            .registerTypeAdapter(EventData.class, new EventDataDeserializer())
//            .registerTypeAdapter(ReedpayRawJsonObject.class, new ReedpayRawJsonObjectDeserializer())
            .create();

    public static Gson getGson() {
        try {
            Class<?> klass = Class.forName("com.reedsec.net.APIResource");
            Field field = klass.getField("GSON");
            return (Gson) field.get(klass);
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return GSON;
    }

    /**
     * @param clazz
     * @return
     */
    protected static String className(Class<?> clazz) {
        String className = clazz.getSimpleName().toLowerCase().replace("$", " ");

        if (className.equals("redenvelope")) {
            return "red_envelope";
        } else if (className.equals("batchrefund")) {
            return "batch_refund";
        } else if (className.equals("batchtransfer")) {
            return "batch_transfer";
        } else if (className.equals("customs")) {
            return "custom";
        } else {
            return className;
        }
    }

    /**
     * @param clazz
     * @return
     */
    protected static String singleClassURL(Class<?> clazz) throws InvalidRequestException {
        return String.format("%s/v1/%s", Reedpay.getApiBase(), className(clazz));
    }

    /**
     * @param clazz
     * @return
     */
    protected static String classURL(Class<?> clazz) throws InvalidRequestException {
        return String.format("%ss", singleClassURL(clazz));
    }

    /**
     * @param clazz
     * @param id
     * @return
     * @throws InvalidRequestException
     */
    protected static String instanceURL(Class<?> clazz, String id) throws InvalidRequestException {
        try {
            return String.format("%s/%s", classURL(clazz), urlEncode(id));
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to " + CHARSET, null, e);
        }
    }

    protected static String apiBasePrefixedURL(String url) {
        return String.format("%s%s", Reedpay.getApiBase(), url);
    }

    /**
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    protected static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        } else {
            return URLEncoder.encode(str, CHARSET);
        }
    }

    /**
     * @param k
     * @param v
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String urlEncodePair(String k, String v)
            throws UnsupportedEncodingException {
        return String.format("%s=%s", urlEncode(k), urlEncode(v));
    }

    /**
     * @param apiKey
     * @return
     */
    static Map<String, String> getHeaders(String apiKey) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Charset", CHARSET);
        headers.put("User-Agent",
                String.format("Reedpay/v2 JavaBindings/%s", Reedpay.VERSION));

        if (apiKey == null) {
            apiKey = Reedpay.apiKey;
        }

        headers.put("Authorization", "Basic "+ ReedpaySignature.encode((apiKey+":").getBytes()));
        System.out.println("Authorization :"+"Basic "+ ReedpaySignature.encode((apiKey+":").getBytes()));
        headers.put("Accept-Language", Reedpay.AcceptLanguage);
        headers.put("Accept","application/json");
        if (Reedpay.apiVersion != null) {
            headers.put("Reedpay-Version", Reedpay.apiVersion);
        }
        return headers;
    }

    /**
     * @param url
     * @param apiKey
     * @return
     * @throws IOException
     */
    private static HttpURLConnection createReedpayConnection(
            String url, String apiKey) throws IOException {
        URL ReedpayURL = new URL(url);
        HttpURLConnection conn;
        if (ReedpayURL.getProtocol().equals("https")) {
            conn = (HttpsURLConnection) ReedpayURL.openConnection();
        } else {
            conn = (HttpURLConnection) ReedpayURL.openConnection();
        }

        conn.setConnectTimeout(CONNECT_TIMEOUT * 1000);
        conn.setReadTimeout(READ_TIMEOUT * 1000);
        conn.setUseCaches(false);
        for (Map.Entry<String, String> header : getHeaders(apiKey).entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }

        return conn;
    }

    /**
     * @throws APIConnectionException
     */
    private static void throwInvalidCertificateException() throws APIConnectionException {
        throw new APIConnectionException("Invalid server certificate. You tried to connect to a server that has a revoked SSL certificate, which means we cannot securely send data to that server. ");
    }

    /**
     * @param url
     * @param query
     * @return
     */
    private static String formatURL(String url, String query) {
        if (query == null || query.isEmpty()) {
            return url;
        } else {
            // In some cases, URL can already contain a question mark (eg, upcoming invoice lines)
            String separator = url.contains("?") ? "&" : "?";
            return String.format("%s%s%s", url, separator, query);
        }
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return
     * @throws IOException
     * @throws APIConnectionException
     */
    private static HttpURLConnection createGetConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        String getURL = formatURL(url, query);

        HttpURLConnection conn = createReedpayConnection(getURL,
                apiKey);
        conn.setRequestMethod("GET");

//        String requestTime = currentTimeString();
//        String stringToBeSigned = getRequestURIFromURL(conn.getURL()) + requestTime;
//        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);
//        String signature = generateSign(stringToBeSigned);
//        if (signature != null) {
//            conn.setRequestProperty("Reedpay-Signature", signature);
//        }

        return conn;
    }

    private static HttpURLConnection createDeleteConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        String getURL = formatURL(url, query);
        HttpURLConnection conn = createReedpayConnection(getURL,
                apiKey);
        conn.setRequestMethod("DELETE");

//        String requestTime = currentTimeString();
//        String stringToBeSigned = getRequestURIFromURL(conn.getURL()) + requestTime;
//        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);
//        String signature = generateSign(stringToBeSigned);
//        if (signature != null) {
//            conn.setRequestProperty("Reedpay-Signature", signature);
//        }

        return conn;
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return
     * @throws IOException
     * @throws APIConnectionException
     */
    private static HttpURLConnection createPostConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        HttpURLConnection conn = createReedpayConnection(url,
                apiKey);

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", String.format(
                "application/json;charset=%s", CHARSET));

//        String stringToBeSigned = query;
//        stringToBeSigned += getRequestURIFromURL(conn.getURL());
        String requestTime = currentTimeString();
//        stringToBeSigned += requestTime;

//        String signature = generateSign(stringToBeSigned);
//        if (signature != null) {
//            conn.setRequestProperty("Reedpay-Signature", signature);
//        }
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(CHARSET));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return
     * @throws IOException
     * @throws APIConnectionException
     */
    private static HttpURLConnection createPutConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        HttpURLConnection conn = createReedpayConnection(url,
                apiKey);

        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", String.format(
                "application/json;charset=%s", CHARSET));

        String stringToBeSigned = query;
        stringToBeSigned += getRequestURIFromURL(conn.getURL());
        String requestTime = currentTimeString();
        stringToBeSigned += requestTime;

//        String signature = generateSign(stringToBeSigned);
//        if (signature != null) {
//            conn.setRequestProperty("Reedpay-Signature", signature);
//        }
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(CHARSET));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    /**
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     * @throws InvalidRequestException
     */
    public static String createQuery(Map<String, Object> params)
            throws UnsupportedEncodingException, InvalidRequestException {
        Map<String, String> flatParams = flattenParams(params);
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : flatParams.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(entry.getKey(),
                    entry.getValue()));
        }
        return queryStringBuffer.toString();
    }

    /**
     * @param params
     * @return
     */
    private static String createJSONString(Map<String, Object> params) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(params);
    }

    /**
     * @param params
     * @return
     * @throws InvalidRequestException
     */
    private static Map<String, String> flattenParams(Map<String, Object> params)
            throws InvalidRequestException {
        if (params == null) {
            return new HashMap<String, String>();
        }
        Map<String, String> flatParams = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                Map<?, ?> nestedMap = (Map<?, ?>) value;
                for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
                    flatNestedMap.put(
                            String.format("%s[%s]", key, nestedEntry.getKey()),
                            nestedEntry.getValue());
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if (value instanceof ArrayList<?>) {
                ArrayList<?> ar = (ArrayList<?>) value;
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                int size = ar.size();
                for (int i = 0; i < size; i++) {
                    flatNestedMap.put(String.format("%s[%d]", key, i), ar.get(i));
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if ("".equals(value)) {
                throw new InvalidRequestException("You cannot set '" + key + "' to an empty string. " +
                        "We interpret empty strings as null in requests. " +
                        "You may set '" + key + "' to null to delete the property.",
                        key, null);
            } else if (value == null) {
                flatParams.put(key, "");
            } else {
                flatParams.put(key, value.toString());
            }
        }
        return flatParams;
    }


    // represents Errors returned as JSON
    private static class ErrorContainer {
        private Error error;
    }

    /**
     *
     */
    private static class Error {
        @SuppressWarnings("unused")
        String type;

        String message;

        String code;

        String param;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (null != type && !type.isEmpty()) {
                sb.append("Error type: " + type + "\n");
            }
            if (null != message && !message.isEmpty()) {
                sb.append("\t Error message: " + message + "\n");
            }
            if (null != code && !code.isEmpty()) {
                sb.append("\t Error code: " + code + "\n");
            }

            return sb.toString();
        }
    }

    /**
     * @param responseStream
     * @return
     * @throws IOException
     */
    private static String getResponseBody(InputStream responseStream)
            throws IOException {
        //\A is the beginning of
        // the stream boundary
        String rBody = new Scanner(responseStream, CHARSET)
                .useDelimiter("\\A")
                .next(); //
        responseStream.close();
        return rBody;
    }

    /**
     * @param method
     * @param url
     * @param query
     * @param apiKey
     * @return
     * @throws APIConnectionException
     */
    private static ReedpayResponse makeURLConnectionRequest(
            RequestMethod method, String url, String query,
            String apiKey) throws APIConnectionException {
        HttpURLConnection conn = null;
        try {
            switch (method) {
                case GET:
                    conn = createGetConnection(url, query, apiKey);
                    break;
                case POST:
                    conn = createPostConnection(url, query, apiKey);
                    break;
                case DELETE:
                    conn = createDeleteConnection(url, query, apiKey);
                    break;
                case PUT:
                    conn = createPutConnection(url, query, apiKey);
                    break;
                default:
                    throw new APIConnectionException(
                            String.format("Unrecognized HTTP method %s. ", method));
            }
            // trigger the request
            int rCode = conn.getResponseCode();
            String rBody = null;
            Map<String, List<String>> headers;

            if (rCode >= 200 && rCode < 300) {
                rBody = getResponseBody(conn.getInputStream());
            } else {
                rBody = getResponseBody(conn.getErrorStream());
            }
            headers = conn.getHeaderFields();
            return new ReedpayResponse(rCode, rBody, headers);

        } catch (IOException e) {
            throw new APIConnectionException(
                    String.format(
                            "IOException during API request to Reedpay (%s): %s "
                                    + "Please check your internet connection and try again.",
                            Reedpay.getApiBase(), e.getMessage()), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * @param method
     * @param url
     * @param params
     * @param clazz
     * @param <T>
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     */
    protected static <T> T request(RequestMethod method, String url, Map<String, Object> params, Class<T> clazz) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        if ((Reedpay.apiKey == null || Reedpay.apiKey.length() == 0)) {
            throw new AuthenticationException(
                    "No API key provided. (HINT: set your API key using 'Reedpay.apiKey = <API-KEY>'.");
        }

        String query = null;
        switch (method) {
            case GET:
            case DELETE:
                try {
                    query = createQuery(params);
                } catch (UnsupportedEncodingException e) {
                    throw new InvalidRequestException("Unable to encode parameters to " + CHARSET, null, e);
                }
                break;
            case POST:
            case PUT:
                query = createJSONString(params);
                break;
        }
        ReedpayResponse response;
        try {
            // HTTPSURLConnection verifies SSL cert by default
            response = makeURLConnectionRequest(method, url, query, Reedpay.apiKey);
            if (Reedpay.DEBUG) {
                System.out.println(getGson().toJson(response));
            }
        } catch (ClassCastException ce) {
            throw ce;
        }
        int rCode = response.getResponseCode();
        String rBody = response.getResponseBody();
        if(Reedpay.DEBUG){
            System.out.println("返回的body:"+ rBody);
        }
        JSONObject body_json = new JSONObject(rBody);

        if (rCode < 200 || rCode >= 300) {
            handleAPIError(rBody, rCode);
        }
//        String result_code = null;
//        if(body_json.has("result_code")){
//            result_code = body_json.getString("result_code");
//        }
//        if(!result_code.equals("SUCCESS")){
//            handleAPIFail(rBody);
//        }

        //对返回数据验签
        if(body_json.has("sign_type") && body_json.has("sign")){
            try {

                if(body_json.has("credential") && body_json.get("credential") != null){

                    body_json.put("credential",body_json.get("credential"));
                }
                if(body_json.has("extra") && body_json.get("extra") != null){

                    body_json.put("extra",body_json.get("extra"));
                }
                if(body_json.has("mch_extra") && !body_json.get("mch_extra").equals(null)){

                    body_json.put("mch_extra",body_json.get("mch_extra"));
                }

                Gson gson = new Gson();
                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                String outJson = null;
                if(body_json.has("business_type") && body_json.has("batch_count")){//代付返回
                    Transfer transfer = gson.fromJson(body_json.toString(),Transfer.class);
                    outJson = mapper.writeValueAsString(transfer);
                }else if(body_json.has("refund_id") && body_json.has("refund_amount")){
                    Refund refund = gson.fromJson(body_json.toString(),Refund.class);
                    outJson = mapper.writeValueAsString(refund);
                }else{
                    Sale sale_date = gson.fromJson(body_json.toString(),Sale.class);
                    outJson = mapper.writeValueAsString(sale_date);
                }
                boolean sign_istrue = ReedpaySignature.getsign(outJson);
                if(!sign_istrue){
                    System.out.println("数据验证签名失败！");
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return getGson().fromJson(body_json.toString(), clazz);
    }

    /**
     * 错误处理
     *
     * @param rBody
     * @param rCode
     * @throws InvalidRequestException
     * @throws AuthenticationException
     * @throws APIException
     */
    private static void handleAPIError(String rBody, int rCode)
            throws InvalidRequestException, AuthenticationException,
            APIException, ChannelException, RateLimitException {
//        Error error = getGson().fromJson(rBody,
//                ErrorContainer.class).error;
        switch (rCode) {
            case 404:
                throw new InvalidRequestException("404 "+rBody,null,null);
//            case 403:
//                throw new RateLimitException(error.toString(), null);
//            case 402:
//                throw new ChannelException(error.toString(), error.param, null);
//            case 401:
//                throw new AuthenticationException(error.toString());
//            case 400:
//                throw new InvalidRequestException(error.toString(), error.param, null);
//            default:
//                throw new APIException(error.toString(), null);
        }
    }

//    private static void handleAPIFail(String rBody) throws APIException {
//       Fail fail = getGson().fromJson(rBody,
//                Fail.class);
//        throw new APIException(fail.toString(), null);
//    }

    /**
     * 生成请求签名
     *
     * @param data
     */

//    private static String generateSign(String data)
//            throws IOException {
//        if (Reedpay.privateKey == null) {
//            if (Reedpay.privateKeyPath == null) {
//                return null;
//            }
//            FileInputStream inputStream = new FileInputStream(Reedpay.privateKeyPath);
//            byte[] keyBytes = new byte[inputStream.available()];
//            inputStream.read(keyBytes);
//            inputStream.close();
//            Reedpay.privateKey = new String(keyBytes, CHARSET);
//        }
//
//        return ReedpaySignature.sign(data, Reedpay.privateKey, CHARSET);
//    }

    /**
     * 生成MD5签名
     * @return
     */
//    private static String  MD5Sign(String data){
//
//        JSONObject jb = new JSONObject(data);
//        Map<String, Object> map = (Map<String, Object>)jb;
//        StringBuffer sb = new StringBuffer(ReedpaySignature.get_sign(map));
//        sb.append(Reedpay.privateKey);
//
//        map.put("sign_type","MD5");
//        map.put("sign",ReedpaySignature.MD5_upperCase(sb.toString()));
//
//        return map.toString();
//    }

    private static String currentTimeString() {
        Integer requestTime = (int) (System.currentTimeMillis() / 1000);
        return requestTime.toString();
    }

    private static String getRequestURIFromURL(URL url) {
        String path = url.getPath();
        String query = url.getQuery();
        if (query == null) {
            return path;
        }
        return path + "?" + query;
    }
}
