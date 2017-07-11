package chat;
/***
 * XiaoruiZhang
 * 2017/07/11
 * V2 ���������ı����С��V3 ���봰�ڹرչ���;V4 ����Textfield���¼�����V5��һ��server�ˣ���ʹClient����server��;V6�ͻ��˸�����������Ϣ
 * ����һ�򿪱��Զ�����
 * �Ҽ�source format�ɵ��������ʽ����ʱ���̣������޸Ĵ�ǰ����
 * ���ü�����Ȼ���ټӵ�main
 */
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatClientz1 extends Frame {
	Socket s = null; //��s����connect�������У����Ǵ�Ҷ�������
	
	DataOutputStream dos = null; //��dos������ÿ�ε�����������ֵ���Ͳ�����̧ͷ���ֵ�����
	
	TextField tfTxt = new TextField();
	//������һ�������಻���ʣ���Ϊ�����ܶ���룻 �ò����಻���ʣ��������ڲ��࣡�������棩
	TextArea taContent = new TextArea();

	public static void main(String[] args) {
		new ChatClientz1().launchFrame();
	}
 
	public void launchFrame(){
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);//���������ı����λ��
		add(taContent, BorderLayout.NORTH);
		pack();  //�������ڵĴ�С�����ʺ������������ѡ��С�Ͳ���
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
             * ����Socket��
             * IP��ַ�Ǳ������͵�ַ������������һ̨�����ϲ��Ը����˺ͷ�����
             * �˿ں�ѡȡΪ5000��ע�⣬�˿ں�ֻ����1024~65535֮��ѡȡ
             */
		    s = new Socket("127.0.0.1", 8888); //����ĳ�Socket s = new....�ͻ��п�ָ���
            dos= new DataOutputStream(s.getOutputStream());
System.out.println("connected!");
		} catch (UnknownHostException e) { //�Ҳ�������
			e.printStackTrace();
		} catch (IOException e) { //�޷�������������������ȥ��
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
		//Ҫ��TextField��������ݷŵ�ŶTextArea
			String str = tfTxt.getText().trim();
			taContent.setText(str);
			tfTxt.setText("");   //���ͺ󱾶Ի������
			
			//���͵��������ˣ�����Ҫ�õ�Socket�����������Socket s= null;
			try {
System.out.println(s);//���������Լ���s�ǲ���null�������null�������sgetStream�����ָ���
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(str);
				dos.flush();
				//dos.close();//��ôʲôʱ��Ҫ�ص���An����connect����������ʱ��
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
	}
}
