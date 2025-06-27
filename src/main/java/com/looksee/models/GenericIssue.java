package com.looksee.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * GenericIssue is a class that represents a generic issue.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericIssue {
    private String description;
    private String title;
    private String cssSelector;
    private String recommendation;
}
