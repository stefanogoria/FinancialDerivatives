package com.monte.carlo.derivatives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionManager {

public static enum OptionType{VANILLA, DIGITAL, BARRIER_UP_IN, BARRIER_UP_OUT,
	BARRIER_DOWN_IN, BARRIER_DOWN_OUT };
	
public static enum ExerciseStyle{EUROPEAN, AMERICAN};	
public static enum PayOffType{CALL, PUT};	
	
public static	 Map<String,OptionType> OptionMap = new HashMap<String, OptionType>();	
public static	 Map<String,ExerciseStyle> StyleMap = new HashMap<String, ExerciseStyle>();
public static	 Map<String,PayOffType> PayOffMap = new HashMap<String, PayOffType>();

public static void initOptionManager(){
	OptionMap.put("Vanilla", OptionType.VANILLA);
	OptionMap.put("Digital", OptionType.DIGITAL);
	OptionMap.put("Barrier Up-In", OptionType.BARRIER_UP_IN);
	OptionMap.put("Barrier Up-Out", OptionType.BARRIER_UP_OUT);
	OptionMap.put("Barrier Down-In", OptionType.BARRIER_DOWN_IN);
	OptionMap.put("Barrier Down-Out", OptionType.BARRIER_DOWN_OUT);
	
	StyleMap.put("European", ExerciseStyle.EUROPEAN);
	StyleMap.put("American", ExerciseStyle.AMERICAN);
	
	PayOffMap.put("Call", PayOffType.CALL);
	PayOffMap.put("Put", PayOffType.PUT);
}

public	static ArrayList<String> getParameterList(OptionType theOptionType){

	ArrayList<String> parameterList = new ArrayList<String>();
	switch(theOptionType){
	case VANILLA:
		parameterList.add("Strike");
		parameterList.add("Expiry");
		break;
		
	case DIGITAL:
		parameterList.add("Strike");
		parameterList.add("Expiry");
		break;	
	
	case BARRIER_UP_IN:
		parameterList.add("Strike");
		parameterList.add("Expiry");
		parameterList.add("Barrier");
		parameterList.add("Rebate");
		break;
		
	case BARRIER_UP_OUT:
		parameterList.add("Strike");
		parameterList.add("Expiry");
		parameterList.add("Barrier");
		parameterList.add("Rebate");
		break;	
		
	case BARRIER_DOWN_IN:
		parameterList.add("Strike");
		parameterList.add("Expiry");
		parameterList.add("Barrier");
		parameterList.add("Rebate");
		break;
		
	case BARRIER_DOWN_OUT:
		parameterList.add("Strike");
		parameterList.add("Expiry");
		parameterList.add("Barrier");
		parameterList.add("Rebate");
		break;	
		
	default:
		break;
	
	}
	return parameterList;
}

public static ArrayList<ExerciseStyle> getExerciseStyle(OptionType theOptionType){	
	ArrayList<ExerciseStyle> styles = new ArrayList<ExerciseStyle>();
	
	switch(theOptionType){
	
	case VANILLA:
		styles.add(ExerciseStyle.EUROPEAN);
		styles.add(ExerciseStyle.AMERICAN);
		break;
		
	case DIGITAL:
		styles.add(ExerciseStyle.EUROPEAN);
		break;	
	
	default:
		break;
	}
	
	return styles;
}

public static Option getOptionPricer(OptionType opt_type, PayOffType pay_off_type,
		ExerciseStyle ex_style, Map<String,Double> params){
	Option option = null;
	double spot = params.get("Spot");
	double strike = params.get("Strike");
	double r = params.get("Rate");
	double d = params.get("Dividend");
	double T = params.get("Expiry");
	double vol = params.get("Volatility");
	
	switch(opt_type){
	case VANILLA:
		if(pay_off_type == PayOffType.CALL){
		option = new EuropeanCall(spot, strike, r, d, T, vol);
		}else if(pay_off_type == PayOffType.PUT){
			option = new EuropeanPut(spot, strike, r, d, T, vol);
		}
		break;
		
	default:	
	option = null;
		break;
	}
	
	return option;
}

}
