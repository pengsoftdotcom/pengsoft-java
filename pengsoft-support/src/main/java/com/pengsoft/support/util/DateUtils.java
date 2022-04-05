package com.pengsoft.support.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * The date utility methods.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * default zone id
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("GMT+8");

    /**
     * default zone offset
     */
    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

    /**
     * default time pattern
     */
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * default date pattern
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * default datetime pattern
     */
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * date formatter
     */
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_PATTERN);

    /**
     * time formatter
     */
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_PATTERN);

    /**
     * datetime formatter
     */
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern(DateUtils.DEFAULT_DATETIME_PATTERN);

    /**
     * Returns current {@link LocalDateTime} of default {@link ZoneId}
     *
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now(DateUtils.DEFAULT_ZONE_ID);
    }

    /**
     * Returns current {@link LocalDateTime} of specified {@link ZoneId}
     *
     * @param zoneId {@link ZoneId}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime currentDateTime(final ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }

    /**
     * Returns current {@link LocalDate} of {@link ZoneId}
     *
     * @return {@link LocalDate}
     */
    public static LocalDate currentDate() {
        return DateUtils.currentDate(DateUtils.DEFAULT_ZONE_ID);
    }

    /**
     * Returns current {@link LocalDate} of specified {@link ZoneId}
     *
     * @param zoneId {@link ZoneId}
     * @return {@link LocalDate}
     */
    public static LocalDate currentDate(final ZoneId zoneId) {
        return LocalDate.now(zoneId);
    }

    /**
     * format {@link LocalDate} with time formatter
     *
     * @param localDate {@link LocalDate}
     * @return formatted string
     */
    public static String formatDate(final LocalDate localDate) {
        return DateUtils.dateFormatter.format(localDate);
    }

    /**
     * format {@link LocalDateTime} with time formatter
     *
     * @param localDateTime {@link LocalDateTime}
     * @return formatted string
     */
    public static String formatDate(final LocalDateTime localDateTime) {
        return DateUtils.dateFormatter.format(localDateTime);
    }

    /**
     * format {@link LocalTime} with time formatter
     *
     * @param localTime {@link LocalTime}
     * @return formatted string
     */
    public static String formatTime(final LocalTime localTime) {
        return DateUtils.timeFormatter.format(localTime);
    }

    /**
     * format {@link LocalDateTime} with time formatter
     *
     * @param localDateTime {@link LocalDateTime}
     * @return formatted string
     */
    public static String formatTime(final LocalDateTime localDateTime) {
        return DateUtils.timeFormatter.format(localDateTime);
    }

    /**
     * format {@link LocalDateTime} with datetime formatter
     *
     * @param localDateTime {@link LocalDateTime}
     * @return formatted string
     */
    public static String formatDateTime(final LocalDateTime localDateTime) {
        return DateUtils.dateTimeFormatter.format(localDateTime);
    }

    /**
     * parse a string to with date formatter
     *
     * @param datetime date string
     * @return parsed {@link LocalDate}
     */
    public static LocalDate parseDate(final String datetime) {
        return LocalDate.from(dateFormatter.parse(datetime));
    }

    /**
     * parse a string to with time formatter
     *
     * @param datetime date string
     * @return parsed {@link LocalTime}
     */
    public static LocalTime parseTime(final String datetime) {
        return LocalTime.from(timeFormatter.parse(datetime));
    }

    /**
     * parse a string to with datetime formatter
     *
     * @param datetime date string
     * @return parsed {@link LocalDateTime}
     */
    public static LocalDateTime parseDateTime(final String datetime) {
        return LocalDateTime.from(dateTimeFormatter.parse(datetime));
    }

    /**
     * Returns the start of the current year.
     */
    public static LocalDateTime atStartOfCurrentYear() {
        return LocalDateTime.of(currentDate().getYear(), 1, 1, 0, 0, 0);
    }

    /**
     * Returns the end of the current year.
     */
    public static LocalDateTime atEndOfCurrentYear() {
        return LocalDateTime.of(currentDate().getYear(), 12, 31, 23, 59, 59);
    }

    /**
     * Returns the start of the current month.
     */
    public static LocalDateTime atStartOfCurrentMonth() {
        final var today = currentDate();
        return LocalDateTime.of(today.getYear(), today.getMonthValue(), 1, 0, 0, 0);
    }

    /**
     * Returns the end of the current month.
     */
    public static LocalDateTime atEndOfCurrentMonth() {
        final var today = currentDate();
        return LocalDateTime.of(today.getYear(), today.getMonthValue(), today.lengthOfMonth(), 23, 59, 59);
    }

    /**
     * Returns the start of today.
     */
    public static LocalDateTime atStartOfToday() {
        return LocalDateTime.of(currentDate(), LocalTime.MIN);
    }

    /**
     * Returns the end of today.
     */
    public static LocalDateTime atEndOfToday() {
        return LocalDateTime.of(currentDate(), LocalTime.MAX);
    }

    /**
     * Returns the {@link Date} of specified {@link LocalDateTime}
     *
     * @return {@link Date}
     */
    public static Date convertToDate(final LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(DateUtils.DEFAULT_ZONE_OFFSET));
    }

}