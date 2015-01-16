package com.yuantops.tvplayer.bean;

import java.io.Serializable;

/**
 * DLNA报文报头中路由标签的实体类
 * 
 * @author admin (Email: yuan.tops@gmail.com)
 * @date 2014-12-31
 */
public class RoutingLabel implements Serializable {	

	private String type; // 设备类型：“Control”“Server”“Agent”“Proxy”
	private String family;// 设备所属的家庭ID，属于同一家庭ID的设备才能通信
	private String network;// 设备所在的局域子网地址
	private String ip;
	private String port;
	private String name;// 设备所在的局域网子网的别名
	
	/**
	 * @param type 设备类型：“Control”“Server”“Agent”“Proxy”
	 * @param family 设备所属的家庭ID。属于同一家庭ID的设备才能通信
	 * @param network 设备所在的局域子网的地址
	 * @param ip
	 * @param port
	 * @param name 设备所在的局域网子网的别名
	 */
	public RoutingLabel(String type, String family, String network, String ip,
			String port, String name) {
		super();
		this.type = type;
		this.family = family;
		this.network = network;
		this.ip = ip;
		this.port = port;
		this.name = name;
	}	
	
	/**
	 * @return type:family:network:ip:port:name格式的字符串
	 */
	public String printLabel() {
		String temp = this.type + ":" + this.family + ":" + this.network + ":"
				+ this.ip + ":" + this.port;
		if(this.name==null || this.name.length() == 0 ) {
			return temp;
		} else {
			return temp + ":" + this.name;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
