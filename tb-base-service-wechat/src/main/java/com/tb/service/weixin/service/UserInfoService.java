package com.tb.service.weixin.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface UserInfoService {
    void getUserInfo(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
    void getAppletUserInfo(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler);
}
