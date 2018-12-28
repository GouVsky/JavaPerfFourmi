package org.polytechtours.performance.tp.fourmispeintre.utils;

import sun.java2d.loops.CompositeType;

import java.awt.*;

public class ColorUtils
{
    public static float seuilLuminance;

    public static int getColor(int red, int green, int blue)
    {
        return ((255 & 0xFF) << 24) |
               ((red & 0xFF) << 16) |
               ((green & 0xFF) << 8)  |
               ((blue & 0xFF) << 0);
    }

    public static boolean testColor(int color1, int color2)
    {
        float luminance1 = 0.2426f * (color1 >> 16 & 255)
                            + 0.7152f * (color1 >> 8 & 255)
                            + 0.0722f * (color1 >> 0 & 255);

        float luminance2 = 0.2426f * (color2 >> 16 & 255)
                            + 0.7152f * (color2 >> 8 & 255)
                            + 0.0722f * (color2 >> 0 & 255);

        if (Math.abs(luminance1 - luminance2) < seuilLuminance)
            return true;

        return false;
    }
}
