package com.tb.service.risk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sticker.online.core.anno.AsyncServiceHandler;
import com.sticker.online.core.model.BaseAsyncService;
import com.sticker.online.tools.common.utils.StringUtils;
import com.tb.service.risk.entity.RiskModel;
import com.tb.service.risk.repository.RiskMapRepsitory;
import com.tb.service.risk.service.RiskMapService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@AsyncServiceHandler
public class RiskMapServiceImpl implements RiskMapService,BaseAsyncService {
    @Autowired
    private RiskMapRepsitory riskMapRepsitory;
    private static RiskMapServiceImpl riskMapServiceImpl ;

    private final static String radar_url = "http://192.168.0.3:8035/services/v1/uploadInfo";

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        riskMapServiceImpl = this;
        riskMapServiceImpl.riskMapRepsitory = this.riskMapRepsitory;
        // 初使化时将已静态化的testService实例化
    }

    // 查询路径对应模型的关系列表
    @Override
    public void getModelList(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        Integer pageNum = params.getInteger("pageNum",1);
        Integer pageSize = params.getInteger("pageSize",10);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        RiskModel model = new RiskModel();
        String apiUrl = params.getString("apiPath");
        String modelName = params.getString("modelName");
        String modelGuid = params.getString("modelGuid");

        if (!StringUtils.isEmpty(apiUrl)) {
            model.setApiUrl(apiUrl);
            exampleMatcher.withMatcher("apiUrl", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        if (!StringUtils.isEmpty(modelName)) {
            model.setModelName(modelName);
            exampleMatcher.withMatcher("modelName", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        if (!StringUtils.isEmpty(modelGuid)) {
            model.setModelGuid(modelGuid);
            exampleMatcher.withMatcher("modelGuid", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }

        Example<RiskModel> example = Example.of(model, exampleMatcher);
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize,sort);
        Page<RiskModel> list = riskMapServiceImpl.riskMapRepsitory.findAll(example, pageable);
        future.complete(new JsonObject(Json.encode(list)));
        future.setHandler(resultHandler);
    }

    // 添加对应关系
    @Override
    public void addModel(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(date);
        RiskModel model = new RiskModel(params);
        model.setId(id);
        model.setCreateTime(createTime);
        riskMapServiceImpl.riskMapRepsitory.save(model);
        future.complete(new JsonObject(Json.encode(model)));
        future.setHandler(resultHandler);
    }

    // 编辑对应关系
    @Override
    public void editModelById(JsonObject params, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future<JsonObject> future= Future.future();
        String id = params.getString("id");
        if (id.isEmpty() || id == null) {
            throw new IllegalArgumentException("Not found");
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = sdf.format(date);
        RiskModel model = new RiskModel(params);
        model.setUpdateTime(updateTime);
        RiskModel model1 = riskMapServiceImpl.riskMapRepsitory.save(model);
        future.complete(new JsonObject(Json.encode(model1)));
        future.setHandler(resultHandler);
    }

    // 删除对应关系
    @Override
    public void deleteModelById(JsonObject params, Handler<AsyncResult<JsonArray>> resultHandler) {
        Future<JsonArray> future= Future.future();
        String id = params.getString("id");
        if (id == "" || id ==null) {
            throw new IllegalArgumentException("Not found");
        }else{
            riskMapServiceImpl.riskMapRepsitory.deleteById(id);
            future.complete(new JsonArray());
        }
        future.setHandler(resultHandler);
    }

    /**
     * 封装请求风控系统接口的调用
     * @param modelGuid 模型id
     * @param reqId 请求流水号
     * @param jsonInfo 请求json 数据
     * @return
     */
    public String getRisk(String modelGuid, String reqId, JsonObject jsonInfo) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
        pairList.add(new BasicNameValuePair("modelGuid",modelGuid));
        pairList.add(new BasicNameValuePair("reqId",reqId));
        pairList.add(new BasicNameValuePair("jsonInfo",jsonInfo.toString()));
        UrlEncodedFormEntity uefe = null;
        try {
            uefe= new UrlEncodedFormEntity(pairList, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(radar_url);
        httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.setEntity(uefe);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1, "UTF-8");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        JSONObject json = JSONObject.parseObject(result);
        String risk = json.getJSONObject("activations").getJSONObject("activation_121").getString("risk");
        return risk;
    }

    // 获取接口对应的风控模型id
    public String getList(String apiUrl) {
        RiskModel riskModel = new RiskModel();
        String modelGuid = "";
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        if (!StringUtils.isEmpty(apiUrl)) {
            riskModel.setApiUrl(apiUrl);
            exampleMatcher.withMatcher("apiUrl", ExampleMatcher.GenericPropertyMatchers.startsWith());
        }
        Example<RiskModel> example = Example.of(riskModel, exampleMatcher);
        Optional<RiskModel> optional = riskMapServiceImpl.riskMapRepsitory.findOne(example);
        if (optional.isPresent()) {
            RiskModel model = optional.get();
            modelGuid = model.getModelGuid();
            return modelGuid;
        } else {
            return modelGuid;
        }
    }
}
