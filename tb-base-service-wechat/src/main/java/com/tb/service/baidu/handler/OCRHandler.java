package com.tb.service.baidu.handler;

import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.baidu.service.OCRService;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RouteHandler("tb/baidu")
public class OCRHandler {
    private OCRService ocrService = AsyncServiceUtil.getAsyncServiceInstance(OCRService.class);

    // 文字识别
    @RouteMapping(value = "/ocr", method = RouteMethod.POST)
    public Handler<RoutingContext> getCardInfo() {
        return ctx -> {
            ocrService.getCardInfo(CommonUtil.createCondition(ctx.request(), ctx.getBody()), res -> {
                JsonObject resultJson = new JsonObject(Json.encode(res.result()));
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(resultJson));
            });
        };
    }
}
