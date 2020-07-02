package com.tb.service.weixin.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.tb.base.common.vo.PageVo;
import com.tb.service.system.entity.SysUsers;
import com.tb.service.weixin.entity.WUser;
import com.tb.service.weixin.entity.WWechatUser;
import com.tb.service.weixin.repository.WWechatUserRepositiry;
import com.tb.service.weixin.service.WechatLoginService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
@AsyncServiceHandler
public class WechatLoginServiceImpl implements WechatLoginService, BaseAsyncService {

    @Autowired
    private WWechatUserRepositiry wWechatUserRepositiry;
    @Override
    public void list(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future = Future.future();
        PageVo pageVo = new PageVo(params);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
//        WWechatUser wWechatUser = new WWechatUser();
        WUser wUser=new WUser();
        ExampleMatcher.GenericPropertyMatchers.contains();
        Example<WUser> example=Example.of(wUser,exampleMatcher);
        Pageable pageable = PageRequest.of(pageVo.getPageNo() - 1, pageVo.getPageSize());
        Page<WUser> wUserList=wWechatUserRepositiry.queryAll(pageable);
//        Page<WWechatUser> page = wWechatUserRepositiry.findAll(example, pageable);
        future.complete(new JsonObject(Json.encode(wUserList)));
        future.setHandler(resultHandler);
    }

    @Override
    public void delete(JsonObject params, Handler<AsyncResult<String>> resultHandler) {
        Future<String> future = Future.future();
        wWechatUserRepositiry.deleteById(params.getString("id"));
        future.complete("删除成功！");
        resultHandler.handle(future);
    }

    @Override
    public void model(JsonObject params, Handler<AsyncResult<String>> resultHandler) {
        Future<String> future = Future.future();
        WWechatUser wWechatUser=new WWechatUser(params);
        wWechatUserRepositiry.save(wWechatUser);
        future.complete("绑定成功！");
        resultHandler.handle(future);
    }


}
