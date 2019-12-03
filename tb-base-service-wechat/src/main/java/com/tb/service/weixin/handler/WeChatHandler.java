package com.tb.service.weixin.handler;


import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.weixin.entity.WeChat;
import com.tb.service.weixin.repository.WeChatRepository;
import com.tb.service.weixin.service.WeChatService;
import com.tb.service.weixin.service.impl.WeChatServiceImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;


@RouteHandler("tb/wxApp")
public class WeChatHandler {
    private WeChatService weChatService = AsyncServiceUtil.getAsyncServiceInstance(WeChatService.class);

    // 获取账号信息列表
    @RouteMapping(value = "/list", method = RouteMethod.POST)
    public Handler<RoutingContext> getAppListById() {
        return ctx -> {
            weChatService.getAppList(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 删除账号信息
    @RouteMapping(value = "/deleteAppById", method = RouteMethod.DELETE)
    public Handler<RoutingContext> deleteById() {
        return ctx -> {
            weChatService.deleteAppById(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 新增账号信息
    @RouteMapping(value = "/addApp", method = RouteMethod.POST)
    public Handler<RoutingContext> addApp() {
        return ctx -> {
            weChatService.addApp(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 编辑账号信息
    @RouteMapping(value = "/editAppById", method = RouteMethod.POST)
    public Handler<RoutingContext> editAppById() {
        return ctx -> {
            weChatService.editAppById(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }
}
