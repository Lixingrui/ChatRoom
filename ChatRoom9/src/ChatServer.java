import java.io.*;
import java.net.*;

public class ChatServer {
	
	public static void main(String[] args) {
		boolean started = false; //设置一个布尔型变量，时时刻刻检测软件是否被启动
		ServerSocket ss = null;
		Socket s = null;
		DataInputStream dis = null;
		
	    try {
			 ss = new ServerSocket(8888); //监听端口8888
	    }catch (BindException e){
	    	System.out.println("端口使用中......");
	    	System.out.println("请关闭程序并重新运行Server！");
	    	System.exit(0);
	    } catch (IOException e){ 
	    	e.printStackTrace();
	    }
	    
	    /*catch (IOException e){ //还会有其他的Exception，所以还有改进的地方
System.out.println("系统没有监听启动或连接失败！");
	    	e.printStackTrace();
	    }*/
	    try{
			started = true;
			while (started){
				boolean bConnected = false;
            	 s = ss.accept();//接收2信息
System.out.println("a client connected!");//把以后可能要删掉的调试语句放在最左端
                bConnected = true;  //为每一个客户端建立一个是否连接的connect
                dis = new DataInputStream(s.getInputStream());//放到外面
                while(bConnected){//client发送了，这边就要read，开始只read了一次，现在for可以read很多次，保证server会显示
                	String str = dis.readUTF();//bug：readUTF是阻塞式的一直在等待别人发给他
                	System.out.println(str);
                }
                //dis.close(); //加了一个started,表示是否打开了client，bConnected表示是否连接
            }
			
	    } catch (EOFException e) {//不要IO，不是任何的；eof，end of file
	    	System.out.println("client closed!");
	    } catch (IOException e) {
	    	e.printStackTrace();
	   
		} finally { //catch到了exception，finally也会被执行，所以上面的dis.close()没用,注释掉
			try { 
				if (dis !=null) dis.close();
				if (s != null) s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}//严格处理，并且对其进行判断，然后修改

	}

}

