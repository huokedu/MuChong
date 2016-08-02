package com.htlc.muchong.bean;

public class AddressItemBean {
	private String name;
	private String pcode;
	public String getName(){
		return name;
	}
	public String getPcode(){
		return pcode;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPcode(String pcode){
		this.pcode=pcode;
	}
	@Override
	public String toString() {
		return name;
	}
}
