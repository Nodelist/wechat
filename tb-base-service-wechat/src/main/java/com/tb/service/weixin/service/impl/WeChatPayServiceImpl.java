package com.tb.service.weixin.service.impl;

import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.tb.service.weixin.WXPayTool.HttpRequest;
import com.tb.service.weixin.WXPayTool.WXPayUtil;
import com.tb.service.weixin.service.WeChatPayService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Component
@AsyncServiceHandler
public class WeChatPayServiceImpl implements WeChatPayService, BaseAsyncService {
    private UserInfoServiceImpl userInfoServiceImpl = new UserInfoServiceImpl();
    private WeChatServiceImpl weChatServiceImpl = new WeChatServiceImpl();

    private final static String openid_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
    private final static String pay_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    String spbill_create_ip = "1.80.217.140";
    String notify_url = "http://mapway.net";     // 通知地址(回调地址)
    @Override
    public void wechatPay(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future = Future.future();
        Map<String,String> data = new HashMap<>();
        String appid = params.getString("appid");       // 公众号的appid
        String code = params.getString("code");         // code，用于换取access_token
        // 获取商户号与商户的API密钥
        JsonObject resultJson = new JsonObject();
        weChatServiceImpl.getMerchant(params, res -> {
            resultJson.put("data",res.result());
        });
        JsonObject mch_info = resultJson.getJsonObject("data");
        String mch_id = mch_info.getString("mchId");    // 商户号
        String paternerKey = mch_info.getString("apiKey");     // 商户的API密钥

        // 获取用户openid
        String openid = userInfoServiceImpl.getOpenId(appid,code,openid_URL).getString("openid");
        String total = params.getString("total_fee");
        Double d = Double.valueOf(total)*100;
        int amount = d.intValue();
        String total_fee = String.valueOf(amount);
        String out_trade_no = getTradeNo();
        data.put("appid",appid);
        data.put("mch_id",mch_id);
        data.put("nonce_str",WXPayUtil.generateNonceStr());
        data.put("body","天邦-订单结算");
        data.put("out_trade_no",out_trade_no );
        data.put("total_fee",total_fee );
        data.put("spbill_create_ip",spbill_create_ip );
        data.put("notify_url",notify_url );
        data.put("trade_type","JSAPI" );
        data.put("openid",openid );
        Map<String, String> payMap = new HashMap<String, String>();
        try {
            String sign = WXPayUtil.generateSignature(data,paternerKey);
            data.put("sign",sign );
            String xml = WXPayUtil.mapToXml(data);
            // 发送请求调用统一下单支付接口
            String xmlStr = HttpRequest.sendPost(pay_URL,xml);
            System.out.println("返回信息：" + xmlStr);
            String prepay_id = "";//预支付id
            if (xmlStr.contains("SUCCESS")) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id = (String) map.get("prepay_id");
            }
            payMap.put("appId", appid);
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
            payMap.put("paySign", paySign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        future.complete(new JsonObject(Json.encode(payMap)));
        future.setHandler(resultHandler);
    }

//    @Override
//    public void nativePay(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
//        Future<JsonObject> future = Future.future();
//        Map<String,String> payMap = getPayMap(params,"NATIVE");
//        future.complete(new JsonObject(Json.encode(payMap)));
//        future.setHandler(resultHandler);
//    }

//    // 封装支付参数处理
//    private Map<String,String> getPayMap(JsonObject params, String trade_type) {
//        Map<String,String> data = new HashMap<>();
//        String appid = params.getString("appid");       // 公众号的appid
//        String code = params.getString("code");         // code，用于换取access_token
//        // 获取商户号与商户的API密钥
//        JsonObject resultJson = new JsonObject();
//        weChatServiceImpl.getMerchant(params, res -> {
//            resultJson.put("data",res.result());
//        });
//        JsonObject mch_info = resultJson.getJsonObject("data");
//        String mch_id = mch_info.getString("mchId");    // 商户号
//        String paternerKey = mch_info.getString("apiKey");     // 商户的API密钥
//
//        // 获取用户openid
//        String openid = userInfoServiceImpl.getOpenId(appid,code,openid_URL).getString("openid");
//        String total = params.getString("total_fee");
//        Double d = Double.valueOf(total)*100;
//        int amount = d.intValue();
//        String total_fee = String.valueOf(amount);
//        String out_trade_no = getTradeNo();
//        data.put("appid",appid);
//        data.put("mch_id",mch_id);
//        data.put("nonce_str",WXPayUtil.generateNonceStr());
//        data.put("body","天邦-订单结算");
//        data.put("out_trade_no",out_trade_no );
//        data.put("total_fee",total_fee );
//        data.put("spbill_create_ip",spbill_create_ip );
//        data.put("notify_url",notify_url );
//        data.put("trade_type", trade_type );
//        data.put("openid",openid );
//        Map<String, String> payMap = new HashMap<String, String>();
//        try {
//            String sign = WXPayUtil.generateSignature(data,paternerKey);
//            data.put("sign",sign );
//            String xml = WXPayUtil.mapToXml(data);
//            // 发送请求调用统一下单支付接口
//            String xmlStr = HttpRequest.sendPost(pay_URL,xml);
//            System.out.println("返回信息：" + xmlStr);
//            String prepay_id = "";//预支付id
//            if (xmlStr.indexOf("SUCCESS") != -1) {
//                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
//                prepay_id = (String) map.get("prepay_id");
//            }
//            payMap.put("appId", appid);
//            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
//            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
//            payMap.put("signType", "MD5");
//            payMap.put("package", "prepay_id=" + prepay_id);
//            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
//            payMap.put("paySign", paySign);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return payMap;
//    }

    // 生成订单号
    private String getTradeNo() {
        String tradeNo = "";
        SimpleDateFormat sfDate = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sfDate.format(new Date());
        Random rand = new Random();
        int n = 20;
        int randInt = 0;
        for (int i = 0; i < 3; i++) {
            randInt = rand.nextInt(10);
            tradeNo = strDate + randInt;
        }
        System.out.println("订单号：" + tradeNo);
        return tradeNo;
    }
}
