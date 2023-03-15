package Component;

import java.awt.*;
import javax.swing.*;

public class BackGroundPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//声明图片
	private Image backIcon;
	public BackGroundPanel(Image backIcon) {
		this.backIcon = backIcon;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//绘制背景
		super.paintComponent(g);
		g.drawImage(backIcon,0,0,this.getWidth(),this.getHeight(),null);
	}
}
