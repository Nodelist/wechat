package com.tb.service.risk.handler;

import com.alibaba.fastjson.JSONObject;
import com.sticker.online.core.anno.RouteHandler;
import com.sticker.online.core.anno.RouteMapping;
import com.sticker.online.core.anno.RouteMethod;
import com.sticker.online.tools.common.utils.CommonUtil;
import com.tb.service.risk.entity.RiskModel;
import com.tb.service.risk.service.impl.RiskMapServiceImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RouteHandler("tbtb")
public class RiskInterceptorHandler {
    private RiskMapServiceImpl riskMapServiceImpl = new RiskMapServiceImpl();

    @RouteMapping(value = "/*",method= RouteMethod.POST, order = 11)
    public Handler<RoutingContext> intercptPostHandle() {
        return ctx -> {
            intercept(ctx);
        };
    }
    @RouteMapping(value = "/*",method= RouteMethod.GET, order = 11)
    public Handler<RoutingContext> intercptGetHandle() {
        return ctx -> {
            intercept(ctx);
        };
    }
    @RouteMapping(value = "/*",method= RouteMethod.DELETE, order = 11)
    public Handler<RoutingContext> inter1cptDelHandle() {
        return ctx -> {
            intercept(ctx);
        };
    }
    @RouteMapping(value = "/*",method= RouteMethod.PUT, order = 11)
    public Handler<RoutingContext> inter1cptPutHandle() {
        return ctx -> {
            intercept(ctx);
        };
    }

    // 拦截方法封装
    private  void intercept(RoutingContext ctx){
        JsonObject json = CommonUtil.createCondition(ctx.request(), ctx.getBody());
        String apiUrl = ctx.normalisedPath();
        System.out.println(json);
//        String userId = json.getString("userId");
//        String eventId = json.getString("eventId");
        String modelGuid = riskMapServiceImpl.getList(apiUrl);
        if (!modelGuid.isEmpty()) {   // 拿到接口路由判断是否有风控模型的映射
            // 映射了模型，执行风控
            JsonObject jsonInfo = new JsonObject();
            String num = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            Long time = new Date().getTime();
            jsonInfo.put("queryId", num);
            jsonInfo.put("openId", "ogSk-uPgFUmb6rALV5H3k76hzi6c");
            jsonInfo.put("queryTime", time);
            String risk = riskMapServiceImpl.getRisk("07FD3098-B250-4392-AAAC-994DC0A1F9A2", num,jsonInfo);

//            jsonInfo.put("eventId", num);
//            jsonInfo.put("userId", "ogSk-uPgFUmb6rALV5H3k76hzi6c");
//            jsonInfo.put("eventTime", time);
//            String risk = riskMapServiceImpl.getRisk(modelGuid, num,jsonInfo);

            System.out.println(risk);
            if (risk.equals("review")) {
                System.out.println("一些操作");
                ctx.next();
                // 返回的risk等级达到警戒值
            } else if (risk.equals("reject")) {
                // 返回的risk等级达到拒绝值
                ctx.fail(401, new Error("请求次数已达上限"));
            } else {
                // 还未到达风控值
                ctx.next();
            }
        } else {            //接口路由未绑定风控模型
            ctx.next();
        }
    }
}
