package com.sunshine.sunxin.util;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.graphics.Color;

/**
 * Created by 钟光燕 on 2016/8/24.
 * e-mail guangyanzhong@163.com
 */
public class ColorUtil {
    /**
     * Modulate the colorAlpha to new alpha
     *
     * @param colorAlpha Color's alpha
     * @param alpha      Modulate alpha
     * @return Modulate alpha
     */
    public static int modulateAlpha(int colorAlpha, int alpha) {
        int scale = alpha + (alpha >>> 7);  // convert to 0..256
        return colorAlpha * scale >>> 8;
    }

    /**
     * Modulate the color to new alpha
     *
     * @param color Color
     * @param alpha Modulate alpha
     * @return Modulate alpha color
     */
    public static int modulateColorAlpha(int color, int alpha) {
        int colorAlpha = color >>> 24;
        int scale = alpha + (alpha >> 7);
        int newAlpha = colorAlpha * scale >> 8;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return newAlpha << 24 | r << 16 | g << 8 | b;
    }


    /**
     * Change the color to new alpha
     *
     * @param color Color
     * @param alpha New alpha
     * @return New alpha color
     */
    public static int changeColorAlpha(int color, int alpha) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return alpha << 24 | r << 16 | g << 8 | b;
    }

    public static int blendColors(int color1, int color2, float percent) {
        float inverseRation = 1 - percent;
        float r = Color.red(color1) * percent + Color.red(color2) * inverseRation;
        float g = Color.green(color1) * percent + Color.green(color2)
                * inverseRation;
        float b = Color.blue(color1) * percent + Color.blue(color2)
                * inverseRation;
        return Color.rgb((int) r, (int) g, (int) b);
    }
}
