package com.tb.service.weixin.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;



@ProxyGen
public interface WeChatService {
    void deleteAppById(JsonObject params, Handler<AsyncResult<JsonArray>> resultHandler);
    void addApp(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    void editAppById(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    void getAppList(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);

}
