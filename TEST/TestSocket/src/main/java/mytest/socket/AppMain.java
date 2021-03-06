package mytest.socket;

import mytest.socket.SocketMsg.SocketState;

public class AppMain {

	private static final String IDENTITY = "624ee1a9-6056-11e7-84dc-005056208505";
	private static final String[] arrayId = { "2d8e98c9-8643-11e7-8931-005056208505",
			"21fd1928-c5e4-11e7-8426-005056208505", "127ff326-e493-11e7-b4e7-005056208505",
			"13d91a66-c5e4-11e7-8426-005056208505" };
	private static SocketProcessClient spc;

	public static void main(String args[]) {
		try {
			spc = new SocketProcessClient("192.168.10.230", 9999, IDENTITY);
			
			// 接收socket
			//accept_test();

			// 发送socket登录
			send_test();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static void accept_test() throws Exception {
		while (true) {
			String serverinfo = spc.clientAccept();
			System.out.println(serverinfo);
			if (spc.socket.isClosed())
				break;
		}
	}

	private static void send_test() throws Exception {
		// 发送socket上线
		for (int i = 0; i < arrayId.length; i++) {
			SendData sd = new SendData();
			sd.setResult("0");
			sd.setType("login");
			String ret = spc.clientSend(arrayId[i], sd, true, SocketState.process);
			System.out.println(ret);
			Thread.sleep(10000);
		}
		// 发送socket离线
		System.in.read();
		for (int i = 0; i < arrayId.length; i++) {
			SendData sd = new SendData();
			sd.setResult("0");
			sd.setType("logout");
			String ret = spc.clientSend(arrayId[i], sd, false, SocketState.process);
			System.out.println(ret);
			Thread.sleep(10000);
		}
	}
}
