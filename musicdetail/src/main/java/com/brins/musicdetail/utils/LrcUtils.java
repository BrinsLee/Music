package com.brins.musicdetail.utils;

import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.brins.musicdetail.model.LrcEntry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcUtils {

    private static final Pattern PATTERN_LINE = Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d{2,3}\\])+)(.+)");
    private static final Pattern PATTERN_TIME = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d{2,3})\\]");


    /**
     * 解析双语歌词
     *
     * @param lrcRaw
     * @param secondLrc
     * @return
     */
    public static List<LrcEntry> parseLrc(String lrcRaw, String secondLrc) {
        if (!TextUtils.isEmpty(lrcRaw) && !TextUtils.isEmpty(secondLrc)) {
            List<LrcEntry> mainLrcList = parseLrc(lrcRaw);
            List<LrcEntry> secondLrcList = parseLrc(secondLrc);

            if (mainLrcList != null && secondLrcList != null) {
                for (LrcEntry mainEntry : mainLrcList) {
                    for (LrcEntry secondEntry : secondLrcList) {
                        if (mainEntry.getTime() == secondEntry.getTime()) {
                            mainEntry.setSecondText(secondEntry.getText());
                        }

                    }
                }
            }
            return mainLrcList;
        }
        return null;
    }

    public static List<LrcEntry> parseLrc(String lrcRaw) {
        List<LrcEntry> entries = new ArrayList<>();
        String[] array = lrcRaw.split("\n");
        for (int i = 0; i < array.length; i++) {
            List<LrcEntry> list = parseLine(array[i]);
            if (list != null && !list.isEmpty()) {
                entries.addAll(list);
            }
        }
        return entries;

    }

    private static List<LrcEntry> parseLine(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        s = s.trim();
        Matcher matcher = PATTERN_LINE.matcher(s);
        if (!matcher.matches()) {
            return null;
        }
        String times = matcher.group(1);
        String text = matcher.group(3);
        List<LrcEntry> entryList = new ArrayList<>();

        Matcher timeMatcher = PATTERN_TIME.matcher(times);
        while (timeMatcher.find()) {
            long min = Long.parseLong(timeMatcher.group(1));
            long sec = Long.parseLong(timeMatcher.group(2));
            String milString = timeMatcher.group(3);
            long mil = Long.parseLong(milString);
            if (milString.length() == 2) {
                mil = mil * 10;
            }
            long time = min * DateUtils.MINUTE_IN_MILLIS + sec * DateUtils.SECOND_IN_MILLIS + mil;
            entryList.add(new LrcEntry(time, text == null ? "" : text));

        }
        return entryList;

    }

    /**
     * 转为[分:秒]
     */
    public static String formatTime(long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return mm + ":" + ss;
    }

    public static void resetDurationScale() {
        try {
            Field mField = ValueAnimator.class.getDeclaredField("sDurationScale");
            mField.setAccessible(true);
            mField.setFloat(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
