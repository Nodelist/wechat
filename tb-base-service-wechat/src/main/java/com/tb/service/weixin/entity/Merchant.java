package com.tb.service.weixin.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import javax.persistence.*;

/**
 * @FileName: com.sticker.online.springvertx.main.entity
 * @Author: Sticker
 * @Date: 2019/6/1
 * @Version: 1.0
 */
@Entity
@DataObject(generateConverter = true)
@Table(name = "mch_info")
public class Merchant {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "mch_name")
    private String mchName;

    @Column(name = "mch_id")
    private String mchId;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "remarks")
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMchName() {
        return mchName;
    }
    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getMchId() {
        return mchId;
    }
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Merchant() { }

    public Merchant(JsonObject jsonObject) {
        MerchantConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        MerchantConverter.toJson(this, json);
        return json;
    }

}

