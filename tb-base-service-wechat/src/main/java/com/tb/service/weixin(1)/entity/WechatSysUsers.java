package com.tb.service.weixin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sticker.online.core.anno.Dict;
import com.sticker.online.core.anno.Excel;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Entity
@DataObject(generateConverter = true)
@Table(name = "s_user")
public class WechatSysUsers implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
//    @GeneratedValue(generator = "jpa-uuid")
//    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;

    /**
     * 登录账号
     */
    @Excel(name = "登录账号", width = 15)
    private String username;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    private String realname;

    /**
     * 密码
     */
    private String password;



    /**
     * md5密码盐
     */
    private String salt;

    /**
     * 头像
     */
    @Excel(name = "头像", width = 15)
    private String avatar;

    /**
     * 生日
     */
    @Column(name = "birthday")
//    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Excel(name = "性别", width = 15,dicCode="sex")
    @Dict(dicCode = "sex")
    private Integer sex;

    /**
     * 电子邮件
     */
    @Excel(name = "电子邮件", width = 15)
    private String email;

    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    private String phone;

    /**
     * 部门code
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    @Excel(name = "状态", width = 15,dicCode="user_status")
    @Dict(dicCode = "user_status")
    private Integer status;

    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;
    /**
     * 同步工作流引擎1同步0不同步
     */
    @Column(name = "activiti_sync")
    private String activitiSync;

    public WechatSysUsers() {
    }
    public WechatSysUsers(JsonObject jsonObject) {
        WechatSysUsersConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        WechatSysUsersConverter.toJson(this, json);
        return json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getActivitiSync() {
        return activitiSync;
    }

    public void setActivitiSync(String activitiSync) {
        this.activitiSync = activitiSync;
    }
}
