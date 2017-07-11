package chat;
import java.net.*;
import java.io.DataInputStream;
import java.io.IOException;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; 
	    try {
	    	  //建立一个服务器端的Socket，监听8888端口，tcp端口号
			ServerSocket ss = new ServerSocket(8888);  
			started = true;
			while (started){
				boolean bConnected = false;
            	Socket s = ss.accept();
System.out.println("m client connected!");//*调试用的语句，客户端连接了,注释 
                bConnected = true;
                DataInputStream dis = new DataInputStream(s.getInputStream());
                while(bConnected){        //client发送，这边就要read，开始只read了一次，现在for可以read很多次，保证server会显示
                	String str = dis.readUTF(); //读一个字符串进来
                	System.out.println(str);
                }
                dis.close(); //加了一个started,表示是否打开了client，bConnected表示是否连接
            }  //*或者用布尔型
	    } catch (IOException e) {
			e.printStackTrace();
		}

	}

}
