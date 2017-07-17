import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

public class Server extends Thread {
	
	// main方法，实例化服务器端程序
	public static void main(String args[]) {
		new Server();
	}
	
	SFrame serverFrame = null;
	ServerSocket serverSocket = null; // 创建服务器端套接字

	public boolean WRun = false;
	private final int SERVER_PORT = 8888;// 定义服务器端口号
	
	public Server() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT); // 启动服务
			WRun = true;

			serverFrame = new SFrame();
			getServerIP(); // 得到并显示服务器端IP
			System.out.println("服务器端口地址为:" + SERVER_PORT);
			
			serverFrame.taLog.setText("服务器已启动");
			while (true) {
				Socket socket = serverSocket.accept(); // 监听客户端的连接请求，并返回客户端socket
				new SThread(socket, serverFrame); // 创建一个新线程来处理与该客户的通讯
			}
		} catch (BindException e) {
			System.out.println("端口使用中，请关掉相关程序，重新运行服务器");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("[ERROR] 无法启动服务器" + e);
		}
		this.start(); // 启动线程
	}

	// 获取服务器的主机名和IP地址
	public void getServerIP() {
		try {
			InetAddress serverAddress = InetAddress.getLocalHost();
			byte[] ipAddress = serverAddress.getAddress();

			serverFrame.txtServerName.setText(serverAddress.getHostName());
			serverFrame.txtIP.setText(serverAddress.getHostAddress());
			serverFrame.txtPort.setText("8888");

System.out.println("服务器IP是:" + (ipAddress[0] & 0xff) + "."
			+ (ipAddress[1] & 0xff) + "." + (ipAddress[2] & 0xff) + "."+ (ipAddress[3] & 0xff));
		} catch (Exception e) {
System.out.println("无法得到服务器IP地址" + e);
		}
	}
}