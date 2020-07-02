package com.tb.service.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.tb.service.weixin.service.WechatShareService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@AsyncServiceHandler
public class WechatShareServiceImpl implements WechatShareService, BaseAsyncService {

    /**
     * 公众号 密钥
     */
    @Value("${appId}")
    private String APPID;

    @Value("${appSecret}")
    private String APPSECRET;

    private String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    private static String appId = "wxf9b981f1b9cfc408";

    @Override
    public void getJsApiTicket(JsonObject params, Handler<AsyncResult<JsonArray>> resultHandler) {
        Future future = Future.future();
        String url = params.getString("url");
        String accessToken=getAccessToken();
        String jsApiTicket = getJsApiTicket(accessToken);
        Map<String, String> map = sign(jsApiTicket,url);
        System.out.println(map);
        future.complete(new JsonObject(Json.encode(map)));
        future.setHandler(resultHandler);
    }

    @Nullable
    private String getAccessToken() {
        String token_url = tokenUrl.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(token_url);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity1 = response.getEntity();
            String result = EntityUtils.toString(entity1, "UTF-8");
            JSONObject json = JSONObject.parseObject(result);
            String access_token = json.getString("access_token");
            return access_token;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private String getJsApiTicket(String accessToken) {
        String ticket_url = ticketUrl.replace("ACCESS_TOKEN", accessToken);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(ticket_url);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity1 = response.getEntity();
            String result = EntityUtils.toString(entity1, "UTF-8");
            JSONObject json = JSONObject.parseObject(result);
            String ticket = json.getString("ticket");
            return ticket;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", appId);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
