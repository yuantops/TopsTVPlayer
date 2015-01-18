package com.yuantops.tvplayer.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DLNA报文的正文
 * @author yuan (Email: yuan.tops@gmail.com) 
 * @date Dec 30, 2014 
 */
public class DLNABody implements Serializable {
	private Map<String,String> records = null;
	
	public DLNABody(){
		this.records = new LinkedHashMap<String,String>();
	}
	
	/**
	 * @param action 信令的种类：CHAT,PLAY,LOGIN
	 * @param fromIP 消息来源的IP
	 * @param fromAccount 消息发送的帐号
	 * @param toIP 消息接收方的IP
	 * @param toAccount 消息接收方的帐号
	 */
	public DLNABody(String action, String fromIP, String fromAccount, String toIP, String toAccount){
		this.records = new LinkedHashMap<String,String>();
		records.put("ACTION", action);
		records.put("FROM_IP", fromIP);
		records.put("FROM_ACCOUNT", fromAccount);
		records.put("TO_IP", toIP);
		records.put("TO_ACCOUNT", toAccount);		
	}	
	
	/**
	 * 添加消息记录
	 * @param key
	 * @param value
	 */
	public void addRecord(String key, String value){
		records.put(key, value);
	}
	
	/**
	 * 获取参数
	 * @param key
	 * @return
	 */
	public String getValue (String key) {
		return records.get(key);
	}
	
	/**
	 * 输出DLNA报文正文：按消息存入的顺序输出。每条消息占一行，前5条消息的顺序固定。消息格式 key:value
	 * @return
	 */
	public String printDLNABody(){
		StringBuilder strBuilder = new StringBuilder();
		if(records.keySet().size() == 0){
			return "";
		}else for(String key:records.keySet()){
			strBuilder.append(key+":"+records.get(key));
			strBuilder.append("\r\n");
		}
		return strBuilder.toString();
	}
	
}
