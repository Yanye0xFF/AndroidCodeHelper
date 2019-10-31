package core;

import java.util.List;

import bean.Variable;
import bean.ViewAndId;

public class RecycleAdapterCreater {
	
	private String mAdapterName,mViewHolderName,mLayoutName;
	private boolean mAddListener = false;
	private Variable datasets = new Variable();
	
	public RecycleAdapterCreater () {
	}
	
	public RecycleAdapterCreater (String adapterName,String viewHolderName,String layoutName) {
		this.mAdapterName = adapterName;
		this.mViewHolderName = viewHolderName;
		this.mLayoutName = layoutName;
	}
	
	public RecycleAdapterCreater (String adapterName,String viewHolderName,String layoutName,boolean addListener) {
		this.mAdapterName = adapterName;
		this.mViewHolderName = viewHolderName;
		this.mLayoutName = layoutName;
		this.mAddListener = addListener;
	}
	
	public String creat(List<Variable> varItem,List<ViewAndId> viewItem) {
		
		StringBuilder container = null;
		int lenVar = varItem.size();
		int lenView = viewItem.size();
		
		if(varItem == null || lenVar <= 0 || viewItem == null || lenView <= 0) {
			return null;
		}else {
			container = new StringBuilder(5120);
		}
		
		//处理数据提取出varItem含有首有List的变量
		//放到datasets
		for(int i = 0; i < lenVar; ++i) {
			Variable var = varItem.get(i);
			if(var.getType().startsWith("List")) {
				datasets = var;
				break;
			}
			var = null;
		}

		//导入必要的包
		container.append("import android.support.annotation.NonNull;\n");
		container.append("import android.content.Context;\n");
		container.append("import android.support.v7.widget.RecyclerView;\n");
		container.append("import android.support.v7.widget.RecyclerView.ViewHolder;\n");
		container.append("import android.view.LayoutInflater;\n");
		container.append("import android.view.View;\n");
		container.append("import android.view.ViewGroup;\n");
		container.append("import android.widget.TextView;\n");
		container.append("import android.widget.ImageView;\n");
		container.append("import java.util.List;\n");
		//导入资源类R.java注释
		container.append("\n");
		//适配器主框架
		container.append("public class " + mAdapterName + " extends RecyclerView.Adapter<RecyclerView.ViewHolder> {\n");
		container.append("\n");
		//类私有变量声明
		for(int i = 0; i < lenVar; ++i) {
			Variable var = varItem.get(i);
			container.append("    private " + var.getType() + " " + var.getName() + ";\n");
			var = null;
		}
		//项目点击监听
		if(mAddListener) {
			container.append("    private ItemClickListener listener = null;\n");
		}
		//变量声明空行
		container.append("\n");
		
		//构造函数
		container.append("    public " + mAdapterName + "(");
		for(int i = 0; i < lenVar; ++i) {
			Variable var = varItem.get(i);
			container.append(var.getType() + " " + var.getName() + ", ");
			var = null;
		}
		//删除末尾多余的一个 逗号","
		container.delete(container.length()-2, container.length());
		container.append(") {\n");
		//内部赋值
		for(int i = 0; i < lenVar; ++i) {
			Variable var = varItem.get(i);
			container.append("        this." + var.getName() + " = " + var.getName() + ";\n");
			var = null;
		}
		container.append("    }\n");
		//构造函数空行
		container.append("\n");
		
		//onCreateViewHolder
		container.append("    @Override\n");
		container.append("    @NonNull\n");
		container.append("    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {\n");
		container.append("        View view = LayoutInflater.from(parent.getContext()).inflate(" + mLayoutName + ", parent, false);\n");
		container.append("        return new " + mViewHolderName + "(view);\n");
		container.append("    }\n");
		//onCreateViewHolder换行
		container.append("\n");
		
		//onBindViewHolder
		container.append("    @Override\n");
		container.append("    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {\n");
		
		String tmp = datasets.getType();
		tmp = tmp.substring(tmp.indexOf("<")+1, tmp.indexOf(">"));
		
		container.append("        if (holder instanceof " + mViewHolderName + ") {\n");
		container.append("            " + tmp + " item = " + datasets.getName() + ".get(position);\n");
		container.append("            " + mViewHolderName + " view = (" + mViewHolderName + ")holder;\n");
		container.append("\n");
		
		if(mAddListener) {
			container.append("            if(listener != null && holder.itemView.getTag() == null){\n");
			container.append("                holder.itemView.setTag(200);\n");
			container.append("                holder.itemView.setOnClickListener(new View.OnClickListener() {\n");
			container.append("                    @Override\n");
			container.append("                    public void onClick(View view) {\n");
			container.append("                        listener.onItemClick(holder.getAdapterPosition());\n");
			container.append("                    }\n");
			container.append("                });\n");
			container.append("            }\n");
		}
		
		container.append("        }\n");
		
		container.append("    }\n");
		//onBindViewHolder换行
		container.append("\n");
		
		//getItemCount
		container.append("    @Override\n    public int getItemCount() {\n");
		container.append("        return " + datasets.getName() + ".size();\n");
		container.append("    }\n");
		//getItemCount换行
		container.append("\n");
		
		//getItem
		container.append("    public " + tmp + " getItem(int position) {\n");
		container.append("        return " + datasets.getName() + ".get(position);\n");
		container.append("    }\n");
		container.append("\n");
		
		//getViewType
		container.append("    @Override\n");
		container.append("    public int getItemViewType(int position) {\n");
		container.append("        return super.getItemViewType(position);\n" + "    }\n");
		container.append("\n");
		tmp = null;
		
		if(mAddListener) {
			//interface
			container.append("    public interface ItemClickListener{\n");
			container.append("        void onItemClick(int position);\n");
			container.append("    }\n");
			container.append("\n");
			//setListener
			container.append("    public void setItemClickListener(ItemClickListener listener) {\n");
			container.append("        this.listener = listener;\n");
			container.append("    }\n");
			container.append("\n");
		}
		
		//ViewHolder
		container.append("    private static class " + mViewHolderName + " extends ViewHolder {\n");
		for(int i = 0; i < lenView; ++i) {
			ViewAndId view = viewItem.get(i);
			container.append("        private " + view.getType() + " "+view.getName()+";\n");
			view = null;
		}
		container.append("        private " + mViewHolderName + "(View itemView) {\n");
		container.append("            super(itemView);\n");
		for(int i = 0; i < lenView; ++i) {
			ViewAndId view = viewItem.get(i);
			container.append("            " + view.getName() + " = itemView.findViewById(" + view.getResId() + ");\n");
			view = null;
		}
		container.append("        }\n");
		container.append("    }\n");
		container.append("\n");
		
		container.append("}\n");
		return container.toString();
	}
}
