package Gobang;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JFrame;


public class Client_frame extends JFrame{
	public static Client_qipan qipan;
	Client_talk talk;
	public Client_frame(){
		qipan=new Client_qipan(); 
		talk=new Client_talk();
		this.setSize(1150, 550);
		//设置关闭
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置居中
	//	this.setLocationRelativeTo(null);
		this.setTitle("五子棋_客户端");
		this.add(qipan,BorderLayout.CENTER); 
		this.add(talk,BorderLayout.WEST);
		
		this.setVisible(true);
	}
}
