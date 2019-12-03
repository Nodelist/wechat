package com.tb.service.weixin.repository;


import com.tb.service.weixin.entity.WeChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeChatRepository extends JpaRepository<WeChat, String>, JpaSpecificationExecutor<WeChat> {

}