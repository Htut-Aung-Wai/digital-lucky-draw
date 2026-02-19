package com.mytel.digital_lucky_draw.service;

import com.mytel.digital_lucky_draw.entity.DrawRecord;
import com.mytel.digital_lucky_draw.entity.Prize;
import com.mytel.digital_lucky_draw.repository.DrawRecordRepository;
import com.mytel.digital_lucky_draw.repository.PrizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrizeDrawServiceImpl implements PrizeDrawService{

    private final PrizeRepository prizeRepository;
    private final DrawRecordRepository drawRecordRepository;
    private final Random random = new Random();
    private static final int MAX_RETRY_ATTEMPTS = 10;
    private static final int PROBABILITY_BASE = 1000; // Base 1000 for probability calculation

    @Override
    @Transactional
    public Prize drawPrize(String username) {
        // Get all available prizes (with remaining quantity > 0)
        List<Prize> availablePrizes = prizeRepository.findAllByRemainingQuantityGreaterThan(0);

        if (availablePrizes.isEmpty()) {
            throw new RuntimeException("No prizes available");
        }

        int retryCount = 0;
        Prize selectedPrize = null;
        int randomValue = 0;

        // Retry until we successfully decrement a prize or reach max attempts
        while (retryCount < MAX_RETRY_ATTEMPTS) {
            // Build probability ranges based on base 1000
            List<PrizeRange> prizeRanges = buildPrizeRanges(availablePrizes);

            // Generate random number between 0 and 999 (base 1000)
            randomValue = random.nextInt(PROBABILITY_BASE);

            // Select prize based on range
            selectedPrize = selectPrizeByRange(prizeRanges, randomValue);

            if (selectedPrize == null) {
                throw new RuntimeException("Failed to select a prize");
            }

            // Try to decrement remaining quantity
            int updated = prizeRepository.decrementRemainingQuantity(selectedPrize.getId());

            if (updated > 0) {
                // Successfully decremented, break out of retry loop
                break;
            }

            // Prize quantity is 0, remove it from available prizes and retry
            log.warn("Prize {} has no remaining quantity, skipping and retrying", selectedPrize.getPrizeName());
            availablePrizes.remove(selectedPrize);

            // Check if we still have available prizes
            if (availablePrizes.isEmpty()) {
                throw new RuntimeException("No prizes available");
            }

            retryCount++;
        }

        if (retryCount >= MAX_RETRY_ATTEMPTS) {
            throw new RuntimeException("Failed to draw prize after multiple attempts");
        }

        // Refresh prize to get updated quantity
        selectedPrize = prizeRepository.findById(selectedPrize.getId())
                .orElseThrow(() -> new RuntimeException("Prize not found"));

        // Save draw record with prizeId
        DrawRecord drawRecord = DrawRecord.builder()
                .prizeId(selectedPrize.getId())
                .prizeName(selectedPrize.getPrizeName())
                .username(username)
                .build();

        drawRecordRepository.save(drawRecord);

        log.info("Prize drawn: {} (random value: {}) for user: {}",
                selectedPrize.getPrizeName(), randomValue, username);

        return selectedPrize;
    }

    /**
     * Builds prize ranges based on base 1000
     * Example:
     * - Calendar 23.3% = 233 (range 0-232)
     * - Notebook 20% = 200 (range 233-432)
     * - Handfan 23.3% = 233 (range 433-665)
     * - Tickets 20% = 200 (range 666-865)
     * - Umbrella 6.7% = 67 (range 866-932)
     * - Water Bottle 6.7% = 67 (range 933-999)
     */
    private List<PrizeRange> buildPrizeRanges(List<Prize> prizes) {
        List<PrizeRange> ranges = new ArrayList<>();
        int startRange = 0;

        for (Prize prize : prizes) {
            // Calculate range size based on percentage of 1000
            // percentage * 1000 / 100 = percentage * 10
            int rangeSize = (int) Math.round(prize.getProbability().doubleValue() * 10);
            int endRange = startRange + rangeSize - 1;

            ranges.add(new PrizeRange(prize, startRange, endRange));
            startRange = endRange + 1;
        }

        return ranges;
    }

    /**
     * Selects prize based on which range the random value falls into
     */
    private Prize selectPrizeByRange(List<PrizeRange> ranges, int randomValue) {
        for (PrizeRange range : ranges) {
            if (randomValue >= range.startRange && randomValue <= range.endRange) {
                return range.prize;
            }
        }
        // Fallback to last prize if somehow out of range
        return ranges.isEmpty() ? null : ranges.get(ranges.size() - 1).prize;
    }

    /**
     * Inner class to hold prize and its range
     */
    private static class PrizeRange {
        Prize prize;
        int startRange;
        int endRange;

        PrizeRange(Prize prize, int startRange, int endRange) {
            this.prize = prize;
            this.startRange = startRange;
            this.endRange = endRange;
        }
    }

    @Override
    public List<Prize> getAllPrizes() {
        return prizeRepository.findAll();
    }

    @Override
    public List<DrawRecord> getUserDrawHistory() {
        return drawRecordRepository.findByIsDeletedFalse();
    }
}