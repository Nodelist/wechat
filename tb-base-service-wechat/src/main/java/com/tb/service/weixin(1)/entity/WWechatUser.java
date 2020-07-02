package com.tb.service.weixin.entity;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@DataObject(generateConverter = true)
@Table(name = "w_wechat_user", schema = "tb_ksgjpt", catalog = "")
public class WWechatUser {

  private String id;
  private String userId;
  private String openId;

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    WWechatUserConverter.toJson(this, json);
    return json;
  }
  public WWechatUser() {}

  public WWechatUser(JsonObject jsonObject) {
    WWechatUserConverter.fromJson(jsonObject, this);
  }

  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "jpa-uuid")
  @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Basic
  @Column(name = "user_id")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "open_id")
  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }




}
