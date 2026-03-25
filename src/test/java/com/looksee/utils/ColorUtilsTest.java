package com.looksee.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.looksee.models.ColorData;

/**
 * Unit tests for ColorUtils.
 */
class ColorUtilsTest {

    @Test
    void meetsTextContrastRequirementsHighContrast() {
        ColorData white = new ColorData(255, 255, 255);
        ColorData black = new ColorData(0, 0, 0);
        // High contrast between white and black should pass for normal text
        assertTrue(ColorUtils.meetsTextContrastRequirements(white, black, 16.0));
    }

    @Test
    void meetsTextContrastRequirementsLowContrast() {
        ColorData c1 = new ColorData(200, 200, 200);
        ColorData c2 = new ColorData(210, 210, 210);
        // Very similar colors should fail contrast check
        assertFalse(ColorUtils.meetsTextContrastRequirements(c1, c2, 16.0));
    }

    @Test
    void meetsNonTextContrastRequirementsHighContrast() {
        ColorData white = new ColorData(255, 255, 255);
        ColorData black = new ColorData(0, 0, 0);
        assertTrue(ColorUtils.meetsNonTextContrastRequirements(white, black));
    }

    @Test
    void meetsNonTextContrastRequirementsLowContrast() {
        ColorData c1 = new ColorData(200, 200, 200);
        ColorData c2 = new ColorData(210, 210, 210);
        assertFalse(ColorUtils.meetsNonTextContrastRequirements(c1, c2));
    }

    @Test
    void getCompliantTextColorForBlackBackground() {
        ColorData black = new ColorData(0, 0, 0);
        ColorData compliant = ColorUtils.getCompliantTextColor(black, 16.0);
        assertNotNull(compliant);
        // The compliant color on black bg should be light
        double contrast = ColorData.computeContrast(black, compliant);
        assertTrue(contrast >= 4.5, "Contrast should be at least 4.5:1 for text");
    }

    @Test
    void getCompliantTextColorForWhiteBackground() {
        ColorData white = new ColorData(255, 255, 255);
        ColorData compliant = ColorUtils.getCompliantTextColor(white, 16.0);
        assertNotNull(compliant);
    }

    @Test
    void getCompliantNonTextColorForBlackBg() {
        ColorData black = new ColorData(0, 0, 0);
        ColorData compliant = ColorUtils.getCompliantNonTextColor(black);
        assertNotNull(compliant);
        double contrast = ColorData.computeContrast(black, compliant);
        assertTrue(contrast >= 3.0, "Contrast should be at least 3:1 for non-text");
    }

    @Test
    void classifyColorRed() {
        String classification = ColorUtils.classifyColor(new ColorData(255, 0, 0));
        assertNotNull(classification);
    }

    @Test
    void classifyColorGreen() {
        String classification = ColorUtils.classifyColor(new ColorData(0, 255, 0));
        assertNotNull(classification);
    }

    @Test
    void classifyColorBlue() {
        String classification = ColorUtils.classifyColor(new ColorData(0, 0, 255));
        assertNotNull(classification);
    }

    @Test
    void classifyColorWhite() {
        String classification = ColorUtils.classifyColor(new ColorData(255, 255, 255));
        assertNotNull(classification);
    }

    @Test
    void classifyColorBlack() {
        String classification = ColorUtils.classifyColor(new ColorData(0, 0, 0));
        assertNotNull(classification);
    }
}
