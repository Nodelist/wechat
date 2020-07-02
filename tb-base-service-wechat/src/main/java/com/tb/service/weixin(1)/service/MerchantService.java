package com.tb.service.weixin.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;



@ProxyGen
public interface MerchantService {
    void deleteById(JsonObject params, Handler<AsyncResult<JsonArray>> resultHandler);
    void addMerchant(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    void editMerchant(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    void getList(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);

}
