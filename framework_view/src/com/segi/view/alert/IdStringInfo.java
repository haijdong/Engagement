package com.segi.view.alert;

public class IdStringInfo {
	/**
	 * id
	 */
	public int id;
	/**
	 * 名字
	 */
	public String name;
	/**
	 * 是否选中
	 */
	public boolean isChecked;
	
	public IdStringInfo(){}
	
	public IdStringInfo(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
}
