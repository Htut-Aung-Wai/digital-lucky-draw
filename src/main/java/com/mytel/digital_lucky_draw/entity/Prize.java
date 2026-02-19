package com.mytel.digital_lucky_draw.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DLD_PRIZES")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prize extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID", columnDefinition = "VARCHAR(255)")
    private String id;

    @Column(name = "PRIZE_NAME", nullable = false)
    private String prizeName;

    @Column(name = "TOTAL_QUANTITY", nullable = false)
    private Integer totalQuantity;

    @Column(name = "REMAINING_QUANTITY", nullable = false)
    private Integer remainingQuantity;

    @Column(name = "PROBABILITY", nullable = false, precision = 5, scale = 2)
    private BigDecimal probability;


}