package com.imkarl.call.audio.core;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志打印
 * @version imkarl 2016-08
 * @see {@link 'https://github.com/zserge/log'}
 */
public final class L {
    private L() {}

    public interface Printer {
        void print(int level, String tag, String msg);
    }

    private static class AndroidPrinter implements Printer {
        public void print(int level, String tag, String msg) {
            Log.println(level, tag, msg);
        }
    }


    public final static int VERBOSE = Log.VERBOSE;
    public final static int DEBUG = Log.DEBUG;
    public final static int INFO = Log.INFO;
    public final static int WARN = Log.WARN;
    public final static int ERROR = Log.ERROR;

    private final static int MAX_LOG_LINE_LENGTH = 3000;

    public final static AndroidPrinter ANDROID = new AndroidPrinter();

    private static Printer mPrinter = L.ANDROID;
    private static String mTag = "AudioCall";
    private static int mMinLevel = L.VERBOSE;
    public static boolean isDebug = true;

    public static synchronized L usePrinter(Printer printer) {
        mPrinter = printer;
        return null;
    }
    public static synchronized L useTag(String tag) {
        mTag = tag;
        return null;
    }
    public static synchronized L minLevel(int minLevel) {
        mMinLevel = minLevel;
        return null;
    }


    public static void v(Object msg) {
        log(VERBOSE, msg);
    }
    public static void d(Object msg) {
        log(DEBUG, msg);
    }
    public static void i(Object msg) {
        log(INFO, msg);
    }
    public static void w(Object msg) {
        log(WARN, msg);
    }
    public static void e(Object msg) {
        log(ERROR, msg);
    }
    public static void e(Object... msg) {
        for (Object item : msg) {
            log(ERROR, item);
        }
    }


    private static void log(int level, Object msg) {
        if (level < mMinLevel) {
            return;
        }
        if (mPrinter == null) {
            return;
        }

        StackTraceElement element = new Throwable().getStackTrace()[2];
        print(level, element, mTag, toString(msg));
    }
    static void print(int level, StackTraceElement element, String tag, Object msg) {
        String className = element.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        String codeLine = className+'.'+element.getMethodName()+'('+element.getFileName()+':'+element.getLineNumber()+')';
        mPrinter.print(level, tag, codeLine);

        String message = L.toString(msg);
        for (String line : message.split("\n")) {
            do {
                int splitPos = Math.min(MAX_LOG_LINE_LENGTH, line.length());
                String part = line.substring(0, splitPos);
                line = line.substring(splitPos);

                if (filter(part)) {
                    mPrinter.print(level, tag, "\t" + part);
                }
            } while (line.length() > 0);
        }
    }
    private static boolean filter(String part) {
        return !contains(part,
                "rx.internal",
                "rx.Subscriber",
                "java.util.concurrent",
                "retrofit2.OkHttpCall",
                "retrofit2.ServiceMethod",
                "okio.RealBufferedSource",
                "okhttp3.internal",
                "okhttp3.RealCall",
                "retrofit2.CustomCallAdapterFactory$",
                "converter.CustomGsonConverterFactory$",
                "com.facebook.stetho.okhttp",
                "com.android.internal.os.ZygoteInit",
                "android.app.ActivityThread.acces",
                "InetAddress.getAllByNameImpl",
                "Posix.android_getaddrinfo",
                "okio.AsyncTimeout",
                "view.Choreographer",
                "view.ViewRootImpl",
                "view.ThreadedRenderer",
                "internal.policy.PhoneWindow",
                "View.updateDisplayListIfDirty");
    }
    private static boolean contains(String text, String... strs) {
        for (String str : strs) {
            if (text.contains(str)) {
                return true;
            }
        }
        return false;
    }
    private static String toString(Object msg) {
        String message;

        if (msg == null) {
            message = "[null]";
        } else if (msg instanceof Enum) {
            Enum enumObj = (Enum) msg;
            message = enumObj.getClass().getSimpleName()+"."+enumObj.name();
        } else if (msg instanceof Throwable) {
            Throwable tr = (Throwable) msg;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            pw.flush();
            message = sw.toString();
        } else {
            message = String.valueOf(msg);
        }

        if (TextUtils.isEmpty(message)) {
            message = "[ ]";
        }

        return message;
    }

}
