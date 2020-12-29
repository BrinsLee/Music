package com.brins.baselib.route;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * created by sunyan on 2019-11-01.
 * ARouter工具类
 */
public class ARouterUtils {

    /**
     * navigation
     *
     * @param path
     */
    public static Object navigation(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        return build(path).navigation();
    }

    /**
     * Activity不带参跳转
     *
     * @param path 目标Activity的路由路径(所有路径配置在RouterHub中)
     */
    public static void go(String path) {
        if (TextUtils.isEmpty(path))
            return;
        build(path).navigation();
    }

    /**
     * Activity带参跳转
     *
     * @param path   目标Activity的路由路径(所有路径配置在RouterHub中)
     * @param bundle
     */
    public static void go(String path, Bundle bundle) {
        build(path).with(bundle).navigation();
    }

    /**
     * 带共享元素的跳转
     *
     * @param path
     * @param bundle
     * @param optionsCompat
     */
    public static void go(String path, Bundle bundle, ActivityOptionsCompat optionsCompat) {
        build(path).with(bundle).withOptionsCompat(optionsCompat).navigation();
    }

    /**
     * 路由获取Fragment
     *
     * @param path
     * @param <F>
     * @return
     */
    public static <F extends Fragment> F getFragment(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        F o = (F) build(path).navigation();
        return o;
    }

    /**
     * 获取Postcard,用于配置参数
     *
     * @param path 目标组件路径(所有路径配置在RouterHub中)
     * @return
     */
    public static Postcard build(String path) {
        return ARouter.getInstance().build(path);
    }

    /**
     * 获取各个module的RouteService
     *
     * @param path 目标组件路径(所有路径配置在RouterHub中)
     * @param <T>
     * @return
     */
    public static <T extends RouteService> T getRouteService(String path) {
        if (TextUtils.isEmpty(path))
            return null;
        RouteService routeService = (RouteService) ARouter.getInstance().build(path).navigation();
        return (T) routeService;
    }
}
