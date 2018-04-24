package com.jude.rollviewpagerdome.utils;

/**
 * Created by garry on 17/11/10.
 */

public class UnicodeUtil {
    public static String readUnicodeStr2(String unicodeStr) {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < unicodeStr.length(); i++) {
            char char1 = unicodeStr.charAt(i);
            if (char1 == '\\' && isUnicode(unicodeStr, i)) {
                String cStr = unicodeStr.substring(i + 2, i + 6);
                int cInt = Integer.parseInt(cStr,16);
                buf.append((char) cInt);
                // 跨过当前unicode码，因为还有i++，所以这里i加5，而不是6
                i = i + 5;
            } else {
                buf.append(char1);
            }
        }
        return buf.toString();
    }

    // 判断以index从i开始的串，是不是unicode码
    private static boolean isUnicode(String unicodeStr, int i) {
        int len = unicodeStr.length();
        int remain = len - i;
        // unicode码，反斜杠后还有5个字符 uxxxx
        if (remain < 5)
            return false;

        char flag2 = unicodeStr.charAt(i + 1);
        if (flag2 != 'u')
            return false;
        String nextFour = unicodeStr.substring(i + 2, i + 6);
        return isHexStr(nextFour);
    }

    /**
     * hex str 1-9 a-f A-F
     */
    private static boolean isHexStr(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            boolean isHex = (ch >= '1' && ch <= '9') || (ch >= 'a' && ch <= 'f')
                    || (ch >= 'A' && ch <= 'F');
            if (!isHex)
                return false;
        }
        return true;
    }

}
