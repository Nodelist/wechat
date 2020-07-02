package com.tb.service.weixin.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface WechatLoginService {

    void list(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);


    void delete(JsonObject params, Handler<AsyncResult<String>> resultHandler);
    void model(JsonObject params, Handler<AsyncResult<String>> resultHandler);

}
