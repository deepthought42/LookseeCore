package com.looksee.browsing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import com.looksee.models.Element;

/**
 * Unit tests for browsing package classes.
 */
class BrowsingClassesTest {

    // ===== Coordinates =====
    @Test
    void coordinatesFromPointAndDimension() {
        Point point = new Point(10, 20);
        Dimension size = new Dimension(100, 200);
        Coordinates coords = new Coordinates(point, size, 2.0);
        assertEquals(200, coords.getWidth());
        assertEquals(400, coords.getHeight());
        assertEquals(20, coords.getX());
        assertEquals(40, coords.getY());
    }

    @Test
    void coordinatesWithPixelRatioOne() {
        Point point = new Point(50, 100);
        Dimension size = new Dimension(300, 400);
        Coordinates coords = new Coordinates(point, size, 1.0);
        assertEquals(300, coords.getWidth());
        assertEquals(400, coords.getHeight());
        assertEquals(50, coords.getX());
        assertEquals(100, coords.getY());
    }

    // ===== ElementNode =====
    @Test
    void elementNodeConstructor() {
        ElementNode<String> node = new ElementNode<>("root");
        assertEquals("root", node.getData());
        assertNotNull(node.getChildren());
        assertTrue(node.getChildren().isEmpty());
    }

    @Test
    void elementNodeAddChild() {
        ElementNode<String> parent = new ElementNode<>("parent");
        ElementNode<String> child = new ElementNode<>("child");
        parent.addChild(child);
        assertEquals(1, parent.getChildren().size());
        assertEquals(parent, child.getParent());
    }

    @Test
    void elementNodeSetParent() {
        ElementNode<String> parent = new ElementNode<>("parent");
        ElementNode<String> child = new ElementNode<>("child");
        child.setParent(parent);
        assertEquals(parent, child.getParent());
    }

    // ===== ValueDomain =====
    @Test
    void valueDomainGetRandomNumber() {
        ValueDomain domain = new ValueDomain();
        String result = domain.getRandomNumber(5);
        assertNotNull(result);
        assertEquals(5, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    void valueDomainGetRandomDecimal() {
        ValueDomain domain = new ValueDomain();
        String result = domain.getRandomDecimal(5);
        assertNotNull(result);
        assertTrue(result.contains("."));
    }

    @Test
    void valueDomainGetRandomAlphabeticString() {
        ValueDomain domain = new ValueDomain();
        String result = domain.getRandomAlphabeticString(8);
        assertNotNull(result);
        assertEquals(8, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    void valueDomainGetRandomSpecialCharacterString() {
        ValueDomain domain = new ValueDomain();
        String result = domain.getRandomSpecialCharacterString(5);
        assertNotNull(result);
        assertEquals(5, result.length());
    }

    // ===== Row =====
    @Test
    void rowDefaultConstructor() {
        com.looksee.browsing.table.Row row = new com.looksee.browsing.table.Row();
        assertNotNull(row);
    }

    // ===== Table =====
    @Test
    void tableDefaultConstructor() {
        com.looksee.browsing.table.Table table = new com.looksee.browsing.table.Table();
        assertNotNull(table);
    }

    // ===== FormField =====
    @Test
    void formFieldDefaultConstructor() {
        com.looksee.browsing.form.FormField field = new com.looksee.browsing.form.FormField();
        assertNotNull(field);
    }
}
