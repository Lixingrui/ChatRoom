package chat;
/***
 * XiaoruiZhang
 * 2017/07/11
 * V2 调整两个文本框大小；V3 加入窗口关闭功能;V4 加入Textfield的事件处理；V5做一个server端，并使Client连到server端;V6客户端给服务器发消息
 * 窗口一打开便自动连接
 * 右键source format可调整代码格式，及时存盘，错误修改从前到后
 * 设置监听，然后再加到main
 */
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatClientz1 extends Frame {
	Socket s = null; //让s不是connect（）独有，而是大家都可以用
	
	DataOutputStream dos = null; //用dos来保存每次的输入框的输入值，就不会有抬头出现的问题
	
	TextField tfTxt = new TextField();
	//这里用一个匿名类不合适：因为会加入很多代码； 用并且类不合适；设里用内部类！（见下面）
	TextArea taContent = new TextArea();

	public static void main(String[] args) {
		new ChatClientz1().launchFrame();
	}
 
	public void launchFrame(){
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);//设置两个文本框的位置
		add(taContent, BorderLayout.NORTH);
		pack();  //调整窗口的大小，以适合其子组件的首选大小和布局
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				System.exit(0);//*0 close the window
			}
			
		});//the way to close the window
			
	    tfTxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		
	}
	
	public void connect(){
		try {
			/**
             * 建立Socket：
             * IP地址是本机回送地址，这样可以在一台电脑上测试各户端和服务器
             * 端口号选取为5000，注意，端口号只能在1024~65535之间选取
             */
		    s = new Socket("127.0.0.1", 8888); //这里改成Socket s = new....就会有空指针错！
            dos= new DataOutputStream(s.getOutputStream());
System.out.println("connected!");
		} catch (UnknownHostException e) { //找不到主机
			e.printStackTrace();
		} catch (IOException e) { //无法连接网（网卡连不上去）
			e.printStackTrace();
		}
	}
	
	
	public void disconnect(){
		try {
			dos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private class TFListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
		//要把TextField里面的内容放单哦TextArea
			String str = tfTxt.getText().trim();
			taContent.setText(str);
			tfTxt.setText("");   //发送后本对话框清空
			
			//发送到服务器端，这是要拿到Socket，因此最上面Socket s= null;
			try {
System.out.println(s);//！！！可以检验s是不是null，如果是null，后面的sgetStream会出空指针错
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(str);
				dos.flush();
				//dos.close();//那么什么时候要关掉？An：在connect（）结束的时候
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
	}
}
