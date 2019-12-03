package com.tb.service.risk.handler;


import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.core.model.ReplyObj;
import com.sticker.online.core.utils.AsyncServiceUtil;
import com.sticker.online.core.utils.HttpUtil;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.risk.service.RiskMapService;
import com.tb.service.weixin.service.WeChatService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;


@RouteHandler("tb/modelMap")
public class RiskModelHandler {
    private RiskMapService riskMapService = AsyncServiceUtil.getAsyncServiceInstance(RiskMapService.class);

    // 获取绑定模型信息列表
    @RouteMapping(value = "/list", method = RouteMethod.POST)
    public Handler<RoutingContext> getList() {
        return ctx -> {
            riskMapService.getModelList(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 删除绑定信息
    @RouteMapping(value = "/delete", method = RouteMethod.DELETE)
    public Handler<RoutingContext> deleteById() {
        return ctx -> {
            riskMapService.deleteModelById(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 新增绑定信息
    @RouteMapping(value = "/add", method = RouteMethod.POST)
    public Handler<RoutingContext> addModel() {
        return ctx -> {
            riskMapService.addModel(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }

    // 编辑绑定信息
    @RouteMapping(value = "/edit", method = RouteMethod.POST)
    public Handler<RoutingContext> editModel() {
        return ctx -> {
            riskMapService.editModelById(CommonUtil.createCondition(ctx.request(), ctx.getBody()),res->{
                HttpUtil.fireJsonResponse(ctx.response(), 200, ReplyObj.build().setData(res.result()));
            });
        };
    }
}
