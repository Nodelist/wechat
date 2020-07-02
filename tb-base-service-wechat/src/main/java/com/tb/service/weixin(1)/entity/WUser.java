package com.tb.service.weixin.entity;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

//@Entity
//@DataObject(generateConverter = true)
public class WUser {

  private String id;
  private String userId;
  private String openId;
  private String realname;
  private String username;
  private String avatar;
  private String birthday;
  private Integer sex;
  private String phone;
  private String email;


  public WUser() {
  }

  public WUser(WechatSysUsers wUser,WWechatUser wWechatUser) {
    this.id=wWechatUser.getId();
    this.userId=wWechatUser.getUserId()==null?"":wWechatUser.getUserId();
    this.openId=wWechatUser.getOpenId()==null?"":wWechatUser.getOpenId();
    if(wUser!=null){
      this.realname=wUser.getRealname()==null?"":wUser.getRealname();
      this.username=wUser.getUsername()==null?"":wUser.getUsername();
      this.avatar=wUser.getAvatar()==null?"":wUser.getAvatar();
      this.birthday=wUser.getBirthday()==null?"":wUser.getAvatar();
      this.sex=wUser.getSex()==null?null:wUser.getSex();
      this.phone=wUser.getPhone()==null?"":wUser.getPhone();
      this.email=wUser.getEmail()==null?"":wUser.getEmail();
    }

  }
//  public WUser(JsonObject jsonObject) {
//    WUserConverter.fromJson(jsonObject, this);
//  }

//  public JsonObject toJson() {
//    JsonObject json = new JsonObject();
//    WUserConverter.toJson(this, json);
//    return json;
//  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }


  public String getRealname() {
    return realname;
  }

  public void setRealname(String realname) {
    this.realname = realname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public Integer getSex() {
    return sex;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
