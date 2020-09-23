package com.brins.baselib.utils.eventbus;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;


public class EventBusManager {
    public static void post(@NonNull String key) {
        EventBus.getDefault().post(new EventBusParams(key, null));
    }

    public static void post(@NonNull String key, @NonNull Object value) {
        EventBus.getDefault().post(new EventBusParams(key, value));
    }

    public static void post(@NonNull String key, @NonNull Object value, @NonNull String extra) {
        EventBus.getDefault().post(new EventBusParams(key, value, extra));
    }

    public static void postSticky(@NonNull String key, @NonNull Object value) {
        EventBus.getDefault().postSticky(new EventBusParams(key, value));
    }

    public static void postSticky(@NonNull String key) {
        EventBus.getDefault().postSticky(new EventBusParams(key, null));
    }
}
