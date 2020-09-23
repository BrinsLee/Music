package com.brins.baselib.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.brins.baselib.R;


public class UIUtils {
    private static Application application;
    private static Context context;

    public static int screenWidth, screenHeight;
    // 当前屏幕的densityDpi
    public static float densityDpi = 0.0f;
    // 密度因子
    public static float scale = 0.0f;

    /**
     * 初始化工具类
     *
     * @param application 上下文
     */
    public static void init(Application application) {
        UIUtils.application = application;
        UIUtils.context = application.getApplicationContext();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (displayMetrics == null)
            return;

        densityDpi = displayMetrics.densityDpi;
        // 密度因子
        scale = densityDpi / 160;

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    public static Application getApplication() {
        if (application != null)
            return application;
        throw new NullPointerException("u should init first");
    }

    public static Context getContext() {
        if (context != null)
            return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static float getDimenSize(int resId){
        return getResources().getDimension(resId);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取资源
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取资源文件图片， 涉及尺寸请传入context
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    /**
     * 获取资源文件图片
     *
     * @param id
     * @param context 涉及尺寸需传入Activity  application 暂不兼容AdaptScreenUtils
     * @return
     */
    public static Drawable getDrawable(Context context, int id) {
        return ContextCompat.getDrawable(context, id);
    }

    public static int pt2Px(float value) {
        return AdaptScreenUtils.pt2Px(value);
    }

    /**
     * 密度转换像素
     */
    public static int dip2px(float dipValue) {
        int px = (int) (dipValue * scale + 0.5f);
        return px;
    }

    // 将px值转换为dip或dp值，保证尺寸大小不变
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取资源文件字符串
     *
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getResources().getString(id);
    }

    /**
     * 获取String format
     */
    public static String getFormatStringByResId(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 设置textview指定文字为某一颜色
     *
     * @param content 显示的文字
     * @param color   需要转换成的颜色值
     * @param start   需要变色文字开始位置
     * @param end     需要变色文字结束位置
     */
    public static SpannableStringBuilder changeTextColor(String content, int color, int start, int end) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 设置textview指定文字为加粗
     * @param content
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder changeTextStyle(String content, int start, int end){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * findViewById 精简版
     *
     * @param view  依附的布局
     * @param resId 控件id
     * @param <T>
     * @return
     */
    public static <T> T bind(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    public static void changeVisible(int visible, View... views) {
        if (views!=null) {
            for (View view : views) {
                if (view != null) {
                    if (visible != view.getVisibility()) {
                        view.setVisibility(visible);
                    }
                }
            }
        }
    }

    /**
     * 获取String format
     */
    public static String formatString(int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }


    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int parseColor(String colorStr) {
        try {
            if (colorStr.startsWith("#")) {
                return Color.parseColor(colorStr);
            } else {
                return Color.parseColor("#" + colorStr);
            }
        } catch (Exception e) {
            return getColor(R.color.white);
        }
    }

}
