package com.mytel.digital_lucky_draw.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Create by
 *
 * @author : Nguyen Ba Hung
 * @since : 1/5/2021, Tue
 **/
public class TimeUtils {

    private static final DateTimeFormatter RESPONSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static Logger log = LoggerFactory.getLogger(TimeUtils.class);

    /**
     * Convert a formatted date string from frontend to LocalDateTime
     */
    public static LocalDateTime inputFormatter(String dateStr) {
        return LocalDateTime.parse(dateStr, INPUT_FORMATTER);
    }

    /**
     * Format LocalDateTime to the required format "hh:mm a, dd MMM yyyy"
     */
    public static String responseFormatter(LocalDateTime dateTime) {
        return dateTime.format(RESPONSE_FORMATTER);
    }

    /**
     * Convert Date object to LocalDateTime
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Timestamp getCurrTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static LocalDate getDateNow() {
        return LocalDate.now();
    }
    public static String getDateStr(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static java.sql.Date getNextDate() {
        return java.sql.Date.valueOf(TimeUtils.getDateNow().plusDays(1L));
    }

    public static java.sql.Date getDateNowByDate() {
        return java.sql.Date.valueOf(LocalDate.now());
    }

    public static boolean isEqualDate(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }
    public static boolean isEqualDateByTwoTimestamp(Timestamp timestamp1, Timestamp timestamp2) {
        long longTimestamp1 = timestamp1.getTime();
        long longTimestamp2 = timestamp2.getTime();

        String date1 = new SimpleDateFormat("yyy-MM-dd").format(new Date(longTimestamp1));
        String date2 = new SimpleDateFormat("yyy-MM-dd").format(new Date(longTimestamp2));

        return date1.equals(date2);
    }
    public static boolean timestamp1GreaterThanTimestamp2ByDate(Timestamp timestamp1, Timestamp timestamp2) {
        long longTimestamp1 = timestamp1.getTime();
        long longTimestamp2 = timestamp2.getTime();

        String date1Str = new SimpleDateFormat("yyy-MM-dd").format(new Date(longTimestamp1));
        String date2Str = new SimpleDateFormat("yyy-MM-dd").format(new Date(longTimestamp2));

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = new SimpleDateFormat("yyy-MM-dd").parse(date1Str);
            date2 = new SimpleDateFormat("yyy-MM-dd").parse(date2Str);

        } catch (ParseException e) {
            log.error("Error convert string to date: {}", e.getMessage());
        }
        if(date1 == null || date2 == null){
            return false;
        }
        return date1.after(date2);
    }

    public static Timestamp randTimestamp(LocalDateTime timestamp){
        Random random = new Random();
        int randomSeconds = random.nextInt(85400);
        LocalDateTime randomTime = timestamp.plusSeconds(randomSeconds);
        return Timestamp.valueOf(randomTime);
    }

    public static int getWeekNowOfYear() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.get(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear());
    }

    public static int getMonthNowOfYear() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.getMonthValue();
    }

    public static int getNextWeekOfYear() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.get(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear()) +1;
    }

    public static int getNextMonthOfYear() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.getMonthValue() + 1;
    }

    public static String timeMonthCode() {
        LocalDateTime currentTime = LocalDateTime.now();
        int monthOfYear = currentTime.getMonthValue();
        int year = currentTime.getYear();
        return String.format("%02d_%04d", monthOfYear, year);
    }
    public static String timeWeekCode() {
        LocalDateTime currentTime = LocalDateTime.now();
        int weekOfYear = currentTime.get(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear());
        int year = currentTime.get(WeekFields.of(Locale.GERMANY).weekBasedYear());
        return String.format("%02d_%04d", weekOfYear, year);
    }

    public static String timeLastWeekCode() {
        LocalDateTime currentTime = LocalDateTime.now();
        int weekOfYear = currentTime.get(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear());
        int year = currentTime.get(WeekFields.of(Locale.GERMANY).weekBasedYear());
        int lastWeekOfYear;
        int lastYear;
        if (weekOfYear == 1) {
            lastWeekOfYear = 52;
            lastYear = year - 1;
        } else {
            lastWeekOfYear = weekOfYear - 1;
            lastYear = year;
        }
        return String.format("%02d_%04d", lastWeekOfYear, lastYear);
    }

    public static Date getDateByHour(Date date, int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Timestamp getTimestampByDateAndHours(Date date, int hours){
        return new Timestamp(getDateByHour(date, hours).getTime());
    }

    public static String getStrDateNow() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    public static String getTimeAgoByTimestamp(Timestamp timestamp){
        long ms = timestamp.getTime();
        long currMs = System.currentTimeMillis();
        long mins = (currMs - ms)/60000;
        if(mins <1){
            return "NOW";
        } else if (mins <60){
            return "MIN_" + mins;
        } else if(mins < 60*24){
            return "HOUR_" + mins/60;
        } else {
            return new SimpleDateFormat("EEEE, dd/MM").format(new Date(timestamp.getTime()));
        }
    }
}