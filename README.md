## 欢迎使用 安卓开发助手 AndroidCodeHelper
 安卓开发助手，一键生成常用模板代码。  
 软件运行需要java环境，使用sun jre或open jre都可。  
 主界面截图  
![main_page](https://raw.githubusercontent.com/Yanye0xFF/PictureBed/master/images/androidcodehelper/main_page.png)  
##### 主要功能
* 正向生成java bean类，通过填写参数方式
* 反向编辑java bean类，在文本域粘贴已有代码
![bean](https://raw.githubusercontent.com/Yanye0xFF/PictureBed/master/images/androidcodehelper/bean.png)  
* 生成ListView Adapter适配器类
* 生成Recycler Adapter适配器类
Recycler Adapter类与ListView Adapter类生成过程相似
![listadapter](https://raw.githubusercontent.com/Yanye0xFF/PictureBed/master/images/androidcodehelper/listadapter.png)  
生成代码如下，(为节省篇幅删除格式化的空行)  

```java
import android.support.annotation.NonNull;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Person> dataset;
    private ItemClickListener listener = null;
    public PersonAdapter(List<Person> dataset) {
        this.dataset = dataset;
    }
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PersonViewHolder) {
            Person item = dataset.get(position);
            PersonViewHolder view = (PersonViewHolder)holder;

            if(listener != null && holder.itemView.getTag() == null){
                holder.itemView.setTag(200);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(holder.getAdapterPosition());
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount() {
        return dataset.size();
    }
    public Person getItem(int position) {
        return dataset.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    public interface ItemClickListener{
        void onItemClick(int position);
    }
    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }
    private static class PersonViewHolder extends ViewHolder {
        private TextView name;
        private ImageView head;
        private TextView info;
        private PersonViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            head = itemView.findViewById(R.id.iv_head);
            info = itemView.findViewById(R.id.tv_indo);
        }
    }
}
```
* 编辑资源id/组件名称生成findViewById
组件编辑框
![findviewbyid](https://raw.githubusercontent.com/Yanye0xFF/PictureBed/master/images/androidcodehelper/findviewbyid.png)  
生成代码如下  
```java
private TextView tvName;
private ImageView ivHead;
private EditText edMsg;
tvName = findViewById(R.id.tv_name);
ivHead = findViewById(R.id.iv_head);
edMsg = findViewById(R.id.ed_msg);
```
* 自动解析xml文件的资源id，生成组件声明
如下xml文件(\app\src\main\res\layout)经过parseXMLtoID  
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:focusable="true"
    android:clickable="true"
    android:background="?android:attr/selectableItemBackground">
    <TextView
        android:id="@+id/tv_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:layout_marginStart="10dp"
        android:textSize="18sp"/>
    <View
        android:layout_width="match_parent"
        android:layout_height="1px"
        android:background="@android:color/darker_gray"
        android:layout_alignParentBottom="true">
    </View>
</RelativeLayout>
```
解析结果，仅显示指定id的组件。  
```java
private TextView tvText;
tvText = findViewById(R.id.tv_text);
```