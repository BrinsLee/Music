package com.brins.baselib.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;

public class AppUtils {

    private static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
    }

    public static Application getContext() {
        return mApplication;
    }

    public static boolean isAppDebug() {
        String packageName = getContext().getPackageName();
        if (UIUtils.isSpace(packageName)) return false;
        ApplicationInfo ai = getContext().getApplicationInfo();
        return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

}
