package com.arthur.sams.common;

/**
 * 通用小工具
 */
public final class Utils {

    private Utils() {
    }

    /** 四舍五入保留 1 位小数，例如 86.53 -> 86.5 */
    public static double round1(Double value) {
        if (value == null) {
            return 0.0;
        }
        return Math.round(value * 10.0) / 10.0;
    }

    /** null / 空白字符串统一返回 null，方便判空 */
    public static String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
