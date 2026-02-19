package com.mytel.digital_lucky_draw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrawResponse {

    private String prizeId;
    private String prizeName;
    private Integer remainingQuantity;
    private LocalDateTime drawDate;
    private String message;
}