package com.mytel.digital_lucky_draw.repository;


import com.mytel.digital_lucky_draw.entity.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, String > {

    List<Prize> findAllByRemainingQuantityGreaterThan(Integer quantity);

    Optional<Prize> findByPrizeName(String prizeName);

    @Modifying
    @Query("UPDATE Prize p SET p.remainingQuantity = p.remainingQuantity - 1 WHERE p.id = :prizeId AND p.remainingQuantity > 0")
    int decrementRemainingQuantity(@Param("prizeId") String prizeId);
}
