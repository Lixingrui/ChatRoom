//发现发送第二条信息的时候就会报错，报错到DataOutputStream dos = new DataOutputStream(s.getOutputStream()); =》不能关掉，每次都要连接
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatClient extends Frame {
	Socket s = null; //让s不是connect（）独有，而是大家都可以用
	DataOutputStream dos = null; //用dos来保存每次的输入框的输入值，就不会有抬头出现的问题
	
	TextField tfTxt = new TextField();
	//这里用一个匿名类不合适：因为会加入很多代码； 用并且类不合适；设里用内部类！（见下面）
	TextArea taContent = new TextArea();

	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}
 
	public void launchFrame(){
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);// 做显示用
		add(taContent, BorderLayout.NORTH);// 做输入用
		// run后中间有空白，用下面这个语句
		pack();
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				//系统退出的时候关闭s，和dos
				disconnect();
				System.exit(0);//传“0”，然后关闭
			}
			
		});
	    tfTxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		
	}
	
	public void connect(){
		try {
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
			tfTxt.setText("");
			
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
