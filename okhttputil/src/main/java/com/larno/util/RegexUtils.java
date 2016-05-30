package com.larno.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author sks
 * 正则表达式的工具类
 */
public class RegexUtils {
	
	private final static String phoneReg = "^1[3|4|5|7|8|][0-9]{9}$"; 	
	private final static String BLANK = " ";
	
	/**
	 * 是否匹配电话号码
	 * @param phoneNum
	 * @return
	 */
	public static boolean isMatchPhoneNum(String phoneNum){
		if(phoneNum == null){
			return false;
		}
		return phoneNum.matches(phoneReg);
	}
	
	/**
	 * 将手机号格式化
	 * @param phoneNum
	 * @return
	 */
	public static String getRegPhoneNum(String phoneNum){
		if(phoneNum == null){
			return "";
		}
		StringBuilder builder = new StringBuilder(phoneNum);
		if(phoneNum.length() > 7){
			builder.insert(3,BLANK);
			builder.insert(8, BLANK);
		}else if(phoneNum.length() > 4){
			builder.insert(3,BLANK);
		}
		return builder.toString();
	}
	
	/**
	 * 将手机号格式化为中间“****”
	 * @param phoneNum
	 * @return
	 */
	public static String getRegPhoneNum2(String phoneNum){
		if(phoneNum == null){
			return "";
		}
		if(phoneNum.length() == 11){//正确的手机号
			String rexPhone = phoneNum.substring(3,7);
			return phoneNum.replaceAll(rexPhone, "****");
		}
		return phoneNum;
	}
	
	/**
	 * 得到除首字符以为的字符全部替换成“*”的新字符串
	 * @param name
	 * @return
	 */
	public static String getRegName(String name){
		String regName = "";
		if(TextUtils.isEmpty(name)){
			return regName;
		}
		if(name.length() == 1){
			return name;
		}
		String targetName = name.substring(1,name.length());//要替换的姓名
		StringBuilder sb = new StringBuilder();//"*"字符串
		for(int i = 0;i<targetName.length();i++){
			sb.append("*");
		}
		regName = name.replace(targetName, sb.toString());
		return regName;
	}
	
	/**
	 * 金额格式化
	 * @param s 金额
	 * @return 格式后的金额
	 */
	public static String insertComma(String number) {
	    if (number == null || number.length() < 1) {
	        return "";
	    }
	    String s = delComma(number);
	    NumberFormat formater = null;
	    double num = Double.parseDouble(s);
	    if(s.contains(".")){
	    	String[] arr = s.split(".");
	    	StringBuffer buff = new StringBuffer();
	        buff.append("###,###.");
	        if(arr.length > 1){//有小数位
	        	for (int i = 0; i < arr[1].length(); i++) {
	        		buff.append("#");
	        	}
	        }else{//默认添加一个小数位
	        	buff.append("#");
	        }
	        formater = new DecimalFormat(buff.toString());
	    }else{
	    	  formater = new DecimalFormat("###,###");
	    }
	    return formater.format(num);
	}
	
	/**
	 * 金额去掉“,”
	 * @param s 金额
	 * @return 去掉“,”后的金额
	 */
	public static String delComma(String s) {
	    String formatString = "";
	    if (s != null && s.length() >= 1) {
	        formatString = s.replaceAll(",", "");
	    }
	 
	    return formatString;
	}
	
}
