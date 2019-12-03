package com.tb.service.weixin.handler;

import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.weixin.service.UserInfoService;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RouteHandler("tb/wechat")
public class UserInfoHandler {
    private UserInfoService userInfoService = AsyncServiceUtil.getAsyncServiceInstance(UserInfoService.class);

    // 公众号用户授权获取用户信息
    @RouteMapping(value = "/getUserInfo", method = RouteMethod.GET)
    public Handler<RoutingContext> getUserInfo() {
        return ctx -> {
            userInfoService.getUserInfo(CommonUtil.createCondition(ctx.request(), ctx.getBody()), res -> {
                JsonObject resultJson = new JsonObject(Json.encode(res.result()));
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(resultJson));
            });
        };
    }

    // 小程序用户授权获取用户信息
    @RouteMapping(value = "/getAppletUserInfo", method = RouteMethod.GET)
    public Handler<RoutingContext> getAppletUserInfo() {
        return ctx -> {
            userInfoService.getAppletUserInfo(CommonUtil.createCondition(ctx.request(), ctx.getBody()), res -> {
                JsonObject resultJson = new JsonObject(Json.encode(res.result()));
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(resultJson));
            });
        };
    }

}
