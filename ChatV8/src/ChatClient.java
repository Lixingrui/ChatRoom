//���ַ��͵ڶ�����Ϣ��ʱ��ͻᱨ������DataOutputStream dos = new DataOutputStream(s.getOutputStream()); =�����ܹص���ÿ�ζ�Ҫ����
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatClient extends Frame {
	Socket s = null; //��s����connect�������У����Ǵ�Ҷ�������
	DataOutputStream dos = null; //��dos������ÿ�ε�����������ֵ���Ͳ�����̧ͷ���ֵ�����
	
	TextField tfTxt = new TextField();
	//������һ�������಻���ʣ���Ϊ�����ܶ���룻 �ò����಻���ʣ��������ڲ��࣡�������棩
	TextArea taContent = new TextArea();

	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}
 
	public void launchFrame(){
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);// ����ʾ��
		add(taContent, BorderLayout.NORTH);// ��������
		// run���м��пհף�������������
		pack();
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				//ϵͳ�˳���ʱ��ر�s����dos
				disconnect();
				System.exit(0);//����0����Ȼ��ر�
			}
			
		});
	    tfTxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		
	}
	
	public void connect(){
		try {
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
			tfTxt.setText("");
			
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
