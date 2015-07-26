package com.yuantops.tvplayer.bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import android.util.Log;

/**
 * DLNA报文实体
 * 
 * @author yuan (Email: yuan.tops@gmail.com)
 * @date Dec 30, 2014
 */
public class DLNAMessage implements Serializable {
	private static final String TAG = DLNAMessage.class.getSimpleName();

	private DLNAHead dlnaHead;
	private DLNABody dlnaBody;

	/**
	 * 根据报文头部对象、报文正文对象生成报文对象
	 * 
	 * @param dlnaHead
	 * @param dlnaBody
	 */
	public DLNAMessage(DLNAHead dlnaHead, DLNABody dlnaBody) {
		this.dlnaHead = dlnaHead;
		this.dlnaBody = dlnaBody;
	}

	/**
	 * 根据字节流生成DLNA报文对象
	 * @param dlnaMsgInputStream
	 */
	public DLNAMessage(InputStream dlnaMsgInputStream) {
		BufferedReader buffReader = null;
		try {
			buffReader = new BufferedReader(new InputStreamReader(dlnaMsgInputStream, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.d(TAG, "UnsupportedEncodingException");
			e.printStackTrace();
		}
		//TODO
		//读出DLNAMessage 头部的字符串
		//根据字符串生成DLNAHead 对象
		//从DLNAHead中提取出DLNABody的字节流长度
		//从字节流中读取固定长度的字节，生成
	}

	/**
	 * 输出DLNA报文全文:头部+正文
	 * 
	 * @return
	 */
	public String printDLNAMessage() {
		return this.dlnaHead.printDLNAHead() + this.dlnaBody.printDLNABody();
	}

}
