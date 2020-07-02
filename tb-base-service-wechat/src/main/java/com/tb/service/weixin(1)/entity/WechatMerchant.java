package com.tb.service.weixin.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @FileName: com.sticker.online.springvertx.main.entity
 * @Author: Sticker
 * @Date: 2019/6/1
 * @Version: 1.0
 */
@Entity
@DataObject(generateConverter = true)
@Table(name = "w_wechat_binding_mch")
public class WechatMerchant {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "pid_mch")
    private String pidMch;

    @Column(name = "pid_app")
    private String pidApp;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPidMch() {
        return pidMch;
    }

    public void setPidMch(String pidMch) {
        this.pidMch = pidMch;
    }

    public String getPidApp() {
        return pidApp;
    }

    public void setPidApp(String pidApp) {
        this.pidApp = pidApp;
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

    public WechatMerchant() { }

    public WechatMerchant(JsonObject jsonObject) {
        WechatMerchantConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        WechatMerchantConverter.toJson(this, json);
        return json;
    }

}

