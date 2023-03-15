package PlayBack;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.*;

import Component.BackGroundPanel;
import Gobang.Client_frame;
import Gobang.Server_frame;
import MainContact.MainInterface;
import Util.ScreenUtils;
import javax.swing.ImageIcon;

public class Watch extends JPanel {
	JFrame jf = new JFrame("回放");
	final int WIDTH = 1200;
	final int HEIGHT = 800;

	// 棋子
	BufferedImage table;
	BufferedImage black;
	BufferedImage white;

	// 棋盘大小
	final int TABLE_WIDTH = 437;
	final int TABLE_HEIGHT = 440;

	// 棋盘容量
	final int BOARDSIZE = 16;

	// 每个棋子占用比例
	final int RATE = TABLE_WIDTH / BOARDSIZE - 2;

	// 回放第几颗棋子
	int COUNT = 0;
	int TOTAL = 0;

	// 记录棋子x方向和y方向的偏移
	final int X_OFFSET = 385;
	final int Y_OFFSET = 130;

	// 声明二维数组 [i][j] = 0-无棋子 1-白棋 2-黑棋
	int[][] board = new int[BOARDSIZE][BOARDSIZE];

	JPanel p = new JPanel();
	JButton fh = new JButton("主界面");
	JButton zt = new JButton("上一步");
	JButton hf = new JButton("下一步");
	JButton ex = new JButton("退出");

	int[][] array = new int[16][16];

	private class ChessBoard extends Canvas {
		@Override
		public void paint(Graphics g) {
			// 绘图
			// 绘制棋盘
			g.drawImage(table, 0, 0, null);
			board[15][15] = 1;
			// board[10][2] = 2;
			// 绘制棋子
			/*
			 * if(board[i][j] == 2) {
			 * g.drawImage(black, i*RATE+X_OFFSET, j*RATE+Y_OFFSET, null);
			 * }
			 * //绘制白棋
			 * if(board[i][j] == 1) {
			 * g.drawImage(white, i*RATE+X_OFFSET, j*RATE+Y_OFFSET, null);
			 * }
			 */

			int[] tmp = new int[3];
			int i;
			int j;
			for (int k = 0; k < COUNT; k++) {
				tmp = array[k];
				i = tmp[1];
				j = tmp[2];
				board[i][j] = tmp[0];
				if (board[i][j] == 2) {
					g.drawImage(black, i * RATE + X_OFFSET, j * RATE + Y_OFFSET, null);
				}
				// 绘制白棋
				if (board[i][j] == 1) {
					g.drawImage(white, i * RATE + X_OFFSET, j * RATE + Y_OFFSET, null);
				}
			}
			/*
			 * for(int i = 0; i < BOARDSIZE; i++) {
			 * for(int j = 0; j < BOARDSIZE; j++) {
			 * //绘制黑棋
			 * if(board[i][j] == 2) {
			 * g.drawImage(black, i*RATE+X_OFFSET, j*RATE+Y_OFFSET, null);
			 * }
			 * //绘制白棋
			 * if(board[i][j] == 1) {
			 * g.drawImage(white, i*RATE+X_OFFSET, j*RATE+Y_OFFSET, null);
			 * }
			 * }
			 * }
			 */
		}
	}

	ChessBoard chessBoard = new ChessBoard();

	public Watch() throws IOException {

		try {
			new FileReaderTest().MyRead();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 初始化图片
		jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH,
				HEIGHT);
		// 不改变大小
		jf.setResizable(false);

		table = ImageIO.read(new File("img/Board.png"));
		white = ImageIO.read(new File("img/white.gif"));
		black = ImageIO.read(new File("img/black.gif"));

		Dimension presize = new Dimension(100, 30);
		fh.setPreferredSize(presize);
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
				}
				jf.dispose();
			}
		});

		Dimension presize1 = new Dimension(100, 30);
		zt.setPreferredSize(presize1);
		zt.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2 + 100, 650, 80, 40);

		zt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 跳转
				// JOptionPane.showMessageDialog(jf,"Ji");
				if (COUNT > 0) {
					COUNT--;
					chessBoard.repaint();
				} else {
					JOptionPane.showMessageDialog(jf, "这是第一步棋");
				}
			}
		});

		Dimension presize2 = new Dimension(100, 30);
		hf.setPreferredSize(presize2);
		hf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2 + 200, 650, 80, 40);

		hf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 跳转
				// JOptionPane.showMessageDialog(jf,"Ji");
				if (COUNT < TOTAL) {
					COUNT++;
					chessBoard.repaint();
				} else {
					JOptionPane.showMessageDialog(jf, "这是最后一步棋");
				}
			}
		});

		Dimension presize3 = new Dimension(100, 30);
		ex.setPreferredSize(presize3);
		ex.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2 + 300, 650, 80, 40);

		ex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 跳转
				// JOptionPane.showMessageDialog(jf,"Ji");
				System.exit(0);
			}
		});

		p.add(fh);
		p.add(zt);
		p.add(hf);
		p.add(ex);
		jf.add(p, BorderLayout.SOUTH);

		chessBoard.setPreferredSize(new Dimension(1200, 770));
		jf.add(chessBoard);

		jf.pack();
		jf.setVisible(true);
	}

	/*
	 * public Watch() throws IOException {
	 * 
	 * //设置窗口属性
	 * jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.
	 * getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
	 * //不改变大小
	 * jf.setResizable(false);
	 * 
	 * 
	 * table = ImageIO.read(new File("img/Board.png"));
	 * white = ImageIO.read(new File("img/white.gif"));
	 * black = ImageIO.read(new File("img/black.gif"));
	 * 
	 * Box vBox = Box.createVerticalBox();
	 * 
	 * Box cBox = Box.createHorizontalBox();
	 * 
	 * Box cBox2 = Box.createHorizontalBox();
	 * JButton fh = new JButton("主界面");
	 * Dimension presize = new Dimension(60,30);
	 * fh.setPreferredSize(presize);
	 * fh.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2 ,450, 80, 40);
	 * 
	 * fh.addActionListener(new ActionListener(){
	 * 
	 * @Override
	 * public void actionPerformed(ActionEvent e) {
	 * //跳转
	 * //JOptionPane.showMessageDialog(jf,"Ji");
	 * try {
	 * new MainInterface();
	 * } catch (IOException e1) {
	 * // TODO Auto-generated catch block
	 * e1.printStackTrace();
	 * }
	 * jf.dispose();
	 * }
	 * });
	 * 
	 * JButton zt = new JButton("暂停");
	 * Dimension presize1 = new Dimension(60,30);
	 * zt.setPreferredSize(presize1);
	 * zt.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2 + 100, 650, 80, 40);
	 * 
	 * zt.addActionListener(new ActionListener(){
	 * 
	 * @Override
	 * public void actionPerformed(ActionEvent e) {
	 * //跳转
	 * //JOptionPane.showMessageDialog(jf,"Ji");
	 * System.exit(0);
	 * }
	 * });
	 * 
	 * JButton hf = new JButton("恢复");
	 * Dimension presize2 = new Dimension(60,30);
	 * hf.setPreferredSize(presize2);
	 * hf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2 + 200, 650, 80, 40);
	 * 
	 * hf.addActionListener(new ActionListener(){
	 * 
	 * @Override
	 * public void actionPerformed(ActionEvent e) {
	 * //跳转
	 * //JOptionPane.showMessageDialog(jf,"Ji");
	 * chessBoard.repaint();
	 * }
	 * });
	 * 
	 * JButton ex = new JButton("退出");
	 * Dimension presize3 = new Dimension(60,30);
	 * ex.setPreferredSize(presize3);
	 * ex.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2 + 300 , 650, 80, 40);
	 * 
	 * ex.addActionListener(new ActionListener(){
	 * 
	 * @Override
	 * public void actionPerformed(ActionEvent e) {
	 * //跳转
	 * //JOptionPane.showMessageDialog(jf,"Ji");
	 * System.exit(0);
	 * }
	 * });
	 * 
	 * 
	 * 
	 * vBox.add(Box.createVerticalStrut(120));
	 * cBox.add(cImg);
	 * vBox.add(cBox);
	 * vBox.add(Box.createVerticalStrut(50));
	 * cBox2.add(fh);
	 * cBox2.add(Box.createHorizontalStrut(20));
	 * cBox2.add(zt);
	 * cBox2.add(Box.createHorizontalStrut(20));
	 * cBox2.add(hf);
	 * cBox2.add(Box.createHorizontalStrut(20));
	 * cBox2.add(ex);
	 * vBox.add(cBox2);
	 * bgPanel.add(vBox);
	 * 
	 * //chessBoard.setPreferredSize(new Dimension(TABLE_WIDTH,TABLE_HEIGHT));
	 * 
	 * 
	 * jf.add(bgPanel);
	 * jf.setVisible(true);
	 * }
	 */

	private class FileReaderTest {
		public void MyRead() throws IOException {
			FileReader reader = null;
			try {
				reader = new FileReader("res.txt");
				// 开始读
				BufferedReader br = new BufferedReader(reader);
				String line = null;
				String[] buf = null;
				while ((line = br.readLine()) != null) {
					buf = line.split(",");
					for (int i = 0; i < 3; i++) {
						array[TOTAL][i] = Integer.parseInt(buf[i]);
					}
					TOTAL++;
				}
				/*
				 * int[] tmp = array.get(3);
				 * System.out.println(tmp[2]);
				 */
				br.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			new Watch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
