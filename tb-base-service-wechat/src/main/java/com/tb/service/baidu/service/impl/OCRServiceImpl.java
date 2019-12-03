package com.tb.service.baidu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.tb.service.baidu.Tool.Base64Util;
import com.tb.service.baidu.Tool.FileUtil;
import com.tb.service.baidu.Tool.HttpUtil;
import com.tb.service.baidu.service.OCRService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;


import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;


@Component
@AsyncServiceHandler
public class OCRServiceImpl implements OCRService, BaseAsyncService {

    /**
     * 百度 API Key 与 Secret Key
     */
    @Value("${api-key}")
    private String API_KEY;

    @Value("${secret-key}")
    private String SECRET_KEY;

    private final static String TOKEN_URL  = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=API_KEY&client_secret=SECRET_KEY";

    private final static String IDCARD_URL  = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
    private final static String BANKCARD_URL  = "https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard";
    private  final static String BUS_LIC_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";
    private  final static String BUS_CARD_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_card";
    private  final static String PASSPORT_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/passport";
    private  final static String HK_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/HK_Macau_exitentrypermit";
    private  final static String TAIWAN_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/taiwan_exitentrypermit";
    private  final static String HOUSE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/household_register";
    private  final static String BIRTH_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/birth_certificate";
    private  final static String INVOICE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice";
    private  final static String VEH_LIC_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license";
    private final static String DRIVE_LIC_URL  = "https://aip.baidubce.com/rest/2.0/ocr/v1/driving_license";
    private  final static String LIC_PLATE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";

    /**
     * 文字识别
     * @param params：image(图片地址)  card_type(卡片类型)
     * @param resultHandler
     */
    @Override
    public void getCardInfo(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future = Future.future();
        String url = "";
        String type = params.getString("img_type");
        // 判断传入的图片类型
        switch (type) {
            // 身份证
            case "id_card":
                url = IDCARD_URL;
                break;

            // 银行卡
            case "bank_card" :
                url = BANKCARD_URL;
                break;

            // 营业执照
            case "bus_lic" :
                url = BUS_LIC_URL;
                break;

            // 名片
            case "bus_card" :
                url = BUS_CARD_URL;
                break;

            // 护照
            case "passport" :
                url = PASSPORT_URL;
                break;

            // 港澳通行证
            case "HK_exit" :
                url = HK_URL;
                break;

            // 台湾通行证
            case "taiwan_exit" :
                url = TAIWAN_URL;
                break;

            // 户口本
            case "household" :
                url = HOUSE_URL;
                break;

            // 出生医学证明
            case "birth" :
                url = BIRTH_URL;
                break;

            // 增值税发票
            case "invoice" :
                url = INVOICE_URL;
                break;

            // 行驶证
            case "veh_lic" :
                url = VEH_LIC_URL;
                break;

            // 驾驶证
            case "drive_lic" :
                url = DRIVE_LIC_URL;
                break;

            // 车牌号
            case "lic_plate" :
                url = LIC_PLATE_URL;
                break;
            default :;
        }
        JsonObject cardInfo = getCardInfo(url,params);
        future.complete(new JsonObject(Json.encode(cardInfo)));
        future.setHandler(resultHandler);
    }

    // 获取图像文字信息
    public JsonObject getCardInfo(String url, JsonObject params) {
        String access_token = getToken();    // 获取token
        String imagePath = params.getString("image");  // 要识别的图片
        String cardSide = params.getString("id_card_side");     // 身份证正反面
        String direction = params.getString("detect_direction");        // 是否检测图像的朝向
        String risk = params.getString("detect_risk");      // 是否开启身份证风险类型功能
        String photo = params.getString("detect_photo");        // 是否检测头像内容
        String accuracy = params.getString("accuracy");     // 识别精准度：normal:普通精准度；high：高精度识别
        String vehLicSide = params.getString("vehicle_license_side");   // 行驶证正反面：front，back
        String unified = params.getString("unified_valid_period"); // 是否归一化格式输出
        String multi = params.getString("multi_detect"); // 是否监检测多张车牌

        String info = "";
        try {
            // 本地文件路径
            byte[] imgData = FileUtil.readFileByBytes(imagePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam ;
            // 判断是否传其他参数
            if (cardSide!=null) {
                param += "&id_card_side=" + cardSide;
            }
            if (direction!=null) {
                param += "&detect_direction=" + direction;
            }
            if (risk!=null) {
                param += "&detect_risk=" + risk;
            }
            if (photo!=null) {
                param += "&detect_photo=" + photo;
            }
            if (accuracy!=null) {
                param += "&accuracy=" + accuracy;
            }
            if (vehLicSide!=null) {
                param += "&vehicle_license_side=" + vehLicSide;
            }
            if (unified!=null) {
                param += "&unified_valid_period=" + unified;
            }
            if (multi!=null) {
                param += "&multi_detect=" + multi;
            }
            info = HttpUtil.post(url, access_token, param);
            System.out.println(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonObject(info);
    }

    // 获取百度API access_token
    public String getToken() {
        String token_url = TOKEN_URL.replace("API_KEY", API_KEY).replace("SECRET_KEY", SECRET_KEY);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(token_url);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity1 = response.getEntity();
            String result = EntityUtils.toString(entity1, "UTF-8");
            JSONObject json = JSONObject.parseObject(result);
            String access_token = json.getString("access_token");
            System.out.println(access_token);
            return access_token;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
