package com.brins.baselib.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Gson工具类
 *
 * @author MUTOU
 * @date 2017/9/12 20:14
 */
public class GsonUtils {

    private static final String TAG = "GsonUtils";

    public static <T> T fromJson(String json, Type type) {
        Gson gson = new Gson();
        Object obj = null;
        try {
            obj = gson.fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        return (T) obj;
    }

    public static <T> T fromJson(JsonElement json, Type type) {
        Gson gson = new Gson();
        Object obj = null;
        try {
            obj = gson.fromJson(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        return (T) obj;
    }

    public static <T> String listToJson(ArrayList<T> list) {
        Gson gson = new Gson();
        String result = "";
        try {
            result = gson.toJson(list);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }

        return result;
    }

    public static <T> ArrayList<T> listFromJson(String value) {
        Gson gson = new Gson();
        ArrayList<T> result = null;
        try {
            result = gson.fromJson(value, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);

        }
        return result;
    }

    /**
     * 保存成json字符串
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJson(T t) {
        try {
            return new Gson().toJson(t);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        return null;
    }

    /**
     * 转换为JsonElement
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JsonElement toJsonTree(T t) {

        return new Gson().toJsonTree(t);
    }

}
