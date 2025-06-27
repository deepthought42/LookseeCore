package com.looksee.models;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a header node in the DOM tree
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeaderNode {
    private String tag;
    private String text;
    private List<HeaderNode> children;

    /**
     * Constructs a new header node with the given tag and text
     * @param tag the tag of the header node
     * @param text the text of the header node
     */
    public HeaderNode(String tag, String text) {
        this.tag = tag;
        this.text = text;
        this.children = new ArrayList<>();
    }

    /**
     * Adds a child to the header node
     * @param child the child to add
     */
    public void addChild(HeaderNode child) {
        this.children.add(child);
    }
}
