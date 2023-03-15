package MainContact;

import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import Component.BackGroundPanel;
import Gobang.Client_frame;
import Gobang.Server_frame;
import PlayBack.Watch;
import Util.ScreenUtils;

public class MainInterface {
	JFrame jf = new JFrame("五子棋");
	
	final int WIDTH = 1200;
	final int HEIGHT = 800;
	
	//组装视图
	public MainInterface() throws IOException {
		//设置窗口属性
		jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
		//不改变大小
		jf.setResizable(false);
		//设置logo
		jf.setIconImage(ImageIO.read(new File("img/logo.jpg")));
		//背景图
		BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(new File("img/land.jpg")));
		bgPanel.setBounds(0,0,WIDTH,HEIGHT);
		
		Box vBox = Box.createVerticalBox();
		
		/*
		JLabel jl = new JLabel("墨攻棋阵",JLabel.CENTER);
		//jl.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2, 50, 100,100);
		jl.setFont(new Font("华文行楷",1,100));
		jl.setForeground(Color.white);
		*/
		
		//组装对战
		JButton cp = new JButton("服务器");
		Dimension presize = new Dimension(200,200);
		cp.setPreferredSize(presize);
		cp.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2 + 200, 250, 200, 200);
		
		cp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//跳转
				//JOptionPane.showMessageDialog(jf,"Ji");
				new Server_frame();
				jf.dispose();
			}
		});
		
		//客服端
		JButton se = new JButton("客服端");
		se.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//跳转
				//JOptionPane.showMessageDialog(jf,"Ji");
				new Client_frame();
				jf.dispose();
			}
		});
		
		//组装加载
		JButton ld = new JButton("加载旧局");
		
		//组装回放
		JButton hk = new JButton("回放比赛");
		hk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//跳转
				//JOptionPane.showMessageDialog(jf,"Ji");
				try {
					new Watch();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jf.dispose();
			}
		});
		
		//组装退出
		JButton ex = new JButton("退出游戏");
		ex.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//跳转
				//JOptionPane.showMessageDialog(jf,"Ji");
				System.exit(0);
			}
		});
		
		vBox.add(Box.createVerticalStrut(250));
		vBox.add(cp);
		vBox.add(Box.createVerticalStrut(50));
		vBox.add(se);
		vBox.add(Box.createVerticalStrut(50));
		vBox.add(ld);
		vBox.add(Box.createVerticalStrut(50));
		vBox.add(hk);
		vBox.add(Box.createVerticalStrut(50));
		vBox.add(ex);
		bgPanel.add(vBox);
		
		jf.add(bgPanel);
		jf.setVisible(true);
	}

	
	public static void main(String[] args) {
		try {
			new MainInterface();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
