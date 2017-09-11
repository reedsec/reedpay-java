# reedpay Java SDK 使用文档

#### apiKey 和 appId
SDK 需要 reedpay 提供的 apiKey 和 AppID 作为凭证获取移动端支付所需的 Sale 对象。  
这两个值目前由由瑞赛网络提供 。


#### 请求接口之前先初始化SDK
#### 设置 apiKey和appId
``` java
 Reedpay.apiKey = API_KEY;
 Reedpay.appId = APP_ID;
 //设置是否开启调试模式，调试模式会有部分日志输出。默认为false
 Reedpay.DEBUG = true;
 //初始化加密的秘钥
 Reedpay.privateKey = "Secret";
 //初始化请求地址，默认使用瑞赛sdk提供的地址。
 Reedpay.setApiBase("server_url");
 
```

##### 创建 Sale
``` java
create(Map<String, Object> params)
```
方法名：create  
类型：实例方法  
参数：Map  
返回：Sale  
示例：

```java
Map<String, Object> SaleMap = new HashMap<String, Object>();
SaleMap.put("amount", 100);
SaleMap.put("subject", "test");
SaleMap.put("order_no", "123456789");
SaleMap.put("trade_type", "WX_QRCODE");
SaleMap.put("app_id", "app_5926ce6b6029dd6f89a90c62");
JSONObject extra = new JSONObject();
extra.put("client_ip","127.0.0.1");// 发起支付请求客户端的 IP 地址，格式为 IPV4
SaleMap.put("extra", extra.toString());
try {
	// 发起 sale 创建请求
	Sale sale = Sale.create(saleMap);
} catch (Exception e) {
	e.printStackTrace();
}
```

##### 备注 请求参数
|字段名	|            变量名	|        必填	| 类型	|       说明|
|瑞赛支付平台的AppID|	app_id	 |        Y	 |  String(32)	|
应用在瑞赛支付平台的唯一标识。例如：app_58e884950aba5c7fc39f7384 |
|商户的交易订单号|	mch_order_no|	 Y	|   String(32)|	商户后台生成的唯一订单号|
|支付方式标识	|    trade_type	|     Y	|   String(16)|	支付方式标识码，取值和含义见后附|
|付款金额(分)	|     amount|	         Y	|     Number|	    付款金额，以分为单位的整数|
|订单标题	|         subject|	     Y|	  String(256)|	UFT8编码格式，256个ASCII字符或85个汉字 |
|订单描述	|         detail|	         N	|  String(1024)	|UTF8编码格式，1024个ASCII字符或340个汉字|
|货币类型	 |        currency	|     N |	   String(3)|	货币类型，默认CNY（人民币）|
|扩展参数	 |         extra|	         Y	|    JSON|	    JSON字符串，支付请求的扩展参数|
|商户的附加数据|	      mch_extra	 |    N	|   JSON	 |       商户自定义的参数，将会在异步通知中原样返回，主要用于携带订单附加信息|

支付请求扩展参数extra：
字段名	                     变量名	      必填	   类型	         说明
客户端的IP地址	            client_ip	    Y	String(16)	  客户端的IP地址
支付成功回调地址            notify_url	    N	String(128)	  支付成功时系统推送异步通知的地址，如已在后台配置则无需填写。 例如：http://example.com/reedpay/notify
是否允许信用卡支付          limit_pay	    N	String(32)	  此参数填no_credit可限制用户不能使用信用卡支付；微信支付是时选填。
刷卡支付授权码	            auth_code	    Y	String(128)	  trade_type为WX_SCAN，ALI_SCAN,QQ_SCAN时必填
微信公众号的唯一id	        openid	        Y	String(128)	  trade_type为WX_JSAPI时必填。
商品展示URL	                show_url	    N	String	      商品展示URL

请用 JSON 格式把 Sale 对象返回给客户端。

##### 查询 query
``` java
query(String transaction_id)
```
方法名：query
类型：实例方法  
参数：String 类型的transaction_id，交易单号 
返回：Sale  
示例：
``` java
 Sale sale = Sale.query(transaction_id);
 // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
String saleString = sale.toString();
```

##### 查询 retrieve 列表
``` java
retrieve(Map<String, Object> params)
```
方法名：retrieve
类型：实例方法  
参数：Map  具体参数详见接口文档
返回：SaleList
示例：
``` java
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("page","1");
        params.put("page_size","3");
        params.put("app_id","58e884950aba5c7fc39f7384");


        JSONObject time_create_json = new JSONObject();
//        time_create_json.put("gt","20170407121230"); //yyyyMMddHHmmss
        params.put("time_created",time_create_json);

        JSONObject amount_json = new JSONObject();
        amount_json.put("gte","1");
        params.put("amount",amount_json);
		SaleList saleList = Sale.retrieve(params);
```

	###查询参数
	字段名				变量名			必填		类型			说明
	查询分页页码		page			 N			Number		查询结果分页的页码
	查询单页数据条数	page_size		 N			Number		查询结果单页条数，默认为10条，最大为20条
	瑞赛支付平台的AppID	app_id			 N			String(32)	应用在瑞赛支付平台的唯一标识
	支付方式标识		trade_type		 N			String(16)	支付方式标识码，取值和含义见后附
	支付交易状态		trade_status 	 N			String(16)	支付交易状态 	支付交易状态码 
	10位UNIX时间戳		time_created	 N			JSON对象	自1970年1月1日零时起至今的秒数，注意某些语言返回毫秒数
	10位UNIX时间戳		amount			 N			JSON对象	自1970年1月1日零时起至今的秒数，注意某些语言返回毫秒数
	
	##扩展参数time_created：
	字段名				 变量名		必填		类型		       说明
	等于交易创建时间	 eq			 N		String(14)		返回交易创建时间等于所给时间的记录，格式为14位日期时间字符串yyyyMMddHHmm
	大于交易创建时间	 gt			 N		String(14)		返回交易创建时间大于所给时间的记录，格式为14位日期时间字符串yyyyMMddHHmmss
	大于等交易创建时间	 gte		 N		String(14)		返回交易创建时间大于等于所给时间的记录，格式为14位日期时间字符串yyyyMMddHHmm
	小于交易创建时间	 lt			 N		String(14)		返回交易创建时间小于所给时间的记录，格式为14位日期时间字符串yyyyMMddHHmmss
	小于等于交易创建时间 lte	     N		String(14)		返回交易创建时间小于等于所给时间，格式为14位日期时间字符串yyyyMMddHHmms

	##扩展参数amount:
	字段名			变量名	必填	类型	说明
	等于金额		eq		N		Number	返回等于金额的交易记录
	大于金额		gt		N		Number	返回大于金额的交易记录
	大于等于金额	gte		N		Number	返回大于等于金额的交易记录
	小于金额		lt		N		Number	返回小于金额的交易记录
	小于等于金额	lte		N		Number	返回小于等于金额的交易记录


##### 签名验证
verify_signature(String body)
方法名：verify_signature
类型：实例方法
参数：String  回调地址收到的消息
返回：boolean (true表示验签成功，flase表示验签失败)
示例：
 boolean b = NotifyVerify.verify_signature(body);

