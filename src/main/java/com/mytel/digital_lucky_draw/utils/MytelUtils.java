package com.mytel.digital_lucky_draw.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
public class MytelUtils {

    public static String toIsdn(String number) {
        String isdn = "";
        if (number == null) {
            return isdn;
        } else {
            isdn = number;
        }
        if (number.startsWith("09")) {
            isdn = number.substring(1);
        } else if (number.startsWith("+959")) {
            isdn = number.substring(3);
        } else if (number.startsWith("959")) {
            isdn = number.substring(2);
        }
        return isdn;
    }


    public static String toMsisdn(String number) {
        return "95" + toIsdn(number);
    }


    public static boolean isMytelNumber(String number) {
        String isdn = toIsdn(number);
        if (isdn.startsWith("965") ||
                isdn.startsWith("966") ||
                isdn.startsWith("967") ||
                isdn.startsWith("968") ||
                isdn.startsWith("969")
        ) {
            return isdn.length() ==10;
        } else return false;
    }

    public static boolean isValidPhoneNumber(String number) {
        if (number == null) return false;

        return number.startsWith("9") &&
                (number.length() == 8 || number.length() == 10);
    }



    public static String toJson(Object object) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json;
        try {
            json = ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert java object to json string", e);
            json = object.toString();
        }
        return json;
    }


    public static String toString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert java object to json string", e);
            return null;
        }
    }


    public static void logProcessTime(String function, LocalDateTime begin, LocalDateTime end) {
        log.info("[{} ms] {}", ChronoUnit.MILLIS.between(begin, end), function);

    }


    public static long getDurationBetween(LocalDateTime from, LocalDateTime to) {
        return ChronoUnit.MILLIS.between(from, to);
    }


    public static String toChar(LocalDateTime time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(dateTimeFormatter);
    }
}
