package com.looksee.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
