package com.brins.baselib.utils.eventbus;


public class EventBusParams {
    public String key;
    public Object object;
    public String extra;

    public EventBusParams(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    public EventBusParams(String key, Object object, String extra) {
        this.key = key;
        this.object = object;
        this.extra = extra;
    }


    @Override
    public String toString() {
        return "EventBusParams{" +
                "key='" + key + '\'' +
                ", object=" + object +
                ", extra='" + extra + '\'' +
                '}';
    }
}
