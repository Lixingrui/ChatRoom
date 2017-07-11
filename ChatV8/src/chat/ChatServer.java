package chat;
import java.net.*;
import java.io.DataInputStream;
import java.io.IOException;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; 
	    try {
	    	  //����һ���������˵�Socket������8888�˿ڣ�tcp�˿ں�
			ServerSocket ss = new ServerSocket(8888);  
			started = true;
			while (started){
				boolean bConnected = false;
            	Socket s = ss.accept();
System.out.println("m client connected!");//*�����õ���䣬�ͻ���������,ע�� 
                bConnected = true;
                DataInputStream dis = new DataInputStream(s.getInputStream());
                while(bConnected){        //client���ͣ���߾�Ҫread����ʼֻread��һ�Σ�����for����read�ܶ�Σ���֤server����ʾ
                	String str = dis.readUTF(); //��һ���ַ�������
                	System.out.println(str);
                }
                dis.close(); //����һ��started,��ʾ�Ƿ����client��bConnected��ʾ�Ƿ�����
            }  //*�����ò�����
	    } catch (IOException e) {
			e.printStackTrace();
		}

	}

}
