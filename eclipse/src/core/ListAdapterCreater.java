package core;

import java.util.List;
import bean.Variable;
import bean.ViewAndId;

public class ListAdapterCreater {
	private String mAdapterName,mViewHolderName,mLayoutName;
	private Variable datasets = new Variable();
	public ListAdapterCreater(String adapterName,String viewHolderName,String layoutName) {
		this.mAdapterName=adapterName;
		this.mViewHolderName=viewHolderName;
		this.mLayoutName=layoutName;
	}
	
	public String creat(List<Variable> varItem,List<ViewAndId> viewItem) {
		
		StringBuilder container=null;
		
		int lenVar=varItem.size();
		int lenView=viewItem.size();
		
		if(varItem==null || lenVar<=0 || viewItem==null || lenView<=0) {
			return null;
		}else {
			container=new StringBuilder(5120);
		}
		
		//处理数据提取出varItem含有首有List的变量
		//放到datasets
		for(int i=0;i<lenVar;++i) {
			Variable var=varItem.get(i);
			if(var.getType().startsWith("List")) {
				datasets=var;
				break;
			}
			var=null;
		}
		
		//导入必要的包
		container.append("import android.content.Context;\n");
		container.append("import android.view.LayoutInflater;\n");
		container.append("import android.view.View;\n");
		container.append("import android.view.ViewGroup;\n");
		container.append("import android.widget.BaseAdapter;\n");
		container.append("import android.widget.TextView;\n");
		container.append("import android.widget.ImageView;\n");
		container.append("import java.util.List;\n\n");
		
		//适配器主框架
		container.append("public class "+mAdapterName+" extends BaseAdapter {\n");
	
		//类私有变量声明
		for(int i=0;i<lenVar;++i) {
			Variable var=varItem.get(i);
			container.append("    private "+var.getType()+" "+var.getName()+" = null;\n");
			var=null;
		}
		container.append("    private LayoutInflater mInflater = null;\n");
		
		//变量声明空行
		container.append("\n");
		
		//构造函数
		container.append("    public "+mAdapterName+"(");
		for(int i=0;i<lenVar;++i) {
			Variable var=varItem.get(i);
			container.append(var.getType()+" "+var.getName()+",");
			var=null;
		}
		//删除末尾多余的一个 逗号","
		container.deleteCharAt(container.length()-1);
		container.append(") {\n");
		//内部赋值
		for(int i=0;i<lenVar;++i) {
			Variable var=varItem.get(i);
			container.append("            this."+var.getName()+" = "+var.getName()+";\n");
			if(var.getType().equals("Context")) {
				container.append("          this.mInflater = LayoutInflater.from(context);\n");
			}
			var=null;
		}
		container.append("    }\n");
		//构造函数空行
		container.append("\n");
		
		//getItemCount
		container.append("    @Override\n    public int getCount() {\n");
		container.append("        return "+datasets.getName()+".size();\n");
		container.append("    }\n");
		//getCount空行
		container.append("\n");
		
		//getItemId
		container.append("    @Override\n    public long getItemId(int arg0) {\n");
		container.append("        return arg0;\n");
		container.append("    }\n");
		//getItemId空行
		container.append("\n");
		
		container.append("    @Override\n");
		//getItem
		String tmp = datasets.getType();
		tmp=tmp.substring(tmp.indexOf("<")+1, tmp.indexOf(">"));
		container.append("    public "+tmp+" getItem(int position) {\n");
		container.append("        return "+datasets.getName()+".get(position);\n");
		container.append("    }\n");
		//getItem空行
		container.append("\n");
		
		container.append("    @Override\n    public View getView(int i, View view, ViewGroup viewGroup) {\n");
		container.append("        final "+mViewHolderName+" viewHolder;\n");
		container.append("        "+tmp+" item = "+datasets.getName()+".get(i);\n");
		container.append("        if(view == null){\n");
		container.append("            view = mInflater.inflate("+mLayoutName+",null);\n");
		container.append("            viewHolder = new ViewHolder();\n");
		for(int i=0;i<lenView;++i) {
			ViewAndId view=viewItem.get(i);
			container.append("            viewHolder."+view.getName()+" = ("+view.getType()+") view.findViewById("+view.getResId()+");\n");
			view=null;
		}
		container.append("            view.setTag(viewHolder);\n");
		container.append("        }else{\n");
		container.append("            viewHolder = (ViewHolder) view.getTag();\n");
		container.append("        }\n");
		container.append("        //据填充区域\n");
		container.append("        return view;\n");
		container.append("    }\n");
		//getView空行
		container.append("\n");
		
		//ViewHolder内部类
		container.append("    class "+mViewHolderName+" {\n");
		for(int i=0;i<lenView;++i) {
			ViewAndId view=viewItem.get(i);
			container.append("        "+view.getType()+" "+view.getName()+";\n");
			view=null;
		}
		container.append("    }\n");
		
		container.append("}\n");
		return container.toString();
	}
}
