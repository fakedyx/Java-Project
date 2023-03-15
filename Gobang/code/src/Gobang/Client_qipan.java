package Gobang;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.BufferedWriter;
import MainContact.MainInterface;

public class Client_qipan extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image board, black, white;
	int getx = 0, gety = 0; // 获取自己的zuo
	static boolean xiaqi = true; // xiaqu=true，黑棋下，否则不能下白棋下
	int cou = 0; // 表示连着棋子的数量
	String message = "ok"; // 发送的消息
	Send_message sendmessage; // 发送的方法
	int hgex = 0, hgey = 0;
	public static String control; // 按钮控制
	public static boolean isgamestart = false;// true游戏开始
	public static boolean myselfok = false; // 为true表示自己准备好了
	public static boolean duifangok = false; // 为true表示对方准备好了
	public static boolean ismyselfxiaqi = true; // 自己下了？？？ishei=true表示下了false表示没下，到自己
	public static boolean isduifangxiaqi = false; // 对方下了的 为true表示对方下了，false表示没下
	private MouseEvent xiaqie; // 下白棋的时候，白棋点的鼠标位置的全局变量
	public static ArrayList<String> myself_list = new ArrayList<String>(); // 自己下棋的数集
	public static ArrayList<String> duifang_list = new ArrayList<String>(); // 对方下棋的数集

	public Client_qipan() {
		// 创建一个对象，并且实例化
		System.out.println(Client_talk.socket + "构造方法里的socket");
		sendmessage = new Send_message(message, Client_talk.socket);
		System.out.println(sendmessage + "构造方法里的sendmessage");
		this.setLayout(null); // 居中
		JButton ks = new JButton("开始/暂停");
		ks.setBounds(10, 460, 100, 25);
		this.add(ks);
		JButton rs = new JButton("认输");
		rs.setBounds(115, 460, 80, 25);
		this.add(rs);
		JButton ht = new JButton("后退");
		ht.setBounds(200, 460, 80, 25);
		this.add(ht);
		JButton cxks = new JButton("重新开始");
		cxks.setBounds(290, 460, 100, 25);
		this.add(cxks);
		JButton fh = new JButton("主界面");
		fh.setBounds(400, 460, 100, 25);
		this.add(fh);
		fh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 跳转
				// JOptionPane.showMessageDialog(jf,"Ji");
				try {
					new MainInterface();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					setVisible(false);
				}
			}
		});

		try {
			board = ImageIO.read(new File("img/board.gif"));
			black = ImageIO.read(new File("img/black.gif"));
			white = ImageIO.read(new File("img/white.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 鼠标监听器，下棋位置
		MouseAdapter mouse = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				// System.out.println("xiaqi鼠标按下去了");
				if (Client_talk.connect) {
					System.out.println("\n-----\nisgamestart=" + isgamestart
							+ "\nismyselfxiaqi=" + ismyselfxiaqi
							+ "\nisduifangxiaqi=" + isduifangxiaqi
							+ "\nismyselfok=" + myselfok + "\nduifangok="
							+ duifangok);
					if (isgamestart) {
						xiaqie = e; // 将鼠标点的位置赋值给全局变量
						getmouse(xiaqie);
					} else {

						Client_talk.chatAear
								.append("\n---系统消息双方至少有一方没有开始游戏---");
					}
				} else {
					JOptionPane.showMessageDialog(null, "还没连接服务器！！");
				}

			}

		};
		// 鼠标监听器--------------开始/暂停
		MouseAdapter start = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if (Client_talk.connect) {
					myselfok = true; // 表示自己准好了
					// 发送消息给对方说自己准备好了
					message = "ANNIU" + "-" + "CLIENT" + "-" + "START";
					sendmessage.send(message, Client_talk.socket);
					System.out.println("duifangok=" + duifangok + "myselfok="
							+ myselfok);
					if (!duifangok) {

						Client_talk.chatAear
								.append("\n---系统消息：对方还没有准备好，请等待对方开始游戏！---");
					} else if (duifangok) {
						isgamestart = true;
						Client_talk.chatAear
								.append("\n---系统消息：双方准备就绪，游戏即将开始,对方先下，请等待。。。---");
					}

				} else {
					Client_talk.chatAear.append("\n---系统消息：请先连接服务器！---");
				}
			}
		};
		MouseAdapter renshu = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				// System.out.println("renshu鼠标按下去了");
				if (Client_talk.connect) {
					if (isgamestart) {

						if (myself_list.size() < 5) {
							Client_talk.chatAear
									.append("\n----系统消息：你还没走到五步就投降了！---");
						}
						// 清除保存的数集
						Save();
						myself_list.clear();
						duifang_list.clear();
						message = "ANNIU" + "-" + "CLIENT" + "-" + "TOUXIANG";
						sendmessage.send(message, Client_talk.socket);
						repaint();
					} else {
						Client_talk.chatAear.append("\n----系统消息：你还没开始游戏！---");
					}
				} else {
					Client_talk.chatAear.append("\n----系统消息：你还没有连接服务器啊！---");
				}
			}
		};
		// 后退
		MouseAdapter houtui = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if (Client_talk.connect) {
					if (isgamestart) {
						if (!ismyselfxiaqi && isduifangxiaqi) {
							Client_talk.chatAear
									.append("\n---系统到自己下棋的时候，不能悔棋---");
						} else {
							myself_list.remove(myself_list.get(myself_list
									.size() - 1));// 后退
							isduifangxiaqi = true;
							ismyselfxiaqi = false;
							message = "ANNIU" + "-" + "CLIENT" + "-" + "HUIQI";
							sendmessage.send(message, Client_talk.socket);
							repaint();
						}
					} else {
						Client_talk.chatAear.append("\n---系统消息：你还没开始游戏！---");
					}
				} else {
					Client_talk.chatAear.append("\n---系统消息：你还没连接服务器啊！---");
				}
			}
		};
		// 重新开始
		MouseAdapter chongxinkaishi = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if (Client_talk.connect) {
					if (isgamestart) {
						Save();
						myself_list.clear();
						duifang_list.clear();
						message = "ANNIU" + "-" + "CLIENT" + "-"
								+ "CHONGXINLAI";
						sendmessage.send(message, Client_talk.socket);
						repaint();
					} else {
						Client_talk.chatAear.append("\n---系统消息：游戏还未开始！--");
					}
				} else {
					Client_talk.chatAear.append("\n---系统消息：服务器还没开启！--");
				}
			}
		};
		this.Save();
		this.addMouseListener(mouse);
		ks.addMouseListener(start); // 开始按钮与鼠标相连
		rs.addMouseListener(renshu); // 认输按钮与鼠标相连
		ht.addMouseListener(houtui); // 后退按钮与鼠标相连
		cxks.addMouseListener(chongxinkaishi);
	}

	/*
	 * 
	 * 获取鼠标位置 保存鼠标位置到数集里面
	 */

	public void getmouse(MouseEvent e) {

		System.out.println("你刚刚点了一下客户端的鼠标");
		for (int i = 0; i < Client_qipan.duifang_list.size(); i++) {
			System.out.println();
			System.out.println("点了鼠标之后就打印对方棋盘数集里面的东西"
					+ Client_qipan.duifang_list.get(i)); // 获得集合里面的单个字符串}
		}
		System.out.println("打印出棋盘里面的数据之后，ismyselfxiaqi=" + ismyselfxiaqi
				+ "===" + "isduifangxiaqi" + isduifangxiaqi);
		if (!ismyselfxiaqi && isduifangxiaqi) {
			int x = e.getX();
			int y = e.getY();
			// 减去边距，除以格子的宽度，取整，得到第几格子
			getx = (x - 18) / 25;
			gety = (y - 18) / 25;
			if ((x - 18) % 25 > 12)
				getx = getx + 1;
			if ((y - 18) % 25 > 12)
				gety = gety + 1;
			// 把格子的x坐标和y坐标变成字符串用逗号连接，保存到list里面
			if (!myself_list.contains(getx + "," + gety)
					&& !duifang_list.contains(getx + "," + gety)) {
				myself_list.add(getx + "," + gety);
				// 自己下完了就不能下，要等对方下了之后才能下
				ismyselfxiaqi = true; // 自己为false表示自己没下，到自己了
				isduifangxiaqi = false; // 对方为true，表示对方刚下了
				// 将下的棋子发送给对方
				message = "XIAQI" + "-" + getx + "," + gety + "-" + "CLIENT";
				System.out.println("服务器的坐标：" + message);
				Client_talk
						.getCilent_panel_west_talk();
				// System.out.println("\n" + Client_talk.socket);
				// System.out.println(sendmessage + "ahfjskjfk");
				sendmessage.send(message, Client_talk.socket);
			}
			repaint();
			if (myself_list.size() > 5)
				baipanduan();
		}
		// if (ismyselfxiaqi && !isduifangxiaqi) {
		// JOptionPane.showMessageDialog(null, "请等待对方下完，在继续！！");
		// }
	}

	/*
	 * 画棋盘
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(board, 0, 0, null);
		// 画自己
		for (int i = 0; i < myself_list.size(); i++) {
			String s = myself_list.get(i); // 获得集合里面的单个字符串
			String[] a = s.split(",");
			getx = Integer.parseInt(a[0]);
			gety = Integer.parseInt(a[1]);
			g.drawImage(white, getx * 25 + 18 - 12, gety * 25 + 18 - 12, null);
		}
		// 画对方
		for (int i = 0; i < duifang_list.size(); i++) {
			String s = duifang_list.get(i); // 获得集合里面的单个字符串
			String[] b = s.split(",");
			getx = Integer.parseInt(b[0]);
			gety = Integer.parseInt(b[1]);
			g.drawImage(black, getx * 25 + 18 - 12, gety * 25 + 18 - 12, null);
		}
	}

	public void Save() {
		try {
			File file1 = new File("res.txt");
			FileOutputStream os = new FileOutputStream(file1);
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bf = new BufferedWriter(osw);
			int i = 0, j = 0;
			for (i = 0; i < myself_list.size() && j < duifang_list.size(); i++, j++) {
				bf.write("2" + "," + duifang_list.get(i));
				bf.newLine();
				bf.flush();
				bf.write("1" + "," + myself_list.get(j));
				bf.newLine();
				bf.flush();
			}
			bf.close();
			osw.close();
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
		}
	}

	// 输赢判断的方法
	public void baipanduan() {
		// if (h_list.size() > 5) {// 黑棋至少点了五次之后才执行
		// f判断是白棋还是黑棋,true表示黑棋,false表示白棋
		for (int i = 0; i < myself_list.size(); i++) {
			cou = 0; // 在判断每一个棋子的时候，都要给cou赋值为0
			String s = myself_list.get(i); // 获得集合里面的单个字符串
			String[] a = s.split(",");
			hgex = Integer.parseInt(a[0]);
			int tempx = hgex;
			hgey = Integer.parseInt(a[1]);
			int tempy = hgey;
			// 横向判断
			// 黑棋的某个点x坐标+1，Y不变，表示横向判断有没有五子相连
			for (int j = 0; j < 4; j++) {
				hgex++;
				if (myself_list.contains(hgex + "," + hgey)) {
					cou++;
				}
			}
			// 纵向判断
			if (cou < 4) {
				hgex = tempx;
				hgey = tempy;
				cou = 0;
				for (int j = 0; j < 4; j++) {
					hgey++;
					if (myself_list.contains(hgex + "," + hgey))
						cou++;
				}
			}
			// 右上方向判断
			if (cou < 4) { // 小余4表示横向没有五子，就把cou值设为0，重新开始
				cou = 0;
				hgex = tempx;
				hgey = tempy;
				for (int j = 0; j < 4; j++) {
					hgey--;
					hgex++;
					if (myself_list.contains(hgex + "," + hgey))
						cou++;
				}
			}
			// 右下判断
			if (cou < 4) {
				cou = 0;
				hgex = tempx;
				hgey = tempy;
				for (int j = 0; j < 4; j++) {
					hgey++;
					hgex++;
					if (myself_list.contains(hgex + "," + hgey))
						cou++;
				}
			}
			if (cou == 4) {
				isgamestart = false;
				JOptionPane.showMessageDialog(null, "恭喜！你赢啦");
				message = "XIAQI" + "-" + "WIN" + "-" + "CLIENT";
				sendmessage.send(message, Server_frame.socket);
				isgamestart = false;
			}
		}
	}
}
