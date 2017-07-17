import java.io.*;
import java.net.*;
import java.util.*;

class SThread extends Thread {
	private Socket socket = null;	// �ͻ����׽���
	private BufferedReader in;	// ������
	private PrintWriter out;	// �����

	private static Vector UserName = new Vector(10, 5);	// ���������û���Ϣ
	private static Vector UserChannel = new Vector(10, 5);

	private String strReceive, strKey;
	private StringTokenizer st;
	private final String UFILE = "F:\\user.txt"; 	// �����û���Ϣ
	private SFrame sFrame = null;		// ��ʽ


	public SThread(Socket client, SFrame frame) throws IOException {
		socket = client;
		sFrame = frame;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 	// �ͻ��˽���
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);	// �ͻ������
		this.start();
	}

	
	public void run() {
		try {
			while (true) {
				strReceive = in.readLine();	// �յ���Ϣ����Ҫ�Ȳ�ֽ���
				st = new StringTokenizer(strReceive, "|");
				strKey = st.nextToken();
System.out.println(strKey);
				if (strKey.equals("login")) 	login(); 
				else if (strKey.equals("talk"))		talk();
				else if (strKey.equals("init")) 	freshUser();
				else if (strKey.equals("reg"))		register();
			}
		} catch (IOException e) { // �û��رտͻ��ˣ�  �رո��û��׽��֡�
			String leaveUser = closeSocket();
			Date t = new Date();
			log("�û�" + leaveUser + "���˳�, " + t.toLocaleString());
			try {
				freshUser();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
System.out.println("֪ͨ�� " + leaveUser + " �뿪");
			sendAll("talk|>>>" + leaveUser + " �뿪");
		}
	}
	
	

	// �û�ע��
	private void register() throws IOException {
		String name = st.nextToken(); 	// �û���
		String password = st.nextToken().trim();	// ����
		Date t = new Date();	//	ʱ��

		if (WExist(name)) {
			System.out.println("[ERROR��] " + name + " ע��ʧ��");
			out.println("warning|�û����ѱ�ע��");
		} else {
			RandomAccessFile userFile = new RandomAccessFile(UFILE,"rw");
			userFile.seek(userFile.length()); 	// ���ļ�β���������û�
			userFile.writeBytes(name + "|" + password + "\r\n");
			log( name + "ע��ɹ�, " +"("+ t.toLocaleString()+")");
			logSuccess(name); // ��½
		}
	}

	// �ж��Ƿ�ע��
	private boolean WExist(String name) {
		String strRead;
		try {
			FileInputStream inputfile = new FileInputStream(UFILE);
			DataInputStream inputdata = new DataInputStream(inputfile);
			while ((strRead = inputdata.readLine()) != null) {
				StringTokenizer stUser = new StringTokenizer(strRead, "|");
				if (stUser.nextToken().equals(name)) {
					return true;
				}
			}
		} catch (FileNotFoundException fn) {
			System.out.println("[ERROR] �û��ļ�������" + fn);
			out.println("warning|��д�ļ�����");
		} catch (IOException ie) {
System.out.println("[ERROR] " + ie);
			out.println("warning|��д�ļ�����");
		}
		return false;
	}

	// �û���������
	private boolean WLogin(String name, String password) {
		String strRead;
		try {
			FileInputStream inputfile = new FileInputStream(UFILE);
			DataInputStream inputdata = new DataInputStream(inputfile);
			while ((strRead = inputdata.readLine()) != null) {
				if (strRead.equals(name + "|" + password)) {
					return true;
				}
			}
		} catch (FileNotFoundException fn) {
			System.out.println("[ERROR] �û��ļ�������" + fn);
			out.println("warning|��д�ļ�����");
		} catch (IOException ie) {
System.out.println("[ERROR] " + ie);
			out.println("warning|��д�ļ�����");
		}
		return false;
	}


	// �û���½
	private void login() throws IOException {
		String name = st.nextToken(); 	// �û���
		String password = st.nextToken().trim();	// �û�����
		boolean succeed = false;
		Date t = new Date();

		log("�û�" + name + "("+"���� :" + password +")"+"���ڵ�½��..."+"("+ t.toLocaleString()+")"+ "\n"+ socket);
System.out.println("[User login] " + name + ":" + password + ":" + socket);

		for (int i = 0; i < UserName.size(); i++) {
			if (UserName.elementAt(i).equals(name)) {
				System.out.println("[ERROR] " + name + " �Ѿ���½");
				out.println("warning|" + name + "�Ѿ���½");
			}
		}
		if (WLogin(name, password)) { // �ж��û���������
			logSuccess(name);
			succeed = true;
		}
		if (!succeed) {
			out.println("warning|" +  name + "������󣬵�½ʧ��!");
			log(name + "��½ʧ�ܣ�" + t.toLocaleString());
System.out.println(name + " ��½ʧ��");
		}
	}

	// ��½�ɹ���
	private void logSuccess(String name) throws IOException {
		Date t = new Date();
		out.println("login|succeed");
		sendAll("�����û�|" + name);

		UserName.addElement(name);
		UserChannel.addElement(socket);

		log("�û�" + name + "��½�ɹ�" +"("+ t.toLocaleString()+")");
		freshUser();
		sendAll("talk|>>"+name + " ��������������/������ͨ�Ű�");
System.out.println(name + " ��½�ɹ�");
	}
	
	// �����û�
	private void freshUser() throws IOException {
		String strOnline = "�����û�";
		String[] userList = new String[20];
		String useName = null;

		for (int i = 0; i < UserName.size(); i++) {
			strOnline += "|" + UserName.elementAt(i);
			useName = " " + UserName.elementAt(i);
			userList[i] = useName;
		}

		sFrame.txtNumber.setText("" + UserName.size());
		sFrame.lstUser.setListData(userList);
System.out.println(strOnline);
		out.println(strOnline);
	}

	// ��Ϣ�����˽�Ĺ���
	@SuppressWarnings("deprecation")
	private void talk() throws IOException {
		String strTalkInfo = st.nextToken(); 	// �õ���������;
		String strSender = st.nextToken(); 	// �õ�����Ϣ��
		String strReceiver = st.nextToken(); 	// �õ�������
System.out.println("[TALK_" + strReceiver + "] " + strTalkInfo);
		Socket socketSend;
		PrintWriter outSend;

		// ��ǰʱ��
		GregorianCalendar calendar = new GregorianCalendar();
		String strTime = "(" + calendar.get(Calendar.HOUR) + ":"+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND) + ")";
		strTalkInfo += strTime;
		log(strSender + "@" + strReceiver + "@ȫ���Ա��  " + strTalkInfo);

		// ���Ͷ���
		if (strReceiver.equals("All")) {
			sendAll("talk|" + strSender + strTalkInfo);
		} else {
			if (strSender.equals(strReceiver)) {
				out.println("talk|>>> ϵͳ��ʾ�������ܸ��Լ�����Ϣ");
			} else {
				for (int i = 0; i < UserName.size(); i++) {
					if (strReceiver.equals(UserName.elementAt(i))) {
						socketSend = (Socket) UserChannel.elementAt(i);
						outSend = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(socketSend.getOutputStream())), true);
						outSend.println("talk|" + strSender + " @�㣺  " + strTalkInfo);
					} else if (strSender.equals(UserName.elementAt(i))) {
						socketSend = (Socket) UserChannel.elementAt(i);
						outSend = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(socketSend.getOutputStream())), true);
						outSend.println("talk|��@ " + strReceiver + "��  " + strTalkInfo);
					}
				}
			}
		}
	}

	// Ⱥ�Ĺ���
	private void sendAll(String strSend) {
		Socket socketSend;
		PrintWriter outSend;
		try {
			for (int i = 0; i < UserChannel.size(); i++) {
				socketSend = (Socket) UserChannel.elementAt(i);
				outSend = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socketSend.getOutputStream())),
						true);
				outSend.println(strSend);
			}
		} catch (IOException e) {
System.out.println("[ERROR] ��Ϣ����ʧ��");
		}
	}

	public void log(String log) {
		String newlog = sFrame.taLog.getText() + "\n" + log;
		sFrame.taLog.setText(newlog);
	}

	//�˳���ɾ���û�
	private String closeSocket() {
		String strUser = "";
		for (int i = 0; i < UserChannel.size(); i++) {
			if (socket.equals((Socket) UserChannel.elementAt(i))) {
				strUser = UserName.elementAt(i).toString();
				UserChannel.removeElementAt(i);
				UserName.removeElementAt(i);
				try {
					freshUser();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sendAll("remove|" + strUser);
			}
		}
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
System.out.println("[ERROR] " + e);
		}
		return strUser;
	}
}