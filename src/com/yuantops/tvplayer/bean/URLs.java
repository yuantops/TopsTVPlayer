package com.yuantops.tvplayer.bean;

public class URLs {
	
	public static final int DLNA_PROXY_PORT = 1902;

	public static final String MULTICAST_GROUP = "239.255.255.250";
	public static final int MULTICAST_PORT = 1900;

	public static final int LOCAL_DEFAULT_CONN_PORT = 1901;
	public static final int MAX_DATAGRAM_BYTES = 640;

	public static final int PROXY_PORT_MIN = 1910;
	public static final int PROXY_PORT_MAX = 1999;
	
	public static String web_server_ip = "";
	public static final String WEB_SERVER_PORT = "8080";
	public static final String WEB_SERVER_APP_NAME = "FlingHttpServer";
	
	public static final String HTTP_URL_DELIMITER = "/";
	
	//TODO 填充每个servlet
	public static final String LOGIN_SERVLET = "";
	public static final String LOGOUT_SERVLET = "";
	public static final String GET_VIDEO_LIST_SERVLET = "";
	public static final String GET_CHANNEL_STATUS_SERVLET = "";
	public static final String COMMENT_SERVLET = "";
	public static final String PLAY_RECORD_SERVLET = "";
}
