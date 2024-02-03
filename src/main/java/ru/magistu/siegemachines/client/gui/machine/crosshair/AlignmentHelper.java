package ru.magistu.siegemachines.client.gui.machine.crosshair;

import java.util.Arrays;
import java.util.List;

public class AlignmentHelper
{
    public static final List<String> validAlignmentValues = Arrays.asList(new String[] { "top_left", "top_center", "top_right", "center_left", "center", "center_right", "bottom_left", "bottom_center", "bottom_right" });

    public enum Alignment
    {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        CENTER_LEFT,
        CENTER,
        CENTER_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT;


        public static Alignment fromString(String align) {
            int idx = AlignmentHelper.validAlignmentValues.indexOf(align);
            if (idx != -1) {
                return values()[idx];
            }
            return CENTER;
        }
    }
}