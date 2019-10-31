package core;

import java.util.List;
import bean.ViewAndId;

public class FindViewCreater {
	
	private boolean varAnnounce=false;
	private String viewParent=null;
	
	public FindViewCreater() {
		
	}
	
	public FindViewCreater(String viewParent,boolean announce) {
		this.viewParent = viewParent;
		this.varAnnounce = announce;
	}
	
	public FindViewCreater(boolean announce) {
		this.varAnnounce=announce;
	}
	
	/**
	 * 创建Android findViewById代码
	 * @param 元素结构  name(控件名称),type(控件类型),resId(控件资源ID)
	 * @return findViewById处理后的文本
	 * */
	public String creat(List<ViewAndId> item) {
		StringBuilder container=null;
		int len=item.size();
		if(len>0) {
			container=new StringBuilder(1024);
		}else {
			return null;
		}
		if(varAnnounce) {
			container.append(creatAnnounce(item,len));
		}
		
		for(int i=0;i<len;++i) {
			ViewAndId mView=item.get(i);
			if(viewParent != null) {
				container.append(mView.getName()+" = " + viewParent + ".findViewById("+mView.getResId()+");\n");
			}else {
				container.append(mView.getName()+" = "+"findViewById("+mView.getResId()+");\n");
			}
			mView=null;
		}
		return container.toString();
	}
	
	private String creatAnnounce(List<ViewAndId> item,int len) {
		StringBuilder container=null;
		if(len>0) {
			container=new StringBuilder(512);
		}else {
			return null;
		}
		for(int i=0;i<len;++i) {
			ViewAndId mView=item.get(i);
			container.append("private "+mView.getType()+" "+mView.getName()+";\n");
			mView=null;
		}
		container.append("\n");
		return container.toString();
	}
	
	
}
