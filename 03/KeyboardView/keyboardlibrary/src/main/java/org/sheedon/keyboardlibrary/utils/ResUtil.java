package org.sheedon.keyboardlibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;


/**
 * 获取应用资源的封装
 *
 * @author Phoenix
 * @date 2016-10-17 16:16
 */
public class ResUtil {

    private ResUtil() {
    }

    /**
     * 获取Resource对象
     *
     * @return Returns a Resources instance for your application's Package.
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    /**
     * 获取Drawable资源
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    /**
     * 获取字符串资源
     */
    public static String getString(Context context, int resId) {
        return getResources(context).getString(resId);
    }

    /**
     * 获取color资源
     */
    public static int getColor(Context context, int resId) {
        return ContextCompat.getColor(context, resId);
    }

    /**
     * 获取dimens资源
     *
     * @param resId
     * @return
     */
    public static float getDimens(Context context, int resId) {
        return getResources(context).getDimension(resId);
    }

    /**
     * 获取字符串数组资源
     *
     * @param resId
     * @return
     */
    public static String[] getStringArray(Context context, int resId) {
        return getResources(context).getStringArray(resId);
    }

    /**
     * 根据颜色值计算颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的颜色
     */
    public static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}
