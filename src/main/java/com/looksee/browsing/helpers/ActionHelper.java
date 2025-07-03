package com.looksee.browsing.helpers;

import com.looksee.models.ActionOLD;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains the Order of operations for actions. A lower value indicates that
 *   an action has less precedence.
 *
 */
public class ActionHelper {
	public static HashMap<String, Integer> actionOrderOfOperationsMap = new HashMap<>();
	
	private static List<List<ActionOLD>> action_lists = new ArrayList<>();
	
	/*
	 * ACTION POLICIES
	 * <<<<<<<<<<<<>>>>>>>>>>>>>>>
	 * 
	 * Mouse over
	 * --------------------
	 * Click
	 * clickAt
	 * click and wait
	 * clickAtAndWait
	 * Click and hold
	 * Release
	 * Double Click
	 * -------------------------
	 * keypresses - all same policy
	 * --------------------
	 * drag and drop
	 * -------------------------
	 */
	static {
		List<ActionOLD> mouse_motion_actions = new ArrayList<>();
		//mouse_motion_actions.add(new Action("mouseover"));
		//mouse_motion_actions.add(new Action("scroll","100"));
		
		List<ActionOLD> click_actions = new ArrayList<>();
		//click_actions.add(new Action("clickAndHold"));
		click_actions.add(new ActionOLD("click"));
		//click_actions.add(new Action("clickAt"));
		//click_actions.add(new Action("clickAndWait"));
		//click_actions.add(new Action("clickAtAndWait"));
		//click_actions.add(new Action("release"));
		//click_actions.add(new Action("doubleClick"));
		
		//List<Action> keyboard_actions = new ArrayList<Action>();
		//keyboard_actions.add(new Action("sendKeys"));
		
		action_lists.add(mouse_motion_actions);
		action_lists.add(click_actions);
		//action_lists.add(keyboard_actions);
	}
	
	/**
	 * Gets the order of operation for an action
	 * 
	 * @param actionName name of the action
	 * @return {@link Integer} representing the order of operation
	 */
	public static Integer getOrderOfOperationForAction(String actionName){
		return actionOrderOfOperationsMap.get(actionName);
	}

	/**
	 * Gets the action lists
	 * 
	 * @return {@link List} of {@link List} of {@link ActionOLD}s
	 */
	public static List<List<ActionOLD>> getActionLists() {
		return action_lists;
	}
}
