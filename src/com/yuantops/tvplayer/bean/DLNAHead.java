package com.yuantops.tvplayer.bean;

import java.io.Serializable;

/**
 * DLNA报文的报头
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Dec 30, 2014 
 */
public class DLNAHead implements Serializable{
	
	private String MessageType;//"NOTIFY""FORWARD""CONNECT""ACCEPT""REJECT"
	private RoutingLabel From;//起始路由标签
	private RoutingLabel To;//终点路由标签
	private RoutingLabel Source;//源dlnaagent网关标签
	private RoutingLabel Target;//目标dlnaagent网关标签
	private String ContentLength;//消息正文字节长度
	
	/**
	 * @param messageType "NOTIFY""FORWARD""CONNECT""ACCEPT""REJECT"
	 * @param from 起始路由标签
	 * @param to 终点路由标签
	 * @param contentLength 消息正文字节长度
	 * @param source 源dlnaagent网关标签
	 * @param target 目标dlnaagent网关标签
	 */
	public DLNAHead(String messageType, RoutingLabel from, RoutingLabel to, RoutingLabel source, RoutingLabel target,
			String contentLength) {
		MessageType = messageType;
		From = from;
		To = to;
		ContentLength = contentLength;
		Source = source;
		Target = target;
	}
	
	/**
	 * 输出DLNA报文头部(已包含空白行)
	 * @return
	 */
	public String printDLNAHead(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("POST /DLNA/PROXY/" + this.MessageType + " HTTP/1.0"+ "\r\n");
		strBuilder.append(this.From.printLabel()+ "\r\n");
		strBuilder.append(this.To.printLabel()+ "\r\n");
		strBuilder.append(this.Source.printLabel()+ "\r\n");
		strBuilder.append(this.Target.printLabel()+ "\r\n");
		strBuilder.append("Content-length: " + this.ContentLength+ "\r\n");
		strBuilder.append("\r\n");
		return strBuilder.toString();
	}
	
	public String getMessageType() {
		return MessageType;
	}
	public void setMessageType(String messageType) {
		MessageType = messageType;
	}
	public RoutingLabel getFrom() {
		return From;
	}
	public void setFrom(RoutingLabel from) {
		From = from;
	}
	public RoutingLabel getTo() {
		return To;
	}
	public void setTo(RoutingLabel to) {
		To = to;
	}
	public String getContentLength() {
		return ContentLength;
	}
	public void setContentLength(String contentLength) {
		ContentLength = contentLength;
	}
	public RoutingLabel getSource() {
		return Source;
	}
	public void setSource(RoutingLabel source) {
		Source = source;
	}
	public RoutingLabel getTarget() {
		return Target;
	}
	public void setTarget(RoutingLabel target) {
		Target = target;
	}
}
