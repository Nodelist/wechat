package com.tb.service.weixin.repository;


import com.tb.service.weixin.entity.WeChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional

public interface WeChatRepository extends JpaRepository<WeChat, String>, JpaSpecificationExecutor<WeChat> {
//    @Modifying
//    @Query(value = "update WeChat set WeChat.bindStatus=:bindStatus, WeChat.updateTime=:updateTime where WeChat.id=:id")
//    int updateBindStatus(@Param("id") String id, @Param("bindStatus") Integer bindStatus, @Param("updateTime") String updateTime);

    @Modifying
    @Query(value = "update WeChat wx SET wx.bindStatus=:bindStatus,wx.updateTime=:updateTime where wx.id=:id")
    int updateBindStatus(@Param("id") String id, @Param("bindStatus") Integer bindStatus, @Param("updateTime") String updateTime);
}