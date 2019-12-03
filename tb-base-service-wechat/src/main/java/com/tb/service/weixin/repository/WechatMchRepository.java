package com.tb.service.weixin.repository;

import com.tb.service.weixin.entity.WechatMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WechatMchRepository extends JpaRepository<WechatMerchant, String>, JpaSpecificationExecutor<WechatMerchant> {

}
