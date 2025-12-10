package com.yupi.yupicture.infrastructure.utils;

public class ColorTransformUtils {

    private ColorTransformUtils() {
        // 工具类不需要实例化
    }

    public static String getStandardColor(String color) {
        if (color.length() == 7) {
            color = color.substring(0, 4) + "0" + color.substring(4, 7);
        }
        return color;
    }


}
