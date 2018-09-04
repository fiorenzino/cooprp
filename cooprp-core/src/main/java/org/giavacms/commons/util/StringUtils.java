package org.giavacms.commons.util;

import org.jboss.logging.Logger;

public class StringUtils {


    public static final String SPACE = " ";
    public static final String EMPTY = "";
    private static final int PAD_LIMIT = 8192;


    static Logger logger = Logger.getLogger(StringUtils.class);

    public static boolean isNullOrEmpty(String value) {
        if (value == null || value.trim().isEmpty())
            return true;
        return false;

    }

    public static void evaluateNullOrEmpty(String value, String name)
            throws Exception {
        if (value == null)
            throw new Exception(name + " is null");
        if (value.trim().isEmpty())
            throw new Exception(name + " is empty");
        logger.info(name + ": " + value);
    }

    public static String leftPad(final String str, final int size) {
        return leftPad(str, size, ' ');
    }

    /**
     * 6374     * <p>Left pad a String with a specified character.</p>
     * 6375     *
     * 6376     * <p>Pad to a size of {@code size}.</p>
     * 6377     *
     * 6378     * <pre>
     * 6379     * StringUtils.leftPad(null, *, *)     = null
     * 6380     * StringUtils.leftPad("", 3, 'z')     = "zzz"
     * 6381     * StringUtils.leftPad("bat", 3, 'z')  = "bat"
     * 6382     * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
     * 6383     * StringUtils.leftPad("bat", 1, 'z')  = "bat"
     * 6384     * StringUtils.leftPad("bat", -1, 'z') = "bat"
     * 6385     * </pre>
     * 6386     *
     * 6387     * @param str  the String to pad out, may be null
     * 6388     * @param size  the size to pad to
     * 6389     * @param padChar  the character to pad with
     * 6390     * @return left padded String or original String if no padding is necessary,
     * 6391     *  {@code null} if null String input
     * 6392     * @since 2.0
     * 6393
     */
    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(String.valueOf(padChar), pads).concat(str);
    }

    /**
     * 6409     * <p>Left pad a String with a specified String.</p>
     * 6410     *
     * 6411     * <p>Pad to a size of {@code size}.</p>
     * 6412     *
     * 6413     * <pre>
     * 6414     * StringUtils.leftPad(null, *, *)      = null
     * 6415     * StringUtils.leftPad("", 3, "z")      = "zzz"
     * 6416     * StringUtils.leftPad("bat", 3, "yz")  = "bat"
     * 6417     * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
     * 6418     * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
     * 6419     * StringUtils.leftPad("bat", 1, "yz")  = "bat"
     * 6420     * StringUtils.leftPad("bat", -1, "yz") = "bat"
     * 6421     * StringUtils.leftPad("bat", 5, null)  = "  bat"
     * 6422     * StringUtils.leftPad("bat", 5, "")    = "  bat"
     * 6423     * </pre>
     * 6424     *
     * 6425     * @param str  the String to pad out, may be null
     * 6426     * @param size  the size to pad to
     * 6427     * @param padStr  the String to pad with, null or empty treated as single space
     * 6428     * @return left padded String or original String if no padding is necessary,
     * 6429     *  {@code null} if null String input
     * 6430
     */
    public static String leftPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            final char[] padding = new char[pads];
            final char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }


    public static String repeat(final String str, final int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        final int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return repeat(String.valueOf(str.charAt(0)), repeat);
        }

        final int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                return repeat(String.valueOf(str.charAt(0)), repeat);
            case 2:
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                final StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }


    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
