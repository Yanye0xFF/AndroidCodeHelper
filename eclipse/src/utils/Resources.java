package utils;

import java.awt.Color;

/**
 * 保存常量资源
 * */
public class Resources {

	public static final int FONT_MAX=24;
	public static final int FONT_MIN=12;
	
	public static Color COLOR_GRAY = new Color(152,152,152);
    public static Color COLOR_GRAY_DARKER = new Color(193,193,193);
    public static Color COLOR_LIGHT_GREEN = new Color(72,172,63);
    public static Color COLOR_GREEN_DARKER = new Color(0,128,64);
    public static Color COLOR_INDIAN_RED = new Color(238,99,99);
    public static Color COLOR_HOVER = new Color(119,136,153);

	public static boolean fontInRange(int fontSize) {
		return (fontSize>=FONT_MIN && fontSize<=FONT_MAX) ? true : false;
	}

	public static String getImageResource(String imageName) {
		return System.getProperty("user.dir")+"\\resource\\"+imageName;
	}
	public static String getTemplateResource(String fileName) {
		return System.getProperty("user.dir")+"\\template\\"+fileName;
	}
}
