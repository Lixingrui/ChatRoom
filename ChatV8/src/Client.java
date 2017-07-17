import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Client extends JFrame implements ActionListener {

	// �������
	JPanel Chatback, pnlChat,pnlTo;
	JButton Chatbtn;
	JTextArea ChattxtView;
	JLabel lblTo,list;
	JComboBox listOnline;
	JFrame clientFrame = null;
	
	// ��¼����
	JPanel pnlLogin;
	JLabel lblIP, lblName, lblPassword;
	JTextField txtTalk, txtIP, txtName;
	JPasswordField txtPassword;
	JButton btnLogin, btnReg;
	JDialog dialogLogin = null;
	
	//ҳ�沼��
	GridBagLayout gl;          //��һ����̬�ľ��ε�Ԫ����
	BorderLayout bdl;         //�߽粼�֣��ϱ�����
	GridBagConstraints gbc;   //�� GridBagLayout �಼�õ������Լ��
			
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;

	String strSend, strReceive, strKey, strStatus;
	private StringTokenizer st;

	public Client() {
		// ��ʼ������
		gl = new GridBagLayout();
		bdl = new BorderLayout();
		gbc = new GridBagConstraints();
		Chatback = (JPanel) getContentPane();
		Chatback.setLayout(bdl);

		// ��¼ҳ��
		pnlLogin = new JPanel();
		pnlLogin.setLayout(gl);
		lblIP = new JLabel("������IP:");
		lblName = new JLabel("    �û���:");
		lblPassword = new JLabel("        ����:");
		txtIP = new JTextField(12);
		txtName = new JTextField(12);
		txtPassword = new JPasswordField(12);
		txtIP.setText("127.0.0.1");
		btnLogin = new JButton("��¼");
		btnReg = new JButton("ע��");
		
		//����ҳ��
		Chatbtn = new JButton("����");
		listOnline = new JComboBox();
		lblTo = new JLabel(" ");
		txtTalk = new JTextField(40);
		pnlChat = new JPanel();
		ChattxtView = new JTextArea(20, 40);
		
		//�������ѡ��ҳ��
		pnlTo = new JPanel();
		pnlTo.setLayout(new BorderLayout());
		pnlTo.setPreferredSize(new Dimension(200, 30));
		list = new JLabel("������IP:");
		pnlTo.add(list,BorderLayout.NORTH);
		pnlTo.add(listOnline,BorderLayout.NORTH);
		add(pnlTo, BorderLayout.EAST);	
		
		// ��������
		Chatbtn.addActionListener(this);
		btnLogin.addActionListener(this);
		btnReg.addActionListener(this);
		listOnline.addItem("All");

		pnlChat.add(lblTo);
		pnlChat.add(txtTalk);
		pnlChat.add(Chatbtn);
		Chatback.add("Center", ChattxtView);
		Chatback.add("South", pnlChat);
		
		Chatbtn.setEnabled(false);
		
		// ��½���ʼ��
		dialogLogin = new JDialog(this, "��½", true);
		dialogLogin.getContentPane().setLayout(new FlowLayout());
		dialogLogin.getContentPane().add(lblIP);
		dialogLogin.getContentPane().add(txtIP);
		dialogLogin.getContentPane().add(lblName);
		dialogLogin.getContentPane().add(txtName);
		dialogLogin.getContentPane().add(lblPassword);
		dialogLogin.getContentPane().add(txtPassword);
		dialogLogin.getContentPane().add(btnLogin);
		dialogLogin.getContentPane().add(btnReg);
		dialogLogin.setBounds(300, 300, 250, 170);
		dialogLogin.show(true);
		
		// ������ʼ��
		clientFrame = new JFrame(txtName.getText());
		clientFrame.getContentPane().add(Chatback);
		clientFrame.setSize(600, 450);
		clientFrame.setVisible(true);
		clientFrame.setResizable(false);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Client();
	}

	// ����������ͨ�ŵ��׽���
	void connectServer() {
		try {
			socket = new Socket(txtIP.getText(), 8888);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(this, "���ӷ�����ʧ��!", "ERROR",JOptionPane.INFORMATION_MESSAGE);
			txtIP.setText("");
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ��������
	public void popWindows(String strWarning, String strTitle) {
		JOptionPane.showMessageDialog(this, strWarning, strTitle,
				JOptionPane.INFORMATION_MESSAGE);
	}

	//��½��ʼ��
	private void initLogin() throws IOException {
		strReceive = in.readLine();
		st = new StringTokenizer(strReceive, "|");
		strKey = st.nextToken();
		if (strKey.equals("login")) {
			strStatus = st.nextToken();	
			//�����¼�ɹ�
			if (strStatus.equals("succeed")) {
				btnLogin.setEnabled(false);
				Chatbtn.setEnabled(true);
				pnlLogin.setVisible(false);
				dialogLogin.dispose();
				new ClientThread(socket);
				out.println("init|online");
			}
			popWindows(strKey + " " + strStatus + "!", "Login");
		}
		if (strKey.equals("warning")) {
			strStatus = st.nextToken();
			popWindows(strStatus, "Register");
		}
	}

	//�������ӿ�
	public void actionPerformed(ActionEvent evt) {
		Object obj = evt.getSource();
		try {
			if (obj.equals(btnLogin)) {
				if ((txtIP.getText().length()>0) && (txtName.getText().length()>0)
						&& (txtPassword.getText().length() > 0)) {
					connectServer();
					strSend = "login|" + txtName.getText() + "|"+ String.valueOf(txtPassword.getPassword());
					out.println(strSend);
					initLogin();
				} else {
					popWindows("������������Ϣ", "ERROR");
				}
			} else if (obj.equals(btnReg)) {
				if ((txtName.getText().length()>0) && (txtPassword.getText().length()>0)) {
					connectServer();
					strSend = "reg|" + txtName.getText() + "|"+ String.valueOf(txtPassword.getPassword());
					out.println(strSend);
					initLogin();
				}
			} else if (obj.equals(Chatbtn)) {
				if (txtTalk.getText().length() > 0) {
					out.println("talk|" + txtTalk.getText() + "|"+ txtName.getText() + "|"
							+ listOnline.getSelectedItem().toString());
					txtTalk.setText("");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	class ClientThread implements Runnable {
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private String strReceive, strKey;
		private Thread threadTalk;
		private StringTokenizer st;

		public ClientThread(Socket s) throws IOException {
			this.socket = s;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			threadTalk = new Thread(this);
			threadTalk.start();
		}
		
		// ����
		public void run() {
			while (true) {
				synchronized (this) {
					try {
						strReceive = in.readLine();
						st = new StringTokenizer(strReceive, "|");
						strKey = st.nextToken();
						if (strKey.equals("talk")) {
							String strTalk = st.nextToken();
							strTalk = ChattxtView.getText() + "\r\n "+ strTalk;
							ChattxtView.setText(strTalk);
						} else if (strKey.equals("online")) {
							String strOnline;
							while (st.hasMoreTokens()) {
								strOnline = st.nextToken();
								listOnline.removeItem(strOnline);
								listOnline.addItem(strOnline);
							}
						} else if (strKey.equals("remove")) {
							String strRemove;
							while (st.hasMoreTokens()) {
								strRemove = st.nextToken();
								listOnline.removeItem(strRemove);
							}
						} else if (strKey.equals("warning")) {
							String strWarning = st.nextToken();
							popWindows(strWarning, "Warning");
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					} catch (IOException e) {
					}
				}
			}
		}
	}
}
