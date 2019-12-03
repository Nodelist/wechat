package com.tb.service.weixin.service.impl;

import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.sticker.online.tools.common.utils.StringUtils;
import com.tb.service.weixin.entity.Merchant;
import com.tb.service.weixin.repository.MerchantRepository;
import com.tb.service.weixin.service.MerchantService;
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
import java.util.UUID;

@Component
@Service
@AsyncServiceHandler
public class MerchantServiceImpl implements MerchantService, BaseAsyncService {

    @Autowired
    private MerchantRepository merchantRepository;
    private static MerchantServiceImpl merchantServiceImpl ;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        merchantServiceImpl = this;
        merchantServiceImpl.merchantRepository = this.merchantRepository;
        // 初使化时将已静态化的testService实例化
    }
    @Override
    public void getList(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        Integer pageNum = params.getInteger("pageNum",1);
        Integer pageSize = params.getInteger("pageSize",10);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Merchant merchant = new Merchant();
        String mchId = params.getString("mchId");
        String mchName = params.getString("mchName");
        if (!StringUtils.isEmpty(mchId)) {
            merchant.setMchId(mchId);
            exampleMatcher.withMatcher("mchId", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        if (!StringUtils.isEmpty(mchName)) {
            merchant.setMchName(mchName);
            exampleMatcher.withMatcher("mchName", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        Example<Merchant> example = Example.of(merchant, exampleMatcher);
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize,sort);
        Page<Merchant> list = merchantServiceImpl.merchantRepository.findAll(example, pageable);
        future.complete(new JsonObject(Json.encode(list)));
        future.setHandler(resultHandler);
    }

    @Override
    public void deleteById(JsonObject params, Handler<AsyncResult<JsonArray>> resultHandler) throws IllegalArgumentException {
        Future<JsonArray> future= Future.future();
        String id = params.getString("id");
        if (id == "" || id ==null) {
            throw new IllegalArgumentException("Not found");
        }else{
            merchantServiceImpl.merchantRepository.deleteById(id);
            future.complete(new JsonArray());
        }
        future.setHandler(resultHandler);
    }

    @Override
    public void addMerchant(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(date);
        Merchant merchant = new Merchant(params);
        merchant.setId(id);
        merchant.setCreateTime(createTime);
        merchantServiceImpl.merchantRepository.save(merchant);
        future.complete(new JsonObject(Json.encode(merchant)));
        future.setHandler(resultHandler);
    }

    @Override
    public void editMerchant(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) throws IllegalArgumentException {
        Future<JsonObject> future= Future.future();
        String id = params.getString("id");
        if (id.isEmpty() || id == null) {
            throw new IllegalArgumentException("app is not found");
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = sdf.format(date);
        Merchant merchant = new Merchant(params);
        merchant.setUpdateTime(updateTime);
        Merchant merchant1 = merchantServiceImpl.merchantRepository.save(merchant);
        future.complete(new JsonObject(Json.encode(merchant1)));
        future.setHandler(resultHandler);
    }
}
