package com.mytel.digital_lucky_draw.controller;

import com.mytel.digital_lucky_draw.constant.ErrorCode;
import com.mytel.digital_lucky_draw.dto.DrawRequest;
import com.mytel.digital_lucky_draw.dto.DrawResponse;
import com.mytel.digital_lucky_draw.entity.DrawRecord;
import com.mytel.digital_lucky_draw.entity.Prize;
import com.mytel.digital_lucky_draw.response.Basic;
import com.mytel.digital_lucky_draw.response.ResponseFactory;
import com.mytel.digital_lucky_draw.service.PrizeDrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/prize-draw")
public class PrizeDrawController {

    private final PrizeDrawService prizeDrawService;
    private final ResponseFactory responseFactory;

    @PostMapping("/draw")
    public ResponseEntity<?> drawPrize() {
        try {
            // Get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "anonymous";

            Prize drawnPrize = prizeDrawService.drawPrize(username);

            DrawResponse response = DrawResponse.builder()
                    .prizeId(drawnPrize.getId())
                    .prizeName(drawnPrize.getPrizeName())
                    .remainingQuantity(drawnPrize.getRemainingQuantity())
                    .drawDate(drawnPrize.getCreatedAt())
                    .message("Congratulations! You won: " + drawnPrize.getPrizeName())
                    .build();

            return responseFactory.buildSuccess(
                    HttpStatus.OK,
                    response,
                    ErrorCode.CODE_200,
                    "Prize drawn successfully"
            );
        } catch (RuntimeException e) {
            log.error("Error drawing prize: {}", e.getMessage());
            return responseFactory.buildError(
                    HttpStatus.BAD_REQUEST,
                    null,
                    ErrorCode.ERROR_400,
                    e.getMessage()
            );
        } catch (Exception e) {
            log.error("Unexpected error drawing prize", e);
            return responseFactory.buildError(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    ErrorCode.ERROR_500,
                    "An error occurred while drawing the prize"
            );
        }
    }

    @GetMapping("/prizes")
    public ResponseEntity<Basic> getAllPrizes() {
        try {
            List<Prize> prizes = prizeDrawService.getAllPrizes();
            return responseFactory.buildSuccess(
                    HttpStatus.OK,
                    prizes,
                    ErrorCode.CODE_200,
                    "Prizes retrieved successfully"
            );
        } catch (Exception e) {
            log.error("Error retrieving prizes", e);
            return responseFactory.buildError(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    ErrorCode.ERROR_500,
                    "An error occurred while retrieving prizes"
            );
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Basic> getUserDrawHistory() {
        try {
            List<DrawRecord> drawHistory = prizeDrawService.getUserDrawHistory();

            List<DrawResponse> historyResponse = drawHistory.stream()
                    .map(record -> DrawResponse.builder()
                            .prizeId(record.getPrizeId())
                            .prizeName(record.getPrizeName())
                            .drawDate(record.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());

            return responseFactory.buildSuccess(
                    HttpStatus.OK,
                    historyResponse,
                    ErrorCode.CODE_200,
                    "Draw history retrieved successfully"
            );
        } catch (Exception e) {
            log.error("Error retrieving draw history", e);
            return responseFactory.buildError(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    ErrorCode.ERROR_500,
                    "An error occurred while retrieving draw history"
            );
        }
    }
}