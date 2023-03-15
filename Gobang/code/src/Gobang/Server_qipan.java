package Gobang;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import MainContact.MainInterface;

public class Server_qipan extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image board, black, white;
	int hgex, hgey; // 棋子坐标的变量
	int bgex, bgey; // 棋子坐标的变量
	int cou = 0; // 表示连着棋子的数量

	Send_message sendmessage; // 发送的方法
	String message; // 要发送的消息
	public static String control;
	public static boolean isgamestart = false; // 游戏开始？？
	public static boolean myselfok = false; // 自己准备好了？？？
	public static boolean duifangok = false; // 对方准备好了？？
	public static boolean ismyselfxiaqi = false; // 自己下棋了？？？为true表示下了false表示没下，到自己
	public static boolean isduifangxiaqi = true; // 对方下棋了???
	// 为true表示对方下了，false表示没下
	private MouseEvent xiaqie;
	public static ArrayList<String> myself_list = new ArrayList<String>(); // 黑
	public static ArrayList<String> duifang_list = new ArrayList<String>(); // 白

	public Server_qipan() {

		sendmessage = new Send_message(message, Client_talk.socket);

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
		hgex = 0;
		hgey = 0;
		bgex = 0;
		bgey = 0;
		try {
			board = ImageIO.read(new File("img/Board.gif"));
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
				if (Server_frame.isstart) {
					// System.out.println("\n-----\nisgamestart=" + isgamestart
					// + "\nismyselfxiaqi=" + ismyselfxiaqi
					// + "\nisduifangxiaqi=" + isduifangxiaqi
					// + "\nismyselfok=" + myselfok + "\nduifangok="
					// + duifangok);
					if (isgamestart) {
						xiaqie = e; // 将鼠标点的位置赋值给全局变量
						getmouse(xiaqie);
					} else {
						Server_talk.chantAear
								.append("\n---系统消息双方至少有一方没有开始游戏---");
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先开启服务器！！");
				}
			}
		};
		// 鼠标监听器--------------开始/暂停
		MouseAdapter start = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				// System.out.println("kaishi鼠标按下去了");
				if (Server_frame.isstart) {
					myselfok = true; // 表示自己准好了
					// 发送消息给对方说自己准备好了
					message = "ANNIU" + "-" + "SERVER" + "-" + "START";
					sendmessage.send(message, Server_frame.socket);
					System.out.println("duifangok=" + duifangok + "myselfok="
							+ myselfok);
					if (!duifangok) {
						Server_talk.chantAear
								.append("\n---系统消息：对方还没有点击开始，请等待---");
					} else {
						isgamestart = true;
						Server_talk.chantAear
								.append("\n---双方准备就绪，游戏即将开始，服务器先下---");
					}

				} else {
					Server_talk.chantAear
							.append("\n---系统消息：服务器还没开启，请先开启服务器---");
				}
			}
		};
		MouseAdapter renshu = new MouseAdapter() {
			@Override

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if (Server_frame.isstart) {
					if (isgamestart) {
						// System.out.println("renshu鼠标按下去了");
						message = "ANNIU" + "-" + "SERVER" + "-" + "TOUXIANG";
						sendmessage.send(message, Server_frame.socket);
						isgamestart = false;
						Save();
						myself_list.clear();
						duifang_list.clear();
						repaint();
					} else {
						Server_talk.chantAear.append("\n---系统消息：游戏还没开始---");
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先开启服务器！！");
				}
			}

		};
		// 后退
		MouseAdapter houtui = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if (Server_frame.isstart) {
					if (isgamestart) {
						if (!ismyselfxiaqi && isduifangxiaqi) {
							Server_talk.chantAear
									.append("\n---系统消息：到自己下棋的时候，不能悔棋---");
						} else {

							ismyselfxiaqi = false;
							isduifangxiaqi = true;
							myself_list.remove(myself_list.get(myself_list
									.size() - 1));// 后退
							Server_talk.chantAear
									.append("\n---系统消息：对方毁了一步棋！---");
							message = "ANNIU" + "-" + "SERVER" + "-" + "HUIQI";
							sendmessage.send(message, Server_frame.socket);
							repaint();
						}
					} else {
						Server_talk.chantAear.append("\n---系统消息：游戏还没开始---");
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先开启服务器！！");
				}

			}
		};
		// 重新开始
		MouseAdapter chongxinkaishi = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				if (Server_frame.isstart) {
					if (isgamestart) {
						Save();
						myself_list.clear();
						duifang_list.clear();
						message = "ANNIU" + "-" + "SERVER" + "-"
								+ "CHONGXINLAI";
						sendmessage.send(message, Server_frame.socket);
						repaint();
					} else {
						Server_talk.chantAear.append("\n---系统消息：游戏还没开始---");
					}
				} else {
					JOptionPane.showMessageDialog(null, "请先开启服务器！！");
				}
			}
		};
		this.addMouseListener(mouse);
		cxks.addMouseListener(chongxinkaishi);
		ks.addMouseListener(start); // 开始按钮与鼠标相连
		rs.addMouseListener(renshu); // 认输按钮与鼠标相连
		ht.addMouseListener(houtui); // 后退按钮与鼠标相连
		this.Save();
	}

	/*
	 * 获取鼠标位置 保存鼠标位置到数集里面
	 */
	public void getmouse(MouseEvent e) {
		if (!ismyselfxiaqi && isduifangxiaqi) {
			// isgamestart表示游戏开始，才能在棋盘里面下棋
			// if(isgamestart){
			int x = e.getX();
			int y = e.getY();
			// 减去边距，除以格子的宽度，取整，得到第几格子
			hgex = (x - 18) / 25;
			hgey = (y - 18) / 25;
			if ((x - 18) % 25 > 12)
				hgex = hgex + 1;
			if ((y - 18) % 25 > 12)
				hgey = hgey + 1;
			// 把格子的x坐标和y坐标变成字符串用逗号连接，保存到list里面
			if (!myself_list.contains(hgex + "," + hgey)
					&& !duifang_list.contains(hgex + "," + hgey)) {
				myself_list.add(hgex + "," + hgey);
				// 自己下完了就不能下，要等对方下了之后才能下
				ismyselfxiaqi = true; // 自己为false表示自己没下，到自己了
				isduifangxiaqi = false; // 对方为true，表示对方刚下了
				// 将下的棋子发送给对方
				message = "XIAQI" + "-" + hgex + "," + hgey + "-" + "SERVER";
				System.out.println("服务器的坐标：" + message);
				sendmessage.send(message, Server_frame.socket);
			}
			repaint();
			// if (myself_list.size() >= 5) {
			heipanduan();
			// }
		}
		// if (ismyselfxiaqi && !isduifangxiaqi) {
		// JOptionPane.showMessageDialog(null, "请等待对方下完，在继续！！");
		// }
	}

	/*
	 * 
	 * 画棋盘
	 */
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(board, 0, 0, null);
		// 黑棋获取鼠标点的位置,画黑棋
		for (int i = 0; i < myself_list.size(); i++) {
			String s = myself_list.get(i); // 获得集合里面的单个字符串
			String[] a = s.split(",");
			hgex = Integer.parseInt(a[0]);
			hgey = Integer.parseInt(a[1]);
			g.drawImage(black, hgex * 25 + 18 - 12, hgey * 25 + 18 - 12, null);
		}
		// 白棋获取鼠标点的位置，画白棋
		for (int i = 0; i < duifang_list.size(); i++) {
			String s = duifang_list.get(i); // 获得集合里面的单个字符串
			String[] b = s.split(",");
			bgex = Integer.parseInt(b[0]);
			bgey = Integer.parseInt(b[1]);
			g.drawImage(white, bgex * 25 + 18 - 12, bgey * 25 + 18 - 12, null);
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
				bf.write("2" + "," + myself_list.get(i));
				bf.newLine();
				bf.flush();
				bf.write("1" + "," + duifang_list.get(j));
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

	// 输赢判断的方法,黑判断
	public void heipanduan() {
		// if (h_list.size() > 5) {// 黑棋至少点了五次之后才执行
		// f判断是白棋还是黑棋,true表示黑棋,false表示白棋
		// 黑棋判断输赢
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
				JOptionPane.showMessageDialog(null, "恭喜！你赢啦");
				isgamestart = false;
				// 给对方发送自己赢的消息

				message = "XIAQI" + "-" + "WIN" + "-" + "SERVER";
				sendmessage.send(message, Server_frame.socket);

			}
		}
	}

}
