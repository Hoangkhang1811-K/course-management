package util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ValidateUtil {
    public static final int ACTIVE_STATUS = 1;
    public static final int INACTIVE_STATUS = 0;
    public static final int MIN_PASSWORD_LENGTH = 6;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{10}$"
    );

    private ValidateUtil() {
    }

    public static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isBlank(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return isBlank(phone) || PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidLength(String value, int maxLength) {
        return value == null || value.length() <= maxLength;
    }

    public static Integer parseInteger(String value) {
        if (isBlank(value)) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int parseIntegerOrDefault(String value, int defaultValue) {
        Integer parsedValue = parseInteger(value);
        return parsedValue == null ? defaultValue : parsedValue;
    }

    public static BigDecimal parseBigDecimal(String value) {
        if (isBlank(value)) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int parseStatus(String value) {
        Integer status = parseInteger(value);
        if (status == null) {
            return ACTIVE_STATUS;
        }

        return status == INACTIVE_STATUS ? INACTIVE_STATUS : ACTIVE_STATUS;
    }

    public static String normalizePhone(String phone) {
        String normalizedPhone = trimToEmpty(phone);
        return normalizedPhone.isEmpty() ? null : normalizedPhone;
    }

    public static String validateRegisterForm(
            String fullName,
            String email,
            String phone,
            String password,
            String confirmPassword
    ) {
        if (isBlank(fullName)) {
            return "Họ và tên không được để trống.";
        }

        if (!isValidLength(fullName, 100)) {
            return "Họ và tên không quá 100 ký tự .";
        }

        if (isBlank(email)) {
            return "Email không được để trống.";
        }

        if (!isValidEmail(email)) {
            return "Email không đúng định dạng.";
        }

        if (!isValidLength(email, 100)) {
            return "Email không được vuợt quá 100 ký tự.";
        }

        if (!isValidPhone(phone)) {
            return "số điện thoại không hợp lệ. Vui lòng nhập đúng 10 chữ số từ 0 đến 9.";
        }

        if (isBlank(password)) {
            return "Mật khẩu không được để trống .";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Mật khẩu có ít nhất 6 ký tự.";
        }

        if (password.length() > 100) {
            return "Mật khẩu không được quá  100 ký tự.";
        }

        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp.";
        }

        return null;
    }
}
