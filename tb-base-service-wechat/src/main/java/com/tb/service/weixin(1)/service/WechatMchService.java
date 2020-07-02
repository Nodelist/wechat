package com.tb.service.weixin.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;




@ProxyGen
public interface WechatMchService {
    void add(JsonObject params, Handler<AsyncResult<String>> resultHandler);
    void getMch(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    void edit(JsonObject params, Handler<AsyncResult<String>> resultHandler);
    void delete(JsonObject params, Handler<AsyncResult<String>> resultHandler);
}
