package org.polytechtours.performance.tp.fourmispeintre.utils;

import sun.java2d.loops.CompositeType;

import java.awt.*;

public class ColorUtils
{
    public static int setColor(int red, int green, int blue, int alpha)
    {
        return ((alpha & 0xFF) << 24) |
                ((red & 0xFF) << 16)  |
                ((green & 0xFF) << 8) |
                ((blue & 0xFF) << 0);
    }
}
