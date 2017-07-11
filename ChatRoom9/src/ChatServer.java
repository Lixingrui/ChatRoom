import java.io.*;
import java.net.*;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; 
	    try {
			ServerSocket ss = new ServerSocket(8888); //监听端口8888
			started = true;
			while (started){
				boolean bConnected = false;
            	Socket s = ss.accept();
System.out.println("m client connected!");//把以后可能要删掉的调试语句放在最左端
                bConnected = true;
                DataInputStream dis = new DataInputStream(s.getInputStream());
                while(bConnected){//client发送了，这边就要read，开始只read了一次，现在for可以read很多次，保证server会显示
                	String str = dis.readUTF();
                	System.out.println(str);
                }
                dis.close(); //加了一个started,表示是否打开了client，bConnected表示是否连接
            }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}

	}

}
