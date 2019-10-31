package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import bean.SettingItem;

public class ConfigUtils {
	private static final String FILE_PATH="config/settings.properties";
	
	public ConfigUtils() {
	}
	
	public SettingItem readConfig() {
		//属性集合对象
		Properties prop = null; 
		FileInputStream fis=null;
		SettingItem item = new SettingItem();;
		try {
			prop = new Properties();
			fis = new FileInputStream(FILE_PATH);
			prop.load(fis);
			fis.close();
			fis=null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//读取功能展开状态
		String expand=prop.getProperty("expand_all", "true");   
		if(expand!=null && expand.equals("true")) {
			item.setExpandAll(true);
		}else {
			item.setExpandAll(false);
		}
		expand=null;
		//读取编码信息
		String encode=prop.getProperty("encode", "utf-8");
		item.setEncodeType(encode);
		encode=null;
		prop=null;
		return item;
	}
	
	public void updateConfig(SettingItem item) {
		Properties prop = new Properties();
		FileOutputStream fos=null;
		if(item.getExpandAll()) {
			prop.setProperty("expand_all", "true");   
		}else {
			prop.setProperty("expand_all", "false");   
		}
		prop.setProperty("encode", item.getEncodeType());   
		try {
			fos = new FileOutputStream(FILE_PATH);
			prop.store(fos, "Android Helper Config File");   
			fos.close();// 关闭流   
			fos=null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
