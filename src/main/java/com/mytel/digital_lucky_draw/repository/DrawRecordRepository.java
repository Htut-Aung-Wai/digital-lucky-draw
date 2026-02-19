package com.mytel.digital_lucky_draw.repository;

import com.mytel.digital_lucky_draw.entity.DrawRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DrawRecordRepository extends JpaRepository<DrawRecord, Long> {

    List<DrawRecord> findByIsDeletedFalse();

    //List<DrawRecord> findByDrawDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    long countByPrizeId(Long prizeId);
}