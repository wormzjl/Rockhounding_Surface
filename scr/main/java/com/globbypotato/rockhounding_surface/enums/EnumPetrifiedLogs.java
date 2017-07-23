package com.globbypotato.rockhounding_surface.enums;

public enum EnumPetrifiedLogs implements BaseEnum {
	BETULA, 
	CASTANO,
	OPALIZED,
	RAINBOW;

	//---------CUSTOM----------------
	public static int size(){
		return values().length;
	}

	public static String name(int index) {
		return values()[index].getName();
	}

	//---------ENUM----------------
	public static String[] getNames(){
		String[] temp = new String[size()];
		for(int i = 0; i < size(); i++){
			temp[i] = getName(i);
		}
		return temp;
	}

	public static String getName(int index){
		return name(index);
	}
}