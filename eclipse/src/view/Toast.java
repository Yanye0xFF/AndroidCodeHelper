package view;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JWindow;

import bean.Pointi;
import bean.WindowPosition;

public class Toast extends JWindow {

	private static final long serialVersionUID = 1L;
	private String message = "";
	private final Insets insets = new Insets(12, 14, 12, 14);
	private int period = 1500;
	private Font font;
	public static final int msg = 0;// 提示 黑色背景色
	public static final int success = 1;// 成功提示 浅蓝色背景色
	public static final int error = 2;// 错误提示 粉红色背景色
	private Color background;
	private Color foreground;
	
	public static final int SHORT=1000;
	public static final int MIDDLE=1500;
	public static final int LONG=3000;
	
	private int textHeight=0;
	
	private int arcSize=30;
	
	/**
	 * 
	 * @param parent
	 *            父窗体 (Frame Dialog Window)
	 * @param message
	 *            消息
	 * @param period
	 *            显示时间
	 */
	public Toast(Window frame,WindowPosition parent, String message, int period) {
		this(frame, parent,message,period, 0);

	}

	/**
	 * 
	 * @param parent
	 * @param message
	 * @param period
	 * @param type
	 *            提示类型 msg:黑色背景色 success :浅蓝色背景色  error: 粉红色背景色
	 */
	public Toast(Window frame,WindowPosition parent, String message, int period, int type) {
		super(frame);
		this.message = message;
		this.period = period;
		Pointi point=new Pointi();
		point.setX((parent.getW()/2+parent.getX()));
		point.setY((parent.getH()/2+parent.getY()));
		font = new Font("宋体", Font.PLAIN, 14);
		Dimension dim=getStringSize(font, true, message);
		setSize(dim);
		this.textHeight=(int)dim.getHeight();
		setLocation(parent.getW()/2+parent.getX(),(parent.getH()-textHeight*3)+parent.getY());
		installTheme(type);

	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// old
		Composite oldComposite = g2.getComposite();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(font);
		FontMetrics fm = g2.getFontMetrics();
		g2.setColor(background);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);
		g2.setColor(foreground);
		g2.drawString(message, insets.left, fm.getAscent() + insets.top);
		// restore
		g2.setComposite(oldComposite);
	}

	/**
	 * 启动提示
	 */
	public void display() {
		this.setVisible(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				setVisible(false);
			}
		}, period);
	}
	
	/**
	 * 设置窗口位置
	 * @param parent 父级窗口位置参数
	 * */
	public void setLoaction(WindowPosition parent) {
		setLocation(parent.getW()/2+parent.getX(),(parent.getH()-textHeight*3)+parent.getY());
	}
	
	/**
	 * 设置窗口位置
	 * @param point toast指定的位置
	 * */
	public void setLoaction(Point point) {
		setLocation(point.x,point.y);
	}
	
	/**
	 * 修改消息
	 * @param message
	 */
	public void setText(String message) {
		this.message = message;
		Dimension size = getStringSize(font, true, message);
		setSize(size);
		revalidate();
		repaint(0, 0, size.width, size.height);
		if (!isVisible()) {
			display();
		}
	}
	
	
	/**
	 * 修改超时
	 * */
	public void setTimeout(int type) {
		this.period=type;
	}
	
	/*
	 * 设置样式
	 */
	private void installTheme(int type) {
		switch (type) {
		case msg:
			background = new Color(0x515151);
			foreground = Color.WHITE;
			break;
		case success:
			background = new Color(223, 240, 216);
			foreground = new Color(49, 112, 143);
			break;
		case error:
			background = new Color(242, 222, 222);
			foreground = new Color(221, 17, 68);
			break;
		default:
			background = new Color(0x515151);
			foreground = Color.WHITE;
			break;
		}
	}

	/**
	 * 得到字符串的宽-高
	 * 
	 * @param font
	 *            字体
	 * @param isAntiAliased
	 *            反锯齿
	 * @param text
	 *            文本
	 * @return
	 */
	private Dimension getStringSize(Font font, boolean isAntiAliased,
			String text) {
		FontRenderContext renderContext = new FontRenderContext(null,
				isAntiAliased, false);
		Rectangle2D bounds = font.getStringBounds(text, renderContext);
		int width = (int) bounds.getWidth() + 2 * insets.left;
		int height = (int) bounds.getHeight() + insets.top * 2;
		return new Dimension(width, height);
	}

}

