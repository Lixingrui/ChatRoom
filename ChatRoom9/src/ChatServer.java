import java.io.*;
import java.net.*;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; //设置一个布尔型变量，时时刻刻检测软件是否被启动
	    try {
			ServerSocket ss = new ServerSocket(8888); //监听端口8888
			started = true;
			while (started){
				boolean bConnected = false;
            	Socket s = ss.accept();
System.out.println("a client connected!");//把以后可能要删掉的调试语句放在最左端
                bConnected = true;  //为每一个客户端建立一个是否连接的connect
                DataInputStream dis = new DataInputStream(s.getInputStream());
               /* for(;;){
                	String str = dis.readUTF();
                	System.out.println(str);
                }//无法判断是否结束*/
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
