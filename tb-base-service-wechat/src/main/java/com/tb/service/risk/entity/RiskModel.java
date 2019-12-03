package com.tb.service.risk.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import javax.persistence.*;

@Entity
@DataObject(generateConverter = true)
@Table(name = "path_model")
public class RiskModel {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "model_guid")
    private String modelGuid;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "api_url")
    private String apiUrl;

    @Column(name = "status")
    private String status;

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

    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelGuid() {
        return modelGuid;
    }
    public void setModelGuid(String modelGuid) {
        this.modelGuid = modelGuid;
    }

    public String getApiUrl() {
        return apiUrl;
    }
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public RiskModel() { }

    public RiskModel(JsonObject jsonObject) {
        RiskModelConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RiskModelConverter.toJson(this, json);
        return json;
    }

}