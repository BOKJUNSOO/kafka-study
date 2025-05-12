package com.spring.seoulmoaapi.global.common.batch.repository;

import com.spring.seoulmoaapi.global.common.batch.entity.BatchStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchStatusRepository extends JpaRepository<BatchStatus,Long> {
    @Query("SELECT b FROM BatchStatus b WHERE b.status = 0 ORDER BY b.executeTime DESC")
    List<BatchStatus> findByStatusOrderByExecuteTimeDesc(Pageable pageable);
}
