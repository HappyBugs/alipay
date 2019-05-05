package alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092700606702";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCJUdv3bM9FOG23Yb6aGMAnq6eVy0grkFVSiLa3g/eR5TonkGuxLLdIhmsvAZW1aDcImkCS55BDm2OabQGAPV8qX8dkMtyzM5apL1I6MpWbgD5bzojD62SHrRU2FT6GfzeNtsb1tQwl+QyK+wpI9/IpjUFG2OL0nxuJKCBUDrlFCsg5zpCuCq7OcPBpSqLpWfIpIlIPO5pMHXVorHr8aSP+6WtABA/jthlbRQ5TiTyuE+MqxuBNe6S5uShr64SHgI7pJq8I9FA9Hx423WDC+4Bk8/rAOjLzuWgsqtzIDPzV58MDm42Sgl05nacytAdGb+VYHl4hWh58TQz/b55HMn6VAgMBAAECggEBAIhohsf8kNFEQskeEcblu0wtGmaU7vZHhFpecJNQRofpW6GdtzzjHJaw4JxrSJpSEgk7odXMreRa4dCvZARN8tEXH+0LEW36WzlpRpeFVFSKSmfMfNOSa+TTLPTymY/lo/vkizPdS1oVUj5lLkxZ8XcRWLzWCm3R9/qBn8ugbK6FmCcykLtD192BC7qggVu15zOnphRvIWSRrscECDx/3bLqhQ5EO+KAd8CYjmBhlxhWfRyBVqIsZ2+HrlethxzyZpluLqZim69cHDj/BCIHGocpnqOeptHY9Qq+LBfYjpllEE7EMdQ+J6f8+vzpt9LayCNsXhBTgFjy1/uxpD9FJWkCgYEAx+yjj1GtOHOj7NO5TeVZXP4MyGpkyAm9+Yf3oUdWghBaQaijho3uEzXwGHENeHHF2nEUyv0Nog6oheDqLtk//7aTrB0Lc1ahqlOsRzkUD6wmEgfvX0Pj7kw5vzYo0Vb4ZHQN0ueW6ZiHPdycuw9WiJGfwZ+/Kj9K8PRGTTug/qsCgYEAr9X2MebTn4tIS1mPXbKBKDrXZpFKeH5+VNPOK2EAACWAFg96287c8VsFrH8hXYA1kc0Wgm3m8BGZWHADtOk5BN47PguTScEQNRmkK31K0qaO0/nivMiBhqpsUpvKTTIdeQSTng35ZV7FWO5ix3FvqBPzawsOaU0EDmmDpj8Zd78CgYEAhi9THDeSzuf251a38gt0rlCpnb6jRAwd4SScVTg/XO7C1XztvLr4RxaVvqHqi6O5S0NI1HGCO/ogrDLom2cfaG9PsF6SN+i2e07Lu8YYq+g8aS/APy2fB2tL+wXL4a3A7BNkz+C2zrScWS4AWCEUUHeFRX0F3sI6d31MzjuudB8CgYAh5wMdBABxlo1N2erGihBVDWNSp1jQerMo792kh4NUg+Z6Wmm5z9zRvFcPt0Nbn8i0X/KjG0WXrA0A/Df6vaz8v0x03g57/fl+A3+f0vyGOQ742AlblN1Edb+E/fy3fZ4pIzTDGLapUOiECRKKmdiJc69PSEZzDyJlFPsJJslmSQKBgDWUCS4ZAtwcPH0OvcCStzBydTCg+6Ks1oXlXE40husaA53mG2rIYAfZNr8HEtJrwjQ41sNd1KE9zFlK6dcveVJkpmzrG8coWpuDry9YBFLllAA21Ab06OY7EYj/9fCQD5Tvuzp1tNwQPliwh0g+BosOHawMSldzWGsdDHNWLS/0";
	
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy9GIgZz/YNUVoD95M4nJ66HuybSZdZhQQWlLzR7mWE2ibjRrY0eQ1Wumag0vqU5CQOgM1te1XPYRGp/tuY+DmTTQvKUV4asmOszfWnv02VQG3ovI46hmY3Mwu6Q2yOu8A7N/Wli8GtstZsMNle+dn7zQJxtgDTWGZs7DeXpJrF6FN2FBAi3AqB6wiHaloEG+BAu8fj4bkLtd0AhLaLy+6RUL9+Wc75QSve5e5q79Jhx5IL7wxGYXO5Xt2w0PVIotV4NGycEvsgHV8g3Ao9Mve4fhXTJCUoofU+bRG8q9RmpaXNl7Cs+lz3eCJDjUyzOa+7eRSCiVs8hUGQqMYVD16QIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://hc8dcn.natappfree.cc/aliPay/payNotify";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://hc8dcn.natappfree.cc/aliPay/payReturn";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关           
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "d:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

