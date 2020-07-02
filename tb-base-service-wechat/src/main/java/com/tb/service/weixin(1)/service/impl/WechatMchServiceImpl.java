package com.tb.service.weixin.service.impl;

import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.sticker.online.core.utils.TimeUtil;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * @param handler
     */
    // 获取公众号对应的商户号信息
    @Override
    public void getMch(JsonObject params, Handler<AsyncResult<JsonObject>> handler) {
        Future<JsonObject> future= Future.future();
        String pidApp = params.getString("pidApp");
        WechatMerchant wMch = wechatMchServiceImpl.wechatMchRepository.queryMch(pidApp).get();
        System.out.println(wMch);
        future.complete(new JsonObject(Json.encode(wMch)));
        handler.handle(future);
    }

    // 给公众号添加绑定
    @Override
    public void add(JsonObject params, Handler<AsyncResult<String>> handler) throws IllegalArgumentException {
        Future<String> future= Future.future();
        // 在绑定关系表中新增一条记录
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        WechatMerchant wechatMch = new WechatMerchant();
        wechatMch.setId(id);
        wechatMch.setPidMch(params.getString("pidMch"));
        wechatMch.setPidApp(params.getString("pidApp"));
        wechatMch.setCreateTime(time);
        try {
            // 新增一条绑定记录
            wechatMchServiceImpl.wechatMchRepository.save(wechatMch);
            // 改变wechat_info表的绑定状态值
            wechatMchServiceImpl.weChatRepository.updateBindStatus(params.getString("pidApp"), 1, time);
            future.complete("绑定成功");
        } catch(Exception e) {
            future.complete("绑定失败");
        }
        handler.handle(future);
    }

    // 给公众号修改绑定
    @Override
    public void edit(JsonObject params, Handler<AsyncResult<String>> handler) throws IllegalArgumentException {
        Future<String> future= Future.future();
        String id = params.getString("id");
        // 判断id值是否存在，若存在，则更新；若不存在，则新增一条记录
        if (id.isEmpty()) {
            throw new IllegalArgumentException("App is not bind");
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = sdf.format(date);
//        WechatMerchant wechatMch = new WechatMerchant(params);
        WechatMerchant wechatMch = new WechatMerchant();
        wechatMch.setId(params.getString("id"));
        wechatMch.setPidMch(params.getString("pidMch"));
        wechatMch.setPidApp(params.getString("pidApp"));
        wechatMch.setCreateTime(params.getString("createTime"));
        wechatMch.setUpdateTime(updateTime);
        WechatMerchant wechatMch1 = wechatMchServiceImpl.wechatMchRepository.save(wechatMch);
        future.complete("修改成功");
        handler.handle(future);
    }

    // 解除绑定商户号
    @Override
    public void delete(JsonObject params, Handler<AsyncResult<String>> handler) throws IllegalArgumentException {
        Future<String> future= Future.future();
        // 删除绑定记录
        String id = params.getString("id");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        if (id == "" || id ==null) {
            throw new IllegalArgumentException("bind app is not found");
        }else{
            wechatMchServiceImpl.wechatMchRepository.deleteById(id);
            // 改变wechat_info表的绑定状态值
            String pidApp = params.getString("pidApp");
            wechatMchServiceImpl.weChatRepository.updateBindStatus(params.getString("pidApp"), 0, time);
            future.complete("解除成功");
            handler.handle(future);
        }
    }
}
