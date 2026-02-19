package com.mytel.digital_lucky_draw.service;

import com.mytel.digital_lucky_draw.entity.DrawRecord;
import com.mytel.digital_lucky_draw.entity.Prize;

import java.util.List;

public interface PrizeDrawService {

    Prize drawPrize(String username);

    List<Prize> getAllPrizes();

    List<DrawRecord> getUserDrawHistory();


}
