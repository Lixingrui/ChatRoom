import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class SFrame extends JFrame implements ActionListener {
	
	
	JTabbedPane tpServer;
	JPanel pnlServer;
	JLabel lblNumber, lblServerName, lblIP, lblPort, lblLog;
	JTextField txtNumber, txtServerName, txtIP, txtPort;
	JButton btnSaveLog;
	TextArea taLog;
	JLabel lblUser;
	JList lstUser;
	JScrollPane spUser;

	public SFrame() {
		super("������");
		setSize(550, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();// ����Ļ������ʾ
		Dimension fra = this.getSize();
		this.setLocation((scr.width - fra.width) / 2,(scr.height - fra.height) / 2);

		pnlServer = new JPanel();
		pnlServer.setLayout(null);
	
		lblNumber = new JLabel("��������:");
		txtNumber = new JTextField("0 ��", 10);
		txtNumber.setEditable(false);

		lblServerName = new JLabel("����������:");
		txtServerName = new JTextField(10);
		txtServerName.setEditable(false);

		lblIP = new JLabel("������IP:");
		txtIP = new JTextField(10);
		txtIP.setEditable(false);

		lblPort = new JLabel("�������˿�:");
		txtPort = new JTextField("8888", 10);
		txtPort.setEditable(false);
	
		lblLog = new JLabel("[��������־]");
		taLog = new TextArea(20, 50);
		lblLog.setBounds(5, 5, 100, 30);
		taLog.setBounds(5, 35, 380, 370);
		
		btnSaveLog = new JButton("������־");
		btnSaveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveLog();
			}
		});
		btnSaveLog.setBounds(200, 5, 100, 25);

		pnlServer.add(lblLog);
		pnlServer.add(taLog);
		pnlServer.add(btnSaveLog);
		
		lblUser = new JLabel("�����û�");
		lblUser.setBounds(380, 5, 150, 30);
		
		lstUser = new JList();
		lstUser.setVisibleRowCount(17);
		lstUser.setFixedCellWidth(180);
		lstUser.setFixedCellHeight(18);

		spUser = new JScrollPane();
		spUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spUser.getViewport().setView(lstUser);
		spUser.setBounds(380, 35, 150, 360);
		
		pnlServer.setBounds(380, 150, 300, 400);
		pnlServer.add(lblUser);
		pnlServer.add(spUser);
		
		// ����ǩ���
		this.getContentPane().add(pnlServer);
		setVisible(true);
	}

	// �ر�
	protected void closeServer() {
		this.dispose();
	}

	// ��־����
	protected void saveLog() {
		try {
			FileOutputStream fileoutput = new FileOutputStream("log.txt", true);
			String temp = taLog.getText();
			fileoutput.write(temp.getBytes());
			fileoutput.close();
			JOptionPane.showMessageDialog(null, "��¼������log.txt");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ����
	public void actionPerformed(ActionEvent evt) {
	}

	// ����������
	public static void main(String[] args) {
		new SFrame();
	}
}