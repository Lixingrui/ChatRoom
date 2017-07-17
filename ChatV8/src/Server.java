import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

public class Server extends Thread {
	
	// main������ʵ�����������˳���
	public static void main(String args[]) {
		new Server();
	}
	
	SFrame serverFrame = null;
	ServerSocket serverSocket = null; // �������������׽���

	public boolean WRun = false;
	private final int SERVER_PORT = 8888;// ����������˿ں�
	
	public Server() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT); // ��������
			WRun = true;

			serverFrame = new SFrame();
			getServerIP(); // �õ�����ʾ��������IP
			System.out.println("�������˿ڵ�ַΪ:" + SERVER_PORT);
			
			serverFrame.taLog.setText("������������");
			while (true) {
				Socket socket = serverSocket.accept(); // �����ͻ��˵��������󣬲����ؿͻ���socket
				new SThread(socket, serverFrame); // ����һ�����߳���������ÿͻ���ͨѶ
			}
		} catch (BindException e) {
			System.out.println("�˿�ʹ���У���ص���س����������з�����");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("[ERROR] �޷�����������" + e);
		}
		this.start(); // �����߳�
	}

	// ��ȡ����������������IP��ַ
	public void getServerIP() {
		try {
			InetAddress serverAddress = InetAddress.getLocalHost();
			byte[] ipAddress = serverAddress.getAddress();

			serverFrame.txtServerName.setText(serverAddress.getHostName());
			serverFrame.txtIP.setText(serverAddress.getHostAddress());
			serverFrame.txtPort.setText("8888");

System.out.println("������IP��:" + (ipAddress[0] & 0xff) + "."
			+ (ipAddress[1] & 0xff) + "." + (ipAddress[2] & 0xff) + "."+ (ipAddress[3] & 0xff));
		} catch (Exception e) {
System.out.println("�޷��õ�������IP��ַ" + e);
		}
	}
}