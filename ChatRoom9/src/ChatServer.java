import java.io.*;
import java.net.*;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; 
	    try {
			ServerSocket ss = new ServerSocket(8888); //�����˿�8888
			started = true;
			while (started){
				boolean bConnected = false;
            	Socket s = ss.accept();
System.out.println("m client connected!");//���Ժ����Ҫɾ���ĵ��������������
                bConnected = true;
                DataInputStream dis = new DataInputStream(s.getInputStream());
                while(bConnected){//client�����ˣ���߾�Ҫread����ʼֻread��һ�Σ�����for����read�ܶ�Σ���֤server����ʾ
                	String str = dis.readUTF();
                	System.out.println(str);
                }
                dis.close(); //����һ��started,��ʾ�Ƿ����client��bConnected��ʾ�Ƿ�����
            }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}

	}

}
