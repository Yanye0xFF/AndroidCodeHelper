package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

import utils.Resources;


public class RoundBorder implements Border {

	private Color color=null;
	
	public RoundBorder() {
		//默认深灰色边框
		this.color=Resources.COLOR_GRAY_DARKER;
	}
	
	public RoundBorder(Color color) {
		if(color!=null) {
			this.color = color;
		}else {
			this.color=Resources.COLOR_GRAY_DARKER;
		}
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(0, 0, 0, 0);
	}

	public boolean isBorderOpaque() {
		return false;
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,int height) {
		g.setColor(color);
		g.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 15, 15);
	}
}
