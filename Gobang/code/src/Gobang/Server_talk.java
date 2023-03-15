package Gobang;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("unused")
public class Server_talk extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String who="服务器说：";
	public static  TextArea chantAear;
	private JTextField txt_message;
	public static String message="ok"; // 要发送客户端的内容
	Send_message sendmessage;		//发送的方法
	public Server_talk(){
			sendmessage = new Send_message(message, Server_frame.socket);
			JButton jb_send = new JButton("发送");
			//this.setLayout(new FlowLayout(FlowLayout.LEFT));
			
			txt_message = new JTextField("请输入你要发的内容");
			//txt_message.setPreferredSize(new Dimension(200,30));
			chantAear = new TextArea("------------------\n", 20, 40);
			MouseAdapter send = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					send(); // 服务在自己的textarea输出信息
				}
			};
			
			jb_send.addMouseListener(send);
			this.add(chantAear);
			this.add(txt_message);
			this.add(jb_send);
	}


	private void send() {
		// TODO Auto-generated method stub
		if (!Server_frame.isstart) {
			JOptionPane.showMessageDialog(null, "服务器还未启动,不能发送消息！", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			String message = txt_message.getText();
			if (message == null || message.equals("")) {
				JOptionPane.showMessageDialog(null, "消息不能为空！", "错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			chantAear.append("\n服务器说：" + txt_message.getText() + "");
			message = "CHANT" + "-" +who+ txt_message.getText() + "\n";
			//System.out.println("服务器要发送的的message为"+message);
			sendmessage.send(message, Server_frame.socket);
		}
	}

}
