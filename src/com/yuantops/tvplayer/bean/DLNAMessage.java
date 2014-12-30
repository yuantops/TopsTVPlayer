package com.yuantops.tvplayer.bean;

import java.io.Serializable;

/**
 * DLNA报文实体
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Dec 30, 2014 
 */
public class DLNAMessage implements Serializable{
	private DLNAHead dlnaHead;
	private DLNABody dlnaBody;
}
