package com.looksee.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.looksee.models.ColorData;
import com.looksee.models.enums.ColorScheme;

/**
 * Unit tests for ColorPaletteUtils.
 */
class ColorPaletteUtilsTest {

    @Test
    void identifyColorSchemeMonochromatic() {
        List<ColorData> palette = new ArrayList<>();
        palette.add(new ColorData(100, 100, 100));
        palette.add(new ColorData(150, 150, 150));
        palette.add(new ColorData(200, 200, 200));
        ColorScheme scheme = ColorPaletteUtils.identifyColorScheme(palette);
        assertNotNull(scheme);
    }

    @Test
    void identifyColorSchemeEmptyPalette() {
        List<ColorData> palette = new ArrayList<>();
        ColorScheme scheme = ColorPaletteUtils.identifyColorScheme(palette);
        assertNotNull(scheme);
    }

    @Test
    void identifyColorSchemeSingleColor() {
        List<ColorData> palette = new ArrayList<>();
        palette.add(new ColorData(255, 0, 0));
        ColorScheme scheme = ColorPaletteUtils.identifyColorScheme(palette);
        assertNotNull(scheme);
    }

    @Test
    void scorePaletteEmptyList() {
        List<ColorData> palette = new ArrayList<>();
        double score = ColorPaletteUtils.scorePalette(palette);
        assertTrue(score >= 0.0);
    }

    @Test
    void scorePaletteWithColors() {
        List<ColorData> palette = new ArrayList<>();
        palette.add(new ColorData(255, 0, 0));
        palette.add(new ColorData(0, 255, 0));
        palette.add(new ColorData(0, 0, 255));
        double score = ColorPaletteUtils.scorePalette(palette);
        assertTrue(score >= 0.0);
    }

    @Test
    void isGrayscaleTrue() {
        ColorData gray = new ColorData(128, 128, 128);
        assertTrue(ColorPaletteUtils.isGrayscale(gray));
    }

    @Test
    void isGrayscaleFalse() {
        ColorData red = new ColorData(255, 0, 0);
        assertFalse(ColorPaletteUtils.isGrayscale(red));
    }

    @Test
    void isGrayscaleBlack() {
        ColorData black = new ColorData(0, 0, 0);
        assertTrue(ColorPaletteUtils.isGrayscale(black));
    }

    @Test
    void isGrayscaleWhite() {
        ColorData white = new ColorData(255, 255, 255);
        assertTrue(ColorPaletteUtils.isGrayscale(white));
    }

    @Test
    void groupSimilarColorsEmptyList() {
        List<ColorData> colors = new ArrayList<>();
        List<List<ColorData>> groups = ColorPaletteUtils.groupSimilarColors(colors);
        assertNotNull(groups);
        assertTrue(groups.isEmpty());
    }

    @Test
    void groupSimilarColorsDistinctColors() {
        List<ColorData> colors = new ArrayList<>();
        colors.add(new ColorData(255, 0, 0));
        colors.add(new ColorData(0, 255, 0));
        colors.add(new ColorData(0, 0, 255));
        List<List<ColorData>> groups = ColorPaletteUtils.groupSimilarColors(colors);
        assertNotNull(groups);
        assertTrue(groups.size() >= 1);
    }
}
