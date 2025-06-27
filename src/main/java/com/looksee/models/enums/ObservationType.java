package com.looksee.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Defines all {@link ObservationType observation types} that exist in the system
 */
public enum ObservationType {
    
	ELEMENT("Element"),
	TYPOGRAPHY("Typography"),
	COLOR_PALETTE("Color_Palette"),
	PROPERTY_MAP("Property_Map"),
	STYLE_MISSING("Style_Missing"),
	PAGE_STATE("Page"),
	TYPEFACE("Typeface"),
	COLOR_CONTRAST("Color_Contrast"),
	SECURITY("Security"),
	SEO("SEO"),
	PAGE_LANGUAGE("Page_Language"),
	UNKNOWN("Unknown");
	
	private String shortName;

	ObservationType (String shortName) {
        this.shortName = shortName;
    }

    /**
     * Returns the short name of the observation type.
     * @return the short name of the observation type
     */
    @Override
    public String toString() {
        return shortName;
    }

    /**
     * Creates an ObservationType from a string.
     * @param value the string to create the ObservationType from
     * @return the ObservationType
     */
    @JsonCreator
    public static ObservationType create (String value) {
        if(value == null) {
            return UNKNOWN;
        }
        for(ObservationType v : values()) {
            if(value.equalsIgnoreCase(v.getShortName())) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the short name of the observation type.
     * @return the short name of the observation type
     */
    public String getShortName() {
        return shortName;
    }
}
