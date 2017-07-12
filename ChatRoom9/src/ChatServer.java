import java.io.*;
import java.net.*;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; //����һ�������ͱ�����ʱʱ�̼̿������Ƿ�����
		ServerSocket ss = null;
		Socket s = null;
		DataInputStream dis = null;
		
	    try {
			 ss = new ServerSocket(8888); //�����˿�8888
	    }catch (BindException e){
	    	System.out.println("�˿�ʹ����......");
	    	System.out.println("��رճ�����������Server��");
	    	System.exit(0);
	    } catch (IOException e){ 
	    	e.printStackTrace();
	    }
	    
	    /*catch (IOException e){ //������������Exception�����Ի��иĽ��ĵط�
System.out.println("ϵͳû�м�������������ʧ�ܣ�");
	    	e.printStackTrace();
	    }*/
	    try{
			started = true;
			while (started){
				boolean bConnected = false;
            	 s = ss.accept();//����2��Ϣ
System.out.println("a client connected!");//���Ժ����Ҫɾ���ĵ��������������
                bConnected = true;  //Ϊÿһ���ͻ��˽���һ���Ƿ����ӵ�connect
                dis = new DataInputStream(s.getInputStream());//�ŵ�����
                while(bConnected){//client�����ˣ���߾�Ҫread����ʼֻread��һ�Σ�����for����read�ܶ�Σ���֤server����ʾ
                	String str = dis.readUTF();//bug��readUTF������ʽ��һֱ�ڵȴ����˷�����
                	System.out.println(str);
                }
                //dis.close(); //����һ��started,��ʾ�Ƿ����client��bConnected��ʾ�Ƿ�����
            }
			
	    } catch (EOFException e) {//��ҪIO�������κεģ�eof��end of file
	    	System.out.println("client closed!");
	    } catch (IOException e) {
	    	e.printStackTrace();
	   
		} finally { //catch����exception��finallyҲ�ᱻִ�У����������dis.close()û��,ע�͵�
			try { 
				if (dis !=null) dis.close();
				if (s != null) s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}//�ϸ������Ҷ�������жϣ�Ȼ���޸�

	}

}

