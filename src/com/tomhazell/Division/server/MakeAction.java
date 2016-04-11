package com.tomhazell.Division.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//this uses a map which stores the data sent from the server and and an array of buttons to be pressed. it then iteratues through the list, pressing 
//down the buttons in that order then letting go of them in reverse order.

public class MakeAction {
	
	private Robot robot;
	private Map<String, Integer[]> ActionMap;
	
	public MakeAction(){
		makeMap();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println(e.toString());
		}
	}
	
	public String doAction(String command) throws InterruptedException{
		Integer[] Instructions = ActionMap.get(command);//fetches the list of commands related to the command
		
		if (Instructions == null){//the command is not found
			return "unknown instruction";
		}
		
		//for each key hold it down in order
		for (int i = 0; i < Instructions.length; i++){
			robot.keyPress(Instructions[i]);
			Thread.sleep(50);
		}
		
		//let go of each key in reverse order
		for (int i = Instructions.length - 1; i > -1; i = i - 1){
			robot.keyRelease(Instructions[i]);
			Thread.sleep(50);
		}
		
		return "0";
		
	}
	
	private void makeMap(){
		ActionMap = new HashMap<String, Integer[]>();
		ActionMap.put("AmmoI" ,new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_1});
		ActionMap.put("AmmoE" ,new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_2});
		
		ActionMap.put("DrinkS" ,new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_3});
		ActionMap.put("DrinkW" ,new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_4});
		
		ActionMap.put("FoodC" ,new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_5});
		ActionMap.put("FoodB" ,new Integer[]{KeyEvent.VK_ALT, KeyEvent.VK_6});
		
		ActionMap.put("NadeFrag" ,new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_1});
		ActionMap.put("NadeFlash" ,new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_2});
		ActionMap.put("NadeEMP" ,new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_3});
		ActionMap.put("NadeGas" ,new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_4});
		ActionMap.put("NadeShock" ,new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_5});
		ActionMap.put("NadeInc" ,new Integer[]{KeyEvent.VK_CONTROL, KeyEvent.VK_6});
		
		
		
	}
}
