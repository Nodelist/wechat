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
import com.tb.service.weixin.service.WechatMchService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
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
public class WechatMchServiceImpl implements WechatMchService, BaseAsyncService {

    @Autowired
    private WechatMchRepository wechatMchRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private WeChatRepository weChatRepository;

    private static WechatMchServiceImpl wechatMchServiceImpl ;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        wechatMchServiceImpl = this;
        wechatMchServiceImpl.wechatMchRepository = this.wechatMchRepository;
        wechatMchServiceImpl.merchantRepository = this.merchantRepository;
        wechatMchServiceImpl.weChatRepository = this.weChatRepository;
        // 初使化时将已静态化的testService实例化
    }

    /**
     * 公众号绑定商户号
     * @param params
     * @param resultHandler
     */
    // 获取公众号对应的商户号信息
    @Override
    public void getMch(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        WechatMerchant wechatMch = new WechatMerchant();
        String appId = params.getString("appId");
        if (!StringUtils.isEmpty(appId)) {
            wechatMch.setAppId(appId);
            exampleMatcher.withMatcher("appId", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        Example<WechatMerchant> example = Example.of(wechatMch, exampleMatcher);
        Optional<WechatMerchant> opt = wechatMchServiceImpl.wechatMchRepository.findOne(example);
        if (opt.isPresent()) {
            WechatMerchant wxMch = opt.get();
            String id = wxMch.getId();
            String pId = wxMch.getPId();
            if(pId!=null){
                // 查询商户表
                Merchant merchant = new Merchant();
                ExampleMatcher exampleMatcher_mch = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
                if (!StringUtils.isEmpty(pId)) {
                    merchant.setId(pId);
                    exampleMatcher_mch.withMatcher("pId", ExampleMatcher.GenericPropertyMatchers.startsWith());
                }
                Example<Merchant> example_mch = Example.of(merchant, exampleMatcher_mch);
                Optional<Merchant> mch_Info = wechatMchServiceImpl.merchantRepository.findOne(example_mch);
                if (mch_Info.isPresent()) {
                    Merchant mch = mch_Info.get();
                    JsonObject result = new JsonObject(Json.encode(mch));
                    result.put("bindId",id);
                    future.complete(result);
                    future.setHandler(resultHandler);
                }
            } else {
                future.setHandler(resultHandler);
            }
        } else {
            future.setHandler(resultHandler);
        }
    }

    // 给公众号添加绑定
    @Override
    public void add(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) throws IllegalArgumentException {
        Future<JsonObject> future= Future.future();
        // 在绑定关系表中新增一条记录
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(date);
        WechatMerchant wechatMch = new WechatMerchant();
        String appid = params.getString("appId");
        String pId = params.getString("pId");
        wechatMch.setId(id);
        wechatMch.setPId(pId);
        wechatMch.setAppId(appid);
        wechatMch.setCreateTime(createTime);
        WechatMerchant wechatMch1 = wechatMchServiceImpl.wechatMchRepository.save(wechatMch);
        JsonObject result = new JsonObject(Json.encode(wechatMch1));

        // 改变wechat_info表的绑定状态值
        String wechatId = params.getString("wechatId");
        String appName = params.getString("appName");
        String appSecret = params.getString("appSecret");
        String remarks = params.getString("remarks");
        String type = params.getString("appType");
        String creat = params.getString("createTime");
        Integer bindStatus = 1;
        WeChat weChat = new WeChat();
        weChat.setId(wechatId);
        weChat.setAppId(appid);
        weChat.setAppName(appName);
        weChat.setAppSecret(appSecret);
        weChat.setRemarks(remarks);
        weChat.setAppType(type);
        weChat.setCreateTime(creat);
        weChat.setUpdateTime(createTime);
        weChat.setBindStatus(bindStatus);
        wechatMchServiceImpl.weChatRepository.save(weChat);
        future.complete(result.put("bindStatus",bindStatus));
        future.setHandler(resultHandler);
    }

    // 给公众号修改绑定
    @Override
    public void edit(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) throws IllegalArgumentException {
        Future<JsonObject> future= Future.future();
        String id = params.getString("id");
        // 判断id值是否存在，若存在，则更新；若不存在，则新增一条记录
        if (id.isEmpty()) {
            throw new IllegalArgumentException("App is not bind");
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = sdf.format(date);
        WechatMerchant wechatMch = new WechatMerchant(params);
        wechatMch.setUpdateTime(updateTime);
        WechatMerchant wechatMch1 = wechatMchServiceImpl.wechatMchRepository.save(wechatMch);
        future.complete(new JsonObject(Json.encode(wechatMch1)));
        future.setHandler(resultHandler);
    }

    // 解除绑定商户号
    @Override
    public void delete(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) throws IllegalArgumentException {
        Future<JsonObject> future= Future.future();
        // 删除绑定记录
        String id = params.getString("id");
        if (id == "" || id ==null) {
            throw new IllegalArgumentException("bind app is not found");
        }else{
            wechatMchServiceImpl.wechatMchRepository.deleteById(id);
        }

        // 改变wechat_info表的绑定状态值
        String wechatId = params.getString("wechatId");
        String appid = params.getString("appId");
        String appName = params.getString("appName");
        String appSecret = params.getString("appSecret");
        String remarks = params.getString("remarks");
        String type = params.getString("appType");
        String creat = params.getString("createTime");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = sdf.format(date);
        Integer bindStatus = 0;
        WeChat weChat = new WeChat();
        weChat.setId(wechatId);
        weChat.setAppId(appid);
        weChat.setAppName(appName);
        weChat.setAppSecret(appSecret);
        weChat.setRemarks(remarks);
        weChat.setAppType(type);
        weChat.setCreateTime(creat);
        weChat.setUpdateTime(updateTime);
        weChat.setBindStatus(bindStatus);
        WeChat weChat1 = wechatMchServiceImpl.weChatRepository.save(weChat);
        future.complete(new JsonObject(Json.encode(weChat1)));
        future.setHandler(resultHandler);
    }
}
