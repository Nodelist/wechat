package com.tb.service.risk.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface RiskMapService {
    // 获取模型路径映射关系列表
    void getModelList(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    // 获取模型路径映射关系
    void addModel(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    // 获取模型路径映射关系
    void editModelById(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    // 获取模型路径映射关系
    void deleteModelById(JsonObject params, Handler<AsyncResult<JsonArray>> resultHandler);

}
