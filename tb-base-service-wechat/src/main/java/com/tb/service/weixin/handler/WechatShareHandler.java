package com.tb.service.weixin.handler;


import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.weixin.service.WechatShareService;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RouteHandler("tb/wechatShare")
public class WechatShareHandler {
    private WechatShareService wechatShareService = AsyncServiceUtil.getAsyncServiceInstance(WechatShareService.class);

    // 公众号用户授权获取用户信息
    @RouteMapping(value = "/getJsApiTicket", method = RouteMethod.GET)
    public Handler<RoutingContext> getJsApiTicket() {
        return ctx -> {
            wechatShareService.getJsApiTicket(CommonUtil.createCondition(ctx.request(), ctx.getBody()), res -> {
                JsonObject resultJson = new JsonObject(Json.encode(res.result()));
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(resultJson));
            });
        };
    }
}

