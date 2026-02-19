package com.mytel.digital_lucky_draw.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DLD_DRAW_RECORDS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrawRecord extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID", columnDefinition = "VARCHAR(255)")
    private String id;

    @Column(name = "PRIZE_ID", nullable = false)
    private String prizeId;

    @Column(name = "PRIZE_NAME", nullable = false)
    private String prizeName;

    @Column(name = "USERNAME")
    private String username;


}