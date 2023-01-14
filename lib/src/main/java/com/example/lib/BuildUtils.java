package com.example.lib;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author AlexisYin
 */
public class BuildUtils {

    private static BuildUtils singleton;

    public static BuildUtils get() {
        if (singleton == null) {
            synchronized (BuildUtils.class) {
                if (singleton == null) {
                    singleton = new BuildUtils();
                }
            }
        }
        return singleton;
    }

    public static String getModel(){
        return BuildUtils.get().model();
    }

    private String model;
    public String model() {
        if (TextUtils.isEmpty(model)) {
            model = Build.MODEL;
        }
        Log.d("TAG", "ASM --> model: " + model);
        return model;
    }
}
