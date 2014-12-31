package com.yuantops.tvplayer.bean;

import java.io.Serializable;

/**
 * DLNA报文实体
 * @author yuan (Email: yuan.tops@gmail.com) 
 * @date Dec 30, 2014 
 */
public class DLNAMessage implements Serializable{
	
	private DLNAHead dlnaHead;
	private DLNABody dlnaBody;
	
	/**
	 * 根据报文头部、报文正文生成报文
	 * @param dlnaHead
	 * @param dlnaBody
	 */
	public DLNAMessage(DLNAHead dlnaHead, DLNABody dlnaBody) {
		this.dlnaHead = dlnaHead;
		this.dlnaBody = dlnaBody;
	}
	
	/**
	 * 输出DLNA报文全文:头部+正文
	 * @return
	 */
	public String printDLNAMessage(){
		return this.dlnaHead.printDLNAHead() + this.dlnaBody.printDLNABody();
	}
}
