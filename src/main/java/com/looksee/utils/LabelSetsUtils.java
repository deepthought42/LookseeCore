package com.looksee.utils;

import org.springframework.stereotype.Service;

import com.looksee.models.enums.FormType;

/**
 * Utility class for label sets.
 */
@Service
public class LabelSetsUtils {

	/**
	 * Returns the form type options.
	 * @return the form type options
	 */
	public static FormType[] getFormTypeOptions() {
		return FormType.values();
	}
}
