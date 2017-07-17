import java.io.*;
import java.net.*;
import java.util.*;

class SThread extends Thread {
	private Socket socket = null;	// 客户端套接字
	private BufferedReader in;	// 输入流
	private PrintWriter out;	// 输出流

	private static Vector UserName = new Vector(10, 5);	// 保存在线用户信息
	private static Vector UserChannel = new Vector(10, 5);

	private String strReceive, strKey;
	private StringTokenizer st;
	private final String UFILE = "F:\\user.txt"; 	// 储存用户信息
	private SFrame sFrame = null;		// 样式


	public SThread(Socket client, SFrame frame) throws IOException {
		socket = client;
		sFrame = frame;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 	// 客户端接收
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);	// 客户端输出
		this.start();
	}

	
	public void run() {
		try {
			while (true) {
				strReceive = in.readLine();	// 收到消息后需要先拆分解析
				st = new StringTokenizer(strReceive, "|");
				strKey = st.nextToken();
System.out.println(strKey);
				if (strKey.equals("login")) 	login(); 
				else if (strKey.equals("talk"))		talk();
				else if (strKey.equals("init")) 	freshUser();
				else if (strKey.equals("reg"))		register();
			}
		} catch (IOException e) { // 用户关闭客户端，  关闭该用户套接字。
			String leaveUser = closeSocket();
			Date t = new Date();
			log("用户" + leaveUser + "已退出, " + t.toLocaleString());
			try {
				freshUser();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
System.out.println("通知： " + leaveUser + " 离开");
			sendAll("talk|>>>" + leaveUser + " 离开");
		}
	}
	
	

	// 用户注册
	private void register() throws IOException {
		String name = st.nextToken(); 	// 用户名
		String password = st.nextToken().trim();	// 密码
		Date t = new Date();	//	时间

		if (WExist(name)) {
			System.out.println("[ERROR：] " + name + " 注册失败");
			out.println("warning|用户名已被注册");
		} else {
			RandomAccessFile userFile = new RandomAccessFile(UFILE,"rw");
			userFile.seek(userFile.length()); 	// 在文件尾部加入新用户
			userFile.writeBytes(name + "|" + password + "\r\n");
			log( name + "注册成功, " +"("+ t.toLocaleString()+")");
			logSuccess(name); // 登陆
		}
	}

	// 判断是否注册
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
			System.out.println("[ERROR] 用户文件不存在" + fn);
			out.println("warning|读写文件出错");
		} catch (IOException ie) {
System.out.println("[ERROR] " + ie);
			out.println("warning|读写文件出错");
		}
		return false;
	}

	// 用户名与密码
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
			System.out.println("[ERROR] 用户文件不存在" + fn);
			out.println("warning|读写文件出错");
		} catch (IOException ie) {
System.out.println("[ERROR] " + ie);
			out.println("warning|读写文件出错");
		}
		return false;
	}


	// 用户登陆
	private void login() throws IOException {
		String name = st.nextToken(); 	// 用户名
		String password = st.nextToken().trim();	// 用户密码
		boolean succeed = false;
		Date t = new Date();

		log("用户" + name + "("+"密码 :" + password +")"+"正在登陆中..."+"("+ t.toLocaleString()+")"+ "\n"+ socket);
System.out.println("[User login] " + name + ":" + password + ":" + socket);

		for (int i = 0; i < UserName.size(); i++) {
			if (UserName.elementAt(i).equals(name)) {
				System.out.println("[ERROR] " + name + " 已经登陆");
				out.println("warning|" + name + "已经登陆");
			}
		}
		if (WLogin(name, password)) { // 判断用户名和密码
			logSuccess(name);
			succeed = true;
		}
		if (!succeed) {
			out.println("warning|" +  name + "密码错误，登陆失败!");
			log(name + "登陆失败！" + t.toLocaleString());
System.out.println(name + " 登陆失败");
		}
	}

	// 登陆成功后
	private void logSuccess(String name) throws IOException {
		Date t = new Date();
		out.println("login|succeed");
		sendAll("在线用户|" + name);

		UserName.addElement(name);
		UserChannel.addElement(socket);

		log("用户" + name + "登陆成功" +"("+ t.toLocaleString()+")");
		freshUser();
		sendAll("talk|>>"+name + " 上线啦，快与他/她进行通信吧");
System.out.println(name + " 登陆成功");
	}
	
	// 在线用户
	private void freshUser() throws IOException {
		String strOnline = "在线用户";
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

	// 消息处理和私聊功能
	@SuppressWarnings("deprecation")
	private void talk() throws IOException {
		String strTalkInfo = st.nextToken(); 	// 得到聊天内容;
		String strSender = st.nextToken(); 	// 得到发消息人
		String strReceiver = st.nextToken(); 	// 得到接收人
System.out.println("[TALK_" + strReceiver + "] " + strTalkInfo);
		Socket socketSend;
		PrintWriter outSend;

		// 当前时间
		GregorianCalendar calendar = new GregorianCalendar();
		String strTime = "(" + calendar.get(Calendar.HOUR) + ":"+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND) + ")";
		strTalkInfo += strTime;
		log(strSender + "@" + strReceiver + "@全体成员：  " + strTalkInfo);

		// 发送对象
		if (strReceiver.equals("All")) {
			sendAll("talk|" + strSender + strTalkInfo);
		} else {
			if (strSender.equals(strReceiver)) {
				out.println("talk|>>> 系统提示：您不能给自己发消息");
			} else {
				for (int i = 0; i < UserName.size(); i++) {
					if (strReceiver.equals(UserName.elementAt(i))) {
						socketSend = (Socket) UserChannel.elementAt(i);
						outSend = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(socketSend.getOutputStream())), true);
						outSend.println("talk|" + strSender + " @你：  " + strTalkInfo);
					} else if (strSender.equals(UserName.elementAt(i))) {
						socketSend = (Socket) UserChannel.elementAt(i);
						outSend = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(socketSend.getOutputStream())), true);
						outSend.println("talk|你@ " + strReceiver + "：  " + strTalkInfo);
					}
				}
			}
		}
	}

	// 群聊功能
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
System.out.println("[ERROR] 消息发送失败");
		}
	}

	public void log(String log) {
		String newlog = sFrame.taLog.getText() + "\n" + log;
		sFrame.taLog.setText(newlog);
	}

	//退出后删除用户
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