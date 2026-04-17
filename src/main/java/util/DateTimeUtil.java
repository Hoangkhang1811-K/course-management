package util;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String FORM_PATTERN = "yyyy-MM-dd'T'HH:mm";

    private DateTimeUtil() {
        // Prevent instantiation
    }

    // Lấy thời gian hiện tại
    public static Timestamp getCurrentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    // Convert String -> Timestamp theo pattern mặc định: yyyy-MM-dd HH:mm:ss
    public static Timestamp parseToTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(
                    dateTimeStr.trim(),
                    DateTimeFormatter.ofPattern(DEFAULT_PATTERN)
            );
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Convert String từ input datetime-local của HTML --> Timestamp
    public static Timestamp parseFormDateTimeToTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(
                    dateTimeStr.trim(),
                    DateTimeFormatter.ofPattern(FORM_PATTERN)
            );
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Format Timestamp -> String theo pattern mặc định
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }

    // Format Timestamp -> String theo pattern truyền vào
    public static String formatTimestamp(Timestamp timestamp, String pattern) {
        if (timestamp == null || pattern == null || pattern.trim().isEmpty()) {
            return "";
        }
        try {
            return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern));
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    // Check chuỗi datetime theo pattern mặc định có hợp lệ không
    public static boolean isValidDateTime(String dateTimeStr) {
        return parseToTimestamp(dateTimeStr) != null;
    }

    // Check chuỗi datetime-local từ form có hợp lệ không
    public static boolean isValidFormDateTime(String dateTimeStr) {
        return parseFormDateTimeToTimestamp(dateTimeStr) != null;
    }
}
