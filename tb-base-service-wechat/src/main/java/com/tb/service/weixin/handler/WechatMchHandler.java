package com.tb.service.weixin.handler;


import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.weixin.service.WeChatService;
import com.tb.service.weixin.service.WechatMchService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;


@RouteHandler("tb/wxMch")
public class WechatMchHandler {
    private WechatMchService wechatMchService = AsyncServiceUtil.getAsyncServiceInstance(WechatMchService.class);

    // 根据appid获取商户号信息列表
    @RouteMapping(value = "/getMch", method = RouteMethod.POST)
    public Handler<RoutingContext> getByappId() {
        return ctx -> {
            wechatMchService.getMch(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 添加绑定信息
    @RouteMapping(value = "/add", method = RouteMethod.POST)
    public Handler<RoutingContext> addBind() {
        return ctx -> {
            wechatMchService.add(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 编辑绑定信息
    @RouteMapping(value = "/edit", method = RouteMethod.POST)
    public Handler<RoutingContext> editBind() {
        return ctx -> {
            wechatMchService.edit(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 删除绑定信息
    @RouteMapping(value = "/delete", method = RouteMethod.DELETE)
    public Handler<RoutingContext> deleteBind() {
        return ctx -> {
            wechatMchService.delete(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

}
