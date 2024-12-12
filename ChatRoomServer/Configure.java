package cn.shawyaw.chatroom;

final public class Configure {
	public static final int PORT = 8080;
	public static final String IP = "127.0.0.1";
	
	public static final int DB_PORT = 3306;
	public static final String DB_IP = "127.0.0.1";
	
	/**互发消息，全都定义成0，则服务器可以直接转发*/
	public static final byte MESSAGE = 0;
	
	/**客户端向服务器发送*/
	public static final byte LOGIN = 1;
	public static final byte QUIT = 2;
	public static final byte REGISTER = 3; 
	public static final byte REGISTER_SUCCESS = 9;
	public static final byte REGISTER_FAIL = 10;
	
	/**服务向客户端发送*/
	public static final byte NO_USER = 6;
	public static final byte PASS_ERROR = 7;
	public static final byte USER_LIST = 8;
	
}

