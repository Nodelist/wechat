package com.tb.service.weixin.handler;


import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.weixin.service.WeChatService;
import com.tb.service.weixin.service.WechatLoginService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@RouteHandler("tb/wxUser")
public class WechatLoginHandler {

    private WechatLoginService wechatLoginService = AsyncServiceUtil.getAsyncServiceInstance(WechatLoginService.class);


    @RouteMapping(value = "/list", method = RouteMethod.GET)
    public Handler<RoutingContext> list() {
        return ctx -> {
            wechatLoginService.list(CommonUtil.createCondition(ctx.request(), ctx.getBody()), res->{
                if (res.succeeded()) {
                    HttpUtil.fireJsonResponse(ctx.response(), HTTP_OK,
                            ReplyObj.build().setSuccess(true).setResult(res.result()).setMsg("succeed"));
                } else {
                    HttpUtil.fireJsonResponse(ctx.response(), HTTP_BAD_REQUEST,
                            ReplyObj.build().setSuccess(false).setMsg(res.cause().getMessage()));
                }
//                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    @RouteMapping(value = "/delete", method = RouteMethod.DELETE)
    public Handler<RoutingContext> delete() {
        return ctx -> {
            wechatLoginService.delete(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                if (res.succeeded()) {
                    HttpUtil.fireJsonResponse(ctx.response(), HTTP_OK,
                            ReplyObj.build().setSuccess(true).setResult(res.result()).setMsg("succeed"));
                } else {
                    HttpUtil.fireJsonResponse(ctx.response(), HTTP_BAD_REQUEST,
                            ReplyObj.build().setSuccess(false).setMsg(res.cause().getMessage()));
                }
            });
        };
    }
    //绑定
    @RouteMapping(value = "/model", method = RouteMethod.PUT)
    public Handler<RoutingContext> model() {
        return ctx -> {
            wechatLoginService.model(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                if (res.succeeded()) {
                    HttpUtil.fireJsonResponse(ctx.response(), HTTP_OK,
                            ReplyObj.build().setSuccess(true).setResult(res.result()).setMsg("succeed"));
                } else {
                    HttpUtil.fireJsonResponse(ctx.response(), HTTP_BAD_REQUEST,
                            ReplyObj.build().setSuccess(false).setMsg(res.cause().getMessage()));
                }
            });
        };
    }
}
