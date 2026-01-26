package com.duckpao.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.duckpao.order.model.ApiLog;
public interface ApiLogRepository  extends JpaRepository<ApiLog, Long> {


}
