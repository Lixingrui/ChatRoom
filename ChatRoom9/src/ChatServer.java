import java.io.*;
import java.net.*;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; //����һ�������ͱ�����ʱʱ�̼̿������Ƿ�����
	    try {
			ServerSocket ss = new ServerSocket(8888); //�����˿�8888
			started = true;
			while (started){
				boolean bConnected = false;
            	Socket s = ss.accept();
System.out.println("a client connected!");//���Ժ����Ҫɾ���ĵ��������������
                bConnected = true;  //Ϊÿһ���ͻ��˽���һ���Ƿ����ӵ�connect
                DataInputStream dis = new DataInputStream(s.getInputStream());
               /* for(;;){
                	String str = dis.readUTF();
                	System.out.println(str);
                }//�޷��ж��Ƿ����*/
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
