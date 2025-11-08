package model;

import android.app.Application;

public class Global extends Application {
    private static String uid;

    public static String getUid() {
        return uid;
    }

    public static void setUid(String id) {
        uid = id;
    }
}
