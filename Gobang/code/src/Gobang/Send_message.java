package Gobang;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;


public class Send_message {
	Socket socket;
	String message;

	public Send_message(String message, Socket socket) {
		this.message = message;
		this.socket = socket;
	}

	public void send(String message, Socket socket) {
		OutputStream os;
		try {
			os = socket.getOutputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
			System.out.println("send类里面要发送给对方的消息是"+message);	
			//用输出流把它给输出去
			bw.write(message);
			//令起一行
			bw.newLine();
			bw.flush(); // 清空缓存
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
