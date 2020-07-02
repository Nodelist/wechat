package com.tb.service.weixin.repository;

import com.tb.service.weixin.entity.Merchant;
import com.tb.service.weixin.entity.WeChat;
import com.tb.service.weixin.entity.WechatMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional

public interface WechatMchRepository extends JpaRepository<WechatMerchant, String>, JpaSpecificationExecutor<WechatMerchant> {
    @Query(value = "select wMch from WechatMerchant wMch LEFT JOIN Merchant mch on mch.id=wMch.pidMch where wMch.pidApp=:pidApp")
    Optional<WechatMerchant> queryMch(@Param("pidApp") String pidApp);

//    @Modifying
//    @Query(value = "update WeChat set WeChat.bindStatus=:bindStatus, WeChat.updateTime=:updateTime where WeChat.id=:id")
//    int updateBindStatus(@Param("id") String id, @Param("bindStatus") Integer bindStatus, @Param("updateTime") String updateTime);

//    @Modifying
//    @Query(value = "insert into WechatMerchant (id, pidApp, pidMch, createTime) values(:id, :pidApp, :pidMch, :createTime)")
//    int addBindInfo(@Param("id") String id, @Param("pidApp") String pidApp, @Param("pidMch") String pidMch,
//                    @Param("createTime") Date createTime);
}
