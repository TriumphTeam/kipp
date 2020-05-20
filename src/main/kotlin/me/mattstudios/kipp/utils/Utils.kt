package me.mattstudios.kipp.utils

import java.awt.Color




object Utils {

    /**
     * Gets the color from HEX code
     */
    fun hexToRgb(colorStr: String): Color {
        return Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16))
    }

}