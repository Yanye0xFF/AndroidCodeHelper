package core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import bean.KeyValue;

public class XML2IdParser {
	
	private String rawData;
	private Stack<String> widgetStack;
	private List<KeyValue> resultSets;
	
	public XML2IdParser() {
		widgetStack = new Stack<>();
		resultSets = new ArrayList<>(32);
	}
	
	public XML2IdParser(String raw) {
		this.rawData = raw;
		widgetStack = new Stack<>();
		resultSets = new ArrayList<>(32);
	}
	
	public void setRawData(String raw) {
		this.rawData = raw;
	}
	public String getRawData() {
		return this.rawData;
	}
	
	public List<KeyValue> getResults() {
		return this.resultSets;
	}
	
	public boolean start() {
		//按行解析，组件类型以<开头，压入widgetStack，检测到id属性，弹出组件类型，根据id与名称规则生成代码
		BufferedReader reader = null;
		//检查输入数据情况
		if(rawData == null || rawData.isEmpty()) {
			return false;
		}
		//按行解析
		try {
			reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rawData.getBytes()), "utf-8"));
			int endPos = -1;
			while(true) {
				//读取一行加入行缓存
				String temp = reader.readLine();
				//读到末尾跳出
				if(temp == null) {
					break;
				}
				//跳过空行
				if(temp.isEmpty()) {
					temp = null;
					continue;
				}
				//跳过xml声明标签
				String line = temp.trim();
				temp = null;
				if(line.startsWith("<?xml")) {
					line = null;
					continue;
				}
				//弹出
				if(line.endsWith("/>") || (line.charAt(0) == '<' && line.charAt(1) == '/')) {
					widgetStack.pop();
					line = null;
					continue;
				}
				//取出id属性值
				if(line.startsWith("android:id")) {
					resultSets.add(new KeyValue(widgetStack.peek(),
							line.substring(line.indexOf("/") + 1, line.length()-1)));
					line = null;
					continue;
				}
				//节点开始，压栈
				if(line.charAt(0) == '<') {
					endPos = line.indexOf(' ');	
					widgetStack.push(line.substring(1, (endPos == -1 ? line.length() : endPos)));
				}
				endPos = -1;
				line = null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
