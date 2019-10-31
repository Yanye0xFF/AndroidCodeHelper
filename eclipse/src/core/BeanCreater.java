package core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import bean.Variable;
import utils.Tools;

public class BeanCreater {
	

	private String mName=null;
	private String mPackage=null;
	private List<Boolean> options=null;

	/**
	 * 构造参数
	 * @param pack 包名
	 * @param name 类名
	 * @param options 操作选项
	 * */	
	public BeanCreater(String pack,String name,List<Variable> item,List<Boolean> options) {
		this.mPackage=pack;
		this.mName=name;
		this.options=options;
	}
	
	/**
	 * 创建JavaBean 
	 * @param item结构  name(变量名),type(变量类型),
	 * @return 创建完成后的文本
	 * */
	public String creat(List<Variable> item) {
		StringBuilder container=null;
		int len=item.size();
		if(item!=null && len>0) {
			container=new StringBuilder(5120);
		}else {
			return null;
		}
		
		/**
		 * 按照JavaBean的线性结构添加信息，缩进统一使用四个空格。
		 * */
		
		//所属包声明
		if(Tools.strOK(mPackage)) {
			container.append("package "+mPackage+";\n");
			container.append("\n");
		}
		
		//基本类框架。
		if(options.size()>2 && options.get(0)) {
			container.append("import java.io.Serializable;\n");
			//引入包声明空行
			container.append("\n");
			container.append("public class "+mName+" implements Serializable { \n");
		}else {
			container.append("public class "+mName+" { \n");
		}
		//类声明完成空行
		container.append("\n");
		
		//私有成员变量
		for(int i=0;i<len;++i) {
			Variable mBean=item.get(i);
			container.append("    private "+mBean.getType()+" "+mBean.getName()+";\n");
			mBean=null;
		}
		//私有成员变量声明完成空行
		container.append("\n");
		
		//默认的传入参数为空的类构造函数
		container.append("    public "+mName+"() {\n");
		container.append("    }\n");
		//默认构造函数空行
		container.append("\n");
		
		//带参数的构造函数
		container.append("    public "+mName+"(");
		for(int i=0;i<len;++i) {
			Variable mBean=item.get(i);
			//除了首行每隔4个参数换一行避免代码过长
			if(i>0 && i%4==0) {
				container.append("\n            ");
			}
			container.append(mBean.getType()+" "+mBean.getName()+",");
			mBean=null;
		}
		//删除末尾多余的一个 逗号','
		container.deleteCharAt(container.length()-1);
		container.append(") {\n");
		//内部赋值
		for(int i=0;i<len;++i) {
			Variable mBean=item.get(i);
			container.append("        this."+mBean.getName()+" = "+mBean.getName()+";\n");
			mBean=null;
		}
		container.append("    }\n");
		//带参数构造函数空行
		container.append("\n");
		//getXXX,setXXX方法
		for(int i=0;i<len;++i) {
			
			//getXXX
			Variable mBean=item.get(i);
			container.append("    public "+mBean.getType()+" get"+toUpper(mBean.getName())+"() {\n");
			container.append("        return this."+mBean.getName()+";\n");
			container.append("    }\n");
			//setXXX
			container.append("    public void set"+toUpper(mBean.getName())+"("+mBean.getType()
			+" "+mBean.getName()+") {\n");
			container.append("        this."+mBean.getName()+" = "+mBean.getName()+";\n");
			container.append("    }\n");
			mBean=null;
			//每组方法换行区分
			container.append("\n");
		}
		
		if(options.size()>2 && options.get(2)) {
			//深拷贝
			container.append("    public void copyFrom("+mName+" item) {\n");
			for(int i=0;i<len;++i) {
				Variable mBean=item.get(i);
				container.append("        this."+mBean.getName()+" = item.get"+toUpper(mBean.getName())+"();\n");
				mBean=null;
			}
			container.append("    }\n");
			//深拷贝方法换行
			container.append("\n");
		}
		
		if(options.size()>2 && options.get(1)) {
			//重写toJSON方法
			container.append("    public String toJSONString() {\n");
			container.append("        return \"{");
			for(int i=0;i<len;++i) {
				Variable bean=item.get(i);
				container.append("\\\""+bean.getName()+"\\\":\\\"\"+"+bean.getName()+"+\"\\\",");
				
			}
			container.deleteCharAt(container.length()-1);
			container.append("}\";\n");
			
			
			container.append("    }\n");
			//toJSON方法换行
			container.append("\n");
			
		}
		
		
		//toString方法
		container.append("    public String toString() {\n");
		container.append("        return \""+mName+" [ \"+");
		for(int i=0;i<len;++i) {
			Variable mBean=item.get(i);
			container.append("\""+mBean.getName()+"=\"+this."+mBean.getName()+"+\",\"+");
			mBean=null;
		}
		//删除掉末尾多余的一段"+\",\"+"其长度为5 ，故删除区域为container长度-5开始 到container总长结束
		container.delete(container.length()-5,container.length());
		container.append("+\" ]\";\n");
		container.append("    }\n");
		//toString方法换行
		container.append("\n");
		
		container.append("}");
		return container.toString();
	}
	
	/**
	 * 获取当前的日期信息
	 * @param 空
	 * @return yyyy-MM-dd 格式的时间文本信息
	 * */
	public String getDate() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format1.format(new Date(System.currentTimeMillis()));
		return date1;
	}
	
	/**
	 * 大写传入文本的第一个字幕
	 * @param 英文源文本
	 * @return 第一个字母大写后的文本
	 * */
	private String toUpper(String text) {
		text = text.substring(0, 1).toUpperCase() + text.substring(1);
		return  text;
	}
}
