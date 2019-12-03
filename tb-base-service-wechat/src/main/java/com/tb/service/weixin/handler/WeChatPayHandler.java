package com.tb.service.weixin.handler;

import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.weixin.service.WeChatPayService;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RouteHandler("tb/wxPay")
public class WeChatPayHandler {
    private WeChatPayService weChatPayService = AsyncServiceUtil.getAsyncServiceInstance(WeChatPayService.class);

    // JSAPI 支付
    @RouteMapping(value = "/JSAPI", method = RouteMethod.POST)
    public Handler<RoutingContext> wechatPay() {
        return ctx -> {
            weChatPayService.wechatPay(CommonUtil.createCondition(ctx.request(), ctx.getBody()), res -> {
                JsonObject resultJson = new JsonObject(Json.encode(res.result()));
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(resultJson));
            });
        };
    }
}
