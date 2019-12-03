package com.tb.service.weixin.service.impl;

import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.sticker.online.tools.common.utils.StringUtils;
import com.tb.service.weixin.entity.Merchant;
import com.tb.service.weixin.entity.WeChat;
import com.tb.service.weixin.entity.WechatMerchant;
import com.tb.service.weixin.repository.MerchantRepository;
import com.tb.service.weixin.repository.WeChatRepository;
import com.tb.service.weixin.repository.WechatMchRepository;
import com.tb.service.weixin.service.WeChatService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
@Service
@AsyncServiceHandler
public class WeChatServiceImpl implements WeChatService, BaseAsyncService {

    @Autowired
    private WeChatRepository weChatRepository;
    @Autowired
    private WechatMchRepository wechatMchRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    private static WeChatServiceImpl weChatServiceImpl ;


    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        weChatServiceImpl = this;
        weChatServiceImpl.weChatRepository = this.weChatRepository;
        weChatServiceImpl.wechatMchRepository = this.wechatMchRepository;
        weChatServiceImpl.merchantRepository = this.merchantRepository;
        // 初使化时将已静态化的testService实例化
    }
    @Override
    public void getAppList(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        Integer pageNum = params.getInteger("pageNum",1);
        Integer pageSize = params.getInteger("pageSize",10);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        WeChat weChat = new WeChat();
        String appId = params.getString("appId");
        String appName = params.getString("appName");
        String appType = params.getString("appType");

        if (!StringUtils.isEmpty(appId)) {
            weChat.setAppId(appId);
            exampleMatcher.withMatcher("appId", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        if (!StringUtils.isEmpty(appName)) {
            weChat.setAppName(appName);
            exampleMatcher.withMatcher("appName", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        if (!StringUtils.isEmpty(appType)) {
            weChat.setAppType(appType);
            exampleMatcher.withMatcher("appType", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }

        Example<WeChat> example = Example.of(weChat, exampleMatcher);
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize,sort);
        Page<WeChat> list = weChatServiceImpl.weChatRepository.findAll(example, pageable);
        future.complete(new JsonObject(Json.encode(list)));
        future.setHandler(resultHandler);
    }

    @Override
    public void deleteAppById(JsonObject params, Handler<AsyncResult<JsonArray>> resultHandler) throws IllegalArgumentException {
        Future<JsonArray> future= Future.future();
        String id = params.getString("id");
        if (id == "" || id ==null) {
            throw new IllegalArgumentException("app is not found");
        }else{
            weChatServiceImpl.weChatRepository.deleteById(id);
            future.complete(new JsonArray());
        }
        future.setHandler(resultHandler);
    }

    @Override
    public void addApp(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(date);
        WeChat weChat = new WeChat(params);
        weChat.setId(id);
        weChat.setCreateTime(createTime);
        weChatServiceImpl.weChatRepository.save(weChat);
        future.complete(new JsonObject(Json.encode(weChat)));
        future.setHandler(resultHandler);
    }

    @Override
    public void editAppById(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) throws IllegalArgumentException {
        Future<JsonObject> future= Future.future();
        String id = params.getString("id");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("app is not found");
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = sdf.format(date);
        WeChat weChat = new WeChat(params);
        weChat.setUpdateTime(updateTime);
        WeChat weChat1 = weChatServiceImpl.weChatRepository.save(weChat);
        future.complete(new JsonObject(Json.encode(weChat1)));
        future.setHandler(resultHandler);
    }

    // 异步查询密钥
    public void getAppSecret(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        WeChat weChat = new WeChat();
        String appid = params.getString("appid");
        if (!StringUtils.isEmpty(appid)) {
            weChat.setAppId(appid);
            exampleMatcher.withMatcher("appid", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }

        Example<WeChat> example = Example.of(weChat, exampleMatcher);
        Optional<WeChat> opt = weChatServiceImpl.weChatRepository.findOne(example);
        if (opt.isPresent()) {
            WeChat weChat1 = opt.get();
            JsonObject appSecret = new JsonObject();
            appSecret.put("appSecret",weChat1.getAppSecret());
            future.complete(appSecret);
            future.setHandler(resultHandler);
        } else {
            future.setHandler(resultHandler);
        }

    }

    // 异步查询商户信息
    public void getMerchant(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        WechatMerchant wechatMerchant = new WechatMerchant();
        String appid = params.getString("appid");
        if (!StringUtils.isEmpty(appid)) {
            wechatMerchant.setAppId(appid);
            exampleMatcher.withMatcher("appId", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        Example<WechatMerchant> example = Example.of(wechatMerchant, exampleMatcher);
        Optional<WechatMerchant> opt = weChatServiceImpl.wechatMchRepository.findOne(example);
        if (opt.isPresent()) {
            WechatMerchant wxMch = opt.get();
            String pId = wxMch.getPId();

            // 查询商户表
            Merchant merchant = new Merchant();
            ExampleMatcher exampleMatcher_mch = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
            if (!StringUtils.isEmpty(pId)) {
                merchant.setId(pId);
                exampleMatcher_mch.withMatcher("pId", ExampleMatcher.GenericPropertyMatchers.startsWith());
            }
            Example<Merchant> example_mch = Example.of(merchant, exampleMatcher_mch);
            Optional<Merchant> mch_list = weChatServiceImpl.merchantRepository.findOne(example_mch);
            if (mch_list.isPresent()) {
                Merchant mch = mch_list.get();
                future.complete(new JsonObject(Json.encode(mch)));
                future.setHandler(resultHandler);
            }
        } else {
            future.setHandler(resultHandler);
        }

    }
}
