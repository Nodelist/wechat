package com.tb.service.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.tb.service.weixin.service.UserInfoService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

@Component
@AsyncServiceHandler
public class UserInfoServiceImpl implements UserInfoService, BaseAsyncService {

    private final static String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
    private final static String userInfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    private final static String session_url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";

    @Autowired
    public WeChatServiceImpl weChatServiceImpl = new WeChatServiceImpl();

    /**
     * 微信公众号获取用户信息
     *
     * @param params：appid  账号id; code 用于换取access_token
     * @param resultHandler
     */
    @Override
    public void getUserInfo(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future = Future.future();
        JSONObject userInfo;
        String appid = params.getString("appid");
        String code = params.getString("code");
        JSONObject openId = getOpenId(appid, code, token_url);
        String access_token = openId.getString("access_token");
        String openid = openId.getString("openid");
        // 可能需要判断access_token是否失效（暂不判断，每次都获取）
        if (true) {
            userInfo = getInfoList(access_token, openid);           // 获取用户信息
            future.complete(new JsonObject(Json.encode(userInfo)));
        } else {
        }
        future.setHandler(resultHandler);
    }

    @Override
    public void getOpenId(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future = Future.future();
        String appid = params.getString("appid");
        String code = params.getString("code");
        JSONObject openId = getOpenId(appid, code, session_url);
        future.complete(new JsonObject(Json.encode(openId)));
        future.setHandler(resultHandler);
    }

    /**
     * 微信小程序获取用户信息
     *
     * @param params：code   临时登录凭证，用于换取access_token
     *                      encryptedData 用户加密数据
     * @param resultHandler
     */
    @Override
    public void getAppletUserInfo(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future = Future.future();
        JSONObject encryptedData = null;
        String appid = params.getString("appid");
        String code = params.getString("code");
        JSONObject openId = getOpenId(appid, code, session_url);
        String session_key = openId.getString("session_key");
        String openid = openId.getString("openid");
        // 可能需要判断access_token是否过期（暂不判断）
        if (true) {
            encryptedData = getInfoList(session_key, openid);           // 获取用户信息
            future.complete(new JsonObject(Json.encode(encryptedData)));
        } else {
        }
        future.setHandler(resultHandler);
    }


    /**
     * 调用微信接口获取用户信息
     *
     * @param access_token 网页授权token
     * @param openid       用户openid
     * @return
     */
    private JSONObject getInfoList(String access_token, String openid) {
        Future<JsonArray> future = Future.future();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String userInfoUrl = userInfo_url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        JSONObject userInfo = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(userInfoUrl);
            HttpGet get = new HttpGet(uriBuilder.build());
            //执行请求
            closeableHttpResponse = httpClient.execute(get);
            //取响应结果
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = closeableHttpResponse.getEntity();
                String jsonStr = EntityUtils.toString(entity, "utf-8");
                userInfo = JSONObject.parseObject(jsonStr);
                System.out.println("userInfo:" + userInfo);
            }
        } catch (Exception e) {
            future.fail(e.getCause().getMessage());
        } finally {
            try {
                closeableHttpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userInfo;
    }

    // 封装获取openid与凭证的方法
    public JSONObject getOpenId(String appid, String code, String url) {
        JsonObject paramter = new JsonObject();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        paramter.put("appid", appid);
        JsonObject resultJson = new JsonObject();
        weChatServiceImpl.getAppSecret(paramter, res -> {
            resultJson.put("data", res.result().getValue("appSecret"));
        });
        String appSecret = resultJson.getString("data");
        String openid_Url = url.replace("APPID", appid).replace("SECRET", appSecret).replace("CODE", code);
        CloseableHttpResponse closeableHttpResponse = null;
        JSONObject token = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(openid_Url);
            HttpGet get = new HttpGet(uriBuilder.build());
            //执行请求
            closeableHttpResponse = httpClient.execute(get);
            //取响应结果
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = closeableHttpResponse.getEntity();
                String jsonStr = EntityUtils.toString(entity, "utf-8");
                token = JSONObject.parseObject(jsonStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeableHttpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    // 对用户敏感数据进行解密
    private JSONObject deCodeDate(String sessionKey, String encryptedData, String iv) {
        String decrypts = null;//解密
        try {
            decrypts = decrypt(encryptedData, sessionKey, iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject userInfo = JSONObject.parseObject(decrypts);
        return userInfo;
    }


    // 解密数据
    @Nullable
    private static String decrypt(String encryptedData, String key, String iv) throws Exception {
        //被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        //加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        //偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, "utf-8");
            }
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}