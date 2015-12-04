package org.benzeneframework.utils;

/**
 * Created by jeongukjae on 15. 11. 3..
 *
 * This Class helps logging.
 * List : Debug, Info, Release, Error
 */
public class Log {
    /**
     * This Logging method is for Debug
     *
     * @param tag Message's tag
     * @param msg Message to log
     */
    @SuppressWarnings("unused")
    public static void d(String tag, String msg) {
        logging("Debug/[" + tag + "]", msg);
    }

    /**
     * This Logging method is for Information
     *
     * @param tag Message's tag
     * @param msg Message to log
     */
    @SuppressWarnings("unused")
    public static void i(String tag, String msg) {
        logging("Info/[" + tag + "]", msg);
    }

    /**
     * This Logging method is for Release
     *
     * @param tag Message's tag
     * @param msg Message to log
     */
    @SuppressWarnings("unused")
    public static void r(String tag, String msg) {
        logging("Release/[" + tag + "]", msg);
    }

    /**
     * This Logging method is for Error
     *
     * @param tag Message's tag
     * @param msg Message to log
     */
    @SuppressWarnings("unused")
    public static void e(String tag, String msg) {
        logging("Error/[" + tag + "]", msg);
    }

    /**
     * This Logging method is private logging
     * Upper logging methods call this method to log
     *
     * @param tag Message's tag
     * @param msg Message to log
     */
    private static void logging(String tag, String msg) {
        System.out.println(tag + " : " + msg);
    }
}
