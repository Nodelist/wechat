package com.tb.service.risk.repository;

import com.tb.service.risk.entity.RiskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskMapRepsitory extends JpaRepository<RiskModel, String>, JpaSpecificationExecutor<RiskModel> {
}
