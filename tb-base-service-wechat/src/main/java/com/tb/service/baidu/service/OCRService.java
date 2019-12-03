package com.tb.service.baidu.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface OCRService {
    void getCardInfo(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
}
