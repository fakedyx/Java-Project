package Gobang;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.*;

public class Recive_thred extends Thread {
	private Socket socket;
	TextArea chantAear;
	String who; // who=server表示是从服务器那边发过来的信息
	String[] split;
	int xianxing = 1;
	JPanel jp = new JPanel();

	// public static ArrayList<String> list = new ArrayList<String>(); // 数集

	public Recive_thred(Socket socket, TextArea chantAear, JPanel jp) {
		this.socket = socket;
		this.chantAear = chantAear;
		this.jp = jp;
	}

	public void Save(ArrayList<String> myself_list, ArrayList<String> duifang_list) {
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		InputStream is;

		try {
			is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			while (true) {
				String msg = reader.readLine();
				System.out.println("recive线程类接受到的信息" + msg);
				split = msg.split("-");
				// 聊天控制
				if (split[0].equals("CHANT")) {
					System.out.println(split[1]);
					if (split[1].equals("OK")) {
						chantAear.append("\n" + split[2]);
						Server_frame.clientconnect = true;
					} else {
						chantAear.append("\n" + split[1]);
					}

				} else if (split[0].equals("ANNIU")) {
					System.out.println("这里是线程类里面按钮控制control=" + split[1]);
					anniucontrol();
				}

				else if (split[0].equals("XIAQI"))//
				{
					System.out.println("到下棋来了");
					// 下棋控制
					who = split[2]; // 谁发的啊？？？
					System.out.println("这里是线程中的XIAQI判断");
					xiaqicontrol();

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 
	 * 按钮控制
	 */
	public void anniucontrol() {

		if (split[1].equals("SERVER")) {
			// 收到是从服务器发过来启动的消息,要对客户端里面进行修改
			if (split[2].equals("START")) {
				System.out.println("线程里面，客户端从服务器收到按下开始按钮");
				Client_qipan.duifangok = true;
				if (Client_qipan.myselfok) {
					Client_qipan.isgamestart = true;
					Client_talk.chatAear.append("\n---系统消息：两方准备就绪，服务器先下---");
				}
				jp.repaint();
			}
			// 服务器投降
			if (split[2].equals("TOUXIANG")) {

				Client_talk.chatAear.append("\n---系统消息：对方已经投降，恭喜你赢了---");
				JOptionPane.showMessageDialog(null, "yingle ,gameover");
				// 清除黑棋白棋
				Save(Client_qipan.duifang_list, Client_qipan.myself_list);
				Client_qipan.myself_list.clear();
				Client_qipan.duifang_list.clear();
				Client_qipan.isgamestart = false;
				jp.repaint();

			}
			// 服务器悔棋
			if (split[2].equals("HUIQI")) {
				Client_qipan.duifang_list.remove(Client_qipan.duifang_list
						.get(Client_qipan.duifang_list.size() - 1));// 后退
				Client_talk.chatAear.append("\n---系统消息：对方毁了一步棋！---");
				Client_qipan.isduifangxiaqi = false;
				Client_qipan.ismyselfxiaqi = true;
				jp.repaint();

			}
			// 服务器重新开始
			if (split[2].equals("CHONGXINLAI")) {
				Client_talk.chatAear.append("\n---系统消息：对方要求重新开始!---");
				JOptionPane.showMessageDialog(null, "对方要求开始，确认就清除所有数据，重新开始");
				Save(Client_qipan.duifang_list, Client_qipan.myself_list);
				Client_qipan.myself_list.clear();
				Client_qipan.duifang_list.clear();
				Client_qipan.isgamestart = false;
				Client_qipan.myselfok = true;
				Client_qipan.duifangok = false;
				jp.repaint();
			}

		} else if (split[1].equals("CLIENT")) {
			// 客户端开始
			if (split[2].equals("START")) {
				Server_qipan.isduifangxiaqi = true;
				System.out.println("线程里面，服务器从客户端收到按下开始按钮");
				Server_qipan.duifangok = true;
				if (Server_qipan.myselfok) {
					Server_qipan.isgamestart = true;
					Server_talk.chantAear.append("\n---系统提示：对方准备就绪，游戏开始了，请服务器先下---");
				}
				jp.repaint();
			}
			// 客户端投降
			if (split[2].equals("TOUXIANG")) {
				Server_talk.chantAear.append("\n---系统消息：对方已经投降，恭喜你赢了---");
				JOptionPane.showMessageDialog(null, "yingle ,gameover");
				// 清除黑棋白棋
				Save(Server_qipan.myself_list, Server_qipan.duifang_list);
				Server_qipan.myself_list.clear();
				Server_qipan.duifang_list.clear();
				Server_qipan.isgamestart = false;
				jp.repaint();

			}
			// 客户端悔棋
			if (split[2].equals("HUIQI")) {
				if (Server_qipan.duifang_list.size() > 0)
					Server_qipan.duifang_list.remove(Server_qipan.duifang_list
							.get(Server_qipan.duifang_list.size() - 1));// 后退
				Server_talk.chantAear.append("---系统消息：对方毁了一步棋！---\n");
				Server_qipan.isduifangxiaqi = false;
				Server_qipan.ismyselfxiaqi = true;
				jp.repaint();
			}
			// 客户端重新开始
			if (split[2].equals("CHONGXINLAI")) {
				Server_talk.chantAear.append("\n---系统消息：对方要求重新开始!---");
				JOptionPane.showMessageDialog(null, "对方要求开始，确认就清除所有数据，重新开始");
				Save(Server_qipan.myself_list, Server_qipan.duifang_list);
				Server_qipan.myself_list.clear();
				Server_qipan.duifang_list.clear();
				Server_qipan.isgamestart = false;
				Server_qipan.myselfok = false;
				Server_qipan.duifangok = true;
				jp.repaint();
			}
		}
	}

	/*
	 * 
	 * 下棋控制
	 */
	public void xiaqicontrol() {
		// 谁先下？？？又到谁下了，既然是传值过来，当然是对方下了，然后添加到自己给对方设置的数集里面
		if (who.equals("SERVER")) {
			// 这里 是有服务器发送给客户端，客户端接收，接收一方要改变信息
			if (split[1].equals("WIN")) {
				JOptionPane.showMessageDialog(null, "你输了哦");
				Client_talk.chatAear
						.append("\n---系统消息---\n你已经输了，\n要跟继续跟对方下棋点在点一次开始\n或者认输，或者重新开始");
			} else {
				System.out.println("客户端接收到服务器的split[1]=" + split[1]);
				System.out.println(split[1]);
				System.out.println(split[1]);
				System.out.println(split[1]);
				System.out.println(split[1]);
				String[] zuobiao = split[1].split(",");
				int tempx = Integer.parseInt(zuobiao[0]);
				int tempy = Integer.parseInt(zuobiao[1]);
				// 将服务器发送过来的鼠标坐标里面的值添加到客户端里面的duifang数集里面
				if (!Client_qipan.duifang_list.contains(tempx + "," + tempy)
						&& !Client_qipan.myself_list.contains(tempx + ","
								+ tempy)) {
					// System.out.println("这里是给对方数集赋值");
					Client_qipan.ismyselfxiaqi = false;
					Client_qipan.isduifangxiaqi = true;
					Client_qipan.duifang_list.add(tempx + "," + tempy);
					for (int i = 0; i < Client_qipan.duifang_list.size(); i++) {
						System.out.println();
						System.out.println("对方棋盘数集里面的东西"
								+ Client_qipan.duifang_list.get(i)); // 获得集合里面的单个字符串}
					}
				}
			}
			jp.repaint();
		} else if (who.equals("CLIENT")) {
			// 这里 是有客户端发送给服务器，客户端接收，接收一方要改变信息
			if (split[1].equals("WIN")) {
				JOptionPane.showMessageDialog(null, "你输了哦");
				Client_talk.chatAear
						.append("\n---系统消息---\n你已经输了，\n要跟继续跟对方下棋点在点一次开始\n或者认输，或者重新开始");
			} else {

				System.out.println("服务器接收到客户端的split[1]=" + split[1]);
				System.out.println(split[1]);
				System.out.println(split[1]);
				System.out.println(split[1]);
				System.out.println(split[1]);
				String[] zuobiao = split[1].split(",");
				int tempx = Integer.parseInt(zuobiao[0]);
				int tempy = Integer.parseInt(zuobiao[1]);
				// 将客户端发送过来的鼠标坐标里面的值添加到服务器里面的duifang数集里面
				if (!Server_qipan.myself_list.contains(tempx + "," + tempy)
						&& !Server_qipan.duifang_list.contains(tempx + ","
								+ tempy)) {

					Server_qipan.duifang_list.add(tempx + "," + tempy);
					Server_qipan.ismyselfxiaqi = false;
					Server_qipan.isduifangxiaqi = true;
				}
			}
		}
		jp.repaint();
	}
}
