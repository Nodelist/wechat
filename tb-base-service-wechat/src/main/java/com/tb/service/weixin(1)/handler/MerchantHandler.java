package com.tb.service.weixin.handler;


import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.weixin.service.MerchantService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;


@RouteHandler("tb/mch")
public class MerchantHandler {
    private MerchantService merchantService = AsyncServiceUtil.getAsyncServiceInstance(MerchantService.class);

    // 获取账号信息列表
    @RouteMapping(value = "/list", method = RouteMethod.GET)
    public Handler<RoutingContext> getList() {
        return ctx -> {
            merchantService.getList(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
//                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
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

    // 删除账号信息
    @RouteMapping(value = "/deleteById", method = RouteMethod.DELETE)
    public Handler<RoutingContext> deleteById() {
        return ctx -> {
            merchantService.deleteById(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
//                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
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

    // 新增账号信息
    @RouteMapping(value = "/add", method = RouteMethod.POST)
    public Handler<RoutingContext> add() {
        return ctx -> {
            merchantService.addMerchant(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
//                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
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

    // 编辑账号信息
    @RouteMapping(value = "/edit", method = RouteMethod.PUT)
    public Handler<RoutingContext> edit() {
        return ctx -> {
            merchantService.editMerchant(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
//                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
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
