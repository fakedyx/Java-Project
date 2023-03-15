package Gobang;

import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client_talk extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Client_qipan qipan;
	public static boolean connect = false; // 给出连接服务器的boolean，true表示连接成功了
	static TextArea chatAear; // 所有消息内容区域
	public static Socket socket; // 连接服务器的socket = new
	// Socket(serverip.getText(), 5555);
	private String message = "ok"; // 要发送给服务器的内容
	Send_message sendmessage; // 发送的方法
	private String who = "客户端说:";
	private JTextField txt_message; // 将要发送给服务器的内容
	private JTextField serverip; // 服务器的IP地址
	static Client_talk cilentPanelWestTalk = new Client_talk();

	public static Client_talk getCilent_panel_west_talk() {
		return cilentPanelWestTalk;
	}

	public Client_talk() {
		socket = new Socket();
		sendmessage = new Send_message(message, socket);
		chatAear = new TextArea("----------------------\n", 20, 40);
		txt_message = new JTextField("请输入你要发送的消息");
		JButton jb_connectserver = new JButton("连接服务器");
		serverip = new JTextField("192.168.31.251");
		JButton jb_send = new JButton("发送");
		MouseAdapter connect = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if (connectserver()) {
					JOptionPane.showMessageDialog(null, "\n我成功连接!!!!");
					message = "CHANT" + "-" + "OK" + "-" + "我是客户端，我已经连接成功了" + "\n";
					System.out.println("客户端的message为" + message);
					// 客户端连接服务器后才打开线程
					Recive_thred rth = new Recive_thred(socket, chatAear, Client_frame.qipan);
					rth.start();
					sendmessage.send(message, socket);
				}
			}

		};
		// 给服务器发送消息按钮
		MouseAdapter send = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				chatAear.append("\n客户端说:" + txt_message.getText());
				message = "CHANT" + "-" + who + txt_message.getText() + "\n";
				System.out.println("客户端要发送的的message为" + message);
				sendmessage.send(message, socket);
			}
		};
		this.add(serverip);
		jb_connectserver.addMouseListener(connect);
		this.add(jb_connectserver);

		jb_send.addMouseListener(send);
		this.add(chatAear);
		this.add(txt_message);
		this.add(jb_send);

	}

	/*
	 * 连接服务器
	 */
	public boolean connectserver() {
		try {
			// 获取输入的IP地址，并且进行连接
			socket = new Socket(serverip.getText(), 12345);
			chatAear.append("\n连接服务器成功！");
			connect = true;
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			chatAear.append("\n连接失败");
		}
		return false;
	}

}
