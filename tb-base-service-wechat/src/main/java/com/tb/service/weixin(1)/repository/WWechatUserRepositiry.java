package com.tb.service.weixin.repository;

import com.tb.service.weixin.entity.WUser;
import com.tb.service.weixin.entity.WWechatUser;
import com.tb.service.weixin.entity.WechatMerchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tb.service.weixin.entity.WechatSysUsers;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface WWechatUserRepositiry extends JpaRepository<WWechatUser, String> {


    @Query(value = "SELECT new com.tb.service.weixin.entity.WUser(s,w) from WWechatUser w left join WechatSysUsers s ON w.userId=s.id ")
    Page<WUser> queryAll(Pageable pageable);


}
