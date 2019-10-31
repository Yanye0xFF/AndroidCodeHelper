package thread;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import bean.KeyValue;

public class XML2IdThread extends Thread{
	
	private List<KeyValue> resultSets;
	private JTextArea codeArea;
	
	public XML2IdThread(List<KeyValue> resultSets, JTextArea codeArea) {
		this.resultSets = resultSets;
		this.codeArea = codeArea;
	}

	@Override
	public void run() {
		//合成数据
		StringBuilder builder = new StringBuilder(1024);
		List<String> names = new ArrayList<>(resultSets.size());
		
		int i = 0, j = 0, len = 0;
		int deleted = 0;
	
		for(KeyValue keyValue : resultSets) {
			char[] rawArray = keyValue.getValue().toCharArray();
			char[] resultArray = new char[rawArray.length];
			
			deleted = 0;
			len = rawArray.length;
			
			for(i = 0, j = 0; i < len; i++) {
				if(rawArray[i] == '_') {
					if(i + 1 < len) {
						rawArray[i + 1] -= 32;
						deleted++;
					}
					continue;
				}
				resultArray[j++] = rawArray[i];
			}
			
			names.add(new String(resultArray, 0, rawArray.length - deleted));
		}
		
		i = 0;
		for(KeyValue keyValue : resultSets) {
			builder.append("private " + keyValue.getKey() + " " + names.get(i++) + ";\n");
		}
		
		builder.append('\n');
		
		i = 0;
		for(KeyValue keyValue : resultSets) {
			builder.append(names.get(i++) + " = findViewById(R.id." + keyValue.getValue() + ");\n");
		}
		
		names.clear();
		names = null;
		
		SwingUtilities.invokeLater(new  Runnable(){
			public void run() {
				codeArea.setText("");
				codeArea.append(builder.toString());
				
				builder.delete(0, builder.length());
			}
		});
	}
	
}
